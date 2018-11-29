package com.trio.standard.adapter.base;

/**
 * Created by lixia on 2018/11/27.
 */

public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(IViewHolder holder, T t, int position);

}