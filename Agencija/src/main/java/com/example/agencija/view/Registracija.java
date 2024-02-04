package com.example.agencija.view;

import com.example.agencija.model.Admin;
import com.example.agencija.model.Bankovni_racun;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.example.agencija.sql.DBUtils;
import com.example.agencija.model.Klijent;


import java.util.ArrayList;

import static com.example.agencija.model.Klijent.getMaxKlijentID;

public class Registracija extends Kontroler {

    public TextField firstName , lastName , phoneNumber, JMBG , username, bankAcc;
    public PasswordField password , passwordConfirmation;
    public Label ErrReg;

    public void registracijaKorisnika(ActionEvent event){
        String korisnickoIme = username.getText().strip();
        if (Klijent.getKlijentByKorisnickoIme(korisnickoIme) == null) {
            String ime = firstName.getText().strip();
            String prezime = lastName.getText().strip();
            String brTel = phoneNumber.getText().strip();
            String jmbg = JMBG.getText().strip();
            String bankovniRac = bankAcc.getText().strip();
            String sifra = password.getText().strip();
            String sifraPotvrda = passwordConfirmation.getText().strip();
            System.out.println(ime);
            System.out.println(prezime);
            System.out.println(korisnickoIme);
            System.out.println(brTel);
            System.out.println(jmbg);
            System.out.println(bankovniRac);
            System.out.println(sifra);

            if (validateInput(korisnickoIme, sifra)) {
                if (sifra.equals(sifraPotvrda)){
                    if (jmbg.length() != 13) {
                        throw new IllegalArgumentException("JMBG nije validan. Unos treba da ima 13 cifara.");
                    }

                    int i;
                    i= getMaxKlijentID()+1;
                    System.out.println(i);

                    ArrayList<Bankovni_racun> sviRacuni = Bankovni_racun.getSviBankovniRacuni();
                    for (Bankovni_racun br : sviRacuni){
                        if(br.getJmbg().equals(jmbg)){
                            DBUtils.dodajKlijentaDB(i,ime,prezime,korisnickoIme,bankovniRac,brTel,jmbg,sifra);
                            username.setText("");
                            firstName.setText("");
                            lastName.setText("");
                            phoneNumber.setText("");
                            JMBG.setText("");
                            bankAcc.setText("");
                            password.setText("");
                            passwordConfirmation.setText("");
                            promijeniScenuLogin(event);
                            DBUtils.getDataFromDB();
                        }
                        else{
                         //   prozorObavjestenja("Greška", "Ne postoji jmbg koji se podudara");
                        }
                    }





                    } else {
                        prozorObavjestenja("Greška", "Sifre se ne podudaraju.");
                        ErrReg.setText("Sifre se ne podudaraju.");

                    }
                }
        }
        else {
            ErrReg.setText("Vec postoji korisnik sa ovim imenom!");
            prozorObavjestenja("Greška", "Vec postoji korisnik sa ovim imenom.");
        }
    }

    private boolean validateInput(String usernameInput, String userpassInput) {
        boolean validInput = false;

        if (usernameInput.isBlank() && userpassInput.isBlank()) {
            ErrReg.setText("Unesite korisničko ime i šifru.");
        } else if (!usernameInput.isBlank() && !usernameInput.replace(".", "_").matches("[a-z]+_[a-z]+")) {
            ErrReg.setText("Korisničko ime treba biti u formatu ime_prezime(ako ima vise istih imena dodaj jos jedno slovo na kraj prezimena)");
            prozorObavjestenja("Greška", "Korisničko ime treba biti u formatu ime_prezime");
        } else if (!usernameInput.isBlank() && userpassInput.isBlank()) {
            ErrReg.setText("Niste unijeli šifru.");
        } else if (usernameInput.replace(".", "_").matches("[a-z]+_[a-z]+") && !userpassInput.isBlank()) {
            validInput = true;
        }
        return validInput;
    }

}

