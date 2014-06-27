package yujia.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static SQLiteDatabase db;

	public DatabaseHelper(Context context, String databaseName) {
		super(context, databaseName, null, 1);
		db = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	public Cursor query(String tableName) {
		Cursor c = db.query(tableName, null, null, null, null, null, null);
		return c;
	}

	public void deleteRow(String tableName, String whereClause) {
		int rows = db.delete(tableName, whereClause, null);
		Logger.i("deleterow called table = " + tableName + " whereClause = "
				+ whereClause + " rows= " + rows);
	}

	public boolean isTabExist(String tabName) {
		Logger.i("isTabExist called");
		boolean result = false;
		if (tabName == null) {
			return false;
		}
		Cursor cursor = null;
		try {

			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
					+ tabName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
			cursor.close();

		} catch (Exception e) {
			e.printStackTrace();
			Logger.i("查询表是否存在时报错");
		}
		return result;
	}

	public void openWritableDb() {
		db = getWritableDatabase();
	}

	public void getReadableDb() {
		db = getReadableDatabase();
	}

	public boolean isRegionDataExist(String table, String region) {
		Cursor c = db.query(table, null, "Region = '" + region + "'", null,
				null, null, null);
		boolean b = false;
		if (c == null || c.getCount() == 0) {
			b = false;
			if (c != null)
				c.close();
		} else {
			b = true;
			c.close();
		}
		return b;
	}

	public void excuteSql(String sql) {
		Logger.i("执行sql语句 " + sql);
		db.execSQL(sql);
	}

	public long insertRow(String table, String nullColumnHack,
			ContentValues values) {
		Logger.i("执行insert row 语句 " + table + "  values = " + values.toString());
		return db.insert(table, nullColumnHack, values);

	}

	public Cursor rawQuery(String sql, String[] selectionArgs) {
		Logger.i("执行sql 查询 语句 " + sql);
		return getReadableDatabase().rawQuery(sql, selectionArgs);
	}

	public void update(String table, ContentValues values, String whereClause) {
		int rows = db.update(table, values, whereClause, null);
		Logger.i("执行sql update 语句 " + table + "  values = " + values.toString()
				+ " whereclause = " + whereClause + "  " + rows + " 被更新");

	}

	public String[] getColumnNames(String tableName) {
		String[] columnNames = null;
		Cursor c = null;

		try {
			c = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
			if (null != c) {
				int columnIndex = c.getColumnIndex("name");
				if (-1 == columnIndex) {
					return null;
				}

				int index = 0;
				columnNames = new String[c.getCount()];
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					columnNames[index] = c.getString(columnIndex);
					index++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c.close();
		}

		return columnNames;
	}

	public String getColomnString(String tableName) {
		String[] cols = getColumnNames(tableName);
		if (cols == null || cols.length == 0) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		for (String col : cols) {
			buffer.append(col).append(",");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		return buffer.toString();
	}

}
