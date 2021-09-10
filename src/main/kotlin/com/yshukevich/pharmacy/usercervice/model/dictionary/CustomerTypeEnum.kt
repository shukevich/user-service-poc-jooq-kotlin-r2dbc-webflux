package com.yshukevich.pharmacy.usercervice.model.dictionary

enum class CustomerTypeEnum(val value: String) {
    RETAIL("Customer as a person"),
    PARTNER("Customer as a company"),
    NOT_CUSTOMER("Not a Customer"),
    NONE("No type")
}
