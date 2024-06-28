package app.cashadvisor.analytics.data.db.entities.converteters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import app.cashadvisor.analytics.data.db.models.Category

@ProvidedTypeConverter
class EnumConverter {
    @TypeConverter
    fun toCategory(value: Int) = enumValues<Category>()[value]

    @TypeConverter
    fun fromCategory(value: Category) = value.ordinal
}