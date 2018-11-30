package com.trio.standard.adapter;

import android.content.Context;

import com.trio.standard.R;
import com.trio.standard.adapter.base.ItemViewDelegate;
import com.trio.standard.adapter.base.MultiItemTypeAdapter;
import com.trio.standard.adapter.base.IViewHolder;
import com.trio.standard.api.bean.ShowInfo;

import java.util.List;

/**
 * Created by lixia on 2018/11/26.
 */

public class ShowAdapter extends MultiItemTypeAdapter<ShowInfo> {

    public ShowAdapter(Context context, List<ShowInfo> data) {
        super(context, data);
        addItemViewDelegate(new ShowDelegate());
    }

    class ShowDelegate implements ItemViewDelegate<ShowInfo> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_show;
        }

        @Override
        public boolean isForViewType(ShowInfo item, int position) {
            return true;
        }

        @Override
        public void convert(IViewHolder holder, ShowInfo showInfo, int position) {
            holder.setText(R.id.tv_name, showInfo.getName());
            holder.setText(R.id.tv_desc, showInfo.getDesc());
            holder.setImageUrl(R.id.iv_img, showInfo.getIcon());
            holder.setVisible(R.id.line, position != mData.size() - 1);
        }
    }
}
