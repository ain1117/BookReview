package fastcampus.aop.part3.bookreview

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import fastcampus.aop.part3.bookreview.dao.HistoryDao
import fastcampus.aop.part3.bookreview.dao.ReviewDao
import fastcampus.aop.part3.bookreview.model.History
import fastcampus.aop.part3.bookreview.model.Review

@Database(entities = [History::class, Review::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao
}
fun getAppDatabase(context: Context): AppDatabase {

    val migration_1_2 = object : Migration(1,2) { //마이그레이션
        override fun migrate(database: SupportSQLiteDatabase) { //쿼리문을 작성해서 어떤 새로운 데이터테이블을 바꾸는지에 대한 정보를 제공한다.
            TODO("Not yet implemented")
            database.execSQL("CREATE TABLE 'REVIEW' ('id' INTEGER, 'review' TEXT," + "PRIMARY KEY('id'))")
        }

    }

    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "BookSearchDB"
    )
        .addMigrations()
        .build()

}