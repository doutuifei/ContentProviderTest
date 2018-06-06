package com.muzi.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 * 作者: lipeng
 * 时间: 2018/5/30
 * 邮箱: lipeng@moyi365.com
 * 功能: 内容提供者
 */
public class EkwContentProvider extends ContentProvider {
    private static final int CODE_LOGIN = 1;
    public static final String AUTHORITY = "com.ekwing.wisdom.teacher.EkwContentProvider";  //授权
    public static final String PATH = "login";
    public static Uri uri = Uri.parse("content://" + AUTHORITY + "/" + PATH);

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        //关联不同的 URI 和 code，便于后续 getType
        mUriMatcher.addURI(AUTHORITY, "login", CODE_LOGIN);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case CODE_LOGIN:
                SharedPreferences sp = getContext().getSharedPreferences("sp_ek_wisdom_user_data", Context.MODE_PRIVATE);
                cursor = queryLogin(sp);
                break;
        }
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        int index = 0;
        switch (match) {
            case CODE_LOGIN:
                SharedPreferences sp = getContext().getSharedPreferences("sp_ek_wisdom_user_data", Context.MODE_PRIVATE);
                delete(sp, selectionArgs);
                break;
        }
        return index;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        int index = 0;
        switch (match) {
            case CODE_LOGIN:
                SharedPreferences sp = getContext().getSharedPreferences("sp_ek_wisdom_user_data", Context.MODE_PRIVATE);
                index = updateAndInsert(sp, values);
                break;
        }
        return index;
    }


    /**
     * 查询查询登录相关数据
     * 数据格式如下
     * _____________________________________
     * |  sp_is_logined  |  sp_login_type  |
     * -------------------------------------
     * |       true      |         1       |
     * -------------------------------------
     *
     * @param sp
     * @return
     */
    private Cursor queryLogin(SharedPreferences sp) {
        if (sp == null) {
            return null;
        }
        //列名称
        final String[] COLUMN_NAME_LOGIN = {
                Constant.SP_KEY_IS_LOGINED,
                Constant.SP_LOGIN_TYPE,
                Constant.SP_USER_UID,
                Constant.SP_USER_TOKEN
        };
        MatrixCursor cursor = new MatrixCursor(COLUMN_NAME_LOGIN);

        boolean isLogined = sp.getBoolean(Constant.SP_KEY_IS_LOGINED, false);//是否登录
        int pageType = sp.getInt(Constant.SP_LOGIN_TYPE, -1);
        String userUid = sp.getString(Constant.SP_USER_UID, "");
        String userToken = sp.getString(Constant.SP_USER_TOKEN, "");

        cursor.addRow(new Object[]{isLogined, pageType, userUid, userToken});
        return cursor;
    }


    /**
     * 更新数据
     * 如果没有对应的数据就添加一条
     *
     * @param sp
     * @param values
     * @return
     */
    private int updateAndInsert(SharedPreferences sp, ContentValues values) {
        if (sp == null || values == null || values.size() < 1) {
            return 0;
        }
        Set<String> keySet = values.keySet();
        if (keySet.size() < 1) {
            return 0;
        }
        int index = 0;
        SharedPreferences.Editor editor = sp.edit();
        for (String key : keySet) {
            Object o = values.get(key);
            if (o instanceof String) {
                String s = (String) o;
                editor.putString(key, s);
                index++;
            } else if (o instanceof Boolean) {
                boolean b = (boolean) o;
                editor.putBoolean(key, b);
                index++;
            } else if (o instanceof Integer) {
                int i = (int) o;
                editor.putInt(key, i);
                index++;
            } else if (o instanceof Float) {
                float f = (float) o;
                editor.putFloat(key, f);
                index++;
            } else if (o instanceof Long) {
                long l = (long) o;
                editor.putLong(key, l);
                index++;
            }
        }
        editor.apply();
        return index;
    }

    /**
     * 删除数据
     *
     * @param sp
     * @param selectionArgs
     * @return
     */
    private void delete(SharedPreferences sp, String[] selectionArgs) {
        if (sp == null || selectionArgs == null || selectionArgs.length < 1) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        for (String key : selectionArgs) {
            editor.remove(key);
        }
        editor.apply();
    }

    /**
     * 通知ContentResolver数据刷新
     *
     * @param context
     */
    public static void notifyChange(Context context) {
        context.getContentResolver().notifyChange(uri, null);
    }

}