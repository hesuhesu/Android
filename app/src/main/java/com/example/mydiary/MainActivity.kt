package com.example.mydiary

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.example.mydiary.databinding.ActivityMainBinding
import java.lang.Math.abs
import java.util.Calendar
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로가기 시 실행할 코드
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        var DB:DBHelper?=null
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.onBackPressedDispatcher.addCallback(this,onBackPressedCallback)

        val value1 = intent.getStringExtra("1")


        DB = DBHelper(this)

        binding.userInformation.text = value1 + "님 환영합니다."

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            val today = LocalDate.now()
            // 선택한 날짜
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth) // month는 0부터 시작하므로 1을 더해줍니다.

            // 날짜 차이 계산
            val dateDifference = ChronoUnit.DAYS.between(today, selectedDate)
            if (dateDifference > 0) {
                // 선택한 날짜가 오늘 이후인 경우에만 실행할 코드
                binding.preview.text = "해당 날짜는 미래입니다."
            } else {
                // 선택한 날짜가 오늘 이전이나 같으면 실행
                binding.preview.text = DB!!.DBcontentinf(value1, "${year}${month}${dayOfMonth}")
            }
        }

        binding.showRegister.setOnClickListener {
            val intent2 = Intent(applicationContext, infActivity::class.java)
            intent2.putExtra("1", value1)
            startActivity(intent2)
        }
        binding.writeDiary.setOnClickListener{
            showDatePicker()
        }
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        binding.logout.setOnClickListener{
            startActivity(intent)
        }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        val today = LocalDate.now()

        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, y, m, d->
            val selectedDates = LocalDate.of(y, m + 1, d) // month는 0부터 시작하므로 1을 더해줍니다.
            val dateDifference = ChronoUnit.DAYS.between(today, selectedDates)

            if (dateDifference > 0) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("오류")
                    .setMessage("미래에 대해 적는건 불가능합니다.")
                    .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id -> })
                builder.show()
            }
            else {
                // 선택한 날짜가 오늘 이전이나 같으면 실행
                val value1 = intent.getStringExtra("1")
                val selectedDate = "${y}년 ${m + 1}월 ${d}일"
                val dataDate = "${y}${m}${d}"
                val intent2 = Intent(applicationContext, ContentActivity::class.java)
                intent2.putExtra("1", value1)
                intent2.putExtra("2", selectedDate)
                intent2.putExtra("3", dataDate)
                startActivity(intent2)
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
    }
    fun LocalDateTime.differentDays(from: LocalDate): Int {
        return Period.between(this.toLocalDate(), from).days
    }
}