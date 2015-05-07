package ro.adipascu.retrofitissue;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Adi Pascu on 5/7/2015.
 * Email mail@adipascu.ro
 */
public interface Api {

    @GET("/polo/{marco}")
    Observable<EchoModel> echo(@Path("marco") String shout);
}
