package com.cms.bp.schema.core;

import com.cms.bp.validator.SchemaValidatorResult;

public class SingleLineText extends BpSchema {
    public SingleLineText(String schemaFileName) {
        super(schemaFileName);
    }

    @Override
    public SchemaValidatorResult validate(String jsonContent) {
        return new SchemaValidatorResult(SchemaValidatorResult.SUCCESS);
    }
}

