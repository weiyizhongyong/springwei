package com.aura.test_influxDB;

import java.util.HashMap;
import java.util.Map;

public class Test02_map {


    public static void main(String[] args) {
        Map<String, String> tags = new HashMap();
        Map<String, Object> fields = new HashMap();
        tags.put("point_code", "point_code");
        fields.put("point_name", 32.32);
        fields.put("id", "id");


    }
}