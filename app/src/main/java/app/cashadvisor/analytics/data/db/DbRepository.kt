package app.cashadvisor.analytics.data.db

import app.cashadvisor.analytics.data.db.entities.CategoryEntity
import app.cashadvisor.analytics.data.db.entities.UserAnalyticsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DbRepository(private val dao: Dao) {

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

    suspend fun getCategoryWithUserAnalytics(): Flow<List<CategoryWithUserAnalytics>> {
        return withContext(Dispatchers.IO){
            dao.getCategoryWithUserAnalytics()
        }
    }

    suspend fun removeAllUserAnalyticsTable(){
        withContext(Dispatchers.IO){
            dao.removeAllUserAnalyticsTable()
        }
    }

    suspend fun removeAllCategoryTable(){
        withContext(Dispatchers.IO){
            dao.removeAllCategoryTable()
        }
    }

    suspend fun removeAllCategoryAnalyticsTable(){
        withContext(Dispatchers.IO){
            dao.removeAllCategoryAnalyticsTable()
        }
    }
}