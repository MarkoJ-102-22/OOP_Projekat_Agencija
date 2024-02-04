package com.example.agencija.model;

import java.util.ArrayList;

public class Klijent {

    private final int klijentID;
    private final String ime;
    private final String prezime;
    private final String broj_telefona;
    private final String jmbg;
    private final String broj_racuna;

    private final String korisnickoIme;
    private String sifra;

    public static ArrayList<Klijent> sviKlijenti = new ArrayList<>();

    public Klijent(int klijentID, String ime, String prezime, String broj_telefona, String jmbg,String broj_racuna, String korisnickoIme, String sifra) throws Exception {
        this.klijentID = klijentID;
        this.ime = ime;
        this.prezime = prezime;
        this.broj_telefona = broj_telefona;

        if (jmbg.length() != 13) {
            throw new IllegalArgumentException("JMBG nije validan. Unos treba da ima 13 cifara.");
        }
        this.jmbg = jmbg;
        this.broj_racuna = broj_racuna;
        this.korisnickoIme = korisnickoIme;
        this.sifra = sifra;

        if (!postoji(this)) {
            sviKlijenti.add(this);
        } else {
            throw new Exception("Postoji korisnicki nalog sa zadatim podacima!");
        }
    }


    private boolean postoji(Klijent data) {
        if (sviKlijenti != null) {
            for (Klijent kl : sviKlijenti) {
                if (kl.klijentID != data.klijentID && kl.korisnickoIme.equals(data.korisnickoIme) && kl.jmbg.equals(data.jmbg)) {
                    return true;
                }
            }
        }

        return false;
    }

    //pravim sve getere pre
    public int getKlijentID() {
        return klijentID;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getBroj_telefona() {
        return broj_telefona;
    }

    public String getJmbg() {
        return jmbg;
    }

    public String getBroj_racuna() {
        return broj_racuna;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }
    public static Klijent getKlijentByKorisnickoIme(String korisnickoIme) {
        for (Klijent k : sviKlijenti) {
            if (k.getKorisnickoIme().equals(korisnickoIme)) {
                return k;
            }
        }
        return null;
    }
    public String getSifra() {
        return sifra;
    }

    public static ArrayList<Klijent> getSviKlijenti() {
        return sviKlijenti;
    }

    //mislim zasad da samo sifra treba od setera jer se trazi mogucnost zaboravljenja sifra
    public void setKlijentSifra(String sifra) {
        this.sifra = sifra;
    }

    public static Klijent getKlijentID(int klijentID) {
        for (Klijent kl : sviKlijenti) {
            if (kl.klijentID == klijentID) {
                return kl;
            }
        }

        return null;
    }

    public static int getMaxKlijentID() {
        int maxKlijentID = Integer.MIN_VALUE;

        for (Klijent kl : sviKlijenti) {
            if (kl.getKlijentID() > maxKlijentID) {
                maxKlijentID = kl.getKlijentID();
            }
        }

        return maxKlijentID;
    }


    public static Klijent getKlijentDuze(String korisnickoIme, String sifra) {
        //System.out.println(korisnickoIme + " " + sifra);
        for (Klijent k : sviKlijenti) {
            if (k.getKorisnickoIme().equals(korisnickoIme) && k.getSifra().equals(sifra)) {
                return k;
            }
            else {
                System.out.println("ne podudaraju se:");
                System.out.println(korisnickoIme + " " + sifra);
                System.out.println(k.getKorisnickoIme() + " " + k.getSifra());
            }
        }

        return null;
    }



}
