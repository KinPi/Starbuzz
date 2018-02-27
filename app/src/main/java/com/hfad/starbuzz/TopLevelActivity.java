package com.hfad.starbuzz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TopLevelActivity extends Activity {
    private SQLiteDatabase db = null;
    private Cursor cursor = null;
    private ListView listFavorites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);
        setupOptionsListView();
        setupFavoritesListView();
    }

    private void setupOptionsListView() {
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(TopLevelActivity.this, DrinkCategoryActivity.class);
                    startActivity(intent);
                }
            }
        };

        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);

    }


    private void setupFavoritesListView() {
        listFavorites = (ListView) findViewById(R.id.list_favorites);
        try {
            SQLiteOpenHelper helper = new StarbuzzDatabaseHelper(this);
            db = helper.getReadableDatabase();
            cursor = db.query("DRINK",
                    new String [] {"_id", "NAME"},
                    "FAVORITE = ?",
                    new String[] {Integer.toString(1)},
                    null, null, null);
            CursorAdapter adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[] {"NAME"},
                    new int[] {android.R.id.text1},
                    0);
            listFavorites.setAdapter(adapter);
        }
        catch (SQLException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TopLevelActivity.this, DrinkActivity.class);
                intent.putExtra(DrinkActivity.EXTRA_DRINKID, (int) id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    public void onRestart () {
        super.onRestart();
        Cursor newCursor = db.query("DRINK",
                new String [] {"_id", "NAME"},
                "FAVORITE = 1",
                null, null, null, null);
        CursorAdapter adapter = (CursorAdapter) listFavorites.getAdapter();
        adapter.changeCursor(newCursor);
        cursor = newCursor;
    }

}
