package com.muzi.contentprovidertest;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Uri uri = Uri.parse("content://com.ekwing.wisdom.teacher.EkwContentProvider/login");

    private Button btnQuery, btnUpdateAndInsert, btnDelete;
    private android.widget.TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.text = (TextView) findViewById(R.id.text);
        btnQuery = findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(null);
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(uri, null, null, null, null);
                String[] columnNames = cursor.getColumnNames();
                cursor.moveToFirst();
                for (String columnName : columnNames) {
                    int columnIndex = cursor.getColumnIndex(columnName);
                    String value = cursor.getString(columnIndex);
                    Log.d("MainActivity", columnName + ":" + value);
                    text.append(columnName + ":" + value);
                    text.append("\n");
                }
                cursor.close();
            }
        });

        btnUpdateAndInsert = findViewById(R.id.btnUpdateAndInsert);
        btnUpdateAndInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(null);
                ContentResolver contentResolver = getContentResolver();
                ContentValues values = new ContentValues();
                values.put("sp_is_logined", true);//更改登录状态
                int index = contentResolver.update(uri, values, null, null);
                Log.d("MainActivity", "操作记录:" + index);
                text.append("操作记录:" + index);
            }
        });

        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver contentResolver = getContentResolver();
                String[] values = new String[]{"sp_is_logined"};//删除登录状态
                contentResolver.delete(uri, null, values);
            }
        });

    }

}
