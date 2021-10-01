package hs.capstone.tantanbody.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import hs.capstone.tantanbody.R
import hs.capstone.tantanbody.model.GoogleLoginRepository

class LogInActivity: Activity() {
    val TAG = "LogInActivity"
//    lateinit var emailEt: EditText
//    lateinit var pwEt: EditText
//    lateinit var signInBtn: Button
    lateinit var signInGoogle: SignInButton

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signInGoogle = findViewById(R.id.signInGoogle)
        signInGoogle.setSize(SignInButton.SIZE_WIDE)
        signInGoogle.setColorScheme(SignInButton.COLOR_DARK)

        // Internet 연결되었는지 Check

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // 이전에 Google 로그인한 경우
        val alreadyAccount = GoogleSignIn.getLastSignedInAccount(this)
        loginFromGoogleAccount(alreadyAccount)


        signInGoogle.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    // 처음 Google로 로그인하고 오면
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            loginFromGoogleAccount2()
//            TODO 구글로 로그인 error 때문에 임시로 막음 - 이거 빼면 구글로 로그인 상관 없이 일단 메인 페이지로 넘어감
//            val account = completedTask.getResult(ApiException::class.java)
//            loginFromGoogleAccount(account)
        } catch (e: ApiException) {
//            TODO 여기서 추가 오류 발생해서 일단 막음
//            Toast.makeText(this, e.statusCode, Toast.LENGTH_LONG).show()
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }
    fun loginFromGoogleAccount(account: GoogleSignInAccount?) {
        account?.run {
            GoogleLoginRepository(this)

            val intent = Intent(this@LogInActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }

    /**
     * 구글로 로그인 패스 후
     * 메인 화면으로 넘어갈 수 있도록 만든 function
     */
    fun loginFromGoogleAccount2() {
        run {
            //myAppId = "pigismile@gmail.com"
            val intent = Intent(this@LogInActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }
}