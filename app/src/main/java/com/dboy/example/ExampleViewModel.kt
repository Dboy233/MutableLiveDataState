package com.dboy.example

import androidx.lifecycle.ViewModel
import com.dboy.livedata.MutableLiveDataState
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ExampleViewModel : ViewModel() {
    /**
     * 模拟子线程更新数据
     */
    var executor: Executor = Executors.newCachedThreadPool()

    /**
     * 数据模拟1 在主线程中更新
     */
    val dataExt1 = MutableLiveDataState<String>()

    /**
     * 数据模拟2 在子线程中更新
     */
    val dataExt2 = MutableLiveDataState<String>()

    /**
     * 主线程更新
     */
    fun changeData1() {
        dataExt1.onStart()
        dataExt1.setValueSuccess("name = 老王; sex = 男")
    }

    /**
     * 主线程更新
     */
    fun changeErrorData1() {
        dataExt1.onStart()
        dataExt1.setValueError("NULL", "数据1请求出错了")
    }

    /**
     * 子线程更新
     */
    fun changeData2() {
        dataExt2.onStart()
        executor.execute {
            Thread.sleep(300)
            dataExt2.postValue("name = 老红；sex = 女")
            dataExt2.onSuccess()
        }
    }

    //子线程更新
    fun changeErrorData2() {
        dataExt2.onStart()
        executor.execute {
            Thread.sleep(300)
            dataExt2.onError("数据2请求出错了")
        }
    }
}