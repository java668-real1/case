package com.janey668.mybatis.janey668mybatisdemo.test;

import com.janey668.mybatis.janey668mybatisdemo.entity.SysUser;
import com.janey668.mybatis.janey668mybatisdemo.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class MybatisTest {

    public static void main(String[] args) throws IOException {
        //1. 读取mybatis-config.xml 文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");

        //2. 构建SqlSessionFactory(创建了DefaultSqlSessionFactory)
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //3. 打开SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //4. 获取Mapper 接口对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        //5. 获取mapper 接口对象的方法操作数据库
        List<SysUser> sysUsers = mapper.selectByIdList(Arrays.asList(1L));
        System.out.println("查询结果为：" + sysUsers.size());
    }
}
