package com.example.uielements2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQuery
import android.provider.MediaStore
import com.example.uielements2.models.Song
import java.net.ContentHandler

class SongsTableHandler(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "songs_database"
        private const val TABLE_NAME = "songs"
        private const val COL_ID = "id"
        private const val COL_TITLE = "title"
        private const val COL_ARTIST = "artist"
        private const val COL_ALBUM = "album"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //define the query
        val query = "CREATE TABLE "+TABLE_NAME+" ("+COL_ID+" INTEGER PRIMARY KEY, "+COL_TITLE+" TEXT, "+COL_ARTIST+" TEXT, "+COL_ALBUM+" TEXT)"

        //execute
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS"+ TABLE_NAME)
        onCreate(db)
    }

    fun add(song: Song): Boolean{
        //get database
        val database = this.writableDatabase
        //set the ContentValues
        val contentValues = ContentValues()
        contentValues.put(COL_TITLE, song.title)
        contentValues.put(COL_ARTIST, song.artist)
        contentValues.put(COL_ALBUM, song.album)
        //insert
        var result = database.insert(TABLE_NAME, null, contentValues)
        //check for result
        if(result == (0).toLong()){
            return false
        }
        return true
    }

    fun read(): MutableList<Song> {
        val songList: MutableList<Song> = ArrayList<Song>()
        val query = "SELECT * FROM "+ TABLE_NAME
        val database = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = database.rawQuery(query, null)
        }catch(e: SQLiteException){
            return songList
        }

        var id: Int
        var title: String
        var artist: String
        var album: String
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                title = cursor.getString(cursor.getColumnIndex(COL_TITLE))
                artist = cursor.getString(cursor.getColumnIndex(COL_ARTIST))
                album = cursor.getString(cursor.getColumnIndex(COL_ALBUM))
                val song = Song(id, title, artist, album)
                songList.add(song)
            }while(cursor.moveToNext())
        }
        return songList
    }

    fun readOne(songId: Int): Song {
        var song: Song = Song(0, "", "", "")
        val query = "SELECT * FROM ${TABLE_NAME} WHERE id = $songId"
        val database = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = database.rawQuery(query, null)
        }catch(e: SQLiteException){
            return song
        }

        var id: Int
        var title: String
        var artist: String
        var album: String

        if(cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex(COL_ID))
            title = cursor.getString(cursor.getColumnIndex(COL_TITLE))
            artist = cursor.getString(cursor.getColumnIndex(COL_ARTIST))
            album = cursor.getString(cursor.getColumnIndex(COL_ALBUM))
            song = Song(id, title, artist, album)
        }
        return song
    }

    fun update(song: Song): Boolean{
        //get database
        val database = this.writableDatabase
        //set the ContentValues
        val contentValues = ContentValues()
        contentValues.put(COL_TITLE, song.title)
        contentValues.put(COL_ARTIST, song.artist)
        contentValues.put(COL_ALBUM, song.album)
        //insert
        var result = database.update(TABLE_NAME, contentValues, "id="+song.id, null)
        //check for result
        if(result == 0){
            return false
        }
        return true
    }

    fun delete(song: Song): Boolean{
        val database = this.writableDatabase
        val result = database.delete(TABLE_NAME, "id = ${song.id}", null)
        if(result == 0){
            return false
        }
        return true
    }
}