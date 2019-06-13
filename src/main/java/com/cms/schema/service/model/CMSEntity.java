package com.cms.schema.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
@Document
public class CMSEntity {
    @Id
    private String id;

    @JsonProperty("entity_type")
    private String entityType;

    @JsonProperty("external_id")
    private String externalId;

    @JsonProperty("bp_name")
    private String bpName;


    @JsonProperty("url")
    private String urn;

    private String status;

    private String title;

    @JsonProperty("bp_version")
    private Integer bpVersion;

    private Map<String,Object> others;

    public CMSEntity(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getBpName() {
        return bpName;
    }

    public void setBpName(String bpName) {
        this.bpName = bpName;
    }

    public String getUrn() {
        if(StringUtils.isNotEmpty(bpName) && StringUtils.isNotEmpty(externalId) ) {
            return new StringBuilder(bpName).append("_").append(externalId).toString();
        } else {
            return null;
        }
    }

    public static void main(String args[]){
        new CMSEntity().getUrn();
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getBpVersion() {
        return bpVersion;
    }

    public void setBpVersion(Integer bpVersion) {
        this.bpVersion = bpVersion;
    }

    public Map<String, Object> getOthers() {
        return others;
    }

    public void setOthers(Map<String, Object> others) {
        this.others = others;
    }

    @Override
    public String toString() {
        return "CMSEntity{" +
                "id='" + id + '\'' +
                ", entityType='" + entityType + '\'' +
                ", externalId='" + externalId + '\'' +
                ", bpName='" + bpName + '\'' +
                ", urn='" + urn + '\'' +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                ", bpVersion=" + bpVersion +
                '}';
    }
}
