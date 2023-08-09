package com.zhigaras.auth

import com.zhigaras.core.DiscussException

class NetworkException : DiscussException() {
    override fun errorId() = R.string.check_internet_connection
}

abstract class AuthException : Exception()

class NoSuchUser : DiscussException() {
    override fun errorId() = R.string.no_such_user
}

abstract class RegistrationException : DiscussException()

class RegistrationFailed : RegistrationException() {
    override fun errorId() = R.string.registration_failed
}

class EmailIsUsed : RegistrationException() {
    override fun errorId(): Int = R.string.email_is_used
}

abstract class LoginException : DiscussException()

class SigningInFailed : LoginException() {
    override fun errorId(): Int = R.string.signing_in_failed
}

class InvalidUser : LoginException() {
    override fun errorId(): Int = R.string.invalid_credentials
}

class InvalidCredentials : LoginException() {
    override fun errorId(): Int = R.string.invalid_password
}

class TooManyRequests : LoginException() {
    override fun errorId(): Int = R.string.account_disabled
}

abstract class OneTapSignInException : DiscussException()

class CouldNotStartOneTapSignIn : OneTapSignInException() {
    override fun errorId(): Int = R.string.couldn_t_start_signing_in_with_google
}

class CouldNotGetCredentials : OneTapSignInException() {
    override fun errorId(): Int = R.string.couldn_t_get_credentials
}

class NoGoogleAccountsFound : OneTapSignInException() {
    override fun errorId(): Int = R.string.no_google_accounts_found
}

class TokenNotReceived : OneTapSignInException() {
    override fun errorId(): Int = R.string.token_not_received
}

class OneTapSignInCanceled : OneTapSignInException() {
    override fun errorId(): Int = R.string.signing_in_with_google_canceled
}

class TooManyOneTapRequests : OneTapSignInException() {
    override fun errorId(): Int = R.string.too_many_google_signIn_requests
}
