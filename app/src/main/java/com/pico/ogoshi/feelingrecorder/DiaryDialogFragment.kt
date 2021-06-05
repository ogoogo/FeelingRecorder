package com.pico.ogoshi.feelingrecorder

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.diary_dialog.*

class DiaryDialogFragment:DialogFragment() {
    interface DialogListener{
        fun onDialogTextRecieve(dialog: DialogFragment,diary: String)
    }
    var listener:DialogListener? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val diaryView = inflater.inflate(R.layout.diary_dialog, null)

        builder.setView(diaryView)
            .setTitle("日記を追加する")
            .setPositiveButton("OK") { dialog, id ->
                val diary = diaryView.findViewById<EditText>(R.id.diary)?.text
                if (!diary.isNullOrEmpty()){//textが空でなければ
                    listener?.onDialogTextRecieve(this,diary.toString())
                }

            }
            .setNegativeButton("Cancel") { dialog, id ->

            }

        return builder.create()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DialogListener
        }catch (e: Exception){
            Log.e("ERROR","CANNOT FIND LISTENER")
        }
    }

}