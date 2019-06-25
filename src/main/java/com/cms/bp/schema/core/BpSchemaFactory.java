package com.cms.bp.schema.core;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class BpSchemaFactory {

    private static BpSchemaFactory instance;

    static {

        getInstance();

    }

    private ConcurrentHashMap<String, BpSchema> schemaMap = new ConcurrentHashMap<>();

    private BpSchemaFactory() {
        schemaMap.put(SingleLineBoolean.class.getSimpleName(), new SingleLineBoolean("SingleLineBoolean"));
        schemaMap.put(SingleLineDouble.class.getSimpleName(), new SingleLineDouble("SingleLineDouble"));
        schemaMap.put(SingleLineNumber.class.getSimpleName(), new SingleLineNumber("SingleLineNumber"));
        schemaMap.put(SingleLineText.class.getSimpleName(), new SingleLineText("SingleLineText"));

        BpSchema bpVodTitle = new CompoundSchema("bpVodTitle");

        try {
            bpVodTitle.reloadSchemaDescriptor();
        } catch (IOException e) {

        }
        schemaMap.put("BpVodTitle", bpVodTitle);

        BpSchema attrAsset = new AttributeCompoundSchema("AttrAsset");

        try {
            attrAsset.reloadSchemaDescriptor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        schemaMap.put("AttrAsset", attrAsset);

        BpSchema attrCatalogAvailibility = new AttributeCompoundSchema("AttrCatalogAvailibility");

        try {
            attrCatalogAvailibility.reloadSchemaDescriptor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        schemaMap.put("AttrCatalogAvailibility", attrCatalogAvailibility);

        BpSchema attrEntitlement = new AttributeCompoundSchema("AttrEntitlements");
        try {
            attrEntitlement.reloadSchemaDescriptor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        schemaMap.put("AttrEntitlement", attrEntitlement);

        BpSchema attrLocalizedData = new AttributeCompoundSchema("AttrLocalizedData");

        try {
            attrLocalizedData.reloadSchemaDescriptor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        schemaMap.put("AttrLocalizedData", attrLocalizedData);


        BpSchema attrResourceRef = new AttributeCompoundSchema("AttrResourceRef");

        try {
            attrResourceRef.reloadSchemaDescriptor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        schemaMap.put("AttrResourceRef", attrResourceRef);

        BpSchema attrCastnCrew = new AttributeCompoundSchema("AttrCastnCrew");
        try {
            attrCastnCrew.reloadSchemaDescriptor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        schemaMap.put("AttrCastnCrew", attrCastnCrew);

        BpSchema attrCustomParam = new AttrCustomParam("attrCustomParam");
        try {
            attrCustomParam.reloadSchemaDescriptor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        schemaMap.put("AttrCustomParam", attrCustomParam);

        BpSchema attrPersonis = new AttributeCompoundSchema("attrPerson");
        try {
            attrPersonis.reloadSchemaDescriptor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        schemaMap.put("AttrPerson", attrPersonis);

    }

    public static BpSchemaFactory getInstance() {
        return instance != null ? instance : (instance = new BpSchemaFactory());
    }

    public BpSchema getSchemaByName(String name) {
        return schemaMap.get(name);
    }
}
