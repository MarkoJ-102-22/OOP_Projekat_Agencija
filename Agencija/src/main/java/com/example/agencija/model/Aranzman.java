package com.example.agencija.model;

import java.util.ArrayList;
import java.util.Date;

public class Aranzman {

    private final String id;

    private final String nazivPutovanja;

    private final String destinacija;

    private String prevoz;

    private Date datumPolaska;

    private Date datumDolaska;

    private String cijenaAranzmana;

    private final Integer smjestajId;


    public static ArrayList<Aranzman> sviAranzmani = new ArrayList<>();

    public Aranzman(String id, String nazivPutovanja, String destinacija, String prevoz, Date datumPolaska, Date datumDolaska, String cijenaAranzmana, Integer smjestajId) throws Exception {
        this.id = id;
        this.nazivPutovanja = nazivPutovanja;
        this.destinacija = destinacija;
        this.prevoz = prevoz;
        this.datumPolaska = datumPolaska;
        this.datumDolaska = datumDolaska;
        this.cijenaAranzmana = cijenaAranzmana;
        this.smjestajId = smjestajId;

        if (!postojiAr(this)) {
            sviAranzmani.add(this);
        } else {
            try {
                throw new Exception("Postoji adminski nalog sa zadatim podacima!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }


    private boolean postojiAr(Aranzman data) {
        if (sviAranzmani != null) {
            for (Aranzman ar : sviAranzmani) {
                if (ar.smjestajId != data.smjestajId && ar.id.equals(data.id)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getId() {
        return id;
    }

    public String getNazivPutovanja() {
        return nazivPutovanja;
    }

    public String getDestinacija() {
        return destinacija;
    }

    public String getPrevoz() {
        return prevoz;
    }

    public static ArrayList<Aranzman> getSviAranzmani() {
        return sviAranzmani;
    }

    public Date getDatumPolaska() {
        return datumPolaska;
    }

    public Date getDatumDolaska() {
        return datumDolaska;
    }

    public String getCijenaAranzmana() {
        return cijenaAranzmana;
    }

    public Integer getSmjestajId() {
        return smjestajId;
    }

    public static int getBrojSvihAranzmana() {
        return sviAranzmani.size();
    }

    public static int getMax() {
        int maxKlijentID = 0;

        if (sviAranzmani != null) {
            for (Aranzman ar : sviAranzmani) {
                String aranzmanIdString = ar.getId();
                int aranzmanId;
                try {
                    aranzmanId = Integer.parseInt(aranzmanIdString);

                    System.out.println("Aranzman ID kao int: " + aranzmanId);
                    if (aranzmanId > maxKlijentID) {
                        maxKlijentID = aranzmanId;
                    }
                } catch (NumberFormatException e) {

                    System.out.println("Nije moguće pretvoriti String u int.");
                }
            }
        }


        return maxKlijentID;
    }

    public static Aranzman getAranzmanById(String targetId) {
        for (Aranzman ar : sviAranzmani) {
            String aranzmanIdString = ar.getId();
            if (aranzmanIdString.equals(targetId)) {

                return ar;
            }
        }


        return null;
    }


    public void setPrevoz(String prevoz) {
        this.prevoz = prevoz;
    }

    public void setDatumPolaska(Date datumPolaska) {
        this.datumPolaska = datumPolaska;
    }

    public void setDatumDolaska(Date datumDolaska) {
        this.datumDolaska = datumDolaska;
    }

    public void setCijenaAranzmana(String cijenaAranzmana) {
        this.cijenaAranzmana = cijenaAranzmana;
    }

    @Override
    public String toString() {
        return  id +
                " - " + nazivPutovanja +
                " - " + destinacija +
                " - " + prevoz +
                " - " + datumPolaska +
                " - " + datumDolaska +
                " - " + cijenaAranzmana +
                " - " + smjestajId;
    }

    public static void ocistiSveAranzmane() {
        sviAranzmani.clear();
    }
}

