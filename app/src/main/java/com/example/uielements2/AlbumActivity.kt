package com.example.uielements2

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.uielements2.models.Album
import com.example.uielements2.models.Song
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_queue.*
import kotlinx.android.synthetic.main.album_entry.view.*

class AlbumActivity : AppCompatActivity() {
    companion object {
        var adapter: AlbumAdapter? = null
        lateinit var albumsTableHandler: AlbumsTableHandler
        lateinit var albums: MutableList<Album>
        lateinit var albumGrid: GridView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        //get the table handler
        albumsTableHandler = AlbumsTableHandler(this)

        //get the records
        albums = albumsTableHandler.read()

        adapter = AlbumAdapter(this, albums)
        albumGrid = findViewById<GridView>(R.id.albumList)
        albumGrid.adapter = adapter
        registerForContextMenu(albumGrid)

        val fab: View = findViewById(R.id.addAlbumFab)
        fab.setOnClickListener {
            startActivity(Intent(this, AddAlbum::class.java))
        }
    }

    //Create Context Menu when you long press an item in the song list
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater

        //Add the menu items for the context menu
        inflater.inflate(R.menu.album_item_menu, menu)
    }

    //Method when a context item is selected
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId){
            R.id.edit_album -> {
                //get the song that was selected
                val albumId = albums[info.position].id

                //put it in an extra
                val intent = Intent(applicationContext, EditAlbumActivity::class.java)
                intent.putExtra("albumId", albumId)

                //start activity
                startActivity(intent)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    class AlbumAdapter : BaseAdapter {
        var context: Context? = null
        var albumList: MutableList<Album>

        constructor(context: Context, albumList: MutableList<Album>) : super() {
            this.context = context
            this.albumList = albumList
        }

        override fun getCount(): Int {
            return albumList.size
        }

        override fun getItem(position: Int): Any {
            return albumList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val album = this.albumList[position]

            var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var view = inflator.inflate(R.layout.album_entry, null)
            view.imgAlbum.setOnClickListener {
                val intent = Intent(context, AlbumDetails::class.java)
                intent.putExtra("position", position)
                context!!.startActivity(intent)
            }
            view.imgAlbum.setImageResource(MainActivity.albumPics[position])
            view.name.text = album.toString()

            return view
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
                //startActivity(Intent(this, AlbumActivity::class.java))
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