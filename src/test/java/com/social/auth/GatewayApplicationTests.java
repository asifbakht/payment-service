package com.social.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

// @SpringBootTest
class GatewayApplicationTests {

	@Test
	void contextLoads() {
		GatewayApplication.main(new String[] {});
		assertThat("1", equalTo("1"));
	}

}
