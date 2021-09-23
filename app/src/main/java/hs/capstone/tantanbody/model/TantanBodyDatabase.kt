package hs.capstone.tantanbody.model

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import org.apache.ibatis.session.SqlSessionFactory

class TantanBodyDatabase {

    private class TantanBodyDatabaseCallback(
        private val scope: CoroutineScope) {
        // table data 가져오기
    }

    companion object {
        private var INSTANCE: SqlSessionFactory? = null
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): SqlSessionFactory {

            return INSTANCE ?: synchronized(this) {
                // 서버와 연결
                val instance = ConnectToMyBatis.sqlSessionFactory
                INSTANCE = instance
                instance
            }
        }
    }
}