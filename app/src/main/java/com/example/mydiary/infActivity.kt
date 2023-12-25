package com.example.mydiary

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiary.databinding.ActivityInfBinding

class infActivity : AppCompatActivity(){
    lateinit var infBinding: ActivityInfBinding
    var DB:DBHelper?=null
    var waitTime = 0L

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로가기 시 실행할 코드
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        infBinding = ActivityInfBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(infBinding.root)
        this.onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
        DB = DBHelper(this)

        val value1 = intent.getStringExtra("1")

        val id = value1
        var password = DB!!.DBinf(value1)
        val trueName = DB!!.DBinf2(value1)
        infBinding.userId.text = id
        infBinding.userPassword.text = password
        infBinding.userTrueName.text = trueName
        infBinding.fixPassword.setOnClickListener{
            val regex = "^[a-zA-Z0-9]*\$".toRegex()
            val str = infBinding.inputChangePassword.text.toString()
            val builder = AlertDialog.Builder(this)
            if (str == password){

                builder.setTitle("오류")
                    .setMessage("비밀번호가 그대로입니다. 다시 입력하세요")
                    .setPositiveButton("확인", DialogInterface.OnClickListener{dialog, id ->
                    })
                builder.show()
                infBinding.inputChangePassword.text.clear()
            }
            else if (!regex.matches(str)){
                builder.setTitle("오류")
                    .setMessage("잘못된 형식입니다. 영어나 숫자가 포함된 조합으로만 해주세요.")
                    .setPositiveButton("확인", DialogInterface.OnClickListener{dialog, id ->
                    })
                builder.show()
                infBinding.inputChangePassword.text.clear()
            }
            else if (str == ""){
                builder.setTitle("오류")
                    .setMessage("아무 글자나 입력해주세요.")
                    .setPositiveButton("확인", DialogInterface.OnClickListener{dialog, id ->
                    })
                builder.show()
                infBinding.inputChangePassword.text.clear()
            }
            else {
                val password1 = str
                infBinding.userPassword.text = str
                DB!!.update(id, password1)

                val builder = AlertDialog.Builder(this)
                builder.setTitle("변경 완료")
                    .setMessage("비밀번호가 변경되었습니다.")
                    .setPositiveButton("확인", DialogInterface.OnClickListener{dialog, id ->
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.putExtra("1", value1)
                        startActivity(intent)
                    })
                builder.show()
            }
        }
        infBinding.back.setOnClickListener{
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("1", value1)
            startActivity(intent)
        }
        infBinding.withdraw.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("회원탈퇴")
                .setMessage("정말 탈퇴하시겠습니까?")
                .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id ->
                    DB!!.delete(value1)
                    DB!!.deleteContentAll(value1)
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                })
                .setNegativeButton("취소", DialogInterface.OnClickListener{ dialog, id -> })
            builder.show()
        }
    }
}