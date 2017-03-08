package sample.metao.com.sampleapp.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by metao on 3/8/2017.
 */
public class RecyclerViewLoadManager extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager layoutManager;
    private final OnLoadMoreListener onLoadMoreListener;
    private int visibleThreshold = 4;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    public RecyclerViewLoadManager(LinearLayoutManager layoutManager, OnLoadMoreListener onLoadMoreListener) {
        this.layoutManager = layoutManager;
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        totalItemCount = layoutManager.getItemCount();
        lastVisibleItem = layoutManager
                .findLastVisibleItemPosition();
        if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            if (onLoadMoreListener != null) {
                onLoadMoreListener.onLoadMore();
            }
            loading = true;
        }
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
