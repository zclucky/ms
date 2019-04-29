package com.ms.response;

public class CommomRetrunType {

    // 状态 success fail
    private String status;

    // 数据
    private Object data;

    // 返回通用的数据格式
    public static CommomRetrunType create(Object object){
        return CommomRetrunType.create(object, "success");
    }

    public static CommomRetrunType create(Object object, String status){
        CommomRetrunType type = new CommomRetrunType();
        type.setStatus(status);
        type.setData(object);
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
}
