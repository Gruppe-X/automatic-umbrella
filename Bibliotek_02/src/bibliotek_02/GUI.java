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

/**
 *
 * @author Thomas Todal, Oscar Wika
 */
public class GUI extends Application
{

    private TextField searchBooks;
    private TextField searchCopy;
    private TextField searchInventory;
    private TextField searchBorrower;
    private TextField searchLibrarian;
    private TableView tableViewLoansTop;
    private TableView tableViewBook;
    private TableView<Inventory>tableViewInventory;
    private TableView<Librarian> tableViewLibrarian;
    private TableView<Borrower> tableViewBorrower;       

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
        (
                new Inventory("1", "Forest Gum", "10"),
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
     * Creates the "Loans" tab.
     *
     * @return The "Loans" tab.
     */
    private Tab createLoansTab()
    {
        Tab loansTab = new Tab("Utlån");
        BorderPane loansBorderPane = new BorderPane();
        VBox loansVBoxTop = createLoansVBoxTop();
        BorderPane loansnBorderPaneBottom = createLoansBorderPaneBottom();

        loansTab.setContent(loansBorderPane);
        loansBorderPane.setTop(loansVBoxTop);
        loansBorderPane.setBottom(loansnBorderPaneBottom);

        loansTab.setClosable(false);

        return loansTab;
    }

    /**
     * Creates the "Book" tab.
     *
     * @return Returns the "Book" tab.
     */
    private Tab createBookTab()
    {
        Tab bookTab = new Tab("Bok");
        BorderPane bookBorderPane = new BorderPane();
        VBox bookVBox = createBookVBox();
        BorderPane loansBorderPaneBottom = createLoansBorderPaneBottom();

        bookTab.setContent(bookBorderPane);
        bookBorderPane.setTop(bookVBox);
        bookBorderPane.setBottom(loansBorderPaneBottom);
        
        bookTab.setClosable(false);

        return bookTab;
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
        VBox inventoryVBox = createInventoryVBox();
        BorderPane loansBorderPaneBottom = createLoansBorderPaneBottom();

        inventoryTab.setContent(inventoryBorderPane);
        inventoryBorderPane.setTop(inventoryVBox);
        inventoryBorderPane.setBottom(loansBorderPaneBottom);

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
        BorderPane loansBorderPaneBottom = createLoansBorderPaneBottom();

        borrowerTab.setContent(borrowerBorderPane);
        borrowerBorderPane.setTop(borrowerHBox);
        borrowerBorderPane.setBottom(loansBorderPaneBottom);

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
        BorderPane loansBorderPaneBottom = createLoansBorderPaneBottom();

        librarianTab.setContent(librarianBorderPane);
        librarianBorderPane.setTop(librarianHBox);
        librarianBorderPane.setBottom(loansBorderPaneBottom);

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
        Tab loans = createLoansTab();
        Tab book = createBookTab();
        Tab inventory = createInventoryTab();
        Tab borrower = createBorrowerTab();
        Tab librarian = createLibrarianTab();

        TabPane tabPane = new TabPane(loans, book, inventory, borrower, librarian);

        return tabPane;
    }

    /**
     * Creates a search bar in the "Loans" tab.
     *
     * @return Return the search bar.
     */
    private VBox createLoansVBoxTop()
    {
        VBox loansVBoxTop = new VBox();
        searchBooks = new TextField();
        tableViewLoansTop = new TableView();

        searchBooks.setPromptText("Søk etter Bok-ID, ISBN, Tittel, Forfatter...");

        TableColumn fornavnCol = new TableColumn("N/A");
        TableColumn etternavnCol = new TableColumn("N/A");
        TableColumn telefonCol = new TableColumn("N/A");
        tableViewLoansTop.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);
        tableViewLoansTop.setMinSize(450, 150);
        tableViewLoansTop.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loansVBoxTop.getChildren().add(searchBooks);
        loansVBoxTop.getChildren().add(tableViewLoansTop);

        return loansVBoxTop;
    }

    /**
     * Creates the table in the "Book" tab.
     *
     * @return Returns a HBox containing a table for the "Book" tab.
     */
    private VBox createBookVBox()
    {
        VBox bookVBox = new VBox();
        tableViewBook = new TableView();
        
        searchCopy = new TextField();
        Button addButton = new Button("Add");
        Button removeButton = new Button("Remove");
        
        searchCopy.setPromptText("Search after copy");

        TableColumn fornavnCol = new TableColumn("N/A");
        TableColumn etternavnCol = new TableColumn("N/A");
        TableColumn telefonCol = new TableColumn("N/A");

        
        bookVBox.getChildren().add(removeButton);
        bookVBox.getChildren().add(addButton);
        bookVBox.getChildren().add(searchCopy);
        bookVBox.getChildren().add(tableViewBook);

        tableViewBook.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);

        tableViewBook.setMinSize(450, 175);
        tableViewBook.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        bookVBox.getChildren().add(searchCopy);
        bookVBox.getChildren().add(tableViewBook);

        return bookVBox;
    }

    /**
     * Creates the table in the "Inventory" tab.
     *
     * @return Returns a HBox containing a table for the "Inventory" tab.
     */
    private VBox createInventoryVBox()
    {
        VBox inventoryVBox = new VBox();
        tableViewInventory = new TableView();
        searchInventory = new TextField();
        
        searchInventory.setPromptText("Search through the inventory");
        
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
        inventoryVBox.getChildren().add(tableViewInventory);

        return inventoryVBox;
    }

    /**
     * Creates the table in the "Borrower" tab.
     *
     * @return Returns a HBox containing a table for the "Borrower" tab.
     */
    private HBox createBorrowerHBox()
    {
        HBox borrowerHBox = new HBox();
        tableViewBorrower = new TableView();

        TableColumn fornavnCol = new TableColumn("Fornavn");
        fornavnCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));

        TableColumn etternavnCol = new TableColumn("Etternavn");
        etternavnCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));

        TableColumn telefonCol = new TableColumn("Telefon");
        telefonCol.setCellValueFactory(new PropertyValueFactory<>("Telephone"));

        tableViewBorrower.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);

        tableViewBorrower.setItems(borrowerList);
        tableViewBorrower.setMinSize(450, 175);
        tableViewBorrower.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        borrowerHBox.getChildren().add(tableViewBorrower);

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
    private BorderPane createLoansBorderPaneBottom()
    {
        BorderPane loansBorderPaneBottom = new BorderPane();
        VBox loansVBoxBottomLeft = new VBox();
        VBox loansVBoxBottomRight = new VBox();

        loansBorderPaneBottom.setLeft(loansVBoxBottomLeft);
        loansBorderPaneBottom.setRight(loansVBoxBottomRight);

        return loansBorderPaneBottom;
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
