package com.yshukevich.pharmacy.userservice.test.unit.util

import org.jooq.generated.tables.pojos.UserCredentials
import org.jooq.generated.tables.pojos.UserProfile
import reactor.core.publisher.Mono
import java.math.BigDecimal

fun createUserCredentials(
    id: Long = 1L,
    email: String = "user@gmail.com",
    phone: String = "+123456799",
    login: String = "auser",
    password: String = "psswd",
    userRole: String = "Customer as a person",
    isActive: Boolean = true
):
        Mono<UserCredentials> =
    Mono.just(UserCredentials(id, null, null, phone, login, email, password, userRole, isActive))

fun createUserMono(
    id: Long = 1L,
    name: String = "UserName1",
    surname: String = "UserSurname1",
    country: String = "BY",
    city: String = "Minsk",
    address: String = "Nezavisimosti 115",
    index: String = "220044",
    company: String = "VECTOR",
    personalDiscount: BigDecimal = BigDecimal.TEN,
    customerType: String = "Customer as a person"
): Mono<UserProfile> = Mono.just(
    UserProfile(
        id,
        null,
        null,
        name,
        surname,
        null,
        country,
        city,
        address,
        index,
        company,
        personalDiscount,
        customerType
    )
)

fun createUser(
    id: Long = 1L,
    name: String = "UserName1",
    surname: String = "UserSurname1",
    country: String = "BY",
    city: String = "Minsk",
    address: String = "Nezavisimosti 115",
    index: String = "220044",
    company: String = "VECTOR",
    personalDiscount: BigDecimal = BigDecimal.TEN,
    customerType: String = "Customer as a person"
): UserProfile =
    UserProfile(
        id,
        null,
        null,
        name,
        surname,
        null,
        null,
        null,
        null,
        null,
        null,
        personalDiscount,
        customerType

    )
