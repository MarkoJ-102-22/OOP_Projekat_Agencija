package com.example.agencija.sql;

import com.example.agencija.model.Aranzman;
import com.example.agencija.model.Admin;
import com.example.agencija.model.Bankovni_racun;
import com.example.agencija.model.Klijent;
import com.example.agencija.model.Rezervacija;
import com.example.agencija.model.Smjestaj;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class DBUtils {

    private static final Driver dr = new Driver();

    public static void getDataFromDB() {
        dr.startConnection();

        getKlijentiDB();
        getAdminiDB();
        getAranzmaniDB();
        getBankovniRacuniDB();
        getRezervacijeDB();
        getSmejstajeviDB();

        dr.endConnection();
    }

    private static void getKlijentiDB(){
        try{
            Statement statement = dr.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Klijent");

            while (resultSet.next()){
                int id= resultSet.getInt("id");
                String ime= resultSet.getString("ime");
                String prezime= resultSet.getString("prezime");
                String broj_telefona= resultSet.getString("broj_telefona");
                String jmbg= resultSet.getString("jmbg");
                String broj_racuna= resultSet.getString("broj_racuna");
                String korisnicko_ime= resultSet.getString("korisnicko_ime");
                String lozinka= resultSet.getString("lozinka");

                new Klijent(id,ime,prezime,broj_telefona,jmbg,broj_racuna,korisnicko_ime,lozinka);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void promjenaSifreKlijenta(int klijentID, String nova_lozinka) {
        dr.startConnection();

        try {
            String query = "UPDATE Klijent SET lozinka = ? WHERE id = ?";
            try (PreparedStatement statement = dr.getConn().prepareStatement(query)) {
                //mogu dodati da se hesira sifra ali to onda trebam promjeniti da se prilikom login salje heshirana lozinka
                statement.setString(1, nova_lozinka);
                statement.setInt(2, klijentID);
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Šifra klijenta uspešno promenjena.");
                    Klijent.getKlijentID(klijentID).setKlijentSifra(nova_lozinka);
                } else {
                    System.out.println("Nije pronađen klijent sa ID-om: " + klijentID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dr.endConnection();
        }
    }




    private static void getAdminiDB(){
        try{
            Statement statement = dr.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Admin");

            while (resultSet.next()){
                int id= resultSet.getInt("id");
                String ime= resultSet.getString("ime");
                String prezime= resultSet.getString("prezime");
                String korisnicko_ime= resultSet.getString("korisnicko_ime");
                String lozinka= resultSet.getString("lozinka");

                new Admin(id,ime,prezime,korisnicko_ime,lozinka);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void promjenaSifreAdmina(int adminID, String nova_lozinka) {
        dr.startConnection();

        try {
            String query = "UPDATE Admin SET lozinka = ? WHERE id = ?";
            try (PreparedStatement statement = dr.getConn().prepareStatement(query)) {
                //mogu dodati da se hesira sifra ali to onda trebam promjeniti da se prilikom login salje heshirana lozinka
                statement.setString(1, nova_lozinka);
                statement.setInt(2, adminID);
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Šifra admina uspešno promenjena.");
                    Admin.getAdmin(adminID).setAdminSifra(nova_lozinka);
                } else {
                    System.out.println("Nije pronađen admin sa ID-om: " + adminID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dr.endConnection();
        }
    }

    private static void getAranzmaniDB(){
        try{
            Statement statement = dr.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Aranzman");

            while (resultSet.next()){
                String id= resultSet.getString("id");
                String naziv_putovanja= resultSet.getString("naziv_putovanja");
                String destinacija= resultSet.getString("destinacija");
                String prevoz= resultSet.getString("prevoz");
                Date datum_polaska= resultSet.getDate("datum_polaska");
                Date datum_dolaska= resultSet.getDate("datum_dolaska");
                String cijena_aranzmana= resultSet.getString("cijena_aranzmana");
                int smjestaj_id= resultSet.getInt("smjestaj_id");

                new Aranzman(id,naziv_putovanja,destinacija,prevoz,datum_polaska,datum_dolaska,cijena_aranzmana,smjestaj_id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void getBankovniRacuniDB(){
        try{
            Statement statement = dr.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Bankovni_racun");

            while (resultSet.next()){
                int id= resultSet.getInt("id");
                String broj_racuna= resultSet.getString("broj_racuna");
                String jmbg= resultSet.getString("jmbg");
                Double stanje= resultSet.getDouble("stanje");

                new Bankovni_racun(id,broj_racuna,jmbg,stanje);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void getRezervacijeDB(){
        try{
            Statement statement = dr.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Rezervacija");

            while (resultSet.next()){
                int Klijent_id= resultSet.getInt("Klijent_id");
                String Aranzman_id= resultSet.getString("Aranzman_id");
                String ukupna_cijena= resultSet.getString("ukupna_cijena");
                String placena_cijena= resultSet.getString("placena_cijena");

                System.out.println(Klijent_id + Aranzman_id);
                new Rezervacija(Klijent_id,Aranzman_id,ukupna_cijena,placena_cijena);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void getSmejstajeviDB(){
        try{
            Statement statement = dr.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Smjestaj");

            while (resultSet.next()){
                int id= resultSet.getInt("id");
                String naziv= resultSet.getString("naziv");
                String broj_zvjezdica= resultSet.getString("broj_zvjezdica");
                String vrsta_sobe= resultSet.getString("vrsta_sobe");
                String cjena_po_nocenju= resultSet.getString("cjena_po_nocenju");


                new Smjestaj(id,naziv,broj_zvjezdica,vrsta_sobe,cjena_po_nocenju);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void dodajKlijentaDB(int id, String ime, String prezime, String korisnickoIme, String broj_racuna, String broj_telefona, String jmbg, String lozinka) {
        dr.startConnection();

        try {
            String query = "INSERT INTO Klijent(id, ime, prezime, broj_telefona, jmbg, broj_racuna, korisnicko_ime, lozinka) VALUES (?,?,?,?,?,?,?,?)";
            try (PreparedStatement statement = dr.getConn().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, id);
                statement.setString(2, ime);
                statement.setString(3, prezime);
                statement.setString(4, broj_telefona);
                statement.setString(5, jmbg);
                statement.setString(6, broj_racuna);
                statement.setString(7, korisnickoIme);
                statement.setString(8, lozinka);

                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Klijent uspešno dodat u bazu.");


                    ResultSet rs = statement.getGeneratedKeys();
                    if (rs.next()) {
                        int newID = rs.getInt(1);
                        System.out.println("Novi ID klijenta: " + newID);


                        new Klijent(newID, ime, prezime, broj_telefona, jmbg, broj_racuna, korisnickoIme, lozinka);
                    }
                } else {
                    System.out.println("Nije uspelo dodavanje klijenta u bazu.");
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

    public static void dodajAdminaDB(int id, String ime, String prezime, String korisnicko_ime,String lozinka) {
        dr.startConnection();

        try {
            String query = "INSERT INTO  admin (id, ime, prezime, korisnicko_ime, lozinka) VALUES (?,?,?,?,?)";
            try (PreparedStatement statement = dr.getConn().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, id);
                statement.setString(2, ime);
                statement.setString(3, prezime);
                statement.setString(4, korisnicko_ime);
                statement.setString(5, lozinka);
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Admin uspešno dodat u bazu.");

                    ResultSet rs = statement.getGeneratedKeys();
                    if (rs.next()) {
                        int newID = rs.getInt(1);
                        System.out.println("Novi ID admina: " + newID);


                        new Admin(newID, ime, prezime,korisnicko_ime, lozinka);
                    }
                } else {
                    System.out.println("Nije uspelo dodavanje admina u bazu.");
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

    public static void dodajSmjestajDB(int id, String naziv, String broj_zvjezdica, String vrsta_sobe,String cjena_po_nocenju) {
        dr.startConnection();

        try {
            String query = "INSERT INTO Smjestaj(id, naziv, broj_zvjezdica, vrsta_sobe, cjena_po_nocenju) VALUES (?,?,?,?,?)";
            try (PreparedStatement statement = dr.getConn().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, id);
                statement.setString(2, naziv);
                statement.setString(3, broj_zvjezdica);
                statement.setString(4, vrsta_sobe);
                statement.setString(5, cjena_po_nocenju);
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Smjestaj uspešno dodat u bazu.");


                    ResultSet rs = statement.getGeneratedKeys();
                    if (rs.next()) {
                        int newID = rs.getInt(1);
                        System.out.println("Novi ID smjestaja: " + newID);


                        new Smjestaj(newID, naziv, broj_zvjezdica,vrsta_sobe, cjena_po_nocenju);
                    }
                } else {
                    System.out.println("Nije uspelo dodavanje smjestaja u bazu.");
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

    public static void dodajBankovniRacunDB(int id, String broj_racuna, String jmbg, Double stanje) {
        dr.startConnection();

        try {
            String query = "INSERT INTO Klijent(id, broj_racuna, jmbg, stanje) VALUES (?,?,?,?)";
            try (PreparedStatement statement = dr.getConn().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, id);
                statement.setString(2, broj_racuna);
                statement.setString(3, jmbg);
                statement.setDouble(4, stanje);
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Bankovni racun uspešno dodat u bazu.");


                    ResultSet rs = statement.getGeneratedKeys();
                    if (rs.next()) {
                        int newID = rs.getInt(1);
                        System.out.println("Novi ID bankovnog racuna: " + newID);


                        new Bankovni_racun(newID, broj_racuna, jmbg,stanje);
                    }
                } else {
                    System.out.println("Nije uspelo dodavanje bankovnoh racuna u bazu.");
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

    public static void dodajAranzmanDB(int id, String naziv_putovanja, String destinacija, String prevoz, Date datum_polaska, Date datum_dolaska, String cijena_aranzmana, int Smjestaj_id) {
        dr.startConnection();

        try {
            String query = "INSERT INTO Aranzman(id, naziv_putovanja, destinacija, prevoz,datum_polaska,datum_dolaska,cijena_aranzmana,Smjestaj_id) VALUES (?,?,?,?,?,?,?,?)";
            try (PreparedStatement statement = dr.getConn().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, id);
                statement.setString(2, naziv_putovanja);
                statement.setString(3, destinacija);
                statement.setString(4, prevoz);
                statement.setDate(5, (java.sql.Date) datum_polaska);
                statement.setDate(6, (java.sql.Date) datum_dolaska);
                statement.setString(7, cijena_aranzmana);
                statement.setInt(8, Smjestaj_id);
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Aranzman uspešno dodat u bazu.");


                    ResultSet rs = statement.getGeneratedKeys();
                    if (rs.next()) {
                        int newID = rs.getInt(1);
                        System.out.println("Novi ID Aranzmana racuna: " + newID);
                        String stringNewID = Integer.toString(newID);


                        new Aranzman(stringNewID, naziv_putovanja, destinacija,prevoz,datum_polaska,datum_dolaska,cijena_aranzmana,Smjestaj_id);
                    }
                } else {
                    System.out.println("Nije uspelo dodavanje Aranzmana u bazu.");
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

    public static void dodajIzletDB(int id, String naziv_izleta, String destinacija, Date datum_polaska, String cijena_izleta) {
        dr.startConnection();

        try {
            String query = "INSERT INTO Aranzman(id, naziv_putovanja, destinacija, prevoz, datum_polaska, datum_dolaska, cijena_aranzmana, Smjestaj_id) VALUES (?,?,?,?,?,?,?,?)";
            try (PreparedStatement statement = dr.getConn().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, id);
                statement.setString(2, naziv_izleta);
                statement.setString(3, destinacija);
                statement.setNull(4, Types.VARCHAR); // prevoz
                statement.setDate(5, (java.sql.Date) datum_polaska);
                statement.setNull(6, Types.DATE); // datum_dolaska
                statement.setString(7, cijena_izleta);
                statement.setNull(8, Types.INTEGER); // Smjestaj_id

                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Izlet uspešno dodat u bazu.");


                    ResultSet rs = statement.getGeneratedKeys();
                    if (rs.next()) {
                        int newID = rs.getInt(1);
                        System.out.println("Novi ID Izleta racuna: " + newID);
                        String stringNewID = Integer.toString(newID);


                        new Aranzman(stringNewID, naziv_izleta, destinacija, null, datum_polaska, null, cijena_izleta, null);
                    }
                } else {
                    System.out.println("Nije uspelo dodavanje Izleta u bazu.");
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



    public static void dodajRezervacijuDB(int Klijent_id, String Aranzman_id, String ukupna_cijena, String placena_cijena) {
        dr.startConnection();

        try {
            String query = "INSERT INTO Rezervacija(Klijent_id, Aranzman_id, ukupna_cijena, placena_cijena) VALUES (?,?,?,?)";
            try (PreparedStatement statement = dr.getConn().prepareStatement(query)) {
                statement.setInt(1, Klijent_id);
                statement.setString(2, Aranzman_id);
                statement.setString(3, ukupna_cijena);
                statement.setString(4, placena_cijena);

                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Rezervacija uspešno dodata u bazu.");
                } else {
                    System.out.println("Nije uspelo dodavanje rezervacije u bazu.");
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

    public static void obrisiRezervacijuDB(int klijentId, String aranzmanId) {




        String rezervacijeFileName = "del_rez.txt";

        try {
            File rezervacijeFile = new File(rezervacijeFileName);
            if (!rezervacijeFile.exists()) {
                rezervacijeFile.createNewFile();
                System.out.println("Napravljeno za rezervacije");
            }


            boolean rezervacijeVecUpisane = false;
            try (Scanner scanner = new Scanner(rezervacijeFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();

                    if (line.contains(aranzmanId)) {
                        rezervacijeVecUpisane = true;
                        break;
                    }
                }
            }

            if (!rezervacijeVecUpisane) {
                try (FileWriter rezervacijeFileWriter = new FileWriter(rezervacijeFileName, true)) {

                        Rezervacija rez = Rezervacija.getRez(klijentId, aranzmanId);
                        rezervacijeFileWriter.write(rez + "\n");


                    System.out.println("Ispisano za rezervacije");
                }
            } else {
                System.out.println("Rezervacije već upisane za brisanje");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        dr.startConnection();


        try {
            String query = "DELETE FROM Rezervacija WHERE Klijent_id = ? AND Aranzman_id = ?";
            try (PreparedStatement statement = dr.getConn().prepareStatement(query)) {
                statement.setInt(1, klijentId);
                statement.setString(2, aranzmanId);

                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Rezervacija uspešno obrisana iz baze.");
                } else {
                    System.out.println("Nije uspelo brisanje rezervacije iz baze.");
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

    public static String getHashValue(String password) {
        StringBuilder sb = new StringBuilder();


        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            byte[] bytesOfPassword = password.getBytes(StandardCharsets.UTF_8);
            byte[] hash = md5.digest(bytesOfPassword);

            // Convert hash into HEX value
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    public static void otkaziPutovanje(String Aranzman_id) {


        String aranzmaniFileName = "del_ar.txt";

        try {
            File aranzmaniFile = new File(aranzmaniFileName);
            if (!aranzmaniFile.exists()) {
                aranzmaniFile.createNewFile();
                System.out.println("Napravljen novi fajl za aranžmane");
            }


            boolean aranzmanVecUpisan = false;
            try (Scanner scanner = new Scanner(aranzmaniFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains(Aranzman_id)) {
                        aranzmanVecUpisan = true;
                        break;
                    }
                }
            }

            if (!aranzmanVecUpisan) {
                try (FileWriter aranzmaniFileWriter = new FileWriter(aranzmaniFileName, true)) {
                    Aranzman ar = Aranzman.getAranzmanById(Aranzman_id);
                    aranzmaniFileWriter.write(ar + "\n");
                    System.out.println("Ispisan aranžman za brisanje");
                }
            } else {
                System.out.println("Aranžman već upisan za brisanje");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        String rezervacijeFileName = "del_rez.txt";

        try {
            File rezervacijeFile = new File(rezervacijeFileName);
            if (!rezervacijeFile.exists()) {
                rezervacijeFile.createNewFile();
                System.out.println("Napravljeno za rezervacije");
            }


            boolean rezervacijeVecUpisane = false;
            try (Scanner scanner = new Scanner(rezervacijeFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();

                    if (line.contains(Aranzman_id)) {
                        rezervacijeVecUpisane = true;
                        break;
                    }
                }
            }

            if (!rezervacijeVecUpisane) {
                try (FileWriter rezervacijeFileWriter = new FileWriter(rezervacijeFileName, true)) {
                    ArrayList<Klijent> sviKl = Klijent.getSviKlijenti();
                    for (Klijent kl : sviKl) {
                        Rezervacija rez = Rezervacija.getRez(kl.getKlijentID(), Aranzman_id);
                        rezervacijeFileWriter.write(rez + "\n");
                    }

                    System.out.println("Ispisano za rezervacije");
                }
            } else {
                System.out.println("Rezervacije već upisane za brisanje");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Klijent> sviKL = Klijent.getSviKlijenti();
        for (Klijent kl :sviKL){
            Rezervacija rez = Rezervacija.getRez(kl.getKlijentID(),Aranzman_id);
            if(rez!=null){
                obrisiRezervacijuDB(kl.getKlijentID(),Aranzman_id);
            }

        }


        dr.startConnection();

        try {
            String query = "DELETE FROM Aranzman WHERE id = ?";
            try (PreparedStatement statement = dr.getConn().prepareStatement(query)) {
                statement.setString(1, Aranzman_id);

                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Putovanje uspešno otkazano iz baze.");

                } else {
                    System.out.println("Nije uspelo otkazivanje putovanja iz baze.");
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

    public static void azurirajPlacenuCijenuRezervacije(int klijentId, String aranzmanId, String novaPlacenaCijena) {
        dr.startConnection();

        try {
            String query = "UPDATE Rezervacija SET placena_cijena = ? WHERE Klijent_id = ? AND Aranzman_id = ?";
            try (PreparedStatement statement = dr.getConn().prepareStatement(query)) {
                statement.setString(1, novaPlacenaCijena);
                statement.setInt(2, klijentId);
                statement.setString(3, aranzmanId);

                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Plaćena cijena rezervacije uspešno ažurirana.");
                } else {
                    System.out.println("Nije pronađena rezervacija sa datim Klijent_id i Aranzman_id.");
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


