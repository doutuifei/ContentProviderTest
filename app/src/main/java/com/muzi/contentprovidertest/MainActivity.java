package com.muzi.contentprovidertest;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Uri uri = Uri.parse("content://com.ekwing.wisdom.teacher.EkwContentProvider/login");

    private TextView text;
    private ContentResolver contentResolver;
    private EkwContentObserver observer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == EkwContentObserver.DATA_CHANGED) {
                query();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentResolver = getContentResolver();
        observer = new EkwContentObserver(handler);
        contentResolver.registerContentObserver(uri, true, observer);

        text = findViewById(R.id.text);

        findViewById(R.id.btnQuery).setOnClickListener(this);

        findViewById(R.id.btnUpdateAndInsert).setOnClickListener(this);

        findViewById(R.id.btnDelete).setOnClickListener(this);

        findViewById(R.id.btnClean).setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (contentResolver != null && observer != null) {
            contentResolver.unregisterContentObserver(observer);
        }
    }

    private void query() {
        text.setText(null);
        contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        String[] columnNames = cursor.getColumnNames();
        cursor.moveToFirst();
        for (String columnName : columnNames) {
            int columnIndex = cursor.getColumnIndex(columnName);
            String value = cursor.getString(columnIndex);
            text.append(columnName + ":" + value);
            text.append("\n");
        }
        cursor.close();
    }

    private void updateAndInsert() {
        text.setText(null);
        contentResolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("sp_is_logined", true);//更改登录状态
        int index = contentResolver.update(uri, values, null, null);
        text.append("操作记录:" + index);
    }

    private void delete() {
        contentResolver = getContentResolver();
        String[] values = new String[]{"sp_is_logined"};//删除登录状态
        contentResolver.delete(uri, null, values);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnQuery:
                query();
                break;
            case R.id.btnUpdateAndInsert:
                updateAndInsert();
                break;
            case R.id.btnDelete:
                delete();
                break;
            case R.id.btnClean:
                text.setText(null);
                break;
        }
    }
}
