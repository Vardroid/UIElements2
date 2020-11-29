package com.example.uielements2

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.uielements2.models.Song

class AlbumDetails : AppCompatActivity() {
    private lateinit var adapter: MainActivity.MyCustomAdapterList
    private lateinit var arraySongs: MutableList<Song>
    private var albumId = 0

    //function that removes an element in an array given its index
    private fun remove(arr: Array<String>, index: Int): Array<String>{
        if(index < 0 || index >= arr.size){
            return arr
        }

        val result = arr.toMutableList()
        result.removeAt(index)
        return result.toTypedArray()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_details)

        val bundle = intent.extras
        val albumId = bundle?.getInt("albumId")
        arraySongs = AlbumActivity.albums[albumId!!].albumSongs
        findViewById<ImageView>(R.id.albumImg).setImageResource(R.drawable.love_poem)
        findViewById<TextView>(R.id.albumNameTxt).text = AlbumActivity.albums[albumId!!].title

        adapter = MainActivity.MyCustomAdapterList(applicationContext, arraySongs)
        val songList = findViewById<ListView>(R.id.albumSongList)
        Toast.makeText(applicationContext, "${arraySongs.size}", Toast.LENGTH_LONG).show()
        songList.adapter = adapter
        registerForContextMenu(songList)
    }

    //Create Context Menu when you long press an item in the album list
    override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater

        //Add the menu items for the context menu
        inflater.inflate(R.menu.queue_item_menu, menu)
    }

    //Method when a context item is selected
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val bundle = intent.extras
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId){
            R.id.remove_from_queue ->{
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Do you want to remove this song from the album?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", DialogInterface.OnClickListener{
                            _, _ ->
                            AlbumActivity.albums[albumId].albumSongs.removeAt(info.position)
                            arraySongs = AlbumActivity.albums[albumId!!].albumSongs
                            adapter = MainActivity.MyCustomAdapterList(applicationContext, arraySongs)
                            val songList = findViewById<ListView>(R.id.albumSongList)
                            songList.adapter = adapter
                        })
                        .setNegativeButton("No", DialogInterface.OnClickListener{
                            dialog, _ ->
                                dialog.cancel()
                        })

                val alert = dialogBuilder.create()
                alert.setTitle("Remove")
                alert.show()

                return true
            }
            else -> super.onContextItemSelected(item)
        }
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
                startActivity(Intent(this, AddSong::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}