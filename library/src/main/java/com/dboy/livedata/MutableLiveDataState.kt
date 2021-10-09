package com.dboy.livedata

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * - 文件描述: 扩展MutableLiveData的状态
 *      在主线程中会先更新数据，再更新数据状态。根据自己需要进行选择。
 *
 *      如果需要先更新状态:
 *      liveData.onSuccess()
 *      liveData.value = value
 *
 *      如果需要先更新数据:
 *      liveData.value = value
 *      liveData.onSuccess()
 *      或者
 *      liveData.setValueSuccess(value)
 *
 * @author DBoy
 * @since 2021/10/9 2:06 下午
 */
class MutableLiveDataState<T> : MutableLiveData<T> {

    /**
     * 记录数据状态
     */
    private val state: DataState by lazy {
        DataState()
    }

    // region 默认构造器
    constructor() : super()

    constructor(value: T) : super(value)
    //endregion

    // region 观察数据
    /**
     * 观察LiveData的 [数据] 和 [状态]
     */
    fun observe(
        owner: LifecycleOwner,
        observer: Observer<in T>,
        observerStatus: Observer<DataStateEnum>
    ) {
        observe(owner, observer)
        observeStatus(owner, observerStatus)
    }

    /**
     * 观察LiveData的 [状态]
     */
    fun observeStatus(owner: LifecycleOwner, observer: Observer<DataStateEnum>) {
        this.state.observe(owner, observer)
    }
    //endregion

    //region 通知修改数据状态
    /**
     * 通知LiveData的数据开始变化状态 [setValue]之前调用
     * @param msg 状态附加消息
     * @param any 状态附加数据
     */
    @JvmOverloads
    fun onStart(msg: String? = null, any: Any? = null) {
        changeLiveDataStatus(this.state, DataStateEnum.START, msg, any)
    }

    /**
     * 通知LiveData的数据变化成功状态；
     * @param msg 状态附加消息
     * @param any 状态附加数据
     */
    @JvmOverloads
    fun onSuccess(msg: String? = null, any: Any? = null) {
        if (isMainThread()) {
            //如果是子线程，SUCCESS会被SUCCESS_COMPLETE覆盖，不如不通知，交给State.setValue去处理逻辑
            changeLiveDataStatus(this.state, DataStateEnum.SUCCESS, msg, any)
        }
        changeLiveDataStatus(this.state, DataStateEnum.SUCCESS_COMPLETE, msg, any)
    }

    /**
     * 通知LiveData的数据变化失败状态；
     * @param msg 状态附加消息
     * @param any 状态附加数据
     */
    @JvmOverloads
    fun onError(msg: String? = null, any: Any? = null) {
        if (isMainThread()) {
            //如果是子线程，ERROR会被ERROR_COMPLETE覆盖，不如不通知，交给State.setValue去处理逻辑
            changeLiveDataStatus(this.state, DataStateEnum.ERROR, msg, any)
        }
        changeLiveDataStatus(this.state, DataStateEnum.ERROR_COMPLETE, msg, any)
    }

    /**
     * 通知LiveData的数据重置状态，一般用于触发重新获取数据
     * @param msg 状态附加消息
     * @param any 状态附加数据
     */
    @JvmOverloads
    fun onReset(msg: String? = null, any: Any? = null) {
        changeLiveDataStatus(this.state, DataStateEnum.RESET, msg, any)
    }


    /**
     * 与[MutableLiveData.setValue],[MutableLiveData.postValue]相同，增加了自动设置成功状态
     * @param msg 状态附加消息
     * @param any 状态附加数据
     */
    @JvmOverloads
    fun setValueSuccess(value: T, msg: String? = null, any: Any? = null) {
        if (isMainThread()) {
            setValue(value)
        } else {
            postValue(value)
        }
        onSuccess(msg, any)
    }

    /**
     * 与[MutableLiveData.setValue],[MutableLiveData.postValue]相同，增加了自动设置成功状态
     * @param msg 状态附加消息
     * @param any 状态附加数据
     */
    @JvmOverloads
    fun setValueError(value: T, msg: String? = null, any: Any? = null) {
        if (isMainThread()) {
            setValue(value)
        } else {
            postValue(value)
        }
        onError(msg, any)
    }

    //endregion

    /**
     * 修改LiveData的状态
     */
    private fun changeLiveDataStatus(
        liveData: MutableLiveData<DataStateEnum>,
        state: DataStateEnum,
        msg: String?,
        any: Any?
    ) {
        msg?.let {
            state.msg = it
        }
        any?.let {
            state.any = it
        }
        if (isMainThread()) {
            liveData.value = state
        } else {
            liveData.postValue(state)
        }
    }

    /**
     * 判断是否在主线程
     */
    private fun isMainThread() = Looper.getMainLooper() == Looper.myLooper()

}