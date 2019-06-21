package com.cms.bp.schema.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public abstract class BpSchema implements Serializable {
    public static ObjectMapper mapper = new ObjectMapper();

    private String schemaJson;
    private JsonNode schemaJsonNode;

    //    private String type = "Native";
//    private String clazz = "string";
    private String schemaFileName = null;

    public static void main(String[] args) throws Exception {
        BpSchema p = new SingleLineDouble();
        p.reloadSchemaDescriptor();
        System.out.print(p.getSchemaJson());
        System.out.println("Validate : " + p.validate("3"));
    }

    public String getSchemaFileName() {
        return schemaFileName;
    }

    public void setSchemaFileName(String schemaFileName) {
        this.schemaFileName = schemaFileName;
    }

    public String getSchemaJson() {
        return schemaJson;
    }

//    public String getType() {
//        return type;
//    }
//
//    public String getClazz() {
//        return clazz;
//    }

    private void setSchemaJson(String schemaJson) {
        this.schemaJson = schemaJson;
    }

    public JsonNode getSchemaJsonNode() {
        return schemaJsonNode;
    }

    public void setSchemaJsonNode(JsonNode schemaJsonNode) {
        this.schemaJsonNode = schemaJsonNode;
    }

    public void reloadSchemaDescriptor() throws IOException {

        String schemaFileName = (this.schemaFileName == null) ? this.getClass().getSimpleName() : this.schemaFileName;
        char[] c = schemaFileName.toCharArray();
        Character.toLowerCase(c[0]);

        schemaFileName = new String(c);
        InputStream in = BpSchema.class.getClassLoader().getResourceAsStream(new StringBuilder("schemas/")
                .append(schemaFileName).append(".json").toString());
        String schemaJsonData = IOUtils.toString(in, "UTF-8");
        in.close();
        setSchemaJson(schemaJsonData);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(schemaJson);
        setSchemaJsonNode(jsonNode);
//        this.type = jsonNode.get("attrType").asText();
//        this.clazz = jsonNode.get("attrClass").asText();
    }

    public abstract boolean validate(String jsonContent);
}