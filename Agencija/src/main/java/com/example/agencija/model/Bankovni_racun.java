package com.example.agencija.model;

import com.example.agencija.sql.Driver;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Bankovni_racun {

    private final int bankovniRacunID;
    private final String brojRacuna;
    private final String jmbg;
    private Double stanje;

    private static final Driver dr = new Driver();

    public static ArrayList<Bankovni_racun> sviBankovniRacuni = new ArrayList<>();

    public Bankovni_racun(int bankovniRacunID, String brojRacuna, String jmbg, Double stanje) throws Exception {
        this.bankovniRacunID = bankovniRacunID;
        this.brojRacuna = brojRacuna;
        this.jmbg = jmbg;
        this.stanje = stanje;

        if (!postojiBR(this)) {
            sviBankovniRacuni.add(this);
        } else {
            throw new Exception("Postoji adminski nalog sa zadatim podacima!");
        }
    }

    private boolean postojiBR(Bankovni_racun data) {
        if (sviBankovniRacuni != null) {
            for (Bankovni_racun br : sviBankovniRacuni) {
                if (br.bankovniRacunID != data.bankovniRacunID && br.jmbg.equals(data.jmbg)) {
                    return true;
                }
            }
        }

        return false;
    }

    public int getBankovniRacunID() {
        return bankovniRacunID;
    }

    public String getBrojRacuna() {
        return brojRacuna;
    }

    public String getJmbg() {
        return jmbg;
    }

    public static Bankovni_racun getRacunByBrojRacuna(String brojRacuna) {
        for (Bankovni_racun br : sviBankovniRacuni) {
            if (br.getBrojRacuna().equals(brojRacuna)) {
                return br;
            }
        }
        return null;
    }

    public Double getStanje() {
        return stanje;
    }

    public void setStanje(Double stanje) {
        this.stanje = stanje;
    }

    public static ArrayList<Bankovni_racun> getSviBankovniRacuni() {
        return sviBankovniRacuni;
    }


    public static void vratiNovacAgenciji(double iznos) {
        dr.startConnection();

        try {
            String query = "UPDATE Bankovni_racun SET stanje = stanje + ? WHERE id = ?";
            //UPDATE `bankovni_racun` SET `stanje` = '5555.00' WHERE `bankovni_racun`.`id` = 3;
            try (PreparedStatement statement = dr.getConn().prepareStatement(query)) {
                statement.setDouble(1, iznos);
                statement.setString(2, "11");
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Novac je uspešno vraćen na račun agencije.");
                } else {
                    System.out.println("Nije uspelo vraćanje novca na račun agencije.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            dr.endConnection();
        }
    }

    public static void skiniNovacAgenciji(double iznos) {
        dr.startConnection();

        try {
            String query = "UPDATE Bankovni_racun SET stanje = stanje - ? WHERE id = ?";
            //UPDATE `bankovni_racun` SET `stanje` = '5555.00' WHERE `bankovni_racun`.`id` = 3;
            try (PreparedStatement statement = dr.getConn().prepareStatement(query)) {
                statement.setDouble(1, iznos);
                statement.setString(2, "11");
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Novac je uspešno vraćen na račun agencije.");
                } else {
                    System.out.println("Nije uspelo vraćanje novca na račun agencije.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            dr.endConnection();
        }
    }


    public static void vratiNovacKlijentu(int klijentID, double iznos) {
        dr.startConnection();

        try {
            String query = "UPDATE Bankovni_racun SET stanje = stanje + ? WHERE jmbg IN (SELECT jmbg FROM Klijent WHERE id = ?)";
            try (PreparedStatement statement = dr.getConn().prepareStatement(query)) {
                statement.setDouble(1, iznos);
                statement.setInt(2, klijentID);
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Novac je uspešno vraćen na račun klijenta.");
                } else {
                    System.out.println("Nije uspelo vraćanje novca na račun klijenta.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            dr.endConnection();
        }
    }

    public static void skiniNovacKlijentu(int klijentID, double iznos) {
        dr.startConnection();

        try {
            String query = "UPDATE Bankovni_racun SET stanje = stanje - ? WHERE jmbg IN (SELECT jmbg FROM Klijent WHERE id = ?)";
            try (PreparedStatement statement = dr.getConn().prepareStatement(query)) {
                statement.setDouble(1, iznos);
                statement.setInt(2, klijentID);
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Novac je uspešno vraćen na račun klijenta.");
                } else {
                    System.out.println("Nije uspelo vraćanje novca na račun klijenta.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            dr.endConnection();
        }
    }
}
