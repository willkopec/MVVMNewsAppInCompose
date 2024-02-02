package com.example.mvvmnewsappincompose.db

import androidx.room.TypeConverter
import com.example.mvvmnewsappincompose.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source{
        return Source(name, name)
    }

}
