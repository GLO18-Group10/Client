/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.GUI;

import client.Acquaintance.IGUI;
import client.Acquaintance.ILogic;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author antonio
 */
public class adminController implements Initializable {

    IGUI gui;
    ILogic logic;
    @FXML
    AnchorPane adminOverview;
    ListView Listview;
    @FXML
    Pane PaneBar;
    @FXML
    Button Close_Button;
    TextField SearchField;
    @FXML
    Button LogoutButton;
    @FXML
    Button Open_Button;
    @FXML
    Button CreateButton;
    @FXML
    TextField FirstNameField;
    @FXML
    TextField LastnameField;
    @FXML
    TextField CPRField;
    @FXML
    TextField EmailField;
    @FXML
    TextField PhoneField;
    ArrayList<TextField> textFields;
    @FXML
    private TextField AddressField;
    @FXML
    private DatePicker birthdayFieldDatePicker;
    @FXML
    private ListView<?> customerAccountsListView;
    @FXML
    private TextArea statusTextArea;
    @FXML
    private Button updateIDButton;
    @FXML
    private Label activateIDLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gui = GUIrun.getInstance();
        logic = GUIrun.getLogic();
        textFields = new ArrayList<>();
        textFields.add(CPRField);
        textFields.add(FirstNameField);
        textFields.add(LastnameField);
        textFields.add(PhoneField);
        textFields.add(AddressField);
        textFields.add(EmailField);
        getIdForList();
    }

    @FXML
    private void CreateButtonHandler(ActionEvent event) {
        if (isEmptyOrContainsIllegalChar(textFields) || birthdayFieldDatePicker.getValue() == null || !isValid(CPRField.getText()) || !isValid(PhoneField.getText())) {
        } else {
            String ID = "C" + CPRField.getText();
            String name = FirstNameField.getText() + " " + LastnameField.getText();
            String birthdayTest = birthdayFieldDatePicker.getValue().toString();
            String phoneNumber = PhoneField.getText();
            String address = AddressField.getText();
            String email = EmailField.getText();
            String password = logic.getAdmin().generatePassword();
            String success = createCustomer(ID, name, birthdayTest, phoneNumber, address, email, password);
            if (success.equalsIgnoreCase("true")) {
                logic.sendMail(ID, email, name, password);
            }
        }
    }

    private String createCustomer(String ID, String name, String birthday, String phoneNumber, String address, String email, String password) {
        return logic.toProtocol07(ID, name, birthday, phoneNumber, address, email, password);
    }

    @FXML
    private void logoutHandler(ActionEvent event) {
        if (logic.logout().equalsIgnoreCase("true")) {
            try {
                Parent nextView = FXMLLoader.load(getClass().getResource("login.fxml"));
                Scene newScene = new Scene(nextView);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(newScene);
                stage.show();
            } catch (IOException ex) {
                System.out.println("Error; logoutHandler(admin)");
                ex.printStackTrace();
            }
        } else if (logic.logout().equalsIgnoreCase("false")) {
            System.out.println("could not log out"); //this should be changed to a label in the GUI
        }
    }

    private boolean isEmptyOrContainsIllegalChar(ArrayList<TextField> textFieldArray) {
        boolean isEmpty = false;
        statusTextArea.clear();
        for (TextField textField : textFieldArray) {
            if (textField.getText().trim().isEmpty()) {
                String[] textFieldEmpty = textField.toString().split(",");
                statusTextArea.appendText(textFieldEmpty[0].substring(13) + " IS EMPTY\n");
                isEmpty = true;
            }
            if (textField.getText().contains(";")) {
                String[] textFieldEmpty = textField.toString().split(",");
                statusTextArea.appendText(textFieldEmpty[0].substring(13) + " CANNOT CONTAIN ;\n");
                isEmpty = true;
            }
        }
        return isEmpty;
    }

    private boolean isValid(String input) {
        char[] inputCharArray = input.toCharArray();
        for (int i = 0; i < inputCharArray.length; i++) {
            if (!Character.isDigit(inputCharArray[i])) {
                statusTextArea.appendText("Only digits in phone field and CPR");
                return false;
            }
        }
        return true;
    }

    @FXML
    private void getIdForList() {
        String[] data = logic.getAdmin().getCustomerId();
        ObservableList list = FXCollections.observableArrayList(data);
        customerAccountsListView.setItems(list);
    }

    @FXML
    private void CloseCustomerAccount(ActionEvent event) {
        if (event.getSource() == Close_Button) {
            String response = logic.getAdmin().closeCustomerAccount((String) customerAccountsListView.getSelectionModel().getSelectedItem());
            activateIDLabel.setText("Deactivation: " + response);
        }

    }

    @FXML
    private void openCustomerAccount(ActionEvent event) {
        if (event.getSource() == Open_Button) {
            String response = logic.getAdmin().openCustomerAccount((String) customerAccountsListView.getSelectionModel().getSelectedItem());
            activateIDLabel.setText("Activation: " + response); 
        }
    }
}
