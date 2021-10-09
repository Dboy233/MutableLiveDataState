package com.dboy.livedata

import androidx.lifecycle.MutableLiveData
import com.dboy.livedata.DataStateEnum.*

/**
 * - 文件描述: 状态容器
 * @author DBoy
 * @since 2021/10/9 2:37 下午
 */
internal class DataState : MutableLiveData<DataStateEnum>(RESET) {

    /**
     * [postValue]最终也会调用[setValue]改变数据的,所以这里对异步时的连续更新操作进行判断；
     * 如果它的前一个状态不是[SUCCESS]且下一个状态是[SUCCESS_COMPLETE]必须先通知[SUCCESS].[ERROR]同理。
     */
    override fun setValue(value: DataStateEnum?) {
        when (value) {
            SUCCESS_COMPLETE -> {
                //确保逻辑执行顺序。它的上一个状态必须是SUCCESS
                if (getValue() != SUCCESS) {
                    super.setValue(SUCCESS)
                }
            }
            ERROR_COMPLETE -> {
                //确保逻辑执行顺序。它的上一个状态必须是ERROR
                if (getValue() != ERROR) {
                    super.setValue(ERROR)
                }
            }
            else -> {
            }
        }
        super.setValue(value)
    }
}