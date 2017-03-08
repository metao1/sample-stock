package sample.metao.com.sampleapp.network;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by metao on 3/8/2017.
 */
public class ServiceObserver {

    private final ServiceObserverListener serviceObserverListener;
    private final ConcurrentHashMap<String, String> params;
    private NetworkManager networkManager;
    private UIHandler uiHandler;
    private String endPointAddress;
    private static int itemsToLoad;

    public ServiceObserver(ServiceObserverListener serviceObserverListener) {
        this.serviceObserverListener = serviceObserverListener;
        this.params = new ConcurrentHashMap<>();
    }

    public void setService(String endPointAddress) {
        this.endPointAddress = endPointAddress;
        params.put("limit", "4");
        startToLoad();
    }

    private void startToLoad() {
        uiHandler = new UIHandler(this, Looper.getMainLooper());
        if (endPointAddress != null) {
            networkManager = new NetworkManager(params, endPointAddress) {
                @Override
                protected void onFinished(String result) {
                    Message message = new Message();
                    message.obj = result;
                    message.what = 2;
                    uiHandler.sendMessage(message);
                }

                @Override
                protected void onError(Throwable cause) {
                    Message message = new Message();
                    message.obj = cause;
                    uiHandler.sendMessage(message);
                }

                @Override
                public void onNext(String data) {
                    Message message = new Message();
                    message.obj = data;
                    message.what = 1;
                    uiHandler.sendMessage(message);
                }
            };
        }
    }

    public void stop() {
        if (uiHandler != null) {
            if (uiHandler.hasMessages(1)) {
                uiHandler.removeMessages(1);
            }
            if (uiHandler.hasMessages(2)) {
                uiHandler.removeMessages(2);
            }
        }
        if (networkManager != null) {
            networkManager.stop();
        }
    }

    public void loadMore(int load) {
        if (load > 0) {
            this.itemsToLoad += load;
            params.put("skip", String.valueOf(itemsToLoad));
        }
        params.put("limit", "3");
        startToLoad();
    }

    public void fetchStarter() {
        startToLoad();
    }

    public void onlyInStock(boolean isItemsInStock) {
        if (isItemsInStock) {
            params.put("onlyInStock", String.valueOf(true));
        } else {
            if (params.containsKey("onlyInStock")) {
                params.remove("onlyInStock");
            }
        }
        startToLoad();
    }

    public void search(String searchText) {
        params.put("q", searchText);
        startToLoad();
    }

    private static class UIHandler extends Handler {
        private final WeakReference<ServiceObserver> serviceObserverWeakReference;

        UIHandler(ServiceObserver serviceObserver, Looper mainLooper) {
            super(mainLooper);
            serviceObserverWeakReference = new WeakReference<>(serviceObserver);
        }

        @Override
        public void handleMessage(Message msg) {
            ServiceObserver serviceObserver = serviceObserverWeakReference.get();
            if (msg.obj instanceof Throwable) {
                serviceObserver.serviceObserverListener.onError((Throwable) msg.obj);
                return;
            }
            switch (msg.what) {
                case 1:
                    serviceObserver.serviceObserverListener.onNext((String) msg.obj);
                    break;
                case 2:
                    serviceObserver.serviceObserverListener.onFinished((String) msg.obj);
                    break;
            }
        }
    }
}
