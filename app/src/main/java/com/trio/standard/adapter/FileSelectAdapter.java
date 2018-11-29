package com.trio.standard.adapter;

import android.content.Context;

import com.trio.standard.R;
import com.trio.standard.adapter.base.IViewHolder;
import com.trio.standard.adapter.base.ItemViewDelegate;
import com.trio.standard.adapter.base.MultiItemTypeAdapter;

import java.util.List;

public class FileSelectAdapter extends MultiItemTypeAdapter<String> {

    public FileSelectAdapter(Context context, List<String> data) {
        super(context, data);
        addItemViewDelegate(new FileSelectDelegate());
    }

    class FileSelectDelegate implements ItemViewDelegate<String> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_file_select;
        }

        @Override
        public boolean isForViewType(String item, int position) {
            return true;
        }

        @Override
        public void convert(IViewHolder holder, String str, int position) {
            holder.setText(R.id.tv_path, str);
        }
    }

}
