package hs.capstone.tantanbody.model

import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactoryBuilder

object ConnectToMyBatis {
    val resource = "/app/src/main/res/xml/mybatis_config.xml"
    val sqlSessionFactory by lazy {
        // sqlSessionFactory 생성
        val inputStream = Resources.getResourceAsStream(resource)
        SqlSessionFactoryBuilder().build(inputStream)
    }

    // method 별로 sqlSession 주기?
}