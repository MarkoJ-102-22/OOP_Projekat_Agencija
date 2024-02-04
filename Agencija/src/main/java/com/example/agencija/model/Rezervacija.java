package com.example.agencija.model;

import java.util.ArrayList;

public class Rezervacija {

    private final int KlijentID;
    private final String AranzmanID;
    private String ukupnaCijena;
    private String placenaCijena;

    public static ArrayList<Rezervacija> sveRezervacije = new ArrayList<>();

    public Rezervacija(int klijentID, String aranzmanID, String ukupnaCijena, String placenaCijena) throws Exception {
        this.KlijentID = klijentID;
        this.AranzmanID = aranzmanID;
        this.ukupnaCijena = ukupnaCijena;
        this.placenaCijena = placenaCijena;

        if (!postojiRe(this)) {
            sveRezervacije.add(this);
        } else {
            //throw new Exception("Postoji adminski nalog sa zadatim podacima!");
        }
    }

    private boolean postojiRe(Rezervacija data) {
        if (sveRezervacije != null) {
            for (Rezervacija re : sveRezervacije) {
                if (re.KlijentID == data.KlijentID && re.AranzmanID.equals(data.AranzmanID)) {
                    return true;
                }
            }
        }

        return false;
    }

    public int getKlijentID() {
        return KlijentID;
    }

    public String getAranzmanID() {
        return AranzmanID;
    }

    public String getUkupnaCijena() {
        return ukupnaCijena;
    }

    public static ArrayList<Rezervacija> getSveRezervacije() {
        return sveRezervacije;
    }

    public String getPlacenaCijena() {
        return placenaCijena;
    }


    public static Rezervacija getRez(int klijentID, String aranzmanID) {
        for (Rezervacija rezervacija : sveRezervacije) {
            if (rezervacija.getKlijentID() == klijentID && rezervacija.getAranzmanID().equals(aranzmanID)) {
                return rezervacija;
            }
        }
        return null;
    }
    public void setUkupnaCijena(String ukupnaCijena) {
        this.ukupnaCijena = ukupnaCijena;
    }

    public void setPlacenaCijena(String placenaCijena) {
        this.placenaCijena = placenaCijena;
    }

    @Override
    public String toString() {
        return  KlijentID +
                " - " + AranzmanID +
                " - " + ukupnaCijena +
                " - " + placenaCijena;
    }
}
