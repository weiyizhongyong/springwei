package com.aura.domain.Mysql_point;
import java.io.Serializable;


public class Mon_point implements Serializable{

    private String point_code;
    private String point_name;
    private String point_describe;
    private String type_code;
    private String eq_code;
    private String eq_name;
    private String upper_limit;
    private String lower_limit;
    private String data_unit;
    private String location;
    private String product_line;
    private String product_process;
    private String standard_value;
    private String ds_code;
    private String procedure_code;
    private String procedure_name;

    public String getProcedure_code() {
        return procedure_code;
    }

    public void setProcedure_code(String procedure_code) {
        this.procedure_code = procedure_code;
    }

    public String getProcedure_name() {
        return procedure_name;
    }

    public void setProcedure_name(String procedure_name) {
        this.procedure_name = procedure_name;
    }

    public void setPoint_describe(String point_describe) {
        this.point_describe = point_describe;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setProduct_line(String product_line) {
        this.product_line = product_line;
    }

    public void setProduct_process(String product_process) {
        this.product_process = product_process;
    }

    public void setStandard_value(String standard_value) {
        this.standard_value = standard_value;
    }

    public void setDs_code(String ds_code) {
        this.ds_code = ds_code;
    }

    public String getPoint_describe() {
        return point_describe;
    }

    public String getLocation() {
        return location;
    }

    public String getProduct_line() {
        return product_line;
    }

    public String getProduct_process() {
        return product_process;
    }

    public String getStandard_value() {
        return standard_value;
    }

    public String getDs_code() {
        return ds_code;
    }

    public void setData_unit(String data_type) {
        this.data_unit = data_type;
    }

    public String getData_unit() {
        return data_unit;
    }

    public void setPoint_code(String point_code) {
        this.point_code = point_code;
    }

    public String getPoint_code() {
        return point_code;
    }


    public void setPoint_name(String point_name) {
        this.point_name = point_name;
    }

    public void setType_code(String type_code) {
        this.type_code = type_code;
    }

    public void setEq_code(String eq_code) {
        this.eq_code = eq_code;
    }

    public void setEq_name(String eq_name) {
        this.eq_name = eq_name;
    }

    public void setUpper_limit(String upper_limit) {
        this.upper_limit = upper_limit;
    }

    public void setLower_limit(String lower_limit) {
        this.lower_limit = lower_limit;
    }


    public String getPoint_name() {
        return point_name;
    }

    public String getType_code() {
        return type_code;
    }

    public String getEq_code() {
        return eq_code;
    }

    public String getEq_name() {
        return eq_name;
    }

    public String getUpper_limit() {
        return upper_limit;
    }

    public String getLower_limit() {
        return lower_limit;
    }



    @Override
    public String toString() {
        return "Mon_point{" +
                "point_code='" + point_code + '\'' +
                ", point_name='" + point_name + '\'' +
                ", point_describe='" + point_describe + '\'' +
                ", type_code='" + type_code + '\'' +
                ", eq_code='" + eq_code + '\'' +
                ", eq_name='" + eq_name + '\'' +
                ", upper_limit='" + upper_limit + '\'' +
                ", lower_limit='" + lower_limit + '\'' +
                ", data_unit='" + data_unit + '\'' +
                ", location='" + location + '\'' +
                ", product_line='" + product_line + '\'' +
                ", product_process='" + product_process + '\'' +
                ", standard_value='" + standard_value + '\'' +
                ", ds_code='" + ds_code + '\'' +
                ", procedure_code='" + procedure_code + '\'' +
                ", procedure_name='" + procedure_name + '\'' +
                '}';
    }
}

