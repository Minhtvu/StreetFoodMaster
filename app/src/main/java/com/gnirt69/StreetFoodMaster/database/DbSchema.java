package com.gnirt69.StreetFoodMaster.database;

/**
 * Created by Connor on 4/3/2017.
 */

public class DbSchema {
    public static final class StoreTable {

         // we can change what our database is storing
        public static final String NAME = "stores";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String LOCATION = "location";
        }
    }
}
