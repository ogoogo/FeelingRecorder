package com.pico.ogoshi.feelingrecorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    val realm:Realm= Realm.getDefaultInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val editIntent:Intent= Intent(this,EditActivity::class.java)

        val idInQuestion:String?=intent.getStringExtra("idInQuestion")
        val dataInQuestion:Memo?=realm.where(Memo::class.java).equalTo("id",idInQuestion).findFirst()

        if(dataInQuestion?.quoteOrNot==true){
            dateTextViewDetail.text="${dataInQuestion.year}年${dataInQuestion.month}月${dataInQuestion.date}日に"
            PersonEditTextDetail.text=dataInQuestion.personName
            eventTextDetail.text=dataInQuestion.quote
            barometerTextDetail.text=dataInQuestion.barometer.toString()
        }else if (dataInQuestion?.quoteOrNot==false){
            PersonEditTextDetail.isVisible=false
            textView2Detail.isVisible=false
            textView3Detail.isVisible=false
            dateTextViewDetail.text="${dataInQuestion.year}年${dataInQuestion.month}月${dataInQuestion.date}日に"
            eventTextDetail.text=dataInQuestion.event

        }

        editButtonDetail.setOnClickListener {
            editIntent.putExtra("idInQuestion",idInQuestion)
            startActivity(editIntent)
        }
    }
}