package com.cms.schema.application.mongo;

import com.cms.schema.service.model.CMSEntity;
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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Service
public class MongoDBEntityService {

    @Autowired
    MongoTemplate mongoTemplate;

    public CMSEntity upsert(CMSEntity entity) throws Exception {
        if (entity == null) {
            throw new Exception("Entity is null");
        }
        if (entity.getId() != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(new ObjectId(entity.getId())));
            CMSEntity existing = mongoTemplate.findOne(query, CMSEntity.class, entity.getEntityType());

            DBObject dbDoc = new BasicDBObject();
            mongoTemplate.getConverter().write(existing, dbDoc); //it is the one spring use for convertions.
            mongoTemplate.getConverter().write(entity, dbDoc); //it is the one spring use for convertions.
            Update update = Update.fromDBObject(dbDoc);

            FindAndModifyOptions options = new FindAndModifyOptions();
            options.returnNew(true);
            entity = mongoTemplate.findAndModify(query, update, options, CMSEntity.class, entity.getEntityType());
        } else {
            mongoTemplate.insert(entity, entity.getEntityType());
        }
        return entity;
    }

    public List<CMSEntity> delete(List<CMSEntity> entities) throws Exception {
        if (entities == null || entities.size() == 0) {
            throw new Exception("Entity is null");
        }
        for (CMSEntity entity : entities) {
            if (entity.getId() != null) {
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(new ObjectId(entity.getId())));

                mongoTemplate.findAndRemove(query, CMSEntity.class, entity.getEntityType());
            }
        }
        return entities;
    }

    public List<CMSEntity> search(CMSEntity entity) throws Exception {
        if (entity == null) {
            throw new Exception("Entity is null");
        }

        Query query = new Query();
        if (entity.getId() != null) {

            query.addCriteria(Criteria.where("_id").is(new ObjectId(entity.getId())));
            CMSEntity existing = mongoTemplate.findOne(query, CMSEntity.class, entity.getEntityType());
            return Arrays.asList(existing);

        } else {

            for (Field f : CMSEntity.class.getDeclaredFields()) {
                try {
                    f.setAccessible(true);
                    if (f.get(entity) != null) {
                        query.addCriteria(Criteria.where(f.getName()).is(f.get(entity)));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            return mongoTemplate.find(query, CMSEntity.class, entity.getEntityType());
        }
    }

}
