package com.pico.ogoshi.feelingrecorder

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.slider.Slider
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_writing.*
import java.util.*

class WritingActivity : AppCompatActivity() {

    val realm:Realm = Realm.getDefaultInstance()
    var quoteOrNot:Boolean=true
    var barometer:Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        val good2 :Boolean = intent.getBooleanExtra("good",true)
        if (good2==false){
            barometerTextView.text="ヤナコト度"
        }

        var diaryOrNot=false
        diaryEditText.isVisible=false

        diarySwitch.setOnCheckedChangeListener { buttonView, isChecked
            // Responds to switch being checked/unchecked
            ->
            diaryOrNot=isChecked
            if (diaryOrNot){
                diaryEditText.isVisible=true
            }else{
                diaryOrNot=false
                diaryEditText.isVisible=false
            }
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
        dateTextView.text="${year}年${month}月${day}日に"


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

        barometerSlider.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            barometer=value.toInt()
        }



        saveButton.setOnClickListener {

            if(quoteOrNot){
                if (diaryOrNot){
                    if(PersonEditText.length()!=0 && eventText.length()!=0 &&  diaryEditText.length()!=0){
                        val personName=PersonEditText.text.toString()
                        val quote=eventText.text.toString()
                        val event="「${quote}」by${personName}"
                        val diary=diaryEditText.text.toString()
                        save(event,day,good2,barometer,personName,quoteOrNot,quote,year,month,diaryOrNot,diary)
                        savedIntent.putExtra("quote",quote)
                        savedIntent.putExtra("good2",good2)
                        savedIntent.putExtra("quoteOrNot",quoteOrNot)
                        startActivity(savedIntent)
                    }else{
                        Toast.makeText(applicationContext,"全て埋めてください！",Toast.LENGTH_SHORT).show()
                    }

                } else{
                    if(PersonEditText.length()!=0 && eventText.length()!=0 ){
                        val personName=PersonEditText.text.toString()
                        val quote=eventText.text.toString()
                        val event="「${quote}」by${personName}"
                        save(event!!,day,good2,barometer!!,personName!!,quoteOrNot,quote!!,year,month,diaryOrNot,"")
                        savedIntent.putExtra("quote",quote)
                        savedIntent.putExtra("good2",good2)
                        savedIntent.putExtra("quoteOrNot",quoteOrNot)
                        startActivity(savedIntent)
                    }else{
                        Toast.makeText(applicationContext,"全て埋めてください！",Toast.LENGTH_SHORT).show()
                    }
                }

            }else {
                if (diaryOrNot){
                    if (eventText.length()!=0  && diaryEditText.length()!=0){
                        val event = eventText.text.toString()
                        val diary = diaryEditText.text.toString()
                        save(event, day, good2, barometer,"",quoteOrNot,"",year,month,diaryOrNot,diary)
                        savedIntent.putExtra("good2",good2)
                        savedIntent.putExtra("quoteOrNot",quoteOrNot)
                        startActivity(savedIntent)
                    } else{
                        Toast.makeText(applicationContext,"全て埋めてください！",Toast.LENGTH_LONG).show()
                    }
                }else{
                    if (eventText.length()!=0 ){
                        val event = eventText.text.toString()
                        save(event, day, good2, barometer,"",quoteOrNot,"",year,month,diaryOrNot,"")
                        savedIntent.putExtra("good2",good2)
                        savedIntent.putExtra("quoteOrNot",quoteOrNot)
                        startActivity(savedIntent)
                    } else{
                        Toast.makeText(applicationContext,"全て埋めてください！",Toast.LENGTH_LONG).show()
                    }
                }


            }

        }



    }
    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(application, ChoosingActivity::class.java)
        startActivity(intent)
        finish()
        return super.onSupportNavigateUp()
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // InputMethodManager をキャストしながら取得
        val inputMethodManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // エルビス演算子でViewを取得できなければ return false
        // focusViewには入力しようとしているのEditTextが取得されるはず
        val focusView = currentFocus ?: return false

        // このメソッドでキーボードを閉じる
        inputMethodManager.hideSoftInputFromWindow(
            focusView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

        return false
    }
    /*
    override fun onDialogTextRecieve(dialog: DialogFragment, diary: String) {

        Log.d("dialog",diary)
    }*/

    fun save(event:String,
             date:Int,
             good:Boolean,
             barometer:Int,
             personName:String,
             quoteOrNot:Boolean,
             quote:String,
             year:Int,
             month:Int,
             diaryOrNot:Boolean,
             diary:String){
        realm.executeTransaction{
           val newMemo:Memo=it.createObject(Memo::class.java,UUID.randomUUID().toString())
            newMemo.event=event
            newMemo.date=date
            newMemo.good=good
            newMemo.barometer=barometer
            newMemo.personName=personName
            newMemo.quoteOrNot=quoteOrNot
            newMemo.quote=quote
            newMemo.year=year
            newMemo.month=month
            newMemo.diaryOrNot=diaryOrNot
            newMemo.diary=diary

        }
    }
}