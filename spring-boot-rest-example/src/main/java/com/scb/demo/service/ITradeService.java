package com.scb.demo.service;

import java.util.List;

import com.scb.demo.exception.TradeException;
import com.scb.demo.model.Trade;

public interface ITradeService {
	
  List<Trade> getAllTrades();
  List<Trade> getTradeById(String tradeId) throws TradeException;
  Trade getLatestTrade(String tradeId, int version) throws TradeException;
  
  Trade addTrade(Trade tradeBook) throws TradeException;
  
  Trade updateTrade(Trade trade) throws TradeException;
  Trade deleteTrade(Trade trade) throws TradeException;
  
}
