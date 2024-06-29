package app.cashadvisor.analytics.data.db

import app.cashadvisor.analytics.data.db.entities.CategoryEntity
import app.cashadvisor.analytics.data.db.entities.UserAnalyticsEntity
import app.cashadvisor.analytics.data.db.entities.converteters.convertToLong
import app.cashadvisor.analytics.data.db.models.SumByCategory
import kotlinx.coroutines.flow.Flow

class DbRepository(private val dao: Dao) {

    suspend fun upsertUserAnalytics(userAnalyticsEntity: List<UserAnalyticsEntity>) {
        userAnalyticsEntity.forEach {
            dao.upsertUserAnalyticsEntity(it)
        }
    }

    suspend fun upsertCategory(categoryEntity: List<CategoryEntity>) {
        categoryEntity.forEach {
            dao.upsertCategoryEntity(it)
        }
    }

    fun getSumAmountByCategory(
        type: Int,
        dateStart: String,
        dateEnd: String,
        isPlanned: Boolean
    ): Flow<List<SumByCategory>>{
        return dao.getSumAmountByCategory(
            type,
            dateStart.convertToLong(),
            dateEnd.convertToLong(),
            isPlanned
        )
    }

}