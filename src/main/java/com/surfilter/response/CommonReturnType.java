package com.surfilter.response;

public class CommonReturnType {
    /**
     * 表明对应请求的返回处理结果 "success" or "fail"
     */
    private String status;


    private Integer code;
    /**
     * 若status=success,则data内返回前端需要的json数据
     * 若status=fail,则data内使用通用的错误码格式
     */
    private Object data;

    private Integer count;

    /**
     * 定义一个通用的创建方法
     */
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }

    public static CommonReturnType create(Object result, String status){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }
    public static CommonReturnType create(Object result, Integer status, Integer count){
        CommonReturnType type = new CommonReturnType();
        type.setCode(status);
        type.setData(result);
        type.setCount(count);
        type.setStatus("success");
        return type;
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
