package com.android.foodorderapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "orders.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CUSTOMER_NAME = "customer_name";
    private static final String COLUMN_DELIVERY_CITY = "delivery_city";
    private static final String COLUMN_RESTAURANT_NAME = "restaurant_name";
    private static final String COLUMN_ITEMS = "items";
    private static final String COLUMN_TOTAL_AMOUNT = "total_amount";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_CUSTOMER_NAME + " TEXT," +
                COLUMN_DELIVERY_CITY + " TEXT," +
                COLUMN_RESTAURANT_NAME + " TEXT," +
                COLUMN_ITEMS + " TEXT," +
                COLUMN_TOTAL_AMOUNT + " REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public long addOrder(String customerName, String deliveryCity, String restaurantName, String items, float totalAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CUSTOMER_NAME, customerName);
        values.put(COLUMN_DELIVERY_CITY, deliveryCity);
        values.put(COLUMN_RESTAURANT_NAME, restaurantName);
        values.put(COLUMN_ITEMS, items);
        values.put(COLUMN_TOTAL_AMOUNT, totalAmount);
        long newRowId = db.insert(TABLE_ORDERS, null, values);
        db.close();
        return newRowId;
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                order.setCustomerName(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_NAME)));
                order.setDeliveryCity(cursor.getString(cursor.getColumnIndex(COLUMN_DELIVERY_CITY)));
                order.setRestaurantName(cursor.getString(cursor.getColumnIndex(COLUMN_RESTAURANT_NAME)));
                order.setItems(cursor.getString(cursor.getColumnIndex(COLUMN_ITEMS)));
                order.setTotalAmount(cursor.getFloat(cursor.getColumnIndex(COLUMN_TOTAL_AMOUNT)));
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orderList;
    }
}
