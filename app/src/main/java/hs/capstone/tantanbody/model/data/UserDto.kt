package hs.capstone.tantanbody.model.data

/**
 * JSON 타입 변환에 사용
 */
data class UserDto(
    /**
     * var : getter(), setter() 생성
     * val : getter() 생성
     */
    var userEmail: String,
    var userName: String,
    var userPhoto: String
)
