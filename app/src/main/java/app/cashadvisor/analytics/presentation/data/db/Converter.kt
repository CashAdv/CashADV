package app.cashadvisor.analytics.presentation.data.db

import androidx.room.TypeConverter

class Converter {

    @TypeConverter
    fun categoryToString(category: Category): String {
        return category.name
    }

    @TypeConverter
    fun stringToCategory(category: String): Category {
        return Category.valueOf(category)
    }

}