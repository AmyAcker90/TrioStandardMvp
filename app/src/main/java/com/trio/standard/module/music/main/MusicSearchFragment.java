package com.trio.standard.module.music.main;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.trio.standard.R;
import com.trio.standard.adapter.MusicAdapter;
import com.trio.standard.adapter.base.MultiItemTypeAdapter;
import com.trio.standard.api.bean.MusicBean;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BaseFragment;
import com.trio.standard.widgets.CustomToolBar;
import com.trio.standard.widgets.DelEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by lixia on 2018/11/26.
 */

public class MusicSearchFragment extends BaseFragment<MusicSearchPresenter> implements MusicSearchView {

    @Bind(R.id.customToolBar)
    CustomToolBar mCustomToolBar;
    @Bind(R.id.et_keyword)
    DelEditText mEtKeyword;
    @Bind(R.id.vs_empty)
    ViewStub mVsEmpty;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private MusicAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<MusicBean> mData = new ArrayList<>();
    private boolean isQuery = false;
    private boolean isBottom = false;
    private String keyword;
    private int index = 0;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_music_search;
    }

    @Override
    protected void init() {
        mAdapter = new MusicAdapter(mContext, mData);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);
        mPresenter = new MusicSearchPresenter(this);
        mEtKeyword.setOnEditorActionListener((v, actionId, event) -> {
            keyword = mEtKeyword.getText().toString();
            if (actionId == EditorInfo.IME_ACTION_SEARCH && !TextUtils.isEmpty(keyword)) {
                queryData(true);
                KeyboardUtils.hideSoftInput(getActivity());
            }
            return false;
        });

        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!mSwipeRefresh.isRefreshing()
                        && newState == RecyclerView.SCROLL_STATE_IDLE && mLayoutManager != null
                        && mLayoutManager.findLastVisibleItemPosition() + 1 == mLayoutManager.getItemCount() &&
                        !isBottom) {
                    queryData(false);
                }
            }
        });

        mSwipeRefresh.setOnRefreshListener(() -> {
            isBottom = false;
            queryData(true);
        });
    }

    @Override
    protected void queryData(boolean isRefresh) {
        if (isRefresh)
            index = 0;
        mPresenter.loadDataByKeyword(keyword, index);
    }

    @Override
    public void showData(List<MusicBean> data) {
        mSwipeRefresh.setRefreshing(false);
        isQuery = false;
        if (data.size() < HttpConstant.default_page_size)
            isBottom = true;
        else
            isBottom = false;
        mAdapter.setData(data);
    }

}
