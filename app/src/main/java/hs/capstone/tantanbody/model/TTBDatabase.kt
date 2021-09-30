package hs.capstone.tantanbody.model

import android.content.Context
import hs.capstone.tantanbody.R
import kotlinx.coroutines.CoroutineScope

class TTBDatabase {
    private class TantanBodyDatabaseCallback(
        private val scope: CoroutineScope) {
        // table data 가져오기
    }

    companion object {
        private var INSTANCE: Any? = null
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): Any? {
            // DB와 연결할 때 필요한 것들 설정
            return INSTANCE
//                ?: synchronized(this) {
//                val instance = ConnectToMyBatis.sqlSessionFactory
//                INSTANCE = instance
//                instance
//            }
        }
    }
}