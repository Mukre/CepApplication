package com.teste.crudapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbGateway {
    private static DbGateway gw;
    private final SQLiteDatabase db;

    private DbGateway(Context ctx){
        DbHelper helper = new DbHelper(ctx);
        db = helper.getReadableDatabase();
    }

    public static DbGateway getInstance(Context ctx){
        if(gw == null)
            gw = new DbGateway(ctx);
        return gw;
    }

    public SQLiteDatabase getDb(){
        return this.db;
    }
}
