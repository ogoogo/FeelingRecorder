package com.pico.ogoshi.feelingrecorder

import android.app.AlertDialog
import android.content.Context
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val shr = getSharedPreferences("beginner", Context.MODE_PRIVATE)
        val editor=shr.edit()
        var beginnerNumber=shr.getInt("number",0)
        if(beginnerNumber==1){
            editor.putInt("number",2)
            editor.apply()
            }

        val editIntent:Intent= Intent(this,EditActivity::class.java)
        val editedIntent:Intent=Intent(this,MainActivity::class.java)
        diaryTextView.isVisible=false

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
            barometerTextDetail.text=dataInQuestion?.barometer.toString()
        }

        if(dataInQuestion?.diaryOrNot==true){
            diaryTextView.isVisible=true
            diaryTextView.text=dataInQuestion?.diary
        }

        editButtonDetail.setOnClickListener {
            editIntent.putExtra("idInQuestion",idInQuestion)
            startActivity(editIntent)
        }
        deleteButton.setOnClickListener {
            AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                .setTitle("消去しますか？")
                .setMessage("内容を元に戻すことはできません")
                .setPositiveButton("消去", { dialog, which ->
                    realm.executeTransaction {
                        val deletingData=realm.where(Memo::class.java).equalTo("id",idInQuestion).findFirst()
                        deletingData?.deleteFromRealm()
                    }
                    startActivity(editedIntent)
                })
                .setNegativeButton("キャンセル", { dialog, which ->

                })
                .show()


        }

    }
    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(application, MainActivity::class.java)
        startActivity(intent)
        finish()
        return super.onSupportNavigateUp()
    }
}