package com.example.pavlo.networkin.content_providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by pavlo on 5/18/2017.
 */

public class NetworksProvider extends ContentProvider {

    private final String LOG_TAG = "Networks provider log";

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(NetworksProviderConfig.AUTHORITY, NetworksProviderConfig.NETWORKS_PATH, NetworksProviderConfig.URI_NETWORKS);
        uriMatcher.addURI(NetworksProviderConfig.AUTHORITY, NetworksProviderConfig.NETWORKS_PATH + "/#", NetworksProviderConfig.URI_NETWORKS_ID);
    }

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        database = dbHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case NetworksProviderConfig.URI_NETWORKS:
                Log.d(LOG_TAG, "URI_NETWORKS");
                if (TextUtils.isEmpty(sortOrder))
                    sortOrder = DatabaseConfig.NETWORK_SSID + " ASC";
                break;
            case NetworksProviderConfig.URI_NETWORKS_ID:
                Log.d(LOG_TAG, "URI_NETWORKS_ID");
                String id = uri.getLastPathSegment();
                selection = TextUtils.isEmpty(selection) ?
                        DatabaseConfig.NETWORK_ID + " = " + id :
                        selection + " AND" + DatabaseConfig.NETWORK_ID + " = " + id;
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }

        Cursor cursor = database.query(DatabaseConfig.NETWORKS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), NetworksProviderConfig.NETWORKS_CONTENT_URI);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Log.d(LOG_TAG, "insert" + uri.toString());
        if (uriMatcher.match(uri) != NetworksProviderConfig.URI_NETWORKS)
            throw new IllegalArgumentException("Incorrect uri: " + uri);

        long rowId = database.insert(DatabaseConfig.NETWORKS_TABLE, null, contentValues);
        Uri resultUri = ContentUris.withAppendedId(NetworksProviderConfig.NETWORKS_CONTENT_URI, rowId);
        getContext().getContentResolver().notifyChange(resultUri, null);

        return resultUri;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "update" + uri.toString());
        switch (uriMatcher.match(uri)) {
            case NetworksProviderConfig.URI_NETWORKS:
                Log.d(LOG_TAG, "URI_NETWORKS");
                break;
            case NetworksProviderConfig.URI_NETWORKS_ID:
                Log.d(LOG_TAG, "URI_NETWORKS_ID");
                String id = uri.getLastPathSegment();
                selection = TextUtils.isEmpty(selection) ?
                        DatabaseConfig.NETWORK_ID + " = " + id :
                        selection + " AND" + DatabaseConfig.NETWORK_ID + " = " + id;
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }
        int id = database.update(DatabaseConfig.NETWORKS_TABLE, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return id;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete" + uri.toString());
        switch (uriMatcher.match(uri)) {
            case NetworksProviderConfig.URI_NETWORKS:
                Log.d(LOG_TAG, "URI_NETWORKS");
                break;
            case NetworksProviderConfig.URI_NETWORKS_ID:
                Log.d(LOG_TAG, "URI_NETWORKS_ID");
                String id = uri.getLastPathSegment();
                selection = TextUtils.isEmpty(selection) ?
                        DatabaseConfig.NETWORK_ID + " = " + id :
                        selection + " AND" + DatabaseConfig.NETWORK_ID + " = " + id;
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }
        int id = database.delete(DatabaseConfig.NETWORKS_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return id;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType" + uri.toString());
        switch (uriMatcher.match(uri)) {
            case NetworksProviderConfig.URI_NETWORKS:
                return NetworksProviderConfig.NETWORK_CONTENT_TYPE;
            case NetworksProviderConfig.URI_NETWORKS_ID:
                return NetworksProviderConfig.NETWORK_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DatabaseConfig.DB_NAME, null, DatabaseConfig.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(DatabaseConfig.DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        }
    }
}
