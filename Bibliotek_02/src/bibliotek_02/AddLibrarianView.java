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
public class AddLibrarianView {
    
    //Fields
    private Stage window;
    private Librarian newLibrarian = null;
    private TextField employeeIDField;
    private TextField firstNameField;
    private TextField lastNameField;

    /**
     * Constructor, creates a AlertWindow that is used to display errors.
     */
    public AddLibrarianView() {
        //alert = new AlertWindow();
    }

    /**
     * Primary method in the class. Creates the stage and scene. returns a librarian
     * object when ok is pressed or null if window is closed.
     *
     * @return Librarian object or null if window is closed.
     */
    public Librarian display() {
        window = new Stage();
        window.setTitle("Legg til ansatt");
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

        return newLibrarian;
    }

    /**
     * Creates the bottom container of the stage.
     *
     * @return HBox with button nodes.
     */
    private HBox createBottomContainer() {
        HBox bottomContainer = new HBox(10);
        bottomContainer.setPadding(new Insets(10, 10, 10, 10));

        Button confirmButton = new Button("Legg til");
        confirmButton.setDefaultButton(true);

        Button cancelButton = new Button("Avbryt");

        confirmButton.setOnAction((ActionEvent e) -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            newLibrarian = new Librarian(firstName, lastName);
            window.close();
        });
        cancelButton.setOnAction((ActionEvent e) -> {
            newLibrarian = null;
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

        employeeIDField = new TextField();
        firstNameField = new TextField();
        lastNameField = new TextField();
        
        centerContainer.add(new Label("Fornavn"), 0, 0);
        centerContainer.add(firstNameField, 1, 0);
        centerContainer.add(new Label("Etternavn"), 0, 1);
        centerContainer.add(lastNameField, 1, 1);

        return centerContainer;
    }
}