package bibliotek_02;

import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;

/**
 *
 * @author Thomas Todal, Oscar Wika
 */
public class GUI extends Application
{

    private TextField searchBooks;
    private TableView tableViewUtlanTop;
    private TableView tableViewKopi;
    private TableView<Inventory>tableViewInventory;
    private TableView<Librarian> tableViewLibrarian;
    private TableView<Borrower> tableViewLåntaker;

    // Filler for the Librarian table
    final ObservableList<Librarian> librarianList = FXCollections.observableArrayList
        (
            new Librarian("1", "Smith", "Smith"),
            new Librarian("2", "Johnson", "Johnson"),
            new Librarian("3", "Williams", "Ethan-Williams"),
            new Librarian("4", "Jones", "Emma"),
            new Librarian("5", "Brown", "Michael")
    );
    //Filler for the Inventory table
    final ObservableList<Inventory> bookList = FXCollections.observableArrayList
            (new Inventory("1", "Forest Gum", "10"),
                new Inventory("2", "Google", "22"),
                new Inventory("3", "Web Design", "15"),
                new Inventory("4", "SQL", "4"),
                new Inventory("5", "WoW ProTip", "100")
                    
            );
    
    final ObservableList<Borrower> borrowerList = FXCollections.observableArrayList
        (new Borrower("John", "Swagmeister", "99911888"),
            new Borrower("Peter", "Toppris", "33399111"),
            new Borrower("Lise", "Imsdal", "99933222"),
            new Borrower("Cristiano", "Google", "88877333"),
            new Borrower("Del", "Piero", "88855222")
    );

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void stop()
    {
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Window
        BorderPane mainBorderPane = new BorderPane();
        // Menu
        TabPane tabPane = createTabPane();

        mainBorderPane.setCenter(tabPane);

        Scene scene = new Scene(mainBorderPane, 800, 450);
        scene.getStylesheets().add(getClass().getResource("Stylesheet.css").toExternalForm());
        primaryStage.setTitle("Bibliotek X");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Close window confirmation
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            doExitApplication();
        });
    }

    /**
     * Creates the "Utlån" tab.
     *
     * @return The "Utlån" tab.
     */
    private Tab createUtlanTab()
    {
        Tab utlanTab = new Tab("Utlån");
        BorderPane utlanBorderPane = new BorderPane();
        VBox utlanVBoxTop = createUtlanVBoxTop();
        BorderPane utlanBorderPaneBottom = createUtlanBorderPaneBottom();

        utlanTab.setContent(utlanBorderPane);
        utlanBorderPane.setTop(utlanVBoxTop);
        utlanBorderPane.setBottom(utlanBorderPaneBottom);

        utlanTab.setClosable(false);

        return utlanTab;
    }

    /**
     * Creates the "Kopi" tab.
     *
     * @return Returns the "Kopi" tab.
     */
    private Tab createKopiTab()
    {
        Tab kopiTab = new Tab("Kopi");
        BorderPane kopiBorderPane = new BorderPane();
        HBox kopiHBox = createKopiHBox();
        BorderPane utlanBorderPaneBottom = createUtlanBorderPaneBottom();

        kopiTab.setContent(kopiBorderPane);
        kopiBorderPane.setTop(kopiHBox);
        kopiBorderPane.setBottom(utlanBorderPaneBottom);

        kopiTab.setClosable(false);

        return kopiTab;
    }

    /**
     * Creates the "Inventory" tab.
     *
     * @return Returns the "Inventory" tab.
     */
    private Tab createInventoryTab()
    {
        Tab inventoryTab = new Tab("Beholdning");
        BorderPane inventoryBorderPane = new BorderPane();
        HBox inventoryHBox = createInventoryHBox();
        BorderPane utlanBorderPaneBottom = createUtlanBorderPaneBottom();

        inventoryTab.setContent(inventoryBorderPane);
        inventoryBorderPane.setTop(inventoryHBox);
        inventoryBorderPane.setBottom(utlanBorderPaneBottom);

        inventoryTab.setClosable(false);

        return inventoryTab;
    }

    /**
     * Creates the "Borrower" tab.
     *
     * @return Returns the "Borrower" tab.
     */
    private Tab createBorrowerTab()
    {
        Tab borrowerTab = new Tab("Låntaker");
        BorderPane borrowerBorderPane = new BorderPane();
        HBox borrowerHBox = createBorrowerHBox();
        BorderPane utlanBorderPaneBottom = createUtlanBorderPaneBottom();

        borrowerTab.setContent(borrowerBorderPane);
        borrowerBorderPane.setTop(borrowerHBox);
        borrowerBorderPane.setBottom(utlanBorderPaneBottom);

        borrowerTab.setClosable(false);

        return borrowerTab;
    }

    /**
     * Creates the "Librarian" tab.
     *
     * @return Returns the "Librarian" tab.
     */
    private Tab createLibrarianTab()
    {
        Tab librarianTab = new Tab("Bibliotekar");
        BorderPane librarianBorderPane = new BorderPane();
        HBox librarianHBox = createLibrarianHBox();
        BorderPane utlanBorderPaneBottom = createUtlanBorderPaneBottom();

        librarianTab.setContent(librarianBorderPane);
        librarianBorderPane.setTop(librarianHBox);
        librarianBorderPane.setBottom(utlanBorderPaneBottom);

        librarianTab.setClosable(false);

        return librarianTab;
    }

    /**
     * Creates the tab pane.
     *
     * @return Returns the tab pane.
     */
    private TabPane createTabPane()
    {
        Tab utlan = createUtlanTab();
        Tab kopi = createKopiTab();
        Tab inventory = createInventoryTab();
        Tab borrower = createBorrowerTab();
        Tab librarian = createLibrarianTab();

        TabPane tabPane = new TabPane(utlan, kopi, inventory, borrower, librarian);

        return tabPane;
    }

    /**
     * Creates a search bar in the "Utlån" tab.
     *
     * @return Return the search bar.
     */
    private VBox createUtlanVBoxTop()
    {
        VBox utlanVBoxTop = new VBox();
        searchBooks = new TextField();
        tableViewUtlanTop = new TableView();

        searchBooks.setPromptText("Søk etter Bok-ID, ISBN, Tittel, Forfatter...");

        TableColumn fornavnCol = new TableColumn("N/A");
        TableColumn etternavnCol = new TableColumn("N/A");
        TableColumn telefonCol = new TableColumn("N/A");
        tableViewUtlanTop.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);
        tableViewUtlanTop.setMinSize(450, 150);
        tableViewUtlanTop.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        utlanVBoxTop.getChildren().add(searchBooks);
        utlanVBoxTop.getChildren().add(tableViewUtlanTop);

        return utlanVBoxTop;
    }

    /**
     * Creates the table in the "Kopi" tab.
     *
     * @return Returns a HBox containing a table for the "Kopi" tab.
     */
    private HBox createKopiHBox()
    {
        HBox kopiHBox = new HBox();
        tableViewKopi = new TableView();

        TableColumn fornavnCol = new TableColumn("N/A");
        TableColumn etternavnCol = new TableColumn("N/A");
        TableColumn telefonCol = new TableColumn("N/A");
        tableViewKopi.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);

        tableViewKopi.setMinSize(450, 175);
        tableViewKopi.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        kopiHBox.getChildren().add(tableViewKopi);

        return kopiHBox;
    }

    /**
     * Creates the table in the "Inventory" tab.
     *
     * @return Returns a HBox containing a table for the "Inventory" tab.
     */
    private HBox createInventoryHBox()
    {
        HBox inventoryHBox = new HBox();
        tableViewInventory = new TableView();
        
        TableColumn fornavnCol = new TableColumn("ID");
        fornavnCol.setCellValueFactory(new PropertyValueFactory<>("BookID"));
        
        TableColumn etternavnCol = new TableColumn("Bok Navn");
        etternavnCol.setCellValueFactory(new PropertyValueFactory<>("BookName"));
        
        TableColumn telefonCol = new TableColumn("Antall");
        telefonCol.setCellValueFactory(new PropertyValueFactory<>("BookQuantity"));
        
        tableViewInventory.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);
        
        tableViewInventory.setItems(bookList);
        tableViewInventory.setMinSize(450, 175);
        tableViewInventory.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        inventoryHBox.getChildren().add(tableViewInventory);

        return inventoryHBox;
    }

    /**
     * Creates the table in the "Borrower" tab.
     *
     * @return Returns a HBox containing a table for the "Borrower" tab.
     */
    private HBox createBorrowerHBox()
    {
        HBox borrowerHBox = new HBox();
        tableViewLåntaker = new TableView();

        TableColumn fornavnCol = new TableColumn("Fornavn");
        fornavnCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));

        TableColumn etternavnCol = new TableColumn("Etternavn");
        etternavnCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));

        TableColumn telefonCol = new TableColumn("Telefon");
        telefonCol.setCellValueFactory(new PropertyValueFactory<>("Telephone"));

        tableViewLåntaker.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);

        tableViewLåntaker.setItems(borrowerList);
        tableViewLåntaker.setMinSize(450, 175);
        tableViewLåntaker.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        borrowerHBox.getChildren().add(tableViewLåntaker);

        return borrowerHBox;
    }

    /**
     * Creates the table in the "Librarian" tab.
     *
     * @return Returns a HBox containing a table for the "Librarian" tab.
     */
    private HBox createLibrarianHBox()
    {
        HBox librarianHBox = new HBox();
        tableViewLibrarian = new TableView();

        TableColumn librarianIDCol = new TableColumn("AnsattID");
        librarianIDCol.setCellValueFactory(new PropertyValueFactory<>("ansattID"));

        TableColumn fornavnCol = new TableColumn("Fornavn");
        fornavnCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));

        TableColumn etternavnCol = new TableColumn("Etternavn");
        etternavnCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));

        tableViewLibrarian.getColumns().addAll(librarianIDCol, fornavnCol, etternavnCol);

        tableViewLibrarian.setItems(librarianList);
        tableViewLibrarian.setMinSize(450, 175);
        tableViewLibrarian.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        librarianHBox.getChildren().add(tableViewLibrarian);

        return librarianHBox;
    }

    /**
     *
     * @return
     */
    private BorderPane createUtlanBorderPaneBottom()
    {
        BorderPane utlanBorderPaneBottom = new BorderPane();
        VBox utlanVBoxBottomLeft = new VBox();
        VBox utlanVBoxBottomRight = new VBox();

        utlanBorderPaneBottom.setLeft(utlanVBoxBottomLeft);
        utlanBorderPaneBottom.setRight(utlanVBoxBottomRight);

        return utlanBorderPaneBottom;
    }

    /**
     * Exit the application. Displays a confirmation dialog.
     */
    private void doExitApplication()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Exit");
        alert.setHeaderText("Unsaved files");
        alert.setContentText("Our program doesn't support file saving at the moment."
                + "\nDo you still wish to exit the application?");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        //Deactivate Defaultbehavior for yes-Button:
        Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
        yesButton.setDefaultButton(true);

        //Activate Defaultbehavior for no-Button:
        Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
        noButton.setDefaultButton(false);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.YES) {
            System.exit(0);
        } else {
            // ... user chose CANCEL or closed the dialog
            // then do nothing.
        }
    }
}
