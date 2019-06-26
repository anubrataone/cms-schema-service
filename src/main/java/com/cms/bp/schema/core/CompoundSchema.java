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
        return validate(getSchemaJsonNode().get("elements"), jsonData);
    }

    public SchemaValidatorResult validate(JsonNode schemaNodeToValidate, String jsonContent) {

        StringBuilder validResultMsg = new StringBuilder();
        StringBuilder invalidResultMsg = new StringBuilder();

        try {

            //Validate 3 steps:
            //1. Checked if any missing required node
            //2. Checked if provided node contain valida data
            //3.
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
            if (keyValueElementSchemaNode.getValue().get("required") != null &&
                    keyValueElementSchemaNode.getValue().get("required").booleanValue() == true) {
                if (foundDataNode == null) {
                    invalidResultMsg.append("Missing Required Node - ")
                            .append(keyValueElementSchemaNode.getKey())
                            .append("\n");
                }
            }

            if (keyValueElementSchemaNode.getValue().get("occurrence") != null &&
                    keyValueElementSchemaNode.getValue().get("occurrence").asText().equals("multiple")) {
                if (!(foundDataNode.isArray())) {
                    invalidResultMsg.append("Expected Array Node - ")
                            .append(keyValueElementSchemaNode.getKey())
                            .append("\n");
                }
            }

            if (foundDataNode != null) {
                BpSchema schemaByName = BpSchemaFactory.getInstance().getSchemaByName(
                        keyValueElementSchemaNode.getValue().get("type").asText());
                if (schemaByName == null) {
                    invalidResultMsg.append("Schema name:").append(keyValueElementSchemaNode.getValue().get("type").asText())
                            .append(" is not supported");
                    throw new NullPointerException(invalidResultMsg.toString());
                }
                SchemaValidatorResult validatorResult = schemaByName.validate(foundDataNode.toString());
                if (SchemaValidatorResult.SUCCESS != validatorResult.getCode()) {
                    invalidResultMsg.append(keyValueElementSchemaNode).append("Not valid at validation: ").append(validatorResult.getMsg());
                } else {
                    validResultMsg.append("Valid - " + foundDataNode.toString()).append(validatorResult.getMsg());
                }
            }

        });
        if (invalidResultMsg.length() != 0) {
            return new SchemaValidatorResult(SchemaValidatorResult.FIELD_INVALID, invalidResultMsg.toString());
        } else {
            return new SchemaValidatorResult(SchemaValidatorResult.SUCCESS, validResultMsg.toString());
        }
    }

}
