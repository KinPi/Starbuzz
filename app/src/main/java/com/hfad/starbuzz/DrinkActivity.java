package com.hfad.starbuzz;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends Activity {

    public static final String EXTRA_DRINKID = "drinkId";
    private int id;
    private CheckBox favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        Intent intent = getIntent();
        id = (Integer) intent.getExtras().get(EXTRA_DRINKID);

        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("DRINK",
                    new String [] {"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[] {Integer.toString(id)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String name = cursor.getString(0);
                String description = cursor.getString(1);
                int imageResourceId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);

                TextView nameTextView = (TextView) findViewById(R.id.name);
                nameTextView.setText(name);

                TextView descriptionTextView = (TextView) findViewById(R.id.description);
                descriptionTextView.setText(description);

                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(imageResourceId);
                photo.setContentDescription(name);

                favorite = (CheckBox) findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
            }

            cursor.close();
            db.close();
        }
        catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void onFavoriteClicked (View v) {
        new UpdateDrinkTask().execute(id);
    }

    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean> {
        private ContentValues drinkValues;

        @Override
        protected void onPreExecute () {
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());
        }

        @Override
        protected Boolean doInBackground(Integer... drinks) {
            int drinkId = drinks[0];
            SQLiteOpenHelper helper = new StarbuzzDatabaseHelper(DrinkActivity.this);
            try {
                SQLiteDatabase db = helper.getWritableDatabase();
                db.update("DRINK", drinkValues, "_id = ?", new String[]{Integer.toString(drinkId)});
                db.close();
                return true;
            }
            catch (SQLiteException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute (Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(DrinkActivity.this, "Database Unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
