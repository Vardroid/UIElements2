package com.example.uielements2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import kotlinx.android.synthetic.main.activity_queue.*
import kotlinx.android.synthetic.main.album_entry.view.*

class AlbumActivity : AppCompatActivity() {
    var adapter: AlbumAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        adapter = AlbumAdapter(this)
        findViewById<GridView>(R.id.albumList).adapter = adapter
    }

    class AlbumAdapter : BaseAdapter {
        var context: Context? = null
        val albumList = MainActivity.albumArray

        constructor(context: Context) : super(){
            this.context = context
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
                intent.putExtra("name", album)
                intent.putExtra("songList", MainActivity.songsArray)
                intent.putExtra("position", position)
                context!!.startActivity(intent)
            }
            view.imgAlbum.setImageResource(MainActivity.albumPics[position])
            view.name.text = album

            return view
        }

    }
}