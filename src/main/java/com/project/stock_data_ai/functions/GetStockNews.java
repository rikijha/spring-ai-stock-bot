package com.project.stock_data_ai.functions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.function.Function;

public class GetStockNews implements Function<Request, GetStockNews.Response> {

    @Value("${rapid.api-key}")
    private String apiKey;

    @Override
    public Response apply(Request request) {
        StockNews stockNews = RestClient.builder()
                .baseUrl("https://real-time-finance-data.p.rapidapi.com/stock-news")
                .defaultHeader("x-rapidapi-key", apiKey)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("symbol", request.stock())
                        .queryParam("language", "en")
                        .build())
                .retrieve()
                .body(StockNews.class);
        return new Response(stockNews.data);
    }

    public record Response(StockNewsData stockNewsData){}

    public record StockNews(StockNewsData data){}

    public record StockNewsData(String symbol, String name, List<News> news){}

    public record News(String article_title, String article_url){}
}
