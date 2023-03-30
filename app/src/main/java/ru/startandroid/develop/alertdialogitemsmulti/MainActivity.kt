package ru.startandroid.develop.alertdialogitemsmulti

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.View
import android.widget.ListView
import androidx.core.util.size

const val LOG_TAG = "myLogs"

const val DIALOG_ITEMS = 1
const val DIALOG_CURSOR = 3

class MainActivity : AppCompatActivity() {

    var db:DB? = null
    var cursor: Cursor? = null
    var data = arrayOf("one", "two", "three", "four")
    var chkd = booleanArrayOf(false, true, true, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DB(this)
        db?.open()
        cursor = db!!.getAllData()
        startManagingCursor(cursor)
    }

    fun onClick(v: View) {
        when(v.id) {
            R.id.btnItems -> showDialog(DIALOG_ITEMS)
            R.id.btnCursor -> showDialog(DIALOG_CURSOR)
            else -> {}
        }
    }

    override fun onCreateDialog(id: Int): Dialog {
        val adb = AlertDialog.Builder(this)
        when(id) {
            DIALOG_ITEMS -> {
                adb.setTitle(R.string.items)
                adb.setMultiChoiceItems(data, chkd,
                    myItemsMultiClickListenner)
            }
            DIALOG_CURSOR -> {
                adb.setTitle(R.string.items)
                adb.setMultiChoiceItems(cursor,
                    COLUMN_CHK,
                    COLUMN_TXT,
                    myCursorMultiClickListener)
            }
        }
        adb.setPositiveButton(R.string.ok, myBtnClickListener)
        return adb.create()
    }

    var myItemsMultiClickListenner: OnMultiChoiceClickListener = object : OnMultiChoiceClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean) {
            Log.d(LOG_TAG, "which = $which, isChecked = $isChecked")
        }
    }

    var myCursorMultiClickListener: OnMultiChoiceClickListener = object : OnMultiChoiceClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean) {
            var lv: ListView = (dialog as AlertDialog).listView
            Log.d(LOG_TAG, "which = $which, isChecked = $isChecked")
            db!!.changeRec(which, isChecked)
            cursor!!.requery()
        }
    }

    var myBtnClickListener = object : OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            val sbArray: SparseBooleanArray = (dialog as AlertDialog).listView.checkedItemPositions
            for (i in 0 until sbArray.size()) {
                val key = sbArray.keyAt(i)
                if (sbArray[key]) {
                    Log.d("qwe", "checked $key")
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        db!!.close()
    }
}