package com.example.shoppingapp.activities.Register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.example.shoppingapp.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener(View.OnClickListener {
            if (firstNameInput.text == null){
                firstNameInputLayout.error = getString(R.string.first_name_error)
            }else if (lastNameInput.text == null){
                lastNameInputLayout.error = getString(R.string.last_name_error)
            }else if (registerEmailInputText.text == null || !Patterns.EMAIL_ADDRESS.matcher(registerEmailInputText.text.toString()).matches()){
                registerEmailInputLayout.error = getString(R.string.email_input_error)
            }else if (registerPasswordInputText.text == null || registerPasswordInputText.text!!.length < 6){
                registerPasswordInputLayout.error = getString(R.string.password_length_error)
            }else if(confirmInputText.text == null || confirmInputText.text != registerPasswordInputText.text){
                confirmInputLayout.error = getString(R.string.confirm_password_error)
            }else{
                registerUser()
            }
        })
    }

    private fun registerUser() {

    }
}