package bibliotek_02;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Oscar Wika
 */
public class AddBorrowerView {

    //Fields
    private Stage window;
    private Borrower newBorrower = null;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField telephoneField;

    /**
     * Constructor, creates a AlertWindow that is used to display errors.
     */
    public AddBorrowerView() {
        //alert = new AlertWindow();
    }

    /**
     * Primary method in the class. Creates the stage and scene. returns a borrower
     * object when ok is pressed or null if window is closed.
     *
     * @return Borrower object or null if window is closed.
     */
    public Borrower display() {
        window = new Stage();
        window.setTitle("Add borrower");
        window.setAlwaysOnTop(true);
        window.initModality(Modality.APPLICATION_MODAL);

        //Creates the containers for the window.
        BorderPane layout = new BorderPane();
        HBox bottomContainer = createBottomContainer();
        GridPane centerContainer = createCenterContainer();

        //Set position of containers
        layout.setCenter(centerContainer);
        layout.setBottom(bottomContainer);

        //Set scene
        Scene scene = new Scene(layout);
        window.setScene(scene);

        //size stage to scene and displays the stage.
        window.sizeToScene();
        window.showAndWait();

        return newBorrower;
    }

    /**
     * Creates the bottom container of the stage.
     *
     * @return HBox with button nodes.
     */
    private HBox createBottomContainer() {
        HBox bottomContainer = new HBox(10);
        bottomContainer.setPadding(new Insets(10, 10, 10, 10));

        Button confirmButton = new Button("Add borrower");
        confirmButton.setDefaultButton(true);

        Button cancelButton = new Button("Cancel");

        confirmButton.setOnAction((ActionEvent e) -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String telephone = telephoneField.getText();
            newBorrower = new Borrower(firstName, lastName, telephone);
            window.close();
        });
        cancelButton.setOnAction((ActionEvent e) -> {
            newBorrower = null;
            window.close();
        });

        bottomContainer.getChildren().addAll(confirmButton, cancelButton);

        return bottomContainer;
    }

    /**
     * Creates the center container of the stage. Filled with TextFields and
     * Labels
     *
     * @return GridPane with TextField and Label nodes.
     */
    private GridPane createCenterContainer() {
        GridPane centerContainer = new GridPane();

        centerContainer.setHgap(20);
        centerContainer.setVgap(20);
        centerContainer.setPadding(new Insets(10, 10, 10, 10));

        firstNameField = new TextField();
        lastNameField = new TextField();
        telephoneField = new TextField();

        centerContainer.add(new Label("First name"), 0, 0);
        centerContainer.add(firstNameField, 1, 0);
        centerContainer.add(new Label("Last name"), 0, 1);
        centerContainer.add(lastNameField, 1, 1);
        centerContainer.add(new Label("Telephone"), 0, 2);
        centerContainer.add(telephoneField, 1, 2);

        return centerContainer;
    }
}