package com.aura.bigdata.analysis.accumulators

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

object Hive01 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("hive01")//.setMaster("local[2]")

    val context = new SparkContext(conf)
    val sql = new HiveContext(context)

    sql.sql("use test")
    /*sql.sql("set hive.exec.dynamic.partition=true")
    sql.sql("set hive.exec.dynamic.partition.mode=nonstrict")*/
    sql.sql("insert into table point_datetime01 " +
      "select  point_name,time_long,monitor_value,type_code ,eq_code,eq_name,data_type," +
      "warn_type,procedure_code,procedure_name from point_datetime where datetime ='20190411' and point_code="+args(0))
   /* sql.sql("create table procedure_eq_point_datetime02 ("+
                  "point_code String comment '测点编码'," +
                  "point_name String comment '测点名称',"+
                  "dates string comment 'yyyy-MM-dd hh:mm:ss',"+
      "monitor_value double comment '监测值',"+
      "type_code String comment '监测类型',"+
      "eq_code String comment '设备编码',"+
      "eq_name String comment '设备名称',"+
      " data_type String comment '数据单位',"+
      " warn_type int comment '设备状态',"+
      " procedure_code String comment '工序编码',"+
      " procedure_name String comment '工序名称',"+
      "datetime string comment '日期'"+
      ")  row format delimited fields terminated by ','")*/

    context.stop()
  }

}
