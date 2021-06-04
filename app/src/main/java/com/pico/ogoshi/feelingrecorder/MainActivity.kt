package com.pico.ogoshi.feelingrecorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val realm:Realm by lazy {  Realm.getDefaultInstance()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val writingIntent: Intent = Intent(this, ChoosingActivity::class.java)
        val detailIntent=Intent(this,DetailActivity::class.java)
        val eventList=read()


/*
        realm.executeTransaction{
            eventList.deleteAllFromRealm()
        }

*/


/*
        if (eventList.isEmpty()){
            for (i in 0..10){
                create(26,"岡田准一${i}にあった！")
            }
        }
*/
        val adapter=Adapter(this,eventList,true)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.adapter=adapter
        adapter.setOnItemClickListener(object:Adapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int,event:Memo) {

                val id = event.id
                detailIntent.putExtra("idInQuestion",id)
                startActivity(detailIntent)

            }
        })



        writeButton.setOnClickListener{
            startActivity(writingIntent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun read(): RealmResults<Memo>{
        return realm.where(Memo::class.java).equalTo("good",true)
                                            .findAll()
                                            .sort("createdAt",Sort.DESCENDING)
                                            .sort("date",Sort.DESCENDING)
    }

    fun create(date:Int, event:String){
        realm.executeTransaction{
            val newevent = it.createObject(Memo::class.java, UUID.randomUUID().toString())
            newevent.date=date
            newevent.event=event
        }
    }


}