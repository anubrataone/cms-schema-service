package com.cms.schema.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Document
public class SchemaAttributeDto implements Serializable {

    @Id
    private String id;

    @JsonProperty("attrName")
    private String attrName;

    @JsonProperty("attrType")
    private int attrType;

    @JsonProperty("attrClass")
    private String attrClass;

    @JsonProperty("elements")
    private Map<String, LinkedHashMap> elements;

    public SchemaAttributeDto() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, LinkedHashMap> getElements() {
        return elements;
    }

    public void setElements(Map<String, LinkedHashMap> elements) {
        this.elements = elements;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public int getAttrType() {
        return attrType;
    }

    public void setAttrType(int attrType) {
        this.attrType = attrType;
    }

    public String getAttrClass() {
        return attrClass;
    }

    public void setAttrClass(String attrClass) {
        this.attrClass = attrClass;
    }
}
