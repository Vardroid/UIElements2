package com.example.uielements2

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
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
        lateinit var adapter: MyCustomAdapterList
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
        adapter = MyCustomAdapterList(this, songs)
        songList = findViewById<ListView>(R.id.songList)
        songList.adapter = adapter
        registerForContextMenu(songList)

        val fab: View = findViewById(R.id.addSongFab)
        fab.setOnClickListener {
            startActivity(Intent(this, AddSong::class.java))
        }
    }

    class MyCustomAdapterList(context: Context, songs: MutableList<Song>): BaseAdapter(){
        private val mContext: Context = context
        private val mSongs: MutableList<Song> = songs

        override fun getCount(): Int {
            return mSongs.size
        }

        override fun getItem(position: Int): Any {
            return mSongs[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.row_main, parent, false)

            val rowMainDescTxt = rowMain.findViewById<TextView>(R.id.rowMainDescTxt)
            val rowMainSongTxt = rowMain.findViewById<TextView>(R.id.rowMainSongTxt)
            rowMainSongTxt.text = mSongs[position].title
            val desc = "${mSongs[position].artist} - ${mSongs[position].album}"
            rowMainDescTxt.text = desc

            return rowMain
        }

    }

    //CONTEXT MENU
    //
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
            R.id.add_to_album -> {
                //get the table handler
                AlbumActivity.albumsTableHandler = AlbumsTableHandler(this)

                //get the records
                AlbumActivity.albums = AlbumActivity.albumsTableHandler.read()

                val arrayAdapt: ArrayAdapter<String> = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1)
                for(album in AlbumActivity.albums){
                    arrayAdapt.add(album.title)
                }

                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setTitle("Choose an Album")
                dialogBuilder.setAdapter(arrayAdapt) { _, which ->
                    AlbumActivity.albums[which].albumSongs.add(songs[info.position])
                    Toast.makeText(applicationContext, "${AlbumActivity.albums[which].albumSongs.size} Song has been added to album.", Toast.LENGTH_LONG).show()
                }
                dialogBuilder.setNegativeButton("Cancel", null)

                val dialog: AlertDialog = dialogBuilder.create()
                dialog.show()

                true
            }
            else -> super.onContextItemSelected(item)
        }
    }


    //OPTIONS MENU
    //
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