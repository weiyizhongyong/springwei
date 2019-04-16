package com.aura.mon_point;

import com.aura.dao.impl.Mysql.Mon_Test;
import com.aura.domain.Mysql_point.Mon_point;

public class Test_point {



    public static void main(String[] args) {
        Mon_Test mm =  new Mon_Test();
        String aa="8477900010";
        int ndd =43;
        Mon_point n= mm.query0ne(aa);
        System.out.println(n.getPoint_name());
    }
 /*   public void ss(){

        Mon_point n= mm.query0ne(aa);
        System.out.println(n.getPoint_name());
    }*/
}
