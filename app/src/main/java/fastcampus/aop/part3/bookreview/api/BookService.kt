package fastcampus.aop.part3.bookreview.api

import fastcampus.aop.part3.bookreview.model.BestSellerDto
import fastcampus.aop.part3.bookreview.model.SearchBookDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("/api/search.api?output=json") //search api
    fun getBookByName(
        @Query("key") apiKey : String, //SQL의 Query. 변수에 값을 받는다.
        @Query ("query") keyword: String
    ): Call<SearchBookDto> //JSON데이터를 <>안의 자료형으로 받겠다

    @GET("/api/bestSeller.api?categoryId=100&output=json") //bestSellerapi
    fun getBestSellerBooks(
        @Query("key") apiKey: String,

    ):Call<BestSellerDto> //데이터를 <>안의 자료형으로 받는다.하기실어죽갯당..


}