package com.trio.standard.module.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by lixia on 2018/11/30.
 */
public abstract class BaseMvpActivity< P extends BasePresenter>
        extends BaseActivity  {

    protected P mPresenter;

    protected abstract P createPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public void showLoading(String msg, boolean cancelable) {
//
//    }
//
//    @Override
//    public void hideLoading() {
//
//    }
//
//    @Override
//    public void onError(int requestCode, int errorCode, String msg) {
//        switch (errorCode) {
//            case HttpConstant.error_no_data:
//                break;
//            case HttpConstant.error_repetitive_operation:
//                showToast(mContext.getString(R.string.toast_please_wait));
//                break;
//            case HttpConstant.error_no_internet:
//                showToast(mContext.getString(R.string.error_no_internet));
//                break;
//            default:
//                showToast(msg);
//                break;
//        }
//    }


//    @Override
//    public void setRefreshing(boolean isRefreshing) {
//
//    }
//
//    @Override
//    public void onBottom(boolean isBottom) {
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }


}
