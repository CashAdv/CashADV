package app.cashadvisor.profile.data

import java.io.IOException


sealed class UserProfileException(
    override val message: String
) : IOException(message) {

    data class NoConnection(
        override val message: String = NO_INTERNET_CONNECTION
    ) : UserProfileException(message)

    data class Undefined(override val message: String = UNDEFINED_MESSAGE) : UserProfileException(message)

    sealed class Profile(
        message: String
    ) : UserProfileException(message) {

        class BadRequestInvalidInputOrContentType(
            override val message: String,
            val statusCode: Int
        ) : Profile(message)

        class UnauthorizedUserNotAuthenticated(
            override val message: String,
            val statusCode: Int
        ) : Profile(message)

        class InternalServerErrorFailedToUpload(
            override val message: String,
            val statusCode: Int
        ) : Profile(message)

        class InternalServerErrorFailedToRetrieve(
            override val message: String,
            val statusCode: Int
        ) : Profile(message)
    }


    companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val UNDEFINED_MESSAGE = "Undefined"
    }
}
