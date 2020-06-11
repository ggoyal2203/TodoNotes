package com.example.todonotes.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.todonotes.utils.AppConstant
import com.example.todonotes.R

class DetailActivity : AppCompatActivity(){

    val TAG = "DetailActivity"
    lateinit var textViewTitle : TextView
    lateinit var textViewDescription : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        bindView()
        setupIntentData()
    }

    private fun setupIntentData() {
        val intent = intent
        val title = intent.getStringExtra(AppConstant.TITLE)
        val description = intent.getStringExtra(AppConstant.DESCRIPTION)
        textViewTitle.text = title
        textViewDescription.text = description

    }

    private fun bindView() {

        textViewTitle = findViewById(R.id.textViewTitle)
        textViewDescription = findViewById(R.id.textViewDescription)
    }
}