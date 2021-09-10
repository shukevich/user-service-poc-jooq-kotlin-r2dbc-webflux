package com.yshukevich.pharmacy.usercervice.service

import com.yshukevich.pharmacy.usercervice.mapper.UserMapper
import com.yshukevich.pharmacy.usercervice.model.UserCredentialsModel
import com.yshukevich.pharmacy.usercervice.model.UserProfileModel
import com.yshukevich.pharmacy.usercervice.model.dictionary.CustomerTypeEnum
import com.yshukevich.pharmacy.usercervice.repository.UserRepository
import com.yshukevich.pharmacy.usercervice.validation.exception.UserCredentialsAlreadyExistsException
import com.yshukevich.pharmacy.usercervice.validation.exception.UserCredentialsNotFoundException
import com.yshukevich.pharmacy.usercervice.validation.exception.UserProfileNotFoundException
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


interface UserService {
    suspend fun getUserProfileById(id: Long): UserProfileModel
    suspend fun isUserProfileExists(userProfileModel: UserProfileModel): Boolean
    suspend fun isUserCredentialsExists(userCredentialsModel: UserCredentialsModel): Boolean
    suspend fun isUserCredentialsExists(id: Long): Boolean
    suspend fun getUserCredentialsById(id: Long): UserCredentialsModel
    suspend fun getUserCredentialsByPhone(phone: String): UserCredentialsModel
    suspend fun createUserCredentials(credentialsModel: UserCredentialsModel): UserCredentialsModel
    suspend fun updateUserProfile(userProfileModel: UserProfileModel): UserProfileModel
    suspend fun updateUserCredentials(credentialsModel: UserCredentialsModel): UserCredentialsModel
    suspend fun deleteUser(id: Long): Int
}

@Service
class DefaultUserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserService {

    override suspend fun getUserProfileById(id: Long): UserProfileModel {
        val credentials = userRepository.getUserCredentialsById(id)
        val profile = userRepository.getUserProfileById(id)
        val profileAndCreds = Mono.zip(profile, credentials)
        return profileAndCreds
            .switchIfEmpty(Mono.error(UserProfileNotFoundException("User profile with id $id not found!")))
            .map { userMapper.mapUserProfileModel(it.t1, it.t2) }
            .awaitSingle()
    }

    override suspend fun getUserCredentialsById(id: Long): UserCredentialsModel {
        return userRepository
            .getUserCredentialsById(id)
            .switchIfEmpty(Mono.error(UserCredentialsNotFoundException("User credentials with id $id not found!")))
            .map {
                userMapper.mapUserCredentialsModel(it)
            }
            .awaitSingle()
    }

    override suspend fun getUserCredentialsByPhone(phone: String): UserCredentialsModel {
        return userRepository
            .getUserCredentialsByPhone(phone)
            .switchIfEmpty(Mono.error(UserCredentialsNotFoundException("User credentials with phone $phone not found!")))
            .map {
                userMapper.mapUserCredentialsModel(it)
            }
            .awaitSingle()
    }

    override suspend fun createUserCredentials(credentialsModel: UserCredentialsModel): UserCredentialsModel {
        if (isUserCredentialsExists(credentialsModel))
            throw UserCredentialsAlreadyExistsException("User credentials with phone ${credentialsModel.phone} already exists")
        userRepository
            .createUserCredentials(credentialsModel)
            .awaitSingle()
        val credentials = getUserCredentialsByPhone(credentialsModel.phone)
        userRepository.createUserProfile(credentials.id!!, CustomerTypeEnum.RETAIL.value)
            .awaitSingle()
        return credentials
    }

    override suspend fun updateUserProfile(userProfileModel: UserProfileModel): UserProfileModel {
        if (!isUserProfileExists(userProfileModel))
            throw UserProfileNotFoundException("User profile with id ${userProfileModel.id} not found")
        userRepository.updateUserCredentials(userMapper.mapUserCredentialsModel(userProfileModel))
        userRepository.updateUserProfile(userMapper.mapUserProfile(userProfileModel))
        return getUserProfileById(userProfileModel.id)
    }

    override suspend fun updateUserCredentials(credentialsModel: UserCredentialsModel): UserCredentialsModel {
        if (!isUserCredentialsExists(credentialsModel))
            throw UserCredentialsNotFoundException("User credentials with id ${credentialsModel.id} not found")
        userRepository.updateUserCredentials(credentialsModel)
            .awaitSingle()
        return getUserCredentialsByPhone(credentialsModel.phone)
    }

    override suspend fun deleteUser(id: Long): Int {
        if (isUserCredentialsExists(id))
            throw UserCredentialsNotFoundException("User credentials with id $id not found")
        return userRepository
            .deleteUserCredentials(id)
            .awaitSingle()
    }

    override suspend fun isUserProfileExists(userProfileModel: UserProfileModel): Boolean {
        return userRepository
            .isUserProfileExists(userProfileModel)
            .awaitSingle()
    }

    override suspend fun isUserCredentialsExists(userCredentialsModel: UserCredentialsModel): Boolean {
        return userRepository
            .isUserCredentialsExists(userCredentialsModel)
            .awaitSingle()
    }

    override suspend fun isUserCredentialsExists(id: Long): Boolean {
        return userRepository
            .isUserCredentialsExists(id)
            .awaitSingle()
    }
}
