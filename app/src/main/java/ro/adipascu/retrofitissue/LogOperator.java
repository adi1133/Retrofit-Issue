package ro.adipascu.retrofitissue;

import rx.functions.Action1;
import rx.internal.operators.OperatorDoOnEach;

/**
 * Created by Adi Pascu on 5/5/2015.
 * Email mail@adipascu.ro
 */
public class LogOperator<T> extends OperatorDoOnEach<T> {
    public LogOperator(String tag) {
        super(new LogSubscriber<T>(tag));
    }

    public LogOperator(String tag, Action1<T> onNextListener) {
        super(new LogSubscriber<>(tag, onNextListener));
    }
}
