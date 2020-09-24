package com.example.shoppingapp.activities.Login

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppingapp.models.UserModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var loginComplete = MutableLiveData<Boolean>()
    var loginError: MutableLiveData<Throwable> = MediatorLiveData()
    val resetLinkSent = MutableLiveData<Boolean>()
    val resetLinkError = MutableLiveData<Throwable>()
    private val TAG : String = "LOGINVIEWMODEL"

    private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {


        mAuth.signInWithCredential(googleAuthCredential).addOnSuccessListener(OnSuccessListener {
            val newuser :Boolean = it.additionalUserInfo!!.isNewUser
                if(newuser){
                    val firebaseUser: FirebaseUser? = mAuth.getCurrentUser()
                    val userId = firebaseUser!!.uid
                    val email = firebaseUser.email
                    val userName = firebaseUser.displayName
                    val phone = firebaseUser.phoneNumber
                    val profilePic = firebaseUser.photoUrl.toString()
                    val theNewUser : UserModel = UserModel(userId, email, userName, phone, profilePic, null)

                    saveUserToDatabase(theNewUser)
        }else{
                    loginComplete.postValue(true)
                }
    }).addOnFailureListener {
            loginError.postValue(it)
            Log.d(TAG, "signInWithGoogleAuthCredential: "+ it.message)
        }

}

    private fun saveUserToDatabase(userModel: UserModel) {
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        val dbRef : DocumentReference? = userModel.userID?.let { db.collection("users").document(it) }

        dbRef!!.set(userModel).addOnSuccessListener {
            loginComplete.postValue(true)
            Log.d(TAG, "saveUserToDatabase: User Successfully saved to database")
        }.addOnFailureListener {
            Log.d(TAG, "saveUserToDatabase: ")
            loginError.postValue(it)
            Log.d(TAG, "saveUserToDatabase: "+ it.message)
        }

    }

    fun performUserLogin(email : String, password : String){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                loginComplete.postValue(true)
            }else{
                loginError.postValue(it.exception)
                Log.d(TAG, "performUserLogin: "+ it.exception!!.message)
            }
        }
    }
    fun sendPasswordResetLink(resetEmailAddress: String) {
        mAuth.sendPasswordResetEmail(resetEmailAddress)
            .addOnSuccessListener {
                resetLinkSent.postValue(true)
            }
            .addOnFailureListener {
                resetLinkError.postValue(it)
            }
    }
}


