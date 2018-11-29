package com.trio.standard.context;

import android.app.Application;
import android.os.Environment;

import com.blankj.utilcode.util.Utils;
import com.trio.standard.R;
import com.trio.standard.utils.ImageUtil;

import java.io.File;

/**
 * Created by lixia on 2018/9/13.
 */

public class AppContext extends Application {

    //个推clientid
    public static String clientId;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        //Matisse框架拍照的图片的保存路径,目前其他无效
        File f = new File(Environment.getExternalStorageDirectory() + "/Pictures/");
        if (!f.exists()) {
            f.mkdirs();
        }
        ImageUtil.getInstance(getApplicationContext()).setPlaceholder(R.mipmap.ic_holder);
        ImageUtil.getInstance(getApplicationContext()).setErrorholder(R.mipmap.ic_holder);
    }
}
