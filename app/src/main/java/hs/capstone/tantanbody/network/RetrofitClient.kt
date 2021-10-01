package hs.capstone.tantanbody.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 서버 호출 시 마다 인터페이스 구현은 비효율적
 * '싱글톤(Object)'으로 제작하는 것이 바람직
 */
object RetrofitClient {
    private var instance: Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()
    // server address
    private const val BASE_URL = "http://192.168.10.27:8080/"

    //singleton
    fun getInstance(): Retrofit{
        if(instance == null){
            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        return instance!!
    }
    private val myTestClient: Retrofit.Builder by lazy{
       Retrofit.Builder().baseUrl("http://192.168.10.27:8080/")
           .addConverterFactory(GsonConverterFactory.create())
    }
    val myTestClientService: RetrofitService by lazy {
        myTestClient.build().create(RetrofitService::class.java)
    }
}