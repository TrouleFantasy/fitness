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
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.seeker.fitness.all.mapper.fitnessmapper"},sqlSessionFactoryRef = FitnessDataSourceConfig.SQL_SESSION_FACTORY_NAME)
public class FitnessDataSourceConfig {
    public static final String SQL_SESSION_FACTORY_NAME = "fitnessdatasourceFactory";
    public static final String TX_MANAGER = "txManagerNewsRead";
    @Autowired
    private MybatisProperties properties;

    @Bean(name = "fitnessdataSource")
    @ConfigurationProperties(prefix = "spring.datasource.fitness-datasource")
    @Primary
    public DataSource datasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "fitnessTxManagerUser")
    @Primary
    public PlatformTransactionManager txManagerUser() {
        return new DataSourceTransactionManager(datasource());
    }

    @Bean(name = FitnessDataSourceConfig.SQL_SESSION_FACTORY_NAME)
    @Primary
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(datasource());
        sqlSessionFactoryBean.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        return sqlSessionFactoryBean.getObject();
    }




}
