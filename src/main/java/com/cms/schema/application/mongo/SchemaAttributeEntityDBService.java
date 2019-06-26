package com.cms.schema.application.mongo;

import com.cms.bp.schema.core.BpSchema;
import com.cms.bp.schema.core.BpSchemaFactory;
import com.cms.schema.rest.dto.SchemaAttributeDto;
import com.cms.schema.application.SpringMongoConfig;
import com.cms.schema.rest.dto.SchemaDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SchemaAttributeEntityDBService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    SpringMongoConfig springMongoConfig;

    public SchemaAttributeDto upsert(SchemaAttributeDto entity) throws Exception {
        if (entity == null) {
            throw new Exception("Entity is null");
        }
        if (entity.getId() != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(new ObjectId(entity.getId())));
            SchemaAttributeDto existing = mongoTemplate.findOne(query, SchemaAttributeDto.class, springMongoConfig.getSchemaCollectionName());

            DBObject dbDoc = new BasicDBObject();
            mongoTemplate.getConverter().write(existing, dbDoc); //it is the one spring use for convertions.
            mongoTemplate.getConverter().write(entity, dbDoc); //it is the one spring use for convertions.
            Update update = Update.fromDBObject(dbDoc);

            FindAndModifyOptions options = new FindAndModifyOptions();
            options.returnNew(true);
            entity = mongoTemplate.findAndModify(query, update, options, SchemaAttributeDto.class, springMongoConfig.getSchemaCollectionName());
        } else {
            mongoTemplate.insert(entity, springMongoConfig.getSchemaCollectionName());
        }
        return entity;
    }

    public List<SchemaAttributeDto> loadAllBpAttributeSchemas() throws Exception {

        Query query = new Query();

        query.addCriteria(Criteria.where("bpName").is(null));

        return mongoTemplate.findAll(SchemaAttributeDto.class, springMongoConfig.getSchemaCollectionName());

    }


    public List<SchemaAttributeDto> delete(List<SchemaAttributeDto> entities) throws Exception {
        if (entities == null || entities.size() == 0) {
            throw new Exception("Entity is null");
        }
        for (SchemaAttributeDto entity : entities) {
            if (entity.getId() != null) {
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(new ObjectId(entity.getId())));

                mongoTemplate.findAndRemove(query, SchemaAttributeDto.class, springMongoConfig.getSchemaCollectionName());
            }
        }
        return entities;
    }

    public List<SchemaAttributeDto> search(SchemaAttributeDto entity) throws Exception {
        if (entity == null) {
            throw new Exception("Entity is null");
        }

        Query query = new Query();
        if (entity.getId() != null) {

            query.addCriteria(Criteria.where("_id").is(new ObjectId(entity.getId())));
            SchemaAttributeDto existing = mongoTemplate.findOne(query, SchemaAttributeDto.class, springMongoConfig.getSchemaCollectionName());
            return Arrays.asList(existing);

        } else {

            for (Field f : SchemaAttributeDto.class.getDeclaredFields()) {
                try {
                    f.setAccessible(true);
                    if (f.get(entity) != null) {
                        query.addCriteria(Criteria.where(f.getName()).is(f.get(entity)));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            return mongoTemplate.find(query, SchemaAttributeDto.class, springMongoConfig.getSchemaCollectionName());
        }
    }

    @PostConstruct
    public void init() throws Exception {
        List<SchemaAttributeDto> schemaEntities = loadAllBpAttributeSchemas();
        ObjectMapper mapper = new ObjectMapper();

        schemaEntities.stream().forEach(schemaEntity -> {
            JsonNode schemaNode = mapper.convertValue(schemaEntity, JsonNode.class);

                try {
                    BpSchemaFactory.getInstance().createBpSchemaInstance(schemaNode, "attrName", "attrClass", schemaNode.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }


        });
    }
}
