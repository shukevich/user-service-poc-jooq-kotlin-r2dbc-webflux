package com.yshukevich.pharmacy.usercervice.mapper

import com.yshukevich.pharmacy.usercervice.model.UserCredentialsModel
import com.yshukevich.pharmacy.usercervice.model.UserProfileModel
import com.yshukevich.pharmacy.usercervice.model.dictionary.CustomerTypeEnum
import kotlinx.datetime.LocalDate
import org.jooq.generated.tables.pojos.UserCredentials
import org.jooq.generated.tables.pojos.UserProfile
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class UserMapper {

    fun mapUserProfileModel(user: UserProfile, credentials: UserCredentialsModel): UserProfileModel {
        return UserProfileModel(
            user.id!!,
            credentials.email,
            credentials.login,
            credentials.phone,
            user.name!!,
            user.surname,
            mapDate(user.birthdate),
            user.country,
            user.city,
            user.address,
            user.index,
            user.company,
            user.personalDiscount?.toDouble(),
            mapCustomerTypeEnum(user.customerType!!),
        )
    }

    fun mapUserProfileModel(user: UserProfile, credentials: UserCredentials): UserProfileModel {
        return UserProfileModel(
            user.id!!,
            credentials.email,
            credentials.login,
            credentials.phone!!,
            user.name!!,
            user.surname,
            mapDate(user.birthdate),
            user.country,
            user.city,
            user.address,
            user.index,
            user.company,
            user.personalDiscount?.toDouble(),
            mapCustomerTypeEnum(user.customerType!!),
        )
    }

    fun mapUserProfile(userProfileModel: UserProfileModel): UserProfile {
        return UserProfile(
            id = userProfileModel.id,
            name = userProfileModel.name,
            surname = userProfileModel.surname,
            birthdate = mapDate(userProfileModel.birthdate),
            country = userProfileModel.country,
            city = userProfileModel.city,
            address = userProfileModel.address,
            index = userProfileModel.index,
            company = userProfileModel.company,
            personalDiscount = userProfileModel.personalDiscount?.let { BigDecimal.valueOf(it) },
            customerType = mapCustomerType(userProfileModel.customerType)
        )
    }

    fun mapUserCredentialsModel(userProfileModel: UserProfileModel): UserCredentialsModel {
        return UserCredentialsModel(
            userProfileModel.id,
            userProfileModel.email,
            userProfileModel.phone,
            userProfileModel.login
        )
    }

    fun mapUserCredentialsModel(userCredentials: UserCredentials): UserCredentialsModel {
        return UserCredentialsModel(
            userCredentials.id!!,
            userCredentials.email,
            userCredentials.phone!!,
            userCredentials.login,
            userCredentials.password!!
        )
    }

    private fun mapCustomerTypeEnum(customerType: String): CustomerTypeEnum {
        return CustomerTypeEnum.values().find { it.value == customerType } ?: CustomerTypeEnum.NONE
    }

    private fun mapCustomerType(customerType: CustomerTypeEnum): String {
        return customerType.value
    }

    private fun mapDate(date: LocalDate?): java.time.LocalDate? {
        return if (date != null) {
            java.time.LocalDate.of(date.year, date.monthNumber, date.dayOfMonth)
        } else null
    }

    private fun mapDate(date: java.time.LocalDate?): LocalDate? {
        return if (date != null) {
            LocalDate(date.year, date.monthValue, date.dayOfMonth)
        } else null
    }
}
