package com.yshukevich.pharmacy.usercervice.router

import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.function.server.coRouter


@Configuration
class UserServiceRouter {

    @Bean
    @RouterOperations(
        RouterOperation(
            path = "/user/{id}",
            method = arrayOf(RequestMethod.GET),
            beanClass = UserServiceHandler::class,
            beanMethod = "getUserCredentialsById"
        ),
        RouterOperation(
            path = "/user/profile/{id}",
            method = arrayOf(RequestMethod.GET),
            beanClass = UserServiceHandler::class,
            beanMethod = "getUserProfileById"
        ),
        RouterOperation(
            path = "/user/create/",
            method = [RequestMethod.POST],
            beanClass = UserServiceHandler::class,
            beanMethod = "createUserCredentials"
        ),
        RouterOperation(
            path = "/user/profile/update/",
            method = arrayOf(RequestMethod.PUT),
            beanClass = UserServiceHandler::class,
            beanMethod = "updateUserProfile"
        ),
        RouterOperation(
            path = "/user/update/",
            method = arrayOf(RequestMethod.PUT),
            beanClass = UserServiceHandler::class,
            beanMethod = "updateUserCredentials"
        )
    )
    fun userRoute(userServiceHandler: UserServiceHandler) = coRouter {
        path("/user")
            .nest {
                GET("/{id}", userServiceHandler::getUserCredentialsById)
                GET("/profile/{id}", userServiceHandler::getUserProfileById)
                POST("/create/", userServiceHandler::createUserCredentials)
                PUT("/profile/update/", userServiceHandler::updateUserProfile)
                PUT("/update/", userServiceHandler::updateUserCredentials)
            }
    }

}
