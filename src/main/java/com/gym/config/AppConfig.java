package com.gym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

//spring configuration class
@Configuration
@ComponentScan("com.gym")
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
public class AppConfig {
    private final Environment env;

    public AppConfig(Environment env) {
        this.env = env;
    }

    //creates datasource for postgresql connection
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        return dataSource;
    }

    //factory to create EntityManager
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        //creates container which saves JPA settings
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
                new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("com.gym.models"); //that's where entity classes are

        //connecting to Hibernate
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(adapter);

        //hibernate settings
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"); //generates postgresql
        props.put("hibernate.hbm2ddl.auto", "update"); //auto creation of db
        props.put("hibernate.show_sql", "true"); //shows table in console

        entityManagerFactoryBean.setJpaProperties(props);

        return entityManagerFactoryBean;
    }

    //bean to work with transactions
    @Bean
    public JpaTransactionManager transactionManager() {
        return new JpaTransactionManager(
                entityManagerFactory().getObject()
        );
    }
}
