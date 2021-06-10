package com.pico.ogoshi.feelingrecorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_choosing.*

class ChoosingActivity : AppCompatActivity() {

    var goodOrBad :Boolean= true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosing)
        val recordIntent:Intent = Intent(this, WritingActivity::class.java)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        goodButton.setOnClickListener {
            goodOrBad=true
            recordIntent.putExtra("good",goodOrBad)
            startActivity(recordIntent)
            finish()

        }

        badButton.setOnClickListener {
            goodOrBad=false

            recordIntent.putExtra("good",goodOrBad)
            startActivity(recordIntent)
            finish()


        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(application, MainActivity::class.java)
        startActivity(intent)
        finish()
        return super.onSupportNavigateUp()
    }
}