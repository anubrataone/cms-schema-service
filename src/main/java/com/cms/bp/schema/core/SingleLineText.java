package com.cms.bp.schema.core;

import com.cms.bp.validator.SchemaValidatorResult;

import java.io.IOException;

public class SingleLineText extends BpSchema {
    public SingleLineText(String schemaJsonStr) throws IOException {
        super(schemaJsonStr);
    }

    @Override
    public SchemaValidatorResult validate(String jsonContent) {
        return new SchemaValidatorResult(SchemaValidatorResult.SUCCESS);
    }
}

