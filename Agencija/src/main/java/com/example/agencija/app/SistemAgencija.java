package com.example.agencija.app;

import com.example.agencija.sql.DBUtils;
import com.example.agencija.view.View;

public class SistemAgencija {

    private final View view;

    public SistemAgencija (View view) {
        this.view = view;
    }

    public void run() {
        DBUtils.getDataFromDB();
        view.drawView();
    }
}

