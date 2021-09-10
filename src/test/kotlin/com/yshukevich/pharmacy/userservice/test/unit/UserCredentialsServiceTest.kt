package com.yshukevich.pharmacy.userservice.test.unit

import com.yshukevich.pharmacy.usercervice.mapper.UserMapper
import com.yshukevich.pharmacy.usercervice.model.UserCredentialsModel
import com.yshukevich.pharmacy.usercervice.model.dictionary.CustomerTypeEnum
import com.yshukevich.pharmacy.usercervice.repository.DefaultUserRepository
import com.yshukevich.pharmacy.usercervice.service.DefaultUserService
import com.yshukevich.pharmacy.usercervice.validation.exception.UserCredentialsAlreadyExistsException
import com.yshukevich.pharmacy.usercervice.validation.exception.UserCredentialsNotFoundException
import com.yshukevich.pharmacy.userservice.test.unit.util.createUserCredentials
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono

internal class UserCredentialsServiceTest {

    @Test
    fun `existing getUserCredentials by id returns it`() {
        coEvery { repository.getUserCredentialsById(1) } returns createUserCredentials(id = 1)
        runBlocking {
            val credentials = service.getUserCredentialsById(1)
            assertEquals(1L, credentials.id)
            assertEquals("+123456799", credentials.phone)
        }
    }

    @Test
    fun `existing getUserCredentials by phone returns it`() {
        coEvery { repository.getUserCredentialsByPhone("+123456799") } returns
                createUserCredentials(phone = "+123456799")
        runBlocking {
            val credentials = service.getUserCredentialsByPhone("+123456799")
            assertEquals(1L, credentials.id)
            assertEquals("+123456799", credentials.phone)
            assertEquals("user@gmail.com", credentials.email)
        }
    }

    @Test
    fun `non-existing getUserCredentials by id returns exception`() {
        coEvery { repository.getUserCredentialsById(999) } returns Mono.empty()
        runBlocking {
            assertThrows<UserCredentialsNotFoundException> { service.getUserCredentialsById(999) }
        }
    }

    @Test
    fun `non-existing getUserCredentials by phone returns exception`() {
        coEvery { repository.getUserCredentialsByPhone("999") } returns Mono.empty()
        runBlocking {
            assertThrows<UserCredentialsNotFoundException> { service.getUserCredentialsByPhone("999") }
        }
    }

    @Test
    fun `creating of new creds with old phone is not allowed`() {
        coEvery { repository.isUserCredentialsExists(credentials) } returns Mono.just(true)
        coEvery { repository.createUserCredentials(credentials) } returns Mono.empty()
        coEvery { repository.createUserProfile(1, CustomerTypeEnum.RETAIL.value) } returns Mono.empty()
        runBlocking {
            assertThrows<UserCredentialsAlreadyExistsException> { service.createUserCredentials(credentials) }
        }
    }

    @Test
    fun `creating of new creds with valid new phone is OK`() {
        coEvery { repository.isUserCredentialsExists(credentials) } returns Mono.just(false)
        coEvery { repository.createUserCredentials(credentials) } returns Mono.just(1)
        coEvery { repository.getUserCredentialsByPhone("+123456799") } returns createUserCredentials(1)
        coEvery { repository.createUserProfile(1, CustomerTypeEnum.RETAIL.value) } returns Mono.just(1)
        runBlocking {
            val credentials = service.createUserCredentials(credentials)
            assertEquals(1L, credentials.id)
            assertEquals("+123456799", credentials.phone)
        }
    }

    @Test
    fun `updating of valid creds is OK`() {
        coEvery { repository.isUserCredentialsExists(credentials) } returns Mono.just(true)
        coEvery { repository.updateUserCredentials(credentials) } returns Mono.just(1)
        coEvery { repository.getUserCredentialsByPhone("+123456799") } returns createUserCredentials(1)
        runBlocking {
            val credentials = service.updateUserCredentials(credentials)
            assertEquals(1L, credentials.id)
            assertEquals("+123456799", credentials.phone)
        }
    }

    @Test
    fun `updating of invalid creds throws exception`() {
        coEvery { repository.isUserCredentialsExists(credentials) } returns Mono.just(false)
        runBlocking {
            assertThrows<UserCredentialsNotFoundException> { service.updateUserCredentials(credentials) }
        }
    }
}

private val repository = mockk<DefaultUserRepository>()
private val userMapper = UserMapper()
private val service = DefaultUserService(repository, userMapper)
private val credentials = UserCredentialsModel(
    1,
    "user@gmail.com",
    "+123456799",
    "auser",
    "psswd"
)
