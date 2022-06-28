This project is to populate Trades to Datasource. For simplicity, we are not using any datasource and using a util class to do the function.

Compile the file
----------------

mvn clean install

Execute the file
-----------------
java -jar spring-boot-rest-example-0.0.1-SNAPSHOT.jar

APIs
-----

/trades -- To get list of Trades
/latestTrade?tradeId=<<tradeId>>&versionId=<<versionId>> -- To get the latest version details of the Trade
/addTrade {Trade Object} -- Add Trade
/updateTrade {Trade Object} -- Updates Trade if exists else creates new Trade
/deleteTrade {Trade Object} -- Delete Trade if exists
  
Access Api
----------
  
  http://<<serverName>>:8080/<<APIs>>

