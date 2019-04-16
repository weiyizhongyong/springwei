package com.aura.dao.impl.Mysql;

import com.aura.conf.ConfigurationManager;
import com.aura.dao.timeStamp.PointTimestamp;
import com.aura.domain.Mysql_point.PointValueStamp;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;



import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PointTimestampImp implements PointTimestamp {
    //设置连接池
    private static DataSource ds = null;
    private static String url = null;
    private static String username = null;
    private static String passwrod = null;
    static {
        BasicDataSource dbs = new BasicDataSource();
        dbs.setDriverClassName("com.jdbc.mysql");
        Properties properties = new Properties();
        String path = "dev/dbcp-config.properties";
        InputStream in = ConfigurationManager.class.getClassLoader().getResourceAsStream(path);
        try {
            properties.load(in);
            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");

                ds = BasicDataSourceFactory.createDataSource(properties);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private QueryRunner qr = new QueryRunner(ds);
    String selectSQL = "SELECT click_count FROM PointTimestamp WHERE `date_long`=? AND monitor_value=? AND point_code=?";
    String sql = "INSERT INTO PointTimestamp VALUES(?, ?, ?)";

    @Override
    public void insert(PointValueStamp entity) {

    }

    @Override
    public void insertBatch(List<PointValueStamp> list) {
        List<PointValueStamp> insertList = new ArrayList<>();
        List<PointValueStamp> updatetList = new ArrayList<>();
        try {
        for(PointValueStamp entity :list){

               /*Integer clickCount = qr.query(sql,new ScalarHandler<Integer>(),entity.getDate_long(),entity.getMonitor_value(),entity.getPoint_code());

               if(clickCount == null){

                }*//*else {
                    updatetList.add(entity);
                }*/
            insertList.add(entity);
        }

            if(!insertList.isEmpty()){
                Object[][] insertParams = new Object[insertList.size()][];
                for (int i = 0; i < insertList.size(); i++) {
                    PointValueStamp entity = insertList.get(i);
                    Object[] obj = {entity.getDate_long(),entity.getMonitor_value(),entity.getPoint_code()};
                    insertParams[i] = obj;
                }
                qr.batch(sql, insertParams);

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public List<PointValueStamp> querty() {
        return null;
    }
}
