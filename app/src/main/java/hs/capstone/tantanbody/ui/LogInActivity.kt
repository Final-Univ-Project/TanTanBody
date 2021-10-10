package hs.capstone.tantanbody.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import hs.capstone.tantanbody.R
import hs.capstone.tantanbody.model.TTBApplication
import hs.capstone.tantanbody.model.data.UserDto
import hs.capstone.tantanbody.user.UserViewModel
import hs.capstone.tantanbody.user.UserViewModelFactory

class LogInActivity: Activity() {
    val TAG = "LogInActivity"
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
            if (task.isSuccessful) {
                handleSignInResult(task)
            } else {
                // 앱 종료 방지
                Log.e(TAG, "Google 로그인 중단")
                Toast.makeText(applicationContext, getString(R.string.fail_google_login), Toast.LENGTH_LONG)
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            loginFromGoogleAccount(account)
        } catch (e: ApiException) {
            Toast.makeText(this, e.statusCode, Toast.LENGTH_LONG).show()
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    fun loginFromGoogleAccount(account: GoogleSignInAccount?) {
        if (account != null) {
            val app = this@LogInActivity.application as TTBApplication
            app.userRepository.checkIsSignedUser(UserDto(
                userEmail = account.email,
                userName = account.displayName,
                userPhoto = account.photoUrl.toString()
            ))

            val intent = Intent(this@LogInActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

}