package com.cms.bp.schema.core;

public class SingleLineNumber extends BpSchema {
    @Override
    public boolean validate(String jsonContent) {
        try {
            Integer.valueOf(jsonContent);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
