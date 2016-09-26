/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotek_02;

import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author ThomasSTodal
 */
public class GUI extends Application
{
    
    private TextField searchBooks;
    private ObservableList<String> books;
    private TableView<String> boooks;
    
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
        books = FXCollections.observableArrayList();
        
        // Window
        BorderPane mainBorderPane = new BorderPane();
        // Menu
        TabPane tabPane = createTabPane();
        
        mainBorderPane.setCenter(tabPane);
        
        Scene scene = new Scene(mainBorderPane, 1280, 720);
        scene.getStylesheets().add("stylesheet.css");
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
     * 
     * @return 
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

    private Tab createKopiTab()
    {
        Tab kopiTab = new Tab("Kopi");
        BorderPane kopiBorderPane = new BorderPane();
        
        kopiTab.setContent(kopiBorderPane);
        
        kopiTab.setClosable(false);
        
        return kopiTab;
    }

    /**
     * 
     * @return 
     */
    private Tab createBeholdningTab()
    {
        Tab beholdningTab = new Tab("Beholdning");
        BorderPane beholdningBorderPane = new BorderPane();
        
        beholdningTab.setContent(beholdningBorderPane);
        
        beholdningTab.setClosable(false);
        
        return beholdningTab;
    }

    /**
     * 
     * @return 
     */
    private Tab createKundeTab()
    {
        Tab kundeTab = new Tab("Kunde");
        BorderPane kundeBorderPane = new BorderPane();
        
        kundeTab.setContent(kundeBorderPane);
        
        kundeTab.setClosable(false);
        
        return kundeTab;
    }

    /**
     * 
     * @return 
     */
    private Tab createAnsattTab()
    {
        Tab ansattTab = new Tab("Ansatt");
        BorderPane ansattBorderPane = new BorderPane();
        
        ansattTab.setContent(ansattBorderPane);
        
        ansattTab.setClosable(false);
        
        return ansattTab;
    }

    /**
     * 
     * @return 
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
     * 
     * @return 
     */
    private HBox createUtlanHBoxTop()
    {
        HBox utlanHBoxTop = new HBox();
        searchBooks = new TextField();
        
        
        searchBooks.setPromptText("Søk etter ISBN, Tittel, Forfatter...");
        
        utlanHBoxTop.getChildren().add(searchBooks);
        // utlanHBoxTop.getChildren().add(books);
        
        return utlanHBoxTop;
    }

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