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
public class EditBookView {

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
    public EditBookView() {
        //alert = new AlertWindow();
    }

    /**
     * Primary method in the class. Creates the stage and scene. returns a book
     * object when ok is pressed or null if window is closed.
     *
     * @param book book to edit.
     * @return Copy object or null if window is closed.
     */
    public InventoryBook display(InventoryBook book) {
        window = new Stage();
        window.setTitle("Edit book");
        window.initModality(Modality.APPLICATION_MODAL);

        //Creates the containers for the window.
        BorderPane layout = new BorderPane();
        HBox bottomContainer = createBottomContainer();
        GridPane centerContainer = createCenterContainer(book);

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

        Button confirmButton = new Button("Add book");
        confirmButton.setDefaultButton(true);

        Button cancelButton = new Button("Cancel");

        confirmButton.setOnAction((ActionEvent e) -> {
            try{
                String ISBN = ISBNField.getText();
                String title = titleField.getText();
                String publisher = publisherField.getText();
                String author = authorField.getText();
                String edition = editionField.getText();
                Integer.parseInt(edition);
                String released = releasedField.getText();
                Integer.parseInt(released);
                newBook = new InventoryBook(ISBN, title, author, edition, released, publisher, "-1", "-1");
                window.close();
            } catch (NumberFormatException NFEx) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid edition or publishing year. Must be integers");
                alert.setContentText(NFEx.getMessage());
                alert.showAndWait();
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
    private GridPane createCenterContainer(InventoryBook book) {
        GridPane centerContainer = new GridPane();

        centerContainer.setHgap(20);
        centerContainer.setVgap(20);
        centerContainer.setPadding(new Insets(10, 10, 10, 10));

        ISBNField = new TextField(book.getBookID());
        ISBNField.setDisable(true);
        titleField = new TextField(book.getBookName());
        publisherField = new TextField(book.getBookPublisher());
        authorField = new TextField(book.getBookAuthor());
        editionField =new TextField(book.getBookEdition());
        releasedField = new TextField(book.getBookYear());

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
