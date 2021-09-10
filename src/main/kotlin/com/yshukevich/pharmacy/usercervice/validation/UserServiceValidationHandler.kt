package com.yshukevich.pharmacy.usercervice.validation

import com.yshukevich.pharmacy.usercervice.model.UserCredentialsModel
import com.yshukevich.pharmacy.usercervice.model.UserProfileModel
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.server.ServerWebInputException

@Component
class UserServiceValidationHandler {

    suspend fun validateUserCredentials(request: ServerRequest): UserCredentialsModel {
        return request.bodyToMono<UserCredentialsModel>().awaitFirstOrNull()
            ?: throw ServerWebInputException("UserCredentialsModel must not be null")
    }

    suspend fun validateUserModel(request: ServerRequest): UserProfileModel {
        return request.bodyToMono<UserProfileModel>().awaitFirstOrNull()
            ?: throw ServerWebInputException("UserModel must not be null")
    }

    fun validatePathIdVariable(request: ServerRequest): Long {
        return request.pathVariable("id").toLongOrNull() ?: throw ServerWebInputException("`id` must be numeric")
    }
}
