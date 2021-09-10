package com.yshukevich.pharmacy.userservice.test.unit

import com.yshukevich.pharmacy.usercervice.mapper.UserMapper
import com.yshukevich.pharmacy.usercervice.model.UserCredentialsModel
import com.yshukevich.pharmacy.usercervice.model.UserProfileModel
import com.yshukevich.pharmacy.usercervice.model.dictionary.CustomerTypeEnum
import com.yshukevich.pharmacy.usercervice.repository.DefaultUserRepository
import com.yshukevich.pharmacy.usercervice.service.DefaultUserService
import com.yshukevich.pharmacy.usercervice.validation.exception.UserProfileNotFoundException
import com.yshukevich.pharmacy.userservice.test.unit.util.createUserCredentials
import com.yshukevich.pharmacy.userservice.test.unit.util.createUserMono
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono


internal class UserProfileServiceTest {

    @Test
    fun `existing getUserModel by id returns it`() {
        coEvery { repository.getUserCredentialsById(1) } returns createUserCredentials(id = 1)
        coEvery { repository.getUserProfileById(1) } returns createUserMono(id = 1)
        runBlocking {
            val profile = service.getUserProfileById(1)
            assertEquals(1L, profile.id)
            assertEquals("auser", profile.login)
            assertEquals("user@gmail.com", profile.email)
            assertEquals(CustomerTypeEnum.RETAIL, profile.customerType)
        }
    }

    @Test
    fun `getUserModel by invalid id throws exception`() {
        coEvery { repository.getUserCredentialsById(1) } returns Mono.empty()
        coEvery { repository.getUserProfileById(1) } returns Mono.empty()
        runBlocking {
            assertThrows<UserProfileNotFoundException> { service.getUserProfileById(1) }
        }
    }

    @Test
    fun `getUserModel by invalid credentials id throws exception`() {
        coEvery { repository.getUserCredentialsById(1) } returns Mono.empty()
        coEvery { repository.getUserProfileById(1) } returns createUserMono(id = 1)
        runBlocking {
            assertThrows<UserProfileNotFoundException> { service.getUserProfileById(1) }
        }
    }

    @Test
    fun `getUserModel by invalid profile id throws exception`() {
        coEvery { repository.getUserCredentialsById(1) } returns createUserCredentials(id = 1)
        coEvery { repository.getUserProfileById(1) } returns Mono.empty()
        runBlocking {
            assertThrows<UserProfileNotFoundException> { service.getUserProfileById(1) }
        }
    }

    @Test
    fun `updating of valid user profile is OK`() {
        coEvery { repository.isUserProfileExists(userProfile) } returns Mono.just(true)
        coEvery { repository.updateUserProfile(any()) } returns Mono.just(1)
        coEvery { repository.updateUserCredentials(credentialsUpd) } returns Mono.just(1)
        coEvery { repository.getUserProfileById(1) } returns createUserMono()
        coEvery { repository.getUserCredentialsById(1) } returns createUserCredentials(1)
        runBlocking {
            val profile = service.updateUserProfile(userProfile)
            assertEquals(1L, profile.id)
        }
    }
}

private val repository = mockk<DefaultUserRepository>()
private val userMapper = UserMapper()
private val service = DefaultUserService(repository, userMapper)
private val credentialsUpd = UserCredentialsModel(1, "user@gmail.com", "+123456799", "auser", "defaultPassword")
private val userProfile = UserProfileModel(
    1L,
    "user@gmail.com",
    "auser",
    "+123456799",
    "UserName1",
    "UserSurname1",
    null,
    null,
    null,
    null,
    null,
    null,
    10.0,
    CustomerTypeEnum.RETAIL
)
