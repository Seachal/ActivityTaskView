

>  http://note.youdao.com/noteshare?id=44b342e9570b3bc61f894a23d9934db6

# onNewIntent 的调用时机


```
 I/#--#MainActivity :#------: onCreate()
 I/#--#MainActivity :#------: onStart()
 I/#--#MainActivity :#------: onResume()
 I/#--#MainActivity :#------: onPause()
 I/#--#StandardActivity :#------: onCreate()
 I/#--#StandardActivity :#------: onStart()
 I/#--#StandardActivity :#------: onResume()
 I/#--#MainActivity :#------: onStop()
 I/#--#MainActivity :#------: onSaveInstanceState()
 I/#--#StandardActivity :#------: onPause()
 I/#--#SingleTopActivity :#------: onCreate()
 I/#--#SingleTopActivity :#------: onStart()
 I/#--#SingleTopActivity :#------: onResume()
 I/#--#StandardActivity :#------: onStop()
 I/#--#StandardActivity :#------: onSaveInstanceState()
 I/#--#SingleTopActivity :#------: onPause()
 I/#--#SingleTopActivity :#------: onNewIntent()
 I/#--#SingleTopActivity :#------: onResume()
```

![image](http://note.youdao.com/yws/res/25607/D931CCE1A68444329951C6A1D31D6DF5)

接上段的生命周期

![image](http://note.youdao.com/yws/res/25610/2847EF8D8FD143E79B98DCC9F353A708)



## 在 A 启动 A 的情况 ，（A指，SingleTopActivity 或者SingleTaskActivity ，B 指其他 activity）
### singTop 

```
#SingleTopActivity :#------: onPause()
#SingleTopActivity :#------: onNewIntent()
#SingleTopActivity :#------: onResume()
#SingleTopActivity :#------: onPause()
#SingleTopActivity :#------: onNewIntent()
#SingleTopActivity :#------: onResume()
```



### SingTask

```
#SingleTaskActivity :#------: onPause()
#SingleTaskActivity :#------: onNewIntent()
#SingleTaskActivity :#------: onResume()
#SingleTaskActivity :#------: onPause()
#SingleTaskActivity :#------: onNewIntent()
#SingleTaskActivity :#------: onResume()
```



## 在 A 启动 B ，再启动A的情况 （A指，SingleTopActivity 或者SingleTaskActivity ，B 指其他 StandardActivity）

### singTop 

```
#SingleTopActivity :#------: onPause()
#StandardActivity :#------: onCreate()
#StandardActivity :#------: onStart()
#StandardActivity :#------: onResume()
#SingleTopActivity :#------: onStop()
#SingleTopActivity :#------: onSaveInstanceState()
#StandardActivity :#------: onPause()
#SingleTopActivity :#------: onCreate()
#SingleTopActivity :#------: onStart()
#SingleTopActivity :#------: onResume()
#StandardActivity :#------: onStop()
#StandardActivity :#------: onSaveInstanceState()
```

### SingTask
```
#SingleTaskActivity :#------: onStop()
#SingleTaskActivity :#------: onSaveInstanceState()
#StandardActivity :#------: onPause()
#SingleTaskActivity :#------: onRestart()
#SingleTaskActivity :#------: onStart()
#SingleTaskActivity :#------: onNewIntent()
#SingleTaskActivity :#------: onResume()
#StandardActivity :#------: onStop()
#StandardActivity :#------: onDestroy()
```

### 小总结 


当 activity (假设为 A) 的 launchMode 为 singleTop 且 A 的实例已经在 task 栈顶，或者 launchMode 为 singleTask 且 A 的实例已在 task 栈里 (无论是栈顶还是栈中)，再次启动 activity A 时，便不会调用 onCreate() 去产生新的实例，而是调用 onNewIntent() 并重用 task 栈里的 A 实例。


如果 A 在栈顶，那么调用顺序依次是 A.onPause() --> A.onNewIntent() --> A.onResume()。A 的 launchMode 可以是 singleTop 或者是 singlTask。android 开发者官网 上描述的是这种情况。

如果 A 不在栈顶，此时它处于 A.onStop() 状态，当再次启动时，调用顺序依次是 A.onStop() --> A.onNewIntent() --> A.onRestart() --> A.onStart() --> A.onResume()。A 的 launchMode 只能是 singleTask。google 到的其它大多文章描述的是这种情况。

![image](http://note.youdao.com/yws/res/25647/88FBB3C4EC7D4943901CE862AF94AC1C)





___
另外，网上的文章在谈及 activity 的生命周期时，往往只说明单个 activity 的生命周期，而不说明从一个 activity 进入到另一个 activity 时，或者从一个 activity 返回到上一个 activity 时这些函数的调用顺序。现整理如下图所示：


![image](http://note.youdao.com/yws/res/25654/3BD0079DC06D485087229536FDE9959C)



###  A 启动B,  再从 B 返回 A 的情况


* SingleTopActivity
```
#--#StandardActivity :#------: onPause()
#--#SingleTopActivity :#------: onCreate()
#--#SingleTopActivity :#------: onStart()
#--#SingleTopActivity :#------: onResume()
#--#StandardActivity :#------: onStop()
#--#StandardActivity :#------: onSaveInstanceState()
#--#SingleTopActivity :#------: onPause()
#--#StandardActivity :#------: onRestart()
#--#StandardActivity :#------: onStart()
#--#StandardActivity :#------: onResume()
#--#SingleTopActivity :#------: onStop()
#--#SingleTopActivity :#------: onDestroy()
```



* SingleTaskActivity
```
#StandardActivity :#------: onPause()
#SingleTaskActivity :#------: onCreate()
#SingleTaskActivity :#------: onStart()
#SingleTaskActivity :#------: onResume()
#StandardActivity :#------: onStop()
#StandardActivity :#------: onSaveInstanceState()
#SingleTaskActivity :#------: onPause()
#StandardActivity :#------: onRestart()
#StandardActivity :#------: onStart()
#StandardActivity :#------: onResume()
#SingleTaskActivity :#------: onStop()
#SingleTaskActivity :#------: onDestroy()
```


## 总结，只有在B返回 A 时，执行 onRestart。 A启动 B， B 再启动 A 时， 如果启动模式是 singleTask,因为 singleTask 会让 B出栈， 所以相当于返回了 A，  会执行 A的 onRestart 方法，其他启动情况（不包括返回）都不会执行 onRestart 方法。


## 拓展：  https://github.com/baurine/gatsby-blog/blob/8ae0d2510d7b0c88ab29497593ea9b108b35ca86/src/posts/2015-12-26/2015-12-26-android_onnewintent.md


https://github.com/android-exchange/Android-Interview/blob/92794c586e7842d92eddb08928fd4209258b1846/bak/resources/sourcefile/%E5%9F%BA%E6%9C%AC%E7%9F%A5%E8%AF%86%E7%82%B9%E7%9A%84%E7%BB%86%E8%8A%823%E9%AB%98%E7%BA%A7/Activity%E5%90%AF%E5%8A%A8%E6%A8%A1%E5%BC%8F%E5%92%8C%E6%A0%87%E8%AE%B0%E4%BD%8D/-242-Activity%E5%90%AF%E5%8A%A8%E6%A8%A1%E5%BC%8F.md


[Android面试官装逼失败之：Activity的启动模式](https://juejin.im/post/59b0f25551882538cb1ecae1)



----
###  接续拓展，  字节跳动的面试题Activity的启动模式?现在使用 Single Task来启动一个已经存在实例的 Activity?会经历哪些生命周期?在 onnewlntent()中,通过 getlntent(拿到的 Intent,是一个新的 Intent对象吗?

答 ： 
1.生命周期是  onNewIntent,  onRestart  onStart onResume,
*  getIntent 得到的不是一个新对象， 如果想更新 Intent ,需要调用 setIntent(intent)




解答： [Activity以singleTask模式启动，intent传值的解决办法](https://blog.csdn.net/harryweasley/article/details/46557827)


从微信支付的类中，也印证了 '解答'中的回到
![image](http://note.youdao.com/yws/res/25687/2ACD641491B04D6E8CBAB44C60A93181)

----

##  展示一个 Dialog 时的生命周期 （其实是一个透明的 Activity）



```

 <activity
            android:name=".DialogActivity"
            android:theme="@style/dialogstyle" />


```

```
#MainActivity :#------: onPause()
#StandardActivity :#------: onCreate()
#StandardActivity :#------: onStart()
#StandardActivity :#------: onResume()
#MainActivity :#------: onStop()
#MainActivity :#------: onSaveInstanceState()
#StandardActivity :#------: onPause()
#DialogActivity :#------: onCreate()
#DialogActivity :#------: onStart()
#DialogActivity :#------: onResume()
```
可以看到 StandardActivity  的 onStop 方法并没有被执行。


```
-#DialogActivity :#------: onPause()
-#StandardActivity :#------: onResume()
-#DialogActivity :#------: onStop()
-#DialogActivity :#------: onDestroy()
```

###  如果创建一个真正的 dialog 生命周期是怎样的呢？

```
#MainActivity :#------: onPause()
#StandardActivity :#------: onCreate()
#StandardActivity :#------: onStart()
#StandardActivity :#------: onResume()
#MainActivity :#------: onStop()
#MainActivity :#------: onSaveInstanceState()
```
创建一个真实的 AlterDialog 时，并没有影响StandardActivity 的生命周期，（#StandardActivity : onResume() ）。

---


# Android：onNewIntent()触发机制及注意事项 
**一、****onNewIntent()**

在IntentActivity中重写下列方法：onCreate onStart onRestart  onResume  onPause onStop onDestroy  onNewIntent

1、其他应用发Intent，执行下列方法：
onCreate
onStart
onResume

发Intent的方法:

```
Uri uri = Uri.parse("philn://blog.163.com");
Intent it = new Intent(Intent.ACTION_VIEW, uri);    
startActivity(it);
```

2、接收Intent声明：

```
<activity android:name=".IntentActivity" android:launchMode="singleTask"
                  android:label="@string/testname">
             <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:scheme="philn"/>
            </intent-filter>
</activity>
```

3、如果IntentActivity处于任务栈的顶端，也就是说之前打开过的Activity，现在处于onPause、onStop 状态的话，其他应用再发送Intent的话，执行顺序为：
onNewIntent，onRestart，onStart，onResume。

在Android应用程序开发的时候，从一个Activity启动另一个Activity并传递一些数据到新的Activity上非常简单，但是当您需要让后台运行的Activity回到前台并传递一些数据可能就会存在一点点小问题。

首先，在默认情况下，当您通过Intent启到一个Activity的时候，就算已经存在一个相同的正在运行的Activity,系统都会创建一个新的Activity实例并显示出来。为了不让Activity实例化多次，我们需要通过在AndroidManifest.xml配置activity的加载方式（launchMode）以实现单任务模式，如下所示：

```
<activity android:label="@string/app_name" android:launchmode="singleTask"android:name="Activity1"></activity>
```

launchMode为singleTask的时候，通过Intent启到一个Activity,如果系统已经存在一个实例，系统就会将请求发送到这个实例上，但这个时候，系统就不会再调用通常情况下我们处理请求数据的onCreate方法，而是调用onNewIntent方法，如下所示:

```
protected void onNewIntent(Intent intent) {

  super.onNewIntent(intent);

  setIntent(intent);//must store the new intent unless getIntent() will return the old one

  processExtraData();

}
```

不要忘记，系统可能会随时杀掉后台运行的 Activity ，如果这一切发生，那么系统就会调用 onCreate 方法，而不调用 onNewIntent 方法，一个好的解决方法就是在 onCreate 和 onNewIntent 方法中调用同一个处理数据的方法，如下所示：

```
public void onCreate(Bundle savedInstanceState) {

  super.onCreate(savedInstanceState);

  setContentView(R.layout.main);

  processExtraData();

}
```

```
protected void onNewIntent(Intent intent) {

  super.onNewIntent(intent);

   setIntent(intent);//must store the new intent unless getIntent() will return the old one

  processExtraData()

}
```

```
private void processExtraData(){

  Intent intent = getIntent();

  //use the data received here

}
```

 

**二、****onNewIntent()的setIntent()和getIntent()**

```
@Override
protected void onNewIntent(Intent intent) {
	super.onNewIntent(intent);

	// setIntent(intent);

	int data = getIntent().getIntExtra("HAHA", 0);
	// int data = intent.getIntExtra("HAHA", 0);
}
```

如果没有调用setIntent(intent)，则getIntent()获取的数据将不是你所期望的。但是使用intent.getInXxx，貌似可以获得正确的结果。

注意这句话：
Note that getIntent() still returns the original Intent. You can use setIntent(Intent) to update it to this new Intent.
> 请注意，getIntent（）仍返回原始Intent。您可以使用setIntent（Intent）将其更新为新的Intent。

所以最好是调用setIntent(intent)，这样在使用getIntent()的时候就不会有问题了。
----








