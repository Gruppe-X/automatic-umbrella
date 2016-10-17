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
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import static javafx.application.Application.launch;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;

/**
 *
 * @author Thomas Todal, Oscar Wika
 */
public class GUI extends Application
{

    private Stage primaryStage;

    private DatabaseHandler handler;
    private TextField searchBooks;
    private TextField searchCopy;
    private TextField searchInventory;
    private TextField searchBorrower;
    private TextField searchLibrarian;
    private TableView tableViewLoansTop;
    private TableView tableViewCopy;
    private TableView<Copy> tableViewInventory;
    private TableView<Librarian> tableViewLibrarian;
    private TableView<Borrower> tableViewBorrower;

    private AddBookView addBookView;

    ObservableList<Librarian> librarianList;
    //Filler for the Inventory table
    ObservableList<Copy> bookList = FXCollections.observableArrayList();

    ObservableList<Borrower> borrowerList;

    public GUI()
    {
        handler = new DatabaseHandler();
        borrowerList = FXCollections.observableArrayList(handler.listBorrowers());
        bookList = FXCollections.observableArrayList(handler.listBooks()); //TODO lag listBooks i DatabaseHandler
        librarianList = FXCollections.observableArrayList(handler.listLibrarians());

        addBookView = new AddBookView();
    }

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
        this.primaryStage = primaryStage;
        // Window
        BorderPane mainBorderPane = new BorderPane();
        // Menu
        TabPane tabPane = createTabPane();

        mainBorderPane.setCenter(tabPane);

        Scene scene = new Scene(mainBorderPane);
        scene.getStylesheets().add(getClass().getResource("Stylesheet.css").toExternalForm());
        primaryStage.setTitle("Bibliotek X");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1280);
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
        loansTab.setContent(loansBorderPane);

        VBox content = new VBox();
        VBox loansTopContent = createLoansTopContent();
        HBox loansBottomContent = createLoansBottomContent();
        
        content.getChildren().addAll(loansTopContent, loansBottomContent);
        VBox.setVgrow(loansTopContent, Priority.ALWAYS);
        VBox.setVgrow(loansBottomContent, Priority.ALWAYS);
        loansBorderPane.setCenter(content);
        
        return loansTab;
    }

    private HBox createLoansBottomContent()
    {
        HBox bottomContent = new HBox();
        BorderPane botLeftCont = createLoansBottomLeftContent();
        VBox botRightCont = createLoansBottomRightContent();
        bottomContent.getChildren().addAll(botLeftCont, botRightCont);
        bottomContent.setMinWidth(500);
        HBox.setHgrow(botLeftCont, Priority.ALWAYS);
        HBox.setHgrow(botRightCont, Priority.ALWAYS);

        return bottomContent;
    }

    private BorderPane createLoansBottomLeftContent()
    {
        BorderPane bottomLeftContent = new BorderPane();
        Button addButton = new Button("Legg til");
        Button removeButton = new Button("Fjern");
        HBox buttonsBox = new HBox(addButton, removeButton);

        TableView registeredBooks = new TableView();
        
        registeredBooks.setMinWidth(240);
        registeredBooks.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        bottomLeftContent.setTop(buttonsBox);
        //set table center.
        bottomLeftContent.setCenter(registeredBooks);
        Button registerLoanButton = new Button("Registrer Lån");
        bottomLeftContent.setBottom(registerLoanButton);

        bottomLeftContent.setPadding(new Insets(0, 10, 0, 0));
        bottomLeftContent.setMinWidth(240);

        return bottomLeftContent;
    }

    private VBox createLoansBottomRightContent()
    {
        VBox bottomRightContent = new VBox();
        GridPane topContent = new GridPane();

        topContent.setPadding(new Insets(0, 0, 10, 0));
        //topContent.setGridLinesVisible(true);

        TextField firstNameField = new TextField();
        firstNameField.setPadding(new Insets(5));
        TextField lastNameField = new TextField();
        lastNameField.setPadding(new Insets(5));
        Button findBorrowerButton = new Button("Finn lånetaker");
        topContent.add(new Label("Lånetaker"), 0, 0);
        topContent.add(firstNameField, 0, 1);
        topContent.add(lastNameField, 0, 2);
        topContent.add(findBorrowerButton, 1, 2);

        TableView borrowerTable = new TableView();
        
        borrowerTable.setMinWidth(240);
        borrowerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        borrowerTable.setMinHeight(50);

        bottomRightContent.getChildren().addAll(topContent, borrowerTable);

        bottomRightContent.setPadding(new Insets(0, 0, 0, 10));
        bottomRightContent.setMinWidth(240);

        return bottomRightContent;
    }

    /**
     * Creates the "Copy" tab.
     *
     * @return Returns the "Copy" tab.
     */
    private Tab createCopyTab()
    {
        Tab copyTab = new Tab("Kopi");
        BorderPane copyBorderPane = new BorderPane();

        VBox copyVBox = createCopyVBox();
        //BorderPane loansBorderPaneBottom = createLoansBorderPaneBottom();

        copyTab.setContent(copyBorderPane);
        copyBorderPane.setTop(copyVBox);
        //bookBorderPane.setBottom(loansBorderPaneBottom);

        return copyTab;
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
        //BorderPane loansBorderPaneBottom = createLoansBorderPaneBottom();

        inventoryTab.setContent(inventoryBorderPane);
        inventoryBorderPane.setTop(inventoryVBox);
        //inventoryBorderPane.setBottom(loansBorderPaneBottom);

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

        VBox borrowerVBox = createBorrowerVBox();
        //BorderPane loansBorderPaneBottom = createLoansBorderPaneBottom();

        borrowerTab.setContent(borrowerBorderPane);
        borrowerBorderPane.setTop(borrowerVBox);
        //borrowerBorderPane.setBottom(loansBorderPaneBottom);

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

        VBox librarianVBox = createLibrarianVBox();
        //BorderPane loansBorderPaneBottom = createLoansBorderPaneBottom();

        librarianTab.setContent(librarianBorderPane);
        librarianBorderPane.setTop(librarianVBox);
        //librarianBorderPane.setBottom(loansBorderPaneBottom);

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
        Tab book = createCopyTab();
        Tab bookCopy = createInventoryTab();
        Tab borrower = createBorrowerTab();
        Tab librarian = createLibrarianTab();

        TabPane tabPane = new TabPane(loans, book, bookCopy, borrower, librarian);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        //Resize stage on tab change
        tabPane.getSelectionModel().selectedItemProperty().addListener(l -> primaryStage.sizeToScene());

        return tabPane;
    }

    /**
     * Creates a VBox with all the content that goes in the 'Utlån' tab.
     *
     * @return loansVBox the VBox containing the structure of the 'Utlån' tab.
     */
    private VBox createLoansTopContent()
    {
        VBox loansVBox = new VBox();
        searchBooks = new TextField();
        searchBooks.setPromptText("Søk etter Bok-ID, ISBN, Tittel, Forfatter...");
        createBooksTable();

        loansVBox.getChildren().add(searchBooks);
        loansVBox.getChildren().add(tableViewLoansTop);

        return loansVBox;
    }

    private void createBooksTable()
    {
        tableViewLoansTop = new TableView();
        TableColumn bokIDCol = new TableColumn("Bok-ID");
        TableColumn ISBNCol = new TableColumn("ISBN");
        TableColumn tittelCol = new TableColumn("Tittel");
        TableColumn forfatterCol = new TableColumn("Forfatter");
        tableViewLoansTop.getColumns().addAll(bokIDCol, ISBNCol, tittelCol, forfatterCol);
        tableViewLoansTop.setMinHeight(225);
        tableViewLoansTop.setMinWidth(300);
        tableViewLoansTop.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Creates the table in the "Copy" tab.
     *
     * @return Returns a HBox containing a table for the "Copy" tab.
     */
    private VBox createCopyVBox()
    {
        VBox copyVBox = new VBox();
        tableViewCopy = new TableView();
        searchCopy = new TextField();
        HBox buttonContainer = new HBox();
        Button addButton = new Button("Add");
        Button removeButton = new Button("Remove");

        searchCopy.setPromptText("Søk etter kvitteringsNr, Lånetaker ...");

        TableColumn fornavnCol = new TableColumn("N/A");
        TableColumn etternavnCol = new TableColumn("N/A");
        TableColumn telefonCol = new TableColumn("N/A");

        tableViewCopy.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);

        tableViewCopy.setMinSize(450, 175);
        tableViewCopy.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        buttonContainer.getChildren().addAll(addButton, removeButton);
        copyVBox.getChildren().add(buttonContainer);
        copyVBox.getChildren().add(searchCopy);
        copyVBox.getChildren().add(tableViewCopy);

        return copyVBox;
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
        HBox buttonContainer = new HBox();
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addBook());
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> removeBook());

        searchInventory.setPromptText("Search through the inventory");

        TableColumn antallCol = new TableColumn("Antall");
        antallCol.setCellValueFactory(new PropertyValueFactory<>("BookQuantity"));

        TableColumn tittelCol = new TableColumn("Tittel");
        tittelCol.setCellValueFactory(new PropertyValueFactory<>("BookName"));

        TableColumn forfatterCol = new TableColumn("Forfatter");
        forfatterCol.setCellValueFactory(new PropertyValueFactory<>("BookAuthor"));

        TableColumn forlagCol = new TableColumn("Forlag");
        forlagCol.setCellValueFactory(new PropertyValueFactory<>("BookPublisher"));

        TableColumn ISBNCol = new TableColumn("ISBN");
        ISBNCol.setCellValueFactory(new PropertyValueFactory<>("BookID"));

        tableViewInventory.getColumns().addAll(antallCol, tittelCol, forfatterCol, forlagCol, ISBNCol);

        tableViewInventory.setItems(bookList);
        tableViewInventory.setMinSize(450, 175);
        buttonContainer.getChildren().addAll(addButton, removeButton);
        inventoryVBox.getChildren().add(buttonContainer);
        inventoryVBox.getChildren().add(searchInventory);
        inventoryVBox.getChildren().add(tableViewInventory);

        return inventoryVBox;
    }

    /**
     * Creates the table in the "Borrower" tab.
     *
     * @return Returns a HBox containing a table for the "Borrower" tab.
     */
    private VBox createBorrowerVBox()
    {
        VBox borrowerVBox = new VBox();
        tableViewBorrower = new TableView();
        searchInventory = new TextField();
        HBox buttonContainer = new HBox();
        Button addButton = new Button("Add");
        Button removeButton = new Button("Remove");

        searchInventory.setPromptText("Search through this lists");

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
        buttonContainer.getChildren().addAll(addButton, removeButton);
        borrowerVBox.getChildren().add(buttonContainer);
        borrowerVBox.getChildren().add(searchInventory);
        borrowerVBox.getChildren().add(tableViewBorrower);

        return borrowerVBox;
    }

    /**
     * Creates the table in the "Librarian" tab.
     *
     * @return Returns a HBox containing a table for the "Librarian" tab.
     */
    private VBox createLibrarianVBox()
    {
        VBox librarianVBox = new VBox();
        tableViewLibrarian = new TableView();
        searchLibrarian = new TextField();
        HBox buttonContainer = new HBox();
        Button addButton = new Button("Add");
        Button removeButton = new Button("Remove");

        searchLibrarian.setPromptText("Search through this lists");

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
        buttonContainer.getChildren().addAll(addButton, removeButton);
        librarianVBox.getChildren().add(buttonContainer);
        librarianVBox.getChildren().add(searchLibrarian);
        librarianVBox.getChildren().add(tableViewLibrarian);

        return librarianVBox;
    }

    /**
     * Exit the application. Displays a confirmation dialog.
     */
    private void doExitApplication()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avslutt");
        alert.setHeaderText("Er du sikker på at du vil avslutte programmet?");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        //Deactivate Defaultbehavior for yes-Button:
        Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
        yesButton.setDefaultButton(true);
        yesButton.setText("Ja");

        //Activate Defaultbehavior for no-Button:
        Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
        noButton.setDefaultButton(false);
        noButton.setText("Nei");
        
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(primaryStage);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.YES) {
            System.exit(0);
        } else {
            // ... user chose CANCEL or closed the dialog
            // then do nothing.
        }
    }

    private void updateInventoryList()
    {
        bookList.clear();
        bookList.addAll(handler.listBooks());
    }

    private void addBook()
    {
        Copy newBook = addBookView.display();
        if (newBook != null && handler.addBook(newBook)) {
            System.out.println(newBook.getBookName() + " was added");
        } else {
            System.out.println("Failed to add book");
        }
        updateInventoryList();
    }

    private void removeBook()
    {
        Copy bookToDelete = tableViewInventory.getSelectionModel().getSelectedItem();
        handler.deleteBook(bookToDelete);
        updateInventoryList();
    }
}
