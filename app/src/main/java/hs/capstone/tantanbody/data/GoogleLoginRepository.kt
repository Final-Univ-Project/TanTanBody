package hs.capstone.tantanbody.data

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import hs.capstone.tantanbody.data.model.GoogleAccount

class GoogleLoginRepository(signIn: GoogleSignInAccount?) {
    val TAG = "GoogleLoginRepository"
    var user: GoogleAccount? = null
    val isLoggedIn: Boolean
        get() = user != null

    init {
        signIn?.apply {
            user = GoogleAccount(
                displayName,
                familyName,
                givenName,
                email,
                id,
                photoUrl
            )
            Log.d(TAG, "User: ${account.name} type: ${account.type} isLoggedIn: ${isLoggedIn}")
        }
    }

    fun logout() {
        user = null
        // logout
    }
}