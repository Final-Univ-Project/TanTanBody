package hs.capstone.tantanbody.model.data

import com.google.gson.annotations.SerializedName

/**
 * 음식, 식단 DTO
 */
data class DietDto(
    /**
     * var : getter(), setter() 생성 - db에 쓰거나 가져오거나가 가능해야함
     * val : getter() 생성 - db에서 가져오기만 함
     */
    val userEmail: String,
    @SerializedName("food_name")
    val foodName: String,
    @SerializedName("EAT_COUNT")
    val eatCount: Int,
    @SerializedName("TOTAL_KCAL")
    val eatTotalKcal: Int,
    @SerializedName("EAT_DATM")
    val eatDatm: String //time 이지만 json으로 가져오는 타입은 string
)