package com.buddhi.multidatasource;

import com.buddhi.multidatasource.model.ApiUser;
import com.buddhi.multidatasource.repository.ApiUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultidatasourceApplicationTests {

	@Autowired
	ApiUserRepository apiUserRepository;
	@Autowired
	private ApplicationContext context;


	@Test
	public void contextLoads() {
		assertTrue(context.getBean("mybean") instanceof DataSource);
	}

	@Test
	public void getApiUser() {
		assertEquals(1l, apiUserRepository.findById(1l).get().getId().longValue());
	}
}
