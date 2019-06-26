package com.cms.bp.schema.core;

import com.cms.bp.validator.SchemaValidatorResult;

import java.io.File;
import java.io.IOException;

public class SingleLineNumber extends BpSchema {

    public SingleLineNumber(File schemaJsonStr) {
        super(schemaJsonStr);
    }

    public SingleLineNumber(String schemaJsonStr) throws IOException {
        super(schemaJsonStr);
    }

    @Override
    public SchemaValidatorResult validate(String jsonContent) {
        try {
            Integer.valueOf(jsonContent);
        } catch (NumberFormatException e) {
            return new SchemaValidatorResult(SchemaValidatorResult.FIELD_INVALID);
        }
        return new SchemaValidatorResult(SchemaValidatorResult.SUCCESS);
    }
}
