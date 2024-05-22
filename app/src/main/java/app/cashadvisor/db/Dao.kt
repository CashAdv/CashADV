package app.cashadvisor.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    fun insert(spend: Spend)

    @Query("SELECT * FROM spendTable")
    fun getAllSpends(): Flow<List<Spend>>

    @Query("DELETE FROM spendTable")
    fun deleteAll()
}