package com.muzi.contentprovidertest;

import android.database.ContentObserver;
import android.os.Handler;

/**
 * 作者: lipeng
 * 时间: 2018/6/6
 * 邮箱: lipeng@moyi365.com
 * 功能:
 */
public class EkwContentObserver extends ContentObserver {
    public static final int DATA_CHANGED = 1;

    private Handler handler;


    public EkwContentObserver(Handler handler) {
        super(handler);
        this.handler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        handler.sendEmptyMessage(DATA_CHANGED);
    }
}
