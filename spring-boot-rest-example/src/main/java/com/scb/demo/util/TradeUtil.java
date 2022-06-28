package com.scb.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.scb.demo.exception.TradeException;
import com.scb.demo.model.Trade;

public class TradeUtil {
	
	static List<Trade> tradeBook = new ArrayList<>();
	
    private static TradeUtil _instance = null;

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	
   private TradeUtil(){
	   initTrade();
	   updateExpiredFlag();
   }
	
   public static synchronized TradeUtil getInstance() {
	   if(_instance == null) {
		   _instance = new TradeUtil();
	   }
	   return _instance;
   }
	
	private void initTrade() {
		if(tradeBook.size() == 0) {
			tradeBook.add(new Trade("T1", 1, "CPT1", "B1", "2023-01-07", "2022-01-31", "N"));
			tradeBook.add(new Trade("T1", 2, "CPT2", "B2", "2023-02-17", "2022-01-31", "N"));
			tradeBook.add(new Trade("T2", 1, "CPT1", "B1", "2021-03-19", "2022-01-31", "N"));
			tradeBook.add(new Trade("T2", 2, "CPT1", "B2", "2023-01-07", "2022-01-31", "N"));
			tradeBook.add(new Trade("T1", 3, "CPT1", "B1", "2023-01-07", "2022-03-31", "N"));
		}
	}
	
	private void updateExpiredFlag() {
		List<Trade> expiredTrades = tradeBook.stream().filter(x -> convertToDate(x.getMaturityDate()).compareTo(new Date()) < 0).collect(Collectors.toList());
		if(expiredTrades.size() > 0) {
			expiredTrades.forEach(x-> x.setExpired("Y"));
		}
	}
	
    private Date convertToDate(String date){
    	try {
			return df.parse(date);
		} catch (ParseException e) {
			System.out.println("Parse Exception "+e);
			return null;
		}	
	}
	
	public Trade addTrade(Trade trade) throws TradeException{
		initTrade();
		if(trade != null) {
			String tradeId = trade.getTradeId();
			boolean isTradeUpdated = false;
			
			int maxVersion = 0;
			List<Trade> tradeByIds = tradeBook.stream().filter(x-> x.getTradeId().equals(tradeId)).collect(Collectors.toList());
			if(tradeByIds.size() > 0) {
				Trade maxVersionForTrade = tradeByIds.stream().max(Comparator.comparingInt(Trade::getVersion)).get();
				maxVersion = maxVersionForTrade.getVersion();
			    isTradeUpdated = validateVersion(maxVersionForTrade, trade);
			    if(trade.getMaturityDate() != null)
			    	validateMaturityDate(trade);
			 }
			  if(!isTradeUpdated) {
				 trade.setVersion(maxVersion+1);
			     tradeBook.add(trade);
			    System.out.println("Trade sucessfully added");
			  }else {
				  System.out.println("Trade successfully updated");
			  }
			  
			}
		return trade;	

	}
	
	public Trade updateTrade(Trade trade){
		if(trade != null) {
		    Trade tradeById = findTradeByIdAndVersion(trade.getTradeId(), trade.getVersion());
			if(tradeById != null) {
				tradeById.setCounterPartyId(trade.getCounterPartyId() != null ? trade.getCounterPartyId() :tradeById.getCounterPartyId());
				tradeById.setMaturityDate(trade.getMaturityDate() != null ? trade.getMaturityDate() :tradeById.getMaturityDate());
				tradeById.setBookId(trade.getBookId() != null ? trade.getBookId() :tradeById.getBookId());
				return tradeById;
			}
				addTrade(trade);		
		}
		return trade;
	}
	
	public Trade deleteTrade(Trade trade) {
		if(trade != null) {
		    Trade tradeById = findTradeByIdAndVersion(trade.getTradeId(), trade.getVersion());
			if(tradeById != null) {
				tradeBook.remove(tradeById);
			}
		}
		return trade;
	}
	
	public List<Trade> getAllTrades() {
		List<Trade> trades = new ArrayList<>();
		trades.addAll(tradeBook);
		return trades;
	}
	
	public List<Trade> getTradeById(String tradeId) {
		List<Trade> tradeById = new ArrayList<>();
		if(tradeBook != null) {
		  tradeById.addAll(tradeBook.stream().filter(x-> x.getTradeId().equals(tradeId)).collect(Collectors.toList()));
		}
		return tradeById;
	}
	
	public Trade getLatestTrade(String tradeId, int versionId) {
		return findTradeByIdAndVersion(tradeId, versionId);
	}
	
	private  Boolean validateVersion(Trade maxVersionForTrade, Trade trade) throws TradeException{
		boolean tradeUpdated= Boolean.FALSE;
		if (maxVersionForTrade.getVersion() > trade.getVersion()) {
			throw new TradeException("Trade has less version than the TradeBook.");
		}else if(maxVersionForTrade.getVersion() == trade.getVersion()) {
			maxVersionForTrade.setCounterPartyId(trade.getCounterPartyId());
			maxVersionForTrade.setBookId(trade.getBookId());
			maxVersionForTrade.setMaturityDate(trade.getMaturityDate());
			maxVersionForTrade.setCreatedDate(trade.getCreatedDate());
			tradeUpdated = Boolean.TRUE;
		}
		return tradeUpdated;
	}
	
	private void validateMaturityDate(Trade trade) throws TradeException{
		 if (convertToDate(trade.getMaturityDate()).compareTo(new Date()) < 0) {
				throw new TradeException("Maturity Date cannot be less than current date");
			}
	}
	
	private Trade findTradeByIdAndVersion(String tradeId, int versionId) {
		return tradeBook.stream().filter(x-> (x.getTradeId().equals(tradeId) && x.getVersion() == versionId)).findAny().orElse(null);
	}
	
}
