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
public class AddBookView {

    //Fields
    private Stage window;
    private InventoryBook newBook = null;
    private TextField ISBNField;
    private TextField titleField;
    private TextField publisherField;
    private TextField authorField;
    private TextField editionField;
    private TextField releasedField;

    /**
     * Constructor, creates a AlertWindow that is used to display errors.
     */
    public AddBookView() {
        //alert = new AlertWindow();
    }

    /**
     * Primary method in the class. Creates the stage and scene. returns a book
     * object when ok is pressed or null if window is closed.
     *
     * @return Copy object or null if window is closed.
     */
    public InventoryBook display() {
        window = new Stage();
        window.setTitle("Legg til bok");
        window.setAlwaysOnTop(false);
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

        return newBook;
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
            String ISBN = ISBNField.getText();
            String title = titleField.getText();
            String publisher = publisherField.getText();
            String author = authorField.getText();
            String edition = editionField.getText();
            String released = releasedField.getText();
            boolean bookValid = false;
                
            try{
               Integer.parseInt(editionField.getText());
               Integer.parseInt(releasedField.getText());
               bookValid = true;
            }catch(NumberFormatException ne)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Feilmelding");
                alert.setHeaderText("Ugyldig");
                alert.setContentText("Ooops, tast inn gylding utgave og årstall");
                alert.showAndWait();
            }
            if(bookValid)
            {    
            newBook = new InventoryBook(ISBN, title, author, edition, released, publisher, "0", "0");
            window.close();
            }
        });
        cancelButton.setOnAction((ActionEvent e) -> {
            newBook = null;
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

        ISBNField = new TextField();
        titleField = new TextField();
        publisherField = new TextField();
        authorField = new TextField();
        editionField =new TextField();
        releasedField = new TextField();

        centerContainer.add(new Label("ISBN"), 0, 0);
        centerContainer.add(ISBNField, 1, 0);
        centerContainer.add(new Label("Title"), 0, 1);
        centerContainer.add(titleField, 1, 1);
        centerContainer.add(new Label("Publisher"), 0, 2);
        centerContainer.add(publisherField, 1, 2);
        centerContainer.add(new Label("Author"), 0, 3);
        centerContainer.add(authorField, 1, 3);
        centerContainer.add(new Label("Edition"), 0, 4);
        centerContainer.add(editionField, 1, 4);
        centerContainer.add(new Label("Release Year"), 0, 5);
        centerContainer.add(releasedField, 1, 5);

        return centerContainer;
    }
}
