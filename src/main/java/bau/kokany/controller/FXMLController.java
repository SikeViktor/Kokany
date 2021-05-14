package bau.kokany.controller;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import bau.kokany.model.Expert;
import bau.kokany.model.ExpertDAO;
import bau.kokany.model.JPAExpertDAO;
import bau.kokany.model.StatusType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class FXMLController implements Initializable {

    Map<String, String> ugyfelek=Map.of(
            "Gyuszi", "hgy",
            "Boti", "dmb",
            "Dávid", "md",
            "Vityu", "sv");

    void refresh() throws Exception {
        row.removeAll();
        try(ExpertDAO eDAO = new JPAExpertDAO();) {
            List<Expert> expertList = eDAO.getExperts();
            for(Expert e : expertList) {
                if(row.size() < expertList.size()) {
                    row.add(e);
                }
            }
        }

    }
    //Főoldal
    @FXML
    private AnchorPane main_page;

    @FXML
    private Button customer_button;

    @FXML
    private Button expert_button;

    @FXML
    private Button adminButton;

    @FXML
    private PasswordField adminpwd;

    @FXML
    private Label adminpwdLabel;

    @FXML
    void adminButtonClicked(ActionEvent event) {
        adminpwd.setVisible(true);
    }

    @FXML
    void customerbuttonPushed(ActionEvent event) {
        AtomicBoolean belepve= new AtomicBoolean(false);
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Look, a Custom Login Dialog");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        PasswordField password = new PasswordField();

        grid.add(new Label("Felhasználónév:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Jelszó:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            for(Map.Entry<String, String> par : ugyfelek.entrySet()) {
                if (Objects.equals(usernamePassword.getKey(), par.getKey()) && Objects.equals(usernamePassword.getValue(), par.getValue())) {
                    belepve.set(true);
                    break;
                } else {
                    belepve.set(false);
                }
            }
        });

        if(belepve.get()) {
            main_page.setVisible(false);
            customer_page.setVisible(true);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba");
            alert.setHeaderText(null);
            alert.setContentText("Hibás bejelentkezési adatok!");

            alert.showAndWait();
        }


    }

    @FXML
    void expertbuttonPushed(ActionEvent event) {
        main_page.setVisible(false);
        expert_page.setVisible(true);
    }

    @FXML
    void pwdPassed(ActionEvent event) throws Exception {
        if(!adminpwd.getText().equals("admin")) {
            adminpwdLabel.setVisible(true);
            adminpwd.clear();
        }
        else {
            adminpwd.clear();
            adminpwd.setVisible(false);
            adminpwdLabel.setVisible(false);
            main_page.setVisible(false);
            admin_page.setVisible(true);

            refresh();
        }
    }

    //Admin oldal
    @FXML
    private AnchorPane admin_page;

    @FXML
    private Button prev_button2;

    @FXML
    private Button admin_new_button;

    @FXML
    private Button admin_modify_button;

    @FXML
    private Button admin_delete_button;

    @FXML
    private TableView<Expert> lista1;

    @FXML
    private TableColumn<Expert, String> lista_name1;

    @FXML
    private TableColumn<Expert, String> lista_proffession1;

    @FXML
    private TableColumn<Expert, String> lista_status1;

    ObservableList<Expert> row = FXCollections.observableArrayList();

    @FXML
    void admin_delete_buttonPushed(ActionEvent event) throws Exception {
        Expert selectedItem = lista1.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Szakember Törlése");
        alert.setHeaderText(null);
        alert.setContentText("Biztosan törlöd?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try(ExpertDAO eDAO = new JPAExpertDAO();) {
                eDAO.deleteExpert(selectedItem);
                lista1.getItems().remove(selectedItem);
            }
        }

    }

    @FXML
    void admin_modify_buttonPushed(ActionEvent event) throws Exception {
        Expert selectedItem = lista1.getSelectionModel().getSelectedItem();
        Dialog<Expert> dialog = new Dialog<>();
        dialog.setTitle("Szakember Adatainak Módosítása");
        dialog.setHeaderText(null);
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK);
        TextField expert_name = new TextField(selectedItem.getName());
        TextField expert_prof= new TextField(selectedItem.getProfession());
        ObservableList<StatusType> options =
                FXCollections.observableArrayList(StatusType.values());
        ComboBox<StatusType> comboBox = new ComboBox<>(options);
        comboBox.getSelectionModel().select(selectedItem.getStatus());
        dialogPane.setContent(new VBox(8, expert_name, expert_prof, comboBox));
        Platform.runLater(expert_name::requestFocus);
        try(ExpertDAO eDAO = new JPAExpertDAO();) {
            dialog.setResultConverter((ButtonType button) -> {
                if (button == ButtonType.OK) {
                    for(Expert e : eDAO.getExperts()) {
                        if(Objects.equals(selectedItem.getName(), e.getName()) && Objects.equals(selectedItem.getProfession(), e.getProfession())) {
                            e.setName(expert_name.getText());
                            e.setProfession(expert_prof.getText());
                            e.setStatus(comboBox.getValue());
                            eDAO.updateExpert(e);
                        }

                    }

                    selectedItem.setName(expert_name.getText());
                    selectedItem.setProfession(expert_prof.getText());
                    selectedItem.setStatus(comboBox.getValue());

                }
                return selectedItem;

            });
            Optional<Expert> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((Expert results) -> {
                lista1.refresh();
            });
        }
    }

    @FXML
    void admin_new_buttonPushed(ActionEvent event) {
        Expert expert = new Expert();
        Dialog<Expert> dialog = new Dialog<>();
        dialog.setTitle("Új Szakember Felvétele");
        dialog.setHeaderText(null);
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK);
        TextField expert_name = new TextField("Név");
        TextField expert_prof= new TextField("Foglalkozás");
        ObservableList<StatusType> options =
                FXCollections.observableArrayList(StatusType.values());
        ComboBox<StatusType> comboBox = new ComboBox<>(options);
        comboBox.getSelectionModel().selectFirst();
        dialogPane.setContent(new VBox(8, expert_name, expert_prof, comboBox));
        Platform.runLater(expert_name::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                expert.setName(expert_name.getText());
                expert.setProfession(expert_prof.getText());
                expert.setStatus(comboBox.getValue());
            }
            return expert;

        });
        Optional<Expert> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Expert results) -> {
            try(ExpertDAO eDAO = new JPAExpertDAO();) {
                eDAO.saveExpert(expert);
                lista1.getItems().add(expert);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        });


    }

    //Szakember oldal
    @FXML
    private AnchorPane expert_page;

    @FXML
    private Button prev_button1;

    @FXML
    private TextField nameText;

    @FXML
    private TextField professionText;

    @FXML
    private ChoiceBox<StatusType> StatusCheckBox = new ChoiceBox<>();

    @FXML
    private Button newExpertButton;



    @FXML
    void newExpertButtonPushed(ActionEvent event) throws Exception {
        Expert expert = new Expert();
        expert.setName(nameText.getText());
        expert.setProfession(professionText.getText());
        expert.setStatus(StatusCheckBox.getValue());

        try(ExpertDAO eDAO = new JPAExpertDAO();) {
            eDAO.saveExpert(expert);
            lista.getItems().add(expert);
        }
        Alert expertAlert = new Alert(Alert.AlertType.INFORMATION);
        expert_page.setOpacity(0.4);
        expertAlert.setTitle("Mentés");
        expertAlert.setHeaderText("Alábbi adatok kerültek mentésre: ");
        expertAlert.setContentText("Név:\t" + nameText.getText() + "\nFoglalkozás:\t"+ professionText.getText() + "\nStátusz:\t" + StatusCheckBox.getValue());
        expertAlert.showAndWait();
        expert_page.setOpacity(1);
        //System.out.println("Sikeres mentés!");
    }


    //Ügyfél oldal
    @FXML
    private AnchorPane customer_page;

    @FXML
    private Button prev_button;

    @FXML
    private TableView<Expert> lista;

    @FXML
    private TableColumn<Expert, String> lista_name;

    @FXML
    private TableColumn<Expert, String> lista_proffession;

    @FXML
    private TableColumn<Expert, String> lista_status;

    @FXML
    private Button ListExpertButton;

    @FXML
    void ListExpertButtonPushed(ActionEvent event) throws Exception {
        refresh();
        lista.refresh();
    }

    @FXML
    void prevbuttonPushed(ActionEvent event) {
        customer_page.setVisible(false);
        expert_page.setVisible(false);
        admin_page.setVisible(false);
        main_page.setVisible(true);
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Szakember oldal státusz választó
        StatusCheckBox.getItems().add(StatusType.ELFOGLALT);
        StatusCheckBox.getItems().add(StatusType.SZABAD);

        //Ügyfél oldal listázás
        lista_name.setCellValueFactory(new PropertyValueFactory<Expert, String>("name"));
        lista_proffession.setCellValueFactory(new PropertyValueFactory<Expert, String>("profession"));
        lista_status.setCellValueFactory(new PropertyValueFactory<Expert, String>("status"));
        lista.setItems(row);

        //Admin oldal listázás
        lista_name1.setCellValueFactory(new PropertyValueFactory<Expert, String>("name"));
        lista_proffession1.setCellValueFactory(new PropertyValueFactory<Expert, String>("profession"));
        lista_status1.setCellValueFactory(new PropertyValueFactory<Expert, String>("status"));
        lista1.setItems(row);
    }
}
