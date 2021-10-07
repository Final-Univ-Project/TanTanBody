package hs.capstone.tantanbody.model.data

data class UserDto(
    /**
     * var : getter(), setter() 생성
     * val : getter() 생성
     */
    val userEmail: String,
    val userName: String,
    val userPhoto: String
)