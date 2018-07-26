package com.springboot.springboot;

import com.springboot.springboot.model.EntityType;
import com.springboot.springboot.model.User;
import com.springboot.springboot.service.FollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class SpringbootDemo03ApplicationTests {

	@Autowired
	FollowService followService;
	@Test
	public void contextLoads() {
//		for(int i=0; i<11; ++i){
//			//互相关注的测试数据的生成
//			for (int j = 0; j< i; ++j){
//				followService.follow(j, i, EntityType.ENTITY_USER);
//			}
//		}

	}
}
