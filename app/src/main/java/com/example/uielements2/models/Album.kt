package com.example.uielements2.models

class Album(var id: Int = 0, var title: String, var releaseDate: String) {
    val albumSongs: MutableList<Song>
        get() {
            return albumSongs
        }

    override fun toString(): String {
        return "$title - $releaseDate"
    }
}