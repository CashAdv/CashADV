package app.cashadvisor.profile.data

import app.cashadvisor.common.domain.BaseExceptionToErrorMapper
import app.cashadvisor.common.domain.model.ErrorEntity
import javax.inject.Inject

class ProfileExceptionToErrorMapper @Inject constructor() : BaseExceptionToErrorMapper() {

    override fun handleSpecificException(
        exception: Exception,
    ): ErrorEntity {
        return when (exception) {
            is UserProfileException.Profile -> handleProfileException(exception)
            else -> {
                handleUnknownError(exception)
            }
        }
    }

    private fun handleProfileException(exception: UserProfileException.Profile): ErrorEntity {
        return when (exception) {
            is UserProfileException.Profile.BadRequestInvalidInputOrContentType -> {
                ErrorEntity.Profile.InvalidContent(exception.message)
            }
            is UserProfileException.Profile.InternalServerErrorFailedToRetrieve -> {
                ErrorEntity.Profile.FailedToGetData(exception.message)
            }
            is UserProfileException.Profile.InternalServerErrorFailedToUpload -> {
                ErrorEntity.Profile.FailedToSaveData(exception.message)
            }
            is UserProfileException.Profile.UnauthorizedUserNotAuthenticated -> {
                ErrorEntity.Profile.UserNotAuthorized(exception.message)
            }
        }
    }

}
