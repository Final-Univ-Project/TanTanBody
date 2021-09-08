package hs.capstone.tantanbody.data.model

import android.net.Uri

data class GoogleAccount(
    val displayName: String?,
    val familyName: String?,
    val givenName: String?,
    val email: String?,
    val id: String?,
    val photoUrl: Uri?
) {
    override fun toString(): String {
        return "  displayName: ${displayName}\n" +
                " familyName: ${familyName}\n" +
                " givenName: ${givenName}\n" +
                " email: ${email}\n" +
                " id: ${id}\n" +
                " photoUrl: ${photoUrl}\n"
    }
}
