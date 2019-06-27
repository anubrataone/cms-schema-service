package com.cms.schema.application.mongo;

import com.cms.bp.schema.core.BpSchemaFactory;
import com.cms.schema.application.SpringMongoConfig;
import com.cms.schema.rest.dto.SchemaDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchemaEntityDBService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    SpringMongoConfig springMongoConfig;

    public SchemaDto upsert(SchemaDto entity, String wholeBody) throws Exception {
        if (entity == null) {
            throw new Exception("Entity is null");
        }
        if (entity.getId() != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(new ObjectId(entity.getId())));
            SchemaDto existing = mongoTemplate.findOne(query, SchemaDto.class, springMongoConfig.getSchemaCollectionName());

            DBObject dbDoc = new BasicDBObject();
            Map<String, LinkedHashMap> allElements = existing.getElements();
            allElements.putAll(entity.getElements());
            entity.getElements().putAll(allElements);
            //mongoTemplate.getConverter().write(existing, dbDoc); //it is the one spring use for convertions.
            mongoTemplate.getConverter().write(entity, dbDoc); //it is the one spring use for convertions.

            //mongoTemplate.getConverter().write(wholeBody, dbDoc); //it is the one spring use for convertions.

            Update update = Update.fromDBObject(dbDoc);

            FindAndModifyOptions options = new FindAndModifyOptions();
            options.returnNew(true);
            entity = mongoTemplate.findAndModify(query, update, options, SchemaDto.class, springMongoConfig.getSchemaCollectionName());
        } else {
            String newwholeBody = wholeBody;
            mongoTemplate.insert(newwholeBody, springMongoConfig.getSchemaCollectionName());

        }
        return entity;
    }

    public List<SchemaDto> delete(List<SchemaDto> entities) throws Exception {
        if (entities == null || entities.size() == 0) {
            throw new Exception("Entity is null");
        }
        for (SchemaDto entity : entities) {
            if (entity.getId() != null) {
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(new ObjectId(entity.getId())));

                mongoTemplate.findAndRemove(query, SchemaDto.class, springMongoConfig.getSchemaCollectionName());
            }
        }
        return entities;
    }

    public List<SchemaDto> searchByBpNameAndVersion(SchemaDto entity) throws Exception {
        return searchByBpNameAndVersion(entity.getBpName(), Integer.valueOf(entity.getBpVersion()));
    }

    public List<SchemaDto> searchByBpNameAndVersion(String bpName, int bpVersion) throws Exception {
        if (bpName == null) {
            throw new Exception("Entity is null");
        }

        Query query = new Query();

        query.addCriteria(Criteria.where("bpName").is(bpName));
        query.addCriteria(Criteria.where("bpVersion").is(bpVersion));

        SchemaDto existing = mongoTemplate.findOne(query, SchemaDto.class, springMongoConfig.getSchemaCollectionName());
        return Arrays.asList(existing);


    }

    public List<SchemaDto> loadAllBpSchemas() throws Exception {

        Query query = new Query();

        query.addCriteria(Criteria.where("bpName").ne(null));

        return mongoTemplate.findAll(SchemaDto.class, springMongoConfig.getSchemaCollectionName());

    }

    @PostConstruct
    public void init() throws Exception {
        List<SchemaDto> schemaEntities = loadAllBpSchemas();
        ObjectMapper mapper = new ObjectMapper();

        schemaEntities.stream().forEach(schemaEntity -> {
            JsonNode schemaNode = mapper.convertValue(schemaEntity, JsonNode.class);

            try {
                BpSchemaFactory.getInstance().createBpSchemaInstance(schemaNode, "bpName", null, schemaNode.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

}
