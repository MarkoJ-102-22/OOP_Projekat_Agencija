package com.example.agencija.model;

import java.util.ArrayList;

public class Admin {
    private final int adminID;
    private final String ime;
    private final String prezime;
    private final String korisnickoIme;
    private String sifra;

    public static ArrayList<Admin> sviAdmini = new ArrayList<>();

    public Admin(int adminID, String ime, String prezime, String korisnickoIme, String sifra) throws Exception {
        this.adminID = adminID;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.sifra = sifra;

        if (!postojiAd(this)) {
            sviAdmini.add(this);
        } else {
            throw new Exception("Postoji adminski nalog sa zadatim podacima!");
        }



    }


    private boolean postojiAd(Admin data) {
        if (sviAdmini != null) {
            for (Admin ad : sviAdmini) {
                if (ad.adminID != data.adminID && ad.korisnickoIme.equals(data.korisnickoIme)) {
                    return true;
                }
            }
        }

        return false;
    }

    public int getadminID() {
        return adminID;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public static ArrayList<Admin> getSviAdmini() {
        return sviAdmini;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public String getSifra() {
        return sifra;
    }


    public void setAdminSifra(String sifra) {
        this.sifra = sifra;
    }

    public static Admin getAdmin(int adminID) {
        for (Admin ad : sviAdmini) {
            if (ad.adminID == adminID) {
                return ad;
            }
        }

        return null;
    }

    public static int brojSvihAdmina() {
        return sviAdmini.size();
    }

    public static int getMaxAdminID() {
        int maxAdminID = Integer.MIN_VALUE;

        for (Admin ad : sviAdmini) {
            if (ad.getadminID() > maxAdminID) {
                maxAdminID = ad.getadminID();
            }
        }

        return maxAdminID;
    }


    public static Admin getAdminDuze(String korisnickoIme, String sifra) {
        //System.out.println(korisnickoIme + " " + sifra);
        for (Admin a : sviAdmini) {
            if (a.getKorisnickoIme().equals(korisnickoIme) && a.getSifra().equals(sifra)) {
                return a;
            }
            else {
                System.out.println("ne podudaraju se:");
                System.out.println(korisnickoIme + " " + sifra);
                System.out.println(a.getKorisnickoIme() + " " + a.getSifra());
            }
        }

        return null;
    }



}
