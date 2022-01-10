package fastcampus.aop.part3.bookreview.model

import com.google.gson.annotations.SerializedName

data class BestSellerDto ( //전체 api lisponse를 받아온다.
    @SerializedName("title") val title: String,
    @SerializedName("item") val books: List<Book> //list로 되어있는 item을 받아온다.
)