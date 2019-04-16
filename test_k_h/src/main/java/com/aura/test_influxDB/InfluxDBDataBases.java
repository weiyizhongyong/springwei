package com.aura.test_influxDB;

import com.aura.influxdb.converter.PointCollectionConverter;
import com.aura.influxdb.converter.PointConverter;
import okhttp3.OkHttpClient;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InfluxDBDataBases  {
    private static String openurl = "http://172.16.22.244:8086";
    private static String username = "admin";
    private static String password = "admin";
    private static String database = "bonc";
    private  String measurement = "dfgx" ;
    private InfluxDB influxDB;
    private OkHttpClient client = null;
    private PointCollectionConverter<Point> converter = new PointConverter();;


    public InfluxDBDataBases(String username, String password, String openurl, String database) {
        this.username = username;
        this.password = password;
        this.openurl = openurl;
        this.database = database;
    }

    public static InfluxDBDataBases setUp(){
        //创建 连接
        InfluxDBDataBases influxDbUtil = new InfluxDBDataBases(username, password, openurl, database);


        influxDbUtil.influxDbBuild();

        return influxDbUtil;
    }
    /**连接时序数据库；获得InfluxDB**/
    public InfluxDB influxDbBuild(){
        client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5,TimeUnit.MINUTES)
                .build();
        if(influxDB == null){
            influxDB = InfluxDBFactory.connect(openurl, username, password, client.newBuilder());

           // influxDB = InfluxDBImpl(openurl, username, password,new OkHttpClient.Builder().connectTimeout(500, TimeUnit.MILLISECONDS).readTimeout(500,TimeUnit.MILLISECONDS).build())

            influxDB.createDatabase(database);
        }
        return influxDB;
    }
    /**
     * 设置数据保存策略
     * defalut 策略名 /database 数据库名/ 30d 数据保存时限30天/ 1 副本个数为1/ 结尾DEFAULT 表示 设为默认的策略
     */
    public void createRetentionPolicy(){
        String command = String.format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s DEFAULT",
                "defalut", database, "30d", 1);
        this.query(command);
    }
    /**
     * 查询
     * @param command 查询语句
     * @return
     */
    public QueryResult query(String command){
        return influxDB.query(new Query(command, database));
    }
    /**
     * 插入
     * @param tags 标签
     * @param fields 字段
     */
    public void insert(Map<String, String> tags, Map<String, Object> fields){
        Builder builder = Point.measurement(getMeasurement());
        builder.tag(tags);
        builder.fields(fields);

        influxDB.write(database, "", builder.build());
    }

    public void write(List<Point> payload) {
        final String database = getDatabase();
        final String retentionPolicy = "autogen";//存储策略
        final BatchPoints ops = BatchPoints.database(database)
                .retentionPolicy(retentionPolicy)
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();
        payload.forEach(t -> converter.convert(t).forEach(ops::point));
        influxDB.write(ops);
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenurl() {
        return openurl;
    }

    public void setOpenurl(String openurl) {
        this.openurl = openurl;
    }
    public String getDatabase(){
        return database;
    }

    public  String getMeasurement() {
        return measurement;
    }

    public  void setMeasurement(String measurement) {
       this.measurement = measurement;
    }

    public void setDatabase(String database) {
        this.database = database;

    }

    public static void main(String[] args) {
        InfluxDBDataBases influxDBDataBases = InfluxDBDataBases.setUp();
        //设置influxdb 表名

        influxDBDataBases.setMeasurement("dfgx");

        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, Object> sendmap = new HashMap<>();
        map.put("point_code", "afew");
        //标签字段
        sendmap.put("point_name", "afew");
        sendmap.put("date_time", "afew");
        //sendmap.put("times", str2._4)
        sendmap.put("monitor_value", Double.parseDouble("32.4")); //str2._3.toDouble  parse[Double](s)
        sendmap.put("type_code", "afew");
        sendmap.put("eq_code", "afew");
        sendmap.put("eq_name", "afew");
        sendmap.put("data_unit", "afsew");
        sendmap.put("warn_type", "afsew");
        sendmap.put("procedure_code", "afsew");
        sendmap.put("procedure_name", "afsew");
        //单条插入
        influxDBDataBases.insert(map, sendmap);
    }

}
