package com.ming.datasource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * springboot集成mybatis的基本入口
 * 1）创建数据源(如果采用的是默认的tomcat-jdbc数据源，则不需要)
 * 2）创建SqlSessionFactory
 * 3）配置事务管理器，除非需要使用事务，否则不用配置
 *
 * @author chenmingcan
 */
@Configuration
@MapperScan("com.jw.elephant.mapper")
public class MyBatisConfig {

    private final Environment environment;

    @Autowired
    public MyBatisConfig(Environment environment) {
        this.environment = environment;
    }

    /**
     * 创建数据源(数据源的名称：方法名可以取为XXXDataSource(),XXX为数据库名称,该名称也就是数据源的名称)
     *
     * @return datasource
     * @throws Exception 异常
     */
    @Bean
    public DataSource masterDataSource() throws Exception {
        Properties props = this.getBaseProperties();
        props.put("driverClassName", environment.getProperty("jdbc.master.driverClassName"));
        props.put("url", environment.getProperty("jdbc.master.url"));
        props.put("username", environment.getProperty("jdbc.master.username"));
        props.put("password", environment.getProperty("jdbc.master.password"));

        return DruidDataSourceFactory.createDataSource(props);
    }

    @Bean
    public DataSource otherDataSource() throws Exception {
        Properties props = this.getBaseProperties();

        props.put("driverClassName", environment.getProperty("jdbc.other.driverClassName"));
        props.put("url", environment.getProperty("jdbc.other.url"));
        props.put("username", environment.getProperty("jdbc.other.username"));
        props.put("password", environment.getProperty("jdbc.other.password"));

        return DruidDataSourceFactory.createDataSource(props);
    }

    /**
     * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
     * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
     */
    @Bean
    @Primary
    public DynamicDataSource dataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                        @Qualifier("otherDataSource") DataSource otherDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(16);

        targetDataSources.put(DatabaseType.master, masterDataSource);
        targetDataSources.put(DatabaseType.other, otherDataSource);

        DynamicDataSource dataSource = new DynamicDataSource();
        // 该方法是AbstractRoutingDataSource的方法
        dataSource.setTargetDataSources(targetDataSources);
        // 默认的datasource设置为myTestDbDataSource
        dataSource.setDefaultTargetDataSource(masterDataSource);

        return dataSource;
    }

    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("masterDataSource") DataSource masterDataSource,
                                               @Qualifier("otherDataSource") DataSource otherDataSource) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(this.dataSource(masterDataSource, otherDataSource));

        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        // 指定配置文件和model基包
        fb.setTypeAliasesPackage(environment.getProperty("mybatis.type-aliases-package"));
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(environment.getProperty("mybatis.mapper-locations")));

        return fb.getObject();
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 获取公共的mysql配置
     *
     * @return props
     */
    private Properties getBaseProperties() {
        Properties props = new Properties();
        props.put("filters", environment.getProperty("datasource.base.filters"));

        props.put("initialSize", environment.getProperty("datasource.base.initialSize"));
        props.put("maxActive", environment.getProperty("datasource.base.maxActive"));
        props.put("minIdle", environment.getProperty("datasource.base.minIdle"));
        props.put("maxWait", environment.getProperty("datasource.base.maxWait"));

        props.put("timeBetweenEvictionRunsMillis", environment.getProperty("datasource.base.timeBetweenEvictionRunsMillis"));
        props.put("minEvictableIdleTimeMillis", environment.getProperty("datasource.base.minEvictableIdleTimeMillis"));

        props.put("validationQuery", environment.getProperty("datasource.base.validationQuery"));
        props.put("testWhileIdle", environment.getProperty("datasource.base.testWhileIdle"));
        props.put("testOnBorrow", environment.getProperty("datasource.base.testOnBorrow"));
        props.put("testOnReturn", environment.getProperty("datasource.base.testOnReturn"));

        props.put("poolPreparedStatements", environment.getProperty("datasource.base.poolPreparedStatements"));
        props.put("maxOpenPreparedStatements", environment.getProperty("datasource.base.maxPoolPreparedStatementPerConnectionSize"));

        return props;
    }

}
