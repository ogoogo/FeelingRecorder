package com.pico.ogoshi.feelingrecorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_saved.*

class SavedActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)
        val goodOrBad:Boolean= intent.getBooleanExtra("good2",true)
        val homeIntent: Intent =Intent(this,MainActivity::class.java)
        val writingIntent:Intent=Intent(this,ChoosingActivity::class.java)

        backButton.setOnClickListener {
            startActivity(homeIntent)
        }
        writingButton.setOnClickListener {
            startActivity(writingIntent)
        }
    }
}

