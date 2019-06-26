package com.cms.bp.schema.core;

import com.cms.bp.validator.SchemaValidatorResult;

import java.io.IOException;

public class AttrCustomParam extends CompoundSchema {
    private CompoundSchema compoundSchema;

    public AttrCustomParam(String schemaFileName) throws IOException {
        super(schemaFileName);
        //compoundSchema = new CompoundSchema(this.getSchemaFileName());

    }

    @Override
    public SchemaValidatorResult validate(String jsonContent) {
        return super.validate(this.getSchemaJsonNode().get("elements").get("data"), jsonContent);
        //return this.compoundSchema.validate(this.getSchemaJsonNode().get("elements").get("data"), jsonContent);
    }
}
