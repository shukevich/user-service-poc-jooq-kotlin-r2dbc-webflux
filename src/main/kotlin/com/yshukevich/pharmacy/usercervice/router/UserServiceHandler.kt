package com.yshukevich.pharmacy.usercervice.router

import com.yshukevich.pharmacy.usercervice.model.UserCredentialsModel
import com.yshukevich.pharmacy.usercervice.model.UserProfileModel
import com.yshukevich.pharmacy.usercervice.service.UserService
import com.yshukevich.pharmacy.usercervice.validation.UserServiceValidationHandler
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.Explode
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.enums.ParameterStyle
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Service
class UserServiceHandler(
    val userService: UserService,
    val userServiceValidator: UserServiceValidationHandler
) {

    @Operation(
        operationId = "getUserCredentialsById",
        method = "GET",
        parameters = [
            Parameter(
                name = "id",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )]
    )
    suspend fun getUserCredentialsById(request: ServerRequest): ServerResponse {
        val id = userServiceValidator.validatePathIdVariable(request)
        val user = userService.getUserCredentialsById(id)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(user)
    }

    @Operation(
        operationId = "getUserProfileById",
        method = "GET",
        parameters = [
            Parameter(
                name = "id",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )]
    )
    suspend fun getUserProfileById(request: ServerRequest): ServerResponse {
        val id = userServiceValidator.validatePathIdVariable(request)
        val user = userService.getUserProfileById(id)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(user)
    }

    @Operation(
        operationId = "createUserCredentials",
        requestBody = RequestBody(
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = UserCredentialsModel::class)
            )]
        )
    )
    suspend fun createUserCredentials(request: ServerRequest): ServerResponse {
        val newUser = userServiceValidator.validateUserCredentials(request)
        val user = userService.createUserCredentials(newUser)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(user)
    }

    @Operation(
        operationId = "updateUserProfile",
        method = "PUT",
        requestBody = RequestBody(
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = UserProfileModel::class)
            )]
        )
    )
    suspend fun updateUserProfile(request: ServerRequest): ServerResponse {
        val updatedUser = userServiceValidator.validateUserModel(request)
        val user = userService.updateUserProfile(updatedUser)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(user)
    }

    @Operation(
        operationId = "updateUserCredentials",
        method = "PUT",
        requestBody = RequestBody(
            content = [Content(
                mediaType = "application/json",
                schema = Schema(
                    implementation = UserCredentialsModel::class
                )
            )]
        )
    )
    suspend fun updateUserCredentials(request: ServerRequest): ServerResponse {
        val updatedUser = userServiceValidator.validateUserCredentials(request)
        val user = userService.updateUserCredentials(updatedUser)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(user)
    }
}

