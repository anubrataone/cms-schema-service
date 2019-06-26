package com.cms.schema.rest.validators;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ValidatorMsg implements Serializable {
    private String msg;

    @JsonProperty("is_valid")
    private boolean isValid;

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ValidatorMsg{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
