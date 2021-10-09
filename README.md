# MutableLiveDataStatus
对MutableLiveData增加状态信息

## 获取依赖
last-version [![](https://jitpack.io/v/Dboy233/MutableLiveDataState.svg)](https://jitpack.io/#Dboy233/MutableLiveDataState)

    ```
    allprojects {
    		repositories {
    			...
    			maven { url 'https://jitpack.io' }
    		}
    	}

    dependencies {
           implementation 'com.github.Dboy233:MutableLiveDataState:last-version'
    }
    ```

## 使用方式

```kotlin
      class ExampleViewModel : ViewModel(){
        val data = MutableLiveDataStatus<String>()

        fun loadData(){
            data.onStart()
            //RxJava or Coroutines
            //request success
            if(isRequestSuccess){
                data.value = "new data"
                data.onSuccess()
                //data.setValueSuccess("newData")
            }else{
                data.onError()
                //data.setValueError("error data")
            }
        }
      }
```
```kotlin
    class MainActivity :Activity(){

        lateinit var viewModel: ExampleViewModel

        //...

        fun initLiveData(){
           viewModel.data.observe(this,{
               //update UI
           }){
            when (it!!) {
                START -> {
                    //数据开始改变之前的状态 需要手动调用 onStart()
                }
                SUCCESS -> {
                    //处理数据请求成功逻辑  只通知一次 手从触发 onSuccess() or setValueSuccess()
                }
                ERROR -> {
                    //处理数据请求失败逻辑  只通知一次 手动触发 onError() or setValueError()
                }
                SUCCESS_COMPLETE -> {
                    //处理成功逻辑处理完成之后结束状态 用于防止重建导致的重复触发 自动触发 SUCCESS状态之后
                }
                ERROR_COMPLETE -> {
                    //处理失败逻辑处理完成之后的结束状态 用于防止重建导致的重复触发 自动触发 ERROR状态之后
                }
                RESET -> {
                    //重置状态，数据当前需要重置，需要新的数据进行补充。在这里进行数据请求等操作。默认自动触发，可手动重新触发onReset()
                   viewModel.loadData()
                }
            }
           }
        }

    }

```
