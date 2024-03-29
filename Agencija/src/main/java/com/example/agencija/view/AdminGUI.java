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







        filterTipPutovanja.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals("Putovanje")) {
                    filterBrojZvjezdica.setDisable(false);
                    filterVrstaSobe.setDisable(false);
                    filterNacinPutovanja.setDisable(false);
                    filterDatumPovratka.setDisable(false);
                } else if (newValue.equals("Izlet")) {
                    filterBrojZvjezdica.setDisable(true);
                    filterVrstaSobe.setDisable(true);
                    filterNacinPutovanja.setDisable(true);
                    filterDatumPovratka.setDisable(true);
                } else {
                    filterBrojZvjezdica.setDisable(false);
                    filterVrstaSobe.setDisable(false);
                    filterNacinPutovanja.setDisable(false);
                    filterDatumPovratka.setDisable(false);
                }

                }
            else{
                filterBrojZvjezdica.setDisable(false);
                filterVrstaSobe.setDisable(false);
                filterNacinPutovanja.setDisable(false);
                filterDatumPovratka.setDisable(false);
            }
            });


        filterTipPutovanja.getItems().add(null);
        filterTipPutovanja.getItems().add("Izlet");
        filterTipPutovanja.getItems().add("Putovanje");

        filterBrojZvjezdica.getItems().add(null);
        filterBrojZvjezdica.getItems().add("Tri");
        filterBrojZvjezdica.getItems().add("Cetiri");
        filterBrojZvjezdica.getItems().add("Pet");

        filterVrstaSobe.getItems().add(null);
        filterVrstaSobe.getItems().add("Jednokrevetna");
        filterVrstaSobe.getItems().add("Dvokrevetna");
        filterVrstaSobe.getItems().add("Trokrevetna");

        filterNacinPutovanja.getItems().add(null);
        filterNacinPutovanja.getItems().add("Avion");
        filterNacinPutovanja.getItems().add("Autobus");
        filterNacinPutovanja.getItems().add("Samostalan");


        tabRezervacije.setOnSelectionChanged(event -> {
            if (tabRezervacije.isSelected()) {

                prikaziRezervacije();
            }
        });




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




        brisanje.setOnSelectionChanged(event -> {
            if(brisanje.isSelected()){
                prikaziListuAranzmana();
            }
        });


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


        infoTextArea.appendText("Ime i prezime:\n");
        infoTextArea.appendText("\t" + adminKorisnik.getIme() + " " + adminKorisnik.getPrezime() + "\n\n");
        infoTextArea.appendText("Korisničko ime:\n");
        infoTextArea.appendText("\t" + adminKorisnik.getKorisnickoIme() + "\n\n");
        int brojAdmina = Admin.brojSvihAdmina();
        infoTextArea.appendText("\t Ukupan broj admina " + brojAdmina + "\n\n");


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

        tipPutovanja=filterTipPutovanja.getValue();
        if (tipPutovanja != null) {
            System.out.println(tipPutovanja);
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


                    datumKretanja = filterDatumKretanja.getValue();
                    datumPovratka = filterDatumPovratka.getValue();

                    if (datumKretanja != null) {
                        datumKretanja1 = java.sql.Date.valueOf(datumKretanja);
                    }
                    if (datumPovratka != null) {
                        datumPovratka1 = java.sql.Date.valueOf(datumPovratka);
                    }

                    LocalDate trenutniDatum = LocalDate.now();
                    Instant instant = trenutniDatum.atStartOfDay(ZoneId.systemDefault()).toInstant();
                    Date convertedDate = Date.from(instant);
                    long danas= convertedDate.getTime();


                        brojZvjezdica = filterBrojZvjezdica.getValue();
                        vrstaSobe = filterVrstaSobe.getValue();
                        nacinPutovanja = filterNacinPutovanja.getValue();



                        List<String> filtriraniAranzmani = new ArrayList<>();

                        for (Aranzman aranzman : Aranzman.sviAranzmani) {


                            Smjestaj smjestaj = Smjestaj.getSmjestajById(aranzman.getSmjestajId());


                            if (smjestaj != null && (destinacija == null || aranzman.getDestinacija().contains(destinacija))
                                    && (aranzman.getDatumPolaska().after(convertedDate))
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

                        filterListaAranzmana.getItems().clear();
                        System.out.println(filtriraniAranzmani);
                        filterListaAranzmana.getItems().addAll(filtriraniAranzmani);


                } catch (NumberFormatException e) {

                    e.printStackTrace();
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


                    datumKretanja = filterDatumKretanja.getValue();
                    datumPovratka = filterDatumPovratka.getValue();

                    if (datumKretanja != null) {
                        datumKretanja1 = java.sql.Date.valueOf(datumKretanja);
                    }
                    if (datumPovratka != null) {
                        datumPovratka1 = java.sql.Date.valueOf(datumPovratka);
                    }

                    LocalDate trenutniDatum = LocalDate.now();
                    Instant instant = trenutniDatum.atStartOfDay(ZoneId.systemDefault()).toInstant();
                    Date convertedDate = Date.from(instant);
                    long danas= convertedDate.getTime();

                        List<String> filtriraniAranzmani = new ArrayList<>();

                        for (Aranzman aranzman : Aranzman.sviAranzmani) {


//                        Smjestaj smjestaj = Smjestaj.getSmjestajById(aranzman.getSmjestajId());


                            if ((destinacija == null || aranzman.getDestinacija().contains(destinacija))
                                    && (aranzman.getDatumPolaska().after(convertedDate))
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


                        filterListaAranzmana.getItems().clear();
                        System.out.println(filtriraniAranzmani);
                        filterListaAranzmana.getItems().addAll(filtriraniAranzmani);


                } catch (NumberFormatException e) {

                    e.printStackTrace();
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


                    datumKretanja = filterDatumKretanja.getValue();
                    datumPovratka = filterDatumPovratka.getValue();

                    if (datumKretanja != null) {
                        datumKretanja1 = java.sql.Date.valueOf(datumKretanja);
                    }
                    if (datumPovratka != null) {
                        datumPovratka1 = java.sql.Date.valueOf(datumPovratka);
                    }


                    LocalDate trenutniDatum = LocalDate.now();
                    Instant instant = trenutniDatum.atStartOfDay(ZoneId.systemDefault()).toInstant();
                    Date convertedDate = Date.from(instant);
                    long danas= convertedDate.getTime();


                        brojZvjezdica = filterBrojZvjezdica.getValue();
                        vrstaSobe = filterVrstaSobe.getValue();
                        nacinPutovanja = filterNacinPutovanja.getValue();



                        List<String> filtriraniAranzmani = new ArrayList<>();

                        for (Aranzman aranzman : Aranzman.sviAranzmani) {


                            Smjestaj smjestaj = Smjestaj.getSmjestajById(aranzman.getSmjestajId());


                            if (smjestaj != null && (destinacija == null || aranzman.getDestinacija().contains(destinacija))
                                    && (aranzman.getDatumPolaska().after(convertedDate))
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


//                    filterListaAranzmana.getItems().clear();
                        filterListaAranzmana = new ListView<>();
                        System.out.println(filtriraniAranzmani);
                        filterListaAranzmana.getItems().addAll(filtriraniAranzmani);

                } catch (NumberFormatException e) {

                    e.printStackTrace();
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

                datumKretanja = filterDatumKretanja.getValue();
                datumPovratka = filterDatumPovratka.getValue();

                if (datumKretanja != null) {
                    datumKretanja1 = java.sql.Date.valueOf(datumKretanja);
                }
                if (datumPovratka != null) {
                    datumPovratka1 = java.sql.Date.valueOf(datumPovratka);
                }

                LocalDate trenutniDatum = LocalDate.now();
                Instant instant = trenutniDatum.atStartOfDay(ZoneId.systemDefault()).toInstant();
                Date convertedDate = Date.from(instant);
                long danas= convertedDate.getTime();

                    brojZvjezdica = filterBrojZvjezdica.getValue();
                    vrstaSobe = filterVrstaSobe.getValue();
                    nacinPutovanja = filterNacinPutovanja.getValue();


                    List<String> filtriraniAranzmani = new ArrayList<>();

                    for (Aranzman aranzman : Aranzman.sviAranzmani) {


                        Smjestaj smjestaj = Smjestaj.getSmjestajById(aranzman.getSmjestajId());

                        if ((destinacija == null || aranzman.getDestinacija().contains(destinacija))
                                && (aranzman.getDatumPolaska().after(convertedDate))
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


                    filterListaAranzmana.getItems().clear();
                    System.out.println(filtriraniAranzmani);
                    filterListaAranzmana.getItems().addAll(filtriraniAranzmani);


            } catch (NumberFormatException e) {

                e.printStackTrace();
            }
        }



    }



    private void prikaziRezervacije() {

        Aranzman.sviAranzmani.sort(Comparator.comparing(Aranzman::getId));


        rezervacijeLista.getItems().clear();

        for (Aranzman aranzman : sviAranzmani) {

            rezervacijeLista.getItems().add("Aranzman ID: " + aranzman.getId());
        }
    }

    @FXML
    private void prikaziKlijente(ActionEvent event) {
        ArrayList<Rezervacija> sveRezervacije = Rezervacija.getSveRezervacije();
        System.out.println(sveRezervacije);

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

            LocalDate trenutniDatum = LocalDate.now();
            Instant instant = trenutniDatum.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Date convertedDate = Date.from(instant);
            long danas= convertedDate.getTime();
            long datumIzletaMS = datumIzleta.getTime();
            if (datumIzletaMS > danas){
                DBUtils.dodajIzletDB(i, nazivIzleta, destinacijaIzleta, datumIzleta, cijenaIzleta);
                DBUtils.getDataFromDB();
                dodajNazivIzlet.clear();
                dodajCijenaIzlet.clear();
                dodajCijenaIzlet.clear();
                dodajDestinacijaIzlet.clear();
                dodavanjeDatumIzleta.setValue(null);
            }
            else {
                prozorObavjestenja("Greška", "Datum polaska nije dobra vrijednost");
            }
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
        DBUtils.getDataFromDB();
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


            LocalDate trenutniDatum = LocalDate.now();
            Instant instant = trenutniDatum.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Date convertedDate = Date.from(instant);
            long danas= convertedDate.getTime();
            long datumPolaskaPutovanjaMS = datumPolaskaPutovanja.getTime();
            long datumPovrtakaPutovanjaMS = datumPovratkaPutovanja.getTime();

            System.out.println(danas);
            System.out.println(datumPolaskaPutovanjaMS);
            System.out.println(datumPovrtakaPutovanjaMS);
            if (datumPolaskaPutovanjaMS > danas && datumPovrtakaPutovanjaMS> datumPolaskaPutovanjaMS){

                DBUtils.dodajAranzmanDB(id,nazivPutovanja,destinacijaPutovanja,tipPrevoza,datumPolaskaPutovanja,datumPovratkaPutovanja,cijenaPutovanja,id);
                DBUtils.getDataFromDB();

                dodavanjeNazivSmjestajaPutovanja.clear();
                dodavanjeNazivPutovanja.clear();
                dodavanjeDestinacijaPutovanja.clear();
                dodavanjeCijenePutovanja.clear();
                dodavanjeDatumPolaskaPutovanja.setValue(null);
                dodavanjeDatumPovratkaPutovanja.setValue(null);
                dodavanjeTipPrevoza.setValue(null);
                dodavanjeCijeneNocenjaPutovanja.clear();
                dodavanjeVrsteSobe.setValue(null);
                dodavanjeBrojZvjezdica.setValue(null);
            }
            else {
                prozorObavjestenja("Greška", "Datum polaska ili povratka putovanja nije dobra vrijednost");
            }




        }
    }


    public void prikaziListuAranzmana(){
        ArrayList<Aranzman> sviAranzmani = getSviAranzmani();


        ArrayList<String> listaAranzmana = new ArrayList<>();
        for (Aranzman aranzman : sviAranzmani) {

            String aranzmanString = aranzman.getId()+" - "+ aranzman.getNazivPutovanja() + " - " + aranzman.getDestinacija();
            listaAranzmana.add(aranzmanString);
        }


        brisanjeListaAranzmana.setItems(FXCollections.observableArrayList(listaAranzmana));
    }

    @FXML
    private void obrisiAranzman(ActionEvent event) {
        String selektovaniAranzman = brisanjeListaAranzmana.getSelectionModel().getSelectedItem();

        if (selektovaniAranzman != null) {
            System.out.println(selektovaniAranzman);
            int indexPrvogZnaka = selektovaniAranzman.indexOf("-");

            String id = selektovaniAranzman.substring(0, indexPrvogZnaka).trim();

            otkaziPutovanje(id);
            DBUtils.getDataFromDB();


            brisanjeListaAranzmana.getItems().remove(selektovaniAranzman);
        } else {

            System.out.println("Molimo vas da selektujete aranžman za brisanje.");
        }
    }

    public void prikaziListuAdmina(){
        ArrayList<Admin> sviAdmini = getSviAdmini();

        ArrayList<String> listaAdmina = new ArrayList<>();
        for (Admin admin : sviAdmini) {

            String adminString = admin.getadminID()+" - "+ admin.getIme() + " - " + admin.getPrezime();
            listaAdmina.add(adminString);
        }


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
            DBUtils.getDataFromDB();

            ArrayList<Admin> sviAdmini = getSviAdmini();


            ArrayList<String> listaAdmina = new ArrayList<>();
            for (Admin admin : sviAdmini) {

                String adminString = admin.getadminID()+" - "+ admin.getIme() + " - " + admin.getPrezime();
                listaAdmina.add(adminString);
            }


            String adminString = "" + id + " - " + ime + " - " + prezime;
            listaAdmina.add(adminString);

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



