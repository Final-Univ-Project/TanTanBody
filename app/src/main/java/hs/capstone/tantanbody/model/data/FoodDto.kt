package hs.capstone.tantanbody.model.data

import com.google.gson.annotations.SerializedName

data class FoodDto(
    /**
     * var : getter(), setter() 생성 - db에 쓰거나 가져오거나가 가능해야함
     * val : getter() 생성 - db에서 가져오기만 함
     */
    val food_num: Int,
    val food_name: String,
    @SerializedName("one_time_provision")
    val provision: Int,
    val total_kcal: Int,
    @SerializedName("content_unit")
    val unit: String
)