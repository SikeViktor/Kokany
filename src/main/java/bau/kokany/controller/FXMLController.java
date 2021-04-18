package bau.kokany.controller;

import java.net.URL;
import java.util.List;
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
import javafx.scene.layout.AnchorPane;

public class FXMLController implements Initializable {

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
    private AnchorPane customer_page;

    @FXML
    private AnchorPane expert_page;

    @FXML
    private Button prev_button1;

    @FXML
    private Button prev_button;

    @FXML
    private Button newExpertButton;

    @FXML
    private ChoiceBox<StatusType> StatusCheckBox = new ChoiceBox<>();

    @FXML
    private TextField nameText;

    @FXML
    private TextField professionText;

    @FXML
    private TableView<String> lista = new TableView<>();


    @FXML
    private Button ListExpertButton;

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
    void newExpertButtonPushed(ActionEvent event) throws Exception {
        Expert expert = new Expert();
        expert.setName(nameText.getText());
        expert.setProfession(professionText.getText());
        expert.setStatus(StatusCheckBox.getValue());

        try(ExpertDAO eDAO = new JPAExpertDAO();) {
            eDAO.saveExpert(expert);
        }
        System.out.println("Sikeres mentés!");
    }

    @FXML
    void ListExpertButtonPushed(ActionEvent event) throws Exception {
        /*ObservableList<String> row = FXCollections.observableArrayList();
        try(ExpertDAO eDAO = new JPAExpertDAO();) {
            List<Expert> expertList = eDAO.getExperts();
            for(Expert e : expertList) {
                row.add(e.getName());
                row.add(e.getProfession());
                row.add(e.getStatus().toString());
            }
            System.out.println(row);
            lista.setItems(row);
        }*/
    }

    @FXML
    void prevbuttonPushed(ActionEvent event) {
        customer_page.setVisible(false);
        expert_page.setVisible(false);
        main_page.setVisible(true);
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
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        StatusCheckBox.getItems().add(StatusType.ELFOGLALT);
        StatusCheckBox.getItems().add(StatusType.SZABAD);
    }
}
