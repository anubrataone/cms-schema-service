package com.cms.bp.validator;

import java.io.Serializable;

public class SchemaValidatorResult implements Serializable {
    public static int SUCCESS = 200;
    public static int FIELD_NOT_FOUND = 404;
    public static int FIELD_INVALID = 400;

    private StringBuilder msg = new StringBuilder();
    private int code = SUCCESS;

    public SchemaValidatorResult(int code) {
        this.code = code;
    }

    public SchemaValidatorResult(int code, String msg) {
        this.msg.append(msg);
        this.code = code;
    }

    public StringBuilder getMsg() {
        return msg;
    }

    public void setMsg(StringBuilder msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "SchemaValidatorResult{" +
                "msg=" + msg +
                ", code=" + code +
                '}';
    }
}
