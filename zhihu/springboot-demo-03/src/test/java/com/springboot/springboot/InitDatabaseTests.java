package com.springboot.springboot;

import com.springboot.springboot.dao.questionDAO;
import com.springboot.springboot.dao.userDAO;
import com.springboot.springboot.model.EntityType;
import com.springboot.springboot.model.Question;
import com.springboot.springboot.model.User;
import com.springboot.springboot.service.FollowService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

/**
 * @author WilsonSong
 * @date 2018/6/22
 * 生成测试数据
 */
//@RunWith(SpringRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@Sql("/init-schema.sql")
public class InitDatabaseTests {

	@Autowired
	userDAO uDAO;
	@Autowired
	questionDAO qDAO;
	@Autowired
	FollowService followService;

	@Test
	public void initDatabase() {
		Random random = new Random();

		for(int i=0; i<11; ++i){
			User user = new User();
			user.setHead_url(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d",i));
			user.setPassword("");
			user.setSalt("");
			uDAO.addUser(user);

			//互相关注的测试数据的生成
			for (int j = 0; j< i; ++j){
				followService.follow(j, i, EntityType.ENTITY_USER);
			}

			user.setPassword("XXX");
			uDAO.updatePassword(user);

			Question question = new Question();
			question.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + 100*3600*i);
			question.setCreatedDate(date);
			question.setUserId(i+1);
			question.setTitle(String.format("Title%d",i));
			question.setContent(String.format("dgueuhdpwefpckaweni Content %d",i));
			qDAO.addQuestion(question);


		}

		Assert.assertEquals("XXX",uDAO.selectById(1).getPassword());
		uDAO.deleteUserById(1);
		Assert.assertNull(uDAO.selectById(1));

		System.out.println(qDAO.selectLatestQuestions(0,0,10));

	}

}
