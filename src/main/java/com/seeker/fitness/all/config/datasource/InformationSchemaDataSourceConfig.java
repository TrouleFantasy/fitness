package com.seeker.fitness.all.config.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.seeker.fitness.all.mapper.informationschemamapper"},sqlSessionFactoryRef = InformationSchemaDataSourceConfig.SQL_SESSION_FACTORY_NAME)
public class InformationSchemaDataSourceConfig {
    public static final String SQL_SESSION_FACTORY_NAME = "informationSchemaSqlSessionFactory";
    @Autowired
    private MybatisProperties properties;
    @Bean(name = "informationSchemaDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.information-schema-datasource")
    public DataSource datasource() {
        return DataSourceBuilder.create().build();
    }
    @Bean(name = "informationschemaTxManagerUser")
    public PlatformTransactionManager txManagerUser() {
        return new DataSourceTransactionManager(datasource());
    }

    @Bean(name = InformationSchemaDataSourceConfig.SQL_SESSION_FACTORY_NAME)
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(datasource());
        sqlSessionFactoryBean.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        return sqlSessionFactoryBean.getObject();
    }

}
