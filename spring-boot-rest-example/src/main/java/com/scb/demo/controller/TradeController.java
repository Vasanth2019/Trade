package com.scb.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.scb.demo.exception.TradeException;
import com.scb.demo.model.Trade;
import com.scb.demo.service.ITradeService;

@RestController
public class TradeController {
	@Autowired
	private ITradeService tradeService;
	//mapping the getProduct() method to /product
	@GetMapping(value = "/trades")
	public ResponseEntity<List<Trade>> getAllTrades() 
	{
	
	//finds all the products
	List<Trade> trades = tradeService.getAllTrades();
	//returns the product list
    return new ResponseEntity<>(trades, HttpStatus.OK);
	//return trades;
	}
	@GetMapping(value = "/latestTrade")
	public ResponseEntity<Trade> getLatestTrade(@RequestParam String tradeId, @RequestParam int versionId){
		Trade trade = null;
		trade = tradeService.getLatestTrade(tradeId, versionId);
		
		return new ResponseEntity<>(trade, HttpStatus.OK);
	}
	
	@PostMapping(value="/createTrade",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public Trade createTrade(@RequestBody Trade trade){
	    return tradeService.addTrade(trade);
	}
	
	@PutMapping(value="/updateTrade",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public Trade updateTrade(@RequestBody Trade trade){
	    return tradeService.updateTrade(trade);
	}
	
	@DeleteMapping(value="/deleteTrade",consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Trade deleteTrade (@RequestBody Trade trade) {
		return tradeService.deleteTrade(trade);
	}
	
	@ExceptionHandler(TradeException.class)
	  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	  public ResponseEntity<String> handleInternalErrorException(
	      TradeException exception
	  ) {
	    return ResponseEntity
	        .status(HttpStatus.INTERNAL_SERVER_ERROR)
	        .body(exception.getMessage());
	  }
	  
	
}
