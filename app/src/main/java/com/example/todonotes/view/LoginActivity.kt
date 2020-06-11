package com.example.todonotes.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todonotes.utils.AppConstant
import com.example.todonotes.utils.PrefConstant
import com.example.todonotes.R

class LoginActivity : AppCompatActivity() {

    lateinit var editTextFullName : EditText
    lateinit var editTextUserName : EditText
    lateinit var buttonLogin : Button
    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

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
        editTextFullName = findViewById(R.id.editTextFullName)
        editTextUserName = findViewById(R.id.editTextUserName)
        buttonLogin = findViewById(R.id.buttonLogin)

        val clickAction = object : View.OnClickListener{
            override fun onClick(v: View?) {

                val fullName = editTextFullName.text.toString()
                val userName = editTextUserName.text.toString()

                if(fullName.isNotEmpty() && userName.isNotEmpty()){

                    val intent = Intent(this@LoginActivity, MyNotesActivity::class.java)
                    intent.putExtra(AppConstant.FULL_NAME,fullName)
                    startActivity(intent)
                    saveFullName(fullName)
                    saveLoginStatus()
                    finish()
                }else{
                    Toast.makeText(this@LoginActivity,"FullName and UserName can't be empty",Toast.LENGTH_SHORT).show()

                }
            }

        }
        buttonLogin.setOnClickListener(clickAction)
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