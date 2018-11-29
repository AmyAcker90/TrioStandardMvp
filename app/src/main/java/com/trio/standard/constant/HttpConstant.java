package com.trio.standard.constant;

import android.os.Environment;

/**
 * Created by lixiaoyan on 2017/11/20.
 */

public class HttpConstant {

    public final static String url = "http://192.168.1.135:8080/standard/";
//    public final static String url = "http://10.0.2.2:8080/standard/";

    public final static String app_file_dir = Environment.getExternalStorageDirectory() + "/TrioStandard/";
    public final static String app_file_download = app_file_dir + "download/";

    public final static String queryShowInfoList = "showInfo/queryList";
    public final static String queryCategoryList = "category/queryAll";
    public final static String uploadFileOne = "file/uploadOne";
    public final static String uploadFileBatch = "file/uploadBatch";
    public final static String downloadFile = "file/download";
    public final static String pushOne = "push/pushOne";
    public final static String searchMusic = "music/search";

    public final static String ret = "ret";
    public final static String msg = "msg";

    public static final String index = "index";
    public static final String pageSize = "pageSize";
    public static final String keyword = "keyword";
    public static final String category = "category";
    public static final String file = "file";
    public static final String clientId = "clientId";
    public static final String title = "title";
    public static final String content = "content";
    public static final String fileId = "fileId";

    public static final int success = 200;
    public static final int error_no_data = 20;
    public static final int error_unknown = -200;
    public static final int error_repetitive_operation = 500;
    public static final int error_no_internet = -199;

    public static final int default_page_size = 5;

    public static final int queryShowInfoListCode = 1001;
    public static final int queryCategoryListCode = 1002;
    public static final int uploadFileBatchCode = 2001;
    public static final int downloadFileCode = 2002;
    public static final int pushOneCode = 3001;
    public static final int searchMusicCode = 4001;
}