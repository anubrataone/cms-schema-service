package com.cms.schema.rest.validators;

import com.cms.schema.service.model.BPNames;
import com.cms.schema.service.model.CMSEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class CMSEntityValidator {
    public static ValidatorMsg isValid(CMSEntity entity) {
        ValidatorMsg ret = new ValidatorMsg();
        if (entity == null || StringUtils.isEmpty(entity.getEntityType()) ||
                StringUtils.isEmpty(entity.getExternalId())) {
            ret.setMsg("Invalid:Entity is null or missing entity_type/external_id");
            return ret;
        }
        try {
            BPNames.valueOf(entity.getBpName());
        } catch (Exception ex) {
            ret.setMsg("Provided BPNames is not supported - please use: " + Arrays.asList(BPNames.values()));
            return ret;
        }
        ret.setValid(true);
        return ret;
    }

//    public static void main(String args[]){
//        System.out.println(BPNames.valueOf("VODTitle1"));
//    }
}
