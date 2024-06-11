package com.project.stock_data_ai.api;

import com.project.stock_data_ai.functions.GetStockNews;
import com.project.stock_data_ai.functions.GetStockPrice;
import com.project.stock_data_ai.functions.Request;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;

@RestController
public class StockBotController {

    private final ChatClient chatClient;

    public StockBotController (ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Bean
    @Description("Get the stock price")
    public Function<Request, GetStockPrice.Response> getStockPrice() {
        return new GetStockPrice();
    }

    @Bean
    @Description("Get the stock news")
    public Function<Request, GetStockNews.Response> getStockNews() {
        return new GetStockNews();
    }



    @PostMapping("/ask")
    public String getStockBotResponse(@RequestBody Question question) {
       return chatClient.prompt()
               .functions("getStockPrice", "getStockNews")
               .user("You are a stock market expert." + question.question())
               .call()
               .content();
    }
}
