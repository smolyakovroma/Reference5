package ru.smolyakovroma.reference5.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

import ru.smolyakovroma.reference5.Utils;
import ru.smolyakovroma.reference5.model.SprElement;


public class DbAdapter {

    public static final String DB_NAME = "reference.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_REFERENCE = "reference";
    public static final String REFERENCE_ID = "_id";
    public static final String REFERENCE_NAME = "name";
    public static final String REFERENCE_CODE = "code";
    public static final String REFERENCE_FOLDER = "folder";
    public static final String REFERENCE_REMOVE = "remove";
    public static final String REFERENCE_PARENT_ID = "parent_id";
    public static final String REFERENCE_UID = "uid";

    private static final String TABLE_OPERATION = "operation";

    private DbHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;


    private static final String TABLE_REFERENCE_CREATE_SCRIPT = "CREATE TABLE "
            + TABLE_REFERENCE + " (" + BaseColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFERENCE_NAME + " TEXT NOT NULL, "
            + REFERENCE_CODE + " TEXT NOT NULL, " + REFERENCE_FOLDER + " INTEGER, "
            + REFERENCE_REMOVE + " INTEGER, "
            + REFERENCE_UID + " INTEGER, "
            + REFERENCE_PARENT_ID + " INTEGER CONSTRAINT fk_parent_id REFERENCES [reference]([_id]) ON DELETE SET NULL ON UPDATE SET NULL DEFAULT null);";

    public DbAdapter(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
        db = dbHelper.getReadableDatabase();
    }


    public Cursor getElementsReferenceCursor(String selection, String[] selectionArgs, String orderBy) {

        Cursor c = db.query(TABLE_REFERENCE, null, selection, selectionArgs, null, null, orderBy);

        return c;
    }


    public Cursor getAllElements() {
        String sql = "select "
                + "o._id as " + REFERENCE_ID
                + ",o.code as " + REFERENCE_CODE
                + ",o.name as " + REFERENCE_NAME
                + ",o.folder as " + REFERENCE_FOLDER
                + ",o.remove as " + REFERENCE_REMOVE
                + " from " + TABLE_REFERENCE + " o order by " + REFERENCE_FOLDER + " DESC";

        return db.rawQuery(sql, null);
    }

    // показать пункты справочника
//    public ArrayList<SprElement> getSprElements(SprElement sprElement) {
    public int getParentFolder(int current_folder_id) {
        Cursor c = null;
        StringBuilder builder = new StringBuilder();

        builder.append("select "
                + "o.parent_id as " + REFERENCE_PARENT_ID
                + " from " + TABLE_REFERENCE + " o where coalesce(o._id,0) = " + current_folder_id);

        c = db.rawQuery(builder.toString(), null);

        int result = 0;

        while (c.moveToNext()) {

            result = c.getInt(c.getColumnIndex(DbAdapter.REFERENCE_PARENT_ID));
        }

        c.close();
        return result;

    }

    public SprElement getSprElementById(int id) {
        Cursor c = null;
        StringBuilder builder = new StringBuilder();

        builder.append("select "
                + "o._id as " + REFERENCE_ID
                + ",o.code as " + REFERENCE_CODE
                + ",o.name as " + REFERENCE_NAME
                + ",o.folder as " + REFERENCE_FOLDER
                + ",o.remove as " + REFERENCE_REMOVE
                + ",o.parent_id as " + REFERENCE_PARENT_ID
                + ",o.uid as " + REFERENCE_UID
                + " from " + TABLE_REFERENCE + " o");


            builder.append(" where coalesce(o._id,0) = " + id);// все дочерние объекты для выбранного

        builder.append(" order by " + REFERENCE_FOLDER + " DESC");


        c = db.rawQuery(builder.toString(), null);
        SprElement sprValue = new SprElement();

        while (c.moveToNext()) {
            sprValue.setId(c.getInt(c.getColumnIndex(DbAdapter.REFERENCE_ID)));
            sprValue.setName(c.getString(c.getColumnIndex(DbAdapter.REFERENCE_NAME)));
            sprValue.setCode(c.getString(c.getColumnIndex(DbAdapter.REFERENCE_CODE)));
            sprValue.setFolder(Utils.convertIntToBool(c.getInt(c.getColumnIndex(DbAdapter.REFERENCE_FOLDER))));
            sprValue.setRemove(Utils.convertIntToBool(c.getInt(c.getColumnIndex(DbAdapter.REFERENCE_REMOVE))));
            sprValue.setParent_id(c.getInt(c.getColumnIndex(DbAdapter.REFERENCE_PARENT_ID)));
            sprValue.setTopFolder(true);
        }
        c.close();

        return sprValue;

    }

    public ArrayList<SprElement> getSprElements(int current_folder_id) {
        Cursor c = null;
        StringBuilder builder = new StringBuilder();

        ArrayList<SprElement> sprList = new ArrayList<SprElement>();

        builder.append("select "
                + "o._id as " + REFERENCE_ID
                + ",o.code as " + REFERENCE_CODE
                + ",o.name as " + REFERENCE_NAME
                + ",o.folder as " + REFERENCE_FOLDER
                + ",o.remove as " + REFERENCE_REMOVE
                + ",o.parent_id as " + REFERENCE_PARENT_ID
                + ",o.uid as " + REFERENCE_UID
                + " from " + TABLE_REFERENCE + " o");

        if (current_folder_id == 0) {
            builder.append(" where coalesce(o.parent_id,0) = 0");// все корневые элементы
        } else {
            sprList.add(getSprElementById(current_folder_id));
            builder.append(" where coalesce(o.parent_id,0) = " + current_folder_id);// все дочерние объекты для выбранного
        }
        builder.append(" order by " + REFERENCE_FOLDER + " DESC");


        c = db.rawQuery(builder.toString(), null);



        while (c.moveToNext()) {
            SprElement sprValue = new SprElement();
            sprValue.setId(c.getInt(c.getColumnIndex(DbAdapter.REFERENCE_ID)));
            sprValue.setName(c.getString(c.getColumnIndex(DbAdapter.REFERENCE_NAME)));
            sprValue.setCode(c.getString(c.getColumnIndex(DbAdapter.REFERENCE_CODE)));
            sprValue.setFolder(Utils.convertIntToBool(c.getInt(c.getColumnIndex(DbAdapter.REFERENCE_FOLDER))));
            sprValue.setRemove(Utils.convertIntToBool(c.getInt(c.getColumnIndex(DbAdapter.REFERENCE_REMOVE))));
            sprValue.setParent_id(c.getInt(c.getColumnIndex(DbAdapter.REFERENCE_PARENT_ID)));
            sprValue.setTopFolder(false);
            sprList.add(sprValue);
        }
        c.close();

        return sprList;

    }

    public boolean addNewElement(SprElement sprElement){
//        SQLiteStatement stmt = null;

        ContentValues values = new ContentValues();
        values.put(REFERENCE_CODE, sprElement.getCode());
        values.put(REFERENCE_NAME, sprElement.getName());
        values.put(REFERENCE_FOLDER, sprElement.isFolder());

        dbHelper.getWritableDatabase().insert(TABLE_REFERENCE, null, values);
        return true;

//        try {
//
//            stmt = dbHelper.getWritableDatabase().compileStatement("update " + TABLE_REFERENCE + " set code=?, name=?, parent_id = null where _id=?");
//            stmt.bindString(1, sprElement.getCode());
//            stmt.bindString(2, sprElement.getName());
//            stmt.bindLong(3, 11);
//
//
//            stmt.executeInsert();
//
//            return true;
//        } catch (Exception e) {
//            Log.e("ErrorDB", e.getMessage());
//        } finally {
//            if (stmt != null) {
//                stmt.close();
//            }
//        }
//
//        return false;
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_REFERENCE_CREATE_SCRIPT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE " + TABLE_REFERENCE);
            onCreate(db);
        }

    }
}
