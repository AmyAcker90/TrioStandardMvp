package com.trio.standard.module.base;

import lombok.Data;
import lombok.ToString;

/**
 * Created by lixia on 2018/11/22.
 */

@Data
public class BaseResp<T> {

    public int ret;
    public String msg;
    public T data;

}
