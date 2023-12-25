package com.example.mydiary

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiary.databinding.ActivityRegisterBinding

class RegisterActiviy : AppCompatActivity() {
    lateinit var registerBinding: ActivityRegisterBinding
    var DB:DBHelper?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(registerBinding.root)
        DB = DBHelper(this)

        fun inputAllReset(){
            registerBinding.inputId.text.clear()
            registerBinding.inputPassword.text.clear()
            registerBinding.inputRePassword.text.clear()
            registerBinding.inputTrueName.text.clear()
        }

        registerBinding.registComplete!!.setOnClickListener{
            val user = registerBinding.inputId.text.toString()
            val pass = registerBinding.inputPassword.text.toString()
            val repass = registerBinding.inputRePassword.text.toString()
            var truename = registerBinding.inputTrueName.text.toString()
            truename = truename.replace("\\s".toRegex(), "")
            val builder = AlertDialog.Builder(this)
            val regex = "^[a-zA-Z0-9]*\$".toRegex()

            if (user == "" || pass == "" || repass == "") {
                builder.setTitle("오류")
                    .setMessage("회원 정보를 모두 입력해주세요.")
                    .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id -> })
                builder.show()
            }
            else if (!regex.matches(user)){
                builder.setTitle("오류")
                    .setMessage("아이디 형식이 잘못되었습니다.")
                    .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id -> })
                builder.show()
                inputAllReset()
            }
            else {
                if (pass == repass){
                    val checkUsername = DB!!.checkUsername(user)

                    if (user == "admin"){
                        builder.setTitle("오류")
                            .setMessage("가입된 회원입니다.")
                            .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id -> })
                        builder.show()
                        inputAllReset()
                    }
                    else {
                        if (checkUsername == false){
                            if (regex.matches(pass)){
                                val insert = DB!!.insertData(user,pass, truename)
                                if (insert == true){
                                    builder.setTitle("가입완료")
                                        .setMessage("가입 환영합니다! 회원님 성함은 공백이 있을 경우 제외시키고 적용합니다.")
                                        .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id ->
                                            val intent = Intent(applicationContext, LoginActivity::class.java)
                                            startActivity(intent)
                                        })
                                    builder.show()
                                }
                            }
                            else {
                                builder.setTitle("오류")
                                    .setMessage("비밀번호 형식을 확인해주세요. 숫자 or 영어만 사용 가능합니다.")
                                    .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id -> })
                                builder.show()
                                registerBinding.inputPassword.text.clear()
                                registerBinding.inputRePassword.text.clear()
                            }
                        }
                        else {
                            builder.setTitle("오류")
                                .setMessage("가입된 회원입니다.")
                                .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id -> })
                            builder.show()
                            inputAllReset()
                        }
                    }
                }
                else {
                    builder.setTitle("오류")
                        .setMessage("비밀번호가 일치하지 않습니다 다시 입력해주세요.")
                        .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id -> })
                    builder.show()
                    registerBinding.inputRePassword.text.clear()
                }
            }
        }
        registerBinding.registCancel.setOnClickListener{
            inputAllReset()
        }
        registerBinding.back.setOnClickListener{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}