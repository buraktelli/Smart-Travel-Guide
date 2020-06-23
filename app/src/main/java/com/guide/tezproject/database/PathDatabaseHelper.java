package com.guide.tezproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.guide.tezproject.entity.Nokta;

import java.util.ArrayList;

public class PathDatabaseHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME="ROTA.DB";
    public final static String TABLE_NAME="ROTANOKTA";
    public final static String COL_ID="ID";
    public final static String COL_NAME="NAME";
    public final static String COL_CITY="CITY";
    public final static String COL_LATITUDE="LATITUDE";
    public final static String COL_LONGITUDE="LONGITUDE";
    SQLiteDatabase database;

    int id=0;

    public PathDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_QUERY="CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT NOT NULL, CITY TEXT NOT NULL, LATITUDE REAL NOT NULL, LONGITUDE REAL NOT NULL)";
        db.execSQL(CREATE_TABLE_QUERY);
        this.database=db;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String UPGRADE_QUERY="DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(UPGRADE_QUERY);
        this.onCreate(db);
    }



    public boolean insertPoint(String name, String city, Double latitude, Double longitude) {
        database=this.getWritableDatabase();
        String query=  "SELECT * FROM "+TABLE_NAME;
        Cursor cursor=database.rawQuery(query,null);

        ContentValues value=new ContentValues();
        value.put(COL_NAME,name);
        value.put(COL_CITY,city);
        value.put(COL_LATITUDE,latitude);
        value.put(COL_LONGITUDE,longitude);

        Long result=database.insert(TABLE_NAME,null,value);
        database.close();
        if(result==-1)
            return false;
        else
            return true;
    }
    public Nokta getPoint(String name, String city) {
        database=this.getReadableDatabase();
        String query="SELECT * FROM "+TABLE_NAME+" WHERE NAME= '"+name+"'"+" AND CITY= '"+city+"'";
        Cursor cursor=database.rawQuery(query,null);
        Nokta nokta = new Nokta();

        if(cursor.moveToFirst()) {
            database.close();
            nokta.setName(cursor.getString(1));
            nokta.setCity(cursor.getString(2));
            nokta.setLatitude(cursor.getDouble(3));
            nokta.setLongitude(cursor.getDouble(4));
            return nokta;
        }else{
            database.close();
            return null;
        }
    }
    public ArrayList<Nokta> getAllPoints() {
        database=this.getReadableDatabase();
        String query="SELECT * FROM "+TABLE_NAME;
        Cursor cursor=database.rawQuery(query,null);
        ArrayList<Nokta> noktalar=new ArrayList<Nokta>();
        Nokta nokta;
        if(cursor.moveToFirst()){
            do{
                nokta = new Nokta();
                nokta.setName(cursor.getString(1));
                nokta.setCity(cursor.getString(2));
                nokta.setLatitude(cursor.getDouble(3));
                nokta.setLongitude(cursor.getDouble(4));

                noktalar.add(nokta);

            }while(cursor.moveToNext());
        }
        return  noktalar;
    }

    public boolean deletePoint(String name, String city){
        database = getReadableDatabase();
        String DELETE_QUERY = "DELETE FROM "+TABLE_NAME+" WHERE NAME= '"+name+"'"+" AND CITY= '"+city+"'";
        Cursor cursor = database.rawQuery(DELETE_QUERY,null);
        if (cursor.moveToFirst()){
            database.close();
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteAll(){
        database = getReadableDatabase();
        String DELETE_QUERY = "DELETE FROM "+TABLE_NAME;
        Cursor cursor = database.rawQuery(DELETE_QUERY,null);
        if (!cursor.moveToFirst()){
            database.close();
            return true;
        }else{
            return false;
        }
    }
}