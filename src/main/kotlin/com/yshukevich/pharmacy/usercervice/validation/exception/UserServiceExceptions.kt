package com.yshukevich.pharmacy.usercervice.validation.exception

class UserProfileNotFoundException(message: String) : Exception(message)

class UserCredentialsNotFoundException(message: String) : Exception(message)

class UserCredentialsAlreadyExistsException(message: String) : Exception(message)

class UserProfileAlreadyExistsException(message: String) : Exception(message)

class GeneralException(message: String) : Exception(message)
