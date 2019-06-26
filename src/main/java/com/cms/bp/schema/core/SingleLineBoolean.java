package com.cms.bp.schema.core;

import com.cms.bp.validator.SchemaValidatorResult;

import java.io.IOException;

public class SingleLineBoolean extends BpSchema {
    public SingleLineBoolean(String schemaJsonStr) throws IOException {
        super(schemaJsonStr);
    }


    @Override
    public SchemaValidatorResult validate(String jsonContent) {
        try {
            Boolean.valueOf(jsonContent);
            new SchemaValidatorResult(SchemaValidatorResult.SUCCESS);
        } catch (NumberFormatException e) {
            return new SchemaValidatorResult(SchemaValidatorResult.FIELD_INVALID);
        }
        return new SchemaValidatorResult(SchemaValidatorResult.FIELD_INVALID);
    }

}
