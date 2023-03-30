package ru.startandroid.develop.alertdialogitemsmulti

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

const val DB_NAME = "mydb"
const val DB_VERSION = 1
const val DB_TABLE = "mytab"

const val COLUMN_ID = "_id"
const val COLUMN_CHK = "checked"
const val COLUMN_TXT = "txt"

const val DB_CREATE = "create table " + DB_TABLE + "(" +
        COLUMN_ID + " integer primary key, " +
        COLUMN_CHK + " integer, " +
        COLUMN_TXT + " text" +
        ");"

class DB(ctx: Context) {

    var mCtx: Context? = null
    var mDBHelper: DBHelper? = null
    var mDB: SQLiteDatabase? = null

    init {
        mCtx = ctx
    }

    fun open() {
        mDBHelper = DBHelper(mCtx!!, DB_NAME, null, DB_VERSION)
        mDB = mDBHelper!!.writableDatabase
    }

    fun close() {
        if (mDBHelper != null) mDBHelper!!.close()
    }

    fun getAllData(): Cursor {
        return mDB!!.query(DB_TABLE, null, null, null, null, null, null)
    }

    fun changeRec(pos: Int, isChecked: Boolean) {
        val cv: ContentValues = ContentValues()
        cv.put(COLUMN_CHK, isChecked)
        mDB!!.update(DB_TABLE, cv, "$COLUMN_ID = ${pos + 1}", null)
    }

    class DBHelper(
        context: Context?,
        name: String?,
        factory: CursorFactory?,
        version: Int
    ):
        SQLiteOpenHelper(context, name, factory, version) {
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(DB_CREATE)

            val cv = ContentValues()
            for (i in 1.. 4) {
                cv.put(COLUMN_ID, i)
                cv.put(COLUMN_TXT, "sometext $i")
                cv.put(COLUMN_CHK, 0)
                db.insert(DB_TABLE, null, cv)
            }
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }
    }
}