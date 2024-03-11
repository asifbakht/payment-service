package com.microservice.payment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

// @SpringBootTest
class PaymentApplicationTests {

	@Test
	void contextLoads() {
		PaymentApplication.main(new String[] {});
		assertThat("1", equalTo("1"));
	}

}
