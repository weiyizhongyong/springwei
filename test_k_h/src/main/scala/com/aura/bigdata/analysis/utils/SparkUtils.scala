package com.aura.bigdata.analysis.utils


import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

object SparkUtils {


  /*  def setMaster(conf:SparkConf, dMode:DeployMode) {
        if(dMode.equals(DeployMode.DEV)) {
            conf.setMaster("local[4]")
        }
    }
    def getSQLContext(sc: SparkContext, dMode: DeployMode, isHive:Boolean):SQLContext = {
        if(dMode.equals(DeployMode.DEV)) {
            if(isHive) {
                new HiveContext(sc)
            } else {
                new SQLContext(sc)
            }
        } else {
            new HiveContext(sc)
        }
    }*/
}
