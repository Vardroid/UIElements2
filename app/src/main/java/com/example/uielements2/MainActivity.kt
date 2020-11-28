package com.example.uielements2

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.uielements2.models.Song
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    //function that adds an element to an array
    private fun append(arr: Array<String>, element: String): Array<String>{
        val list: MutableList<String> = arr.toMutableList()
        list.add(element)
        return list.toTypedArray()
    }

    companion object{
        var songQueue: Array<String> = emptyArray()
        var songsArray = arrayOf(
                arrayOf("Love Poem","Visitor","Lullaby","Above The Time","Unlucky","Bleuming","Happy Ending"),
                arrayOf("Autumn Morning","Secret Garden","Sleepless Rainy Night","By the Stream","Everyday With You"),
                arrayOf("This Right Now","Palette","Ending Scene","Can't Love You Anymore","Jam Jam","Black Out","Period","Through The Night","And So Love Is","Dear Name")
        )

        var albumArray = arrayOf("Love Poem","Flower Bookmark II","Palette")
        var albumPics = arrayOf(R.drawable.flower_bookmark_2,R.drawable.love_poem,R.drawable.palette)
        lateinit var adapter: ArrayAdapter<Song>
        lateinit var songs: MutableList<Song>
        lateinit var songsTableHandler: SongsTableHandler
        lateinit var songList: ListView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get the table handler
        songsTableHandler = SongsTableHandler(this)

        //get the records
        songs = songsTableHandler.read()

        //attach to adapter
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, songs)
        songList = findViewById<ListView>(R.id.songList)
        songList.adapter = adapter
        registerForContextMenu(songList)

        val fab: View = findViewById(R.id.addSongFab)
        fab.setOnClickListener {
            startActivity(Intent(this, AddSong::class.java))
        }
    }

    //Create Context Menu when you long press an item in the song list
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater

        //Add the menu items for the context menu
        inflater.inflate(R.menu.item_menu, menu)
    }

    //Method when a context item is selected
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId){
            R.id.add_to_queue -> {
                songQueue = append(songQueue, songs[info.position].toString())
                Snackbar.make(window.decorView, "Song added to queue", Snackbar.LENGTH_LONG).setAction("View", View.OnClickListener {
                    startActivity(Intent(this, QueueActivity::class.java))
                }).show()
                //Toast.makeText(this, "Song added to queue", Toast.LENGTH_LONG).show()
                true
            }
            R.id.edit_song -> {
                //get the song that was selected
                val songId = songs[info.position].id

                //put it in an extra
                val intent = Intent(applicationContext, EditSongActivity::class.java)
                intent.putExtra("songId", songId)

                //start activity
                startActivity(intent)
                true
            }
            R.id.delete_song -> {
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Do you want to delete this song?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", DialogInterface.OnClickListener{
                            _, _ ->
                        val song = songs[info.position]
                        if(songsTableHandler.delete(song)){
                            Toast.makeText(applicationContext, "Song has been deleted.", Toast.LENGTH_LONG).show()
                        }
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener{
                            dialog, _ ->
                        dialog.cancel()
                    })

                val alert = dialogBuilder.create()
                alert.setTitle("Delete Song")
                alert.show()
                true
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