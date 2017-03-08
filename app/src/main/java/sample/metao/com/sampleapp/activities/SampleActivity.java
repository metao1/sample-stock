package sample.metao.com.sampleapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import sample.metao.com.sampleapp.R;
import sample.metao.com.sampleapp.adapters.TestAdapter;
import sample.metao.com.sampleapp.models.Item;
import sample.metao.com.sampleapp.network.ServiceObserver;
import sample.metao.com.sampleapp.network.ServiceObserverListener;
import sample.metao.com.sampleapp.utils.Util;
import sample.metao.com.sampleapp.view.OnLoadMoreListener;
import sample.metao.com.sampleapp.view.RecyclerViewLoadManager;

import java.util.ArrayList;

/**
 * Created by metao on 3/7/2017.
 */
public class SampleActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener
        , SearchView.OnQueryTextListener {

    private static final String END_POINT_ADDRESS = "http://192.168.1.3/website/v5/data.php";
    private RecyclerView recycleView;
    private ArrayList<Item> items;
    private String TAG = SampleActivity.class.getSimpleName();
    private ServiceObserver serviceObserver;
    private TestAdapter testAdapter;
    private OnLoadMoreListener loadMoreHandler;
    private ToggleButton onlyInStockToggle;
    private RecyclerViewLoadManager recyclerViewLoadManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_layout);
        setTitle("");
        items = new ArrayList<>();
        loadMoreHandler = new LoadMoreHandler();
        recycleView = (RecyclerView) findViewById(R.id.recycle_view);
        onlyInStockToggle = (ToggleButton) findViewById(R.id.only_in_stock_toggle);
        onlyInStockToggle.setOnCheckedChangeListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recycleView.setLayoutManager(linearLayoutManager);
        testAdapter = new TestAdapter(getBaseContext(), items);
        recycleView.setAdapter(testAdapter);
        recyclerViewLoadManager = new RecyclerViewLoadManager(linearLayoutManager, loadMoreHandler);
        recycleView.addOnScrollListener(recyclerViewLoadManager);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        testAdapter.clearAll();
        serviceObserver.onlyInStock(checked);
    }

    @Override
    public boolean onQueryTextSubmit(String searchText) {
        if (searchText != null && !TextUtils.isEmpty(searchText)) {
            serviceObserver.search(searchText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    class LoadMoreHandler implements OnLoadMoreListener {

        @Override
        public void onLoadMore() {
            serviceObserver.loadMore(3);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (serviceObserver == null) {
            serviceObserver = new ServiceObserver(new ServiceObserverHandler());
        }
        serviceObserver.setService(END_POINT_ADDRESS);
        serviceObserver.fetchStarter();
        recyclerViewLoadManager.setLoading(false);
    }

    @Override
    public void onBackPressed() {
        stopAll();
        super.onBackPressed();

    }

    private void stopAll() {
        serviceObserver.stop();
        recyclerViewLoadManager.setLoading(true);
    }

    @Override
    protected void onDestroy() {
        stopAll();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        stopAll();
        super.onStop();
    }

    private class ServiceObserverHandler implements ServiceObserverListener {

        @Override
        public void onError(Throwable throwable) {
            Toast.makeText(getBaseContext(),
                    "Error while working: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, throwable.getMessage());
        }

        @Override
        public void onNext(String data) {
            if (data != null && !TextUtils.isEmpty(data)) {
                try {
                    Item item = Util.parseData(data);
                    testAdapter.setItem(item);
                } catch (Exception e) {
                    onError(e);
                }
            }
        }

        @Override
        public void onFinished(String result) {
            //Log.d(TAG, result);
            recyclerViewLoadManager.setLoading(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }
}