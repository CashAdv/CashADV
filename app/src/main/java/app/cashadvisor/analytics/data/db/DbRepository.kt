package app.cashadvisor.analytics.data.db

import app.cashadvisor.analytics.data.db.entities.CategoryEntity
import app.cashadvisor.analytics.data.db.entities.TotalAmountByCategoryIdEntity
import app.cashadvisor.analytics.data.db.entities.UserAnalyticsEntity
import app.cashadvisor.analytics.data.db.models.CategoryWithUserAnalytics
import app.cashadvisor.analytics.data.db.models.TotalAmountByCategoryId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DbRepository(private val dao: Dao) {
    // соединить localDatasorce remoteDatasorce


    //remoteDatasorce

    //доделать

    //localDatasorce

    suspend fun upsertUserAnalytics(userAnalyticsEntity: List<UserAnalyticsEntity>){
        withContext(Dispatchers.IO){
            dao.upsertUserAnalyticsEntity(userAnalyticsEntity)
        }
    }

    suspend fun upsertCategory(categoryEntity: List<CategoryEntity>){
        withContext(Dispatchers.IO){
            dao.upsertCategoryEntity(categoryEntity)
        }
    }
}