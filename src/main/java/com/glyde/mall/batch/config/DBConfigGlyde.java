package com.glyde.mall.batch.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(Hikari1Properties.class)
@MapperScan(basePackages = "com.glyde.mall.batch.*.mapper", sqlSessionFactoryRef = "sqlSessionFactoryGlyde")
public class DBConfigGlyde {

	@Value("${mybatis.mapper-locations}")
	private String[] mapperLocation;

	@Value("${mybatis.config-location}")
	private String configLocation;

	@Primary
	@Bean(name = "dataSourceGlyde")
	public DataSource dataSource(Hikari1Properties properties) {
		log.info("datasource-glyde-activated");
		return DataSourceFactory.createHikariDataSource(properties);
	}

	@Bean(name = "sqlSessionFactoryGlyde")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSourceGlyde") DataSource dataSource) throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setConfigLocation(pathResolver.getResource(configLocation));
		sessionFactory.setMapperLocations(pathResolver.getResources(mapperLocation[0]));

		log.info("sqlSessionFactory-glyde-activated");

		return sessionFactory.getObject();
	}

	@Bean(name="txManagerGlyde")
	public PlatformTransactionManager txManager(@Qualifier("dataSourceGlyde") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

}
