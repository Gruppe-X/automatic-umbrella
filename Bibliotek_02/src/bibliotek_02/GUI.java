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

/**
 *
 * @author Thomas Todal, Oscar Wika
 */
public class GUI extends Application
{
    
    private TextField searchBooks;
    private TableView tableViewUtlanTop;
    private TableView tableViewKopi;
    private TableView tableViewBeholdning;
    private TableView tableViewKunde;
    private TableView<Ansatt> tableViewAnsatt;
    
    final ObservableList<Ansatt> data = FXCollections.observableArrayList
        (
            new Ansatt("1", "Smith", "Smith"),
            new Ansatt("2", "Johnson", "Johnson"),
            new Ansatt("3", "Williams", "Ethan-Williams"),
            new Ansatt("4", "Jones", "Emma"),
            new Ansatt("5", "Brown", "Michael")
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
        scene.getStylesheets().add(getClass().getResource("Djstyleshix.css").toExternalForm());
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
        
        TableColumn fornavnCol = new TableColumn("N/A");
        TableColumn etternavnCol = new TableColumn("N/A");
        TableColumn telefonCol = new TableColumn("N/A");
        tableViewBeholdning.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);
        
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
        TableColumn etternavnCol = new TableColumn("Etternavn");
        TableColumn telefonCol = new TableColumn("Telefon");
        tableViewKunde.getColumns().addAll(fornavnCol, etternavnCol, telefonCol);
        
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
        
        
        tableViewAnsatt.setItems(data);
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