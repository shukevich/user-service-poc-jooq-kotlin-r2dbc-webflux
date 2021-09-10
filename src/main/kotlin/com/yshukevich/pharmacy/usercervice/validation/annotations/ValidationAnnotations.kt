package com.yshukevich.pharmacy.usercervice.validation.annotations

import javax.validation.Constraint

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [CustomerTypeValidator::class])
annotation class ValidCustomerType

