package com.example.agencija.view;

import com.example.agencija.model.Admin;
import com.example.agencija.model.Klijent;
import com.example.agencija.sql.DBUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


import static javafx.fxml.FXMLLoader.load;

public class Login extends Kontroler implements Initializable {

    @FXML
    private TextField usernameInput, passwordText;
    @FXML
    private PasswordField userpassInput;
    @FXML
    private Label loginErrorMsg;
    @FXML
    private Button btnSubmit , btnRegister;
    @FXML
    private CheckBox showPassBtn;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSubmit.setOnAction(event -> {
            String korisnickoime = usernameInput.getText();
            String sifra = userpassInput.getText();
            validateInput(korisnickoime,sifra);
            String username = usernameInput.getText();
            String password = hesirajLozinku(userpassInput.getText());
            Klijent klijent = Klijent.getKlijentDuze(username,sifra);

            Admin admin = Admin.getAdminDuze(username,sifra);
            System.out.println(klijent);
            System.out.println(admin);
            if(klijent != null){
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setResizable(false);
                Parent adminParent;
                FXMLLoader loader = null;
                try {
                    loader = new FXMLLoader(getClass().getResource("klijent.fxml"));
                    System.out.println(loader.getLocation().getPath());
                    System.out.println(loader);
                    adminParent= loader.load();

                    KlijentGUI klijenttController = loader.getController();
                    klijenttController.setKlijent(klijent);



                    Scene adminScene = new Scene(adminParent);
                    stage.setScene(adminScene);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                }
            else if (admin != null) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setResizable(false);
                Parent adminParent;
                FXMLLoader loader = null;
                try {

                    loader = new FXMLLoader(getClass().getResource("admin.fxml"));
                    System.out.println(loader.getLocation().getPath());
                    System.out.println(loader);
                    adminParent= loader.load();

                    AdminGUI adminController = loader.getController();
                    adminController.setAdmin(admin);



                    Scene adminScene = new Scene(adminParent);
                    stage.setScene(adminScene);
                    stage.show();



                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                }
            else {
                prozorObavjestenja("Greška", "Pogresna lozinka ili korisnicko ime!");
                usernameInput.clear();
                userpassInput.clear();
                loginErrorMsg.setText("Pogresna lozinka ili korisnicko ime!");
            }

        });


    }

    private boolean validateInput(String usernameInput, String userpassInput) {
        boolean validInput = false;

        if (usernameInput.isBlank() && userpassInput.isBlank()) {
            loginErrorMsg.setText("Unesite korisničko ime i šifru.");
            prozorObavjestenja("Greška", "Unesite korisničko ime i šifru.");
        } else if (!usernameInput.isBlank() && !usernameInput.replace(".", "_").matches("[a-z]+_[a-z]+")) {
            loginErrorMsg.setText("Korisničko ime treba biti u formatu ime_prezime");
        } else if (!usernameInput.isBlank() && userpassInput.isBlank()) {
            loginErrorMsg.setText("Niste unijeli šifru.");
        } else if (usernameInput.replace(".", "_").matches("[a-z]+_[a-z]+") && !userpassInput.isBlank()) {
            validInput = true;
        }
        return validInput;
    }

    public void showPassword() {
        if (showPassBtn.isSelected()) {
            passwordText.setText(userpassInput.getText());
            userpassInput.setVisible(false);
            passwordText.setVisible(true);
        } else {
            userpassInput.setText(passwordText.getText());
            userpassInput.setVisible(true);
            passwordText.setVisible(false);
        }
    }

    public void promijeniScenuRegistracija(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent registracijaParent = load(Objects.requireNonNull(getClass().getResource("registration-page.fxml")));
        Scene registracijaScene = new Scene(registracijaParent);
        stage.setScene(registracijaScene);
        stage.setResizable(false);
        stage.show();
    }



}