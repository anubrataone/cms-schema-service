package com.cms.bp.schema.core;

import com.fasterxml.jackson.databind.JsonNode;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BpSchemaFactory {
    public static Map<String, Class> nativeBpSchemaMap = new HashMap<>();
    public static Map<String, Class> specialBpSchemaMap = new HashMap<>();

    private static BpSchemaFactory instance;

    static {

        getInstance();
        nativeBpSchemaMap.put("boolean", SingleLineBoolean.class);
        nativeBpSchemaMap.put("double", SingleLineDouble.class);
        nativeBpSchemaMap.put("integer", SingleLineNumber.class);
        nativeBpSchemaMap.put("string", SingleLineText.class);

        specialBpSchemaMap.put("CustomKVP", AttrCustomParam.class);
        specialBpSchemaMap.put("KVP", CompoundSchema.class);
    }

    private ConcurrentHashMap<String, BpSchema> schemaMap = new ConcurrentHashMap<>();

    private BpSchemaFactory() {
    }

    public static BpSchemaFactory getInstance() {
        return instance != null ? instance : (instance = new BpSchemaFactory());
    }

    public BpSchema getSchemaByName(String name) {
        return schemaMap.get(name);
    }

    public BpSchema createBpSchemaInstance(JsonNode schemaNode, String schemaNameProperty, String schemaClassProperty,
                                           String fullSchemaJson) throws Exception {

        Class<?> schemaClazz;
        if (schemaNode.get(schemaClassProperty) != null && nativeBpSchemaMap.get(schemaNode.get(schemaClassProperty).asText()) != null) {
            schemaClazz = nativeBpSchemaMap.get(schemaNode.get(schemaClassProperty).asText());
        } else if (schemaClassProperty == null || specialBpSchemaMap.get(schemaNode.get(schemaClassProperty).asText()) == null) {
            schemaClazz = Class.forName(CompoundSchema.class.getName());
        } else {
            schemaClazz = Class.forName(specialBpSchemaMap.get(schemaNode.get(schemaClassProperty).asText()).getName());
        }
        Constructor<?> ctor = schemaClazz.getConstructor(String.class);
        BpSchema object = (BpSchema) ctor.newInstance(new Object[]{fullSchemaJson});

        schemaMap.put(schemaNode.get(schemaNameProperty).asText(), object);
        return object;
    }
}
