package com.cms.bp.schema.core;

import com.cms.bp.validator.SchemaValidatorResult;

public class SingleLineDouble extends BpSchema {

    public SingleLineDouble(String schemaFileName) {
        super(schemaFileName);
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
