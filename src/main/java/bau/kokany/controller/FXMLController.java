package bau.kokany.controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import bau.kokany.model.Expert;
import bau.kokany.model.ExpertDAO;
import bau.kokany.model.JPAExpertDAO;
import bau.kokany.model.StatusType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;


public class FXMLController implements Initializable {

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
        main_page.setVisible(false);
        customer_page.setVisible(true);
    }

    @FXML
    void expertbuttonPushed(ActionEvent event) {
        main_page.setVisible(false);
        expert_page.setVisible(true);
    }

    @FXML
    void pwdPassed(ActionEvent event) {
        if(!adminpwd.getText().equals("admin")) {
            adminpwdLabel.setVisible(true);
            adminpwd.clear();
        }
        else {
            adminpwdLabel.setVisible(false);

        }

            main_page.setVisible(false);
            admin_page.setVisible(true);

            try(ExpertDAO eDAO = new JPAExpertDAO();) {
                List<Expert> expertList = eDAO.getExperts();
                for(Expert e : expertList) {
                    if(row.size() < expertList.size()) {
                        row.add(e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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

    @FXML
    void admin_delete_buttonPushed(ActionEvent event) throws Exception {
        Expert selectedItem = lista1.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Jóváhagyás");
        alert.setHeaderText("Törölni akarod a szakembert.");
        alert.setContentText("Biztosan törlöd?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try(ExpertDAO eDAO = new JPAExpertDAO();) {
                eDAO.deleteExpert(selectedItem);
                lista1.getItems().remove(selectedItem);
            }
        } else {

        }

    }

    @FXML
    void admin_modify_buttonPushed(ActionEvent event) {

    }

    @FXML
    void admin_new_buttonPushed(ActionEvent event) {
        /*Dialog<Triplet<String, String, StatusType>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Look, a Custom Login Dialog");*/

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

    ObservableList<Expert> row = FXCollections.observableArrayList();

    @FXML
    private Button ListExpertButton;

    @FXML
    void ListExpertButtonPushed(ActionEvent event) throws Exception {
        try(ExpertDAO eDAO = new JPAExpertDAO();) {
            List<Expert> expertList = eDAO.getExperts();
            for(Expert e : expertList) {
                if(row.size() < expertList.size()) {
                    row.add(e);
                }
            }
        }
    }

    @FXML
    void prevbuttonPushed(ActionEvent event) {
        customer_page.setVisible(false);
        expert_page.setVisible(false);
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
    }
}
