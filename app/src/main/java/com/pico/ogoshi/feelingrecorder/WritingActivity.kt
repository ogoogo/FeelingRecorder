package com.pico.ogoshi.feelingrecorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_writing.*
import java.util.*

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

            val event:String=eventText.text.toString()
            val date:Int=dateEditText.text.toString().toInt()
            val barometer:Int=barometerEditText.text.toString().toInt()
            save(event,date,good2,barometer)

            savedIntent.putExtra("good2",good2)
            startActivity(savedIntent)


        }



    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun save(event:String, date:Int, good:Boolean, barometer:Int){
        realm.executeTransaction{
           val newMemo:Memo=it.createObject(Memo::class.java,UUID.randomUUID().toString())
            newMemo.event=event
            newMemo.date=date
            newMemo.good=good
            newMemo.barometer=barometer
        }
    }
}