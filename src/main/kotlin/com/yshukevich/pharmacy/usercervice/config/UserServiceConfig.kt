package com.yshukevich.pharmacy.usercervice.config

import io.r2dbc.spi.ConnectionFactory
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class UserServiceConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI().components(
            Components().addSecuritySchemes(
                "basicScheme",
                SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")
            )
        )
            .info(Info().title("User API").license(License().name("Apache 2.0")))
    }

    @Bean
    fun employeesOpenApi(): GroupedOpenApi? {
        val paths = arrayOf("/user/**")
        return GroupedOpenApi.builder().group("user").pathsToMatch(*paths)
            .build()
    }

}

@Configuration
class JooqConfiguration(connectionFactory: ConnectionFactory) {

    private val connection = connectionFactory

    @Bean
    fun createContext(): DSLContext {
        return DSL.using(connection)
    }
}
