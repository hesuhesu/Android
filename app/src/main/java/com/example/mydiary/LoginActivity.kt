package com.example.mydiary

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.mydiary.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding : ActivityLoginBinding
    var DB:DBHelper?=null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로가기 시 실행할 코드
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(loginBinding.root)
        this.onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
        DB = DBHelper(this)

        Glide.with(this).load(R.raw.giphy).into(loginBinding.imageView);

        loginBinding.login!!.setOnClickListener{
            val user = loginBinding.inputId!!.text.toString()
            val pass = loginBinding.inputPassword!!.text.toString()
            val builder = AlertDialog.Builder(this)
            if (user == "" || pass==""){
                builder.setTitle("오류")
                    .setMessage("회원정보를 전부 입력해주세요.")
                    .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id -> })
                builder.show()
            }
            else if (user == "admin" && pass == "admin"){
                builder.setTitle("관리자 로그인")
                    .setMessage("관리자님 환영합니다!")
                    .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id ->
                        val intent = Intent(applicationContext, AdminActivity::class.java)
                        startActivity(intent)
                    })
                builder.show()
            }
            else {
                val checkUserpass = DB!!.checkUserpass(user, pass)
                if (checkUserpass){
                    builder.setTitle("회원 로그인")
                        .setMessage("${user}님 환영합니다!")
                        .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id ->
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.putExtra("1", user)
                            startActivity(intent)
                        })
                    builder.show()
                }
                else {
                    builder.setTitle("오류")
                        .setMessage("회원 정보가 존재하지 않습니다.")
                        .setPositiveButton("확인", DialogInterface.OnClickListener{dialog, id -> })
                    builder.show()
                    loginBinding.inputId.text.clear()
                    loginBinding.inputPassword.text.clear()
                }
            }
        }
        loginBinding.register!!.setOnClickListener {
            val RegisterIntent = Intent(this@LoginActivity, RegisterActiviy::class.java)
            startActivity(RegisterIntent)
        }
        loginBinding.findPassword.setOnClickListener{
            val FindIntent = Intent(this@LoginActivity, FindActivity::class.java)
            startActivity(FindIntent)
        }
        loginBinding.exit.setOnClickListener{
            ActivityCompat.finishAffinity(this)
            System.exit(0)
        }
    }
}