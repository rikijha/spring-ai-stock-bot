package com.project.stock_data_ai.functions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;

import java.util.function.Function;


public class GetStockPrice implements Function<Request, GetStockPrice.Response> {

    @Value("${rapid.api-key}")
    private String apiKey;


    @Override
    public Response apply(Request request) {
        LiveData liveData = RestClient.builder()
                .baseUrl("https://real-time-finance-data.p.rapidapi.com/stock-overview")
                .defaultHeader("x-rapidapi-key", apiKey)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("symbol", request.stock())
                        .queryParam("language", "en")
                        .build())
                .retrieve()
                .body(LiveData.class);
        return new Response(liveData.data);
    }


    public record Response(StockData data){};

    public record LiveData(StockData data){};

    public record StockData(String symbol, String name, double price) {};

}
