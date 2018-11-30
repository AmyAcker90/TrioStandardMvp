package com.trio.standard.module.date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;

import com.trio.standard.R;
import com.trio.standard.api.PushApi;
import com.trio.standard.context.AppContext;
import com.trio.standard.module.base.BasePresenter;
import com.trio.standard.module.base.BaseView;
import com.trio.standard.net.BaseObserver;
import com.trio.standard.net.RetrofitFactory;
import com.trio.standard.widgets.DatePickerBuilder;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lixia on 2018/11/28.
 */

class DatePresenter extends BasePresenter<BaseView> {

    private BaseView mView;
    private Context mContext;

    DatePresenter(Context context, BaseView view) {
        mContext = context;
        mView = view;
    }

    void pushOne(String title, String content) {
        if (!TextUtils.isEmpty(AppContext.clientId)) {
            RetrofitFactory.create(PushApi.class)
                    .pushOne(AppContext.clientId, title, content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<String>() {
                        @Override
                        protected void onSuccessResponse(String result) {
                        }

                        @Override
                        protected void onErrorResponse(int ret, String msg) {
                            mView.showError(ret, msg);
                        }
                    });
        } else
            mView.showError(0, mContext.getString(R.string
                    .error_getui_cid));
    }

    DatePickerDialog showDataPicker(String pattern, String dateString, DatePickerBuilder.onDateSetListener listener) {
        if (TextUtils.isEmpty(pattern))
            return new DatePickerBuilder(mContext)
                    .dateString(dateString)
                    .onDateSetListener(listener).build();
        return new DatePickerBuilder(mContext)
                .pattern(pattern)
                .dateString(dateString)
                .onDateSetListener(listener).build();
    }
}
