package com.example.mydiary

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Dimension
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiary.databinding.ActivityContentBinding

class ContentActivity : AppCompatActivity(){
    lateinit var contentBinding: ActivityContentBinding
    var DB:DBHelper?=null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로가기 시 실행할 코드
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        contentBinding = ActivityContentBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(contentBinding.root)
        this.onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
        DB = DBHelper(this)

        val value1 = intent.getStringExtra("1")
        val value2 = intent.getStringExtra("2")
        val value3 = intent.getStringExtra("3")

        contentBinding.yearMonthDay.text = value2
        contentBinding.data.setText(DB!!.DBcontentinf(value1, value3))

        contentBinding.back.setOnClickListener{
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("1", value1)
            startActivity(intent)
        }

        contentBinding.save.setOnClickListener{
            val content = contentBinding.data.text.toString()
            if (DB!!.checkContent(value1, value3) == false){
                if (content.length > 0){
                    DB!!.insertDataContent(value1, value3, content)
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("저장")
                        .setMessage("저장 완료되었습니다!")
                        .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id ->
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.putExtra("1", value1)
                            startActivity(intent)
                        })
                    builder.show()
                }
            }
            else {
                DB!!.updateContent(value1, value3, content)
                val builder = AlertDialog.Builder(this)
                builder.setTitle("저장")
                    .setMessage("저장 완료되었습니다!")
                    .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id ->
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.putExtra("1", value1)
                        startActivity(intent)
                    })
                builder.show()
            }
        }
    }
}