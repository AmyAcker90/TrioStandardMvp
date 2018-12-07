package com.trio.standard.bean;

import lombok.Data;

/**
 * Created by lixia on 2018/11/26.
 */

@Data
public class MusicBean {

    /**
     * id : 471403427
     * name : 我喜欢上你时的内心活动
     * singer : 陈绮贞
     * pic : https://api.bzqll.com/music/netease/pic?id=471403427&key=579621905
     * lrc : https://api.bzqll.com/music/netease/lrc?id=471403427&key=579621905
     * url : https://api.bzqll.com/music/netease/url?id=471403427&key=579621905
     */

    private String id;

    private String name;

    private String singer;

    private String pic;

    private String lrc;

    private String url;
}
