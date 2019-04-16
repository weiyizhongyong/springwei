package com.aura.bigdata.analysis.jobs.hiveWriter

import com.aura.util.JedisUtil
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.hive.HiveContext

object Th_test03 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Th_test01").setMaster("local")
    val sc = new SparkContext(conf)
    val hiveContext = new HiveContext(sc)
    val starttime = args(0)
    val endtime = args(1)
    val point_code = args(2)

   // val point_code = "5503,5504,5505,5506"
    val num = point_code.split(",").length
    val allPoint = point_code.split(",")
    var r = 0
    var points = "point_code="
    while (r < num){
      if(r==0){
        points = points+allPoint(r)
      }else if(r==num-1){
        points = points +" or point_code = "+allPoint(r) + " and "
      }else{
        points = points +" or point_code = "+allPoint(r)
      }
      r=r+1
    }

    val sessionid = args(3)
    hiveContext.sql("use test_point")
    val dftoRdd = hiveContext.sql("select point_name,time_long/1000,monitor_value,type_code,data_type,warn_type,point_code from procedure_eq_point_timestamp where "+ points +" time_long between '" + starttime + "' and '" + endtime + "'")//
//  select * from procedure_eq_point_timestamp where point_code=5004 or point_code=5005 and time_long between '1551062810996' and '1551063230932'
    val jsonRdd : RDD[String] = dftoRdd.toJSON

      //jsonRdd.foreach(x=>println(x))
    val keyjsonRdd = jsonRdd.map(f=>(sessionid,f))

    val bykeyRdd:RDD[(String, Iterable[String])] = keyjsonRdd.groupByKey()
//      : RDD[mutable.Map[String, Object]]
    val allMapRdd = bykeyRdd.foreach(f=>{
      //var jsonMap:Map[String,Object] = Map(
      val iter = f._2.iterator
      var alljson = ""
      while (iter.hasNext){
        var str = iter.next().toString
        if(alljson.length<3){
          alljson = str
        }
        alljson = alljson+","+str
      }
      alljson="{\"point\":["+alljson+"]}"
      JedisUtil.set("platformcloud:history:"+sessionid, alljson,28800)
    })
    sc.stop()
  }
}



//     将map转化成json
//    val colors:Map[String,Object]  = Map("red" -> "123456")
//    val json = JSONObject(colors)
//    println(json)

//    转化成array
//    val arrayRdd = dftoRdd.map(f => {
//      (f.get(0),f.get(1),f.get(2),f.get(3),f.get(4),f.get(5),f.get(6),f.get(7),f.get(8),f.get(9),f.get(10),f.get(11))
//    }).foreach(f=>println(f))