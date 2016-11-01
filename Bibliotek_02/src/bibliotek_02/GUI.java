package bibliotek_02;

import java.io.IOException;
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
import javafx.event.ActionEvent;
import static javafx.application.Application.launch;

/**
 *
 * @author Thomas Todal, Oscar Wika
 */
public class GUI extends Application {

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
    private TableView<Borrower> tableViewLoanBorrower;

    private AddBookView addBookView;
    private EditBookView editBookView;
    private AddBorrowerView addBorrowerView;
    private EditBorrowerView editBorrowerView;
    private AddLibrarianView addLibrarianView;
    private EditLibrarianView editLibrarianView;

    private ChooseEmployeeView employeeView;

    private ObservableList<InventoryBook> bookList;
    private ObservableList<InventoryBook> bookSearchList;
    private ObservableList<Borrower> borrowerList;
    private ObservableList<Borrower> borrowerSearchList;
    private ObservableList<Librarian> librarianList;
    private ObservableList<Librarian> librarianSearchList;
    private ObservableList<Copy> copyList;

    private ObservableList<BookCopy> copyRegisteredForLoanList;
    private ObservableList<Borrower> loanBorrowers;

    private final int DEFAULT_LOAN_DURATION = 30;

    //Holds the currently logged in user.
    private Librarian currentUser;

    public GUI() {
        handler = new DatabaseHandler();
        if(!handler.connect()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Feilmelding");
            alert.setHeaderText("Kan ikke koble til databasen");
            alert.setContentText("Kan ikke koble til databasen, sjekk nettverks tilkoblingen");
            alert.showAndWait();
            System.exit(0);
        }
        
        borrowerList = FXCollections.observableArrayList(handler.listBorrowers());
        borrowerSearchList = FXCollections.observableArrayList();
        
        bookList = FXCollections.observableArrayList(handler.listBooks());
        bookSearchList = FXCollections.observableArrayList();
        
        librarianList = FXCollections.observableArrayList(handler.listLibrarians());
        librarianSearchList = FXCollections.observableArrayList();
        
        copyList = FXCollections.observableArrayList(handler.listCopies());
        
        copyRegisteredForLoanList = FXCollections.observableArrayList();
        loanBorrowers = FXCollections.observableArrayList();
        
        
        addBookView = new AddBookView();
        editBookView = new EditBookView();
        
        addBorrowerView = new AddBorrowerView();
        editBorrowerView = new EditBorrowerView();
        
        addLibrarianView = new AddLibrarianView();
        editLibrarianView = new EditLibrarianView();
        
        employeeView = new ChooseEmployeeView(handler);

        boolean checkID = true;
        while (checkID) {
            currentUser = employeeView.display();

            if (currentUser != null) {
                checkID = false;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Feilmelding");
                alert.setHeaderText("Ugyldig ID");
                alert.setContentText("Ooops, tast inn en gyldig ID ");
                alert.showAndWait();
            }
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * a
     */
    @Override
    public void stop() {
        System.exit(0);
    }

    /**
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
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
    private TabPane createTabPane() {
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
    private Tab createLoansTab() {
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
    private HBox createLoansBottomContent() {
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
    private BorderPane createLoansBottomLeftContent() {
        BorderPane bottomLeftContent = new BorderPane();
        Button addButton = new Button("Legg til");
        Button regretButton = new Button("Angre");
        regretButton.setOnAction(e -> updateCopyRegisteredForLoan());
        addButton.setOnAction(e -> {
        addBookToLoan();
        tableViewLoansTop.setItems(bookList);
        searchBooks.textProperty().set("");
        });
        HBox buttonsBox = new HBox(addButton, regretButton);
        buttonsBox.setPadding(new Insets(10, 10, 0, 0));

        TableView<BookCopy> registeredCopys = new TableView();
        registeredCopys.setItems(copyRegisteredForLoanList);

        /*
        TableView<BookCopy> registeredBooks = new TableView();
        registeredBooks.setItems(copyRegisteredForLoanList);
         */
        TableColumn copyIdCol = new TableColumn("Eksemplar ID");
        copyIdCol.setCellValueFactory(new PropertyValueFactory<>("CopyID"));
        TableColumn ISBNCol = new TableColumn("ISBN");
        ISBNCol.setCellValueFactory(new PropertyValueFactory<>("BookID"));
        TableColumn tittelCol = new TableColumn("Tittel");
        tittelCol.setCellValueFactory(new PropertyValueFactory<>("BookName"));
        TableColumn forfatterCol = new TableColumn("Forfatter");
        forfatterCol.setCellValueFactory(new PropertyValueFactory<>("BookAuthor"));
        registeredCopys.getColumns().addAll(copyIdCol, ISBNCol, tittelCol, forfatterCol);

        registeredCopys.setMinWidth(240);
        registeredCopys.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        bottomLeftContent.setTop(buttonsBox);
        //set table center.
        bottomLeftContent.setCenter(registeredCopys);
        Button registerLoanButton = new Button("Registrer Lån");
        registerLoanButton.setOnAction(e -> {
            registerLoan();
            tableViewLoansTop.setItems(bookList);
            searchBooks.textProperty().set("");
        });
        bottomLeftContent.setBottom(registerLoanButton);

        bottomLeftContent.setPadding(new Insets(0, 10, 0, 0));
        bottomLeftContent.setMinWidth(240);

        return bottomLeftContent;
    }

    /**
     * Creates content in bottom right of loans tab Includes Searchbar to search
     * for borrowers and table of borrowers matching search.
     *
     * @return VBox with bottom right content.
     */
    private VBox createLoansBottomRightContent() {
        VBox bottomRightContent = new VBox();
        GridPane topContent = new GridPane();

        topContent.setPadding(new Insets(10, 0, 0, 0));
        //topContent.setGridLinesVisible(true);

        TextField searchField = new TextField();
        searchField.setPromptText("Søk etter lånetaker..");
        searchField.setPadding(new Insets(4));
        searchField.textProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.equals("")) {
                tableViewLoanBorrower.setItems(borrowerList);
            } else {
                tableViewLoanBorrower.setItems(borrowerSearchList);
                borrowerSearchList.clear();
                for (Borrower borrower : borrowerList) {
                    if (Integer.toString(borrower.getBorrowerID()).toLowerCase().equals(newValue.toLowerCase())
                            || borrower.getFirstName().toLowerCase().contains(newValue.toLowerCase())
                            || borrower.getLastName().toLowerCase().contains(newValue.toLowerCase())) {
                        borrowerSearchList.add(borrower);
                    }
                }
            }
        });
        
        Button registerBorrowerButton = new Button("Registrer");
        registerBorrowerButton.setOnAction(e -> addBorrower());
        topContent.add(searchField, 0, 0);
        topContent.add(registerBorrowerButton, 1, 0);

        tableViewLoanBorrower = new TableView();
        TableColumn fornavnCol = new TableColumn("Fornavn");
        fornavnCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        TableColumn etternavnCol = new TableColumn("Etternavn");
        etternavnCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        TableColumn telefonCol = new TableColumn("Telefon");
        telefonCol.setCellValueFactory(new PropertyValueFactory<>("Telephone"));
        tableViewLoanBorrower.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);
        loanBorrowers.addAll(borrowerList);
        tableViewLoanBorrower.setItems(loanBorrowers);

        tableViewLoanBorrower.setMinWidth(240);
        tableViewLoanBorrower.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewLoanBorrower.setMinHeight(50);

        bottomRightContent.getChildren().addAll(topContent, tableViewLoanBorrower);

        bottomRightContent.setPadding(new Insets(0, 0, 0, 10));
        bottomRightContent.setMinWidth(240);

        return bottomRightContent;
    }

    /**
     * Creates the "Copy" tab.
     *
     * @return Returns the "Copy" tab.
     */
    private Tab createCopyTab() {
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
     * Creates the table in the "Kopi" tab.
     *
     * @return Returns a HBox containing a table for the "Kopi" tab.
     */
    private VBox createCopyTopContent()
    {
        VBox topContent = new VBox();
        tableViewCopy = new TableView();
        searchCopy = new TextField();
        HBox buttonContainer = new HBox();
        Button addButton = new Button("Legg til");
        Button removeButton = new Button("Fjern");
        
        searchCopy.setPromptText("Søk etter kvitteringsNr, Lånetaker ...");

        TableColumn kvittNrCol = new TableColumn("KvittNr");
        kvittNrCol.setCellValueFactory(new PropertyValueFactory("LoanID"));
        TableColumn datoCol = new TableColumn("Dato");
        datoCol.setCellValueFactory(new PropertyValueFactory("StartDateTime"));
        TableColumn daysLeftCol = new TableColumn("Dager igjen");
        daysLeftCol.setCellValueFactory(new PropertyValueFactory("DaysLeft"));
        TableColumn navnCol = new TableColumn("Navn");
        navnCol.setCellValueFactory(new PropertyValueFactory("LastFirstName"));
        TableColumn allGoodCol = new TableColumn("Tilbakelevert");
        allGoodCol.setCellValueFactory(new PropertyValueFactory("AllGood"));

        tableViewCopy.getColumns().addAll(kvittNrCol, datoCol, daysLeftCol, navnCol, allGoodCol);

        tableViewCopy.setItems(copyList);
        
        tableViewCopy.setMinSize(450, 175);
        tableViewCopy.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        buttonContainer.getChildren().addAll(addButton, removeButton);
        topContent.getChildren().add(buttonContainer);
        topContent.getChildren().add(searchCopy);
        topContent.getChildren().add(tableViewCopy);

        return topContent;
    }
    
    /**
     * 
     * @return 
     */
    private HBox createCopyBottomContent()
    {
        HBox bottomContent = new HBox();
        BorderPane botLeftCont = createCopyBottomLeftContent();
        VBox botRightCont = createCopyBottomRightContent();
        
        bottomContent.getChildren().addAll(botLeftCont, botRightCont);
        HBox.setHgrow(botLeftCont, Priority.ALWAYS);
        HBox.setHgrow(botRightCont, Priority.ALWAYS);
        
        return bottomContent;
    }
    
    private BorderPane createCopyBottomLeftContent()
    {
        BorderPane botLeftContent = new BorderPane();
        return botLeftContent;
    }
    
    private VBox createCopyBottomRightContent()
    {
        VBox botRightContent = new VBox();
        return botRightContent;
    }

    /**
     * Creates the "Inventory" tab.
     *
     * @return Returns the "Inventory" tab.
     */
    private Tab createInventoryTab() {
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
    private Tab createBorrowerTab() {
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
    private Tab createLibrarianTab() {
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
    private VBox createLoansTopContent() {
        VBox loansVBox = new VBox();
        searchBooks = new TextField();
        searchBooks.setPromptText("Søk etter Bok-ID, ISBN, Tittel, Forfatter...");
        searchBooks.textProperty().addListener((v, oldValue, newValue) -> {
            if(newValue.equals("")){
                tableViewLoansTop.setItems(bookList);
            } else {
                bookSearchList.clear();
                for(InventoryBook book : bookList){
                    if(book.getBookID().replaceAll("-", "").replaceAll(" ", "").toLowerCase().contains(newValue.toLowerCase()) ||
                            book.getBookID().toLowerCase().contains(newValue) ||
                            book.getBookName().toLowerCase().contains(newValue.toLowerCase()) ||
                            book.getBookPublisher().toLowerCase().contains(newValue.toLowerCase()) ||
                            book.getBookAuthor().toLowerCase().contains(newValue.toLowerCase())){
                        bookSearchList.add(book);
                    }
                }
                tableViewLoansTop.setItems(bookSearchList);
            }
        });
        
        tableViewLoansTop = createBooksTable();

        loansVBox.getChildren().add(searchBooks);
        loansVBox.getChildren().add(tableViewLoansTop);

        return loansVBox;
    }

    /**
     *
     * @return
     */
    private TableView<InventoryBook> createBooksTable() {
        TableView<InventoryBook> bookTable = new TableView<>();
        bookTable.setItems(bookList);

        TableColumn availableQuantityCol = new TableColumn("Tilgjenglig Antall");
        availableQuantityCol.setCellValueFactory(new PropertyValueFactory<>("BookAvailableQuantity"));

        TableColumn ISBNCol = new TableColumn("ISBN");
        ISBNCol.setCellValueFactory(new PropertyValueFactory<>("BookID"));

        TableColumn tittelCol = new TableColumn("Tittel");
        tittelCol.setCellValueFactory(new PropertyValueFactory<>("BookName"));

        TableColumn forfatterCol = new TableColumn("Forfatter");
        forfatterCol.setCellValueFactory(new PropertyValueFactory<>("BookAuthor"));

        bookTable.getColumns().addAll(availableQuantityCol, ISBNCol, tittelCol, forfatterCol);
        bookTable.setMinHeight(225);
        bookTable.setMinWidth(300);
        bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return bookTable;
    }
    
    /**
     * Creates the table in the "Beholdning" tab.
     *
     * @return Returns a HBox containing a table for the "Beholdning" tab.
     */
    private VBox createInventoryVBox() {
        VBox inventoryVBox = new VBox();
        tableViewInventory = new TableView();
        
        searchInventory = new TextField();
        searchInventory.textProperty().addListener((v, oldValue, newValue) -> {
            if(newValue.equals("")){
                tableViewInventory.setItems(bookList);
            } else {
                bookSearchList.clear();
                for(InventoryBook book : bookList){
                    if(book.getBookID().replaceAll("-", "").replaceAll(" ", "").toLowerCase().contains(newValue.toLowerCase()) ||
                            book.getBookID().toLowerCase().contains(newValue) ||
                            book.getBookName().toLowerCase().contains(newValue.toLowerCase()) ||
                            book.getBookPublisher().toLowerCase().contains(newValue.toLowerCase()) ||
                            book.getBookAuthor().toLowerCase().contains(newValue.toLowerCase())){
                        bookSearchList.add(book);
                    }
                }
                tableViewInventory.setItems(bookSearchList);
            }
        });
        
        HBox buttonContainer = new HBox();
        Button addButton = new Button("Legg til");
        addButton.setOnAction(e -> addBook());
        Button removeButton = new Button("Fjern");
        removeButton.setOnAction(e -> removeBook());
        Button updateButton = new Button("Oppdater");
        updateButton.setOnAction(e -> updateBorrowerList());
        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            if(tableViewInventory.getSelectionModel().getSelectedItem() != null){
                editBook(tableViewInventory.getSelectionModel().getSelectedItem());
            }
        });

        searchInventory.setPromptText("Søk etter bøker..");

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
        buttonContainer.getChildren().addAll(addButton, removeButton, updateButton, editButton);
        inventoryVBox.getChildren().add(buttonContainer);
        inventoryVBox.getChildren().add(searchInventory);
        inventoryVBox.getChildren().add(tableViewInventory);

        return inventoryVBox;
    }
    
    private void editBook(InventoryBook book){
        InventoryBook editedBook = editBookView.display(book);
        if (editedBook != null) {
            boolean success = handler.editBook(editedBook);
            if (success) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Successfull edit");
                successAlert.setHeaderText("Successfull edit");
                successAlert.setContentText("Book was successfully edited.");
                successAlert.show();
                updateBookList();
            } else {
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("Failed edit");
                failure.setHeaderText("Failed edit");
                failure.setContentText("Something went wrong with edit.");
                failure.show();
            }
        }
    }

    /**
     * Creates the table in the "Låntaker" tab.
     *
     * @return Returns a HBox containing a table for the "Låntaker" tab.
     */
    private VBox createBorrowerVBox() {
        VBox borrowerVBox = new VBox();
        tableViewBorrower = new TableView();
        searchBorrower = new TextField();
        HBox buttonContainer = new HBox();
        Button addButton = new Button("Legg til");
        addButton.setOnAction(e -> addBorrower());
        Button removeButton = new Button("Fjern");
        removeButton.setOnAction((ActionEvent event) -> {
            if (tableViewBorrower.getSelectionModel().getSelectedItem() != null) {
                if (checkIfHasBorrowed() == true) {
                    doRemoveBorrowerAlert();
                } else {
                    removeBorrower();
                }
            }
        });
        Button updateButton = new Button("Oppdater");
        updateButton.setOnAction(e -> updateBorrowerList());

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            if(tableViewBorrower.getSelectionModel().getSelectedItem() != null){
                editBorrower(tableViewBorrower.getSelectionModel().getSelectedItem());
            }
        });
        searchBorrower.setPromptText("Søk etter lånetaker..");
        searchBorrower.textProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.equals("")) {
                tableViewBorrower.setItems(borrowerList);
            } else {
                tableViewBorrower.setItems(borrowerSearchList);
                borrowerSearchList.clear();
                for (Borrower borrower : borrowerList) {
                    if (Integer.toString(borrower.getBorrowerID()).toLowerCase().equals(newValue.toLowerCase())
                            || borrower.getFirstName().toLowerCase().contains(newValue.toLowerCase())
                            || borrower.getLastName().toLowerCase().contains(newValue.toLowerCase())) {
                        borrowerSearchList.add(borrower);
                    }
                }
            }
        });

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
        buttonContainer.getChildren().addAll(addButton, removeButton, updateButton, editButton);
        borrowerVBox.getChildren().add(buttonContainer);
        borrowerVBox.getChildren().add(searchBorrower);
        borrowerVBox.getChildren().add(tableViewBorrower);

        return borrowerVBox;
    }
    
    private void editBorrower(Borrower borrower) {
        Borrower editedBorrower = editBorrowerView.display(borrower);
        if (editedBorrower != null) {
            boolean success = handler.editBorrower(editedBorrower);
            if (success) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Successfull edit");
                successAlert.setHeaderText("Successfull edit");
                successAlert.setContentText("Borrower was successfully edited.");
                successAlert.show();
                updateBorrowerList();
            } else {
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("Failed edit");
                failure.setHeaderText("Failed edit");
                failure.setContentText("Something went wrong with edit.");
                failure.show();
            }
        }
    }

    /**
     * Creates the table in the "Bibliotekar" tab.
     *
     * @return Returns a HBox containing a table for the "Bibliotekar" tab.
     */
    private VBox createLibrarianVBox() {
        VBox librarianVBox = new VBox();
        tableViewLibrarian = new TableView();
        searchLibrarian = new TextField();
        HBox buttonContainer = new HBox();
        Button addButton = new Button("Legg til");
        addButton.setOnAction(e -> addLibrarian());
        Button removeButton = new Button("Fjern");
        removeButton.setOnAction(e -> removeLibrarian());
        Button updateButton = new Button("Oppdater");
        updateButton.setOnAction(e -> updateBorrowerList());

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            if(tableViewLibrarian.getSelectionModel().getSelectedItem() != null){
                editLibrarian(tableViewLibrarian.getSelectionModel().getSelectedItem());
            }
        });
        searchLibrarian.setPromptText("Søk etter ansatt..");
        searchLibrarian.textProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.equals("")) {
                tableViewLibrarian.setItems(librarianList);
            } else {
                tableViewLibrarian.setItems(librarianSearchList);
                librarianSearchList.clear();
                for (Librarian librarian : librarianList) {
                    if (librarian.getEmployeeID().toLowerCase().equals(newValue.toLowerCase())
                            || librarian.getFirstName().toLowerCase().contains(newValue.toLowerCase())
                            || librarian.getLastName().toLowerCase().contains(newValue.toLowerCase())) {
                        librarianSearchList.add(librarian);
                    }
                }
            }
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
        buttonContainer.getChildren().addAll(addButton, removeButton, updateButton, editButton);
        librarianVBox.getChildren().add(buttonContainer);
        librarianVBox.getChildren().add(searchLibrarian);
        librarianVBox.getChildren().add(tableViewLibrarian);

        return librarianVBox;
    }
    
    private void editLibrarian(Librarian librarian) {
        Librarian editedLibrarian = editLibrarianView.display(librarian);
        if (editedLibrarian != null) {
            boolean success = handler.editLibrarian(editedLibrarian);
            if (success) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Successfull edit");
                successAlert.setHeaderText("Successfull edit");
                successAlert.setContentText("Borrower was successfully edited.");
                successAlert.show();
                updateLibrarianList();
            } else {
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("Failed edit");
                failure.setHeaderText("Failed edit");
                failure.setContentText("Something went wrong with edit.");
                failure.show();
            }
        }
    }

    // -------- UPDATE METHODS --------
    /**
     * Updates the list of books.
     */
    private void updateBookList() {
        bookList.clear();
        bookList.addAll(handler.listBooks());
    }

    /**
     * Updates the list of borrowers.
     */
    private void updateBorrowerList() {
        borrowerList.clear();
        borrowerList.addAll(handler.listBorrowers());
    }

    /**
     * Updates the list of librarians
     */
    private void updateLibrarianList() {
        librarianList.clear();
        librarianList.addAll(handler.listLibrarians());
    }

    /**
     *
     */
    private void updateCopyList() {
        copyList.clear();
        copyList.addAll(handler.listCopies());
    }

    /**
     * Updates the inventory list.
     */
    private void updateInventoryList() {
        bookList.clear();
        bookList.addAll(handler.listBooks());
    }
    
    /**
     * Updates the loan borrowers list.
     */
    private void updateLoanBorrowers() {
        loanBorrowers.clear();
        loanBorrowers.addAll(borrowerList);
    }

    /**
     * Updates the tab with copys registered for loan
     */
    private void updateCopyRegisteredForLoan() {
        copyRegisteredForLoanList.clear();
    }
    
    /**
     * Updates all the list in the loan tab.
     */
    private void updateLoanTabLists(){
        updateCopyRegisteredForLoan();
        updateLoanBorrowers();
        updateInventoryList();
    }
    
    private void updateLoanTab() {
        updateLoanBorrowers();
        updateInventoryList();
    }
   
    /**
     * Updates all the lists
     */
    private void updateAllList() {
        updateBookList();
        updateBorrowerList();
        updateLibrarianList();
        updateCopyList();
        updateLoanTab();
    }

    // -------- ADD METHODS -------- //
    /**
     *
     */
    private void addBookToLoan() {
        //copyRegisteredForLoanList.add(tableViewLoansTop.getSelectionModel().getSelectedItem());
        InventoryBook selectedBook = tableViewLoansTop.getSelectionModel().getSelectedItem();
        List<BookCopy> selectedCopys = handler.getAvailableCopys(selectedBook);
        BookCopy selectedCopy = null;
        int i = 0;
        boolean looping = true;
        while (i < selectedCopys.size() && looping) {
            if (!copyRegisteredForLoanList.contains(selectedCopys.get(i))) {
                selectedCopy = selectedCopys.get(i);
                looping = false;
            }
            i++;
        }
        if (selectedCopy == null) {
            //TODO Display error
        } else {
            copyRegisteredForLoanList.add(selectedCopy);
        }

    }

    /**
     * Adds a book to the database and updates the list/table.
     */
    //TODO legg til feilmelding
    private void addBook() {
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
    private void addBorrower() {
        Borrower newBorrower = addBorrowerView.display();
        if (newBorrower != null && handler.addBorrower(newBorrower)) {
            System.out.println(newBorrower.getFirstName() + " was added");
        } else {
            System.out.println("Failed to add borrower");
        }
        updateBorrowerList();
        updateLoanBorrowers();
    }

    /**
     * Adds a librarian to the database and updates the list/table.
     */
    private void addLibrarian() {
        Librarian newLibrarian = addLibrarianView.display();
        if (newLibrarian != null && handler.addLibrarian(newLibrarian)) {
            System.out.println(newLibrarian.getFirstName() + " was added");
        } else {
            System.out.println("Failed to add employee");
        }
        updateLibrarianList();
    }

    /**
     *
     * @param borrowerId
     * @param librarianId
     * @param numberOfDays
     * @param copys
     */
    private void registerLoan(int borrowerId, int librarianId, int numberOfDays, List<BookCopy> copys) {
        handler.registerLoan(borrowerId, librarianId, numberOfDays, copys);
    }

    /**
     *
     */
    private void registerLoan() {
        //TODO vis error om ingen lånetaker er valgt.
        int borrowerId = tableViewLoanBorrower.getSelectionModel().getSelectedItem().getBorrowerID();
        int librarianId = Integer.parseInt(currentUser.getEmployeeID()); //TODO finn librarian id
        int numberOfDays = DEFAULT_LOAN_DURATION;
        List<BookCopy> copys = copyRegisteredForLoanList;
        handler.registerLoan(borrowerId, librarianId, numberOfDays, copys);
        updateLoanTabLists();
    }

    // -------- REMOVE METHODS -------- //
    /**
     * Removes a book from the database and updates the list/table.
     */
    private void removeBook() {
        InventoryBook bookToDelete = tableViewInventory.getSelectionModel().getSelectedItem();
        if (bookToDelete != null) {
            handler.deleteBook(bookToDelete);
            updateBookList();
        }
    }

    /**
     * Removes a borrower from the database and updates the list/table.
     */
    private void removeBorrower() {
        Borrower borrowerToDelete = tableViewBorrower.getSelectionModel().getSelectedItem();
        if (borrowerToDelete != null) {
            handler.deleteBorrower(borrowerToDelete);
            updateBorrowerList();
        }
    }
    
    private boolean checkIfHasBorrowed() {
        Borrower borrowerToCheck = tableViewBorrower.getSelectionModel().getSelectedItem();
        boolean hasBorrowed = false;
        if(handler.numberOfLoansOnBorrower(borrowerToCheck) > 0) {
            hasBorrowed = true;
        } else {
            return false;
        }
        return true;
    }

    /**
     * Removes a librarian from the databse and updates the list/table.
     */
    private void removeLibrarian() {
        Librarian librarianToDelete = tableViewLibrarian.getSelectionModel().getSelectedItem();
        if (librarianToDelete != null) {
            handler.deleteLibrarian(librarianToDelete);
            updateLibrarianList();
        }
    }

    /**
     * 
     */
    private void doRemoveBorrowerAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Bruker kan ikke ");
        alert.setContentText("plz no");
        alert.showAndWait();
        
    }
    
    /**
     * Exit the application. Displays a confirmation dialog.
     */
    private void doExitApplication() {
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
