package com.hfad.starbuzz;

/**
 * Created by Kin on 9/17/17.
 */

public class Drink {
    private String name;
    private String description;
    private int imageResourceId;

    public static final Drink[] drinks = {
            new Drink("Latte", "A couple of espresso shots with steamed milk", R.drawable.latte),
            new Drink("Cappuccino", "Espresso, hot milk, and a steamed milk foam", R.drawable.cappuccino),
            new Drink("Filter", "Highest quality beans roasted and brewed fresh", R.drawable.filter)
    };

    private Drink (String name, String description, int imageResourceId) {
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    public String getDescription () {
        return this.description;
    }

    public String getName () {
        return this.name;
    }

    public int getImageResourceId () {
        return this.imageResourceId;
    }

    public String toString () {
        return this.name;
    }
}
