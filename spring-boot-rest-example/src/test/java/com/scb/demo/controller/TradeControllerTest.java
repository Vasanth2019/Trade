package com.scb.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.demo.model.Trade;
import com.scb.demo.service.ITradeService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TradeController.class)
public class TradeControllerTest {
 
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ITradeService tradeService;
	 
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM‰dd'T'hhmmss'Z'");

	
	@Test
	public void testGetAllTrades() throws Exception {

		Trade trade1 = new Trade("T1", 1, "CPT1", "B1", "2022-12-31", "2022-06-26", "N");
		Trade trade2 = new Trade("T2", 2, "CPT1", "B1", "2022-11-30","2022-06-12", "N");

		List<Trade> tradeList = new ArrayList<>();
		tradeList.add(trade1);
		tradeList.add(trade2);
		
		Mockito.when(tradeService.getAllTrades()).thenReturn(tradeList);
		
		
		String URI = "/trades";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				URI).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		String expectedJson = this.mapToJson(tradeList);
		String outputInJson = result.getResponse().getContentAsString();
		assertThat(outputInJson).isEqualTo(expectedJson);
	}
	
	@Test
	public void testGetLatestTrade()  throws Exception{
		Trade trade1 = new Trade("T1", 1, "CPT1", "B1", "2022-12-31", "2022-06-26", "N");

		
		Mockito.when(tradeService.getLatestTrade(Mockito.anyString(),Mockito.anyInt())).thenReturn(trade1);
		String URI = "/latestTrade?tradeId=T3&versionId=1";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				URI).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expectedJson = this.mapToJson(trade1);
		String outputInJson = result.getResponse().getContentAsString();
		assertThat(outputInJson).isEqualTo(expectedJson);

		
	}
	
	@Test
	public void testCreateTrade() throws Exception {
		
		Trade mockTrade = new Trade();
		mockTrade.setTradeId("T1");
		mockTrade.setVersion(1);
		mockTrade.setCounterPartyId("CPT1");
		mockTrade.setBookId("B1");
		mockTrade.setMaturityDate("2022-12-31");
		mockTrade.setCreatedDate("2022-06-27");
		mockTrade.setExpired("N");
		
		String inputInJson = this.mapToJson(mockTrade);
		
		String URI = "/createTrade";
		
		Mockito.when(tradeService.addTrade(Mockito.any(Trade.class))).thenReturn(mockTrade);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(URI)
				.accept(MediaType.APPLICATION_JSON).content(inputInJson)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		String outputInJson = response.getContentAsString();
		
		assertThat(outputInJson).isEqualTo(inputInJson);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	/**
	 * Maps an Object into a JSON String. Uses a Jackson ObjectMapper.
	 */
	private String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.setDateFormat(df);
		return objectMapper.writeValueAsString(object);
	}
	
	 
}
