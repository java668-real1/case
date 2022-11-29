package com.janey668.mybatis.janey668mybatisdemo;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataSourceTest {
    @Autowired
    DataSource dataSource;

    @Test
    public void contextLoads() {
        // 获取配置的数据源
        // 查看配置数据源信息
        System.out.println(dataSource);
        System.out.println(dataSource.getClass().getName());
        //执行SQL,输出查到的数据
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        List<?> resultList = jdbcTemplate.queryForList("select * from test");
//        System.out.println("===>>>>>>>>>>>" + JSON.toJSONString(resultList));
    }

    @Test
    public void testConnectTimeout() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setInitialSize(5);
        dataSource.setUrl("jdbc:mysql://81.69.220.64:3306/test?connectTimeout=5");
        dataSource.setUsername("root");
        dataSource.setPassword("QINg0201$");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.init();//初始化，底层通过mysql-connector-java建立数据库连接
    }

    @Test
    public void testSocketTimeout() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setInitialSize(5);
        dataSource.setUrl("jdbc:mysql://81.69.220.64:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("QINg0201$");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.init();//初始化，底层通过mysql-connector-java建立数据库连接
        Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement("select sleep(10)");
        ps.executeQuery();
    }
}
