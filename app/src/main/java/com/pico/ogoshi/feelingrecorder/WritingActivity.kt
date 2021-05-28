package com.pico.ogoshi.feelingrecorder

import android.app.Person
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_writing.*
import java.util.*

class WritingActivity : AppCompatActivity() {

    val realm:Realm = Realm.getDefaultInstance()
    var quoteOrNot:Boolean=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)



        val good2 :Boolean = intent.getBooleanExtra("good",true)
        if (good2==false){
            barometerTextView.text="ヤナコト度"
        }

        otherButton.setOnClickListener {
            quoteOrNot=false
            PersonEditText.isVisible=false
            textView2.isVisible=false
            eventText.hint="出来事"
            textView3.isVisible=false
        }
        quoteButton.setOnClickListener {
            quoteOrNot=true
            PersonEditText.isVisible=true
            textView2.isVisible=true
            eventText.hint="セリフ"
            textView3.isVisible=true
        }



        val savedIntent:Intent = Intent(this, SavedActivity::class.java)

        saveButton.setOnClickListener {

            if(quoteOrNot){

                val date:Int=dateEditText.text.toString().toInt()
                val personName:String=PersonEditText.text.toString()
                val quote:String=eventText.text.toString()
                val barometer:Int=barometerEditText.text.toString().toInt()
                val event="「${quote}」by${personName}"
                save(event,date,good2,barometer,personName,quoteOrNot,quote)
                savedIntent.putExtra("quote",quote)
            }else {

                val event: String = eventText.text.toString()
                val date: Int = dateEditText.text.toString().toInt()
                val barometer: Int = barometerEditText.text.toString().toInt()
                save(event, date, good2, barometer,"",quoteOrNot,"")
            }

            savedIntent.putExtra("good2",good2)
            savedIntent.putExtra("quoteOrNot",quoteOrNot)

            startActivity(savedIntent)


        }



    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun save(event:String, date:Int, good:Boolean, barometer:Int,personName:String,quoteOrNot:Boolean,quote:String){
        realm.executeTransaction{
           val newMemo:Memo=it.createObject(Memo::class.java,UUID.randomUUID().toString())
            newMemo.event=event
            newMemo.date=date
            newMemo.good=good
            newMemo.barometer=barometer
            newMemo.personName=personName
            newMemo.quoteOrNot=quoteOrNot
            newMemo.quote=quote

        }
    }
}