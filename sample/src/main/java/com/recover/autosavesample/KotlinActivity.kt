package com.recover.autosavesample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.noober.api.NeedSave
import com.noober.savehelper.SaveHelper

class KotlinActivity : AppCompatActivity() {

    @NeedSave
    @JvmField
    var a :Int=3

    @NeedSave
    lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        SaveHelper.recover(this, savedInstanceState)
        Log.e("KotlinActivity",  a.toString())

    }


//    override fun onSaveInstanceState(outState: Bundle?) {
//        Log.e("KotlinActivity",  "onSaveInstanceState")
//        a = 2
//        SaveHelper.save(this,  outState)
//        super.onSaveInstanceState(outState)
//    }
}
