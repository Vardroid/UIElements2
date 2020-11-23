package com.example.uielements2

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songsArray.flatten())
        val songList = findViewById<ListView>(R.id.songList)
        songList.adapter = adapter
        registerForContextMenu(songList)
    }

    //Create Context Menu when you long press an item in the song list
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater

        //Add the menu items for the context menu
        inflater.inflate(R.menu.item_menu, menu)
    }

    //Method when a context item is selected
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId){
            R.id.add_to_queue ->{
                songQueue = append(songQueue, songsArray.flatten()[info.position])
                Snackbar.make(window.decorView, "Song added to queue", Snackbar.LENGTH_LONG).setAction("View", View.OnClickListener {
                    startActivity(Intent(this, QueueActivity::class.java))
                }).show()
                //Toast.makeText(this, "Song added to queue", Toast.LENGTH_LONG).show()
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
                true
            }
            R.id.album ->{
                startActivity(Intent(this, AlbumActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}