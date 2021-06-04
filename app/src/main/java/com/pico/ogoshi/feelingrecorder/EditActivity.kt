package com.pico.ogoshi.feelingrecorder

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_writing.*
import java.util.*

class EditActivity : AppCompatActivity() {
    val realm:Realm= Realm.getDefaultInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val editingId =intent.getStringExtra("idInQuestion")
        val editingData = realm.where(Memo::class.java).equalTo("id",editingId).findFirst()
        val editedIntent = Intent(this,MainActivity::class.java)


        val c = Calendar.getInstance()
        var year = editingData?.year
        var month = editingData?.month
        var  day = editingData?.date

        if(editingData?.quoteOrNot==true){
            dateTextViewEdit.text="${year}年${month}月${day}日に"
            PersonEditTextEdit.setText(editingData.personName)
            eventTextEdit.setText(editingData.quote)
            barometerEditTextEdit.setText(editingData.barometer.toString())
        }else if (editingData?.quoteOrNot==false){
            PersonEditTextEdit.isVisible=false
            textView2Edit.isVisible=false
            textView3Edit.isVisible=false
            dateTextViewEdit.text="${year}年${month}月${day}日に"
            eventTextEdit.setText(editingData.event)
            barometerEditTextEdit.setText(editingData.barometer.toString())

        }


        todayButtonEdit.setOnClickListener {
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)+1
            day = c.get(Calendar.DAY_OF_MONTH)
            dateTextViewEdit.text="${year}年${month}月${day}日に"
        }

        yesterdayButtonEdit.setOnClickListener {
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)+1
            day = c.get(Calendar.DAY_OF_MONTH)-1
            dateTextViewEdit.text="${year}年${month}月${day}日に"
        }

        otherDayButtonEdit.setOnClickListener {
            val datePickerDialog= DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, y, m, d ->
                    year=y
                    month=m+1
                    day=d
                    dateTextViewEdit.text="${year}年${month}月${day}日に"
                },
                editingData!!.year,
                editingData!!.month-1,
                editingData!!.date
            )
            datePickerDialog.show()
        }




        saveButtonEdit.setOnClickListener {
            if (editingData?.quoteOrNot==true){
                val newPerson:String = PersonEditTextEdit.text.toString()
                val newQuote :String= eventTextEdit.text.toString()
                val newBarometer :Int = barometerEditTextEdit.text.toString().toInt()
                val newEvent = "「${newQuote}」 by${newPerson}"
                edit(editingId!!,newPerson,newQuote,newBarometer,newEvent,year!!,month!!,day!!)

            }else if (editingData?.quoteOrNot==false){
                val newEvent :String= eventTextEdit.text.toString()
                val newBarometer :Int = barometerEditTextEdit.text.toString().toInt()
                edit(editingId!!,"","",newBarometer,newEvent,year!!,month!!,day!!)
            }
            startActivity(editedIntent)
        }

        deleteButton.setOnClickListener {
            realm.executeTransaction {
                val deletingData=realm.where(Memo::class.java).equalTo("id",editingId).findFirst()
                deletingData?.deleteFromRealm()
            }
            startActivity(editedIntent)
        }



    }
    fun edit(id:String,personName:String,quote:String,barometer:Int,event: String,year:Int,month:Int,day:Int){
        realm.executeTransaction {
            val changingData:Memo?=realm.where(Memo::class.java).equalTo("id",id).findFirst()
            changingData?.year=year
            changingData?.month=month
            changingData?.date=day
            changingData?.personName=personName
            changingData?.quote=quote
            changingData?.barometer=barometer
            changingData?.event=event
        }
    }


}