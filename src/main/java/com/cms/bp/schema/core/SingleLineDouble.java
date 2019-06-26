package com.cms.bp.schema.core;

import com.cms.bp.validator.SchemaValidatorResult;

import java.io.IOException;

public class SingleLineDouble extends BpSchema {

    public SingleLineDouble(String schemaJsonStr) throws IOException {
        super(schemaJsonStr);
    }

    @Override
    public SchemaValidatorResult validate(String jsonContent) {
        try {
            Double.valueOf(jsonContent);
            new SchemaValidatorResult(SchemaValidatorResult.SUCCESS);
        } catch (NumberFormatException e) {
            return new SchemaValidatorResult(SchemaValidatorResult.FIELD_INVALID);
        }
        return new SchemaValidatorResult(SchemaValidatorResult.FIELD_INVALID);
    }

}
