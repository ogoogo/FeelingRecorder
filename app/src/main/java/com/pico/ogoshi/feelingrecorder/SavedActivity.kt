package com.pico.ogoshi.feelingrecorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        val writingIntent:Intent=Intent(this,ChoosingActivity::class.java)

        backButton.setOnClickListener {
            startActivity(homeIntent)
        }
        writingButton.setOnClickListener {
            startActivity(writingIntent)
        }

        if(goodOrBad==false) {

            val goodfeelings: RealmResults<Memo> = readGood()
            val randomGood = goodfeelings[Random.nextInt(goodfeelings.size)]
            val goodText :String = randomGood?.event.toString()
            val goodDate :String = randomGood?.date.toString()
            messageTextView.text="${goodDate}日は、こんなことがあったみたいですよ！\n${goodText}"
        }else{
            if(quoteOrNot) {
                val quote:String?=intent.getStringExtra("quote")
                messageTextView.text="「$quote」\n嬉しいですね！！！"
            }else{
                messageTextView.text = "よかったですね！！わたしまで嬉しくなってきました！"
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

