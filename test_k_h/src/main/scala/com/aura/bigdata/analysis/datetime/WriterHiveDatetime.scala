package com.aura.bigdata.analysis.datetime

import java.lang._
import java.text.SimpleDateFormat
import java.util.Date

import com.aura.domain.Mysql_point.Mon_point
import com.aura.util.{JSONUtils, JedisUtil}
import kafka.serializer.StringDecoder
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}



/*case class Info12(point_code:String,datetime:String,monitor_value:String)//,zhi:String
class MyRegisterKryo extends KryoRegistrator {
  override def registerClasses(kryo: Kryo): Unit = {
    kryo.register(classOf[Info12])
  }
}*/
/**
  * 连接ioServer
  */
object WriterHiveTimestamp {



  def main(args: Array[String]): Unit = {



    if(args == null || args.length < 4) {
      println(
        """缺少参数
        """.stripMargin)
      System.exit(-1)
    }
    val checkpiont = args(1)
    //  1028_10_22 "/aa/2018_10_25"

    //checkpoint 高可用
    //def functionToCreateSSC():StreamingContext={

    val conf = new SparkConf().setAppName("test_mon")//.set("spark.kryo.registrator", "com.aura.bigdata.analysis.jobs.timestamp.MyRegisterKryo")
    conf.setMaster("local[2]")//mysqlKafka/PiliangTest/IOServer
    //conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sc = new SparkContext(conf)
    val hivespark= new HiveContext(sc)

    val ssc = new StreamingContext(sc,Seconds(args(0).toInt))
      ssc.checkpoint(checkpiont)
    //kafka--topic
    val topics = Set(args(2))
    //influxdb 表名
    val tableming = args(3)
    // hbase和hive表名
    /*val ai_point_id_dt=args(4)*/

    //第一步：关联kafka，接受数据
    val kafkaDSKyro = createStream(ssc, topics)
    //kafkaDSKyro.print()
    //条件过滤
    val kafkaFilter = filterPoint(kafkaDSKyro)//redis 进行判断
    val row= sqlcount(kafkaFilter)
    //kafkaFilter.print()
    //println(kafkaDSKyro)
    row.print()





    //第四步：实时和离线数据处理

    hive_influxdb(row, hivespark, tableming)//hivespark,
    //将数据存入hive分区表中




      //ssc
  //}
    //val ssc = StreamingContext.getOrCreate(checkpiont, functionToCreateSSC _)
    //val ssc = StreamingContext.getOrCreate(checkpiont, functionToCreateSSC _)
    ssc.start()
    ssc.awaitTermination()

  }

  /**
    * 关联kafka接受数据
    * @param ssc StreamingContext 对象
    * @param topics kafka中topic
    * @return 返回从kafka接受的测点编码、时间戳、数值
    */
  def createStream(ssc:StreamingContext,topics:Set[String]) = {
    //kafka 端口设置
    val kafkaParams = Map(
      "metadata.broker.list" -> "172.16.22.236:9092,172.16.22.242:9092,172.16.22.244:9092",
      "auto.offset.reset" -> "largest"
    )
    val kafkaDStream: DStream[String] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics).map(_._2)
    //进行Kryo序列化
    val kafkaDSKyro  = kafkaDStream.map(_.split(" ")).map(xx=>(xx(0),xx(1),xx(2)))
    //kafkaDStream.map(_(1)).persist(StorageLevel.MEMORY_AND_DISK)//数据持久化
    kafkaDSKyro
  }

  /**
    * 对从kafka接受的测点进行判断过滤
    * @param kafkaDSKyro  kafka接受数据
    * @return kafka过滤的数据
    */
  def filterPoint(kafkaDSKyro:DStream[(String, String, String)]) = {
    val value: DStream[(String, String, String)] = kafkaDSKyro.filter(x => {
      val byKey: String = JedisUtil.getByKey("monitor:MonPointServiceImpl:monPoint" + x._1)
      byKey != null
    })
    value
  }

  /**
    * 将kafka接受的测点关联redis元数据，对测点数值进行判断，是否异常，并添加测点详细信息
    * @param kafkaDSKyro kafka过滤后的数据
    * @return (point_code,point_name, datetime,hours, monitor_value,  type_code, eq_code,eq_name, data_type, warn_type,procedure_code,procedure_name)
    *         依次为：测点编码、测点名称、时间戳、时间、分钟、数值、状态类型、设备编码、设备名称、单位、监控类型、工序编码、工序名称
    */
  def sqlcount(kafkaDSKyro:DStream[(String, String, String)]) = kafkaDSKyro.map(rdd => {
  //val my_test = new Mon_Test
  val point_code =rdd._1

  val byKey: String = JedisUtil.getByKey("monitor:MonPointServiceImpl:monPoint" + point_code)
  val test_mysql: Mon_point = JSONUtils.jsonToBean(byKey, new Mon_point).asInstanceOf[Mon_point]
  val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")//yyyy-MM-dd_HH-mm-ss
  var str_date = new Date(rdd._2.toLong)
    val dates: String = format.format(str_date)
  val strings: Array[String] = dates.split(" ")
  val datetime = strings(0).replace("-","")
    val time_long: Long = rdd._2.toLong / 1000
  val monitor_value =  rdd._3

  //val id = rdd._4
  //val test_mysql= my_test.query0ne(point_code)
  val upper = test_mysql.getUpper_limit.toDouble
  val lower = test_mysql.getLower_limit.toDouble
  val type_code = test_mysql.getType_code
  val eq_code = test_mysql.getEq_code
  val data_type = test_mysql.getData_unit
  val point_name = test_mysql.getPoint_name
  val eq_name = test_mysql.getEq_name
  val procedure_code = test_mysql.getProcedure_code
  val procedure_name = test_mysql.getProcedure_name
  var warn_type:Int = 0
  //monitor 警戒线做判断
  if (monitor_value.toDouble <= upper && monitor_value.toDouble >= lower) {
    //正常范围内 设置为0
    warn_type = 0

  } else if(monitor_value.toDouble > upper){
    //超过上限 设置为1
    warn_type = 1
  } else {
    //低于下限 设置为-1
    warn_type = -1
  }
    /*var warn =2
    if(warn==2){
       warn = warn_type
      warn_code = UUID.randomUUID.toString
    }else if(warn!=warn_type){
      warn = warn_type
      warn_code = UUID.randomUUID.toString
    }*/
  //返回数据集（元组）
  (point_code,point_name, datetime,time_long, monitor_value,  type_code, eq_code,eq_name, data_type, warn_type,procedure_code,procedure_name)//,id
   })

  /**
    * 将数据实时写入infludb，离线数据写入hbase
    * @param row 数据
    * @param tableming influxdb 表名
    */
  def hive_influxdb(row:DStream[(String, String, String,Long, String,String, String, String,String,  Int,String,String)] ,hivespark:HiveContext,tableming:String) ={//,ai_point_id_dt:String

     row.foreachRDD(rdd => {
       //RDD做没空判断
      if (!rdd.isEmpty()) {
        val schema = StructType(

          List(
            StructField("point_code", StringType, true),
            StructField("point_name", StringType, true),
            StructField("datetime", StringType, true),
            StructField("time_long", LongType, true),
            StructField("monitor_value", DoubleType, true),
            StructField("type_code", StringType, true),
            StructField("eq_code", StringType, true),
            StructField("eq_name", StringType, true),
            StructField("data_type", StringType, true),
            StructField("warn_type", IntegerType, true),
            StructField("procedure_code", StringType, true),
            StructField("procedure_name", StringType, true)
          )
        )
        val sql: RDD[Row] = rdd.map(
          str => {

            Row(str._1, str._2,  str._3, str._4, str._5.toDouble, str._6, str._7, str._8, str._9, str._10, str._11,str._12)//
          }
        )

        val df: DataFrame = hivespark.createDataFrame(sql, schema)
        df.registerTempTable("mon_qdg_point_id_dt")

        hivespark.sql("use test")
        hivespark.sql("set hive.exec.dynamic.partition=true")
        hivespark.sql("set hive.exec.dynamic.partition.mode=nonstrict")
        hivespark.sql("insert into table point_datetime partition(datetime) " +
                      "select point_code, point_name,time_long,monitor_value,type_code ,eq_code,eq_name,data_type," +
                      "warn_type,procedure_code,procedure_name,datetime from mon_qdg_point_id_dt")
        /*rdd.foreachPartition(str => {
          //RDD做没空判断
          if (!str.isEmpty) {
            //设置InfluxDBDataBases初始化对象
            val influxDBProperties = InfluxDBDataBases.setUp()
            //设置influxdb 表名
            influxDBProperties.setMeasurement(tableming)

            val map = new util.HashMap[String, String]
            val sendmap = new util.HashMap[String, Object]
            val list = new util.ArrayList[Point]();
            //设置hbase初始化配置文件
            /*            val conn: Connection = ConnectionFactory.createConnection(HBaseConfiguration.create)
                        val list_hbase = new util.ArrayList[Put]()*/
            /*           if(!conn.getAdmin.tableExists(TableName.valueOf(ai_point_id_dt))){
                         //初始化HBaseAdmin
                         val admin: HBaseAdmin = new HBaseAdmin(HBaseConfiguration.create())
                         //创建表名
                         val table:HTableDescriptor =  new HTableDescriptor(TableName.valueOf(ai_point_id_dt))
                         //创建列蔟Mvn clean package -DskipTests
                         table.addFamily(new HColumnDescriptor("cf"))
                         admin.createTable(table)}
                       val table: Table = conn.getTable(TableName.valueOf(ai_point_id_dt))*/
            str.foreach(str2 => {
              /*         //str2._3 为"yyyy-MM-dd_HH:mm:ss-SSS" 格式
                       /*val time_format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss-SSS")
                       val date_long:Long= time_format.parse(str2._3).getTime*/

                       //hbase——rowkey
                      val put = new Put(Bytes.toBytes(str2._1.toString+"_"+str2._3.toString+"_"+str2._4))
                       //hbase--cf
                       //put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("id"), Bytes.toBytes(str2._10.toString))
                       put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("point_code"), Bytes.toBytes(str2._1.toString))
                       put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("point_name"), Bytes.toBytes(str2._2.toString))
                       put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("datetime"), Bytes.toBytes(str2._3.toString))
                       put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("hours"), Bytes.toBytes(str2._4.toString))
                       put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("monitor_value"), Bytes.toBytes(str2._5.toString))
                       put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("type_code"), Bytes.toBytes(str2._6.toString))
                       put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("eq_code"), Bytes.toBytes(str2._7.toString))
                       put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("eq_name"), Bytes.toBytes(str2._8.toString))
                       put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("data_type"), Bytes.toBytes(str2._9.toString))
                       put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("warn_type"), Bytes.toBytes(str2._10.toString))//procedure_code,procedure_name
                       put.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("procedure_code"),Bytes.toBytes(str2._11.toString))
                       put.addColumn(Bytes.toBytes("cf"),Bytes.toBytes("procedure_name"),Bytes.toBytes(str2._12.toString))
                       list_hbase.add(put)*/
              //向hbase插入数据，若出错则写入数据关闭

              //插入influxdb
              //指定标签
              //(point_code,point_name, datetime,rdd._2, monitor_value,  type_code, eq_code,eq_name, data_type, warn_type,procedure_code,procedure_name)
              map.put("point_code", str2._1)
              //标签字段
              sendmap.put("point_name", str2._2)
              sendmap.put("date_time", str2._4)
              //sendmap.put("times", str2._4)
              sendmap.put("monitor_value", Double.valueOf(str2._5)) //str2._3.toDouble  parse[Double](s)
              sendmap.put("type_code", str2._6)
              sendmap.put("eq_code", str2._7)
              sendmap.put("eq_name", str2._8)
              sendmap.put("data_unit", str2._9)
              sendmap.put("warn_type", str2._10.toString)
              sendmap.put("procedure_code", str2._11)
              sendmap.put("procedure_name", str2._12)
              //单条插入
              //influxDBProperties.insert(map, sendmap)
              //批量插入
              val builder = Point.measurement(influxDBProperties.getMeasurement)
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag(map);
              builder.fields(sendmap);
              val point: Point = builder.build()
              list.add(point);
            })
            //批量插入
            influxDBProperties.write(list)
            //hbase连接关闭
            /*Try(table.put(list_hbase)).getOrElse(table.close())
            table.close()
            conn.close()*/

          }
        })*/
      }



     })
  }

}



