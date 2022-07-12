package binar.lima.satu.secondhand.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addProduct(productEntity: List<ProductEntity>)

    @Query("SELECT * FROM product")
    fun getProduct() : LiveData<List<ProductEntity>>

    @Query("DELETE FROM product")
    fun deleteAll()
}