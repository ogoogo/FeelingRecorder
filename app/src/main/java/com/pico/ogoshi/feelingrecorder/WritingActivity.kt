package com.pico.ogoshi.feelingrecorder

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.slider.Slider
import com.google.android.material.tabs.TabLayout
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_writing.*
import java.util.*

class WritingActivity : AppCompatActivity() {

    val realm: Realm = Realm.getDefaultInstance()
    var quoteOrNot: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                // Handle tab select
                val tagText = tab.text
                if (tagText == "出来事") {
                    quoteOrNot = false
                    PersonEditText.isVisible = false
                    textField.isVisible = false
                    textView2.isVisible = false
                    textField3.setHint("出来事")
                    textView3.isVisible = false
                }
                if (tagText == "言われたこと") {
                    quoteOrNot = true
                    PersonEditText.isVisible = true
                    textField.isVisible = true
                    textView2.isVisible = true
                    textField3.setHint("言われた言葉")
                    textView3.isVisible = true

                }

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })


        val good2: Boolean = intent.getBooleanExtra("good", true)
        if (good2 == false) {
            barometerTextView.text = "ヤナコト度"
            saveButton.text = "ことりに食べさせる"
        }

        var diaryOrNot = false
        diaryEditText.isVisible = false
        textField2.isVisible = false

        diarySwitch.setOnCheckedChangeListener { buttonView, isChecked
            // Responds to switch being checked/unchecked
            ->
            diaryOrNot = isChecked
            if (diaryOrNot) {
                textField2.isVisible = true
                diaryEditText.isVisible = true
                if (diaryEditText.length() == 0) {
                    saveButton.isEnabled = false
                    saveButton.setBackgroundColor(Color.parseColor("#c79a00"))
                }
            } else {
                diaryOrNot = false
                textField2.isVisible = false
                diaryEditText.isVisible = false
                if(diaryEditText.length()==0){
                    saveButton.isEnabled=true
                    saveButton.setBackgroundColor(Color.parseColor("#ffca28"))
                }
            }
        }


        val savedIntent: Intent = Intent(this, SavedActivity::class.java)

        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH) + 1
        var day = c.get(Calendar.DAY_OF_MONTH)
        dateTextView.text = "${year}年${month}月${day}日に"


        todayButton.setOnClickListener {
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH) + 1
            day = c.get(Calendar.DAY_OF_MONTH)
            dateTextView.text = "${year}年${month}月${day}日に"
        }

        yesterdayButton.setOnClickListener {
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH) + 1
            day = c.get(Calendar.DAY_OF_MONTH) - 1
            dateTextView.text = "${year}年${month}月${day}日に"
        }

        otherDayButton.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, y, m, d ->
                    year = y
                    month = m + 1
                    day = d
                    dateTextView.text = "${year}年${month}月${day}日に"
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        saveButton.isEnabled = false
        saveButton.setBackgroundColor(Color.parseColor("#c79a00"))

        PersonEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                checker(diaryOrNot, quoteOrNot)

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //処理
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //処理
            }
        })
        eventText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                checker(diaryOrNot, quoteOrNot)

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //処理
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //処理
            }
        })
        diaryEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                checker(diaryOrNot, quoteOrNot)

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //処理
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //処理
            }
        })





        saveButton.setOnClickListener {
            val barometer = starBar.getRating().toInt()

            if (quoteOrNot) {
                if (diaryOrNot) {

                    val personName = PersonEditText.text.toString()
                    val quote = eventText.text.toString()
                    val event = "「${quote}」by${personName}"
                    val diary = diaryEditText.text.toString()
                    save(
                        event,
                        day,
                        good2,
                        barometer,
                        personName,
                        quoteOrNot,
                        quote,
                        year,
                        month,
                        diaryOrNot,
                        diary
                    )
                    savedIntent.putExtra("quote", quote)
                    savedIntent.putExtra("good2", good2)
                    savedIntent.putExtra("quoteOrNot", quoteOrNot)
                    startActivity(savedIntent)
                    finish()


                } else {

                    val personName = PersonEditText.text.toString()
                    val quote = eventText.text.toString()
                    val event = "「${quote}」by${personName}"
                    save(
                        event!!,
                        day,
                        good2,
                        barometer!!,
                        personName!!,
                        quoteOrNot,
                        quote!!,
                        year,
                        month,
                        diaryOrNot,
                        ""
                    )
                    savedIntent.putExtra("quote", quote)
                    savedIntent.putExtra("good2", good2)
                    savedIntent.putExtra("quoteOrNot", quoteOrNot)
                    startActivity(savedIntent)
                    finish()

                }

            } else {
                if (diaryOrNot) {

                    val event = eventText.text.toString()
                    val diary = diaryEditText.text.toString()
                    save(event, day, good2, barometer, "", quoteOrNot, "", year, month, diaryOrNot, diary)
                    savedIntent.putExtra("good2", good2)
                    savedIntent.putExtra("quoteOrNot", quoteOrNot)
                    startActivity(savedIntent)
                    finish()

                } else {

                    val event = eventText.text.toString()
                    save(event, day, good2, barometer, "", quoteOrNot, "", year, month, diaryOrNot, "")
                    savedIntent.putExtra("good2", good2)
                    savedIntent.putExtra("quoteOrNot", quoteOrNot)
                    startActivity(savedIntent)
                    finish()

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

    fun save(
        event: String,
        date: Int,
        good: Boolean,
        barometer: Int,
        personName: String,
        quoteOrNot: Boolean,
        quote: String,
        year: Int,
        month: Int,
        diaryOrNot: Boolean,
        diary: String
    ) {
        realm.executeTransaction {
            val newMemo: Memo = it.createObject(Memo::class.java, UUID.randomUUID().toString())
            newMemo.event = event
            newMemo.date = date
            newMemo.good = good
            newMemo.barometer = barometer
            newMemo.personName = personName
            newMemo.quoteOrNot = quoteOrNot
            newMemo.quote = quote
            newMemo.year = year
            newMemo.month = month
            newMemo.diaryOrNot = diaryOrNot
            newMemo.diary = diary

        }
    }

    fun checker(diary: Boolean, quote: Boolean) {
        if (diary && quote) {
            if (PersonEditText.length() != 0 && eventText.length() != 0 && diaryEditText.length() != 0) {
                saveButton.isEnabled = true
                saveButton.setBackgroundColor(Color.parseColor("#ffca28"))
            } else {
                saveButton.isEnabled = false
                saveButton.setBackgroundColor(Color.parseColor("#c79a00"))
            }
        }
        if (diary && quote == false) {
            if (eventText.length() != 0 && diaryEditText.length() != 0) {
                saveButton.isEnabled = true
                saveButton.setBackgroundColor(Color.parseColor("#ffca28"))
            } else {
                saveButton.isEnabled = false
                saveButton.setBackgroundColor(Color.parseColor("#c79a00"))
            }
        }
        if (diary == false && quote) {
            if (PersonEditText.length() != 0 && eventText.length() != 0) {
                saveButton.isEnabled = true
                saveButton.setBackgroundColor(Color.parseColor("#ffca28"))
            } else {
                saveButton.isEnabled = false
                saveButton.setBackgroundColor(Color.parseColor("#c79a00"))
            }
        }
        if (diary == false && quote == false) {
            if (eventText.length() != 0) {
                saveButton.isEnabled = true
                saveButton.setBackgroundColor(Color.parseColor("#ffca28"))
            } else {
                saveButton.isEnabled = false
                saveButton.setBackgroundColor(Color.parseColor("#c79a00"))
            }
        }
    }

}