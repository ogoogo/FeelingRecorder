package com.pico.ogoshi.feelingrecorder

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isEmpty
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_saved.*
import kotlin.random.Random

class SavedActivity : AppCompatActivity() {
    val realm: Realm = Realm.getDefaultInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)
        val goodOrBad:Boolean= intent.getBooleanExtra("good2",true)
        val quoteOrNot:Boolean=intent.getBooleanExtra("quoteOrNot",true)
        val homeIntent: Intent =Intent(this,MainActivity::class.java)

        val shr = getSharedPreferences("beginner", Context.MODE_PRIVATE)
        val editor=shr.edit()
        var beginnerNumber=shr.getInt("number",0)
        if(beginnerNumber==0){
            editor.putInt("number",1)
            editor.apply()
        }

        backButton.setOnClickListener {
            startActivity(homeIntent)
        }


        if(goodOrBad==false) {
            val goodfeelings: RealmResults<Memo> = readGood()
            imageView3.setImageResource(R.drawable.kotori4)
            if (goodfeelings.isEmpty()){
                messageTextView.text="おつかれさまです！\nきっと明日はイイコトありますよ！！"

            }else{
                val randomGood = goodfeelings[Random.nextInt(goodfeelings.size)]
                val goodText :String = randomGood?.event.toString()
                val goodYear:String = randomGood?.year.toString()
                val goodMonth:String = randomGood?.month.toString()
                val goodDate :String = randomGood?.date.toString()
                val goodPerson:String = randomGood?.personName.toString()
                val goodQUote:String = randomGood?.quote.toString()
                val goodQuoteOrNot = randomGood?.quoteOrNot

                if(goodQuoteOrNot==false) {
                    messageTextView.text = "${goodYear}年${goodMonth}月${goodDate}日には\nこんなことがあったみたいですよ！\n${goodText}\n明日はきっとイイコトあります！"
                }else if(goodQuoteOrNot==true){
                    messageTextView.text = "${goodYear}年${goodMonth}月${goodDate}日には\n${goodPerson}に\n「\n${goodQUote}」\nと言われましたね！\n明日はきっとイイコトあります！"
                }

            }

        }else{
            if(quoteOrNot) {
                val quote:String?=intent.getStringExtra("quote")
                messageTextView.text="「$quote」\n嬉しいですね！！！"
            }else{
                messageTextView.text = "よかったですね！！\nわたしまで嬉しくなってきました！"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    fun readGood():RealmResults<Memo>{
        return realm.where(Memo::class.java).equalTo("good",true).findAll()
    }
}

