package bibliotek_02;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
public class EditLibrarianView {

    //Fields
    private Stage window;
    private Librarian editedLibrarian = null;
    private TextField librarianIdField;
    private TextField firstNameField;
    private TextField lastNameField;

    /**
     * Constructor, creates a AlertWindow that is used to display errors.
     */
    public EditLibrarianView() {
        //alert = new AlertWindow();
    }

    /**
     * Primary method in the class. Creates the stage and scene. returns a book
     * object when ok is pressed or null if window is closed.
     *
     * @param book book to edit.
     * @return Copy object or null if window is closed.
     */
    public Librarian display(Librarian librarian) {
        window = new Stage();
        window.setTitle("Edit book");
        window.initModality(Modality.APPLICATION_MODAL);

        //Creates the containers for the window.
        BorderPane layout = new BorderPane();
        HBox bottomContainer = createBottomContainer();
        GridPane centerContainer = createCenterContainer(librarian);

        //Set position of containers
        layout.setCenter(centerContainer);
        layout.setBottom(bottomContainer);

        //Set scene
        Scene scene = new Scene(layout);
        window.setScene(scene);

        //size stage to scene and displays the stage.
        window.sizeToScene();
        window.showAndWait();

        return editedLibrarian;
    }

    /**
     * Creates the bottom container of the stage.
     *
     * @return HBox with button nodes.
     */
    private HBox createBottomContainer() {
        HBox bottomContainer = new HBox(10);
        bottomContainer.setPadding(new Insets(10, 10, 10, 10));

        Button confirmButton = new Button("Add book");
        confirmButton.setDefaultButton(true);

        Button cancelButton = new Button("Cancel");

        confirmButton.setOnAction((ActionEvent e) -> {
                String libraianId = librarianIdField.getText();
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                editedLibrarian = new Librarian(libraianId, firstName, lastName);
                window.close();
            
        });
        cancelButton.setOnAction((ActionEvent e) -> {
            editedLibrarian = null;
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
    private GridPane createCenterContainer(Librarian librarian) {
        GridPane centerContainer = new GridPane();

        centerContainer.setHgap(20);
        centerContainer.setVgap(20);
        centerContainer.setPadding(new Insets(10, 10, 10, 10));

        librarianIdField = new TextField(librarian.getEmployeeID());
        librarianIdField.setDisable(true);
        firstNameField = new TextField(librarian.getFirstName());
        lastNameField = new TextField(librarian.getLastName());

        centerContainer.add(new Label("Librarian ID"), 0, 0);
        centerContainer.add(librarianIdField, 1, 0);
        centerContainer.add(new Label("First Name"), 0, 1);
        centerContainer.add(firstNameField, 1, 1);
        centerContainer.add(new Label("Last Name"), 0, 2);
        centerContainer.add(lastNameField, 1, 2);

        return centerContainer;
    }
}
