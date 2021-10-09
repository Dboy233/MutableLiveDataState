package com.dboy.livedata

/**
 * MutableLiveData的数据状态枚举
 */
enum class DataStateEnum {
    /**
     * 数据开始请求前
     */
    START,

    /**
     * 数据请求完成
     */
    SUCCESS,

    /**
     * 数据请求错误
     */
    ERROR,

    /**
     * 数据成功数据完成
     */
    SUCCESS_COMPLETE,

    /**
     * 数据错误处理完成
     */
    ERROR_COMPLETE,

    /**
     * 数据重制
     */
    RESET;

    /**
     * 附加信息
     */
    var msg: String = this.toString()

    /**
     * 附加数据
     */
    var any: Any? = null

}