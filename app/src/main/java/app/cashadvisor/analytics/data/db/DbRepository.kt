package app.cashadvisor.analytics.data.db

import app.cashadvisor.analytics.data.db.entities.AmountEntity
import app.cashadvisor.analytics.data.db.entities.CategoryEntity
import app.cashadvisor.analytics.data.db.entities.CategoryWithUserAnalyticsEntity
import app.cashadvisor.analytics.data.db.entities.UserAnalyticsEntity
import kotlinx.coroutines.Dispatchers
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

    suspend fun upsertCategoryWithUserAnalytics(categoryWithUserAnalyticsEntity: List<CategoryWithUserAnalyticsEntity>){
        withContext(Dispatchers.IO){
            dao.upsertCategoryWithAnalyticsEntity(categoryWithUserAnalyticsEntity)
        }
    }

    suspend fun getCategoryWithUserAnalyticsEntityList(): List<CategoryWithUserAnalyticsEntity>{
        return dao.getCategoryWithUserAnalyticsEntityList()
    }

    suspend fun getByCategoryName(categoryName: String): List<CategoryWithUserAnalyticsEntity>{
        return dao.getByCategoryName(categoryName)
    }

    suspend fun getByDate(date: String): List<CategoryWithUserAnalyticsEntity>{
        return dao.getByDate(date)
    }

    suspend fun getByTwoDate(dateStart: String, dateEnd: String): List<CategoryWithUserAnalyticsEntity>{
        return dao.getByTwoDate(dateStart, dateEnd)
    }

    suspend fun getByTwoAmount(minAmount: Int, maxAmount: Int): List<CategoryWithUserAnalyticsEntity> {
        return dao.getByTwoAmount(minAmount, maxAmount)
    }

    suspend fun getMoreThenMinAmount(minAmount: Int): List<CategoryWithUserAnalyticsEntity>{
        return dao.getMoreThenMinAmount(minAmount)
    }

    suspend fun getByTwoDateFirstCategoryAndPlanned(dateStart: String, dateEnd: String, category: String, planned: Boolean): List<AmountEntity>{
        return dao.getByTwoDateFirstCategoryAndPlanned(dateStart, dateEnd, category, planned)
    }

}