package com.hfad.starbuzz;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DrinkCategoryActivity extends Activity {
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_category);
        ListView listDrinks = (ListView) findViewById(R.id.list_drinks);

        try {
            SQLiteOpenHelper helper = new StarbuzzDatabaseHelper(this);
            db = helper.getReadableDatabase();
            cursor = db.query("DRINK",
                    new String[] {"_id", "NAME"},
                    null, null, null, null, null);
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[] {"NAME"},
                    new int[] {android.R.id.text1},
                    0);
            listDrinks.setAdapter(adapter);
        }
        catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DrinkCategoryActivity.this, DrinkActivity.class);
                intent.putExtra(DrinkActivity.EXTRA_DRINKID, (int) id);
                startActivity(intent);
            }
        };

        listDrinks.setOnItemClickListener(listener);
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
