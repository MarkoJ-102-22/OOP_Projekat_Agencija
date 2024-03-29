package com.example.agencija.model;

import java.util.ArrayList;

public class Smjestaj {
    private final int SmjestajID;
    private final String SmjestajIme;

    private final String brojZvjezdica;
    private String vrstaSobe;
    private String cijenaPoNocenju;

    public static ArrayList<Smjestaj> sviSmjestajevi = new ArrayList<>();

    public Smjestaj(int smjestajID, String smjestajIme, String brojZvjezdica, String vrstaSobe, String cijenaPoNocenju) throws Exception {
        SmjestajID = smjestajID;
        SmjestajIme = smjestajIme;
        this.brojZvjezdica = brojZvjezdica;
        this.vrstaSobe = vrstaSobe;
        this.cijenaPoNocenju = cijenaPoNocenju;
//
//        if (!postojiSm(this)) {
//            sviSmjestajevi.add(this);
//        } else {
//
//            // throw new Exception("Postoji adminski nalog sa zadatim podacima!");
//        }
    }

    public static void dodajNoviSmjestaj(Smjestaj smjesta) {
        sviSmjestajevi.add(smjesta);
    }

    private boolean postojiSm(Smjestaj data) {
        if (sviSmjestajevi != null) {
            for (Smjestaj sm : sviSmjestajevi) {
                if (sm.getSmjestajIme().equals(data.SmjestajIme)) {
                    return true;
                } else if (sm.brojZvjezdica.equals(data.brojZvjezdica)) {
                    return true;
                } else if (sm.vrstaSobe.equals(data.vrstaSobe)) {
                    return true;
                } else if (sm.cijenaPoNocenju.equals(data.cijenaPoNocenju)) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    public int getSmjestajID() {
        return SmjestajID;
    }

    public String getSmjestajIme() {
        return SmjestajIme;
    }

    public String getBrojZvjezdica() {
        return brojZvjezdica;
    }

    public String getVrstaSobe() {
        return vrstaSobe;
    }

    public String getCijenaPoNocenju() {
        return cijenaPoNocenju;
    }

    public static Smjestaj getSmjestajById(int smjestajId) {
        for (Smjestaj smjestaj : sviSmjestajevi) {
            if (smjestaj.getSmjestajID() == smjestajId) {
                return smjestaj;
            }
        }
        return null;
    }

    public static int brojSvihSoba() {
        return sviSmjestajevi.size();
    }

    public void setCijenaPoNocenju(String cijenaPoNocenju) {
        this.cijenaPoNocenju = cijenaPoNocenju;
    }

    public static void ocistiSveSmjestajeve() {
        sviSmjestajevi.clear();
    }
}
