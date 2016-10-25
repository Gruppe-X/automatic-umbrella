package bibliotek_02;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import static javafx.application.Application.launch;

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
    private TableView<InventoryBook> tableViewLoansTop;
    private TableView tableViewCopy;
    private TableView<InventoryBook> tableViewInventory;
    private TableView<Librarian> tableViewLibrarian;
    private TableView<Borrower> tableViewBorrower;

    private AddBookView addBookView;
    private AddBorrowerView addBorrowerView;
    private AddLibrarianView addLibrarianView;

    ObservableList<InventoryBook> bookList = FXCollections.observableArrayList();
    ObservableList<Borrower> borrowerList;
    ObservableList<Librarian> librarianList;
    ObservableList<BookCopy> copyRegisteredForLoanList;

    public GUI()
    {
        handler = new DatabaseHandler();
        borrowerList = FXCollections.observableArrayList(handler.listBorrowers());
        bookList = FXCollections.observableArrayList(handler.listBooks());
        librarianList = FXCollections.observableArrayList(handler.listLibrarians());
        copyRegisteredForLoanList = FXCollections.observableArrayList();

        addBookView = new AddBookView();
        addBorrowerView = new AddBorrowerView();
        addLibrarianView = new AddLibrarianView();
    }

    /**
     * 
     * @param args 
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * a
     */
    @Override
    public void stop()
    {
        System.exit(0);
    }

    /**
     * 
     * @param primaryStage
     * @throws Exception 
     */
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
        tabPane.getSelectionModel().selectedItemProperty().addListener(l -> updateAllList());

        return tabPane;
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
        VBox loansContent = new VBox();
        
        VBox loansTopContent = createLoansTopContent();
        HBox loansBottomContent = createLoansBottomContent();
        
        loansTab.setContent(loansBorderPane);
        loansBorderPane.setCenter(loansContent);
        loansContent.getChildren().addAll(loansTopContent, loansBottomContent);
        
        VBox.setVgrow(loansTopContent, Priority.ALWAYS);
        VBox.setVgrow(loansBottomContent, Priority.ALWAYS);
        
        return loansTab;
    }
    
    /**
     * 
     * @return 
     */
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

    /**
     * 
     * @return 
     */
    private BorderPane createLoansBottomLeftContent()
    {
        BorderPane bottomLeftContent = new BorderPane();
        Button addButton = new Button("Legg til");
        addButton.setOnAction(e -> addBookToLoan());
        Button removeButton = new Button("Fjern");
        HBox buttonsBox = new HBox(addButton, removeButton);


        TableView<BookCopy> registeredBooks = new TableView();
        registeredBooks.setItems(copyRegisteredForLoanList);
        
        
        TableColumn ISBNCol = new TableColumn("ISBN");
        ISBNCol.setCellValueFactory(new PropertyValueFactory<>("BookID"));
        TableColumn tittelCol = new TableColumn("Tittel");
        tittelCol.setCellValueFactory(new PropertyValueFactory<>("BookName"));
        TableColumn forfatterCol = new TableColumn("Forfatter");
        forfatterCol.setCellValueFactory(new PropertyValueFactory<>("BookAuthor"));
        registeredBooks.getColumns().addAll(ISBNCol, tittelCol, forfatterCol);
        
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

    /**
     * 
     * @return 
     */
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
        TableColumn fornavnCol = new TableColumn("Fornavn");
        fornavnCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        TableColumn etternavnCol = new TableColumn("Etternavn");
        etternavnCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        TableColumn telefonCol = new TableColumn("Telefon");
        telefonCol.setCellValueFactory(new PropertyValueFactory<>("Telephone"));
        
        borrowerTable.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);
        ObservableList<Borrower> borrowers = FXCollections.observableArrayList();
        borrowers.addAll(borrowerList);
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
        VBox copyContent = new VBox();

        VBox copyTopContent = createCopyTopContent();
        HBox copyBottomContent = createCopyBottomContent();

        copyTab.setContent(copyBorderPane);
        copyBorderPane.setCenter(copyContent);
        copyContent.getChildren().addAll(copyTopContent, copyBottomContent);
        
        VBox.setVgrow(copyTopContent, Priority.ALWAYS);
        VBox.setVgrow(copyBottomContent, Priority.ALWAYS);

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
     * Creates a VBox with all the content that goes in the 'Utlån' tab.
     *
     * @return loansVBox the VBox containing the structure of the 'Utlån' tab.
     */
    private VBox createLoansTopContent()
    {
        VBox loansVBox = new VBox();
        searchBooks = new TextField();
        searchBooks.setPromptText("Søk etter Bok-ID, ISBN, Tittel, Forfatter...");
        tableViewLoansTop = createBooksTable();

        loansVBox.getChildren().add(searchBooks);
        loansVBox.getChildren().add(tableViewLoansTop);

        return loansVBox;
    }

    /**
     * 
     * @return 
     */
    private TableView<InventoryBook> createBooksTable()
    {
        TableView<InventoryBook> bookTable = new TableView<>();
        bookTable.setItems(bookList);
        TableColumn ISBNCol = new TableColumn("ISBN");
        ISBNCol.setCellValueFactory(new PropertyValueFactory<>("BookID"));

        TableColumn tittelCol = new TableColumn("Tittel");
        tittelCol.setCellValueFactory(new PropertyValueFactory<>("BookName"));

        TableColumn forfatterCol = new TableColumn("Forfatter");
        forfatterCol.setCellValueFactory(new PropertyValueFactory<>("BookAuthor"));
        
        bookTable.getColumns().addAll(ISBNCol, tittelCol, forfatterCol);
        bookTable.setMinHeight(225);
        bookTable.setMinWidth(300);
        bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return bookTable;
    }

    /**
     * Creates the table in the "Kopi" tab.
     *
     * @return Returns a HBox containing a table for the "Kopi" tab.
     */
    private VBox createCopyTopContent()
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
     * 
     * @return 
     */
    private HBox createCopyBottomContent()
    {
        HBox copyHBox = new HBox();
        return copyHBox;
    }

    /**
     * Creates the table in the "Beholdning" tab.
     *
     * @return Returns a HBox containing a table for the "Beholdning" tab.
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
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateBorrowerList());

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
        buttonContainer.getChildren().addAll(addButton, removeButton, updateButton);
        inventoryVBox.getChildren().add(buttonContainer);
        inventoryVBox.getChildren().add(searchInventory);
        inventoryVBox.getChildren().add(tableViewInventory);

        return inventoryVBox;
    }

    /**
     * Creates the table in the "Låntaker" tab.
     *
     * @return Returns a HBox containing a table for the "Låntaker" tab.
     */
    private VBox createBorrowerVBox()
    {
        VBox borrowerVBox = new VBox();
        tableViewBorrower = new TableView();
        searchBorrower = new TextField();
        HBox buttonContainer = new HBox();
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addBorrower());
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> removeBorrower());
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateBorrowerList());

        searchBorrower.setPromptText("Search through this lists");

        TableColumn lanetakerID = new TableColumn("LånetakerID");
        lanetakerID.setCellValueFactory(new PropertyValueFactory<>("BorrowerID"));
        
        TableColumn fornavnCol = new TableColumn("Fornavn");
        fornavnCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));

        TableColumn etternavnCol = new TableColumn("Etternavn");
        etternavnCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));

        TableColumn telefonCol = new TableColumn("Telefon");
        telefonCol.setCellValueFactory(new PropertyValueFactory<>("Telephone"));

        tableViewBorrower.getColumns().addAll(lanetakerID, fornavnCol, etternavnCol, telefonCol);

        tableViewBorrower.setItems(borrowerList);
        tableViewBorrower.setMinSize(450, 175);
        tableViewBorrower.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        buttonContainer.getChildren().addAll(addButton, removeButton, updateButton);
        borrowerVBox.getChildren().add(buttonContainer);
        borrowerVBox.getChildren().add(searchBorrower);
        borrowerVBox.getChildren().add(tableViewBorrower);

        return borrowerVBox;
    }

    /**
     * Creates the table in the "Bibliotekar" tab.
     *
     * @return Returns a HBox containing a table for the "Bibliotekar" tab.
     */
    private VBox createLibrarianVBox()
    {
        VBox librarianVBox = new VBox();
        tableViewLibrarian = new TableView();
        searchLibrarian = new TextField();
        HBox buttonContainer = new HBox();
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addLibrarian());
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> removeLibrarian());
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateBorrowerList());

        searchLibrarian.setPromptText("Search through this lists");
        searchLibrarian.textProperty().addListener((v, oldValue, newValue) -> {
//            
//            ArrayList<Librarian> searchResult = (newValue);
//            if(newValue.length() > 0)
//            {
//            
//            } else {
//                
//            }
            
        });

        TableColumn librarianIDCol = new TableColumn("AnsattID");
        librarianIDCol.setCellValueFactory(new PropertyValueFactory<>("employeeID"));

        TableColumn fornavnCol = new TableColumn("Fornavn");
        fornavnCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));

        TableColumn etternavnCol = new TableColumn("Etternavn");
        etternavnCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));

        tableViewLibrarian.getColumns().addAll(librarianIDCol, fornavnCol, etternavnCol);

        tableViewLibrarian.setItems(librarianList);
        tableViewLibrarian.setMinSize(450, 175);
        tableViewLibrarian.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        buttonContainer.getChildren().addAll(addButton, removeButton, updateButton);
        librarianVBox.getChildren().add(buttonContainer);
        librarianVBox.getChildren().add(searchLibrarian);
        librarianVBox.getChildren().add(tableViewLibrarian);

        return librarianVBox;
    }
    
    // -------- UPDATE METHODS --------
    /**
     * Updates the list of books.
     */
    private void updateBookList()
    {
        bookList.clear();
        bookList.addAll(handler.listBooks());
    }
    
    /**
     * Updates the list of borrowers.
     */
    private void updateBorrowerList()
    {
        borrowerList.clear();
        borrowerList.addAll(handler.listBorrowers());
    }
    
    /**
     * Updates the list of librarians
     */
    private void updateLibrarianList()
    {
        librarianList.clear();
        librarianList.addAll(handler.listLibrarians());
    }
    
    /**
     * Updates all the lists
     */
    private void updateAllList()
    {
        updateBookList();
        updateBorrowerList();
        updateLibrarianList();
    }

    // -------- ADD METHODS --------
    //TODO fiks feilmelding
    
    private void addBookToLoan() {
        //copyRegisteredForLoanList.add(tableViewLoansTop.getSelectionModel().getSelectedItem());
        InventoryBook selectedBook = tableViewLoansTop.getSelectionModel().getSelectedItem();
        List<BookCopy> bookCopys = handler.listBookCopysWithId(selectedBook.getBookID());
    }
    
    /**
     * Adds a book to the database and updates the list/table.
     */
    private void addBook()
    {
        InventoryBook newBook = addBookView.display();
        if (newBook != null && handler.addBook(newBook)) {
            System.out.println(newBook.getBookName() + " was added");
        } else {
            System.out.println("Failed to add book");
        }
        updateBookList();
    }
    
    /**
     * Adds a borrower to the database and updates the list/table.
     */
    private void addBorrower()
    {
        Borrower newBorrower = addBorrowerView.display();
        if(newBorrower != null && handler.addBorrower(newBorrower)){
            System.out.println(newBorrower.getFirstName() + " was added");
        }
        else {
            System.out.println("Failed to add borrower");
        }
        updateBorrowerList();
    }

    /**
     * Adds a librarian to the database and updates the list/table.
     */
    private void addLibrarian()
    {
        Librarian newLibrarian = addLibrarianView.display();
        if(newLibrarian != null && handler.addLibrarian(newLibrarian)){
            System.out.println(newLibrarian.getFirstName() + " was added");
        }
        else {
            System.out.println("Failed to add employee");
        }
        updateLibrarianList();
    }
    
    // -------- REMOVE METHODS --------
    /**
     * Removes a book from the database and updates the list/table.
     */
    private void removeBook()
    {
        InventoryBook bookToDelete = tableViewInventory.getSelectionModel().getSelectedItem();
        if(bookToDelete != null){
            handler.deleteBook(bookToDelete);
            updateBookList();
        }
    }
    
    /**
     * Removes a borrower from the database and updates the list/table.
     */
    private void removeBorrower()
    {
        Borrower borrowerToDelete = tableViewBorrower.getSelectionModel().getSelectedItem();
        if(borrowerToDelete != null){
            handler.deleteBorrower(borrowerToDelete);
            updateBorrowerList();
        }
    }

    /**
     * Removes a librarian from the databse and updates the list/table.
     */
    private void removeLibrarian()
    {
        Librarian librarianToDelete = tableViewLibrarian.getSelectionModel().getSelectedItem();
        if(librarianToDelete != null){
            handler.deleteLibrarian(librarianToDelete);
            updateLibrarianList();
        } 
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
            try {
                handler.close();
            } catch (IOException ex) {
                //Could not close SQL connection.... TODO
            }
            System.exit(0);
        } else {
            // ... user chose CANCEL or closed the dialog
            // then do nothing.
        }
    }
}
