package com.example.agencija.view;

import com.example.agencija.model.*;
import com.example.agencija.sql.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.example.agencija.model.Admin.getMaxAdminID;
import static com.example.agencija.model.Admin.getSviAdmini;
import static com.example.agencija.model.Aranzman.*;
import static com.example.agencija.model.Klijent.sviKlijenti;
import static com.example.agencija.sql.DBUtils.otkaziPutovanje;
import static com.example.agencija.model.Rezervacija.getSveRezervacije;



public class AdminGUI extends Kontroler implements Initializable{

    public TextArea infoTextArea;
    public PasswordField infoStaraTextBox, infoNovaTextBox;


    public TextField filterCijenaOd, filterCijenaDo,filterDestinacija ;
    public ListView<String> filterListaAranzmana;
    public DatePicker  filterDatumKretanja, filterDatumPovratka;
    public ComboBox<String> filterNacinPutovanja,filterVrstaSobe,filterBrojZvjezdica,filterTipPutovanja;


    @FXML
    private Tab tabRezervacije , brisanje, administratori;
    public TextArea rezervacijeListaKlijenata ;
    public ListView<String> rezervacijeLista;


    public TextField  dodajCijenaIzlet,dodajDestinacijaIzlet,dodajNazivIzlet,dodavanjeCijenePutovanja,dodavanjeDestinacijaPutovanja,dodavanjeNazivPutovanja,dodavanjeCijeneNocenjaPutovanja,dodavanjeNazivSmjestajaPutovanja;
    public DatePicker dodavanjeDatumIzleta,dodavanjeDatumPovratkaPutovanja,dodavanjeDatumPolaskaPutovanja ;
    public ComboBox<String> dodavanjeVrsteSobe,dodavanjeBrojZvjezdica,dodavanjeTipPrevoza ;


    public ListView<String> brisanjeListaAranzmana;


    public ListView<String> adminSpisakadmina;
    public TextField adminKorisnickoIme,adminPrezime,adminIme;

    Admin adminKorisnik;
    public Label viseRez,viseKlijenti,viseZarada,viseCekanje;


    @FXML
    private Tab vise;


    public void initialize(URL location, ResourceBundle resources) {

        // info tab




        // aranzmani
        filterTipPutovanja.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals("Putovanje")) {
                    // Onemogući određena polja
                    filterBrojZvjezdica.setDisable(false);
                    filterVrstaSobe.setDisable(false);
                    filterNacinPutovanja.setDisable(false);
                } else if (newValue.equals("Izlet")) {
                    // Onemogući određena polja
                    filterBrojZvjezdica.setDisable(true);
                    filterVrstaSobe.setDisable(true);
                    filterNacinPutovanja.setDisable(true);
                } else {
                    filterBrojZvjezdica.setDisable(false);
                    filterVrstaSobe.setDisable(false);
                    filterNacinPutovanja.setDisable(false);
                }

                    // Ako nije ni Putovanje ni Izlet, možete postaviti druge akcije po potrebi
                    // Ovdje možete postaviti određene poruke ili akcije
                }
            else{
                filterBrojZvjezdica.setDisable(false);
                filterVrstaSobe.setDisable(false);
                filterNacinPutovanja.setDisable(false);
            }
            });


        filterTipPutovanja.getItems().add("");
        filterTipPutovanja.getItems().add("Izlet");
        filterTipPutovanja.getItems().add("Putovanje");

        filterBrojZvjezdica.getItems().add("");
        filterBrojZvjezdica.getItems().add("Tri");
        filterBrojZvjezdica.getItems().add("Cetiri");
        filterBrojZvjezdica.getItems().add("Pet");

        filterVrstaSobe.getItems().add("");
        filterVrstaSobe.getItems().add("Jednokrevetna");
        filterVrstaSobe.getItems().add("Dvokrevetna");
        filterVrstaSobe.getItems().add("Trokrevetna");

        filterNacinPutovanja.getItems().add("");
        filterNacinPutovanja.getItems().add("Avion");
        filterNacinPutovanja.getItems().add("Autobus");
        filterNacinPutovanja.getItems().add("Samostalan");

        // rezervacije

        // Dodajte listener za otvaranje taba rezervacije
        tabRezervacije.setOnSelectionChanged(event -> {
            if (tabRezervacije.isSelected()) {
                // Pozovite metodu koja će prikazati spisak svih rezervacija
                prikaziRezervacije();
            }
        });






        // dodavanje
        dodavanjeTipPrevoza.getItems().add("Avion");
        dodavanjeTipPrevoza.getItems().add("Autobus");
        dodavanjeTipPrevoza.getItems().add("Samostalan");

        dodavanjeBrojZvjezdica.getItems().add("Dvije");
        dodavanjeBrojZvjezdica.getItems().add("Tri");
        dodavanjeBrojZvjezdica.getItems().add("Cetiri");
        dodavanjeBrojZvjezdica.getItems().add("Pet");

        dodavanjeVrsteSobe.getItems().add("Jednokrevetna");
        dodavanjeVrsteSobe.getItems().add("Dvokrevetna");
        dodavanjeVrsteSobe.getItems().add("Trokrevetna");




        // brisanje

        brisanje.setOnSelectionChanged(event -> {
            if(brisanje.isSelected()){
                prikaziListuAranzmana();
            }
        });


        //admini
        administratori.setOnSelectionChanged(event -> {
            if(administratori.isSelected()){
                prikaziListuAdmina();
            }
        });

        listaAdmina = FXCollections.observableArrayList();
        adminSpisakadmina.setItems(listaAdmina);

        vise.setOnSelectionChanged(event -> {
            if(vise.isSelected()){
                viseInfo();
            }
        });



    }

    public void setAdmin(Admin admin) {
        this.adminKorisnik = admin;

        // Ovdje dodajte kod za postavljanje informacija o administratoru koji je sada postavljen
        // Na primer, možete ažurirati tekst u infoTextArea, kao što radite u initialize metodi.
        infoTextArea.appendText("Ime i prezime:\n");
        infoTextArea.appendText("\t" + adminKorisnik.getIme() + " " + adminKorisnik.getPrezime() + "\n\n");
        infoTextArea.appendText("Korisničko ime:\n");
        infoTextArea.appendText("\t" + adminKorisnik.getKorisnickoIme() + "\n\n");
        int brojAdmina = Admin.brojSvihAdmina();
        infoTextArea.appendText("\t Ukupan broj admina " + brojAdmina + "\n\n");

        // Ostatak koda koji se nalazi u initialize metodi, a odnosi se na postavljanje ComboBox-ova i drugih elemenata,
        // takođe treba premestiti ovde ili ažurirati kako bi koristio informacije o adminu.
    }


    //    private Admin getAdminInfo() {
//        return admin;
//    }
    public void odjavaDugme(ActionEvent event) {
        promijeniScenuLogin(event);
    }

    // Promjeni lozinku tipka:
    public void promjeniLozinku() {
        if (infoStaraTextBox.getText().isEmpty()) {
            prozorObavjestenja("Greška", "Polje za staru lozinku je prazno!");
        } else if (infoNovaTextBox.getText().isEmpty()) {
            prozorObavjestenja("Greška", "Polje za novu lozinku je prazno!");
        } else {

            String lozinka = adminKorisnik.getSifra();
            System.out.println("sadasnja sifra: "+lozinka);
            System.out.println("input sata sifra: "+infoStaraTextBox.getText());
            String staraLozinka = infoStaraTextBox.getText();
            if (lozinka.equals(staraLozinka)) {
                String novaLozinka = infoNovaTextBox.getText();
                if (novaLozinka.length() < 5) {
                    prozorObavjestenja("Greška", "Lozinka mora biti duža od 5 karaktera!");
                } else {
                    String novaLozinkaHes = novaLozinka;
                    adminKorisnik.setAdminSifra(novaLozinkaHes);
                    int adminId= adminKorisnik.getadminID();
                    DBUtils.promjenaSifreAdmina(adminId,novaLozinkaHes);
                    prozorObavjestenja("Gotovo", "Lozinka uspješno promijenjena!");
                    infoStaraTextBox.clear();
                    infoNovaTextBox.clear();
                }
            } else {
                prozorObavjestenja("Greška", "Pogrešna stara lozinka!");
            }
        }
    }

    //
    public void filtriraj(ActionEvent event) {
        // Dobijanje vrijednosti iz polja za filtriranje
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

        tipPutovanja=filterTipPutovanja.getValue();
        if (tipPutovanja != null) {
            if (tipPutovanja.equals("Putovanje")) {
                try {
                    String cijenaOdText = filterCijenaOd.getText();
                    if (cijenaOdText != null && !cijenaOdText.isEmpty()) {
                        cijenaOd = Double.parseDouble(cijenaOdText);
                    }

                    String cijenaDoText = filterCijenaDo.getText();
                    if (cijenaDoText != null && !cijenaDoText.isEmpty()) {
                        cijenaDo = Double.parseDouble(cijenaDoText);
                    }

                    destinacija = filterDestinacija.getText();

                    // Provjera za LocalDate
                    datumKretanja = filterDatumKretanja.getValue();
                    datumPovratka = filterDatumPovratka.getValue();

                    if (datumKretanja != null) {
                        datumKretanja1 = java.sql.Date.valueOf(datumKretanja);
                    }
                    if (datumPovratka != null) {
                        datumPovratka1 = java.sql.Date.valueOf(datumPovratka);
                    }

                    brojZvjezdica = filterBrojZvjezdica.getValue();
                    vrstaSobe = filterVrstaSobe.getValue();
                    nacinPutovanja = filterNacinPutovanja.getValue();


                    // Filtriranje aranžmana
                    List<String> filtriraniAranzmani = new ArrayList<>();

                    for (Aranzman aranzman : Aranzman.sviAranzmani) {

                        // Pronađi povezani smještaj
                        Smjestaj smjestaj = Smjestaj.getSmjestajById(aranzman.getSmjestajId());

                        // Ovdje implementirajte uslov za filtriranje na osnovu unesenih kriterijuma
                        // Primjer:
                        if ((destinacija == null || aranzman.getDestinacija().contains(destinacija))
                                && (cijenaOd == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) >= cijenaOd)
                                && (cijenaDo == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) <= cijenaDo)
                                //                    && (datumKretanja == null || aranzman.getDatumPolaska().equals(datumKretanja))
                                //                    && (datumPovratka == null || aranzman.getDatumDolaska().equals(datumPovratka))
                                && (datumKretanja == null || aranzman.getDatumPolaska().equals(datumKretanja1) || aranzman.getDatumPolaska().after(datumKretanja1))
                                && (datumPovratka == null || aranzman.getDatumDolaska().equals(datumPovratka1) || aranzman.getDatumDolaska().before(datumPovratka1))
                                && (brojZvjezdica == null || smjestaj.getBrojZvjezdica().equals(brojZvjezdica))
                                && (vrstaSobe == null || smjestaj.getVrstaSobe().equals(vrstaSobe))
                                && (nacinPutovanja == null || aranzman.getPrevoz().equals(nacinPutovanja)) && (aranzman.getDatumDolaska() != null)) {
                            String listraFiltriranihAranzmana = ""+ aranzman.getId() + " - " + aranzman.getNazivPutovanja() + " - " + aranzman.getCijenaAranzmana();
                            filtriraniAranzmani.add(listraFiltriranihAranzmana); // Dodajte željene informacije o aranžmanu u listu
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
                    // Postavljanje filtriranih aranžmana u ListView
                    filterListaAranzmana.getItems().clear();
                    System.out.println(filtriraniAranzmani);
                    filterListaAranzmana.getItems().addAll(filtriraniAranzmani);
                } catch (NumberFormatException e) {
                    // Obrada izuzetka, na primjer, prikazivanje korisniku poruke o pogrešnom unosu
                    e.printStackTrace(); // Ovo se obično ne koristi u produkcijskom okruženju
                }
            } else if (tipPutovanja.equals("Izlet")) {
                try {
                    String cijenaOdText = filterCijenaOd.getText();
                    if (cijenaOdText != null && !cijenaOdText.isEmpty()) {
                        cijenaOd = Double.parseDouble(cijenaOdText);
                    }

                    String cijenaDoText = filterCijenaDo.getText();
                    if (cijenaDoText != null && !cijenaDoText.isEmpty()) {
                        cijenaDo = Double.parseDouble(cijenaDoText);
                    }

                    destinacija = filterDestinacija.getText();

                    // Provjera za LocalDate
                    datumKretanja = filterDatumKretanja.getValue();
                    datumPovratka = filterDatumPovratka.getValue();

                    if (datumKretanja != null) {
                        datumKretanja1 = java.sql.Date.valueOf(datumKretanja);
                    }
                    if (datumPovratka != null) {
                        datumPovratka1 = java.sql.Date.valueOf(datumPovratka);
                    }




                    // Filtriranje aranžmana
                    List<String> filtriraniAranzmani = new ArrayList<>();

                    for (Aranzman aranzman : Aranzman.sviAranzmani) {

                        // Pronađi povezani smještaj
                        Smjestaj smjestaj = Smjestaj.getSmjestajById(aranzman.getSmjestajId());

                        // Ovdje implementirajte uslov za filtriranje na osnovu unesenih kriterijuma
                        // Primjer:
                        if ((destinacija == null || aranzman.getDestinacija().contains(destinacija))
                                && (cijenaOd == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) >= cijenaOd)
                                && (cijenaDo == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) <= cijenaDo)
                                //                    && (datumKretanja == null || aranzman.getDatumPolaska().equals(datumKretanja))
                                //                    && (datumPovratka == null || aranzman.getDatumDolaska().equals(datumPovratka))
                                && (datumKretanja == null || aranzman.getDatumPolaska().equals(datumKretanja1) || aranzman.getDatumPolaska().after(datumKretanja1))
                                && (datumPovratka == null || aranzman.getDatumPolaska().equals(datumPovratka1) || aranzman.getDatumPolaska().before(datumPovratka1))
                                && (aranzman.getDatumDolaska() == null)) {
                            String listraFiltriranihAranzmana = ""+ aranzman.getId() + " - " + aranzman.getNazivPutovanja() + " - " + aranzman.getCijenaAranzmana();
                            filtriraniAranzmani.add(listraFiltriranihAranzmana); // Dodajte željene informacije o aranžmanu u listu
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

                    // Postavljanje filtriranih aranžmana u ListView
                    filterListaAranzmana.getItems().clear();
                    System.out.println(filtriraniAranzmani);
                    filterListaAranzmana.getItems().addAll(filtriraniAranzmani);
                } catch (NumberFormatException e) {
                    // Obrada izuzetka, na primjer, prikazivanje korisniku poruke o pogrešnom unosu
                    e.printStackTrace(); // Ovo se obično ne koristi u produkcijskom okruženju
                }

            } else {
                try {
                    String cijenaOdText = filterCijenaOd.getText();
                    if (cijenaOdText != null && !cijenaOdText.isEmpty()) {
                        cijenaOd = Double.parseDouble(cijenaOdText);
                    }

                    String cijenaDoText = filterCijenaDo.getText();
                    if (cijenaDoText != null && !cijenaDoText.isEmpty()) {
                        cijenaDo = Double.parseDouble(cijenaDoText);
                    }

                    destinacija = filterDestinacija.getText();

                    // Provjera za LocalDate
                    datumKretanja = filterDatumKretanja.getValue();
                    datumPovratka = filterDatumPovratka.getValue();

                    if (datumKretanja != null) {
                        datumKretanja1 = java.sql.Date.valueOf(datumKretanja);
                    }
                    if (datumPovratka != null) {
                        datumPovratka1 = java.sql.Date.valueOf(datumPovratka);
                    }


                    brojZvjezdica = filterBrojZvjezdica.getValue();
                    vrstaSobe = filterVrstaSobe.getValue();
                    nacinPutovanja = filterNacinPutovanja.getValue();


                    // Filtriranje aranžmana
                    List<String> filtriraniAranzmani = new ArrayList<>();

                    for (Aranzman aranzman : Aranzman.sviAranzmani) {

                        // Pronađi povezani smještaj
                        Smjestaj smjestaj = Smjestaj.getSmjestajById(aranzman.getSmjestajId());

                        // Ovdje implementirajte uslov za filtriranje na osnovu unesenih kriterijuma
                        // Primjer:
                        if ((destinacija == null || aranzman.getDestinacija().contains(destinacija))
                                && (cijenaOd == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) >= cijenaOd)
                                && (cijenaDo == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) <= cijenaDo)
                                //                    && (datumKretanja == null || aranzman.getDatumPolaska().equals(datumKretanja))
                                //                    && (datumPovratka == null || aranzman.getDatumDolaska().equals(datumPovratka))
                                && (datumKretanja == null || aranzman.getDatumPolaska().equals(datumKretanja1) || aranzman.getDatumPolaska().after(datumKretanja1))
                                && (datumPovratka == null || aranzman.getDatumDolaska().equals(datumPovratka1) || aranzman.getDatumDolaska().before(datumPovratka1))
                                && (brojZvjezdica == null || smjestaj.getBrojZvjezdica().equals(brojZvjezdica))
                                && (vrstaSobe == null || smjestaj.getVrstaSobe().equals(vrstaSobe))
                                && (nacinPutovanja == null || aranzman.getPrevoz().equals(nacinPutovanja))) {
                            String listraFiltriranihAranzmana = ""+ aranzman.getId() + " - " + aranzman.getNazivPutovanja() + " - " + aranzman.getCijenaAranzmana();
                            filtriraniAranzmani.add(listraFiltriranihAranzmana); // Dodajte željene informacije o aranžmanu u listu
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


                    // Postavljanje filtriranih aranžmana u ListView
                    filterListaAranzmana.getItems().clear();
                    System.out.println(filtriraniAranzmani);
                    filterListaAranzmana.getItems().addAll(filtriraniAranzmani);
                } catch (NumberFormatException e) {
                    // Obrada izuzetka, na primjer, prikazivanje korisniku poruke o pogrešnom unosu
                    e.printStackTrace(); // Ovo se obično ne koristi u produkcijskom okruženju
                }
            }
        }
        else {
            try {
                String cijenaOdText = filterCijenaOd.getText();
                if (cijenaOdText != null && !cijenaOdText.isEmpty()) {
                    cijenaOd = Double.parseDouble(cijenaOdText);
                }

                String cijenaDoText = filterCijenaDo.getText();
                if (cijenaDoText != null && !cijenaDoText.isEmpty()) {
                    cijenaDo = Double.parseDouble(cijenaDoText);
                }

                destinacija = filterDestinacija.getText();

                // Provjera za LocalDate
                datumKretanja = filterDatumKretanja.getValue();
                datumPovratka = filterDatumPovratka.getValue();

                if (datumKretanja != null) {
                    datumKretanja1 = java.sql.Date.valueOf(datumKretanja);
                }
                if (datumPovratka != null) {
                    datumPovratka1 = java.sql.Date.valueOf(datumPovratka);
                }

                brojZvjezdica = filterBrojZvjezdica.getValue();
                vrstaSobe = filterVrstaSobe.getValue();
                nacinPutovanja = filterNacinPutovanja.getValue();


                // Filtriranje aranžmana
                List<String> filtriraniAranzmani = new ArrayList<>();

                for (Aranzman aranzman : Aranzman.sviAranzmani) {

                    // Pronađi povezani smještaj
                    Smjestaj smjestaj = Smjestaj.getSmjestajById(aranzman.getSmjestajId());

                    // Ovdje implementirajte uslov za filtriranje na osnovu unesenih kriterijuma
                    // Primjer:
                    if ((destinacija == null || aranzman.getDestinacija().contains(destinacija))
                            && (cijenaOd == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) >= cijenaOd)
                            && (cijenaDo == 0.0 || Double.parseDouble(aranzman.getCijenaAranzmana()) <= cijenaDo)
                            //                    && (datumKretanja == null || aranzman.getDatumPolaska().equals(datumKretanja))
                            //                    && (datumPovratka == null || aranzman.getDatumDolaska().equals(datumPovratka))
                            && (datumKretanja == null || aranzman.getDatumPolaska().equals(datumKretanja1) || aranzman.getDatumPolaska().after(datumKretanja1))
                            && (datumPovratka == null || aranzman.getDatumDolaska().equals(datumPovratka1) || aranzman.getDatumDolaska().before(datumPovratka1))
                            && (brojZvjezdica == null || smjestaj.getBrojZvjezdica().equals(brojZvjezdica))
                            && (vrstaSobe == null || smjestaj.getVrstaSobe().equals(vrstaSobe))
                            && (nacinPutovanja == null || aranzman.getPrevoz().equals(nacinPutovanja))) {
                        String listraFiltriranihAranzmana = ""+ aranzman.getId() + " - " + aranzman.getNazivPutovanja() + " - " + aranzman.getCijenaAranzmana();
                        filtriraniAranzmani.add(listraFiltriranihAranzmana); // Dodajte željene informacije o aranžmanu u listu
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


                // Postavljanje filtriranih aranžmana u ListView
                filterListaAranzmana.getItems().clear();
                System.out.println(filtriraniAranzmani);
                filterListaAranzmana.getItems().addAll(filtriraniAranzmani);
            } catch (NumberFormatException e) {
                // Obrada izuzetka, na primjer, prikazivanje korisniku poruke o pogrešnom unosu
                e.printStackTrace(); // Ovo se obično ne koristi u produkcijskom okruženju
            }
        }



    }



    private void prikaziRezervacije() {
        // Sortiranje rezervacija prema KlijentID
        Aranzman.sviAranzmani.sort(Comparator.comparing(Aranzman::getId));

        // Prikazivanje naziva rezervacija u ListView
        rezervacijeLista.getItems().clear(); // Očisti postojeće stavke u listi

        for (Aranzman aranzman : sviAranzmani) {
            // Dodajte naziv rezervacije u listu   // trebam formatirati da bude kao sto zadatak trazi
            rezervacijeLista.getItems().add("Aranzman ID: " + aranzman.getId());
        }
    }

    @FXML
    private void prikaziKlijente(ActionEvent event) {
        ArrayList<Rezervacija> sveRezervacije = Rezervacija.getSveRezervacije();
        System.out.println(sveRezervacije);
        //ArrayList<Aranzman> sviAranzmani = Aranzman.getSviAranzmani();
        String selektovaniAranzman = rezervacijeLista.getSelectionModel().getSelectedItem();
        System.out.println(selektovaniAranzman);


        String[] dijelovi = selektovaniAranzman.split(":");
        String vrijednost = dijelovi[1].trim();
        Aranzman aranzman = Aranzman.getAranzmanById(vrijednost);
        Date polazak = aranzman.getDatumPolaska();
        long datumpol = polazak.getTime();
        LocalDate trenutniDatum = LocalDate.now();
        Instant instant = trenutniDatum.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Date convertedDate = Date.from(instant);
        long danas= convertedDate.getTime();
        long razlikaUMilisekundama = datumpol-danas;
        long razlikaUDanima = TimeUnit.DAYS.convert(razlikaUMilisekundama, TimeUnit.MILLISECONDS);



        StringBuilder sb = new StringBuilder();
        for (Rezervacija rezervacija : sveRezervacije){

            if (vrijednost.equals(rezervacija.getAranzmanID())){

                int klijentID = rezervacija.getKlijentID();
                ArrayList<Klijent> svi = Klijent.getSviKlijenti();
                for (Klijent klijent : svi) {
                    if (klijentID == klijent.getKlijentID()) {
                        sb.append("Ime i prezime: ").append(klijent.getIme()).append(" ").append(klijent.getPrezime()).append(" - ");
                        sb.append("Broj telefona: ").append(klijent.getBroj_telefona()).append(" \n ");
                        if (razlikaUDanima <= 3) {
                            sb.append("Ostalo dana: !!! - ").append(razlikaUDanima).append(" - !!!\n");
                        } else {
                            sb.append("Ostalo dana: ").append(razlikaUDanima).append(" \n");
                        }

                        sb.append("================================\n");
                    }
                }
            }
        }

        rezervacijeListaKlijenata.setText(sb.toString());
//        String klijenti = "";
//        for (Rezervacija rezervacija : Rezervacija.sveRezervacije) {
//            if (rezervacija.getAranzmanID().equals(rezervacijaId)) {
//                // Get the client details (replace with your actual logic to retrieve client information)
//                String klijentIme = "Klijent 1"; // Example
//                String klijentPrezime = "Prezime 1"; // Example
//                klijenti += klijentIme + " " + klijentPrezime + "\n";
//            }
//        }
//        rezervacijeListaKlijenata.setText(klijenti);
    }



    public void dodajIzlet() {
        String nazivIzleta = dodajNazivIzlet.getText();
        String cijenaIzleta = dodajCijenaIzlet.getText();
        String destinacijaIzleta = dodajDestinacijaIzlet.getText();
        LocalDate datum = dodavanjeDatumIzleta.getValue();
        Date datumIzleta = java.sql.Date.valueOf(datum);
        int i;
        i = getMax() + 1;
        if (nazivIzleta.isBlank()) {
            prozorObavjestenja("Greška", "Naziv izleta je prazno polje");
        } else if (cijenaIzleta.isBlank()) {
            prozorObavjestenja("Greška", "Cijena izleta je prazna");
        } else if (destinacijaIzleta.isBlank()) {
            prozorObavjestenja("Greška", "Destinacija izleta je prazna");
        } else if (datumIzleta == null) {
            prozorObavjestenja("Greška", "Datum izleta je prazno polje");
        } else {
            DBUtils.dodajIzletDB(i, nazivIzleta, destinacijaIzleta, datumIzleta, cijenaIzleta);
        }
    }
    public void dodajPutovanje() {
        String nazivPutovanja = dodavanjeNazivPutovanja.getText();
        String destinacijaPutovanja = dodavanjeDestinacijaPutovanja.getText();
        String cijenaPutovanja = dodavanjeCijenePutovanja.getText();
        LocalDate datumPolaska = dodavanjeDatumPolaskaPutovanja.getValue();
        Date datumPolaskaPutovanja = (datumPolaska != null) ? java.sql.Date.valueOf(datumPolaska) : null;
        LocalDate datumPovratka = dodavanjeDatumPovratkaPutovanja.getValue();
        Date datumPovratkaPutovanja = (datumPovratka != null) ? java.sql.Date.valueOf(datumPovratka) : null;
        String tipPrevoza = dodavanjeTipPrevoza.getValue();

        int id = getMax() + 1;

        String nazivSmjestaja = dodavanjeNazivSmjestajaPutovanja.getText();
        String cijenaNocenjePutovanja = dodavanjeCijeneNocenjaPutovanja.getText();
        String vrstaSobe = dodavanjeVrsteSobe.getValue();
        String brojZvjezdica = dodavanjeBrojZvjezdica.getValue();

        DBUtils.dodajSmjestajDB(id,nazivSmjestaja,brojZvjezdica,vrstaSobe,cijenaNocenjePutovanja);
        if (cijenaNocenjePutovanja.isBlank()) {
            prozorObavjestenja("Greška", "Cijena po nocenju je prazno polje");
        } else if (vrstaSobe.isBlank()) {
            prozorObavjestenja("Greška", "Vrsta sobe putovanja je prazna");
        } else if (brojZvjezdica.isBlank()) {
            prozorObavjestenja("Greška", "Broj zvjezdica putovanja je prazna");
        } else if (nazivSmjestaja.isBlank()) {
            prozorObavjestenja("Greška", "naziv smjestaja je prazno polje");
        } else if (nazivPutovanja.isBlank()) {
            prozorObavjestenja("Greška", "Naziv putovanja je prazno polje");
        } else if (destinacijaPutovanja.isBlank()) {
            prozorObavjestenja("Greška", "Destinacija putovanja je prazna");
        } else if (cijenaPutovanja.isBlank()) {
            prozorObavjestenja("Greška", "Cijena putovanja je prazna");
        } else if (datumPolaskaPutovanja == null) {
            prozorObavjestenja("Greška", "Datum polaska putovanja je prazno polje");
        }else if (datumPovratkaPutovanja == null) {
            prozorObavjestenja("Greška", "Datum polaska putovanja je prazno polje");
        } else {
            // Poziv metode za dodavanje putovanja u bazu

            DBUtils.dodajAranzmanDB(id,nazivPutovanja,destinacijaPutovanja,tipPrevoza,datumPolaskaPutovanja,datumPovratkaPutovanja,cijenaPutovanja,id);
        }
    }


    public void prikaziListuAranzmana(){
        ArrayList<Aranzman> sviAranzmani = getSviAranzmani();

        // Pretvorite ArrayList<Aranzman> u ArrayList<String>
        ArrayList<String> listaAranzmana = new ArrayList<>();
        for (Aranzman aranzman : sviAranzmani) {
            // Ovde dodajte logiku kako želite da prikažete svaki aranžman kao string
            String aranzmanString = aranzman.getId()+" - "+ aranzman.getNazivPutovanja() + " - " + aranzman.getDestinacija();
            listaAranzmana.add(aranzmanString);
        }

        // Postavite vrednosti u ListView
        brisanjeListaAranzmana.setItems(FXCollections.observableArrayList(listaAranzmana));
    }

    @FXML
    private void obrisiAranzman(ActionEvent event) {
        String selektovaniAranzman = brisanjeListaAranzmana.getSelectionModel().getSelectedItem();

        if (selektovaniAranzman != null) {
            System.out.println(selektovaniAranzman);
            int indexPrvogZnaka = selektovaniAranzman.indexOf("-");

            // Izdvajanje prvog dela stringa
            String id = selektovaniAranzman.substring(0, indexPrvogZnaka).trim();
            // Pozovite funkciju za brisanje aranžmana, prosleđujući selektovaniAranzman
            otkaziPutovanje(id);

            // Obrisati odabrani aranžman iz liste
            brisanjeListaAranzmana.getItems().remove(selektovaniAranzman);
        } else {
            // Ako nije selektovan nijedan aranžman, možete prikazati poruku ili obavestiti korisnika
            System.out.println("Molimo vas da selektujete aranžman za brisanje.");
        }
    }

    public void prikaziListuAdmina(){
        ArrayList<Admin> sviAdmini = getSviAdmini();

        // Pretvorite ArrayList<Aranzman> u ArrayList<String>
        ArrayList<String> listaAdmina = new ArrayList<>();
        for (Admin admin : sviAdmini) {
            // Ovde dodajte logiku kako želite da prikažete svaki aranžman kao string
            String adminString = admin.getadminID()+" - "+ admin.getIme() + " - " + admin.getPrezime();
            listaAdmina.add(adminString);
        }

        // Postavite vrednosti u ListView
        adminSpisakadmina.setItems(FXCollections.observableArrayList(listaAdmina));
    }

    public void dodavanjeAdmina(ActionEvent event){
        String ime = adminIme.getText().strip();;
        String prezime = adminPrezime.getText().strip();;
        String korisnickoIme = adminKorisnickoIme.getText().strip();;
        String sifra = "12345678";


        if (korisnickoIme.isBlank() || prezime.isBlank() || ime.isBlank()) {
            prozorObavjestenja("Greška", "Neko polje je prazno");
        } else if (!korisnickoIme.isBlank() && !korisnickoIme.replace(".", "_").matches("[a-z]+_[a-z]+")) {
            prozorObavjestenja("Greška", "Korisničko ime treba biti u formatu ime_prezime");
        } else{
            int id= getMaxAdminID()+1;
            System.out.println(ime);
            System.out.println(prezime);
            System.out.println(korisnickoIme);
            System.out.println(sifra);
            System.out.println(id);
            DBUtils.dodajAdminaDB(id,ime,prezime,korisnickoIme,sifra);
            adminIme.setText("");
            adminPrezime.setText("");
            adminKorisnickoIme.setText("");

            ArrayList<Admin> sviAdmini = getSviAdmini();

            // Pretvorite ArrayList<Aranzman> u ArrayList<String>
            ArrayList<String> listaAdmina = new ArrayList<>();
            for (Admin admin : sviAdmini) {
                // Ovde dodajte logiku kako želite da prikažete svaki aranžman kao string
                String adminString = admin.getadminID()+" - "+ admin.getIme() + " - " + admin.getPrezime();
                listaAdmina.add(adminString);
            }


            String adminString = "" + id + " - " + ime + " - " + prezime;
            listaAdmina.add(adminString);
            //adminSpisakadmina.setItems(listaAdmina);
            adminSpisakadmina.setItems(FXCollections.observableArrayList(listaAdmina));
        }
    }


    public void viseInfo(){
        ArrayList<Rezervacija> sveRezervacije = Rezervacija.getSveRezervacije();
        int brRez=0;
        for (Rezervacija rez : sveRezervacije){
            brRez++;
        }
        ArrayList<Klijent> sviKlijenti = Klijent.getSviKlijenti();
        int brKl=0;
        for (Klijent kl : sviKlijenti){
            brKl++;
        }
        String agencijaBR = "1234567887654321";
        Bankovni_racun aggencijabr = Bankovni_racun.getRacunByBrojRacuna(agencijaBR);
        double stanje= aggencijabr.getStanje();
        double cekanje=0.0;
        for (Rezervacija rezervacija : sveRezervacije){
            Double ukupna = Double.parseDouble(rezervacija.getUkupnaCijena());
            Double placena = Double.parseDouble(rezervacija.getPlacenaCijena());
            cekanje+=(ukupna-placena);
        }

        viseCekanje.setText(String.valueOf(cekanje));
        viseZarada.setText(String.valueOf(stanje));
        viseKlijenti.setText(String.valueOf(brKl));
        viseRez.setText(String.valueOf(brRez));
    }
    private ObservableList<String> listaAdmina;
}



