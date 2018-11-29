package com.trio.standard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.trio.standard.R;
import com.trio.standard.api.bean.Category;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lixia on 2018/11/27.
 */

public class CategoryAdapter extends BaseAdapter {

    private List<Category> mData;
    private Context mContext;

    public CategoryAdapter(Context context, List<Category> data) {
        mData = data;
        mContext = context;
    }

    public void setData(List<Category> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_pop, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTvCategory.setText(mData.get(position).getName());
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.tv_category_name)
        TextView mTvCategory;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}