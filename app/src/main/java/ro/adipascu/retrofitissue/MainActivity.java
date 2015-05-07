package ro.adipascu.retrofitissue;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static ro.adipascu.retrofitissue.App.api;

/**
 * Created by Adi Pascu on 5/7/2015.
 * Email mail@adipascu.ro
 */
public class MainActivity extends Activity implements View.OnClickListener {
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button = new Button(this);
        button.setText("tap me!");
        setContentView(button);
        button.setOnClickListener(this);


//        failLogged();
//        failLogged2();


    }

    @Override
    public void onClick(View v) {
        failUi();
//        hackyFixUi();
//        successTrivialUI();
    }


    private void setBusy(boolean isBusy) {
        button.setEnabled(!isBusy);
        if (isBusy)
            button.setText("I am busy doing IO using Retrofit!");
    }

    private void successTrivialUI() {
        setBusy(true);
        api.echo("first").observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<EchoModel>() {
            @Override
            public void call(EchoModel echoModel) {
                button.setText("success");
                setBusy(false);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                button.setText("error");
                setBusy(false);
            }
        }, new Action0() {
            @Override
            public void call() {
//                This is redundant since we handle onNext
//                setBusy(false);
            }
        });
    }

    private void failUi() {
        setBusy(true);
        api.echo("first").switchMap(new Func1<EchoModel, Observable<EchoModel>>() {
            @Override
            public Observable<EchoModel> call(EchoModel echoModel) {
                return api.echo("second");
            }
        }).switchMap(new Func1<EchoModel, Observable<EchoModel>>() {
            @Override
            public Observable<EchoModel> call(EchoModel echoModel) {
                api.echo("test").subscribe(new LogSubscriber<EchoModel>("Log Branch")); //this works
                return api.echo("third");
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<EchoModel>() {
            @Override
            public void call(EchoModel echoModel) {
                button.setText("success");
                setBusy(false);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                button.setText("error");
                setBusy(false);
            }
        }, new Action0() {
            @Override
            public void call() {
//                This is redundant since we handle onNext
//                setBusy(false);
            }
        });
    }


    private void failLogged() {
        api.echo("first").switchMap(new Func1<EchoModel, Observable<EchoModel>>() {
            @Override
            public Observable<EchoModel> call(EchoModel echoModel) {
                return api.echo("second");
            }
        }).switchMap(new Func1<EchoModel, Observable<EchoModel>>() {
            @Override
            public Observable<EchoModel> call(EchoModel echoModel) {
                api.echo("test").subscribe(new LogSubscriber<EchoModel>("Log Branch")); //this works
                return api.echo("third");
            }
        }).subscribe(new LogSubscriber<EchoModel>("FINAL LOG"));
    }

    private void failLogged2() {
        api.echo("first").lift(new LogOperator<EchoModel>("INTERMEDIATE LOG")).switchMap(new Func1<EchoModel, Observable<EchoModel>>() {
            @Override
            public Observable<EchoModel> call(EchoModel echoModel) {
                return api.echo("second");
            }
        }).lift(new LogOperator<EchoModel>("INTERMEDIATE LOG")).switchMap(new Func1<EchoModel, Observable<EchoModel>>() {
            @Override
            public Observable<EchoModel> call(EchoModel echoModel) {
                api.echo("test").subscribe(new LogSubscriber<EchoModel>("Log Branch")); //this works
                return api.echo("third");
            }
        }).lift(new LogOperator<EchoModel>("INTERMEDIATE LOG")).subscribe(new LogSubscriber<EchoModel>("FINAL LOG"));
    }

    private void hackyFixUi() {
        setBusy(true);
        api.echo("first").switchMap(new Func1<EchoModel, Observable<EchoModel>>() {
            @Override
            public Observable<EchoModel> call(EchoModel echoModel) {
                return api.echo("second");
            }
        }).observeOn(Schedulers.io()).switchMap(new Func1<EchoModel, Observable<EchoModel>>() { //Here is the hack to make it work, it is .observeOn(Schedulers.io())
            @Override
            public Observable<EchoModel> call(EchoModel echoModel) {
                api.echo("test").subscribe(new LogSubscriber<EchoModel>("Log Branch")); //this works
                return api.echo("third");
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<EchoModel>() {
            @Override
            public void call(EchoModel echoModel) {
                button.setText("success");
                setBusy(false);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                button.setText("error");
                setBusy(false);
            }
        }, new Action0() {
            @Override
            public void call() {
//                This is redundant since we handle onNext
//                setBusy(false);
            }
        });
    }
}
