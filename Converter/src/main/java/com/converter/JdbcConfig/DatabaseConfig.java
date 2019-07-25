package com.converter.JdbcConfig;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig {
   @Bean
   public DataSource dataSource(){
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
      dataSource.setUrl("jdbc:mysql://localhost:3306/Imenik?useUnicode=true&\"useJDBCCompliantTimezoneShift=true&useLegac"
      		+ "yDatetimeCode=false&serverTimezone=UTC&useSSL=false");
      dataSource.setUsername( "root" );
      dataSource.setPassword( "ministar" );
      return dataSource;
   }
}


