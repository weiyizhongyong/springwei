package com.aura.domain.Mysql_point;

public class PointValueStamp {
    private String point_code;
    private double monitor_value;
    private long date_long;

    public String getPoint_code() {
        return point_code;
    }

    public void setPoint_code(String point_code) {
        this.point_code = point_code;
    }

    public double getMonitor_value() {
        return monitor_value;
    }

    public void setMonitor_value(double monitor_value) {
        this.monitor_value = monitor_value;
    }

    public long getDate_long() {
        return date_long;
    }

    public void setDate_long(long date_long) {
        this.date_long = date_long;
    }
}
