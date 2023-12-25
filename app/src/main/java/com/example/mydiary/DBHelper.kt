package com.example.mydiary

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?):
    SQLiteOpenHelper(context, "Login.db", null, 1) {
    override fun onCreate(MyDB: SQLiteDatabase) {
        MyDB.execSQL("create Table users(username TEXT primary key Not null, password TEXT Not null, truename TEXT Not null)")
        MyDB.execSQL("create Table diary(username TEXT Not null, date TEXT Not null, content TEXT)")
    }
    override fun onUpgrade(MyDB: SQLiteDatabase, i: Int, i1: Int) {
        MyDB.execSQL("drop Table if exists users")
        MyDB.execSQL("drop Table if exists diary")
        onCreate(MyDB)
    }
    fun update(username1: String?, password1: String?){
        val MyDB = this.writableDatabase
        var query = "UPDATE users SET password = ? WHERE username = ?;"
        MyDB.execSQL(query, arrayOf(password1, username1))
    }
    fun updateContent(username1: String?, date1: String?, content1: String?){
        val MyDB = this.writableDatabase
        var query = "UPDATE diary SET content = ? WHERE username = ? and date = ?;"
        MyDB.execSQL(query, arrayOf(content1, username1, date1))
    }
    fun delete(usernames:String?){
        val MyDB = this.writableDatabase
        var query = "DELETE FROM users WHERE username = ?;"
        MyDB.execSQL(query, arrayOf(usernames))
    }
    fun deleteContentAll(usernames:String?){
        val MyDB = this.writableDatabase
        var query = "DELETE FROM diary WHERE username = ?;"
        MyDB.execSQL(query, arrayOf(usernames))
    }
    fun findAll():String{
        val MyDB = this.writableDatabase
        var query = " SELECT * FROM users;"
        var result = MyDB.rawQuery(query, null)
        var str:String = ""
        var num:Int = 1
        while (result.moveToNext()){
            val columnIndex1 = result.getColumnIndex("username")
            val columnIndex2 = result.getColumnIndex("password")
            val columnIndex3 = result.getColumnIndex("truename")
            if (columnIndex1 != -1) {
                str += "${num}번째 회원 -> id : " + result.getString(columnIndex1)  + ", pw : " + result.getString(columnIndex2) + ", 회원이름 : "+ result.getString(columnIndex3) + "\n\n"
                num++
            } else {
                // "password" 열이 존재하지 않는 경우 빈 문자열 반환 또는 예외 처리
            }
        }

        return str
    }

    fun insertData(username: String?, password: String?, truename: String?): Boolean{
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("password", password)
        contentValues.put("truename", truename)
        val result = MyDB.insert("users", null, contentValues)
        return if (result == -1L) false else true
    }
    fun insertDataContent(username: String?, date: String?, content: String?): Boolean{
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("date", date)
        contentValues.put("content", content)
        val result = MyDB.insert("diary", null, contentValues)
        return if (result == -1L) false else true
    }
    fun checkUsername(username: String): Boolean{
        val MyDB = this.writableDatabase
        var res = true
        val cursor = MyDB.rawQuery("Select * from users where username =?", arrayOf(username))
        if (cursor.count <= 0) res = false
        cursor.close()
        return res
    }
    fun checkContent(username: String?, date: String?): Boolean{
        val MyDB = this.writableDatabase
        var res = true
        val cursor = MyDB.rawQuery("Select * from diary where username =? and date = ?", arrayOf(username, date))
        if (cursor.count <= 0) res = false
        cursor.close()
        return res
    }
    fun checkUserpass(username: String, password: String): Boolean {
        val MyDB = this.writableDatabase
        var res = true
        val cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?",
            arrayOf(username, password)
        )
        if (cursor.count <= 0) res = false
        cursor.close()
        return res
    }
    fun checkUserandtruename(username:String?, truename:String?): Boolean{
        val MyDB = this.writableDatabase
        var res = true
        val cursor = MyDB.rawQuery("Select * from users where username = ? and truename = ?",
            arrayOf(username, truename)
        )
        if (cursor.count <= 0) res = false
        cursor.close()
        return res
    }

    fun DBinf(usernames: String?):String{
        val MyDB = this.writableDatabase
        var query = "SELECT password FROM users WHERE username = ?;"
        var cursor = MyDB.rawQuery(query, arrayOf(usernames))

        return if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex("password")
            val password = if (columnIndex != -1) {
                cursor.getString(columnIndex)
            } else {
                // "password" 열이 존재하지 않는 경우 빈 문자열 반환 또는 예외 처리
                ""
            }
            cursor.close()
            password
        } else {
            cursor.close()
            ""
        }
    }
    fun DBinf2(usernames: String?):String{
        val MyDB = this.writableDatabase
        var query = "SELECT truename FROM users WHERE username = ?;"
        var cursor = MyDB.rawQuery(query, arrayOf(usernames))

        return if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex("truename")
            val password = if (columnIndex != -1) {
                cursor.getString(columnIndex)
            } else {
                // "password" 열이 존재하지 않는 경우 빈 문자열 반환 또는 예외 처리
                ""
            }
            cursor.close()
            password
        } else {
            cursor.close()
            ""
        }
    }

    fun DBcontentinf(usernames: String?, dates: String?):String {
        val MyDB = this.writableDatabase
        var query = "SELECT content FROM diary WHERE username = ? and date = ?;"
        var cursor = MyDB.rawQuery(query, arrayOf(usernames, dates))

        if (cursor.count == 0) {
            return ""
        } else {
            return if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex("content")
                val contentVal = if (columnIndex != -1) {
                    cursor.getString(columnIndex)
                } else {
                    // "content" 열이 존재하지 않는 경우 빈 문자열 반환 또는 예외 처리
                    ""
                }
                cursor.close()
                contentVal
            } else {
                cursor.close()
                ""
            }
        }
    }
    companion object {
        const val DBNAME = "Login.db"
    }
}