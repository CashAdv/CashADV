package app.cashadvisor.common.domain

import app.cashadvisor.common.domain.model.ErrorEntity

//пока назвал Resource, тк он может быть оберткой не только для данных из сети, а просто Result слишком популярное имя
sealed class Resource<T>() {

    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val error: ErrorEntity) : Resource<T>()

}