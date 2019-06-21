package com.cms.bp.schema.core;

public class SingleLineDouble extends BpSchema {
    @Override
    public boolean validate(String jsonContent) {
        try {
            Double.valueOf(jsonContent);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
