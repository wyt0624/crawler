package com.surfilter.error;

public enum EmBusinessError implements CommonError {
    //通用错误类型00001
    PARAMETER_VALIDATION_ERROR( 10001, "参数不合法" ),
    UNKNOWN_ERROR( 10002, "未知错误" ),

    //10000开头为用户信息相关错误定义
    USER_NOT_EXIST( 200001, "用户不存在" ),
    USER_LOGIN_FAIL( 200002, "用户名或密码不正确" ),
    USER_NOT_LOGIN( 200003, "用户还未登陆" ),

    ADD_PRODUCT_ERROR( 300001, "添加失败" ),
    SEARCH_PRODUCT_ERROR( 300002, "查询失败" ),
    UPDATE_PRODUCT_ERROR( 300003, "修改失败" ),
    DELETE_PRODUCT_ERROR( 300004, "删除失败" ),
    ;

    private EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrCode() {
        return errCode;
    }

    @Override
    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
