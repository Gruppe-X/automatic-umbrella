package bibliotek_02;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

/**
 *
 * Class represents window used to add new books, gives user text field to fill
 * in information on new book.
 *
 * @author Robert Rishaug, Vinh Tran and Kjetil Yndestad
 * @version 11.10.2016
 */
public class ChooseEmployeeView {

    //Fields
    private Stage window;
    private DatabaseHandler handler;
    
    private TextField employeeIdField;
    
    private Librarian librarian;

    /**
     * Constructor, creates a AlertWindow that is used to display errors.
     */
    public ChooseEmployeeView(DatabaseHandler handler) {
        //alert = new AlertWindow();
        this.handler = handler;
    }

    /**
     * Primary method in the class. Creates the stage and scene. returns a book
     * object when ok is pressed or null if window is closed.
     *
     * @return Copy object or null if window is closed.
     */
    public Librarian display() {
        window = new Stage();
        window.setTitle("Login");
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

        return null;
    }

    /**
     * Creates the bottom container of the stage.
     *
     * @return HBox with button nodes.
     */
    private HBox createBottomContainer() {
        HBox bottomContainer = new HBox(10);
        bottomContainer.setPadding(new Insets(10, 10, 10, 10));

        Button confirmButton = new Button("Login");
        confirmButton.setDefaultButton(true);

        Button cancelButton = new Button("Cancel");

        confirmButton.setOnAction((ActionEvent e) -> {
            String emplyeeId = employeeIdField.getText();
            librarian = handler.getLibrarianById(emplyeeId);
            window.close();
        });
        cancelButton.setOnAction((ActionEvent e) -> {
            librarian = null;
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

        employeeIdField = new TextField();
        
        centerContainer.add(new Label("Enter librarian ID"), 0, 0);
        centerContainer.add(new Label("Librarian ID"), 0, 1);
        centerContainer.add(employeeIdField, 1, 1);

        return centerContainer;
    }
}
