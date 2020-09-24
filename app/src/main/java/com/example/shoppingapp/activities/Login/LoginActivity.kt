package com.example.shoppingapp.activities.Login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppingapp.R
import com.example.shoppingapp.activities.MainActivity
import com.example.shoppingapp.models.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var googleSignInClient : GoogleSignInClient
    val RC_SIGN_IN = 0
    lateinit var progressDialog: ProgressDialog
    val mAuth :FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        googleSignIn.setOnClickListener(View.OnClickListener {
            signIn();
        })

        initGoogleSignInClient()
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions : GoogleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, this.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            progressDialog = ProgressDialog.show(this, "", "Please Wait", true)
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val theGoogleSignInAccount = task.getResult(ApiException::class.java)
                if (theGoogleSignInAccount != null){
                    getGoogleAuthCredential(theGoogleSignInAccount)
                }
            }catch (e : ApiException){
                progressDialog.dismiss()
                Toast.makeText(this, "An error occured ", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun getGoogleAuthCredential(theGoogleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = theGoogleSignInAccount.idToken
        val googleAuthCredential: AuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(googleAuthCredential);

    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        mAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener(this) {
                if (it.isSuccessful){
                    val newuser: Boolean = it.result!!.additionalUserInfo!!.isNewUser

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
                           Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                           goToMainActivity()
                       }
                }else{
                    Toast.makeText(this, "An Error occurred", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun goToMainActivity() {
        progressDialog.dismiss()
        startActivity(Intent(this, MainActivity::class.java))
        finish();
    }

    private fun saveUserToDatabase(userModel: UserModel) {
        val db :FirebaseFirestore  = FirebaseFirestore.getInstance()
        val dbRef : DocumentReference? = userModel.userID?.let { db.collection("users").document(it) }

        dbRef!!.set(userModel).addOnSuccessListener {
            Log.d("LoginActivity", "User successfullly saved")
            goToMainActivity()
        }.addOnFailureListener {
            progressDialog.dismiss()
            Toast.makeText(this, "An error occurred ", Toast.LENGTH_SHORT).show()
            Log.d("LoginActivity", it.message)
        }


    }
}