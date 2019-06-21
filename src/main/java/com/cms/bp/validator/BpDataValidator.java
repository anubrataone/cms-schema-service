package com.cms.bp.validator;

import com.cms.bp.schema.core.BpSchema;
import com.cms.bp.schema.core.BpSchemaFactory;
import com.cms.bp.schema.core.SingleLineBoolean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class BpDataValidator {

    private ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        BpDataValidator v = new BpDataValidator();
        String jsonData = "";
        ConcurrentHashMap<String, Boolean> validateResult = new ConcurrentHashMap<String, Boolean>();
        v.validate(validateResult, new ObjectMapper(), jsonData, new SingleLineBoolean());
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    private boolean validate(ConcurrentHashMap<String, Boolean> validateResult, ObjectMapper mapper, String jsonData, BpSchema bpSchema) throws IOException {
        JsonNode jsonNode = mapper.readTree(jsonData);
        Consumer<Map.Entry<String, JsonNode>> entryData = (Map.Entry<String, JsonNode> entryNode) -> {

            System.out.println(entryNode.getKey() + "--" + entryNode.getValue().getNodeType());
            if (JsonNodeType.OBJECT.equals(entryNode.getValue().getNodeType())) {
                try {
                    validateResult.put(entryNode.getValue().asText(),
                            validate(validateResult, mapper, entryNode.getValue().asText(), BpSchemaFactory.getInstance().getSchemaByName(entryNode.getValue().get("type").asText())));
                } catch (IOException e) {

                }
                System.out.println("Type:" + BpSchemaFactory.getInstance().getSchemaByName(entryNode.getValue().get("type").asText()));
            } else {
                System.out.println("Type:" + entryNode.getValue().getNodeType());
            }
        };
        jsonNode.fields().forEachRemaining(entryData);
        return true;
    }
}
