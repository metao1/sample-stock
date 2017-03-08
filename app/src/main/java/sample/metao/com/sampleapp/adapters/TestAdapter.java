package sample.metao.com.sampleapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import sample.metao.com.sampleapp.R;
import sample.metao.com.sampleapp.models.Item;

import java.util.ArrayList;

/**
 * Created by metao on 3/7/2017.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.CustomViewHolder> {

    private static final String TAG = TestAdapter.class.getSimpleName();
    private final LayoutInflater inflater;
    private ArrayList<Item> items;

    public TestAdapter(Context context, ArrayList<Item> items) {
        inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Item item = this.items.get(position);
        if (item != null) {
            holder.bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setItem(Item item) {
        items.add(item);
        notifyItemChanged(getItemCount() - 1);
    }

    public void clearAll() {
        items.clear();
        notifyDataSetChanged();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private final TextView faceTextView, priceTextView;

        CustomViewHolder(View itemView) {
            super(itemView);
            faceTextView = (TextView) itemView.findViewById(R.id.face_text_view);
            priceTextView = (TextView) itemView.findViewById(R.id.price_text_view);
        }

        void bind(Item item) {
            if (faceTextView != null) {
                faceTextView.setText(item.getFace());
            }
            if (priceTextView != null) {
                priceTextView.setText(String.format("$%s", String.valueOf(item.getPrice())));
            }
        }
    }
}
