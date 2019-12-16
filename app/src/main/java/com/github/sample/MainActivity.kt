package com.github.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.github.loadingbutton.LoadingButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_loading.setButtonClickListener {
            Log.i("qfxl","click me")
            Handler().postDelayed({
                Toast.makeText(this, "operate failed", Toast.LENGTH_SHORT).show()
                btn_loading.state = LoadingButton.STATE.NORMAL
            }, 2000)
        }


    }
}
