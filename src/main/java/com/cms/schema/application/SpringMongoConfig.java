package com.cms.schema.application;

import com.mongodb.MongoClientURI;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.net.UnknownHostException;

@Configuration
@ConfigurationProperties("mongodb")

public class SpringMongoConfig {

    private String uri;
    private String schemaCollectionName;

    public String getSchemaCollectionName() {
        return schemaCollectionName;
    }

    public void setSchemaCollectionName(String schemaCollectionName) {
        this.schemaCollectionName = schemaCollectionName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public MongoClientURI mongoClientUri() {
        return new MongoClientURI(getUri());
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(mongoClientUri());
    }

    @Bean
    public MongoTemplate mongoTemplate() throws UnknownHostException {

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;

    }


}