package com.trio.standard.module.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.blankj.utilcode.util.LogUtils;
import com.trio.standard.R;
import com.trio.standard.constant.HttpConstant;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by lixia on 2018/11/30.
 */

public abstract class BaseMvpFragment<V extends BaseView, P extends BasePresenter<V>>
        extends Fragment implements BaseView {

    protected P mPresenter;
    protected Context mContext;
    //缓存Fragment view
    private View mRootView;

    protected abstract P createPresenter();

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    @LayoutRes
    protected abstract int attachLayoutRes();

    /**
     * 初始化视图控件
     */
    protected abstract void init();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mPresenter = createPresenter();
        LogUtils.i("onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(attachLayoutRes(), container, false);
            ButterKnife.bind(this, mRootView);
            init();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i("onResume");
        if (mPresenter != null) {
            mPresenter.attach((V) this);
        }
    }

    @Override
    public void showLoading(String msg, boolean cancelable) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(int errorCode, String msg) {
        switch (errorCode) {
            case HttpConstant.error_no_data:
                break;
            case HttpConstant.error_repetitive_operation:
                showToast(mContext.getString(R.string.toast_please_wait));
                break;
            case HttpConstant.error_no_internet:
                showToast(mContext.getString(R.string.error_no_internet));
                break;
            default:
                showToast(msg);
                break;
        }
    }

    public void showToast(String msg) {
        getActivity().runOnUiThread(() -> Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {

    }

    @Override
    public void onBottom(boolean isBottom) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.i("onDestroyView");
        ButterKnife.unbind(this);
    }

    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
        return ts;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.i("onDetach");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.i("onPause");
    }
}
