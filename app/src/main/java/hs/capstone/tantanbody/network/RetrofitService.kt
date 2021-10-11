package hs.capstone.tantanbody.network

import hs.capstone.tantanbody.model.data.DietDto
import hs.capstone.tantanbody.model.data.FoodDto
import hs.capstone.tantanbody.model.data.RecentFoodDto
import hs.capstone.tantanbody.model.data.UserDto
import retrofit2.Call
import retrofit2.http.*

/**
 * 서버에서 호출할 메소드를 선언하는 인터페이스
 */
interface RetrofitService {

    //== 로그인 화면 ==//
    @GET("users") //이건 그냥 test용 입니당
    fun getUsers(): Call<ArrayList<UserDto>>

    @FormUrlEncoded
    @POST("login")
    fun loginChk(
        @Body userDto: UserDto
    ): Call<String>

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
        @Body dietDto: DietDto
    ): Call<String>

    //== 운동 화면 ==//
}