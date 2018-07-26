package com.springboot.springboot;

import com.springboot.springboot.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 单元测试
 * @author WilsonSong
 * @date 2018/7/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringbootDemo03Application.class)  // 指定spring-boot的启动类
public class LikeServiceTests {
    @Autowired
    LikeService likeService;

//    测试需要准备的数据初始化
    @Before
    public void setUp(){
        System.out.println("before");
    }

//    测试可能增加了一些数据，需要在这里把这些数据清理
    @After
    public void tearDown(){
        System.out.println("after");
    }

    //必须是static的
    @BeforeClass
    public static void beforeClass() {
        System.out.println("beforeClass");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("afterClass");
    }

    @Test
    public void testLike(){
        System.out.println("LikeTest");
        likeService.like(123,1,1);
        Assert.assertEquals(1,likeService.getLikeStatus(123,1,1));

        likeService.dislike(123,1,1);
        Assert.assertEquals(-1,likeService.getLikeStatus(123,1,1));
    }

    //期待抛出一些异常
    @Test(expected = IllegalArgumentException.class)
    public void testException(){
        System.out.println("testException");
        throw new IllegalArgumentException("发生异常");

    }
}
