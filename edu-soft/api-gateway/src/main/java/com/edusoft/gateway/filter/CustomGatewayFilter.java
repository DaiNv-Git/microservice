package com.edusoft.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CustomGatewayFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
//        List<String> authorizations = requestHeaders.get(HttpHeaders.AUTHORIZATION);
//        if (!CollectionUtils.isEmpty(authorizations)) {
//            exchange.mutate().request(exchange.getRequest().mutate().header("Test", "Test").build()).build();
//            exchange.mutate().request(exchange.getRequest().mutate()
//                    .header(HttpHeaders.AUTHORIZATION, authorizations.get(0)).build()).build();
//        }
        return chain.filter(exchange);
    }
}
