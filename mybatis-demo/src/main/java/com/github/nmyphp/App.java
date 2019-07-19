package com.github.nmyphp;

import com.github.nmyphp.mybatis.demo.User;
import com.github.nmyphp.mybatis.demo.mapper.UserMapper;
import com.google.gson.Gson;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;

public class App {

    public static void main(String[] args) {
        DataSource dataSource = getDataSource();
        SqlSessionFactory sessionFactory = getFactory(dataSource);
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.find(1L);
        System.out.println(new Gson().toJson(user));
    }

    private static DataSource getDataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setInitialSize(1);
        dataSource.setMaxActive(1);
        dataSource.setValidationQuery("select 1");
        dataSource.setTestOnBorrow(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(5000);
        return dataSource;
    }

    public static SqlSessionFactory getFactory(DataSource dataSource) {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.setCacheEnabled(false);
        configuration.addMappers("com.github.nmyphp.mybatis.demo.mapper");
        return new SqlSessionFactoryBuilder().build(configuration);
    }
}
