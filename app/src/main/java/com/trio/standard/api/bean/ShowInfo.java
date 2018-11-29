package com.trio.standard.api.bean;

import lombok.Data;
import lombok.ToString;

/**
 * Created by lixia on 2018/9/14.
 */

@Data
@ToString
public class ShowInfo {

    private Long showId;

    private String name;

    private String icon;

    private String desc;

    private Integer category;

    private int viewType;

}
