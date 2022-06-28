package com.scb.demo.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import com.scb.demo.exception.TradeException;
import com.scb.demo.model.Trade;

@TestMethodOrder(OrderAnnotation.class)
public class TradeUtilTest {

	private static TradeUtil tradeUtil;

	@BeforeAll
	public static void setUp() {
		System.out.println("Before class executed");
		tradeUtil = TradeUtil.getInstance();
	}

	@Test
	@Order(1)
	public void testAddTrade() throws TradeException {
		Trade trade = new Trade("T1", 5, "CPT1", "B5", "2022-11-10", "2022-06-27", "N");
		Trade newTrade = tradeUtil.addTrade(trade);
		assertEquals(4, newTrade.getVersion());

	}

	@Test
	@Order(2)
	public void testAddTradeWithException() {
		System.out.println("In Trade with exception");
		Trade trade = new Trade("T1", 1, "CPT1", "B5", "2022-11-10", "2022-06-27", "N");
		TradeException thrown = Assertions.assertThrows(TradeException.class, () -> {
			tradeUtil.addTrade(trade);
		}, "TradeException was expected");
		assertTrue(thrown.getMessage().contains("Trade has less version than the TradeBook."));

	}
	
	@Test
	@Order(3)
	public void testAddTradeWithMaturityDateException() {
		Trade trade = new Trade("T1", 4, "CPT1", "B5", "2022-05-10", "2022-06-27", "N");
		TradeException thrown = Assertions.assertThrows(TradeException.class, () -> {
			tradeUtil.addTrade(trade);
		}, "TradeException was expected");
		assertTrue(thrown.getMessage().contains("Maturity Date cannot be less than current date"));

	}
	
	@Test
	@Order(4)
	public void testUpdateTrade() throws TradeException {
		Trade trade = new Trade("T1", 5, "CPT3", "B5", "2022-11-10", "2022-06-27", "N");
		Trade newTrade = tradeUtil.addTrade(trade);
		assertEquals("CPT3", newTrade.getCounterPartyId());

	}
	
	@Test
	@Order(5)
	public void testDeleteTrade() throws TradeException {
		Trade trade = new Trade("T1", 5, "CPT3", "B5", "2022-11-10", "2022-06-27", "N");
		Trade newTrade = tradeUtil.addTrade(trade);
		tradeUtil.deleteTrade(newTrade);
		assertFalse(tradeUtil.getAllTrades().contains(newTrade));

	}
}
