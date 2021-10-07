package hs.capstone.tantanbody.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // server address
    // private const val BASE_URL = ""
    private val myTestClient: Retrofit.Builder by lazy{
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create()
        Retrofit.Builder().baseUrl("http://192.168.10.27:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
    }
    val myTestClientService: RetrofitService by lazy {
        myTestClient.build().create(RetrofitService::class.java)
    }
}