package com.cms.bp.schema.core;

import com.cms.bp.validator.SchemaValidatorResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;

public abstract class BpSchema implements Serializable {


    public static final String REQUIRED_NODE_NAME = "required";
    public static final String OCCURRENCE_NODE_NAME = "occurrence";
    public static final String TYPE_NODE_NAME = "type";
    public static final String ELEMENTS_NODE_NAME = "elements";
    public static ObjectMapper mapper = new ObjectMapper();
    private String schemaJson;
    private JsonNode schemaJsonNode;

    public BpSchema(String schemaJsonStr) throws IOException {
        reloadSchemaDescriptor(schemaJsonStr);
    }

    public String getSchemaJson() {
        return schemaJson;
    }

    private void setSchemaJson(String schemaJson) {
        this.schemaJson = schemaJson;
    }

    public JsonNode getSchemaJsonNode() {
        return schemaJsonNode;
    }

    public void setSchemaJsonNode(JsonNode schemaJsonNode) {
        this.schemaJsonNode = schemaJsonNode;
    }

    public void reloadSchemaDescriptor(String schemaJsonStr) throws IOException {

        setSchemaJson(schemaJsonStr);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(getSchemaJson());
        setSchemaJsonNode(jsonNode);

    }

    public abstract SchemaValidatorResult validate(String jsonContent);
}