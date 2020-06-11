package com.example.todonotes.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.todonotes.NotesApp
import com.example.todonotes.utils.AppConstant
import com.example.todonotes.utils.PrefConstant
import com.example.todonotes.R
import com.example.todonotes.adapter.NotesAdapter
import com.example.todonotes.clicklistener.ItemClickListener
import com.example.todonotes.db.Notes
import com.example.todonotes.workmanager.MyWorker

import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import java.util.concurrent.TimeUnit

class MyNotesActivity : AppCompatActivity() {
    var fullName: String? = null
    var fabAddNotes: FloatingActionButton? = null
    var sharedPreferences: SharedPreferences? = null
    var TAG = "MyNotesActivity"
    var recyclerViewNotes: RecyclerView? = null
    var notesList = ArrayList<Notes>()
    val ADD_NOTES_CODE =100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_notes)
        bindView()
        setupSharedPreferences()
        intentData
        getDataFromDatabase()
        fabAddNotes!!.setOnClickListener { //setupDialogBox()
            val intent = Intent (this@MyNotesActivity,AddNotesActivity::class.java)
            startActivityForResult(intent,ADD_NOTES_CODE)
        }
        supportActionBar!!.title = fullName
        setupRecyclerView()
        setupWorkManager()
    }

    private fun setupWorkManager() {
        val constraint = Constraints.Builder()
                .build()
        val request = PeriodicWorkRequest.Builder(MyWorker::class.java,1,TimeUnit.MINUTES)
                .setConstraints(constraint)
                .build()
        WorkManager.getInstance().enqueue(request)
    }

    private fun getDataFromDatabase() {
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesDb().notesDao()
        val listOfNotes = notesDao.getAll()
        notesList.addAll(notesDao.getAll())
    }

    private fun setupSharedPreferences() {
        sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private val intentData: Unit
        private get() {
            val intent = intent
            if(intent.hasExtra(AppConstant.FULL_NAME)) {
                fullName = intent.getStringExtra(AppConstant.FULL_NAME)
            }
            if (TextUtils.isEmpty(fullName)) {
                fullName = sharedPreferences!!.getString(PrefConstant.FULL_NAME, "")
            }
        }

    private fun bindView() {
        fabAddNotes = findViewById(R.id.fabAddNotes)
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes)
    }

    private fun setupDialogBox() {
        val view = LayoutInflater.from(this@MyNotesActivity).inflate(R.layout.add_notes_dialog_layout, null)
        val editTextTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val editTextDescription = view.findViewById<EditText>(R.id.editTextDescription)
        val buttonSubmit = view.findViewById<Button>(R.id.buttonSubmit)
        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .create()
        buttonSubmit.setOnClickListener {
            val title = editTextTitle.text.toString()
            val description = editTextDescription.text.toString()
            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {
                val notes = Notes(title = title, description = description)
                notesList.add(notes)
                addNotesToDb(notes)
            } else {
                Toast.makeText(this@MyNotesActivity, "Title or Description can't be empty", Toast.LENGTH_SHORT).show()
            }


            dialog.hide()
        }
        dialog.show()
    }

    private fun addNotesToDb(notes: Notes) {
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesDb().notesDao()
        notesDao.insert(notes)

    }


    private fun setupRecyclerView() {
        val itemClickListener: ItemClickListener = object : ItemClickListener {
            override fun onClick(notes: Notes) {
                val intent = Intent(this@MyNotesActivity, DetailActivity::class.java)
                intent.putExtra(AppConstant.TITLE, notes.title)
                intent.putExtra(AppConstant.DESCRIPTION, notes.description)
                startActivity(intent)
            }

            override fun onUpdate(notes: Notes) {
                val notesApp = applicationContext as NotesApp
                val notesDao = notesApp.getNotesDb().notesDao()
                notesDao.updateNotes(notes)

            }
        }
        val notesAdapter = NotesAdapter(notesList, itemClickListener)
        val linearLayoutManager = LinearLayoutManager(this@MyNotesActivity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerViewNotes!!.layoutManager = linearLayoutManager
        recyclerViewNotes!!.adapter = notesAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTES_CODE){
            val title = data?.getStringExtra(AppConstant.TITLE)
            val description = data?.getStringExtra(AppConstant.DESCRIPTION)
            val imagePath = data?.getStringExtra(AppConstant.IMAGE_PATH)

            val notes =Notes(title = title!!,description = description!!,imagePath = imagePath!!, isTaskCompleted = false )
            addNotesToDb(notes)
            notesList.add(notes)
            recyclerViewNotes!!.adapter?.notifyItemChanged(notesList.size -1)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.blog){
            val intent = Intent(this@MyNotesActivity,BlogActivity::class.java)
            startActivity(intent)

            Log.d(TAG,"Click Successful")
        }

        return super.onOptionsItemSelected(item)
    }
}
