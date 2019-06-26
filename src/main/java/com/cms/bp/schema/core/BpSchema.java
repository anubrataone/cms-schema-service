package com.cms.bp.schema.core;

import com.cms.bp.validator.SchemaValidatorResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.*;

public abstract class BpSchema implements Serializable {


    public static ObjectMapper mapper = new ObjectMapper();
    private File schemaFile;
    private String schemaJson;
    private JsonNode schemaJsonNode;

    public BpSchema(File schemaFile) {
        this.schemaFile = schemaFile;
    }

    public BpSchema(String schemaJsonStr) throws IOException {
        reloadSchemaDescriptor(schemaJsonStr);
    }

    public File getSchemaFile() {
        return schemaFile;
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

    public void reloadSchemaDescriptor() {

        InputStream in = null;
        try {

            String schemaJsonData = IOUtils.toString(new FileInputStream(schemaFile), "UTF-8");

            reloadSchemaDescriptor(schemaJsonData);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void reloadSchemaDescriptor(String schemaJsonStr) throws IOException {

        setSchemaJson(schemaJsonStr);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(getSchemaJson());
        setSchemaJsonNode(jsonNode);

    }

    public abstract SchemaValidatorResult validate(String jsonContent);
}