package app.cashadvisor.common.domain

import app.cashadvisor.common.domain.model.ErrorEntity
import app.cashadvisor.common.utill.exceptions.LoginException
import app.cashadvisor.common.utill.exceptions.NetworkException
import app.cashadvisor.common.utill.exceptions.RegisterException
import app.cashadvisor.common.utill.extensions.logNetworkError
import java.net.ConnectException

abstract class BaseExceptionToErrorMapper {

    abstract fun handleSpecificException(
        exception: Exception,
    ): ErrorEntity

    fun handleException(exception: Exception): ErrorEntity {
        return when (exception) {
            is ConnectException, is NetworkException, is LoginException.NoConnection, is RegisterException.NoConnection -> {
                logNetworkError(exception.message)
                handleNetworkError(exception)
            }

            else -> {
                logNetworkError(exception.message)
                handleSpecificException(exception)
            }
        }
    }

    private fun handleNetworkError(exception: Exception): ErrorEntity {
        return ErrorEntity.NetworksError.NoInternet(
            exception.message ?: DEFAULT_ERROR_MESSAGE
        )
    }

    protected fun handleUnknownError(exception: Exception): ErrorEntity {
        return ErrorEntity.UnknownError(
            exception.message ?: DEFAULT_ERROR_MESSAGE
        )
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "No error message"
    }
}

