package com.professional.rxjavademo;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxbaseActivity extends Activity {

    Button btn_base;
    Button btn_base_lianshi;
    Button btn_base_qiduan;
    Button btn_base_thead;
    String TAG = "RxbaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbase);
        btn_base = (Button) findViewById(R.id.btn_base);
        btn_base_lianshi = (Button) findViewById(R.id.btn_base_lianshi);
        btn_base_qiduan = (Button) findViewById(R.id.btn_base_qiduan);
        btn_base_thead = (Button) findViewById(R.id.btn_base_thead);



        initView();

    }


    public void initView(){


        btn_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                test1();


            }
        });
        btn_base_lianshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                test2();


            }
        });
        btn_base_qiduan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                test3();


            }
        });
        btn_base_thead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                test4();


            }
        });


    }


    public void test1(){

        //创建一个上游 Observable：
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });
        //创建一个下游 Observer
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
            }

            @Override
            public void onNext(Integer value) {

                Log.d(TAG, "" + value);



            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
            }
        };
        //建立连接
        observable.subscribe(observer);
    }



    // 链式调用

    /*
    *
    *
    *
    * ObservableEmitter： Emitter是发射器的意思，那就很好猜了，这个就是用来发出事件的，它可以发出三种类型的事件
    * 通过调用emitter的onNext(T value)、onComplete()和onError(Throwable error)就可以分别发出next事件、complete事件和error事件。
      但是，请注意，并不意味着你可以随意乱七八糟发射事件，需要满足一定的规则：

    上游可以发送无限个onNext, 下游也可以接收无限个onNext.
    当上游发送了一个onComplete后, 上游onComplete之后的事件将会继续发送, 而下游收到onComplete事件之后将不再继续接收事件.
    当上游发送了一个onError后, 上游onError之后的事件将继续发送, 而下游收到onError事件之后将不再继续接收事件.
    上游可以不发送onComplete或onError.
    最为关键的是onComplete和onError必须唯一并且互斥, 即不能发多个onComplete, 也不能发多个onError, 也不能先发一个onComplete, 然后再发一个onError, 反之亦然
    注: 关于onComplete和onError唯一并且互斥这一点, 是需要自行在代码中进行控制, 如果你的代码逻辑中违背了这个规则, 并不一定会导致程序崩溃.
    比如发送多个onComplete是可以正常运行的, 依然是收到第一个onComplete就不再接收了, 但若是发送多个onError, 则收到第二个onError事件会导致程序会崩溃.
    *
    *
    *
    *
    * */

    public void test2(){

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
            }
        });


    }


    // 切断管道

    /*
    *调用dispose()并不会导致上游不再继续发送事件, 上游会继续发送剩余的事件.
    *
    * 有很多应用场景
    *
    *
      * 网络请求时如果在请求的过程中Activity已经退出了,
      * 这个时候如果回到主线程去更新UI, 那么APP肯定就崩溃了,
      * 怎么办呢, 上一节我们说到了Disposable ,
      * 说它是个开关, 调用它的dispose()方法时就会切断水管,
       * 使得下游收不到事件, 既然收不到事件,
       * 那么也就不会再去更新UI了. 因此我们可以在Activity中将这个Disposable
       * 保存起来, 当Activity退出时, 切断它即可.

       那如果有多个Disposable 该怎么办呢, RxJava中已经内置了一个容器CompositeDisposable,
       每当我们得到一个Disposable时就调用CompositeDisposable.add()将它添加到容器中, 在退出的时候,
       调用CompositeDisposable.clear() 即可切断所有的水管.
    *
    *
    * */


    public void test3(){


        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            public Disposable dis;

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
                dis = d;
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "" + value);


                if(value==2){
                    dis.dispose();
                    Log.d(TAG, "" + value+dis.isDisposed());
                }

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
            }
        });

    }

    
    // 切换线程

     /*
     *
     * subscribeOn() 指定的是上游发送事件的线程, observeOn() 指定的是下游接收事件的线程.

       多次指定上游的线程只有第一次指定的有效, 也就是说多次调用subscribeOn() 只有第一次的有效, 其余的会被忽略.

       多次指定下游的线程是可以的, 也就是说每调用一次observeOn() , 下游的线程就会切换一次.
     *
     *
     * */

    public void test4(){


        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
                Log.d(TAG, "onNext: " + integer);
            }
        };

      /*  observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);*/

     /*

      每一个onnext 事件可以注册多个onafternext 事件  ，调用数序为 onnext ----》onafternext--->onafternext
      observable.doAfterNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "doAfterNext"+integer);

            }
        }).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "doOnNext "+integer);
            }
        })

                .subscribeOn(Schedulers.newThread())
          .observeOn(Schedulers.computation()).subscribe(consumer);
*/


        /*
        *
        * Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
          Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
          Schedulers.newThread() 代表一个常规的新线程
          AndroidSchedulers.mainThread() 代表Android的主线程


          subscribeOn() 指定的是上游发送事件的线程, observeOn() 指定的是下游接收事件的线程.
          多次指定上游的线程只有第一次指定的有效, 也就是说多次调用subscribeOn() 只有第一次的有效, 其余的会被忽略.
          多次指定下游的线程是可以的, 也就是说每调用一次observeOn() , 下游的线程就会切换一次.

        *
        * */

        observable.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "After observeOn(mainThread), current thread is: " + Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "After observeOn(io), current thread is : " + Thread.currentThread().getName());
                    }
                })
                .subscribe(consumer);
    }




}
