package com.example.final_thesis_recipe_app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.final_thesis_recipe_app.pojo.Meal

@Database(entities = [Meal::class], version = 2)
@TypeConverters(MealTypeConverter::class)
abstract class MealDatabase: RoomDatabase(){
    //tao 1 instance tu MealDao
    abstract fun mealDao(): MealDao

    companion object{
        //Volatile: gia tri cua bien se dc cap nhat ngay lap tuc khi co thay doi, cac thread khac nhau co the nhin thay gia tri moi nhat cua bien
        @Volatile
        var INSTANCE: MealDatabase?=null

        @Synchronized //only 1 thread can have an instance from this room db
        fun getInstance(context: Context): MealDatabase{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context, MealDatabase::class.java, "meal.db")
                    .allowMainThreadQueries() //cho phep truy van tren main thread
                    .fallbackToDestructiveMigration() //khi khong co migration(su di cu) hoac migration bi loi, Room se xoa db hien co va tao db moi
                    .build()
            }
            return INSTANCE as MealDatabase
        }

    }


}