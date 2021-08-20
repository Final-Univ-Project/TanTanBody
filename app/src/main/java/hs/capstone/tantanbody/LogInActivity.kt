package hs.capstone.tantanbody

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import hs.capstone.tantanbody.data.GoogleAccount

class LogInActivity: Activity() {
    val TAG = "LogInActivity"
    lateinit var signInGoogle: SignInButton
    lateinit var GoToMain: Button

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signInGoogle = findViewById(R.id.signInGoogle)
        GoToMain = findViewById(R.id.GoToMain)

        GoToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // 이전에 로그인한 경우
        val alreadyAccount = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(alreadyAccount)

        // 처음 Google로 로그인하러 온 경우
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

    // 로그인 한 사용자와 같은 이름으로 UI세팅
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }


    fun updateUI(account: GoogleSignInAccount?) {
        account?.run {
            val glogin = GoogleAccount(
                this.displayName,
                this.familyName,
                this.givenName,
                this.email,
                this.id,
                this.photoUrl
            )

            // + email, com.google
            Log.d(TAG, "${glogin}\n  ${this.account.name}\n  ${this.account.type}\n")

            val intent = Intent(this@LogInActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }

}