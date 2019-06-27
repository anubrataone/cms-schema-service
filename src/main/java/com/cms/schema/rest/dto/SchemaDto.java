package com.cms.schema.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchemaDto implements Serializable {
    @Id
    private String id;


    @JsonProperty("bpName")
    private String bpName;

    @JsonProperty("bpVersion")
    private int bpVersion;

    @JsonProperty("urn")
    private String urn;

    @JsonProperty("status")
    private String status;

    @JsonProperty("elements")
    private Map<String, LinkedHashMap> elements;

    public SchemaDto() {

    }

    public Map<String, LinkedHashMap> getElements() {
        return elements;
    }

    public void setElements(Map<String, LinkedHashMap> elements) {
        this.elements = elements;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBpName() {
        return bpName;
    }

    public void setBpName(String bpName) {
        this.bpName = bpName;
    }

    public int getBpVersion() {
        return bpVersion;
    }

    public void setBpVersion(int bpVersion) {
        this.bpVersion = bpVersion;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SchemaEntity{" +
                "id='" + id + '\'' +
                ", bpName='" + bpName + '\'' +
                ", bpVersion=" + bpVersion +
                ", urn='" + urn + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
