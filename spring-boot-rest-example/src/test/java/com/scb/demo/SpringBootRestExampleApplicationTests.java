package com.scb.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scb.demo.controller.TradeController;

@SpringBootTest
class SpringBootRestExampleApplicationTests {
	
	@Autowired
	private TradeController controller;
	
	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();

	}

}
