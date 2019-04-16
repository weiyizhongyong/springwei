package com.aura.spark;

import org.apache.spark.deploy.SparkSubmit;

public class SparkTest01 {
    public static void main(String[] args) {
        String [] argse = new String[]{
                "--master","spark://172.16.22.242:7077",
                "--deploy-mode","client",
                "--name","testHive",
                "--conf","spark.locality.wait=2",
                "--class","mysqlKafka.kyro.HiveTest02",
                "--executor-memory","1G",
                "--total-executor-cores", "2",
                "file:///data04/hadoop/ai/pathTest01.jar",//hdfs://node01:9000/
                "hdfs://bonc2018/user/hive/warehouse/aihive01.db/ai_point_id_dt04/point_code=8477900060/*/*"//

        };
        SparkSubmit.main(argse);

    }
}
