package com.example.uielements2

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_queue.*

class QueueActivity : AppCompatActivity() {
    lateinit var adapter: ArrayAdapter<String>

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"

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
        setContentView(R.layout.activity_queue)
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MainActivity.songQueue)
        val queueList = findViewById<ListView>(R.id.queueList)
        queueList.adapter = adapter
        registerForContextMenu(queueList)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
        inflater.inflate(R.menu.queue_item_menu, menu)
    }

    //Method when a context item is selected
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId){
            R.id.remove_from_queue ->{
                MainActivity.songQueue = remove(MainActivity.songQueue, info.position)
                adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MainActivity.songQueue)
                val queueList = findViewById<ListView>(R.id.queueList)
                queueList.adapter = adapter
                Toast.makeText(this, "Song removed from queue", Toast.LENGTH_LONG).show()
                if(MainActivity.songQueue.size == 0){
                    val intent = Intent(this, MainActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        notificationChannel = NotificationChannel(
                                channelId,description, NotificationManager.IMPORTANCE_HIGH)
                        notificationChannel.enableLights(true)
                        notificationChannel.lightColor = Color.GREEN
                        notificationChannel.enableVibration(false)
                        notificationManager.createNotificationChannel(notificationChannel)

                        builder = Notification.Builder(this,channelId)
                                .setContentTitle("Empty Queue")
                                .setContentText("There are no more songs in queue.")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setLargeIcon(BitmapFactory.decodeResource(this.resources,
                                        R.drawable.ic_launcher_background))
                                .setContentIntent(pendingIntent)
                    }else{

                        builder = Notification.Builder(this)
                                .setContentTitle("Example")
                                .setContentText("Hello Hello")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setLargeIcon(BitmapFactory.decodeResource(this.resources,
                                        R.drawable.ic_launcher_background))
                                .setContentIntent(pendingIntent)
                    }
                    notificationManager.notify(1234,builder.build())
                }
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}