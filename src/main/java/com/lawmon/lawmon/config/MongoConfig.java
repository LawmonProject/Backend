package com.lawmon.lawmon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
public class MongoConfig {
  @Autowired
  private MongoMappingContext mongoMappingContext;

  @Bean
  public MappingMongoConverter mappingMongoConverter(
    MongoDatabaseFactory mongoDatabaseFactory,
    MongoMappingContext mongoMappingContext
  ) {
    DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
    MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
    mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null)); //_class 제거
    return mappingConverter;
  }
}
