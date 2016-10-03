package bibliotek_02;

import java.util.Optional;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Thomas Todal, Oscar Wika
 */
public class GUI extends Application
{
    
    private TextField searchBooks;
    private TableView tableViewUtlanTop;
    private TableView tableViewKopi;
    private TableView<Beholdning>tableViewBeholdning;
    private TableView tableViewKunde;
    private TableView<Ansatt> tableViewAnsatt;
    
    
    // Filler for the ansatt table
    final ObservableList<Ansatt> ansattData = FXCollections.observableArrayList
        (
            new Ansatt("1", "Smith", "Smith"),
            new Ansatt("2", "Johnson", "Johnson"),
            new Ansatt("3", "Williams", "Ethan-Williams"),
            new Ansatt("4", "Jones", "Emma"),
            new Ansatt("5", "Brown", "Michael")
        );
    // Filler for the ansatt table
    final ObservableList<Ansatt> kundeData = FXCollections.observableArrayList
        (
            new Ansatt("John", "Swagmeister", "99911888"),
            new Ansatt("Peter", "Toppris", "33399111"),
            new Ansatt("Lise", "Imsdal", "99933222"),
            new Ansatt("Cristiano", "Google", "88877333"),
            new Ansatt("Del", "Piero", "88855222")
        );
    //Filler for the Beholdning table
    final ObservableList<Beholdning> bookData = FXCollections.observableArrayList
            (
                new Beholdning("1", "Forest Gum", "10"),
                new Beholdning("2", "Google", "22"),
                new Beholdning("3", "Web Design", "15"),
                new Beholdning("4", "SQL", "4"),
                new Beholdning("5", "WoW ProTip", "100")
                    
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
     * @return The "Utlån" tab.
     */
    private Tab createUtlanTab()
    {
        Tab utlanTab = new Tab("Utlån");
        BorderPane utlanBorderPane = new BorderPane();
        HBox utlanHBoxTop = createUtlanHBoxTop();
        BorderPane utlanBorderPaneBottom = createUtlanBorderPaneBottom();
        
        utlanTab.setContent(utlanBorderPane);
        utlanBorderPane.setTop(utlanHBoxTop);
        utlanBorderPane.setBottom(utlanBorderPaneBottom);
        
        utlanTab.setClosable(false);
        
        return utlanTab;
    }

    /**
     * Creates the "Kopi" tab.
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
     * Creates the "Beholdning" tab.
     * @return Returns the "Beholdning" tab.
     */
    private Tab createBeholdningTab()
    {
        Tab beholdningTab = new Tab("Beholdning");
        BorderPane beholdningBorderPane = new BorderPane();
        HBox beholdningHBox = createBeholdningHBox();
        BorderPane utlanBorderPaneBottom = createUtlanBorderPaneBottom();
        
        beholdningTab.setContent(beholdningBorderPane);
        beholdningBorderPane.setTop(beholdningHBox);
        beholdningBorderPane.setBottom(utlanBorderPaneBottom);
        
        beholdningTab.setClosable(false);
        
        return beholdningTab;
    }

    /**
     * Creates the "Kunde" tab.
     * @return Returns the "Kunde" tab.
     */
    private Tab createKundeTab()
    {
        Tab kundeTab = new Tab("Kunde");
        BorderPane kundeBorderPane = new BorderPane();
        HBox kundeHBox = createKundeHBox();
        BorderPane utlanBorderPaneBottom = createUtlanBorderPaneBottom();
        
        kundeTab.setContent(kundeBorderPane);
        kundeBorderPane.setTop(kundeHBox);
        kundeBorderPane.setBottom(utlanBorderPaneBottom);
        
        kundeTab.setClosable(false);
        
        return kundeTab;
    }

    /**
     * Creates the "Ansatt" tab.
     * @return Returns the "Ansatt" tab.
     */
    private Tab createAnsattTab()
    {
        Tab ansattTab = new Tab("Ansatt");
        BorderPane ansattBorderPane = new BorderPane();
        HBox ansattHBox = createAnsattHBox();
        BorderPane utlanBorderPaneBottom = createUtlanBorderPaneBottom();
        
        ansattTab.setContent(ansattBorderPane);
        ansattBorderPane.setTop(ansattHBox);
        ansattBorderPane.setBottom(utlanBorderPaneBottom);
        
        ansattTab.setClosable(false);
        
        return ansattTab;
    }

    /**
     * Creates the tab pane.
     * @return Returns the tab pane.
     */
    private TabPane createTabPane()
    {
        Tab utlan = createUtlanTab();
        Tab kopi = createKopiTab();
        Tab beholdning = createBeholdningTab();
        Tab kunde = createKundeTab();
        Tab ansatt = createAnsattTab();
        
        TabPane tabPane = new TabPane(utlan, kopi, beholdning, kunde, ansatt);
        
        return tabPane;
    }
    
    /**
     * Creates a search bar in the "Utlån" tab.
     * @return Return the search bar.
     */
    private HBox createUtlanHBoxTop()
    {
        HBox utlanHBoxTop = new HBox();
        searchBooks = new TextField();
        tableViewUtlanTop = new TableView();
        
        searchBooks.setPromptText("Søk etter Bok-ID, ISBN, Tittel, Forfatter...");
        
        TableColumn fornavnCol = new TableColumn("N/A");
        TableColumn etternavnCol = new TableColumn("N/A");
        TableColumn telefonCol = new TableColumn("N/A");
        tableViewUtlanTop.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);
        tableViewUtlanTop.setMinSize(450, 150);
        tableViewUtlanTop.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        utlanHBoxTop.getChildren().add(searchBooks);
        utlanHBoxTop.getChildren().add(tableViewUtlanTop);
        
        return utlanHBoxTop;
    }
    
    /**
     * Creates the table in the "Kopi" tab.
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
     * Creates the table in the "Beholdning" tab.
     * @return Returns a HBox containing a table for the "Beholdning" tab.
     */
    private HBox createBeholdningHBox()
    {
        HBox beholdningHBox = new HBox();
        tableViewBeholdning = new TableView();
        
        TableColumn fornavnCol = new TableColumn("ID");
        fornavnCol.setCellValueFactory(new PropertyValueFactory<>("BookID"));
        
        TableColumn etternavnCol = new TableColumn("Navn");
        etternavnCol.setCellValueFactory(new PropertyValueFactory<>("BookName"));
        
        TableColumn telefonCol = new TableColumn("Antall");
        telefonCol.setCellValueFactory(new PropertyValueFactory<>("BookQuantity"));
        
        tableViewBeholdning.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);
        
        tableViewBeholdning.setItems(bookData);
        tableViewBeholdning.setMinSize(450, 175);
        tableViewBeholdning.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        beholdningHBox.getChildren().add(tableViewBeholdning);
        
        return beholdningHBox;
    }
    
    /**
     * Creates the table in the "Kunde" tab.
     * @return Returns a HBox containing a table for the "Kudne" tab.
     */
    private HBox createKundeHBox()
    {
        HBox kundeHBox = new HBox();
        tableViewKunde = new TableView();
        
        TableColumn fornavnCol = new TableColumn("Fornavn");
        fornavnCol.setCellValueFactory(new PropertyValueFactory<>("ansattID"));
            
        TableColumn etternavnCol = new TableColumn("Etternavn");
        etternavnCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        
        TableColumn telefonCol = new TableColumn("Telefon");
        telefonCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        
        tableViewKunde.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);
        
        tableViewKunde.setItems(kundeData);
        tableViewKunde.setMinSize(450, 175);
        tableViewKunde.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        kundeHBox.getChildren().add(tableViewKunde);
        
        return kundeHBox;
    }
    
    /**
     * Creates the table in the "Ansatt" tab.   
     * @return Returns a HBox containing a table for the "Ansatt" tab.
     */
    private HBox createAnsattHBox()
    {
        HBox ansattHBox = new HBox();
        tableViewAnsatt = new TableView();
        
        TableColumn ansattIDCol = new TableColumn("AnsattID");
        ansattIDCol.setCellValueFactory(new PropertyValueFactory<>("ansattID"));
        
        TableColumn fornavnCol = new TableColumn("Fornavn");
        fornavnCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        
        TableColumn etternavnCol = new TableColumn("Etternavn");
        etternavnCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        
        tableViewAnsatt.getColumns().addAll(ansattIDCol, fornavnCol, etternavnCol);
        
        
        tableViewAnsatt.setItems(ansattData);
        tableViewAnsatt.setMinSize(450, 175);
        tableViewAnsatt.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ansattHBox.getChildren().add(tableViewAnsatt);
        
        return ansattHBox;
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