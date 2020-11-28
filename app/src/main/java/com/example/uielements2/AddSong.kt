package com.example.uielements2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.uielements2.models.Song

class AddSong : AppCompatActivity() {
    lateinit var addSongBtn: Button
    lateinit var titleTxt: EditText
    lateinit var artistTxt: EditText
    lateinit var albumTxt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_song)

        val databaseHander = SongsTableHandler(this)

        titleTxt = findViewById(R.id.addTitleTxt)
        artistTxt = findViewById(R.id.addArtistTxt)
        albumTxt = findViewById(R.id.addAlbumTxt)
        addSongBtn = findViewById(R.id.addSongBtn)
        addSongBtn.setOnClickListener {
            //get the fields from the form
            val title = titleTxt.text.toString()
            val artist = artistTxt.text.toString()
            val album = albumTxt.text.toString()

            val song = Song(title = title, artist = artist, album = album)
            if(databaseHander.add(song)){
                Toast.makeText(applicationContext, "Song was Added", Toast.LENGTH_LONG).show()
                clearFields()
            }else{
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun clearFields() {
        titleTxt.text.clear()
        artistTxt.text.clear()
        albumTxt.text.clear()
    }

    //Add the options for the main menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }
    //Method when an option in the main menu is selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.queue ->{
                startActivity(Intent(this, QueueActivity::class.java))
                true
            }
            R.id.songs ->{
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            R.id.album ->{
                startActivity(Intent(this, AlbumActivity::class.java))
                true
            }
            R.id.add ->{
                //startActivity(Intent(this, AddSong::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}