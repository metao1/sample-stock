package sample.metao.com.sampleapp.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by metao on 3/7/2017.
 */
public abstract class NetworkManager {
    private AsyncTask<String, String, String> executor;


    public NetworkManager(Map<String, String> params, String endPointAddress) {
        if (endPointAddress != null) {
            try {
                executor = new Connector(params, endPointAddress).execute("");
            } catch (IOException e) {
                e.printStackTrace();
                onError(e.getCause());
            }
        }
    }

    public void stop() {
        if (!executor.isCancelled()) {
            executor.cancel(true);
        }
    }

    private class Connector extends AsyncTask<String, String, String> {

        private String urlAddress;
        private final Map<String, String> params;

        Connector(Map<String, String> params, String urlAddress) throws IOException {
            this.urlAddress = urlAddress;
            this.params = params;
        }

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            String result = "";
            String data = "";
            try {
                String paramsString = "?";
                if (params != null && params.size() > 0) {
                    for (Map.Entry<String, String> set : params.entrySet()) {
                        urlAddress += paramsString.concat(set.getKey()).concat("=")
                                .concat(set.getValue()).concat("&");
                    }
                }
                if (!paramsString.equalsIgnoreCase("?")) {
                    urlAddress = urlAddress.concat(paramsString);
                }
                Log.d("address", urlAddress);
                url = new URL(urlAddress);
                InputStream inputStream = url.openStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
                while ((data = buffer.readLine()) != null) {
                    result += data;
                    publishProgress(data);
                }
                inputStream.close();
            } catch (MalformedURLException e) {
                onError(e.getCause());
            } catch (IOException e) {
                onError(e.getCause());
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values != null && values.length > 0) {
                onNext(values[0]);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            onFinished(result);
        }
    }

    protected abstract void onFinished(String result);

    protected abstract void onError(Throwable cause);

    public abstract void onNext(String data);
}
