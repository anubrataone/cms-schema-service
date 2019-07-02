package com.cms.bp.schema.core;


import com.cms.bp.validator.SchemaValidatorResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;

public class CompoundSchema extends BpSchema {

    public CompoundSchema(String schemaJson) throws IOException {
        super(schemaJson);
    }

    @Override
    public SchemaValidatorResult validate(String jsonData) {
        return validate(getSchemaJsonNode().get(ELEMENTS_NODE_NAME), jsonData);
    }

    public SchemaValidatorResult validate(JsonNode schemaNodeToValidate, String jsonContent) {

        StringBuilder validResultMsg = new StringBuilder();
        StringBuilder invalidResultMsg = new StringBuilder();

        try {

            //Validate 3 steps:
            //1. Checked if any missing required node
            //2. Checked if provided node contain valida data
            //3. ...
            JsonNode jsonDataNode = mapper.readTree(jsonContent);

            if (jsonDataNode instanceof ObjectNode) {
                return validateOneObjectNode(schemaNodeToValidate, jsonDataNode);
            } else if (jsonDataNode.isArray()) {
                ArrayNode arrNode = (ArrayNode) jsonDataNode;

                for (final JsonNode oneJsonNode : arrNode) {
                    SchemaValidatorResult oneNodeResult = validateOneObjectNode(schemaNodeToValidate, oneJsonNode);
                    if (oneNodeResult.getCode() != SchemaValidatorResult.SUCCESS) {
                        invalidResultMsg.append(oneNodeResult.getMsg());

                    } else {
                        validResultMsg.append(oneNodeResult.getMsg());
                    }
                }

            }
        } catch (Exception e) {
            invalidResultMsg.append("Not valid at validation: " + ExceptionUtils.getStackTrace(e));
        }
        if (invalidResultMsg.length() != 0) {
            return new SchemaValidatorResult(SchemaValidatorResult.FIELD_INVALID, invalidResultMsg.toString());
        } else {
            return new SchemaValidatorResult(SchemaValidatorResult.SUCCESS, validResultMsg.toString());
        }
    }

    private SchemaValidatorResult validateOneObjectNode(JsonNode schemaNodeToValidate, JsonNode jsonDataNode) {
        StringBuilder validResultMsg = new StringBuilder();
        StringBuilder invalidResultMsg = new StringBuilder();

        schemaNodeToValidate.fields().forEachRemaining(keyValueElementSchemaNode -> {

            JsonNode foundDataNode = jsonDataNode.get(keyValueElementSchemaNode.getKey());
            if (keyValueElementSchemaNode.getValue().get(REQUIRED_NODE_NAME) != null &&
                    keyValueElementSchemaNode.getValue().get(REQUIRED_NODE_NAME).booleanValue() == true) {
                if (foundDataNode == null) {
                    invalidResultMsg.append("\nMissing Required Node - ")
                            .append(keyValueElementSchemaNode.getKey());
                } else {
                    validResultMsg.append("\n").append(keyValueElementSchemaNode.getKey())
                            .append(" pass 'required' validation step");
                }
            }

            if (keyValueElementSchemaNode.getValue().get(OCCURRENCE_NODE_NAME) != null &&
                    keyValueElementSchemaNode.getValue().get(OCCURRENCE_NODE_NAME).asText().equals("multiple")) {
                if (foundDataNode != null && !(foundDataNode.isArray())) {
                    invalidResultMsg.append("\nExpected Array Node - ")
                            .append(keyValueElementSchemaNode.getKey());
                } else {
                    validResultMsg.append("\n").append(keyValueElementSchemaNode.getKey())
                            .append(":passed 'is array' validation step");
                }
            }

            if (foundDataNode != null) {
                BpSchema schemaByName = BpSchemaFactory.getInstance().getSchemaByName(
                        keyValueElementSchemaNode.getValue().get(TYPE_NODE_NAME).asText());
                if (schemaByName == null) {
                    invalidResultMsg.append("\nSchema name:").append(keyValueElementSchemaNode.getValue().get(TYPE_NODE_NAME).asText())
                            .append(" is not supported");
                    throw new NullPointerException(invalidResultMsg.toString());
                }
                SchemaValidatorResult validatorResult = schemaByName.validate(foundDataNode.toString());
                if (SchemaValidatorResult.SUCCESS != validatorResult.getCode()) {
                    invalidResultMsg.append(keyValueElementSchemaNode).append("\nNot valid at validation: ").append(validatorResult.getMsg());
                } else {
                    validResultMsg.append("\nValid - Node Name:").append(keyValueElementSchemaNode.getKey())
                            .append(" - value:").append(foundDataNode.toString()).append(validatorResult.getMsg())
                            .append(" \n ");
                }
            }

        });
        if (invalidResultMsg.length() != 0) {
            return new SchemaValidatorResult(SchemaValidatorResult.FIELD_INVALID, invalidResultMsg.append(validResultMsg).toString());
        } else {
            return new SchemaValidatorResult(SchemaValidatorResult.SUCCESS, validResultMsg.toString());
        }
    }

}
