package com.janey668.mybatisplus;

import com.janey668.mybatisplus.mapper.UserMapper;
import com.janey668.mybatisplus.po.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

    @Test
    public void testMybatisPlusInterceptor(){
        //1、查询用户信息
        User user = userMapper.selectById(1L);
        //2、修改用户信息
        user.setName("zhangsan");
        user.setEmail("963330213@qq.com");
        //3、执行更新操作
        userMapper.updateById(user);
    }

    //测试乐观锁失败
    @Test
    public void testMybatisPlusInterceptor2(){
        //1、查询用户信息
        User user = userMapper.selectById(1L);
        //2、修改用户信息
        user.setName("zhangsan1");
        user.setEmail("963330213@qq.com");

        //模拟另外一个线程执行了插队操作
        User user2 = userMapper.selectById(1L);
        user2.setName("zhangsan2");
        user2.setEmail("963330213@qq.com");
        int count1 = userMapper.updateById(user2);
        Assert.assertEquals(1, count1);

        //自旋锁来尝试多次提交
        int count2 = userMapper.updateById(user2);//如果没有乐观锁就会覆盖插队线程的值！
        Assert.assertEquals(0, count2);
    }

}