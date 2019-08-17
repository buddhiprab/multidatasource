package com.buddhi.multidatasource;

import com.buddhi.multidatasource.api.repository.ApiUserRepository;
import com.buddhi.multidatasource.einsure.repository.QuoteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultidatasourceApplicationTests {

	@Autowired
	private ApplicationContext context;
	@Autowired
	ApiUserRepository apiUserRepository;
	@Autowired
	QuoteRepository quoteRepository;

	/*@Test
	public void contextLoads() {
		assertTrue(context.getBean("mybean") instanceof DataSource);
	}*/

	@Test
	public void getApiUser() {
		assertEquals(1l, apiUserRepository.findById(1l).get().getId().longValue());
	}

	@Test
	public void getQuote() {
		assertEquals(1l, quoteRepository.findById(1l).get().getId().longValue());
	}
}
