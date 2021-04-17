package com.udacity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private var statusStr: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)


        val bundle = intent.extras
        val status = bundle?.getBoolean("status")
        val description = bundle?.getString("description")
        val primaryDarkGreen = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        if (status == true) {
            statusStr = getString(R.string.success)
            detail_status_text_view.setTextColor(primaryDarkGreen)
        } else {
            statusStr = getString(R.string.failed)
            detail_status_text_view.setTextColor(Color.RED)
        }

        detail_description_text_view.text = description
        detail_status_text_view.text = statusStr

        detail_ok_button.setOnClickListener {
            if (isTaskRoot) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                finish()
            }
        }
    }

}
