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
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog

class AlbumDetails : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var arraySongs: Array<String>

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
        val albumName = bundle?.getString("name")
        arraySongs = MainActivity.songsArray[bundle?.getInt("position")!!]
        findViewById<ImageView>(R.id.albumImg).setImageResource(MainActivity.albumPics[bundle.getInt("position")])
        findViewById<TextView>(R.id.albumNameTxt).text = albumName

        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arraySongs)
        val songList = findViewById<ListView>(R.id.albumSongList)
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
                                MainActivity.songsArray[bundle?.getInt("position")!!] = remove(MainActivity.songsArray[bundle.getInt("position")], info.position)
                                arraySongs = MainActivity.songsArray[bundle.getInt("position")]
                                adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arraySongs)
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
}