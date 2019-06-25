package com.cms.bp.schema.core;


import com.cms.bp.validator.SchemaValidatorResult;

public class AttributeCompoundSchema extends BpSchema {
    private CompoundSchema compoundSchema;

    public AttributeCompoundSchema(String schemaFileName) {
        super(schemaFileName);
        compoundSchema = new CompoundSchema(this.getSchemaFileName());

    }

    @Override
    public SchemaValidatorResult validate(String jsonContent) {
        return this.compoundSchema.validate(this.getSchemaJsonNode().get("elements"), jsonContent);
    }
}
