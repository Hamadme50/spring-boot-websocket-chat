package com.app.chatlinks.bigData;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.ServerSettings;
import com.mongodb.connection.SocketSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class MongoConfig  {

    @Bean
    public MongoClient mongoClient() {
        ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
                .minSize(5)
                .maxSize(5)
                .maxWaitTime(200, TimeUnit.SECONDS)
                .maxConnectionIdleTime(60, TimeUnit.SECONDS)
                .build();

        SocketSettings socketSettings = SocketSettings.builder()
                .connectTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .build();

        ServerSettings serverSettings = ServerSettings.builder()
                .heartbeatFrequency(10, TimeUnit.SECONDS)
                .minHeartbeatFrequency(1, TimeUnit.SECONDS)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToConnectionPoolSettings(builder -> builder.applySettings(connectionPoolSettings))
                .applyToSocketSettings(builder -> builder.applySettings(socketSettings))
                .applyToServerSettings(builder -> builder.applySettings(serverSettings))
                .applyConnectionString(new ConnectionString("mongodb://localhost:27017/chatlinks"))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "chatlinks");
    }
}
