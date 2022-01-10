package fastcampus.aop.part3.bookreview.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize //직렬화 가능하도록 수정한다.
data class Book(
    @SerializedName("itemId") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("priceSales") val priceSales: String,
    @SerializedName("coverSmallUrl") val coverSmallUrl: String,
    @SerializedName("mobileLink") val mobileLink: String
): Parcelable