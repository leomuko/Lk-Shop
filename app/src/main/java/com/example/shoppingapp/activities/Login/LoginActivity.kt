package com.example.shoppingapp.activities.Login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shoppingapp.R
import com.example.shoppingapp.activities.MainActivity
import com.example.shoppingapp.activities.Register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var googleSignInClient : GoogleSignInClient
    val RC_SIGN_IN = 0
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mLoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        googleSignIn.setOnClickListener(View.OnClickListener {
            signIn();
        })

        buttonLogin.setOnClickListener(View.OnClickListener {
            if (emailInput.text.toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailInput.text.toString()).matches()){
                emailInputLayout.error = "Please Enter Valid Email"
                return@OnClickListener
            }else if (passwordInput.text.toString().trim().isEmpty()){
                passwordInputLayout.error = "Field cant be empty"
                return@OnClickListener
            }else{
                performEmailLogin()
            }
        })

        loginToRegisterText.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        })

        resetPasswordText.setOnClickListener {
            val passwordResetView = layoutInflater.inflate(R.layout.password_reset_layout, null)
            AlertDialog.Builder(this).run {
                setMessage(R.string.message_password_reset)
                setPositiveButton(R.string.dialog_option_confirm, null)
                setNegativeButton(R.string.dialog_option_cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                setView(passwordResetView)

                create().apply {
                    setOnShowListener {
                        handlePositiveButtonClick(passwordResetView)
                    }
                    show()
                }
            }
        }

        initGoogleSignInClient()
        initialiseViewModel()
    }

    private fun performEmailLogin() {
        progressDialog = ProgressDialog.show(this, "", "Please Wait", true)
        val theEmail = emailInput.text.toString()
        val thePassword = passwordInput.text.toString()
        mLoginViewModel.performUserLogin(theEmail, thePassword)
        logUserIn()
    }

    private fun initialiseViewModel() {
        mLoginViewModel =  ViewModelProvider(this)[LoginViewModel::class.java]
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
        mLoginViewModel.signInWithGoogleAuthCredential(googleAuthCredential)
        logUserIn()

    }

    private fun logUserIn() {
        mLoginViewModel.loginComplete.observe(this, Observer {
            if (it == true){
                Toast.makeText(this, "User Successfully signed in", Toast.LENGTH_SHORT).show()
                goToMainActivity()
            }
        })
        mLoginViewModel.loginError.observe(this, Observer {
            progressDialog.dismiss()
            Toast.makeText(this, "Error "+ it.message + " occurred", Toast.LENGTH_SHORT).show()
        })
    }


    private fun goToMainActivity() {
        progressDialog.dismiss()
        startActivity(Intent(this, MainActivity::class.java))
        finish();
    }
    private fun AlertDialog.handlePositiveButtonClick(passwordResetView: View) {
        getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            passwordResetView.findViewById<EditText>(R.id.inputResetEmail).run {
                val resetEmail = text.toString()
                if (!Patterns.EMAIL_ADDRESS.matcher(resetEmail).matches()) {
                    error = getString(R.string.error_message_enter_valid_email)
                } else {
                    val progressBar =
                        passwordResetView.findViewById<ProgressBar>(R.id.progressBarPasswordReset)

                    mLoginViewModel.apply {
                        resetLinkSent.observe(this@LoginActivity, Observer {
                            Toast.makeText(context, context.getString(R.string.reset_email_message),Toast.LENGTH_SHORT ).show()
                            dismiss()
                        })
                        resetLinkError.observe(this@LoginActivity, Observer {
                            progressBar.visibility = View.GONE
                            if (it is FirebaseAuthInvalidUserException) {
                                error = getString(R.string.error_message_no_user_with_email)
                            } else {
                                Toast.makeText(context, context.getString(R.string.reset_email_error_message),Toast.LENGTH_SHORT ).show()
                            }
                        })

                        progressBar.visibility = View.VISIBLE
                        sendPasswordResetLink(resetEmail)
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}