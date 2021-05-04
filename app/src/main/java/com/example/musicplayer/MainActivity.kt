package com.example.musicplayer


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.media.MediaPlayer.create as create1

class MainActivity : AppCompatActivity() {
    private var buttonLyrics: Button? = null

    // initialization and declaration of variable
    private var play_btn: ImageButton? = null

    private var icon: ImageView? = null
    private var next_btn: ImageButton? = null
    private var previous_btn: ImageButton? = null


    private var seekbar: SeekBar? = null
    private var volumebar: SeekBar? = null

    private var currentTime: TextView? = null
    private var finishTime: TextView? = null
    var title: TextView? = null

    private var handler = Handler()
    lateinit var runable: Runnable

    private var totalTime: Int = 0
    private var position: Int = 0


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLyrics = findViewById(R.id.button_text)


        play_btn = findViewById(R.id.play_btn)
        next_btn = findViewById(R.id.next_btn)
        previous_btn = findViewById(R.id.previous_btn)

        currentTime = findViewById(R.id.current_time)
        finishTime = findViewById(R.id.finish_time)

        seekbar = findViewById(R.id.seekBar)
        volumebar = findViewById(R.id.volumeBar)

        icon = findViewById(R.id.icon)
        title = findViewById(R.id.music_title)

        buttonLyrics?.setOnClickListener(View.OnClickListener {
            openLyrics(position)
        })

        val mediaplayer1: MediaPlayer = create1(this, R.raw.music1)
        val mediaplayer2: MediaPlayer = create1(this, R.raw.music2)
        val mediaplayer3: MediaPlayer = create1(this, R.raw.music3)
        val mediaplayer4: MediaPlayer = create1(this, R.raw.music4)
        val mediaplayer5: MediaPlayer = create1(this, R.raw.music5)
        val mediaplayer6: MediaPlayer = create1(this, R.raw.music6)
        val mediaplayer7: MediaPlayer = create1(this, R.raw.music7)


        val file1: String =application.assets.open("text1.txt").bufferedReader().use { it.readText() }
        val file2: String =application.assets.open("text2.txt").bufferedReader().use { it.readText() }
        val file3: String =application.assets.open("text3.txt").bufferedReader().use { it.readText() }
        val file4: String =application.assets.open("text4.txt").bufferedReader().use { it.readText() }
        val file5: String =application.assets.open("text5.txt").bufferedReader().use { it.readText() }
        val file6: String =application.assets.open("text6.txt").bufferedReader().use { it.readText() }
        val file7: String =application.assets.open("text7.txt").bufferedReader().use { it.readText() }



        val mySong1 = Music("Music", R.drawable.music1, file1, mediaplayer1)
        val mySong2 = Music("Mahabbatym", R.drawable.music2, file2, mediaplayer2)
        val mySong3 = Music("Janym sol", R.drawable.music3, file3, mediaplayer3)
        val mySong4 = Music("Say that again", R.drawable.music4, file4, mediaplayer4)
        val mySong5 = Music("Ана", R.drawable.music5, file5, mediaplayer5)
        val mySong6 = Music("Көзімнің қарасы", R.drawable.music6, file6, mediaplayer6)
        val mySong7 = Music("Жауынгер ", R.drawable.music7, file7, mediaplayer7)



        val myList2: ArrayList<MediaPlayer>? = arrayListOf(
            mediaplayer1,
            mediaplayer2,
            mediaplayer3,
            mediaplayer4,
            mediaplayer5,
            mediaplayer6,
            mediaplayer7
        )

        val myList1: ArrayList<Music>? = arrayListOf(
            mySong1,
            mySong2,
            mySong3,
            mySong4,
            mySong5,
            mySong6,
            mySong7
        )





        var currMusic: Music = myList1!!.get(position)
        var currSong: MediaPlayer = currMusic.song!!
        totalTime = currSong.duration
        title!!.setText(currMusic.name!!)
        icon!!.setImageResource(currMusic.photo!!)
        var currbitmapIcon: Bitmap? =BitmapFactory.decodeResource(applicationContext.resources, currMusic.photo!!)




        seekbar!!.progress = 0
        seekbar!!.max = totalTime



        //process of pause and resume
        play_btn?.setOnClickListener {

            println("position " + position)
            if (!currSong.isPlaying) {
                currSong.start()
                play_btn!!.setImageResource(R.drawable.ic_baseline_pause_24)
            } else {
                currSong.pause()
                play_btn!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }

        }
        //////////////////////////////////


        //process of next song
        next_btn?.setOnClickListener {

            currSong.pause()
            play_btn!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)

            if (position < 6) {
                position = position + 1
            } else {
                position = 0
            }


            currMusic = myList1!!.get(position)
            currSong = currMusic.song!!
            totalTime = currSong.duration
            title!!.setText(currMusic.name!!)
            icon!!.setImageResource(currMusic.photo!!)
            currbitmapIcon=BitmapFactory.decodeResource(applicationContext.resources, currMusic.photo!!)

            currSong.start()


            play_btn!!.setImageResource(R.drawable.ic_baseline_pause_24)

        }
        //////////////////////////////////


        //process of previous song
        previous_btn?.setOnClickListener {

            currSong.pause()
            play_btn!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)

            if (position > 0) {
                position = position - 1
            } else {
                position = 6
            }

            currMusic = myList1!!.get(position)
            currSong = currMusic.song!!
            totalTime = currSong.duration
            title!!.setText(currMusic.name!!)
            icon!!.setImageResource(currMusic.photo!!)
            currbitmapIcon=BitmapFactory.decodeResource(applicationContext.resources, currMusic.photo!!)

            currSong.start()


            play_btn!!.setImageResource(R.drawable.ic_baseline_pause_24)
        }
        //////////////////////////////////


        // Volume Bar
        volumebar!!.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekbar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        var volumeNum = progress / 100.0f
                        currSong.setVolume(volumeNum, volumeNum)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )


        //process of progress bar
        seekbar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, pos: Int, changed: Boolean) {
                if (changed) {
                    currSong.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        runable = Runnable {
            seekbar!!.progress = currSong.currentPosition
            handler?.postDelayed(runable!!, 1000)

        }
        handler?.postDelayed(runable!!, 1000)


        currSong.setOnCompletionListener {
            play_btn!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            seekbar!!.progress = 0
        }
        /////////////////////////////////////////////////////////////////////////


        //process of increasing and decreasing time
        Thread(Runnable {
            while (currSong != null) {
                try {
                    var msg = Message()
                    msg.what = currSong.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()


        @SuppressLint("HandlerLeak")
        handler = object : Handler() {

            override fun handleMessage(msg: Message) {

                var currentPosition = msg.what


                var newInt = totalTime - currentPosition
                var newFunc = newInt / 1000 % 60
                var min = newInt / 1000 / 60


                var elapsedTime = createTimeLabel(currentPosition)
                //set text
                currentTime!!.setText(elapsedTime)

                var remainingTime = createTimeLabel(totalTime - currentPosition)
                //set text
                finishTime!!.setText("-$remainingTime")

                createNotificationChannel()

                if (newFunc == 10 && min == 0) {

                    sendNotification(currbitmapIcon!!, currMusic.name!!)


                }
            }
        }

    }


    fun createTimeLabel(time: Int): String {

        var timelabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timelabel = "$min:"

        if (sec < 10) timelabel += "0"
        timelabel += sec

        return timelabel
    }

    /////////////////////////////////////////////////////////////////////////////////
    fun noAction(min: Int, sec: Int) {
        if (position < 6) {
            position = position + 1
        } else {
            position = 0
        }
    }
    ////////////////////////////////////////////////////////////////

    private val CHANNEL_ID = "channel_id_01"
    private val notificationId = 101

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Noti Name"
            val descriptionText = "some desc"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(currbitmapIcon: Bitmap,name:String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val bitmapLargeIcon = currbitmapIcon


        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(name)
            .setContentText("It will finish after 10 seconds!")
            .setLargeIcon(bitmapLargeIcon)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    fun openLyrics(i: Int) {
        val intent = Intent(this,Reader::class.java)
        intent.putExtra("key", i);
        startActivity(intent)
    }

}