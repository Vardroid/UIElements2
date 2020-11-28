package com.example.uielements2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.uielements2.models.Album
import com.example.uielements2.models.Song

class AlbumsTableHandler(var context: Context): SQLiteOpenHelper(context, AlbumsTableHandler.DATABASE_NAME, null, AlbumsTableHandler.DATABASE_VERSION)  {
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "albums_database"
        private const val TABLE_NAME = "songs"
        private const val COL_ID = "id"
        private const val COL_TITLE = "title"
        private const val COL_RELEASE_DATE = "release_date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //define the query
        val query = "CREATE TABLE "+TABLE_NAME+" ("+COL_ID+" INTEGER PRIMARY KEY, "+COL_TITLE+" TEXT, "+ COL_RELEASE_DATE+" TEXT)"

        //execute
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS"+ TABLE_NAME)
        onCreate(db)
    }

    fun add(album: Album): Boolean{
        //get database
        val database = this.writableDatabase
        //set the ContentValues
        val contentValues = ContentValues()
        contentValues.put(COL_TITLE, album.title)
        contentValues.put(COL_RELEASE_DATE, album.releaseDate)
        //insert
        var result = database.insert(TABLE_NAME, null, contentValues)
        //check for result
        if(result == (0).toLong()){
            return false
        }
        return true
    }

    fun read(): MutableList<Album> {
        val albumList: MutableList<Album> = ArrayList<Album>()
        val query = "SELECT * FROM "+ TABLE_NAME
        val database = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = database.rawQuery(query, null)
        }catch(e: SQLiteException){
            return albumList
        }

        var id: Int
        var title: String
        var releaseDate: String
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                title = cursor.getString(cursor.getColumnIndex(COL_TITLE))
                releaseDate = cursor.getString(cursor.getColumnIndex(COL_RELEASE_DATE))
                val album = Album(id, title, releaseDate)
                albumList.add(album)
            }while(cursor.moveToNext())
        }
        return albumList
    }

    fun readOne(albumId: Int): Album {
        var album: Album = Album(0, "", "")
        val query = "SELECT * FROM ${TABLE_NAME} WHERE id = $albumId"
        val database = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = database.rawQuery(query, null)
        }catch(e: SQLiteException){
            return album
        }

        var id: Int
        var title: String
        var releaseDate: String

        if(cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex(COL_ID))
            title = cursor.getString(cursor.getColumnIndex(COL_TITLE))
            releaseDate = cursor.getString(cursor.getColumnIndex(COL_RELEASE_DATE))
            album = Album(id, title, releaseDate)
        }
        return album
    }

    fun update(album: Album): Boolean{
        //get database
        val database = this.writableDatabase
        //set the ContentValues
        val contentValues = ContentValues()
        contentValues.put(COL_TITLE, album.title)
        contentValues.put(COL_RELEASE_DATE, album.releaseDate)
        //insert
        var result = database.update(TABLE_NAME, contentValues, "id="+album.id, null)
        //check for result
        if(result == 0){
            return false
        }
        return true
    }

    fun delete(album: Album): Boolean{
        val database = this.writableDatabase
        val result = database.delete(TABLE_NAME, "id = ${album.id}", null)
        if(result == 0){
            return false
        }
        return true
    }
}