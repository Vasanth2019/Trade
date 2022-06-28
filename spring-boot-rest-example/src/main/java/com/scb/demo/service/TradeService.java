package com.scb.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.scb.demo.exception.TradeException;
import com.scb.demo.model.Trade;
import com.scb.demo.util.TradeUtil;

@Service
public class TradeService implements ITradeService {
	
	
	TradeUtil tradeUtil =TradeUtil.getInstance();
	
	@Override
	public List<Trade> getAllTrades() {
		return tradeUtil.getAllTrades();
	}

	@Override
	public List<Trade> getTradeById(String tradeId) throws TradeException{
		List<Trade> trades = tradeUtil.getTradeById(tradeId);
		if(trades.size() == 0) {
			throw new TradeException("No Trades associated with id "+tradeId);
		}
		return trades;
	}

	@Override
	public Trade getLatestTrade(String tradeId, int version) throws TradeException{
		Trade trade =tradeUtil.getLatestTrade(tradeId, version);
		if(trade == null) {
			String message = String.format("No Trade associated with TradeId %s and Version %s", tradeId, version);
			throw new TradeException(message);
		}
		return trade;
	}

	

	@Override
	public Trade addTrade(Trade trade) throws TradeException {
		return tradeUtil.addTrade(trade);
		
	}

	@Override
	public Trade updateTrade(Trade trade) throws TradeException{
		return tradeUtil.updateTrade(trade);
	}

	@Override
	public Trade deleteTrade(Trade trade) throws TradeException {
		return tradeUtil.deleteTrade(trade);

	}

}
