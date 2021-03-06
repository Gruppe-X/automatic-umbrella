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
import javafx.event.ActionEvent;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

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
    private TableView<Copy> tableViewCopy;
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
    
    private List<Node> adminControls;

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
        
        adminControls = new ArrayList<>();

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
        //primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1280);
        setUser(currentUser);
        primaryStage.show();
        // Close window confirmation
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            doExitApplication();
        });
    }
    
    private void setUser(Librarian user){
        if(user.isAdmin()){
            enableAdminControls();
        } else {
            disableAdminControls();
        }
        currentUser = user;
    }
    
    private void enableAdminControls(){
        for(Node node : adminControls){
            node.setDisable(false);
        }
    }
    
    private void disableAdminControls(){
        for(Node node : adminControls){
            node.setDisable(true);
        }
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
        addButton.setTooltip(new Tooltip("Legg til bok i kurven(?)"));
        Button regretButton = new Button("Angre");
        regretButton.setTooltip(new Tooltip("Avbrytt bestilling"));
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
        registerLoanButton.setTooltip(new Tooltip("Registrer lån på en kunde"));
        registerLoanButton.setOnAction(e -> {
            if(copyRegisteredForLoanList.isEmpty() ||tableViewLoanBorrower.getSelectionModel().getSelectedItem() == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Feilmelding");
                alert.setHeaderText("Ugyldig Registrering");
                alert.setContentText("Ooops, mangler bok eller lånetaker ");
                alert.showAndWait();
            }
            else
            {
            registerLoan();
            tableViewLoansTop.setItems(bookList);
            searchBooks.textProperty().set("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informasjon Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Utlån vellykket");
            alert.showAndWait();
            }
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
        registerBorrowerButton.setTooltip(new Tooltip("Legg til ny lånetaker"));
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
        //HBox copyBottomContent = createCopyBottomContent();

        copyTab.setContent(copyBorderPane);
        copyBorderPane.setCenter(copyContent);
        //copyContent.getChildren().addAll(copyTopContent, copyBottomContent);
        copyContent.getChildren().addAll(copyTopContent);

        VBox.setVgrow(copyTopContent, Priority.ALWAYS);
        //VBox.setVgrow(copyBottomContent, Priority.ALWAYS);

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
        BorderPane mainButtonContainer = new BorderPane();
        HBox buttonContainer = new HBox();
        HBox updateButtonContainer = new HBox();
        Button returnButton = new Button("Returner");
        returnButton.setTooltip(new Tooltip("Markerer lånet som returnert"));
        returnButton.setOnAction(e -> returnLoan());
        tableViewCopy = new TableView();
        searchCopy = new TextField();
        
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
        allGoodCol.setCellValueFactory(new PropertyValueFactory("HandedIn"));

        tableViewCopy.getColumns().addAll(kvittNrCol, datoCol, daysLeftCol, navnCol, allGoodCol);

        tableViewCopy.setItems(copyList);
        tableViewCopy.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        buttonContainer.getChildren().add(returnButton);
        mainButtonContainer.setLeft(buttonContainer);
        mainButtonContainer.setRight(updateButtonContainer);
        topContent.getChildren().add(mainButtonContainer);
        topContent.getChildren().add(searchCopy);
        topContent.getChildren().add(tableViewCopy);
        
        VBox.setVgrow(tableViewCopy, Priority.ALWAYS);
        return topContent;
    }
    
    private void returnLoan(){
        int loanId = Integer.parseInt(tableViewCopy.getSelectionModel().getSelectedItem().getLoanID());
        handler.returtLoan(loanId);
        updateAllList();
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
        inventoryBorderPane.setCenter(inventoryVBox);
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
        borrowerBorderPane.setCenter(borrowerVBox);
        //borrowerBorderPane.setBottom(loansBorderPaneBottom);

        return borrowerTab;
    }

    /**
     * Creates the "Librarian" tab.
     *
     * @return Returns the "Librarian" tab.
     */
    private Tab createLibrarianTab() {
        Tab librarianTab = new Tab("Ansatt");
        BorderPane librarianBorderPane = new BorderPane();

        VBox librarianVBox = createLibrarianVBox();
        //BorderPane loansBorderPaneBottom = createLoansBorderPaneBottom();

        librarianTab.setContent(librarianBorderPane);
        librarianBorderPane.setCenter(librarianVBox);
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
        BorderPane mainButtonContainer = new BorderPane();
        HBox buttonContainer = new HBox();
        HBox updateButtonContainer = new HBox();
        //Button updateButton = new Button("Oppdater");
        //updateButton.setTooltip(new Tooltip("Oppdater tabellen"));
        //updateButton.setOnAction(e -> updateLoanTabLists());
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
        //updateButtonContainer.getChildren().add(updateButton);
        mainButtonContainer.setLeft(buttonContainer);
        mainButtonContainer.setRight(updateButtonContainer);
        loansVBox.getChildren().add(mainButtonContainer);
        loansVBox.getChildren().add(searchBooks);
        loansVBox.getChildren().add(tableViewLoansTop);
        VBox.setVgrow(tableViewLoansTop, Priority.ALWAYS);
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
        BorderPane mainButtonContainer = new BorderPane();
        HBox buttonContainer = new HBox();
        HBox updateButtonContainer = new HBox();
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
        
        Button addButton = new Button("Legg til");
        addButton.setTooltip(new Tooltip("Legg til bok"));
        adminControls.add(addButton);
        addButton.setOnAction(e -> addBook());
        Button removeButton = new Button("Fjern");
        adminControls.add(removeButton);
        removeButton.setTooltip(new Tooltip("Fjern bok"));
        removeButton.setOnAction(e -> removeBook());
        //Button updateButton = new Button("Oppdater");
        //updateButton.setTooltip(new Tooltip("Oppdater tabellen"));
        //updateButton.setOnAction(e -> updateBorrowerList());
        Button editButton = new Button("Rediger");
        adminControls.add(editButton);
        editButton.setTooltip(new Tooltip("Rediger bok"));
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
        //tableViewInventory.setMinSize(450, 175);
        tableViewInventory.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        buttonContainer.getChildren().addAll(addButton, removeButton, editButton);
        //updateButtonContainer.getChildren().add(updateButton);
        mainButtonContainer.setLeft(buttonContainer);
        mainButtonContainer.setRight(updateButtonContainer);
        inventoryVBox.getChildren().add(mainButtonContainer);
        inventoryVBox.getChildren().add(searchInventory);
        inventoryVBox.getChildren().add(tableViewInventory);
        
        VBox.setVgrow(tableViewInventory, Priority.ALWAYS);
        return inventoryVBox;
    }
    
    /**
     * 
     * @param book 
     */
    private void editBook(InventoryBook book){
        InventoryBook editedBook = editBookView.display(book);
        if (editedBook != null) {
            boolean success = handler.editBook(editedBook);
            if (success) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Redigering fullført");
                successAlert.setHeaderText("Redigering fullført");
                successAlert.setContentText("Boken ble vellykket redigert.");
                successAlert.show();
                updateBookList();
            } else {
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("Feil");
                failure.setHeaderText("Kunne ikke redigere");
                failure.setContentText("Noe gikk galt, boken ble ikke redigert.");
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
        BorderPane mainButtonContainer = new BorderPane();
        HBox buttonContainer = new HBox();
        HBox updateButtonContainer = new HBox();
        tableViewBorrower = new TableView();
        searchBorrower = new TextField();
        Button addButton = new Button("Legg til");
        addButton.setTooltip(new Tooltip("Legg til lånetaker"));
        addButton.setOnAction(e -> addBorrower());
        Button removeButton = new Button("Fjern");
        adminControls.add(removeButton);
        removeButton.setTooltip(new Tooltip("Fjern lånetaker"));
        removeButton.setOnAction((ActionEvent event) -> {
            if (tableViewBorrower.getSelectionModel().getSelectedItem() != null) {
                if (checkIfHasBorrowed() == true) {
                    doRemoveBorrowerAlert();
                } else {
                    removeBorrower();
                }
            }
        });
//        Button updateButton = new Button("Oppdater");
//        updateButton.setTooltip(new Tooltip("Oppdater tabellen"));
//        updateButton.setOnAction(e -> updateBorrowerList());

        Button editButton = new Button("Rediger");
        editButton.setTooltip(new Tooltip("Rediger lånetaker"));
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
        //tableViewBorrower.setMinSize(450, 175);
        tableViewBorrower.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        buttonContainer.getChildren().addAll(addButton, removeButton, editButton);
//        updateButtonContainer.getChildren().add(updateButton);
        mainButtonContainer.setLeft(buttonContainer);
        mainButtonContainer.setRight(updateButtonContainer);
        borrowerVBox.getChildren().add(mainButtonContainer);
        borrowerVBox.getChildren().add(searchBorrower);
        borrowerVBox.getChildren().add(tableViewBorrower);
        VBox.setVgrow(tableViewBorrower, Priority.ALWAYS);
        return borrowerVBox;
    }
    
    /**
     * 
     * @param borrower 
     */
    private void editBorrower(Borrower borrower) {
        Borrower editedBorrower = editBorrowerView.display(borrower);
        if (editedBorrower != null) {
            boolean success = handler.editBorrower(editedBorrower);
            if (success) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Redigering fullført");
                successAlert.setHeaderText("Redigering fullført");
                successAlert.setContentText("Lånetaker ble vellykket redigert.");
                successAlert.show();
                updateBorrowerList();
            } else {
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("Feil");
                failure.setHeaderText("Kunne ikke redigere");
                failure.setContentText("Noe gikk galt, kunden ble ikke redigert.");
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
        BorderPane mainButtonContainer = new BorderPane();
        HBox buttonContainer = new HBox();
        HBox updateButtonContainer = new HBox();
        tableViewLibrarian = new TableView();
        searchLibrarian = new TextField();
        Button addButton = new Button("Legg til");
        adminControls.add(addButton);
        addButton.setTooltip(new Tooltip("Legg til ny ansatt"));
        addButton.setOnAction(e -> addLibrarian());
        Button removeButton = new Button("Fjern");
        adminControls.add(removeButton);
        removeButton.setTooltip(new Tooltip("Fjern ansatt"));
        removeButton.setOnAction(e -> removeLibrarian());
//        Button updateButton = new Button("Oppdater");
//        updateButton.setTooltip(new Tooltip("Oppdater tabell"));
//        updateButton.setOnAction(e -> updateBorrowerList());

        Button editButton = new Button("Rediger");
        adminControls.add(editButton);
        editButton.setTooltip(new Tooltip("Rediger ansatt"));
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
        //tableViewLibrarian.setMinSize(450, 175);
        tableViewLibrarian.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        buttonContainer.getChildren().addAll(addButton, removeButton, editButton);
//        updateButtonContainer.getChildren().add(updateButton);
        mainButtonContainer.setLeft(buttonContainer);
        mainButtonContainer.setRight(updateButtonContainer);
        librarianVBox.getChildren().add(mainButtonContainer);
        librarianVBox.getChildren().add(searchLibrarian);
        librarianVBox.getChildren().add(tableViewLibrarian);
        VBox.setVgrow(tableViewLibrarian, Priority.ALWAYS);
        return librarianVBox;
    }
    /**
     * 
     * @param librarian 
     */
    private void editLibrarian(Librarian librarian) {
        Librarian editedLibrarian = editLibrarianView.display(librarian);
        if (editedLibrarian != null) {
            boolean success = handler.editLibrarian(editedLibrarian);
            if (success) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Redigering fullført");
                successAlert.setHeaderText("Redigering fullført");
                successAlert.setContentText("Ansatt ble vellykket redigert.");
                successAlert.show();
                updateLibrarianList();
            } else {
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("Feil");
                failure.setHeaderText("Kunne ikke redigere");
                failure.setContentText("Noe gikk galt, ansatt ble ikke redigert.");
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
    
    /**
     * 
     */
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
            //System.out.println("Failed to add book");
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
            //System.out.println("Failed to add borrower");
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
            //System.out.println("Failed to add employee");
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
    
    /**
     * 
     * @return 
     */
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
        alert.setTitle("Feil");
        alert.setHeaderText("Bruker kan ikke slettes");
        alert.setContentText("Denne bruker er koplet til et lån.");
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
