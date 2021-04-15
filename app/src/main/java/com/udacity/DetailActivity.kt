package com.udacity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)


        //Use Parcelize?
        val bundle = intent.extras
        val status = bundle?.getBoolean("status")
        val description = bundle?.getString("description")

        Log.i("TAG", "$status - $description")

        detail_ok_button.setOnClickListener {
            finish()
        }
    }

}
