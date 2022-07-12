package binar.lima.satu.secondhand.data.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
class ProductEntity(
    @PrimaryKey(autoGenerate = true)var id: Int?,
    @ColumnInfo(name = "image_url")var imageUrl: String?,
    @ColumnInfo(name = "name")var name: String?,
    @ColumnInfo(name = "category")var category: String?,
    @ColumnInfo(name = "price")var price: String?,
)