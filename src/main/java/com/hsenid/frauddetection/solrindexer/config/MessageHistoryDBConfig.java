package com.hsenid.frauddetection.solrindexer.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class MessageHistoryDBConfig {

    @Autowired
    private Environment env;

    @Primary
    @Bean()
    @ConfigurationProperties()
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("spring.messagehistory.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.messagehistory.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.messagehistory.datasource.password"));
        return dataSource;
    }
}
