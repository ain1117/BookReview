package fastcampus.aop.part3.bookreview.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fastcampus.aop.part3.bookreview.model.Review

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE uid = :uid")
    fun getOne(uid: Int): Review

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: Review)

}