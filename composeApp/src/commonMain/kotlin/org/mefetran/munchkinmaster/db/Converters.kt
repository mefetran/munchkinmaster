package org.mefetran.munchkinmaster.db

import androidx.room.TypeConverter
import org.mefetran.munchkinmaster.model.Avatar
import org.mefetran.munchkinmaster.model.Sex

class Converters {
    @TypeConverter
    fun fromSex(sex: Sex): Int {
        return sex.ordinal
    }

    @TypeConverter
    fun toSex(value: Int): Sex {
        return Sex.entries[value]
    }

    @TypeConverter
    fun fromAvatar(avatar: Avatar): Int {
        return avatar.ordinal
    }

    @TypeConverter
    fun toAvatar(value: Int): Avatar {
        return Avatar.entries[value]
    }
}