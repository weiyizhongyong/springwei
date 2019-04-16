package com.aura.properties;

import org.springframework.beans.factory.annotation.Value;

public class InfluxDBProperties {
    @Value("${spring.influx.url}") //这是读取properties文件需要的注解
    private String url;
    @Value("${spring.influx.username}")
    private String username;
    @Value("${spring.influx.password}")
    private String password;
    @Value("${spring.influx.database}")
    private String database;
    @Value("${spring.influx.tableName}")
    private String tableName;
    @Value("${spring.influx.retentionPolicy}")
    private String retentionPolicy;
    @Value("${spring.influx.connectTimeout}")
    private int connectTimeout = 10;
    @Value("${spring.influx.readTimeout}")
    private int readTimeout = 30;
    @Value("${spring.influx.writeTimeout}")
    private int writeTimeout = 10;
    @Value("${spring.influx.gzip}")
    private boolean gzip = false;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRetentionPolicy() {
        return retentionPolicy;
    }

    public void setRetentionPolicy(String retentionPolicy) {
        this.retentionPolicy = retentionPolicy;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public boolean isGzip() {
        return gzip;
    }

    public void setGzip(boolean gzip) {
        this.gzip = gzip;
    }

}
