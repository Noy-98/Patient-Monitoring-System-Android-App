package com.jay.patientmonitoringapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Records : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.record
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            if (item.itemId == R.id.home) {
                startActivity(Intent(applicationContext, Home_Dashboard::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
                return@setOnItemSelectedListener true
            } else if (item.itemId == R.id.record) {
                return@setOnItemSelectedListener true
            } else if (item.itemId == R.id.profile) {
                startActivity(Intent(applicationContext, Profile::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
                return@setOnItemSelectedListener true
            } else if (item.itemId == R.id.logout) {
                startActivity(Intent(applicationContext, Login::class.java))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
                return@setOnItemSelectedListener true
            }
            false
        }

        val heartRateValue = findViewById<ImageView>(R.id.heart_rate)
        val humanTempValue = findViewById<ImageView>(R.id.human_temp)
        val humValue = findViewById<ImageView>(R.id.hum)
        val v1 = findViewById<TextView>(R.id.text_bttn)
        val v2 = findViewById<TextView>(R.id.text_bttn2)
        val v3 = findViewById<TextView>(R.id.text_bttn3)
        val n1 = findViewById<ImageView>(R.id.next_bttn)
        val n2 = findViewById<ImageView>(R.id.next_bttn2)
        val n3 = findViewById<ImageView>(R.id.next_bttn3)

        v1.setOnClickListener {
            val intent = Intent (this, BPM_Records::class.java)
            startActivity(intent)
        }

        v2.setOnClickListener {
            val intent = Intent (this, Temp_Records::class.java)
            startActivity(intent)
        }

        v3.setOnClickListener {
            val intent = Intent (this, Hum_Records::class.java)
            startActivity(intent)
        }

        n3.setOnClickListener {
            val intent = Intent (this, Hum_Records::class.java)
            startActivity(intent)
        }

        n2.setOnClickListener {
            val intent = Intent (this, Temp_Records::class.java)
            startActivity(intent)
        }

        n1.setOnClickListener {
            val intent = Intent (this, BPM_Records::class.java)
            startActivity(intent)
        }

        heartRateValue.setOnClickListener {
            val intent = Intent (this, Heart_Rate_Value::class.java)
            startActivity(intent)
        }

        humanTempValue.setOnClickListener {
            val intent = Intent (this, Temp_Value::class.java)
            startActivity(intent)
        }

        humValue.setOnClickListener {
            val intent = Intent (this, Resp_Value::class.java)
            startActivity(intent)
        }
    }
}