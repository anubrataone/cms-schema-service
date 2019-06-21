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
        schemaMap.put(SingleLineBoolean.class.getSimpleName(), new SingleLineBoolean());
        schemaMap.put(SingleLineDouble.class.getSimpleName(), new SingleLineDouble());
        schemaMap.put(SingleLineNumber.class.getSimpleName(), new SingleLineNumber());
        schemaMap.put(SingleLineText.class.getSimpleName(), new SingleLineText());

        BpSchema bpVodTitle = new CompoundSchema();
        bpVodTitle.setSchemaFileName("bpVodTitle");
        try {
            bpVodTitle.reloadSchemaDescriptor();
        } catch (IOException e) {

        }
        schemaMap.put("BpVodTitle", bpVodTitle);

        BpSchema attrAsset = new CompoundSchema();
        attrAsset.setSchemaFileName("AttrAsset");
        try {
            attrAsset.reloadSchemaDescriptor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        schemaMap.put("AttrAsset", attrAsset);

        BpSchema attrCatalogAvailibility = new CompoundSchema();
        attrCatalogAvailibility.setSchemaFileName("AttrCatalogAvailibility");
        try {
            attrCatalogAvailibility.reloadSchemaDescriptor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        schemaMap.put("AttrCatalogAvailibility", attrCatalogAvailibility);

        BpSchema attrEntitlement = new CompoundSchema();
        attrEntitlement.setSchemaFileName("AttrEntitlements");
        try {
            attrEntitlement.reloadSchemaDescriptor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        schemaMap.put("AttrEntitlement", attrEntitlement);

        BpSchema attrLocalizedData = new CompoundSchema();
        attrLocalizedData.setSchemaFileName("AttrLocalizedData");
        try {
            attrLocalizedData.reloadSchemaDescriptor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        schemaMap.put("AttrLocalizedData", attrLocalizedData);

        BpSchema attrResourceRef = new CompoundSchema();
        attrResourceRef.setSchemaFileName("AttrResourceRef");
        try {
            attrResourceRef.reloadSchemaDescriptor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        schemaMap.put("AttrResourceRef", attrResourceRef);


    }

    public static BpSchemaFactory getInstance() {
        return instance != null ? instance : (instance = new BpSchemaFactory());
    }

    public BpSchema getSchemaByName(String name) {
        return schemaMap.get(name);
    }
}
