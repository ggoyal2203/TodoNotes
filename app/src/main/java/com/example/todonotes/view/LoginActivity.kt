package com.example.todonotes.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todonotes.utils.AppConstant
import com.example.todonotes.utils.PrefConstant
import com.example.todonotes.R
import com.example.todonotes.ValidateInput
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var editTextFullName : EditText
    lateinit var editTextEmail : EditText
    lateinit var editTextPassword : EditText
    lateinit var buttonLogin : Button
    lateinit var textViewSignup : TextView
    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    lateinit var validateInput: ValidateInput
    lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bindView()
        setupSharedPreferences()
    }

    private fun setupSharedPreferences() {

        sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private fun bindView() {

        validateInput = ValidateInput(this)

        editTextFullName = findViewById(R.id.editTextFullName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        textViewSignup = findViewById(R.id.textViewSignup)

        mAuth = FirebaseAuth.getInstance()

        val clickAction = object : View.OnClickListener{
            override fun onClick(v: View?) {

                val fullName = editTextFullName.text.toString()
                val email = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()

                if(fullName.isNotEmpty() && validateInput.checkIfEmailIsValid(email) && validateInput.checkIfPasswordIsValid(password)){

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            val intent = Intent(this@LoginActivity, MyNotesActivity::class.java)
                            intent.putExtra(AppConstant.FULL_NAME,fullName)
                            startActivity(intent)
                            saveFullName(fullName)
                            saveLoginStatus()
                            finish()

                        }else{
                            Toast.makeText(this@LoginActivity, "Error occured:" + task.exception, Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this@LoginActivity,"FullName,Email,Password can't be empty",Toast.LENGTH_SHORT).show()

                }

            }

        }
        buttonLogin.setOnClickListener(clickAction)
        textViewSignup.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                intent = Intent(this@LoginActivity,SignupActivity::class.java)
                startActivity(intent)
            }

        })
    }

    private fun saveLoginStatus() {
        editor = sharedPreferences.edit()
        editor.putBoolean(PrefConstant.IS_LOGGED_IN,true)
        editor.apply()
    }

    private fun saveFullName(fullName: String) {

        editor = sharedPreferences.edit()
        editor.putString(PrefConstant.FULL_NAME,fullName)
        editor.apply()

    }
}