package com.aura.dao.impl.Mysql;


import com.aura.dao.interface_point.Mon_One;
import com.aura.dao.interface_point.Mon_pointDao;
import com.aura.domain.Mysql_point.Mon_point;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;


import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class Mon_Test implements Mon_pointDao,Mon_One {
    //创建连接池
    public static DataSource ds = null;
    public static String url;
    public static String username;
    public static String password;
    static{
        //try {
        //1.获取DBCP数据源实现类对象
        BasicDataSource dbs = new BasicDataSource();
       /* //2.设置连接数据库需要的配置信息
        Properties properties = new Properties();
        //String parent = dMode.name().toLowerCase();
        String path = "dev/dbcp-config.properties";
        InputStream in = ConfigurationManager.class.getClassLoader().getResourceAsStream(path);

            properties.load(in);

        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");

            ds = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        dbs.setDriverClassName("com.mysql.jdbc.Driver");
        //dbs.setUrl("jdbc:mysql://172.16.72.250:3306/platformcloud?useSSL=true");//
        dbs.setUrl("jdbc:mysql://172.16.22.236:3306/MysqlTest");
        dbs.setUsername("root");
        //dbs.setPassword("root");
        dbs.setPassword("Ha!@#$1234");
        dbs.setInitialSize(10);
        dbs.setMaxActive(50);
        ds=dbs;
    }
    private QueryRunner qr = new QueryRunner(ds);

    String selectAll = "select * from mon_point";

    private String selectSQL = "SELECT * FROM mon_point WHERE point_code=? ";//data_unit,type_code,eq_code,upper_limit,lower_limit

    //单条插入
    @Override
    public void insert(Mon_point entity) {

    }
    //批量插入
    @Override
    public void insertBatch(List<Mon_point> list) {

    }



    ///迭代查询
    @Override
    public List<Mon_point> querty() {
        //String sql = "SELECT * FROM  mon_point";
        try {
            List<Mon_point> stu = qr.query(selectAll, new BeanListHandler<Mon_point>(Mon_point.class));
            return stu;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //单条查询
    public Mon_point query0ne(String str) {
        try {
            return   qr.query(selectSQL,new BeanHandler<Mon_point>(Mon_point.class), str);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
