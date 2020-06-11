package com.example.todonotes.clicklistener

import com.example.todonotes.db.Notes


interface ItemClickListener {
    fun onClick(notes: Notes)
    fun onUpdate(notes: Notes)
}