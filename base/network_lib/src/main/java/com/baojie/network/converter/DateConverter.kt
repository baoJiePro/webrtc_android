package com.baojie.network.converter

import androidx.room.TypeConverter
import java.util.*

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2021/5/30 10:16 上午
 */
object DateConverter {

    @TypeConverter
    fun date2Long(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun long2Date(date: Long): Date {
        return Date(date)
    }
}