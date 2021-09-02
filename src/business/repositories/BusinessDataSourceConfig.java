package business.repositories;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "RentApplication.Business",
        mongoTemplateRef = "businessMongoTemplate")
public class BusinessDataSourceConfig {

    @Bean(name = "businessMongoProperties")
    @ConfigurationProperties("business.spring.data.mongodb")
    public MongoProperties businessDataSourceProperties() {
        return new MongoProperties();
    }

    @Bean(name = "businessMongoDatabaseFactory")
    public MongoDatabaseFactory businessMongoDataBaseFactory(@Qualifier("businessMongoProperties") MongoProperties dataSourceProperties){
        return new SimpleMongoClientDatabaseFactory(
                MongoClients.create(dataSourceProperties.getUri()),
                dataSourceProperties.getDatabase());
    }

    @Bean(name = "businessMongoTemplate")
    public MongoTemplate businessMongoTemplate(@Qualifier("businessMongoDatabaseFactory") MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }
}