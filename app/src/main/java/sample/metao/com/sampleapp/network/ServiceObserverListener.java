package sample.metao.com.sampleapp.network;

/**
 * Created by metao on 3/8/2017.
 */
public interface ServiceObserverListener {

    void onError(Throwable throwable);

    void onNext(String data);

    void onFinished(String result);
}
