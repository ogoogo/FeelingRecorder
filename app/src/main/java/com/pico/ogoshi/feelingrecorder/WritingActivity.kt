package com.pico.ogoshi.feelingrecorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_writing.*

class WritingActivity : AppCompatActivity() {

    val realm:Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)



        val good2 :Boolean = intent.getBooleanExtra("good",true)
        if (good2==false){
            barometerTextView.text="ヤナコト度"
        }



        val savedIntent:Intent = Intent(this, SavedActivity::class.java)

        saveButton.setOnClickListener {
            startActivity(savedIntent)
            savedIntent.putExtra("good2",good2)
            val event:String=eventText.text.toString()
            val date:Int=dateEditText.text.toString().toInt()
            val barometer:Int=barometerEditText.text.toString().toInt()
            save(event,date,good2,barometer)
        }



    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun save(event:String, date:Int, good2:Boolean, barometer:Int){
        realm.executeTransaction{
           val newMemo:Memo=it.createObject(Memo::class.java)
            newMemo.event=event
            newMemo.date=date
            newMemo.good=good2
            newMemo.barometer=barometer
        }
    }
}