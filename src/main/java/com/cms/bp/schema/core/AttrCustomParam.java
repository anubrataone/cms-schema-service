package com.cms.bp.schema.core;

import com.cms.bp.validator.SchemaValidatorResult;

import java.io.IOException;

public class AttrCustomParam extends CompoundSchema {
     public AttrCustomParam(String jsonSchema) throws IOException {
        super(jsonSchema);
    }

    @Override
    public SchemaValidatorResult validate(String jsonContent) {
        return super.validate(this.getSchemaJsonNode().get(ELEMENTS_NODE_NAME).get("data"), jsonContent);
    }
}
