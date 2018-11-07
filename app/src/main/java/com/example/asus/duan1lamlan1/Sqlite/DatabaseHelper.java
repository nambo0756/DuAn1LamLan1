package com.example.asus.duan1lamlan1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asus.duan1lamlan1.Model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="UseManger.db";
    private static final String TABLE_USER="user";

    private static final String COLUMN_USER_ID="user_id";
    private static final String COLUMN_USER_NAME="user_name";
    private static final String COULMN_USER_EMAIL="user_email";
    private static final String COULMN_USER_PASSWORD="user_passwword";


    private String CREATE_USER_TABLE="CREATE TABLE "+TABLE_USER +"("+
            COLUMN_USER_ID + " INTEGTER PRIMARY KEY AUTOINCREMENT,"+COLUMN_USER_NAME +" TEXT,"
            +COULMN_USER_EMAIL +" TEXT,"+COULMN_USER_PASSWORD + " TEXT "+")";
    private String DROP_USER_TABLE="DROP TABLE IF EXISTS "+TABLE_USER;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         db.execSQL(DROP_USER_TABLE);
     onCreate(db);
    }
    public  void addUser(User user){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(COLUMN_USER_NAME,user.getName());
        values.put(COULMN_USER_PASSWORD,user.getPassword());
        values.put(COULMN_USER_EMAIL,user.getEmail());
        db.insert(TABLE_USER,null,values);
        db.close();


    }
    public List<User> getAllUser(){
        String[] coulmn={COLUMN_USER_ID,COULMN_USER_EMAIL,COLUMN_USER_NAME,COULMN_USER_PASSWORD};
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_USER,coulmn,null,null,null,null,sortOrder);
        if (cursor.moveToFirst()){
           do{ User user=new User();
              user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
              user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
              user.setEmail(cursor.getString(cursor.getColumnIndex(COULMN_USER_EMAIL)));
              user.setPassword(cursor.getString(cursor.getColumnIndex(COULMN_USER_PASSWORD)));
               userList.add(user);
        }while (cursor.moveToNext());
            }
            cursor.close();
        db.close();
        return userList;
    }
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COULMN_USER_EMAIL, user.getEmail());
        values.put(COULMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COULMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
    public boolean checkUser(String email, String password) {

        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COULMN_USER_EMAIL + " = ?" + " AND " + COULMN_USER_PASSWORD + " = ?";

        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }



}
