package com.cms.bp.schema.core;

public class SingleLineBoolean extends BpSchema {
    @Override
    public boolean validate(String jsonContent) {
        try {
            return Boolean.valueOf(jsonContent);
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
