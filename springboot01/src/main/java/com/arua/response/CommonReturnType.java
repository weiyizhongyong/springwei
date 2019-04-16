package com.arua.response;

public class CommonReturnType {
    //表名对应请求的放回处理结果“sucess”huo“fall
    private String status;
    private Object data;
    //定义一个通用的创建方法

    public static CommonReturnType create(Object object){
        //CommonReturnType ty = new CommonReturnType();
        return CommonReturnType.create(object,"success");
    }
    public static CommonReturnType create(Object result,String success){
        CommonReturnType tys = new CommonReturnType();
        tys.setStatus(success);
        tys.setData(result);
        return tys;




    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
