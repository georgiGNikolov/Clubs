package com.xcomputers.clubs.clubs.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.xcomputers.networking.clubs.Club;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by xComputers on 05/08/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "clubs.db";
    private static final String TABLE_NAME = "clubs";
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "NAME";
    private static final String COL_PHONE = "PHONE";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_FACEBOOK_URL = "FACEBOOK_URL";
    private static final String COL_UPDATED_AT = "UPDATED_AT";
    private static final String COL_CREATED_AT = "CREATED_AT";
    private static final String COL_FULLSIZE_IMG_URL = "FULLSIZE_IMG_URL";
    private static final String COL_THUMBNAIL_URL = "THUMBNAIL_URL";

    private static final int ID_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int PHONE_INDEX = 2;
    private static final int EMAIL_INDEX = 3;
    private static final int FACEBOOK_INDEX = 4;
    private static final int UPDATED_AT_INDEX = 5;
    private static final int CREATED_AT_INDEX = 6;
    private static final int FULLSIZE_IMAGE_INDEX = 7;
    private static final int THUMBNAIL_INDEX = 8;


    private static DbHelper instance;

    public static DbHelper getInstance(){
        if(instance == null){
            throw new IllegalStateException("The DbHelper is not initialized");
        }
        return instance;
    }

    public static void init(Context context){
        instance = new DbHelper(context);
    }

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY" + ","
                + COL_NAME + " TEXT" + ","
                + COL_PHONE + " TEXT" + ","
                + COL_EMAIL + " TEXT" + ","
                + COL_FACEBOOK_URL + " TEXT" + ","
                + COL_UPDATED_AT + " INTEGER" + ","
                + COL_CREATED_AT + " INTEGER" + ","
                + COL_FULLSIZE_IMG_URL + " TEXT" + ","
                + COL_THUMBNAIL_URL + " TEXT"
                + ")"
        );
    }


    public void addData(Club club){
        //I could have used ContentValues instead of executing compiled statements and the code would be
        // more readable however this works faster on orders of magnitude and its worth the messier code
        getWritableDatabase().beginTransaction();
        SQLiteStatement statement = getWritableDatabase().compileStatement("INSERT INTO " + TABLE_NAME
                + " ("
                + COL_ID + ", "
                + COL_NAME + ", "
                + COL_PHONE + ", "
                + COL_EMAIL + ", "
                + COL_FACEBOOK_URL + ", "
                + COL_UPDATED_AT + ", "
                + COL_CREATED_AT + ", "
                + COL_FULLSIZE_IMG_URL + ", "
                + COL_THUMBNAIL_URL + ") "
                + "VALUES(?,?,?,?,?,?,?,?,?)");
        try {
            statement.bindLong(ID_INDEX + 1, club.getId());
            statement.bindString(NAME_INDEX + 1, club.getName());
            statement.bindString(PHONE_INDEX + 1, club.getPhoneNumber());
            statement.bindString(EMAIL_INDEX + 1, club.getEmail());
            statement.bindString(FACEBOOK_INDEX + 1, club.getFbUrl());
            statement.bindLong(UPDATED_AT_INDEX + 1, club.getUpdatedAt().getTime());
            statement.bindLong(CREATED_AT_INDEX + 1, club.getCreatedAt().getTime());
            statement.bindString(FULLSIZE_IMAGE_INDEX + 1, club.getFullSizeUrl());
            statement.bindString(THUMBNAIL_INDEX + 1, club.getThumbNailUrl());
            statement.executeInsert();
            getWritableDatabase().setTransactionSuccessful();
        }finally {
            statement.close();
            getWritableDatabase().endTransaction();
        }
    }

    public void updateData(Club club){
        //I could have used ContentValues instead of executing compiled statements and the code would be
        // more readable however this works faster on orders of magnitude and its worth the messier code
        getWritableDatabase().beginTransaction();
        SQLiteStatement statement = getWritableDatabase().compileStatement(
                "UPDATE " + TABLE_NAME
                        + " SET "
                        + COL_ID + "=?,"
                        + COL_NAME +"=?,"
                        + COL_PHONE + "=?,"
                        + COL_EMAIL +"=?,"
                        + COL_FACEBOOK_URL + "=?,"
                        + COL_UPDATED_AT +"=?,"
                        + COL_CREATED_AT +"=?,"
                        + COL_FULLSIZE_IMG_URL + "=?,"
                        + COL_THUMBNAIL_URL +"=?"
                        + " WHERE " + COL_ID + "=" + club.getId());

        try{
            statement.bindLong(ID_INDEX + 1, club.getId());
            statement.bindString(NAME_INDEX + 1, club.getName());
            statement.bindString(PHONE_INDEX + 1, club.getPhoneNumber());
            statement.bindString(EMAIL_INDEX + 1, club.getEmail());
            statement.bindString(FACEBOOK_INDEX + 1, club.getFbUrl());
            statement.bindLong(UPDATED_AT_INDEX + 1, club.getUpdatedAt().getTime());
            statement.bindLong(CREATED_AT_INDEX + 1, club.getCreatedAt().getTime());
            statement.bindString(FULLSIZE_IMAGE_INDEX + 1, club.getFullSizeUrl());
            statement.bindString(THUMBNAIL_INDEX + 1, club.getThumbNailUrl());
            statement.executeUpdateDelete();
            getWritableDatabase().setTransactionSuccessful();
        }finally {
            statement.close();
            getWritableDatabase().endTransaction();
        }
    }

    public List<Club> getAllData(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);;
        List<Club> clubs = null;
        cursor.moveToFirst();
        try {
            clubs = new ArrayList<>();
            while (!cursor.isAfterLast()) {

                long id = cursor.getLong(ID_INDEX);
                String name = cursor.getString(NAME_INDEX);
                String phone = cursor.getString(PHONE_INDEX);
                String email = cursor.getString(EMAIL_INDEX);
                String facebookUrl = cursor.getString(FACEBOOK_INDEX);
                long updatedAt = cursor.getLong(UPDATED_AT_INDEX);
                long createdAt = cursor.getLong(CREATED_AT_INDEX);
                String fullSizeUrl = cursor.getString(FULLSIZE_IMAGE_INDEX);
                String thumbUrl = cursor.getString(THUMBNAIL_INDEX);
                clubs.add(new Club(id, name, phone, email, facebookUrl, new Date(updatedAt), new Date(createdAt), thumbUrl, fullSizeUrl));
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return clubs;
    }


    public void deleteData(Club club){
        getWritableDatabase().delete(TABLE_NAME, COL_ID + "=" + club.getId(), null);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
