## ContentProvide

### 注意
数据改变时需要调用``` EkwContentProvider.notifyChange(context);```通知数据刷新。

### 1.配置权限
```
    <!--provider读写权限-->
    <uses-permission android:name="com.ekwing.wisdom.permission.READ_CONTENT" />
    <uses-permission android:name="com.ekwing.wisdom.permission.WRITE_CONTENT" />
```

### 2.Uri：获取登录信息
```
    Uri uri = Uri.parse("content://com.ekwing.wisdom.teacher.EkwContentProvider/login");
```

### 3.方法

#### 1.查询
```
     ContentResolver contentResolver = getContentResolver();
     Cursor cursor = contentResolver.query(uri, null, null, null, null);
     String[] columnNames = cursor.getColumnNames();
     cursor.moveToFirst();
     for (String columnName : columnNames) {
         int columnIndex = cursor.getColumnIndex(columnName);
         String value = cursor.getString(columnIndex);
         Log.d("MainActivity", columnName + ":" + value);
     }
     cursor.close();
```

#### 2.更新或者添加：如果没有相应的数据就插入一条
```
     ContentResolver contentResolver = getContentResolver();
     ContentValues values = new ContentValues();
     values.put("sp_is_logined", true);//更改登录状态
     int index = contentResolver.update(uri, values, null, null);
     Log.d("MainActivity", "操作记录:" + index);
```

#### 3.删除数据
```
    ContentResolver contentResolver = getContentResolver();
    String[] values = new String[]{"sp_is_logined"};//删除登录状态
    contentResolver.delete(uri, null, values);
```

### 4.监听数据刷新

#### 1.设置监听
```
    registerContentObserver(Uri uri, boolean notifyForDescendants,ContentObserver observer)
```

#### 2.取消监听
```
    unregisterContentObserver(ContentObserver observer);
```