package com.trio.standard.module.show;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.trio.standard.R;
import com.trio.standard.adapter.CategoryAdapter;
import com.trio.standard.adapter.ShowAdapter;
import com.trio.standard.bean.Category;
import com.trio.standard.bean.ShowInfo;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BaseMvpFragment;
import com.trio.standard.mvp.contract.ShowContract;
import com.trio.standard.mvp.presenter.ShowListPresenter;
import com.trio.standard.utils.CacheUtil;
import com.trio.standard.widgets.CustomToolBar;
import com.trio.standard.widgets.DelEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lixia on 2018/11/26.
 */

public class ShowListFragment extends BaseMvpFragment<ShowListPresenter>
        implements ShowContract.ShowListView {

    @Bind(R.id.customToolBar)
    CustomToolBar mCustomToolBar;
    @Bind(R.id.et_keyword)
    DelEditText mEtKeyword;
    @Bind(R.id.tv_category)
    TextView mTvCategory;
    @Bind(R.id.vs_empty)
    ViewStub mVsEmpty;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private LinearLayoutManager mLayoutManager;
    private ShowAdapter mAdapter;
    private List<ShowInfo> mData = new ArrayList<>();
    private boolean isBottom = false;
    private Long index = 0L;
    private String keyword;

    private CategoryAdapter mCategoryAdapter;
    private List<Category> mCategories;
    private ListPopupWindow mCategoryPop;
    private Long category;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_show_list;
    }

    @Override
    protected void init() {
        mAdapter = new ShowAdapter(mContext, mData);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);

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
        mSwipeRefresh.setOnRefreshListener(() -> queryData(true));
        mEtKeyword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                keyword = mEtKeyword.getText().toString();
                queryData(true);
                KeyboardUtils.hideSoftInput(getActivity());
            }
            return false;
        });

        mPresenter.queryCategotyAll();
        //获取缓存
        String cache = CacheUtil.getInstance(mContext).getString(HttpConstant.queryShowInfoList);
        if (!TextUtils.isEmpty(cache))
            updateShows(jsonToList(cache, ShowInfo.class));
        else
            queryData(true);
    }

    @Override
    protected ShowListPresenter createPresenter() {
        return new ShowListPresenter(mContext, this);
    }

    void queryData(boolean isRefresh) {
        if (isRefresh)
            index = 0L;
        mPresenter.queryShowInfoList(keyword, category, index);
    }

    @Override
    public void updateShows(List<ShowInfo> data) {
        if (index == 0)
            mData.clear();
        if (data != null && data.size() > 0) {
            mData.addAll(data);
            mVsEmpty.setVisibility(View.GONE);
            mRecyclerview.setVisibility(View.VISIBLE);
            index = data.get(data.size() - 1).getShowId();
        } else {
            mVsEmpty.setVisibility(View.VISIBLE);
            mRecyclerview.setVisibility(View.GONE);
        }
        mAdapter.setData(mData);
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        mSwipeRefresh.setRefreshing(isRefreshing);
    }

    public void updateCategorys(List<Category> data) {
        mCategories = data;
    }

    @Override
    public void onBottom(boolean isBottom) {
        super.onBottom(isBottom);
        this.isBottom = isBottom;
    }

    @Override
    public void onError(int requestCode, int errorCode, String msg) {
        super.onError(requestCode, errorCode, msg);
        if (requestCode == HttpConstant.queryShowInfoListCode) {
            if (index == 0)
                updateShows(null);
        }
    }

    @OnClick({R.id.tv_category})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_category:
                if (mCategoryPop == null) {
                    mCategoryPop = new ListPopupWindow(mContext);
                    mCategoryAdapter = new CategoryAdapter(mContext, mCategories);
                    mCategoryPop.setAdapter(mCategoryAdapter);
                    mCategoryPop.setWidth(v.getWidth());
                    mCategoryPop.setAnchorView(v);
                    mCategoryPop.setDropDownGravity(Gravity.CENTER_HORIZONTAL);
                    mCategoryPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    mCategoryPop.setModal(false);
                    mCategoryPop.setOnItemClickListener((parent, view, position, id) -> {
                        mTvCategory.setText(mCategories.get(position).getName());
                        category = mCategories.get(position).getCategoryId();
                        mCategoryPop.dismiss();
                        queryData(true);
                    });
                }
                if (mCategories != null && mCategories.size() > 0) {
                    mCategoryAdapter.setData(mCategories);
                    mCategoryPop.show();
                } else {
                    mPresenter.queryCategotyAll();
                }
                break;
        }
    }

}
