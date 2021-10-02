package hs.capstone.tantanbody.model

import android.util.Log
import hs.capstone.tantanbody.model.data.GoogleAccount
import java.time.LocalDateTime

class UserRepository {
    val TAG = "UserRepository"
    // DB에 변화를 알리는 자료형으로 감싸기
    var workoutTimes: List<Int> = initT() // userTable.getWorkoutTimes()
    var userWeights: List<Int> = initW() // userTable.getWeights()

    var googleLoginUser: GoogleAccount ?= null

    fun initT() : List<Int> {
        return listOf(23, 20, 30, 21, 16, 26, 18)
    }
    fun initW() : List<Int> {
        Log.d(TAG, "set userWeights!")
        return listOf(23, 20, 30, 21, 16, 26, 18)
    }

    fun insertWorkoutTime(date: LocalDateTime, minute: Int) {
        // 같은 날짜면 업데이트, 없으면 추가
        Log.d(TAG, "date: ${date}, minute: ${minute}")
    }
    fun insertWeight(date: LocalDateTime, kg: Int) {
        // 위와 동일
        Log.d(TAG, "date: ${date}, kg: ${kg}")
    }
}