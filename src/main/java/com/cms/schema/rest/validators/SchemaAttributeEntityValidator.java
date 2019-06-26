package com.cms.schema.rest.validators;

import com.cms.schema.rest.dto.SchemaAttributeDto;

public class SchemaAttributeEntityValidator {
    public static ValidatorMsg isValid(SchemaAttributeDto entity) {
        ValidatorMsg ret = new ValidatorMsg();
        if (entity == null || entity.getAttrName() == null ||
                entity.getAttrClass() == null) {
            ret.setMsg("Invalid:Entity is null or missing attrName/attrType/attrClass");
            return ret;
        }

        ret.setValid(true);
        return ret;
    }

}
