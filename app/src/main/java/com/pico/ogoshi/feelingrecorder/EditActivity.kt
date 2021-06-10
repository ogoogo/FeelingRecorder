package com.pico.ogoshi.feelingrecorder

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val editingId =intent.getStringExtra("idInQuestion")
        val shf=getSharedPreferences("id", Context.MODE_PRIVATE)
        shf.edit().putString("id",editingId).apply()
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
            barometerSliderEdit.setValue(editingData.barometer.toFloat())
        }else if (editingData?.quoteOrNot==false){
            PersonEditTextEdit.isVisible=false
            textView2Edit.isVisible=false
            textView3Edit.isVisible=false
            dateTextViewEdit.text="${year}年${month}月${day}日に"
            eventTextEdit.setText(editingData.event)
            barometerSliderEdit.setValue(editingData.barometer.toFloat())
            textField3Edit.setHint("出来事")

        }

        var diaryOrNot=false
        textField2Edit.isVisible=false

        diarySwitchEdit.setOnCheckedChangeListener { buttonView, isChecked
            // Responds to switch being checked/unchecked
            ->
            diaryOrNot=isChecked
            if (diaryOrNot){
                textField2Edit.isVisible=true
            }else{
                diaryOrNot=false
                textField2Edit.isVisible=false
            }
        }

        var newBarometer=1
        barometerSliderEdit.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            newBarometer=value.toInt()
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
                if(diaryOrNot==true && diaryEditTextEdit.length()!=0){
                    val newPerson:String = PersonEditTextEdit.text.toString()
                    val newQuote :String= eventTextEdit.text.toString()
                    val newEvent = "「${newQuote}」 by${newPerson}"
                    val newDiary = diaryEditTextEdit.text.toString()
                    edit(editingId!!,newPerson,newQuote,newBarometer,newEvent,year!!,month!!,day!!,diaryOrNot!!,newDiary)
                }else if(diaryOrNot==true && diaryEditTextEdit.length()==0){
                    Toast.makeText(applicationContext,"全て埋めてください！", Toast.LENGTH_SHORT).show()
                }else if(diaryOrNot==false){
                    val newPerson:String = PersonEditTextEdit.text.toString()
                    val newQuote :String= eventTextEdit.text.toString()
                    val newEvent = "「${newQuote}」 by${newPerson}"
                    edit(editingId!!,newPerson,newQuote,newBarometer,newEvent,year!!,month!!,day!!,diaryOrNot!!,"")
                }

            }else if (editingData?.quoteOrNot==false){
                if(diaryOrNot==true && diaryEditTextEdit.length()!=0){
                    val newEvent :String= eventTextEdit.text.toString()
                    val newDiary = diaryEditTextEdit.text.toString()
                    edit(editingId!!,"","",newBarometer,newEvent,year!!,month!!,day!!,diaryOrNot!!,newDiary)
                }else if (diaryOrNot==true && diaryEditTextEdit.length()==0){
                    Toast.makeText(applicationContext,"全て埋めてください！", Toast.LENGTH_SHORT).show()
                }else if (diaryOrNot==false){
                    val newEvent :String= eventTextEdit.text.toString()
                    edit(editingId!!,"","",newBarometer,newEvent,year!!,month!!,day!!,diaryOrNot!!,"")
                }

            }
            startActivity(editedIntent)
        }




    }
    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(application, DetailActivity::class.java)
        val shf=getSharedPreferences("id", Context.MODE_PRIVATE)
        val editingId=shf.getString("id","")
        intent.putExtra("idInQuestion",editingId)
        startActivity(intent)
        finish()
        return super.onSupportNavigateUp()
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
    fun edit(id:String,personName:String,quote:String,barometer:Int,event: String,year:Int,month:Int,day:Int,diaryOrNot:Boolean,diary:String){
        realm.executeTransaction {
            val changingData:Memo?=realm.where(Memo::class.java).equalTo("id",id).findFirst()
            changingData?.year=year
            changingData?.month=month
            changingData?.date=day
            changingData?.personName=personName
            changingData?.quote=quote
            changingData?.barometer=barometer
            changingData?.event=event
            changingData?.diaryOrNot=diaryOrNot
            changingData?.diary=diary
        }
    }


}