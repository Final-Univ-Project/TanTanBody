package hs.capstone.tantanbody.model.network

import hs.capstone.tantanbody.model.data.*
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * 서버에서 호출할 메소드를 선언하는 인터페이스
 *
 */
interface RetrofitService {

    //== 로그인 화면 ==//
    @FormUrlEncoded
    @POST("login")
    fun saveUserData(
        @Field("userEmail") userEmail: String,
        @Field("userName") userName: String,
        @Field("photo") userPhoto: String,
        @Field("goal") userGoal: String?
    ): Call<String>

    //== 기록 화면 ==//
    fun getGoal(
        @Field("userEmail") userEmail: String
    ): Call<String>

    //== 운동 화면 ==//
    // UserDto에서 ?(nullable)가 붙으면, 고칠코드가 많아요
    // 그래서 그냥 이메일String으로 전송하도록 해주세요
    fun getFavExercise(
        @Field("userEmail") userEmail: String
    ): Call<List<YouTubeVideo>>

    //
    fun saveVideo(
        @Field("videoId") videoId: String,
        @Field("uploadDate") publishedAt: Date,
        @Field("videoTitle") title: String,
        @Field("") description: String,
        @Field("thumbnail") thumbnail: String,
        @Field("") channelTitle: String,
        @Field("") keyworks: List<String>
    ): Call<List<String>>

    //== 식단 화면 ==//
    @GET("diet") //사실상 post로 바꿔야함 ... back도 마찬가지
    fun getFoods(
        @Query("userEmail") userEmail : String): Call<ArrayList<DietDto>>

    @GET("diet/w/search")
    fun getRecentFoods(
        @Query("userEmail") userEmail: String) : Call<RecentFoodDto>

    @GET("diet/w/search/foodList")
    fun getFoodList(
        @Query("sFoodName") searchFood : String) : Call<ArrayList<FoodDto>>

    @FormUrlEncoded
    @POST("diet/save")
    fun saveDiet(
        //POST 방식으로 데이터 통신 시 넘기는 변수는 Field 라고 해야함
        @Field("userName") userName: String,
        @Field("foodNum") foodNum: Int,
        @Field("eatCount") eatCount: Int,
        @Field("totalKcal") totalKcal: Int
    ): Call<String>

    //== 운동 화면 ==//
}