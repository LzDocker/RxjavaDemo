package com.professional.rxjavademo;

import android.support.v7.app.AppCompatActivity;
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
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class FuActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "FuActivity";

    Button btn_zip;
    Button btn_base_filter;
    Button btn_sample;
    Button btn_map;
    Button btn_flatmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fu);

        initView();
    }


    public void initView(){

        btn_zip = (Button) findViewById(R.id.btn_zip);
        btn_zip.setOnClickListener(this);

        btn_base_filter = (Button) findViewById(R.id.btn_base_filter);
        btn_base_filter.setOnClickListener(this);

        btn_sample = (Button) findViewById(R.id.btn_sample);
        btn_sample.setOnClickListener(this);

        btn_map = (Button) findViewById(R.id.btn_map);
        btn_map.setOnClickListener(this);


        btn_flatmap = (Button) findViewById(R.id.btn_flatmap);
        btn_flatmap.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_zip :
                zip();
                break;

           case R.id.btn_base_filter :
                filter();
                break;


         case R.id.btn_sample :
                        sample();
                        break;

   case R.id.btn_map :
                        map();
                        break;

   case R.id.btn_flatmap :
                        flatmap();
                        break;




        }
    }


    /*
    *
        分别从 两根水管里各取出一个事件 来进行组合, 并且一个事件只能被使用一次,
        组合的顺序是严格按照事件发送的顺利 来进行的,
        也就是说不会出现圆形1 事件和三角形B 事件进行合并, 也不可能出现圆形2 和三角形A 进行合并的情况.
        最终下游收到的事件数量 是和上游中发送事件最少的那一根水管的事件数量 相同.
        这个也很好理解, 因为是从每一根水管 里取一个事件来进行合并,
        最少的 那个肯定就最先取完 , 这个时候其他的水管尽管还有事件 ,
        但是已经没有足够的事件来组合了, 因此下游就不会收到剩余的事件了.

03-23 17:06:18.489 8623-8623/com.professional.rxjavademo D/FuActivity: onSubscribe
03-23 17:06:18.491 8623-23150/com.professional.rxjavademo D/FuActivity: emit 1
03-23 17:06:18.494 8623-23151/com.professional.rxjavademo D/FuActivity: emit A
03-23 17:06:18.494 8623-23151/com.professional.rxjavademo D/FuActivity: onNext: 1A
03-23 17:06:19.492 8623-23150/com.professional.rxjavademo D/FuActivity: emit 2
03-23 17:06:19.494 8623-23151/com.professional.rxjavademo D/FuActivity: emit B
03-23 17:06:19.494 8623-23151/com.professional.rxjavademo D/FuActivity: onNext: 2B
03-23 17:06:20.492 8623-23150/com.professional.rxjavademo D/FuActivity: emit 3
03-23 17:06:20.494 8623-23151/com.professional.rxjavademo D/FuActivity: emit C
03-23 17:06:20.494 8623-23151/com.professional.rxjavademo D/FuActivity: onNext: 3C
03-23 17:06:21.492 8623-23150/com.professional.rxjavademo D/FuActivity: emit 4
03-23 17:06:21.495 8623-23151/com.professional.rxjavademo D/FuActivity: emit complete2
03-23 17:06:21.495 8623-23151/com.professional.rxjavademo D/FuActivity: onComplete




    * */
    public void zip(){


        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Thread.sleep(1000);

                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Thread.sleep(1000);

                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Thread.sleep(1000);

                Log.d(TAG, "emit 4");
                emitter.onNext(4);
                Thread.sleep(1000);

                Log.d(TAG, "emit complete1");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "emit A");
                emitter.onNext("A");
                Thread.sleep(1000);

                Log.d(TAG, "emit B");
                emitter.onNext("B");
                Thread.sleep(1000);

                Log.d(TAG, "emit C");
                emitter.onNext("C");
                Thread.sleep(1000);

                Log.d(TAG, "emit complete2");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });

    }


    //  zip 应用场景


    /*
    *
    *
    * 比如一个界面需要展示用户的一些信息, 而这些信息分别要从两个服务器接口中获取, 而只有当两个都获取到了之后才能进行展示, 这个时候就可以用Zip了:

      首先分别定义这两个请求接口:

 public interface Api {
    @GET
    Observable<UserBaseInfoResponse> getUserBaseInfo(@Body UserBaseInfoRequest request);

    @GET
    Observable<UserExtraInfoResponse> getUserExtraInfo(@Body UserExtraInfoRequest request);

}
接着用Zip来打包请求:

Observable<UserBaseInfoResponse> observable1 =
        api.getUserBaseInfo(new UserBaseInfoRequest()).subscribeOn(Schedulers.io());

Observable<UserExtraInfoResponse> observable2 =
        api.getUserExtraInfo(new UserExtraInfoRequest()).subscribeOn(Schedulers.io());

Observable.zip(observable1, observable2,
        new BiFunction<UserBaseInfoResponse, UserExtraInfoResponse, UserInfo>() {
            @Override
            public UserInfo apply(UserBaseInfoResponse baseInfo,
                                  UserExtraInfoResponse extraInfo) throws Exception {
                return new UserInfo(baseInfo, extraInfo);
            }
        }).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<UserInfo>() {
            @Override
            public void accept(UserInfo userInfo) throws Exception {
                //do something;
            }
        });

    */



// filter  过滤满足条件的时间继续传递给下游

    /*
    * 当上游有不确定数量的事件发送的时候  过滤有效的事件传递给下游
    * */
  public void filter(){

      Observable.create(new ObservableOnSubscribe<Integer>() {
          @Override
          public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
              for (int i = 0; ; i++) {
                  emitter.onNext(i);
              }
          }
      }).subscribeOn(Schedulers.io())
              .filter(new Predicate<Integer>() {
                  @Override
                  public boolean test(Integer integer) throws Exception {
                      return integer % 10 == 0;
                  }
              })
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Consumer<Integer>() {
                  @Override
                  public void accept(Integer integer) throws Exception {
                      Log.d(TAG, "" + integer);
                  }
              });

  }


    public void sample(){

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
                .sample(2, TimeUnit.SECONDS)  //sample取样
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "" + integer);
                    }
                });

    }


    public void test(){

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                for (int i= 0;;i++){
                    e.onNext(i);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .sample(2,TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Consumer<Integer>() {
              @Override
              public void accept(Integer integer) throws Exception {

              }
          })
        ;


    }


     /*
    *通过Map, 可以将上游发来的事件转换为任意的类型, 可以是一个Object, 也可以是一个集合
    * */

    //   map 操作符
    public void map(){

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });


    }



    /*
    *
    * 上游每发送一个事件, flatMap都将创建一个新的水管,
    * 然后发送转换之后的新的事件, 下游接收到的就是这些新的水管发送的数据.
    * 这里需要注意的是, flatMap并不保证事件的顺序, 也就是图中所看到的,
    * 并不是事件1就在事件2的前面.
    *
    * 如果需要保证顺序则需要使用concatMap.
    *
    *
    *                                                      D/RxbaseActivity: I am value 1
03-23 16:31:02.311 29756-29986/com.professional.rxjavademo D/RxbaseActivity: I am value 1
03-23 16:31:02.311 29756-29986/com.professional.rxjavademo D/RxbaseActivity: I am value 1
03-23 16:31:02.311 29756-29984/com.professional.rxjavademo D/RxbaseActivity: I am value 3
03-23 16:31:02.311 29756-29984/com.professional.rxjavademo D/RxbaseActivity: I am value 3
03-23 16:31:02.311 29756-29984/com.professional.rxjavademo D/RxbaseActivity: I am value 3
03-23 16:31:02.311 29756-31232/com.professional.rxjavademo D/RxbaseActivity: I am value 2
03-23 16:31:02.311 29756-31232/com.professional.rxjavademo D/RxbaseActivity: I am value 2
03-23 16:31:02.311 29756-31232/com.professional.rxjavademo D/RxbaseActivity: I am value 2

    *
    *
    *
    * */

    // flatmap 操作符
    public void flatmap(){

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);  // 为了看到flatMap结果是无序的,所以加了10毫秒的延时
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }



    // flatmap 实践
    public void test7(){

     /*   api.register(new RegisterRequest())            //发起注册请求
                .subscribeOn(Schedulers.io())               //在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求注册结果
                .doOnNext(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(RegisterResponse registerResponse) throws Exception {
                        //先根据注册的响应结果去做一些操作
                    }
                })
                .observeOn(Schedulers.io())                 //回到IO线程去发起登录请求
                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
                    @Override
                    public ObservableSource<LoginResponse> apply(RegisterResponse registerResponse) throws Exception {
                        return api.login(new LoginRequest());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求登录的结果
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse loginResponse) throws Exception {
                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });

*/
    }



}
