package com.anand.company.companyms.response;

import com.anand.company.companyms.Company;

public class CompanyResponse {

    private Object data;
    private  String msg;

    public CompanyResponse() {
    }

    public CompanyResponse(Object data, String msg) {
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
