package com.example.neat_people_app.data.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.amazonaws.mobileconnectors.cognitoidentityprovider.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.regions.Regions
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import com.example.neat_people_app.resources.Secrets
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CognitoAuthService(context: Context) {
    // Initialize Cognito User Pool; Use Secrets.kt constants
    val userPool: CognitoUserPool = CognitoUserPool(
        context,
        Secrets.COGNITO_USER_POOL_ID,
        Secrets.COGNITO_CLIENT_ID,
        Secrets.COGNITO_CLIENT_SECRET,
        Regions.fromName(Secrets.DYNAMO_DB_REGION)
    )

    // Initialize EncryptedSharedPreferences
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // Store the session token after login
    fun storeSessionToken(session: CognitoUserSession) {
        sharedPreferences.edit()
            .putString("access_token", session.accessToken.jwtToken)
            .putString("refresh_token", session.refreshToken.token)
            .apply()
    }

    // Retrieve the access token
    fun getSessionToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    // Clear the session token on logout (optional)
    fun clearSessionToken() {
        sharedPreferences.edit()
            .remove("access_token")
            .remove("refresh_token")
            .apply()
    }


    suspend fun signUp(email: String, password: String):
            Result<Unit> = suspendCoroutine { continuation ->

        val userAttributes = CognitoUserAttributes().apply {
            addAttribute("email", email)
        }
        userPool.signUpInBackground(
            email,
            password,
            userAttributes, null,
            object : SignUpHandler {

            override fun onSuccess(user: CognitoUser?, signUpResult: SignUpResult?) {
                continuation.resume(Result.success(Unit))
            }
            override fun onFailure(exception: Exception?) {
                continuation.resume(Result.failure(exception ?: Exception("Unknown error")))
            }
        })
    }

    suspend fun login(email: String, password: String):
            Result<CognitoUserSession> = suspendCoroutine { continuation ->
        val user = userPool.getUser(email)
        user.getSessionInBackground(object : AuthenticationHandler {
            override fun onSuccess(session: CognitoUserSession?, newDevice: CognitoDevice?) {
                session?.let { continuation.resume(Result.success(it)) }
                    ?: continuation.resume(Result.failure(Exception("Session is null")))
            }
            override fun getAuthenticationDetails(authenticationContinuation:
                                                  AuthenticationContinuation?, userId: String?) {
                val authDetails = AuthenticationDetails(email, password, null)
                authenticationContinuation?.setAuthenticationDetails(authDetails)
                authenticationContinuation?.continueTask()
            }
            override fun onFailure(exception: Exception?) {
                continuation.resume(Result.failure(exception ?: Exception("Unknown error")))
            }
            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                continuation?.continueTask()
            }
            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                continuation?.continueTask()
            }
        })
    }

    suspend fun getUserAttributes():
            Result<Map<String, String>> = suspendCoroutine { continuation ->
        val user = userPool.currentUser
        user.getDetailsInBackground(object : GetDetailsHandler {
            override fun onSuccess(userDetails: CognitoUserDetails?) {
                val attributes =
                    userDetails?.attributes?.attributes?.mapValues { it.value } ?: emptyMap()
                continuation.resume(Result.success(attributes))
            }
            override fun onFailure(exception: Exception?) {
                continuation.resume(Result.failure(exception
                    ?: Exception("Failed to get user attributes")))
            }
        })
    }
}