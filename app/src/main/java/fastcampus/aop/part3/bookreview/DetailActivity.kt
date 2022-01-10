package fastcampus.aop.part3.bookreview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import fastcampus.aop.part3.bookreview.databinding.ActivityDetailBinding
import fastcampus.aop.part3.bookreview.model.Book
import fastcampus.aop.part3.bookreview.model.Review

class DetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding //뷰바인딩

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = getAppDatabase(this)

        val model = intent.getParcelableExtra<Book>("bookModel") //모델 클래스를 가져온다.

        binding.titleTextView.text = model?.title.orEmpty() //가져온 model 안에 있는 값을 바인딩 해 주는데,
        // 모델 속 값이 일수도 있으므로 orEmpty()로 널러블 처리를 해준다.
        binding.descriptionTextView.text=model?.description.orEmpty()

        Glide.with(binding.coverImageView.context) //이미지를 불러오는 라이브러리 glide를 사용하여 뷰에 이미지를 로드한다.
            .load(model?.coverSmallUrl.orEmpty()) //coverSmallUrl을
            .into(binding.coverImageView) //액티비티의 coverImageView에 바인딩한다.

        Thread{
            val review = db.reviewDao().getOneReview(model?.id?.toInt() ?:0)
            runOnUiThread{
                binding.reviewEditText.setText(review?.review.orEmpty())
            }
        }.start()

        binding.saveButton.setOnClickListener{ //저장하기 버튼을 클릭할 경우
            Thread{ //스레드를 실행한다.
                db.reviewDao().saveReview(
                    Review(model?.id?.toInt() ?:0,
                    binding.reviewEditText.text.toString()
                    )
                )
            }.start()
        }

    }



}