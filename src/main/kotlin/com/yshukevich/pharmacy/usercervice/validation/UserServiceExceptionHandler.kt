package com.yshukevich.pharmacy.usercervice.validation

import com.yshukevich.pharmacy.usercervice.validation.exception.UserCredentialsNotFoundException
import com.yshukevich.pharmacy.usercervice.validation.exception.UserProfileNotFoundException
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain

@Component
class UserServiceExceptionHandler {

    @Bean
    fun credentialsNotFoundToStatusNotFound(): WebFilter {
        return WebFilter { exchange: ServerWebExchange, next: WebFilterChain ->
            next.filter(exchange)
                .onErrorResume(UserCredentialsNotFoundException::class.java) {
                    val response: ServerHttpResponse = exchange.response
                    response.statusCode = HttpStatus.NOT_FOUND
                    response.setComplete()
                }
        }
    }

    @Bean
    fun profileNotFoundToStatusNotFound(): WebFilter {
        return WebFilter { exchange: ServerWebExchange, next: WebFilterChain ->
            next.filter(exchange)
                .onErrorResume(UserProfileNotFoundException::class.java) {
                    val response: ServerHttpResponse = exchange.response
                    response.statusCode = HttpStatus.NOT_FOUND
                    response.setComplete()
                }
        }
    }
}
