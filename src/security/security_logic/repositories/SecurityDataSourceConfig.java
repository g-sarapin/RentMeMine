package security.security_logic.repositories;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "security.security_logic",             //package name is security.security_logic
        mongoTemplateRef = "securityMongoTemplate")
public class SecurityDataSourceConfig {

    @Primary
    @Bean(name = "securityMongoProperties")
    @ConfigurationProperties("security.spring.data.mongodb")                   //lines 10-12 from application.properties
    public MongoProperties securityDataSourceProperties() {
        return new MongoProperties();
    }

    @Primary
    @Bean(name = "securityMongoDatabaseFactory")
    public MongoDatabaseFactory securityMongoDataBaseFactory(@Qualifier("securityMongoProperties") MongoProperties dataSourceProperties){
        return new SimpleMongoClientDatabaseFactory(
                MongoClients.create("mongodb://"+dataSourceProperties.getHost()+":"+dataSourceProperties.getPort()), //*****
                dataSourceProperties.getDatabase());
    }

    @Primary
    @Bean(name = "securityMongoTemplate")
    public MongoTemplate securityMongoTemplate(@Qualifier("securityMongoDatabaseFactory") MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }
}