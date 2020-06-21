package com.example.todonotes.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.todonotes.R
import com.example.todonotes.ValidateInput
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var editTextPasswordAgain: EditText
    lateinit var email: String
    lateinit var password : String
    lateinit var passwordAgain : String
    lateinit var validateInput : ValidateInput
    lateinit var buttonSignup : Button
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextPasswordAgain = findViewById(R.id.editTextPasswordAgain)
        buttonSignup = findViewById(R.id.buttonSignup)
        validateInput = ValidateInput(this)

        buttonSignup.setOnClickListener(View.OnClickListener { handleSignUpBtnClick() })

        mAuth = FirebaseAuth.getInstance()

    }

    private fun handleSignUpBtnClick() {

        email = editTextEmail.text.toString()
        password = editTextPassword.text.toString()
        passwordAgain = editTextPasswordAgain.text.toString()

        if (validateInput.checkIfEmailIsValid(email) && validateInput.checkIfPasswordIsValid(password)) {
            if (password == passwordAgain) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        Toast.makeText(this@SignupActivity, "SignUp successfull for:" + user!!.email, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SignupActivity, "Error occured:" + task.exception, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@SignupActivity, "Password Don't Match. Please Enter Again!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
