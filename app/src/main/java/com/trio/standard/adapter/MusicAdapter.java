package com.trio.standard.adapter;

import android.content.Context;

import com.trio.standard.R;
import com.trio.standard.adapter.base.ItemViewDelegate;
import com.trio.standard.adapter.base.MultiItemTypeAdapter;
import com.trio.standard.adapter.base.IViewHolder;
import com.trio.standard.bean.MusicBean;

import java.util.List;

/**
 * Created by lixia on 2018/11/26.
 */

public class MusicAdapter extends MultiItemTypeAdapter<MusicBean> {

    public MusicAdapter(Context context, List<MusicBean> data) {
        super(context, data);
        addItemViewDelegate(new MusicDelegate());
    }

    class MusicDelegate implements ItemViewDelegate<MusicBean> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_music;
        }

        @Override
        public boolean isForViewType(MusicBean item, int position) {
            return true;
        }

        @Override
        public void convert(IViewHolder holder, MusicBean musicBean, int position) {
            holder.setText(R.id.tv_name, musicBean.getSinger() + " " + musicBean.getName());
            holder.setImageUrl(R.id.iv_img, musicBean.getPic());
            holder.setVisible(R.id.line, position != mData.size() - 1);
        }
    }
}
