package com.arua.error;

public enum EmBusinessError implements CommonError {
    //30000 开头为交易信息错误定义
    STOCK_NOT_EXIST(30001,"库存不足"),
    //10001 开头为用户信息相关错误定义
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"用户手机号密码不正确"),
    USER_NOT_LOGIN(20003,"用户不存在"),
    //通用错误类型
    PARAMETER_UALIDATION_ERROR(10001,"参数不合法"),
    //

    UNKNOWN_ERROR(10002,"未知异常")
    ;
    private   EmBusinessError(int errCode,String errMsg){
        this.errCode= errCode;
        this.errMsg = errMsg;

    }
    private  int errCode;
    private  String errMsg;
    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
