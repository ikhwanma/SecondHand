package binar.lima.satu.secondhand.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class, HistoryEntity::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}