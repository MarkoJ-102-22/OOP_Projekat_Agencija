package com.example.agencija.app;

import com.example.agencija.view.GUI;
import com.example.agencija.view.View;

public class Main {
    public static void main(String[] args) {
        View GUI = new GUI();

        SistemAgencija saapp = new SistemAgencija(GUI);
        saapp.run();
    }
}
