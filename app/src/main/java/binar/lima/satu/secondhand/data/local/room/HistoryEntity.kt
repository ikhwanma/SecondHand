package binar.lima.satu.secondhand.data.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
class HistoryEntity(
    @PrimaryKey(autoGenerate = true)var id: Int?,
    @ColumnInfo(name = "image_url")var imageUrl: String?,
    @ColumnInfo(name = "name")var name: String?,
    @ColumnInfo(name = "category")var category: String?,
    @ColumnInfo(name = "price")var price: String?,
    @ColumnInfo(name = "city")var city: String?,
    @ColumnInfo(name = "product_id")var productId: Int?,
)