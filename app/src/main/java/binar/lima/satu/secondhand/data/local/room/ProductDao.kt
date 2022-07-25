package binar.lima.satu.secondhand.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {

    //==product
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addProduct(productEntity: List<ProductEntity>)

    @Query("SELECT * FROM product")
    fun getProduct() : LiveData<List<ProductEntity>>

    @Query("DELETE FROM product")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addHistory(historyEntity: HistoryEntity)

    @Query("SELECT * FROM history")
    fun getHistory() : LiveData<List<HistoryEntity>>

    @Query("DELETE FROM history")
    fun deleteAllHistory()
}