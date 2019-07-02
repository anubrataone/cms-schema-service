package com.cms.bp.schema.core;

import com.cms.bp.validator.SchemaValidatorResult;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class SingleLineNumber extends BpSchema {

    public SingleLineNumber(String schemaJsonStr) throws IOException {
        super(schemaJsonStr);
    }

    @Override
    public SchemaValidatorResult validate(String jsonContent) {
        try {
            if (!StringUtils.isEmpty(jsonContent)) {

                Integer.valueOf(jsonContent.replace("\"", ""));
            } else {
                throw new NumberFormatException(jsonContent + " is null/empty");
            }
        } catch (NumberFormatException e) {
            return new SchemaValidatorResult(SchemaValidatorResult.FIELD_INVALID, new StringBuilder(jsonContent)
                    .append(": is not a number").toString());
        }
        return new SchemaValidatorResult(SchemaValidatorResult.SUCCESS);
    }
}
