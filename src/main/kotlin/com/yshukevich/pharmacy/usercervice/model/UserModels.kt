package com.yshukevich.pharmacy.usercervice.model

import com.yshukevich.pharmacy.usercervice.model.dictionary.CustomerTypeEnum
import com.yshukevich.pharmacy.usercervice.validation.annotations.ValidCustomerType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.LocalDateComponentSerializer
import kotlinx.serialization.Serializable
import javax.validation.constraints.*

@Serializable
data class UserProfileModel(
    @NotNull
    val id: Long,
    @Email
    val email: String?,
    val login: String?,
    val phone: String,
    @NotNull
    val name: String,
    val surname: String?,
    @Past
    @Serializable(with = LocalDateComponentSerializer::class)
    val birthdate: LocalDate?,
    val country: String?,
    val city: String?,
    val address: String?,
    @Digits(integer = 6, fraction = 0)
    val index: String?,
    val company: String?,
    @Digits(integer = 2, fraction = 2)
    @Max(99)
    @PositiveOrZero
    val personalDiscount: Double?,
    @ValidCustomerType
    val customerType: CustomerTypeEnum
)

@Serializable
data class UserCredentialsModel(
    val id: Long?,
    @Email
    val email: String?,
    @NotNull
    val phone: String,
    val login: String?,
    @Size(min = 6)
    @NotNull
    val password: String = "defaultPassword"
)






