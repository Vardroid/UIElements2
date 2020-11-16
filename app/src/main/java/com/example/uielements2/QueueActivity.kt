package com.example.uielements2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class QueueActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue)

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MainActivity.songQueue)
        val queueList = findViewById<ListView>(R.id.queueList)
        queueList.adapter = adapter
    }
}