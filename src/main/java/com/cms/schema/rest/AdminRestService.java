package com.cms.schema.rest;


import com.cms.schema.application.mongo.MongoDBEntityService;
import com.cms.schema.rest.response.ResponseData;
import com.cms.schema.rest.response.ResponseHeader;
import com.cms.schema.rest.validators.CMSEntityValidator;
import com.cms.schema.rest.validators.ValidatorMsg;
import com.cms.schema.service.model.CMSEntity;
import com.cms.schema.service.model.ServiceResponseMsg;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("schema")
@Api(value = "schema")
public class AdminRestService {

    @Autowired
    MongoDBEntityService mongoDBEntityService;

    public AdminRestService() {
        // needed for autowiring
    }

    private static ResponseData createResponse(String message) {
        ResponseData<CMSEntity> responseData = new ResponseData();
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setSource("cms-schema-service");
        responseHeader.setSystemTime(System.currentTimeMillis());

        responseHeader.setMessage(message);

        responseHeader.setStart(0);
        responseData.setHeader(responseHeader);

        return responseData;
    }

    private boolean validateInputParams(MultivaluedMap<String, String> requestInputs) {
        if (requestInputs == null || requestInputs.isEmpty()) {
            throw new IllegalArgumentException("Missing required parameter(s):");
        }

        //To Do - validate
        return true;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseData<CMSEntity> createEntity(@RequestBody CMSEntity entity) {

        //Step 1: Validate input request  via required parameters

        //Step 2: Validate input header
        ValidatorMsg validatorMsg = CMSEntityValidator.isValid(entity);
        if (!validatorMsg.isValid()) {
            ResponseData<CMSEntity> responseData = createResponse("Failed: " + validatorMsg.getMsg());
            responseData.setData(entity);
            return responseData;
        }

        //Step 3: add record
        try {
            entity.setId(null);
            entity = mongoDBEntityService.upsert(entity);
            ResponseData<CMSEntity> responseData = createResponse("Success");
            responseData.setData(entity);

            return responseData;
        } catch (Exception e) {

            ResponseData<CMSEntity> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return responseData;
        }
    }

    @RequestMapping(value = "batchinsert", method = RequestMethod.POST)
    public ResponseData<List<CMSEntity>> createBatchEntity(@RequestBody List<CMSEntity> entities) {

        //Step 1: Validate input request  via required parameters

        //Step 2: Validate input header
        if (entities == null || entities.size() == 0) {
            ResponseData<List<CMSEntity>> responseData = createResponse("Entity is null/empty");
            responseData.setData(entities);
            return responseData;
        }

        List<CMSEntity> addedEntities = new ArrayList<>();
        //Step 3: add record
        try {
            entities.parallelStream().forEach(entity -> {
                ResponseData<CMSEntity> singleEntityRespData = createEntity(entity);
                addedEntities.add(singleEntityRespData.getData());
            });

            ResponseData<List<CMSEntity>> responseData = createResponse("Success");
            responseData.setData(addedEntities);

            return responseData;
        } catch (Exception e) {

            ResponseData<List<CMSEntity>> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return responseData;
        }
    }

    @RequestMapping(value = "batchupdate", method = RequestMethod.PUT)
    public ResponseData<List<CMSEntity>> updateBatchEntity(@RequestBody List<CMSEntity> entities) {

        //Step 1: Validate input request  via required parameters

        //Step 2: Validate input header
        if (entities == null || entities.size() == 0) {
            ResponseData<List<CMSEntity>> responseData = createResponse("Entity is null/empty");
            responseData.setData(entities);
            return responseData;
        }

        List<CMSEntity> addedEntities = new ArrayList<>();
        //Step 3: add record
        try {
            entities.parallelStream().forEach(entity -> {
                ResponseData<CMSEntity> singleEntityRespData = updateEntity(entity.getId(), entity);
                addedEntities.add(singleEntityRespData.getData());
            });

            ResponseData<List<CMSEntity>> responseData = createResponse("Success");
            responseData.setData(addedEntities);

            return responseData;
        } catch (Exception e) {

            ResponseData<List<CMSEntity>> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return responseData;
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseData<CMSEntity> updateEntity(@PathVariable("id") String id, @RequestBody CMSEntity entity) {

        //Step 1: Validate input request  via required parameters

        ValidatorMsg validatorMsg = CMSEntityValidator.isValid(entity);
        if (!validatorMsg.isValid()) {
            ResponseData<CMSEntity> responseData = createResponse("Failed: " + validatorMsg.getMsg());
            responseData.setData(entity);
            return responseData;
        } else if (StringUtils.isEmpty(id)) {
            ResponseData<CMSEntity> responseData = createResponse("Failed: Id must not be empty or null for updating");
            responseData.setData(entity);
            return responseData;
        }

        //Step 2: Validate input header

        //Step 3: add record
        try {
            entity.setId(id);
            entity = mongoDBEntityService.upsert(entity);
            ResponseData<CMSEntity> responseData = createResponse("Success");
            responseData.setData(entity);

            return responseData;
        } catch (Exception e) {

            ResponseData<CMSEntity> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return responseData;
        }
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public ResponseData<List<CMSEntity>> deleteEntityAction(@RequestBody List<CMSEntity> entities) {
        return deleteEntity(entities);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseData<List<CMSEntity>> deleteEntity(@RequestBody List<CMSEntity> entities) {

        //Step 1: Validate input request  via required parameters

        //Step 2: Validate input header

        //Step 3: add record
        try {

            entities = mongoDBEntityService.delete(entities);
            ResponseData<List<CMSEntity>> responseData = createResponse("Success");
            responseData.setData(entities);

            return responseData;
        } catch (Exception e) {

            ResponseData<List<CMSEntity>> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return responseData;
        }
    }

    @RequestMapping(value = "/{entityType}/{id}", method = RequestMethod.GET)
    public ResponseData<CMSEntity> queryByTypeAndId(@PathVariable("entityType") String entityType,
                                                    @PathVariable("id") String id) {

        //Step 1: Validate input request  via required parameters
        //Step 2: Validate input header
        //Step 3: Get priority
        List<SearchHit> records = null;

        try {
            CMSEntity cmsEntity = new CMSEntity();
            cmsEntity.setEntityType(entityType);
            cmsEntity.setId(id);
            List<CMSEntity> results = mongoDBEntityService.search(cmsEntity);
            if (results != null && results.size() > 0) {
                ResponseData<CMSEntity> responseData = createResponse("Success");
                responseData.setData(results.get(0));
                return responseData;
            } else {
                ResponseData<CMSEntity> responseData = createResponse("Not Found");
                responseData.setData(cmsEntity);
                return responseData;
            }

        } catch (Exception e) {
            ResponseData<CMSEntity> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return responseData;
        }
    }

    @RequestMapping(value = "/{entityType}", method = RequestMethod.GET)
    public ResponseData<List<CMSEntity>> queryByType(@PathVariable("entityType") String entityType) {

        //Step 1: Validate input request  via required parameters
        //Step 2: Validate input header
        //Step 3: Get priority
        List<SearchHit> records = null;

        try {
            CMSEntity cmsEntity = new CMSEntity();
            cmsEntity.setEntityType(entityType);

            List<CMSEntity> results = mongoDBEntityService.search(cmsEntity);
            if (results != null && results.size() > 0) {
                ResponseData<List<CMSEntity>> responseData = createResponse("Success");
                responseData.setData(results);
                return responseData;
            } else {
                ResponseData<List<CMSEntity>> responseData = createResponse("Not Found");
                responseData.setData(null);
                return responseData;
            }

        } catch (Exception e) {
            ResponseData<List<CMSEntity>> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return responseData;
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ServiceResponseMsg> handleUserNotFoundException(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(new ServiceResponseMsg(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}