package com.trio.standard.module.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

public abstract class BaseMvpFragment<P extends BasePresenter>
        extends BaseFragment implements BaseView {

    protected P mPresenter;

    protected abstract P createPresenter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.attach(this);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
                if (!TextUtils.isEmpty(msg))
                    showToast(msg);
                else
                    showToast(getString(R.string.error_unknow));
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


}
