package com.aura;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aura.dao.impl.Mysql.PointTimestampImp;
import com.aura.dao.interface_point.StuInsert;

import com.aura.dao.impl.Mysql.Mon_Test;
import com.aura.domain.Mysql_point.Mon_point;
import com.aura.domain.Mysql_point.PointValueStamp;
import com.aura.util.JSONUtils;
import com.aura.util.JedisUtil;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class Test {

StuInsert dao;

    Mon_Test mm =  new Mon_Test();
    PointTimestampImp imp = new PointTimestampImp();
    List<PointValueStamp> pvs = new ArrayList<>();


    String aa="5001";
    int ndd =43;
    @Before
    public void setUp() {
        PointValueStamp a1 = new PointValueStamp();
        a1.setDate_long(1);
        a1.setMonitor_value(23.2);
        a1.setPoint_code("5001");
        PointValueStamp a2 = new PointValueStamp();
        a2.setDate_long(2);
        a2.setMonitor_value(23.3);
        a2.setPoint_code("5002");
        PointValueStamp a3 = new PointValueStamp();
        a3.setDate_long(3);
        a3.setMonitor_value(23.4);
        a3.setPoint_code("5003");
        pvs.add(a1);
        pvs.add(a2);
        pvs.add(a3);




    }
    //@org.junit.Test
    public void test01(){
        imp.insertBatch(pvs);

    }


    @org.junit.Test
    public void ss(){

        /*Mon_point n= mm.query0ne(aa);
        System.out.println(n.getPoint_name());*/

        List<Mon_point> querty = mm.querty();


        try{
            for (Mon_point monPorint : querty) {
                //System.out.println(monPorint);

                JedisUtil.set("monitor:MonPointServiceImpl:monPoint" + monPorint.getPoint_code(), JSON.toJSONString(monPorint));
            }
            String byKey = JedisUtil.getByKey("monitor:MonPointServiceImpl:"+"5001");//monPoint
            System.out.println(byKey);
            //Mon_point o = (Mon_point)JSONUtils.jsonToBean(byKey, new Mon_point());
           // Mon_point ddd = JedisUtil.get("monitor:MonPointServiceImpl:monPoint" , Mon_point.class);
            //if (byKey == null) System.out.println(ddd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
