package app.cashadvisor.common.domain

import app.cashadvisor.common.domain.model.ErrorEntity

//пока назвал Resource, тк он может быть оберткой не только для данных из сети, а просто Result слишком популярное имя
sealed class Resource<T>(val data: T? = null, val error: ErrorEntity? = null) {

    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(error: ErrorEntity) : Resource<T>(error = error)

}