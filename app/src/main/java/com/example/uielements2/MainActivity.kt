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

class MainActivity : AppCompatActivity() {
    private fun append(arr: Array<String>, element: String): Array<String>{
        val list: MutableList<String> = arr.toMutableList()
        list.add(element)
        return list.toTypedArray()
    }

    companion object{
        var songQueue: Array<String> = emptyArray()
        val songsArray = arrayOf("Love Poem","Visitor","Lullaby","Above The Time","Unlucky","Bleuming","Happy Ending"
            ,"Autumn Morning","Secret Garden","Sleepless Rainy Night","By the Stream","Everyday With You"
            ,"This Right Now","Palette","Ending Scene","Can't Love You Anymore","Jam Jam","Black Out","Period","Through The Night","And So Love Is","Dear Name")
        val albumArray = arrayOf("Love Poem","Flower Bookmark II","Palette")
        val albumPics = arrayOf(R.drawable.flower_bookmark_2,R.drawable.love_poem,R.drawable.palette)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songsArray)
        val songList = findViewById<ListView>(R.id.songList)
        songList.adapter = adapter
        registerForContextMenu(songList)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.item_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId){
            R.id.add_to_queue ->{
                songQueue = append(songQueue, songsArray[info.position])
                Toast.makeText(this, "Song added to queue", Toast.LENGTH_LONG).show()
                return true
            }
            else -> super.onContextItemSelected(item)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

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