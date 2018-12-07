package com.trio.standard.module.music;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;

import com.blankj.utilcode.util.KeyboardUtils;
import com.trio.standard.R;
import com.trio.standard.adapter.MusicAdapter;
import com.trio.standard.bean.MusicBean;
import com.trio.standard.module.base.BaseMvpFragment;
import com.trio.standard.mvp.contract.MusicContract;
import com.trio.standard.mvp.presenter.MusicPresenter;
import com.trio.standard.widgets.CustomToolBar;
import com.trio.standard.widgets.DelEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by lixia on 2018/11/30.
 */

public class MusicFragment extends BaseMvpFragment<MusicPresenter>
        implements MusicContract.MusicView {

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
    protected MusicPresenter createPresenter() {
        return new MusicPresenter(this);
    }

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
    public void setRefreshing(boolean isRefreshing) {
        mSwipeRefresh.setRefreshing(isRefreshing);
    }

    private void queryData(boolean isRefresh) {
        if (isRefresh)
            index = 0;
        mPresenter.searchMusic(keyword, index);
    }

    @Override
    public void updateMusic(List<MusicBean> data) {
        if (index == 0)
            mData.clear();
        mData.addAll(data);
        if (data != null && data.size() > 0) {
            mVsEmpty.setVisibility(View.GONE);
            mRecyclerview.setVisibility(View.VISIBLE);
            index++;
        } else {
            mVsEmpty.setVisibility(View.VISIBLE);
            mRecyclerview.setVisibility(View.GONE);
        }
        mAdapter.setData(mData);
    }

    @Override
    public void onBottom(boolean isBottom) {
        super.onBottom(isBottom);
        this.isBottom = isBottom;
    }
}
