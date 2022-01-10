package fastcampus.aop.part3.bookreview.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fastcampus.aop.part3.bookreview.model.History

@Dao //dao: data access object. 데이터에 접근할 수 있는 메서드를 정의해놓은 인터페이스
interface HistoryDao {

    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history WHERE keyword == :keyword")
    fun delete(keyword: String)
}