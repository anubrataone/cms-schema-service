package com.cms.schema.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * "type": "SingleLineText",
 * "required": true,
 * "defaultValue": "BPVODTitle"
 */
public class SchemaField implements Serializable {
    @JsonProperty(value = "type", required = true)
    private String type;

    @JsonProperty(value = "required", required = true)
    private boolean required;

    @JsonProperty("defaultValue")
    private String defaultValue;

    @JsonProperty("primary-key")
    private String isPrimaryKey;

    @JsonProperty(value = "occurrence", defaultValue = "single")
    private String occurrence;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(String isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(String occurrence) {
        this.occurrence = occurrence;
    }

    @Override
    public String toString() {
        return "SchemaField{" +
                "type='" + type + '\'' +
                ", required=" + required +
                ", defaultValue='" + defaultValue + '\'' +
                ", isPrimaryKey='" + isPrimaryKey + '\'' +
                ", occurrence='" + occurrence + '\'' +
                '}';
    }
}
