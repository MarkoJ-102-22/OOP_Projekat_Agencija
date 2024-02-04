package com.example.agencija.view;

import com.example.agencija.model.*;
import com.example.agencija.sql.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.example.agencija.model.Aranzman.getSviAranzmani;
import static com.example.agencija.model.Rezervacija.getSveRezervacije;
import static com.example.agencija.sql.DBUtils.obrisiRezervacijuDB;
import static com.example.agencija.sql.DBUtils.otkaziPutovanje;

public class KlijentGUI extends Kontroler implements Initializable {

    public TextArea infoTextArea;
    public PasswordField infoStaraTextBox, infoNovaTextBox;


    public TextField aranzmaniCijenaOd, aranzmaniCijenaDo,aranzmaniDestinacija ;
    public ListView<String> aranzmanilistaAranzmana;
    public DatePicker aranzmaniDatumKretanja, aranzmaniDatumPovratka;
    public ComboBox<String> aranzmaniTipPutovanja,aranzmaniNacinPrevoza,aranzmaniVrstaSobe,aranzmaniBrZvjezdica;



    public ListView<String> rezervacijeListaOtkazanih, rezervacijeListaProslig, rezervacijeListaAktivnih;
    public Label ostaloID,potrosenoID;
    public TextField kolicina_uplate;
    public PasswordField rezervacijePotvrdaSifre;


    public ListView<String>brisanjeListaRezervacija;
    public PasswordField brisanjeSifraPotvrda;

    Klijent klijent;

    @FXML
    private Tab brisanje;
    @FXML
    private Tab rezervacije;

    public void initialize(URL location, ResourceBundle resources) {

        aranzmaniTipPutovanja.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals("Putovanje")) {

                    aranzmaniBrZvjezdica.setDisable(false);
                    aranzmaniVrstaSobe.setDisable(false);
                    aranzmaniNacinPrevoza.setDisable(false);
                } else if (newValue.equals("Izlet")) {
                    aranzmaniBrZvjezdica.setDisable(false);
                    aranzmaniVrstaSobe.setDisable(false);
                    aranzmaniNacinPrevoza.setDisable(false);
                } else {
                    aranzmaniBrZvjezdica.setDisable(false);
                    aranzmaniVrstaSobe.setDisable(false);
                    aranzmaniNacinPrevoza.setDisable(false);
                }
            }
            else{
                aranzmaniBrZvjezdica.setDisable(false);
                aranzmaniVrstaSobe.setDisable(false);
                aranzmaniNacinPrevoza.setDisable(false);
            }
        });


        aranzmaniTipPutovanja.getItems().add("");
        aranzmaniTipPutovanja.getItems().add("Izlet");
        aranzmaniTipPutovanja.getItems().add("Putovanje");

        aranzmaniBrZvjezdica.getItems().add("");
        aranzmaniBrZvjezdica.getItems().add("Tri");
        aranzmaniBrZvjezdica.getItems().add("Cetiri");
        aranzmaniBrZvjezdica.getItems().add("Pet");

        aranzmaniVrstaSobe.getItems().add("");
        aranzmaniVrstaSobe.getItems().add("Jednokrevetna");
        aranzmaniVrstaSobe.getItems().add("Dvokrevetna");
        aranzmaniVrstaSobe.getItems().add("Trokrevetna");

        aranzmaniNacinPrevoza.getItems().add("");
        aranzmaniNacinPrevoza.getItems().add("Avion");
        aranzmaniNacinPrevoza.getItems().add("Autobus");
        aranzmaniNacinPrevoza.getItems().add("Samostalan");



        brisanje.setOnSelectionChanged(event -> {
            if(brisanje.isSelected()){
                prikaziListuRezervacija();
            }
        });


        rezervacije.setOnSelectionChanged(event -> {
            if(rezervacije.isSelected()){
                prikaziListeRezervacija();
            }
        });

    }


    public void setKlijent(Klijent klijent) {
        this.klijent = klijent;


            infoTextArea.appendText("" + klijent.getKlijentID()+" - ");
            infoTextArea.appendText("Ime i prezime:\n");
            infoTextArea.appendText("\t" + klijent.getIme() + " " + klijent.getPrezime() + "\n\n");
            infoTextArea.appendText("Korisničko ime:\n");
            infoTextArea.appendText("\t" + klijent.getKorisnickoIme() + "\n\n");
            infoTextArea.appendText("Broj telefona:\n");
            infoTextArea.appendText("\t" + klijent.getBroj_telefona() + "\n\n");
            infoTextArea.appendText("JMBG:\n");
            infoTextArea.appendText("\t" + klijent.getJmbg() + "\n\n");
            infoTextArea.appendText("Broj bankovnog racuna:\n");
            infoTextArea.appendText("\t" + klijent.getBroj_racuna() + "\n\n");

        ArrayList<Rezervacija> sveRez = Rezervacija.getSveRezervacije();
        for (Rezervacija rez : sveRez) {
            if (rez.getKlijentID() == klijent.getKlijentID()) {
                Aranzman ar = Aranzman.getAranzmanById(rez.getAranzmanID());
                Date trenutno = new Date();
                Long razlika = TimeUnit.DAYS.convert(trenutno.getTime() - ar.getDatumPolaska().getTime(), TimeUnit.MILLISECONDS);
                if (razlika > 0 && razlika < 3) {
                    prozorObavjestenja("REZERVACIJA OBAVJESTENJE", "OSTALO VAM JE MANJE OD 3 DANA DA UPLATITE REZERVACIJU");
                }
            }
        }

    }

    public void odjavaDugme(ActionEvent event) {
        promijeniScenuLogin(event);
    }


    public void promjeniLozinku() {
        if (infoStaraTextBox.getText().isEmpty()) {
            prozorObavjestenja("Greška", "Polje za staru lozinku je prazno!");
        } else if (infoNovaTextBox.getText().isEmpty()) {
            prozorObavjestenja("Greška", "Polje za novu lozinku je prazno!");
        } else {

            String lozinka = klijent.getSifra();
            String staraLozinka = infoStaraTextBox.getText();
            if (lozinka.equals(staraLozinka)) {
                String novaLozinka = infoNovaTextBox.getText();
                if (novaLozinka.length() < 5) {
                    prozorObavjestenja("Greška", "Lozinka mora biti duža od 5 karaktera!");
                } else {
                    String novaLozinkaHes = novaLozinka;
                    klijent.setKlijentSifra(novaLozinkaHes);
                    int klijentID= klijent.getKlijentID();
                    DBUtils.promjenaSifreKlijenta(klijentID,novaLozinkaHes);
                    prozorObavjestenja("Gotovo", "Lozinka uspješno promijenjena!");
                    infoStaraTextBox.clear();
                    infoNovaTextBox.clear();
                }
            } else {
                prozorObavjestenja("Greška", "Pogrešna stara lozinka!");
            }
        }
    }


    public void filtriraj(ActionEvent event) {

        double cijenaOd = 0.0;
        double cijenaDo = 0.0;
        String destinacija = "";
        LocalDate datumKretanja = null;
        LocalDate datumPovratka = null;
        String brojZvjezdica = "";
        String vrstaSobe = "";
        String nacinPutovanja = "";
        String tipPutovanja = "";
        Date datumKretanja1=null;
        Date datumPovratka1=null;

        tipPutovanja=aranzmaniTipPutovanja.getValue();
        if (tipPutovanja != null) {
            if (tipPutovanja.equals("Putovanje")) {
                try {
                    String cijenaOdText = aranzmaniCijenaOd.getText();
                    if (cijenaOdText != null && !cijenaOdText.isEmpty()) {
                        cijenaOd = Double.parseDouble(cijenaOdText);
                    }

                    String cijenaDoText = aranzmaniCijenaDo.getText();
                    if (cijenaDoText != null && !cijenaDoText.isEmpty()) {
                        cijenaDo = Double.parseDouble(cijenaDoText);
                    }

                    destinacija = aranzmaniDestinacija.getText();


                    datumKretanja = aranzmaniDatumKretanja.getValue();
                    datumPovratka = aranzmaniDatumPovratka.getValue();

                    if (datumKretanja != null) {
                        datumKretanja1 = java.sql.Date.valueOf(datumKretanja);
                    }
                    if (datumPovratka != null) {
                        datumPovratka1 = java.sql.Date.valueOf(datumPovratka);
                    }


                    brojZvjezdica = aranzmaniBrZvjezdica.getValue();
                    vrstaSobe = aranzmaniVrstaSobe.getValue();
                    nacinPutovanja = aranzmaniNacinPrevoza.getValue();



                    List<String> filtriraniAranzmani = new ArrayList<>();

                    for (Aranzman aranzman : Aranzman.sviAranzmani) {


                        Smjestaj smjestaj = Smjestaj.getSmjestajById(aranzman.getSmjestajId());

                        if ((destinacija == null || aranzman.getDestinacija().contains(destinacija))
                                && (cijenaOd == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) >= cijenaOd)
                                && (cijenaDo == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) <= cijenaDo)

                                && (datumKretanja == null || aranzman.getDatumPolaska().equals(datumKretanja1) || aranzman.getDatumPolaska().after(datumKretanja1))
                                && (datumPovratka == null || aranzman.getDatumDolaska().equals(datumPovratka1) || aranzman.getDatumDolaska().before(datumPovratka1))
                                && (brojZvjezdica == null || smjestaj.getBrojZvjezdica().equals(brojZvjezdica))
                                && (vrstaSobe == null || smjestaj.getVrstaSobe().equals(vrstaSobe))
                                && (nacinPutovanja == null || aranzman.getPrevoz().equals(nacinPutovanja)) && (aranzman.getDatumDolaska() != null)) {
                            String listraFiltriranihAranzmana = ""+ aranzman.getId() + " - " + aranzman.getNazivPutovanja() + " - " + aranzman.getCijenaAranzmana();
                            filtriraniAranzmani.add(listraFiltriranihAranzmana);
                        }

                    }
                    System.out.println(tipPutovanja);
                    System.out.println(nacinPutovanja);
                    System.out.println(vrstaSobe);
                    System.out.println(brojZvjezdica);
                    System.out.println(datumPovratka);
                    System.out.println(datumKretanja);
                    System.out.println(destinacija);
                    System.out.println(cijenaDo);
                    System.out.println(cijenaOd);

                    aranzmanilistaAranzmana.getItems().clear();
                    System.out.println(filtriraniAranzmani);
                    aranzmanilistaAranzmana.getItems().addAll(filtriraniAranzmani);
                } catch (NumberFormatException e) {

                    e.printStackTrace();
                }
            } else if (tipPutovanja.equals("Izlet")) {
                try {
                    String cijenaOdText = aranzmaniCijenaOd.getText();
                    if (cijenaOdText != null && !cijenaOdText.isEmpty()) {
                        cijenaOd = Double.parseDouble(cijenaOdText);
                    }

                    String cijenaDoText = aranzmaniCijenaDo.getText();
                    if (cijenaDoText != null && !cijenaDoText.isEmpty()) {
                        cijenaDo = Double.parseDouble(cijenaDoText);
                    }

                    destinacija = aranzmaniDestinacija.getText();

                    datumKretanja = aranzmaniDatumKretanja.getValue();
                    datumPovratka = aranzmaniDatumPovratka.getValue();

                    if (datumKretanja != null) {
                        datumKretanja1 = java.sql.Date.valueOf(datumKretanja);
                    }
                    if (datumPovratka != null) {
                        datumPovratka1 = java.sql.Date.valueOf(datumPovratka);
                    }


                    List<String> filtriraniAranzmani = new ArrayList<>();

                    for (Aranzman aranzman : Aranzman.sviAranzmani) {

                        Smjestaj smjestaj = Smjestaj.getSmjestajById(aranzman.getSmjestajId());

                        if ((destinacija == null || aranzman.getDestinacija().contains(destinacija))
                                && (cijenaOd == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) >= cijenaOd)
                                && (cijenaDo == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) <= cijenaDo)
                                && (datumKretanja == null || aranzman.getDatumPolaska().equals(datumKretanja1) || aranzman.getDatumPolaska().after(datumKretanja1))
                                && (datumPovratka == null || aranzman.getDatumPolaska().equals(datumPovratka1) || aranzman.getDatumPolaska().before(datumPovratka1))
                                && (aranzman.getDatumDolaska() == null)) {
                            String listraFiltriranihAranzmana = ""+ aranzman.getId() + " - " + aranzman.getNazivPutovanja() + " - " + aranzman.getCijenaAranzmana();
                            filtriraniAranzmani.add(listraFiltriranihAranzmana);
                        }

                    }
                    System.out.println(tipPutovanja);
                    System.out.println(nacinPutovanja);
                    System.out.println(vrstaSobe);
                    System.out.println(brojZvjezdica);
                    System.out.println(datumPovratka);
                    System.out.println(datumKretanja);
                    System.out.println(destinacija);
                    System.out.println(cijenaDo);
                    System.out.println(cijenaOd);


                    aranzmanilistaAranzmana.getItems().clear();
                    System.out.println(filtriraniAranzmani);
                    aranzmanilistaAranzmana.getItems().addAll(filtriraniAranzmani);
                } catch (NumberFormatException e) {

                    e.printStackTrace();
                }

            } else {
                try {
                    String cijenaOdText = aranzmaniCijenaOd.getText();
                    if (cijenaOdText != null && !cijenaOdText.isEmpty()) {
                        cijenaOd = Double.parseDouble(cijenaOdText);
                    }

                    String cijenaDoText = aranzmaniCijenaDo.getText();
                    if (cijenaDoText != null && !cijenaDoText.isEmpty()) {
                        cijenaDo = Double.parseDouble(cijenaDoText);
                    }

                    destinacija = aranzmaniDestinacija.getText();

                    datumKretanja = aranzmaniDatumKretanja.getValue();
                    datumPovratka = aranzmaniDatumPovratka.getValue();

                    if (datumKretanja != null) {
                        datumKretanja1 = java.sql.Date.valueOf(datumKretanja);
                    }
                    if (datumPovratka != null) {
                        datumPovratka1 = java.sql.Date.valueOf(datumPovratka);
                    }


                    brojZvjezdica = aranzmaniBrZvjezdica.getValue();
                    vrstaSobe = aranzmaniVrstaSobe.getValue();
                    nacinPutovanja = aranzmaniNacinPrevoza.getValue();


                    List<String> filtriraniAranzmani = new ArrayList<>();

                    for (Aranzman aranzman : Aranzman.sviAranzmani) {

                        Smjestaj smjestaj = Smjestaj.getSmjestajById(aranzman.getSmjestajId());

                        if ((destinacija == null || aranzman.getDestinacija().contains(destinacija))
                                && (cijenaOd == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) >= cijenaOd)
                                && (cijenaDo == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) <= cijenaDo)
                                && (datumKretanja == null || aranzman.getDatumPolaska().equals(datumKretanja1) || aranzman.getDatumPolaska().after(datumKretanja1))
                                && (datumPovratka == null || aranzman.getDatumDolaska().equals(datumPovratka1) || aranzman.getDatumDolaska().before(datumPovratka1))
                                && (brojZvjezdica == null || smjestaj.getBrojZvjezdica().equals(brojZvjezdica))
                                && (vrstaSobe == null || smjestaj.getVrstaSobe().equals(vrstaSobe))
                                && (nacinPutovanja == null || aranzman.getPrevoz().equals(nacinPutovanja))) {
                            String listraFiltriranihAranzmana = ""+ aranzman.getId() + " - " + aranzman.getNazivPutovanja() + " - " + aranzman.getCijenaAranzmana();
                            filtriraniAranzmani.add(listraFiltriranihAranzmana);
                        }

                    }

                    System.out.println(tipPutovanja);
                    System.out.println(nacinPutovanja);
                    System.out.println(vrstaSobe);
                    System.out.println(brojZvjezdica);
                    System.out.println(datumPovratka);
                    System.out.println(datumKretanja);
                    System.out.println(destinacija);
                    System.out.println(cijenaDo);
                    System.out.println(cijenaOd);

                    aranzmanilistaAranzmana.getItems().clear();
                    System.out.println(filtriraniAranzmani);
                    aranzmanilistaAranzmana.getItems().addAll(filtriraniAranzmani);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            try {
                String cijenaOdText = aranzmaniCijenaOd.getText();
                if (cijenaOdText != null && !cijenaOdText.isEmpty()) {
                    cijenaOd = Double.parseDouble(cijenaOdText);
                }

                String cijenaDoText = aranzmaniCijenaDo.getText();
                if (cijenaDoText != null && !cijenaDoText.isEmpty()) {
                    cijenaDo = Double.parseDouble(cijenaDoText);
                }

                destinacija = aranzmaniDestinacija.getText();

                datumKretanja = aranzmaniDatumKretanja.getValue();
                datumPovratka = aranzmaniDatumPovratka.getValue();

                if (datumKretanja != null) {
                    datumKretanja1 = java.sql.Date.valueOf(datumKretanja);
                }
                if (datumPovratka != null) {
                    datumPovratka1 = java.sql.Date.valueOf(datumPovratka);
                }

                brojZvjezdica = aranzmaniBrZvjezdica.getValue();
                vrstaSobe = aranzmaniVrstaSobe.getValue();
                nacinPutovanja = aranzmaniNacinPrevoza.getValue();

                List<String> filtriraniAranzmani = new ArrayList<>();

                for (Aranzman aranzman : Aranzman.sviAranzmani) {

                    Smjestaj smjestaj = Smjestaj.getSmjestajById(aranzman.getSmjestajId());

                    if ((destinacija == null || aranzman.getDestinacija().contains(destinacija))
                            && (cijenaOd == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) >= cijenaOd)
                            && (cijenaDo == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) <= cijenaDo)
                            && (datumKretanja == null || aranzman.getDatumPolaska().equals(datumKretanja1) || aranzman.getDatumPolaska().after(datumKretanja1))
                            && (datumPovratka == null || aranzman.getDatumDolaska().equals(datumPovratka1) || aranzman.getDatumDolaska().before(datumPovratka1))
                            && (brojZvjezdica == null || smjestaj.getBrojZvjezdica().equals(brojZvjezdica))
                            && (vrstaSobe == null || smjestaj.getVrstaSobe().equals(vrstaSobe))
                            && (nacinPutovanja == null || aranzman.getPrevoz().equals(nacinPutovanja))) {
                        String listraFiltriranihAranzmana = ""+ aranzman.getId() + " - " + aranzman.getNazivPutovanja() + " - " + aranzman.getCijenaAranzmana();
                        filtriraniAranzmani.add(listraFiltriranihAranzmana);
                    }

                }

                System.out.println(tipPutovanja);
                System.out.println(nacinPutovanja);
                System.out.println(vrstaSobe);
                System.out.println(brojZvjezdica);
                System.out.println(datumPovratka);
                System.out.println(datumKretanja);
                System.out.println(destinacija);
                System.out.println(cijenaDo);
                System.out.println(cijenaOd);


                aranzmanilistaAranzmana.getItems().clear();
                System.out.println(filtriraniAranzmani);
                aranzmanilistaAranzmana.getItems().addAll(filtriraniAranzmani);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }



    }


    public void prikaziProzorPotvrde(String tekst) {
        Alert potvrdaAlert = new Alert(Alert.AlertType.CONFIRMATION);
        potvrdaAlert.setTitle("Rezervacija");
        potvrdaAlert.setHeaderText(null);
        String[] dijelovi = tekst.split(" - ");

        String id = dijelovi[0];
        Aranzman aranzman = Aranzman.getAranzmanById(id);
        Smjestaj smjestaj = null;
        StringBuilder sb = new StringBuilder();
        String cijenaSmjestaja = null;
        if (aranzman.getDatumDolaska()==null){

            sb.append("ID: ").append(id).append("\n");
            sb.append("Naziv putovanja: ").append(aranzman.getNazivPutovanja()).append("\n");
            sb.append("Destinacija: ").append(aranzman.getDestinacija()).append("\n");
            sb.append("Datum polaska: ").append(aranzman.getDatumPolaska()).append("\n");
            sb.append("Cijena aranžmana: ").append(aranzman.getCijenaAranzmana()).append("\n");
            cijenaSmjestaja=aranzman.getCijenaAranzmana();

        }
        else{

            try {
                int broj = Integer.parseInt(id);
                System.out.println("Pretvoreni broj: " + broj);
                 smjestaj =Smjestaj.getSmjestajById(broj);
            } catch (NumberFormatException e) {
                System.out.println("Nije moguće pretvoriti String u int.");
            }

            sb.append("ID: ").append(id).append(" - ");
            sb.append("Naziv putovanja: ").append(aranzman.getNazivPutovanja()).append(" - ");
            sb.append("Destinacija: ").append(aranzman.getDestinacija()).append(" - ");
            sb.append("Prevoz: ").append(aranzman.getPrevoz()).append(" \n ");
            sb.append("Datum polaska: ").append(aranzman.getDatumPolaska()).append(" - ");
            sb.append("Datum dolaska: ").append(aranzman.getDatumDolaska()).append(" \n ");
            sb.append("Cijena aranžmana: ").append(aranzman.getCijenaAranzmana()).append(" \n ");
            sb.append("Smještaj ime: ").append(smjestaj.getSmjestajIme()).append(" - ");
            sb.append("Smještaj cijena po nocenju: ").append(smjestaj.getCijenaPoNocenju()).append(" - ");
            sb.append("Smještaj vrsta sobe: ").append(smjestaj.getVrstaSobe()).append(" - ");
            sb.append("Smještaj br. zvjezdica: ").append(smjestaj.getBrojZvjezdica()).append("\n");
            cijenaSmjestaja=aranzman.getCijenaAranzmana();

        }
        tekst= sb.toString();
        potvrdaAlert.setContentText(tekst);
        String klientInfo = infoTextArea.getText();
        String[] dijelovii = klientInfo.split(" - ");

        String klientIdString = dijelovii[0];
        int klijentId=0;
        try {
            klijentId= Integer.parseInt(klientIdString);
            System.out.println("Pretvoreni broj: " + klijentId);
        } catch (NumberFormatException e) {
            System.out.println("Nije moguće pretvoriti String u int.");
        }
        String placenoPola=null;
        double placenoPolaInt=0;
        double cijenaSmjestajaInt=0;
        try {
            cijenaSmjestajaInt= Double.parseDouble(cijenaSmjestaja);
            System.out.println("Pretvoreni broj: " + cijenaSmjestajaInt);
        } catch (NumberFormatException e) {
            System.out.println("Nije moguće pretvoriti String u int.");
        }
        placenoPolaInt=cijenaSmjestajaInt/2.0;



        ButtonType potvrdiBtn = new ButtonType("Potvrdi", ButtonBar.ButtonData.OK_DONE);
        ButtonType odbaciBtn = new ButtonType("Odbaci", ButtonBar.ButtonData.CANCEL_CLOSE);

        potvrdaAlert.getButtonTypes().setAll(potvrdiBtn, odbaciBtn);

        Optional<ButtonType> rezultat = potvrdaAlert.showAndWait();


        if (rezultat.isPresent() && rezultat.get() == potvrdiBtn) {



            String textAreaContent = infoTextArea.getText();

            String[] lines = textAreaContent.split("\n");

            String lastLine = lines[lines.length - 1];

            String[] parts = lastLine.split("\t");

            String brojRacuna = parts[parts.length - 1].trim();
            Bankovni_racun bankRacun = Bankovni_racun.getRacunByBrojRacuna(brojRacuna);
            Double stanje = bankRacun.getStanje();
            if (stanje<placenoPolaInt){
                prozorObavjestenja("Greška", "Nedovoljno novca na racunu!!!");
            }
            else{
                String agencijaJmbg="1234567887654321";
                Bankovni_racun agencija = Bankovni_racun.getRacunByBrojRacuna(agencijaJmbg);

                ArrayList<Rezervacija> sveRezervacije = getSveRezervacije();
                for (Rezervacija rezervacija : sveRezervacije){
                    if (klijentId == rezervacija.getKlijentID() && rezervacija.getAranzmanID().equals(id)){
                        prozorObavjestenja("Obavjestenje", "Klijent je vec dodao ovu rezervaciju!!!");
                    }
                }

                agencija.vratiNovacAgenciji(placenoPolaInt);
                bankRacun.skiniNovacKlijentu(klijentId,placenoPolaInt);
                DBUtils.dodajRezervacijuDB(klijentId, id, cijenaSmjestaja, String.valueOf(placenoPolaInt));

                System.out.println("Rezervacija potvrđena!");
            }


        } else {

            System.out.println("Rezervacija odbijena ili prozor zatvoren.");
        }

    }

    public void prikaziProzorPotvrdeBtn(ActionEvent event) {
        String selektovaniAranzman = aranzmanilistaAranzmana.getSelectionModel().getSelectedItem();
        prikaziProzorPotvrde(selektovaniAranzman);

    }

    public void prikaziListuRezervacija(){
        ArrayList<Rezervacija> sverezervacije = getSveRezervacije();


        ArrayList<String> listaRezervacija = new ArrayList<>();
        for (Rezervacija rezervacija : sverezervacije) {
            Klijent kl = this.klijent;
            if (rezervacija.getKlijentID() == kl.getKlijentID())
            {
                String rezervacijaString = rezervacija.getKlijentID()+ " - " + rezervacija.getAranzmanID() +" - "+ rezervacija.getUkupnaCijena() + " - " + rezervacija.getPlacenaCijena();
                listaRezervacija.add(rezervacijaString);
            }

        }


        brisanjeListaRezervacija.setItems(FXCollections.observableArrayList(listaRezervacija));
    }

    @FXML
    private void obrisiRezervaciju(ActionEvent event) {
        String selektovanaRezervacija = brisanjeListaRezervacija.getSelectionModel().getSelectedItem();

        if (selektovanaRezervacija != null) {
            String[] dijelovi = selektovanaRezervacija.split(" - ");


            String klijentid = dijelovi[0].trim();
            String id = dijelovi[1].trim();

            int klijentidInt = Integer.parseInt(klijentid);
            Aranzman aranzman= Aranzman.getAranzmanById(id);

            Date datumPolaska = aranzman.getDatumPolaska();
            long datumpol = datumPolaska.getTime();



            LocalDate trenutniDatum = LocalDate.now();
            Instant instant = trenutniDatum.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Date convertedDate = Date.from(instant);
            long danas= convertedDate.getTime();
            long razlikaUMilisekundama = datumpol-danas;
            long razlikaUDanima = TimeUnit.DAYS.convert(razlikaUMilisekundama, TimeUnit.MILLISECONDS);

            if (razlikaUDanima < 14) {
                prozorObavjestenja("Greska!", "Nije moguce obrisati rezervaciju jer ima manje od 14 dana do polaska.");
                System.out.println("Aranžman polazi za manje od 14 dana.");
            } else {

                Klijent kl = Klijent.getKlijentID(klijentidInt);
                String sifraPotvrda = brisanjeSifraPotvrda.getText();
                String sifra = kl.getSifra();
                if (sifra.equals(sifra)){
                    String br = kl.getBroj_racuna();

                    Rezervacija rezervacija = Rezervacija.getRez(klijentidInt,id);
                    String placeno = rezervacija.getPlacenaCijena();
                    double placenoDouble = Double.parseDouble(placeno);

                    Bankovni_racun.vratiNovacKlijentu(klijentidInt,placenoDouble);
                    Bankovni_racun.skiniNovacAgenciji(placenoDouble);


                    DBUtils.obrisiRezervacijuDB(klijentidInt,id);
                    brisanjeListaRezervacija.getItems().remove(selektovanaRezervacija);
                    System.out.println("Aranžman polazi za 14 ili više dana.");
                }
                else {
                    prozorObavjestenja("Greska" ,"Pogresna sifra");
                }
            }


        } else {

            System.out.println("Molimo vas da selektujete aranžman za brisanje.");
        }
    }


    public void prikaziListeRezervacija(){
        rezervacijeListaOtkazanih.setDisable(false);
        rezervacijeListaProslig.setDisable(false);
        rezervacijeListaProslig.getItems().clear();
        rezervacijeListaOtkazanih.getItems().clear();
        ArrayList<Rezervacija> sveRez = Rezervacija.getSveRezervacije();
        ObservableList<String> otkazane = FXCollections.observableArrayList();
        ObservableList<String> protekle = FXCollections.observableArrayList();
        ObservableList<String> aktivne = FXCollections.observableArrayList();

        for (Rezervacija rez : sveRez){
            if (rez.getKlijentID() == this.klijent.getKlijentID()){
            Aranzman ar = Aranzman.getAranzmanById(rez.getAranzmanID());
            Date trenutno = new Date();

            Long razlika =TimeUnit.DAYS.convert( ar.getDatumPolaska().getTime() - trenutno.getTime(), TimeUnit.MILLISECONDS );
            if(razlika < 0){
            protekle.add(rez.toString());

            } else if (razlika < 14){
                if (! rez.getUkupnaCijena().equals(rez.getPlacenaCijena())){
                    obrisiRezervacijuDB(rez.getKlijentID(),rez.getAranzmanID());
                    Double ukupnoDouble = Double.parseDouble(rez.getUkupnaCijena());
                    Bankovni_racun.vratiNovacKlijentu(rez.getKlijentID(),ukupnoDouble/2);
                    Bankovni_racun.skiniNovacAgenciji(ukupnoDouble/2);

                }


            }
            else {
                aktivne.add(rez.toString());
            }


            }
        }

        String rezervacijeFileName = "del_rez.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(rezervacijeFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String split[] = line.split( " - ");
                System.out.println(split);
                System.out.println(split[0]);
                if (!split[0].equals("null")){
                    int klID = Integer.parseInt(split[0]);
                    System.out.println(this.klijent.getKlijentID() + " ==? " + split[0]);
                    if (this.klijent.getKlijentID() == klID){
                        otkazane.add(line);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        double ostalo=0;
        double placeno=0;



        rezervacijeListaOtkazanih.setItems(otkazane);
        rezervacijeListaProslig.setItems(protekle);
        rezervacijeListaAktivnih.setItems(aktivne);


        for (String red : rezervacijeListaOtkazanih.getItems()){
            String split[] = red.split(" - ");

            System.out.println(split[0] + " " + split[1] + " " + split[2] + " " + split[3] + " ");
            double ukupnaCijena = Double.parseDouble(split[2]);
            double placenaCijena = Double.parseDouble(split[3]);
            System.out.println(ukupnaCijena + " " + placenaCijena);
            placeno+=placenaCijena;
            ostalo+=(ukupnaCijena-placenaCijena);
        }
        for (String red : rezervacijeListaProslig.getItems()){
            String split[] = red.split(" - ");
            System.out.println(split[0] + " " + split[1] + " " + split[2] + " " + split[3] + " ");
            double ukupnaCijena = Double.parseDouble(split[2]);
            double placenaCijena = Double.parseDouble(split[3]);
            System.out.println(ukupnaCijena + " " + placenaCijena);
            placeno+=placenaCijena;
            ostalo+=(ukupnaCijena-placenaCijena);
        }
        for (String red : rezervacijeListaAktivnih.getItems()){
            String split[] = red.split(" - ");
            System.out.println(split[0] + " " + split[1] + " " + split[2] + " " + split[3] + " ");
            double ukupnaCijena = Double.parseDouble(split[2]);
            double placenaCijena = Double.parseDouble(split[3]);
            System.out.println(ukupnaCijena + " " + placenaCijena);
            placeno+=placenaCijena;
            ostalo+=(ukupnaCijena-placenaCijena);
        }


        ostaloID.setText(Double.toString(ostalo));
        potrosenoID.setText(Double.toString(placeno));
        rezervacijeListaOtkazanih.setDisable(true);
        rezervacijeListaProslig.setDisable(true);
    }

    public void potvrdaUplate(){
        if(kolicina_uplate.getText().isEmpty()){
            prozorObavjestenja("Greska kod Kolicine Uplate", "Polje za uplatu je prazno");
        }
        else {
            String uplate = kolicina_uplate.getText();
            double uplataDouble= 0;
            try {
                uplataDouble = Double.parseDouble(uplate);
                String sifra = rezervacijePotvrdaSifre.getText();

                String selektovanaRez = rezervacijeListaAktivnih.getSelectionModel().getSelectedItem();
                if (selektovanaRez == null){
                    prozorObavjestenja("Greska", "Nije izabrana rezervacija");
                }
                else {
                    if (!selektovanaRez.equals("null")){
                        String split[] = selektovanaRez.split(" - ");
                        String klijentid = split[0].trim();
                        String id = split[1].trim();


                        int klijentidInt = Integer.parseInt(klijentid);
                        Klijent klijent1= Klijent.getKlijentID(klijentidInt);
                        Aranzman aranzman= Aranzman.getAranzmanById(id);
                        Rezervacija rez = Rezervacija.getRez(klijentidInt,id);

                        if(sifra.equals(klijent1.getSifra())){

                            String doSadaUplaceno= rez.getPlacenaCijena();
                            double doSadaDouble = Double.parseDouble(doSadaUplaceno);
                            String ukupnaCijena =  rez.getUkupnaCijena();
                            double ukupnaCijenaDouble = Double.parseDouble(ukupnaCijena);

                            if(uplataDouble>(ukupnaCijenaDouble-doSadaDouble)){
                                prozorObavjestenja("Greska","Uplata je veca nego preostala za uplatu");
                                kolicina_uplate.clear();
                            }
                            else {
                                doSadaDouble+=uplataDouble;
                                String novaUplata= Double.toString(doSadaDouble);
                                rez.setPlacenaCijena(novaUplata);

                                Bankovni_racun.skiniNovacKlijentu(klijentidInt,uplataDouble);
                                Bankovni_racun.vratiNovacAgenciji(uplataDouble);
                                DBUtils.azurirajPlacenuCijenuRezervacije(klijentidInt, id, novaUplata);



                                kolicina_uplate.clear();
                                rezervacijePotvrdaSifre.clear();

                            }


                        }else {
                            prozorObavjestenja("Greska" ,"Pogresna sifra");
                        }

                    }
                }

            }catch (NumberFormatException e){
                prozorObavjestenja("Greska kod Kolicine Uplate", "Polje nije u obliku cifara");
            }

            prikaziListeRezervacija();
        }
    }

}
