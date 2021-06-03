package com.pico.ogoshi.feelingrecorder

import android.app.DatePickerDialog
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

        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)+1
        var  day = c.get(Calendar.DAY_OF_MONTH)


        todayButton.setOnClickListener {
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)+1
            day = c.get(Calendar.DAY_OF_MONTH)
            dateTextView.text="${year}年${month}月${day}日に"
        }

        yesterdayButton.setOnClickListener {
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)+1
            day = c.get(Calendar.DAY_OF_MONTH)-1
            dateTextView.text="${year}年${month}月${day}日に"
        }

        otherDayButton.setOnClickListener {
            val datePickerDialog= DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, y, m, d ->
                    year=y
                    month=m+1
                    day=d
                    dateTextView.text="${year}年${month}月${day}日に"
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }



        saveButton.setOnClickListener {

            if(quoteOrNot){


                val personName:String=PersonEditText.text.toString()
                val quote:String=eventText.text.toString()
                val barometer:Int=barometerEditText.text.toString().toInt()
                val event="「${quote}」by${personName}"
                save(event,day,good2,barometer,personName,quoteOrNot,quote)
                savedIntent.putExtra("quote",quote)
            }else {

                val event: String = eventText.text.toString()

                val barometer: Int = barometerEditText.text.toString().toInt()
                save(event, day, good2, barometer,"",quoteOrNot,"")
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