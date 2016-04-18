package ru.smolyakovroma.reference5.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

import ru.smolyakovroma.reference5.R;
import ru.smolyakovroma.reference5.SprTypeSelect;
import ru.smolyakovroma.reference5.Utils;
import ru.smolyakovroma.reference5.enums.DocumentType;
import ru.smolyakovroma.reference5.model.DocElement;
import ru.smolyakovroma.reference5.model.SprElement;


public class DbAdapter {

    public static final String DB_NAME = "reference.db";
    public static final int DB_VERSION = 3;

    public static final String TABLE_REF_STOCKS = "ref_stocks";
    public static final String TABLE_REF_UNITS = "ref_units";
    public static final String TABLE_REF_WAREHOUSES = "ref_warehouess";
    public static final String TABLE_REF_ORGANIZATIONS = "ref_organizations";
    public static final String TABLE_DOC_DISPLACEMENT_STOCKS = "doc_displacement_stocks";
    public static final String TABLE_DOC_DISPLACEMENT_STOCKS_T = "doc_displacement_stocks_t";

    public static final String REFERENCE_ID = "_id";
    public static final String REFERENCE_NAME = "name";
    public static final String REFERENCE_CODE = "code";
    public static final String REFERENCE_FOLDER = "folder";
    public static final String REFERENCE_REMOVE = "remove";
    public static final String REFERENCE_PARENT_ID = "parent_id";
    public static final String REFERENCE_UID = "uid";

    public static final String DOCUMENT_DATETIME = "doc_datetime";
    public static final String DOCUMENT_STATUS = "status";
    public static final String DOCUMENT_ORGANIZATION_ID = "organization_id";
    public static final String DOCUMENT_WAREHOUSE_FROM_ID = "warehouse_from_id";
    public static final String DOCUMENT_WAREHOUSE_TO_ID = "warehouse_to_id";
    public static final String DOCUMENT_STOCK_ID = "stock_id";
    public static final String DOCUMENT_UNIT_ID = "unit_id";
    public static final String DOCUMENT_DOCUMENT_ID = "document_id";
    public static final String DOCUMENT_AMOUNT = "amount";
    public static final String DOCUMENT_PRICE = "price";

    private DbHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;


    private static final String TABLE_REFERENCE_STOCKS_CREATE_SCRIPT = "CREATE TABLE "
            + TABLE_REF_STOCKS + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + REFERENCE_NAME + " TEXT NOT NULL, "
            + REFERENCE_CODE + " TEXT NOT NULL, " + REFERENCE_FOLDER + " INTEGER, "
            + REFERENCE_REMOVE + " INTEGER, "
            + REFERENCE_UID + " INTEGER, "
            + REFERENCE_PARENT_ID + " INTEGER CONSTRAINT fk_parent_id REFERENCES [reference]([_id]) ON DELETE SET NULL ON UPDATE SET NULL DEFAULT null); ";

    private static final String TABLE_REFERENCE_UNITS_CREATE_SCRIPT = " CREATE TABLE " + TABLE_REF_UNITS + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + REFERENCE_NAME + " TEXT NOT NULL, "
            + REFERENCE_CODE + " TEXT NOT NULL); ";

    private static final String TABLE_REFERENCE_WAREHOUSE_SCRIPT = " CREATE TABLE " + TABLE_REF_WAREHOUSES + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + REFERENCE_NAME + " TEXT NOT NULL, "
            + REFERENCE_CODE + " TEXT NOT NULL); ";

    private static final String TABLE_REFERENCE_ORGANIZATIONS_CREATE_SCRIPT = " CREATE TABLE " + TABLE_REF_ORGANIZATIONS + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + REFERENCE_NAME + " TEXT NOT NULL, "
            + REFERENCE_CODE + " TEXT NOT NULL); ";


    private static final String TABLE_DOCUMENT_DISPLACEMENT_STOCKS_CREATE_SCRIPT = "CREATE TABLE "
            + TABLE_DOC_DISPLACEMENT_STOCKS + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DOCUMENT_DATETIME + " INTEGER NOT NULL ON CONFLICT FAIL,"
            + DOCUMENT_STATUS + " INTEGER DEFAULT 0, " //0 - записан, 1 - проведен, 2 - удален
            + DOCUMENT_ORGANIZATION_ID + " INTEGER CONSTRAINT [fk_organization] REFERENCES [ref_organizations]([_id]) ON DELETE SET NULL ON UPDATE CASCADE DEFAULT null, "
            + DOCUMENT_WAREHOUSE_FROM_ID + " INTEGER NOT NULL ON CONFLICT FAIL CONSTRAINT [fk_warehouse_from] REFERENCES [ref_warehouess]([_id]) ON DELETE SET NULL ON UPDATE CASCADE, "
            + DOCUMENT_WAREHOUSE_TO_ID + " INTEGER NOT NULL ON CONFLICT FAIL CONSTRAINT [fk_warehouse_to] REFERENCES [ref_warehouess]([_id]) ON DELETE SET NULL ON UPDATE CASCADE);";

    private static final String TABLE_DOCUMENT_DISPLACEMENT_STOCKS_T_CREATE_SCRIPT = "CREATE TABLE "
            + TABLE_DOC_DISPLACEMENT_STOCKS_T + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DOCUMENT_STOCK_ID + " INTEGER CONSTRAINT [fk_stock] REFERENCES [ref_stocks]([_id]) ON DELETE SET NULL ON UPDATE CASCADE DEFAULT null, "
            + DOCUMENT_UNIT_ID + " INTEGER CONSTRAINT [fk_unit] REFERENCES [ref_units]([_id]) ON DELETE SET NULL ON UPDATE CASCADE DEFAULT null, "
            + DOCUMENT_AMOUNT + " DECIMAL DEFAULT 0, "
            + DOCUMENT_PRICE + " DECIMAL DEFAULT 0, "
            + DOCUMENT_DOCUMENT_ID + " INTEGER NOT NULL ON CONFLICT FAIL CONSTRAINT [fk_document] REFERENCES [doc_displacement_stocks]([_id]) ON DELETE RESTRICT ON UPDATE CASCADE); ";


    public DbAdapter(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
        db = dbHelper.getReadableDatabase();
    }


    public Cursor getElementsReferenceCursor(String selection, String[] selectionArgs, String orderBy) {

        Cursor c = db.query(TABLE_REF_STOCKS, null, selection, selectionArgs, null, null, orderBy);

        return c;
    }


    public Cursor getAllElements() {
        String sql = "select "
                + "o._id as " + REFERENCE_ID
                + ",o.code as " + REFERENCE_CODE
                + ",o.name as " + REFERENCE_NAME
                + ",o.folder as " + REFERENCE_FOLDER
                + ",o.remove as " + REFERENCE_REMOVE
                + " from " + TABLE_REF_STOCKS + " o order by " + REFERENCE_FOLDER + " DESC";

        return db.rawQuery(sql, null);
    }

    public String getNameById(String id) {
        Cursor c = null;
        StringBuilder builder = new StringBuilder();

        builder.append("select "
                + "o.name as " + REFERENCE_NAME
                + " from " + TABLE_REF_STOCKS + " o where coalesce(o._id,0) = " + id);

        c = db.rawQuery(builder.toString(), null);

        String result = context.getString(R.string.error_not_found_element);

        while (c.moveToNext()) {

            result = c.getString(c.getColumnIndex(DbAdapter.REFERENCE_NAME));
        }

        c.close();
        return result;
    }

    // показать пункты справочника
//    public ArrayList<SprElement> getSprElements(SprElement sprElement) {
    public int getParentFolder(int current_folder_id) {
        Cursor c = null;
        StringBuilder builder = new StringBuilder();

        builder.append("select "
                + "o.parent_id as " + REFERENCE_PARENT_ID
                + " from " + TABLE_REF_STOCKS + " o where coalesce(o._id,0) = " + current_folder_id);

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
                + " from " + TABLE_REF_STOCKS + " o");


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

    public ArrayList<SprElement> getSprElements(int current_folder_id, SprTypeSelect type_index) {
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
                + " from " + TABLE_REF_STOCKS + " o");

        if (current_folder_id == 0) {
            builder.append(" where coalesce(o.parent_id,0) = 0");// все корневые элементы
        } else {
            sprList.add(getSprElementById(current_folder_id));
            builder.append(" where coalesce(o.parent_id,0) = " + current_folder_id);// все дочерние объекты для выбранного
        }
        if (type_index == SprTypeSelect.ONLY_GROUPS) {
            builder.append(" and coalesce(o.folder,0) = 1");//только группы
        } else if (type_index == SprTypeSelect.ONLY_ELEMENTS) {
            builder.append(" and coalesce(o.folder,0) = 0");//только элементы
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

    public boolean updateSprElement(SprElement sprElement) {
//        SQLiteStatement stmt = null;

        ContentValues values = new ContentValues();
        values.put(REFERENCE_CODE, sprElement.getCode());
        values.put(REFERENCE_NAME, sprElement.getName());
        values.put(REFERENCE_FOLDER, sprElement.isFolder());
        values.put(REFERENCE_PARENT_ID, sprElement.getParent_id());
        values.put(REFERENCE_REMOVE, sprElement.isRemove());
        if (sprElement.getId() == null) {
            dbHelper.getWritableDatabase().insert(TABLE_REF_STOCKS, null, values);
        } else {
            dbHelper.getWritableDatabase().update(TABLE_REF_STOCKS, values, REFERENCE_ID + " = ? ", new String[]{Integer.toString(sprElement.getId())});
        }
        return true;

    }

    public ArrayList<DocElement> getDocElements(DocumentType docDisplacementStocks) {
        ArrayList<DocElement> docList = new ArrayList<>();

        Cursor c = null;
        StringBuilder builder = new StringBuilder();

        builder.append("select "
                + "o._id as " + REFERENCE_ID
                + ",o.doc_datetime as " + DOCUMENT_DATETIME
                + ",o.status as " + DOCUMENT_STATUS
                + " from " + TABLE_DOC_DISPLACEMENT_STOCKS + " o");



        builder.append(" order by " + DOCUMENT_DATETIME + " DESC");


        c = db.rawQuery(builder.toString(), null);


        while (c.moveToNext()) {
            DocElement docValue = new DocElement();
            docValue.setId(c.getInt(c.getColumnIndex(DbAdapter.REFERENCE_ID)));
            docValue.setDoc_datetime(c.getLong(c.getColumnIndex(DbAdapter.DOCUMENT_DATETIME)));
            docValue.setStatus(c.getInt(c.getColumnIndex(DbAdapter.DOCUMENT_STATUS)));
            docList.add(docValue);
        }
        c.close();

        return docList;
    }

    public boolean updateDocElement(DocElement docElement) {

        ContentValues values = new ContentValues();
        values.put(DOCUMENT_DATETIME, docElement.getDoc_datetime());
        values.put(DOCUMENT_STATUS, docElement.getStatus());

        if (docElement.getId() == null) {
            dbHelper.getWritableDatabase().insert(TABLE_DOC_DISPLACEMENT_STOCKS, null, values);
        } else {
            dbHelper.getWritableDatabase().update(TABLE_DOC_DISPLACEMENT_STOCKS, values, REFERENCE_ID + " = ? ", new String[]{Integer.toString(docElement.getId())});
        }
        return true;
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_REFERENCE_STOCKS_CREATE_SCRIPT);
            db.execSQL(TABLE_REFERENCE_UNITS_CREATE_SCRIPT);
            db.execSQL(TABLE_REFERENCE_WAREHOUSE_SCRIPT);
            db.execSQL(TABLE_REFERENCE_ORGANIZATIONS_CREATE_SCRIPT);
            db.execSQL(TABLE_DOCUMENT_DISPLACEMENT_STOCKS_CREATE_SCRIPT);
            db.execSQL(TABLE_DOCUMENT_DISPLACEMENT_STOCKS_T_CREATE_SCRIPT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE " + TABLE_REF_STOCKS);
//            db.execSQL("DROP TABLE " + TABLE_REF_UNITS);
//            db.execSQL("DROP TABLE " + TABLE_REF_WAREHOUSES);
//            db.execSQL("DROP TABLE " + TABLE_REF_ORGANIZATIONS);
            db.execSQL("DROP TABLE " + TABLE_DOC_DISPLACEMENT_STOCKS);
//            db.execSQL("DROP TABLE " + TABLE_DOC_DISPLACEMENT_STOCKS_T);
            onCreate(db);
        }

    }
}
