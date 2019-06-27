package com.cms.schema.rest;


import com.cms.bp.schema.core.BpSchema;
import com.cms.bp.schema.core.BpSchemaFactory;
import com.cms.bp.validator.SchemaValidatorResult;
import com.cms.schema.application.mongo.SchemaAttributeEntityDBService;
import com.cms.schema.application.mongo.SchemaEntityDBService;
import com.cms.schema.rest.dto.SchemaAttributeDto;
import com.cms.schema.rest.dto.SchemaDto;
import com.cms.schema.rest.response.ResponseData;
import com.cms.schema.rest.response.ResponseHeader;
import com.cms.schema.rest.validators.SchemaAttributeEntityValidator;
import com.cms.schema.rest.validators.SchemaEntityValidator;
import com.cms.schema.rest.validators.ValidatorMsg;
import com.cms.schema.service.model.ServiceResponseMsg;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("schema")
@Api(value = "schema")
public class CMSSchemaRestService {

    private static ObjectMapper mapper = new ObjectMapper();
    @Autowired
    SchemaAttributeEntityDBService schemaAttributeEntityDBService;

    @Autowired
    SchemaEntityDBService schemaEntityDBService;

    public CMSSchemaRestService() {
        // needed for autowiring
    }

    private static ResponseData createResponse(String message) {
        ResponseData<SchemaDto> responseData = new ResponseData();
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

    @RequestMapping(value = "/attribute", method = RequestMethod.POST)
    public ResponseEntity<ResponseData<SchemaAttributeDto>> createSchemaAttributeEntity(@RequestBody SchemaAttributeDto attribute) {

        //Step 1: Validate input request  via required parameters

        //Step 2: Validate input header
        ValidatorMsg validatorMsg = SchemaAttributeEntityValidator.isValid(attribute);
        if (!validatorMsg.isValid()) {
            ResponseData<SchemaAttributeDto> responseData = createResponse("Failed: " + validatorMsg.getMsg());
            responseData.setData(attribute);
            return ResponseEntity.badRequest().body(responseData);
        }

        //Step 3: add record
        try {
            attribute.setId(null);
            attribute = schemaAttributeEntityDBService.upsert(attribute);
            ResponseData<SchemaAttributeDto> responseData = createResponse("Success");
            responseData.setData(attribute);

            return ResponseEntity.ok().body(responseData);
        } catch (Exception e) {

            ResponseData<SchemaAttributeDto> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return ResponseEntity.unprocessableEntity().body(responseData);
        }
    }

    @RequestMapping(value = "/attribute/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseData<SchemaAttributeDto>> updateSchemaAttributeEntity(@PathVariable("id") String id, @RequestBody SchemaAttributeDto entity) {

        //Step 1: Validate input request  via required parameters

        ValidatorMsg validatorMsg = SchemaAttributeEntityValidator.isValid(entity);
        if (!validatorMsg.isValid()) {
            ResponseData<SchemaAttributeDto> responseData = createResponse("Failed: " + validatorMsg.getMsg());
            responseData.setData(entity);
            return ResponseEntity.badRequest().body(responseData);
        } else if (StringUtils.isEmpty(id)) {
            ResponseData<SchemaAttributeDto> responseData = createResponse("Failed: Id must not be empty or null for updating");
            responseData.setData(entity);
            return ResponseEntity.badRequest().body(responseData);
        }

        //Step 2: Validate input header

        //Step 3: add record
        try {
            entity.setId(id);
            entity = schemaAttributeEntityDBService.upsert(entity);
            ResponseData<SchemaAttributeDto> responseData = createResponse("Success");
            responseData.setData(entity);

            return ResponseEntity.ok().body(responseData);
        } catch (Exception e) {

            ResponseData<SchemaAttributeDto> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return ResponseEntity.unprocessableEntity().body(responseData);
        }
    }

    @RequestMapping(value = "/attribute/delete", method = RequestMethod.DELETE)
    public ResponseData<List<SchemaAttributeDto>> deleteAttributeEntityAction(@RequestBody List<SchemaAttributeDto> entities) {
        return deleteSchemaAttributeEntity(entities);
    }

    @RequestMapping(value = "/attribute/delete", method = RequestMethod.POST)
    public ResponseData<List<SchemaAttributeDto>> deleteSchemaAttributeEntity(@RequestBody List<SchemaAttributeDto> entities) {

        //Step 1: Validate input request  via required parameters

        //Step 2: Validate input header

        //Step 3: add record
        try {

            entities = schemaAttributeEntityDBService.delete(entities);
            ResponseData<List<SchemaAttributeDto>> responseData = createResponse("Success");
            responseData.setData(entities);

            return responseData;
        } catch (Exception e) {

            ResponseData<List<SchemaAttributeDto>> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return responseData;
        }
    }


    /***START SCHEMA APIS***************/
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ResponseData<SchemaDto>> createSchemaEntity(@RequestBody String schemaBody) {

        //Step 1: Validate input request  via required parameters
        ObjectMapper mapper = new ObjectMapper();
        SchemaDto schemaDto = null;
        try {
            schemaDto = mapper.readValue(schemaBody, SchemaDto.class);
        } catch (IOException e) {
            ResponseData<SchemaDto> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return ResponseEntity.badRequest().body(responseData);
        }

        //Step 2: Validate input header
        ValidatorMsg validatorMsg = SchemaEntityValidator.isValid(schemaDto);
        if (!validatorMsg.isValid()) {
            ResponseData<SchemaDto> responseData = createResponse("Failed: " + validatorMsg.getMsg());
            responseData.setData(schemaDto);
            return ResponseEntity.badRequest().body(responseData);
        }

        //Step 3: add record
        try {
            schemaDto.setId(null);
            schemaDto = schemaEntityDBService.upsert(schemaDto, schemaBody);
            ResponseData<SchemaDto> responseData = createResponse("Success");
            responseData.setData(schemaDto);

            return ResponseEntity.ok().body(responseData);
        } catch (Exception e) {

            ResponseData<SchemaDto> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return ResponseEntity.badRequest().body(responseData);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseData<SchemaDto>> updateSchema(@PathVariable("id") String id, @RequestBody String schemaBody) {

        //Step 1: Validate input request  via required parameters

        ObjectMapper mapper = new ObjectMapper();
        SchemaDto schemaDto = null;
        try {
            schemaDto = mapper.readValue(schemaBody, SchemaDto.class);
        } catch (IOException e) {
            ResponseData<SchemaDto> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return ResponseEntity.badRequest().body(responseData);
        }

        ValidatorMsg validatorMsg = SchemaEntityValidator.isValid(schemaDto);
        if (!validatorMsg.isValid()) {
            ResponseData<SchemaDto> responseData = createResponse("Failed: " + validatorMsg.getMsg());
            responseData.setData(schemaDto);
            return ResponseEntity.badRequest().body(responseData);
        } else if (StringUtils.isEmpty(id)) {
            ResponseData<SchemaDto> responseData = createResponse("Failed: Id must not be empty or null for updating");
            responseData.setData(schemaDto);
            return ResponseEntity.badRequest().body(responseData);
        }

        //Step 2: Validate input header

        //Step 3: add record
        try {
            schemaDto.setId(id);
            schemaDto = schemaEntityDBService.upsert(schemaDto, schemaBody);
            ResponseData<SchemaDto> responseData = createResponse("Success");
            responseData.setData(schemaDto);

            return ResponseEntity.ok().body(responseData);
        } catch (Exception e) {

            ResponseData<SchemaDto> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return ResponseEntity.badRequest().body(responseData);
        }
    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public ResponseEntity<ResponseData<SchemaValidatorResult>> validate(@RequestBody String bpBody) {

        //Step 1: Validate input request  via required parameters

        //Step 2: Validate input header


        //Step 3: add record
        try {
            JsonNode jsonNode = mapper.readTree(bpBody);

            List<SchemaDto> schemaList = schemaEntityDBService.searchByBpNameAndVersion(jsonNode.get("bpName").asText(), jsonNode.get("bpVersion").intValue());
            BpSchema bpSchema = BpSchemaFactory.getInstance().getSchemaByName(schemaList.get(0).getBpName());

            //  p.reloadSchemaDescriptor();
            SchemaValidatorResult validatorResult = bpSchema.validate(bpBody);

            ResponseData<SchemaValidatorResult> responseData = createResponse("Success");
            responseData.setData(validatorResult);

            return ResponseEntity.ok().body(responseData);
        } catch (Exception e) {

            ResponseData<SchemaValidatorResult> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return ResponseEntity.badRequest().body(responseData);
        }
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public ResponseData<List<SchemaDto>> deleteEntityAction(@RequestBody List<SchemaDto> entities) {
        return deleteSchemaEntity(entities);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseData<List<SchemaDto>> deleteSchemaEntity(@RequestBody List<SchemaDto> entities) {

        //Step 1: Validate input request  via required parameters

        //Step 2: Validate input header

        //Step 3: add record
        try {

            entities = schemaEntityDBService.delete(entities);
            ResponseData<List<SchemaDto>> responseData = createResponse("Success");
            responseData.setData(entities);

            return responseData;
        } catch (Exception e) {

            ResponseData<List<SchemaDto>> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return responseData;
        }
    }

    /***END SCHEMA APIS*****************/


    @RequestMapping(value = "/{bpName}/{bpVersion}", method = RequestMethod.GET)
    public ResponseData<SchemaDto> queryByTypeAndId(@PathVariable("bpName") String bpName,
                                                    @PathVariable("bpVersion") int bpVersion) {

        //Step 1: Validate input request  via required parameters
        //Step 2: Validate input header
        //Step 3: Get priority
        List<SchemaDto> records = null;

        try {
            SchemaDto schemaDto = new SchemaDto();

            List<SchemaDto> results = schemaEntityDBService.searchByBpNameAndVersion(bpName, bpVersion);
            if (results != null && results.size() > 0) {
                ResponseData<SchemaDto> responseData = createResponse("Success");
                responseData.setData(results.get(0));
                return responseData;
            } else {
                ResponseData<SchemaDto> responseData = createResponse("Not Found");
                responseData.setData(schemaDto);
                return responseData;
            }

        } catch (Exception e) {
            ResponseData<SchemaDto> responseData = createResponse("Failed: " + ExceptionUtils.getStackTrace(e));
            return responseData;
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ServiceResponseMsg> handleUserNotFoundException(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(new ServiceResponseMsg(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}