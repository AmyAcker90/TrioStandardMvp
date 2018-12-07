package com.trio.standard.mvp.presenter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;

import com.trio.standard.R;
import com.trio.standard.context.AppContext;
import com.trio.standard.module.base.BasePresenter;
import com.trio.standard.mvp.contract.PushContract;
import com.trio.standard.mvp.model.PushModel;
import com.trio.standard.net.BaseObserver;
import com.trio.standard.net.RxTransformer;
import com.trio.standard.widgets.DatePickerBuilder;

/**
 * Created by lixia on 2018/11/28.
 */

public class PushPresenter extends BasePresenter<PushContract.PushView, PushContract.PushModel> {

    private Context mContext;

    public PushPresenter(Context context, PushContract.PushView view) {
        super(new PushModel(), view);
        mContext = context;
    }

    public void pushOne(String title, String content) {
        if (!TextUtils.isEmpty(AppContext.clientId)) {
            mModel.pushOne(AppContext.clientId, title, content)
                    .compose(RxTransformer.transformWithLoading(getView()))
                    .subscribe(new BaseObserver<String>() {
                        @Override
                        protected void onSuccessResponse(String result) {
                            getView().getOnePush();
                        }

                        @Override
                        protected void onErrorResponse(int ret, String msg) {
                            getView().onError(0, ret, msg);
                        }
                    });
        } else
            getView().onError(0, 0, mContext.getString(R.string
                    .error_getui_cid));
    }

    public DatePickerDialog showDataPicker(String pattern, String dateString, DatePickerBuilder.onDateSetListener
            listener) {
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
