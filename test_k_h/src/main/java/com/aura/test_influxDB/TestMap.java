package com.aura.test_influxDB;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TestMap implements Serializable{
    HttpDeal httpDeal = new HttpDeal();
    Map<String,String> map = new HashMap<String,String>();
    Map<String,String> sendmap = new HashMap<String,String>();

    String m1 = "A";

    public  void show() {

        while(true) {
            map.put("sensor_code", m1);
            map.put("eq_code", m1);
            map.put("CODE", m1);
            map.put("code", m1);
            map.put("data_unit", m1);
            map.put("monitor_value", m1);
            map.put("ts", m1);
            map.put("warn_type", m1);
            //sendmap.put("message",JSONObject.toJSONString(map));
            //httpDeal.post("http://172.16.72.229:8080/influxDBCtl/remoteAddData", map);
            sendmap.put("map", JSONObject.toJSONString(map));
            System.out.println(sendmap);

            try {
                httpDeal.post("http://172.16.72.229:8080/influxDBCtl/remoteAddData",sendmap);
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("---------");
        }
    }

    public static void main(String[] args) {
        TestMap ff = new TestMap();
        ff.show();
    }
}
