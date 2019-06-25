package com.cms.bp.schema.core;

import com.cms.bp.validator.SchemaValidatorResult;

public class SingleLineNumber extends BpSchema {
    public SingleLineNumber(String schemaFileName) {
        super(schemaFileName);
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
