package com.aura.bigdata.analysis.jobs.warn


import com.esotericsoftware.kryo.Kryo
import org.apache.spark.rdd.RDD
import org.apache.spark.serializer.KryoRegistrator
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random


/*
case class Info1(name: String ,age: Int,gender: String,addr: String)
class MyRegisterKryo extends KryoRegistrator {
  override def registerClasses(kryo: Kryo): Unit = {
    kryo.register(classOf[Info1])
  }
}
object KyroTest {
  def main(args: Array[String]) {
    //org.apache.spark.serializer.KryoRegistrator
    val start = System.currentTimeMillis()
    val conf = new SparkConf().setMaster("local[2]").setAppName("KyroTest")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoRegistrator")
    conf.set("spark.kryo.registrator", "com.aura.bigdata.analysis.jobs.warn.MyRegisterKryo")
    conf.set("spark.kryo.registrationRequired", "true")
    //conf.registerKryoClasses(Array(classOf[Info1], classOf[scala.collection.mutable.WrappedArray.ofRef[_]]))
    conf.registerKryoClasses(
      Array(classOf[scala.collection.mutable.WrappedArray.ofRef[_]],
        classOf[Info1]))
    val sc = new SparkContext(conf)

    val arr = new ArrayBuffer[Info1]()
    val str = new ArrayBuffer[String]()

    val nameArr = Array[String]("lsw","yyy","lss")
    val genderArr = Array[String]("male","female")
    val addressArr = Array[String]("beijing","shanghai","shengzhen","wenzhou","hangzhou")

    for(i <- 1 to 1000000){
      val name = nameArr(Random.nextInt(3))
      val age = Random.nextInt(100)
      val gender = genderArr(Random.nextInt(2))
      val address = addressArr(Random.nextInt(5))

      arr.+=(Info1(name,age,gender,address))
      str.+=(name+" "+age+" "+address)
    }

    val rdd: RDD[String] = sc.parallelize(str)
    val rdd0: RDD[Info1] = sc.parallelize(arr)


    //序列化的方式将rdd存到内存
    /*rdd.persist(StorageLevel.MEMORY_ONLY_SER)
    println(rdd.count())
    println(System.currentTimeMillis() - start)*/
    rdd0.persist(StorageLevel.MEMORY_ONLY_SER)
    rdd0.foreach(x=>x.age)
    println(System.currentTimeMillis() - start)
  }
}*/

case class Info(name: String ,age: Int,gender: String,addr: String)

object KyroTest {
  def main(args: Array[String]) {

    val conf = new SparkConf().setMaster("local[2]").setAppName("KyroTest")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    conf.set("spark.kryo.registrationRequired", "true")
    conf.registerKryoClasses(Array(classOf[Info], classOf[scala.collection.mutable.WrappedArray.ofRef[_]]))
    val sc = new SparkContext(conf)

    val arr = new ArrayBuffer[Info]()

    val nameArr = Array[String]("lsw","yyy","lss")
    val genderArr = Array[String]("male","female")
    val addressArr = Array[String]("beijing","shanghai","shengzhen","wenzhou","hangzhou")

    for(i <- 1 to 1000000){
      val name = nameArr(Random.nextInt(3))
      val age = Random.nextInt(100)
      val gender = genderArr(Random.nextInt(2))
      val address = addressArr(Random.nextInt(5))
      arr.+=(Info(name,age,gender,address))
    }

    val rdd = sc.parallelize(arr)

    //序列化的方式将rdd存到内存
    rdd.persist(StorageLevel.MEMORY_ONLY_SER)
    rdd.count()
  }
}
