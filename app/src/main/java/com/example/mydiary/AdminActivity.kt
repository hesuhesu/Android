package com.example.mydiary

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiary.databinding.ActivityAdminBinding
import com.example.mydiary.databinding.ActivityRegisterBinding

class AdminActivity : AppCompatActivity() {
    lateinit var adminBinding: ActivityAdminBinding
    var DB:DBHelper?=null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로가기 시 실행할 코드
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        adminBinding = ActivityAdminBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(adminBinding.root)
        this.onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
        DB = DBHelper(this)

        adminBinding.userList.text = DB!!.findAll()

        adminBinding.delete.setOnClickListener{
            val user = adminBinding.inputUserName.text.toString()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("회원삭제")
                .setMessage("정말 하시겠습니까?")
                .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id ->
                    val builder2 = AlertDialog.Builder(this)
                    builder2.setTitle("삭제 메세지")
                        .setMessage("삭제 완료")
                        .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id ->
                            adminBinding.userList.text = DB!!.findAll()
                        })
                    builder2.show()
                    DB!!.delete(user)
                    DB!!.deleteContentAll(user)
                    adminBinding.inputUserName.text.clear()
                })
                .setNegativeButton("취소", DialogInterface.OnClickListener{ dialog, id ->
                    adminBinding.inputUserName.text.clear()
                })
            builder.show()
        }
        adminBinding.logout.setOnClickListener{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}