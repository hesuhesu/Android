package com.example.mydiary

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiary.databinding.ActivityFindBinding


class FindActivity : AppCompatActivity(){
    lateinit var FindBinding: ActivityFindBinding
    var DB:DBHelper?=null
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로가기 시 실행할 코드
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        FindBinding = ActivityFindBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(FindBinding.root)
        this.onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
        DB = DBHelper(this)

        FindBinding.back.setOnClickListener{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
        FindBinding.findPassword.setOnClickListener{
            val username = FindBinding.inputId.text.toString()
            val truename = FindBinding.inputTrueName.text.toString()
            val builder = AlertDialog.Builder(this)

            if (username == "" || truename == ""){
                builder.setTitle("오류")
                    .setMessage("정보를 모두 입력해주세요.")
                    .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id -> })
                builder.show()
            }
            else {
                if (DB!!.checkUsername(username) == false){
                    builder.setTitle("오류")
                        .setMessage("아이디가 존재하지 않습니다. 다시 확인해주세요.")
                        .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id -> })
                    builder.show()
                    FindBinding.inputId.text.clear()
                    FindBinding.inputTrueName.text.clear()
                }
                else {
                    if (DB!!.checkUserandtruename(username, truename) == false){
                        builder.setTitle("오류")
                            .setMessage("성함이 일치하지 않습니다. 다시 입력해주세요.")
                            .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id -> })
                        builder.show()
                        FindBinding.inputTrueName.text.clear()
                    }
                    else {
                        builder.setTitle("비밀번호 확인")
                            .setMessage("고객님의 비밀번호는 ${DB!!.DBinf(username)}입니다. 잘 기억해주세요^^")
                            .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id ->
                                val intent = Intent(applicationContext, LoginActivity::class.java)
                                startActivity(intent)
                            })
                        builder.show()
                    }
                }
            }
        }
    }
}