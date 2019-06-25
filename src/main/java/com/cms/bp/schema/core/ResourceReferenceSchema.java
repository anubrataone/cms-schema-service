package com.cms.bp.schema.core;

import com.cms.bp.validator.SchemaValidatorResult;

public class ResourceReferenceSchema extends BpSchema {
    public ResourceReferenceSchema(String schemaFileName) {
        super(schemaFileName);
    }

    @Override
    public SchemaValidatorResult validate(String jsonContent) {
        return new SchemaValidatorResult(SchemaValidatorResult.FIELD_INVALID);
    }
}
