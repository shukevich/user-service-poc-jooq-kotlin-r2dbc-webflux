package com.yshukevich.pharmacy.usercervice.repository

import com.yshukevich.pharmacy.usercervice.model.UserCredentialsModel
import com.yshukevich.pharmacy.usercervice.model.UserProfileModel
import com.yshukevich.pharmacy.usercervice.model.dictionary.UserRoleEnum
import org.jooq.DSLContext
import org.jooq.generated.tables.pojos.UserCredentials
import org.jooq.generated.tables.pojos.UserProfile
import org.jooq.generated.tables.references.USER_CREDENTIALS
import org.jooq.generated.tables.references.USER_PROFILE
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface UserRepository {
    fun getUserProfileById(id: Long): Mono<UserProfile>
    fun getUserCredentialsById(id: Long): Mono<UserCredentials>
    fun getUserProfileByPhone(phone: String): Mono<UserProfile>
    fun getUserCredentialsByPhone(phone: String): Mono<UserCredentials>
    fun isUserProfileExists(user: UserProfileModel): Mono<Boolean>
    fun isUserCredentialsExists(userCredentials: UserCredentialsModel): Mono<Boolean>
    fun isUserCredentialsExists(id: Long): Mono<Boolean>
    fun createUserCredentials(userCredentials: UserCredentialsModel): Mono<Int>
    fun updateUserProfile(userProfileModel: UserProfile): Mono<Int>
    fun createUserProfile(id: Long, customerType: String): Mono<Int>
    fun updateUserCredentials(userCredentials: UserCredentialsModel): Mono<Int>
    fun deleteUserCredentials(id: Long): Mono<Int>
}

@Repository
class DefaultUserRepository(private val dsl: DSLContext) : UserRepository {

    override fun getUserProfileById(id: Long): Mono<UserProfile> {
        return Mono.from(
            dsl
                .selectFrom(USER_PROFILE)
                .where(USER_PROFILE.ID.eq(id))
        )
            .map { it.into(UserProfile::class.java) }
    }

    override fun getUserCredentialsById(id: Long): Mono<UserCredentials> {
        return Mono.from(
            dsl.selectFrom(USER_CREDENTIALS)
                .where(USER_CREDENTIALS.ID.eq(id))
                .and(USER_CREDENTIALS.IS_ACTIVE.eq(true))
        )
            .map { it.into(UserCredentials::class.java) }
    }

    override fun getUserProfileByPhone(phone: String): Mono<UserProfile> {
        return Mono.from(
            dsl.select(USER_PROFILE.asterisk())
                .from(USER_PROFILE)
                .leftJoin(USER_CREDENTIALS)
                .on(USER_PROFILE.ID.eq(USER_CREDENTIALS.ID))
                .where(USER_CREDENTIALS.PHONE.eq(phone))
                .and(USER_CREDENTIALS.IS_ACTIVE.eq(true))
        )
            .map { it.into(UserProfile::class.java) }
    }

    override fun getUserCredentialsByPhone(phone: String): Mono<UserCredentials> {
        return Mono.from(
            dsl.selectFrom(USER_CREDENTIALS)
                .where(USER_CREDENTIALS.PHONE.eq(phone))
                .and(USER_CREDENTIALS.IS_ACTIVE.eq(true))
        )
            .map { it.into(UserCredentials::class.java) }
    }

    override fun isUserProfileExists(user: UserProfileModel): Mono<Boolean> {
        return Mono.from(
            dsl.selectCount()
                .from(USER_PROFILE)
                .leftJoin(USER_CREDENTIALS)
                .on(USER_PROFILE.ID.eq(USER_CREDENTIALS.ID))
                .where(USER_PROFILE.ID.eq(user.id))
                .and(USER_CREDENTIALS.IS_ACTIVE.eq(true))
        )
            .map {
                it.into(Boolean::class.java)
            }
    }

    override fun isUserCredentialsExists(userCredentials: UserCredentialsModel): Mono<Boolean> {
        return Mono.from(
            dsl.selectCount()
                .from(USER_CREDENTIALS)
                .where(
                    USER_CREDENTIALS.PHONE.eq(userCredentials.phone)
                        .or(USER_CREDENTIALS.LOGIN.eq(userCredentials.login))
                        .or(USER_CREDENTIALS.EMAIL.eq(userCredentials.email))
                        .or(USER_CREDENTIALS.PASSWORD.eq(userCredentials.password))
                )
                .and(USER_CREDENTIALS.IS_ACTIVE.eq(true))
        )
            .map {
                it.into(Boolean::class.java)
            }
    }

    override fun isUserCredentialsExists(id: Long): Mono<Boolean> {
        return Mono.from(
            dsl.selectCount()
                .from(USER_CREDENTIALS)
                .where(USER_CREDENTIALS.ID.eq(id))
                .and(USER_CREDENTIALS.IS_ACTIVE.eq(true))
        )
            .map {
                it.into(Boolean::class.java)
            }
    }

    override fun createUserCredentials(userCredentials: UserCredentialsModel): Mono<Int> {
        return Mono.from(
            dsl.insertInto(USER_CREDENTIALS)
                .set(USER_CREDENTIALS.CREATED, LocalDateTime.now())
                .set(USER_CREDENTIALS.IS_ACTIVE, true)
                .set(USER_CREDENTIALS.USER_ROLE, UserRoleEnum.CUSTOMER.value)
                .set(USER_CREDENTIALS.PHONE, userCredentials.phone)
                .set(USER_CREDENTIALS.PASSWORD, userCredentials.password)
                .set(USER_CREDENTIALS.EMAIL, userCredentials.email)
                .set(USER_CREDENTIALS.LOGIN, userCredentials.login)
        )
    }

    override fun updateUserProfile(userProfileModel: UserProfile): Mono<Int> {
        return Mono.from(
            dsl.update(USER_PROFILE)
                //FIXME possible issues!
                .set(dsl.newRecord(USER_PROFILE, userProfileModel))
                .set(USER_PROFILE.UPDATED, LocalDateTime.now())
                .where(USER_PROFILE.ID.eq(userProfileModel.id))
        )
    }

    override fun createUserProfile(id: Long, customerType: String): Mono<Int> {
        return Mono.from(
            dsl.insertInto(USER_PROFILE)
                .set(USER_PROFILE.ID, id)
                .set(USER_PROFILE.CREATED, LocalDateTime.now())
                .set(USER_PROFILE.CUSTOMER_TYPE, customerType)
        )
    }

    override fun updateUserCredentials(userCredentials: UserCredentialsModel): Mono<Int> {
        return Mono.from(
            dsl.update(USER_CREDENTIALS)
                //FIXME possible issues!
                .set(dsl.newRecord(USER_CREDENTIALS, userCredentials))
                .set(USER_CREDENTIALS.UPDATED, LocalDateTime.now())
                .where(USER_CREDENTIALS.ID.eq(userCredentials.id))
        )
    }


    override fun deleteUserCredentials(id: Long): Mono<Int> {
        return Mono.from(
            dsl.update(USER_CREDENTIALS)
                .set(USER_CREDENTIALS.IS_ACTIVE, false)
                .where(USER_CREDENTIALS.ID.eq(id))
        )
    }
}
