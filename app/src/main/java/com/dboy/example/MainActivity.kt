package com.dboy.example

import android.os.Bundle
import android.util.Log
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dboy.livedata.DataStateEnum.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var viewModel: ExampleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<TextView>(R.id.showTv).addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            val scrollView = findViewById<ScrollView>(R.id.scrollView)
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }

        viewModel = ViewModelProvider(this).get(ExampleViewModel::class.java)

        viewModel.dataExt1.observe(this, {
            printLog("change1", it)
        }) {
            printLog("status1", "$it - msg:${it.msg}")
            when (it!!) {
                START -> {
                    //数据开始改变之前的状态 需要手动调用 onStart()
                }
                SUCCESS -> {
                    //处理数据请求成功逻辑  只通知一次 自动触发 setValue/postValue
                    printLog("status1", "成功，进行逻辑，toast，页面跳转处理。\n页面重建我不会再执行第二次。旋转屏幕试试。")
                }
                ERROR -> {
                    //处理数据请求失败逻辑  只通知一次 手动触发 onError()
                    printLog("status1", "失败，进行逻辑，toast，页面跳转处理。\n页面重建我不会再执行第二次。旋转屏幕试试")
                }
                SUCCESS_COMPLETE -> {
                    //处理成功逻辑处理完成之后结束状态 用于防止重建导致的重复触发 自动触发 SUCCESS状态之后
                }
                ERROR_COMPLETE -> {
                    //处理失败逻辑处理完成之后的结束状态 用于防止重建导致的重复触发 自动触发 ERROR状态之后
                }
                RESET -> {
                    //重置状态，数据当前需要重置，需要新的数据进行补充。在这里进行数据请求等操作。默认自动触发，可手动重新触发onReset()
//                    viewModel.changeData1()
                }
            }
        }

        viewModel.dataExt2.observe(this, {
            printLog("change2", it)
        }) {
            printLog("status2", "$it - msg:${it.msg}")
        }

    }

    fun setSuccess1(view: android.view.View) {
        printLog("模拟成功", "主线程更新")
        viewModel.changeData1()
    }

    fun setError1(view: android.view.View) {
        printLog("模拟错误", "主线程更新")
        viewModel.changeErrorData1()
    }

    fun setSuccess2(view: android.view.View) {
        printLog("模拟成功", "子线程更新")
        viewModel.changeData2()
    }

    fun setError2(view: android.view.View) {
        printLog("模拟错误", "子线程更新")
        viewModel.changeErrorData2()
    }


    fun clearLog(view: android.view.View) {
        findViewById<TextView>(R.id.showTv).text = ""
    }

    private fun printLog(tag: String, log: String) {
        Log.d("DBoy", "$tag : $log")
        val showTv = findViewById<TextView>(R.id.showTv)
        showTv.text = "${showTv.text}\n $tag : $log"
    }

}