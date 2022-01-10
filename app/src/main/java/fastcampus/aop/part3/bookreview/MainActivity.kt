package fastcampus.aop.part3.bookreview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import fastcampus.aop.part3.bookreview.adapter.BookAdapter
import fastcampus.aop.part3.bookreview.adapter.HistoryAdapter
import fastcampus.aop.part3.bookreview.api.BookService
import fastcampus.aop.part3.bookreview.databinding.ActivityMainBinding
import fastcampus.aop.part3.bookreview.model.BestSellerDto
import fastcampus.aop.part3.bookreview.model.History
import fastcampus.aop.part3.bookreview.model.SearchBookDto
import org.chromium.base.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var bookService: BookService

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //뷰바인딩은 setContentView전에 선언한다.

        binding = ActivityMainBinding.inflate(layoutInflater) //뷰바인딩
        setContentView(binding.root)

        initBookRecyclerView()
        initHistoryRecyclerView()
        initSearchEditText()

        db = getAppDatabase(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com") //baseurl, https로 요청해야 안드로이드 보안 정책을 지킬 수 있다.
            .addConverterFactory(GsonConverterFactory.create()) //gson 변환
            .build() //빌드하여 레트로핏 구현체를 생성해 변수에 할당한다.

        bookService = retrofit.create(BookService::class.java) //bookservice 인터페이스를 구현한다다

        bookService.getBestSellerBooks(getString(R.string.interParkAPIKey)) //apikey를 인자로 추가
            .enqueue(object : Callback<BestSellerDto> {

                override fun onResponse( //api 요청이 성공했을 경우
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {
                    //todo api 성공처리

                    if (response.isSuccessful.not()) { //성공하지 않았을 때 , 예외처리
                        Log.e(TAG, "NOT!! SUCCESS")
                        return
                    }

                    response.body()?.let { //body가 없을수도 있으므로 null허용 ?.을 사용한다.
                        Log.d(TAG, it.toString())
                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }

                        adapter.submitList(it.books) //어댑터에 북 리스트를 전달한다.
                    }
                }

                override fun onFailure(
                    call: Call<BestSellerDto>,
                    t: Throwable
                ) {
                    Log.e(TAG,t.toString())

                    //todo api 실패처리
                }
            })



    }

    private fun search(keyword: String) {

        bookService.getBookByName(getString(R.string.interParkAPIKey),keyword)
            .enqueue(object : Callback<SearchBookDto> {

                override fun onResponse( //api 요청이 성공했을 경우
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>
                ) {
                    //todo api 성공처리
                    hideHistoryVeiew()
                    saveSearchKeyword(keyword)

                    if (response.isSuccessful.not()) { //성공하지 않았을 때 , 예외처리
                        Log.e(TAG, "NOT!! SUCCESS")
                        return
                    }

                    adapter.submitList(response.body()?.books.orEmpty())
                }

                override fun onFailure(
                    call: Call<SearchBookDto>,
                    t: Throwable
                ) {
                    Log.e(TAG,t.toString())
                    hideHistoryVeiew()

                    //todo api 실패처리
                }
            })
    }

    fun initBookRecyclerView() {
        adapter = BookAdapter(itemClickedListener = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bookModel",it) //인텐트로 값을 전달하는 putExtra를 사용해, 직렬화시킨 클래스 자체를 넘긴다.
            startActivity(intent)
        }) //어댑터 생성
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this) //리사이클러뷰가 실제로 어떻게 그려지는가
        binding.bookRecyclerView.adapter = adapter //어댑터를 연결시켜준다.
    }

    private fun showHistoryView() { //검색어를 입력하기 위해 에딧텍스트를 클릭했을 때 보이는 화면
        Thread {
            val keywords = db.historyDao().getAll().reversed()
            runOnUiThread{
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty()) //keyword가 null로 올 경우를 대비해 orEmpty를 사용한다.
            }

        }.start()
        binding.historyRecyclerView.isVisible = true
    }

    private fun hideHistoryVeiew() {
        binding.historyRecyclerView.isVisible = false

    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(historyDeleteClickListener = { //람다를 구현해준다.
        deleteSearchKeyword(it)
        })

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter

    }

    private fun saveSearchKeyword(keyword: String) {
        Thread{
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun deleteSearchKeyword( keyword: String) {
        Thread{
            db.historyDao().delete(keyword) //데이터 지우기
            showHistoryView()
            //todo View 갱신
        }.start()
    }

    private fun initSearchEditText() {
        binding.searchEditText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false

        }
        binding.searchEditText.setOnTouchListener{ v, event ->
            if(event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

