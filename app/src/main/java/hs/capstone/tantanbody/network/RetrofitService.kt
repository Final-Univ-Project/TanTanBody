package hs.capstone.tantanbody.network

import hs.capstone.tantanbody.model.data.UserDto
import retrofit2.Call
import retrofit2.http.*

/**
 * 서버에서 호출할 메소드를 선언하는 인터페이스
 *
 */
interface RetrofitService {
    //@FormUrlEncoded
    //@GET("http://192.168.10.27:8080/users")
    @GET
    fun getUsers(@Url url: String): Call<UserDto>

    @FormUrlEncoded
    @POST("diet/save")
    fun saveDiet(
        //POST 방식으로 데이터 통신 시 넘기는 변수는 Field 라고 해야함
        @Field("userName") userName: String,
        @Field("foodNum") foodNum: Int,
        @Field("eatCount") eatCount: Int,
        @Field("totalKcal") totalKcal: Int
    ): Call<String>
}