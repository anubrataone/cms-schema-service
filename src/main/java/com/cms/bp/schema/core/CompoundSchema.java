package com.cms.bp.schema.core;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class CompoundSchema extends BpSchema {

    public CompoundSchema() {

    }

    @Override
    public boolean validate(String jsonContent) {
        try {

            //Validate 3 steps:
            //1. Checked if any missing required node
            //2. Checked if provided node contain valida data
            //3.
            JsonNode jsonDataNode = mapper.readTree(jsonContent);
            ConcurrentHashMap<Boolean, StringBuilder> result = new ConcurrentHashMap<>();
            result.put(Boolean.TRUE, new StringBuilder());
            result.put(Boolean.FALSE, new StringBuilder());

            if (jsonDataNode instanceof ObjectNode) {
                jsonDataNode.fields().forEachRemaining(keyValueDataData -> {
                    if (null != this.getSchemaJsonNode().get(keyValueDataData.getKey())) {
                        JsonNode foundSchemaNode = this.getSchemaJsonNode().get(keyValueDataData.getKey());
                        if (false == BpSchemaFactory.getInstance().getSchemaByName(foundSchemaNode.get("type").asText())
                                .validate(keyValueDataData.getValue().asText())) {
                            result.get(Boolean.FALSE).append("Not valid at validation");
                        } else {
                            result.get(Boolean.TRUE).append("Valid - " + foundSchemaNode.toString());
                        }
                    }
                });
                return (result.get(Boolean.FALSE).length() == 0);
            }

        } catch (IOException e) {
            return false;
        }
        return false;
    }
}
