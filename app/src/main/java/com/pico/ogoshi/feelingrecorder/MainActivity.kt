package com.pico.ogoshi.feelingrecorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val realm:Realm = Realm.getDefaultInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val writingIntent: Intent = Intent(this@MainActivity, ChoosingActivity::class.java)

        writeButton.setOnClickListener{
            startActivity(writingIntent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}