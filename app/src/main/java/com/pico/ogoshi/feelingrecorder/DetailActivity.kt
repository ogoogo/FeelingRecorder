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


        val editIntent:Intent= Intent(this,EditActivity::class.java)
        val editedIntent:Intent=Intent(this,MainActivity::class.java)
        diaryTextView.isVisible=false

        val idInQuestion:String?=intent.getStringExtra("idInQuestion")
        val dataInQuestion:Memo?=realm.where(Memo::class.java).equalTo("id",idInQuestion).findFirst()

        if(dataInQuestion?.quoteOrNot==true){
            dateTextViewDetail.text="${dataInQuestion.year}年${dataInQuestion.month}月${dataInQuestion.date}日"
            PersonEditTextDetail.text=dataInQuestion.personName
            eventTextDetail.text=dataInQuestion.quote
            ratingBar.rating=dataInQuestion.barometer.toFloat()
        }else if (dataInQuestion?.quoteOrNot==false){
            PersonEditTextDetail.isVisible=false
            textView2Detail.isVisible=false
            textView3Detail.isVisible=false
            dateTextViewDetail.text="${dataInQuestion.year}年${dataInQuestion.month}月${dataInQuestion.date}日"
            eventTextDetail.text=dataInQuestion.event
            ratingBar.rating=dataInQuestion.barometer.toFloat()
        }

        if(dataInQuestion?.diaryOrNot==true){
            diaryTextView.isVisible=true
            diaryTextView.text=dataInQuestion?.diary
            imageView4.isVisible=false
        }else{
            imageView10.isVisible=false
        }

        editButtonDetail.setOnClickListener {
            editIntent.putExtra("idInQuestion",idInQuestion)
            startActivity(editIntent)
            finish()
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
        finish()
        return super.onSupportNavigateUp()
    }

}