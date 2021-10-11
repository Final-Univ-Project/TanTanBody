package hs.capstone.tantanbody.model.network

import hs.capstone.tantanbody.model.data.DietDto
import hs.capstone.tantanbody.model.data.FoodDto
import hs.capstone.tantanbody.model.data.RecentFoodDto
import hs.capstone.tantanbody.model.data.UserDto
import retrofit2.Call
import retrofit2.http.*

/**
 * 서버에서 호출할 메소드를 선언하는 인터페이스
 *
 */
interface RetrofitService {

    //== 로그인 화면 ==//
    @GET("users")
    fun getUsers(): Call<ArrayList<UserDto>>

    @POST("login")
    fun saveUser(): Call<String>

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