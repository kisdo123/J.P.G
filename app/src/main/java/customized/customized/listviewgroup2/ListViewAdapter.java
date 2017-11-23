package customized.customized.listviewgroup2;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import capstone.android.R;
import customized.customized.listviewgroup2.groupdata.CheckableLinearLayout;
import customized.customized.listviewgroup2.groupdata.ListViewItem;

/**
 * Created by User on 2017-11-13.
 */

public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext = null;
    private ArrayList<ListViewItem> listviewDatas = null;
    private int size = 0;

    public ListViewAdapter(Context context,int currentSize,int maxSize){
        this.mContext = context;
        this.size = maxSize-currentSize;
        this.listviewDatas = new ArrayList<ListViewItem>();
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.listviewDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return this.listviewDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHodler viewHodler = new ViewHodler();
        if(view == null) {
            view = mInflater.inflate(R.layout.item_cgraldraw, viewGroup, false);
        }
        ImageView imageView = viewHodler.get(view,R.id.cus_gal_img);
        TextView textView = viewHodler.get(view,R.id.cus_gal_text);

        ListViewItem listViewItem = listviewDatas.get(i);

        imageView.setImageBitmap(listViewItem.getBitmap());
        textView.setText(listViewItem.getString());
        return view;
    }

    public void addItem(Bitmap bitmap, String text){
        ListViewItem item = new ListViewItem();
        item.setBitmap(bitmap);
        item.setString(text);
        this.listviewDatas.add(item);
    }

    public class ViewHodler{
        @SuppressWarnings("unchecked")
        public <T extends View> T get(View view, int id){
            SparseArray<View> viewHolder = (SparseArray<View>)view.getTag();
            if(viewHolder == null){
                viewHolder = new SparseArray<View>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if(childView == null){
                childView = view.findViewById(id);
                viewHolder.put(id,childView);
            }
            return (T) childView;
        }
    }
}
