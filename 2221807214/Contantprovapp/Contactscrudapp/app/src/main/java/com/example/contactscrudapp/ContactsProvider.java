package com.example.contactscrudapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class ContactsProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.contactscrudapp.provider";
    public static final String PATH = "contacts";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);

    // Column names
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PHONE = "phone";

    private static final int CONTACTS = 1;
    private static final int CONTACT_ID = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, PATH, CONTACTS);
        uriMatcher.addURI(AUTHORITY, PATH + "/#", CONTACT_ID);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        SQLiteOpenHelper dbHelper = new SQLiteOpenHelper(getContext(), "contacts.db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE contacts (" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NAME + " TEXT, " +
                        PHONE + " TEXT);");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS contacts");
                onCreate(db);
            }
        };
        db = dbHelper.getWritableDatabase();
        return (db != null);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insert("contacts", null, values);
        if (id > 0) {
            Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(resultUri, null);
            return resultUri;
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                cursor = db.query("contacts", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case CONTACT_ID:
                cursor = db.query("contacts", projection, ID + "=?",
                        new String[]{uri.getLastPathSegment()}, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                count = db.update("contacts", values, selection, selectionArgs);
                break;
            case CONTACT_ID:
                count = db.update("contacts", values, ID + "=?",
                        new String[]{uri.getLastPathSegment()});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                count = db.delete("contacts", selection, selectionArgs);
                break;
            case CONTACT_ID:
                count = db.delete("contacts", ID + "=?",
                        new String[]{uri.getLastPathSegment()});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                return "vnd.android.cursor.dir/vnd.com.example.contactscrudapp.contacts";
            case CONTACT_ID:
                return "vnd.android.cursor.item/vnd.com.example.contactscrudapp.contacts";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
