package com.yshukevich.pharmacy.usercervice.validation.annotations

import com.yshukevich.pharmacy.usercervice.model.dictionary.CustomerTypeEnum
import org.jooq.generated.tables.pojos.UserProfile
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CustomerTypeValidator : ConstraintValidator<ValidCustomerType, UserProfile> {
    override fun isValid(user: UserProfile, context: ConstraintValidatorContext): Boolean {
        return validateCustomerType(user.customerType)
    }

    private fun validateCustomerType(customerType: String?): Boolean {
        var isValid = false
        for (v in CustomerTypeEnum.values()) {
            if (v.value == customerType) isValid = true
        }
        return isValid
    }

}
