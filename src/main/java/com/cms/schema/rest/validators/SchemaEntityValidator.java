package com.cms.schema.rest.validators;

import com.cms.schema.rest.dto.SchemaDto;

public class SchemaEntityValidator {
    public static ValidatorMsg isValid(SchemaDto entity) {
        ValidatorMsg ret = new ValidatorMsg();
        if (entity == null || entity.getBpName() == null || entity.getStatus() == null ||
                entity.getUrn() == null) {
            ret.setMsg("Invalid:Entity is null or missing bpName/bpVersion/status");
            return ret;
        }

        ret.setValid(true);
        return ret;
    }

}
