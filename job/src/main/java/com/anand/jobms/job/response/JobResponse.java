package com.anand.jobms.job.response;

public class JobResponse {

    private Object data;
    private  String msg;

    public JobResponse() {
    }

    public JobResponse(Object data, String msg) {
        this.data = data;
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
