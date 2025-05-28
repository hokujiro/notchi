package com.systems.notchi.presentation.auth

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.systems.notchi.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GoogleSignInUtils {

    companion object {
        fun doGoogleSignIn(
            context: Context,
            scope: CoroutineScope,
            launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
            onTokenReceived: (String) -> Unit,
            onFailure: (Throwable) -> Unit
        ) {
            val credentialManager = CredentialManager.create(context)
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(getCredentialOptions())
                .build()

            scope.launch {
                try {
                    val result = credentialManager.getCredential(context, request)
                    when (val credential = result.credential) {
                        is PublicKeyCredential -> {
                            // WebAuthn handling (unused here)
                        }
                        is PasswordCredential -> {
                            // Optional password-based login handling
                        }
                        is CustomCredential -> {
                            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                val googleIdTokenCredential =
                                    GoogleIdTokenCredential.createFrom(credential.data)
                                val googleTokenId = googleIdTokenCredential.idToken
                                val authCredential = GoogleAuthProvider.getCredential(googleTokenId, null)

                                val user = Firebase.auth.signInWithCredential(authCredential).await().user
                                if (user != null && !user.isAnonymous) {
                                    onTokenReceived(googleTokenId)
                                } else {
                                    onFailure(Exception("Firebase user was null or anonymous"))
                                }
                            }
                        }
                    }
                } catch (e: NoCredentialException) {
                    // Fallback to standard Google Sign-In flow
                    Log.d("GOOGLE_SIGN_IN", "Launching GoogleSignInClient with client ID: ${BuildConfig.GOOGLE_CLIENT_ID}")

                    val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
                        .requestEmail()
                        .build()

                    val signInClient = GoogleSignIn.getClient(context, options)

                    val signInIntent = signInClient.signInIntent

                    Log.d("GOOGLE_SIGN_IN", "Sign-in intent created: $signInIntent")

                    launcher?.launch(signInIntent)
                } catch (e: GetCredentialException) {
                    Log.e("GoogleSignIn", "CredentialManager error: ${e.message}", e)
                    onFailure(e)
                }
            }
        }

        private fun getCredentialOptions(): CredentialOption {
            return GetGoogleIdOption.Builder()
                .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .build()
        }

        fun handleSignInResultFromIntent(
            context: Context,
            data: Intent?,
            onTokenReceived: (String) -> Unit,
            onFailure: (Throwable) -> Unit
        ) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                val token = account.idToken

                if (token != null) {
                    val authCredential = GoogleAuthProvider.getCredential(token, null)
                    CoroutineScope(context.mainExecutor.asCoroutineDispatcher()).launch {
                        try {
                            val user = Firebase.auth.signInWithCredential(authCredential).await().user
                            if (user != null && !user.isAnonymous) {
                                onTokenReceived(token)
                            } else {
                                onFailure(Exception("Firebase user was null or anonymous"))
                            }
                        } catch (authError: Exception) {
                            onFailure(authError)
                        }
                    }
                } else {
                    onFailure(Exception("ID token is null"))
                }
            } catch (e: ApiException) {
                onFailure(e)
            }
        }

        fun hasGoogleAccount(context: Context): Boolean {
            val accountManager = AccountManager.get(context)
            val accounts = accountManager.getAccountsByType("com.google")
            return accounts.isNotEmpty()
        }
    }
}