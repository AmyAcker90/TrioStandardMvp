package com.trio.standard.module.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxFragment;
import com.trio.standard.R;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.widgets.LoadingDialog;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by lixia on 2018/11/26.
 */

public abstract class BaseFragment<T extends BasePresenter> extends RxFragment implements BaseView {

    protected T mPresenter;
    protected Context mContext;
    //缓存Fragment view
    private View mRootView;
    private LoadingDialog mLoadingDialog;

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

    protected void queryData(boolean isRefresh) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
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
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    @Override
    public void onSuccess(int requestCode, Object data) {

    }

    @Override
    public void onError(int requestCode, int errorCode, String msg) {
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
    public void showLoading(String msg, boolean cancelable) {
        hideLoading();
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getActivity(), msg, true);
        }
        mLoadingDialog.setMessage(msg);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        getActivity().runOnUiThread(() -> {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {

    }

    @Override
    public void onBottom(boolean isBottom) {

    }

    @Override
    public void onProgress(int requestCode, int progress) {

    }

    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
        return ts;
    }
}
