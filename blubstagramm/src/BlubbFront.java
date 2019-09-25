import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class BlubbFront extends Application {

    private GridPane root = new GridPane();
    private Text label = new Text();
    private Text userNameLabel = new Text("              Username");
    private Text passwordLabel = new Text("              Password");
    private TextField  userName = new TextField("");
    private TextField password = new TextField("");
    private Button submitButton = new Button("submit");
    private Button cancelButton = new Button("cancel");
    private Button newUserButton = new Button("new User");
    private HBox newUserHBox = new HBox();

    // Database login data
    private String dbPath = "jdbc:mysql://localhost:3306/blubstagram?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String dbUserName = "root";
    private String dbPassword = "";

    //password wrong window
    Stage pwWrong = new Stage();

    // Query output Textarea
    TextArea sqlQueryOutput = new TextArea();

    // addPicture-data
    String newPicturePath = "";
    String newPictureName = "";
    String newPicCategory= "";

    //show picture globals
    ArrayList<ImageView> picturesArrayList;

    //Session Data (logged User)
    String loggedUser = "";
    
    //categoryList
    ArrayList<String> categories= new ArrayList<String>();


    @Override
    public void start(Stage primaryStage) throws Exception, FileNotFoundException {

        // main-layout
        root.setVgap(5);
        root.setHgap(5);
        root.setPadding(new Insets(10, 10, 10, 10));            //padding
        root.setMinSize(400, 350);
        //root.setGridLinesVisible(true);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: rgb(17,120,120)");

        // Login window setup
        label.setText("Blubstagram");
        label.setStyle("-fx-font-family: Courier; -fx-font-size: 40pt; -fx-stroke: white; -fx-stroke-width: 2; -fx-font-color: red");

        userName.setMinSize(200,25);
        password.setMinSize(200, 25);
        submitButton.setMinSize(97,25);
        submitButton.setStyle("-fx-background-color: rgb(136,140,140)");
        //submitButton.setGraphic(new ImageView(ImageView));            //setting graphic for the button (Duh....)
        cancelButton.setMinSize(97,25);
        cancelButton.setStyle("-fx-background-color: rgb(136,140,140)");
        // setting up the newUserButton with own HBox
        newUserHBox.setAlignment(Pos.BOTTOM_RIGHT);
        newUserHBox.setMinSize(400,200);
        newUserHBox.setStyle("-fx-background-color:  rgb(17,120,120)");
        newUserButton.setMinSize(97,25);
        newUserButton.setStyle("-fx-background-color: rgb(136,140,140)");
        newUserHBox.getChildren().add(newUserButton);

        // add pic window globals

        Image placeholder = new Image(new FileInputStream(".\\Images\\2Kreise.jpg"));
        ImageView chosenImagePreview = new ImageView(placeholder);
        ImageView sqlQueryImageView = new ImageView(placeholder);
        
        // categories
        
        categories.add("Cat1");
        categories.add("Cat2");
        categories.add("Cat3");
        categories.add("Cat4");
        categories.add("Cat5");

        // button action handler

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        newUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newUserWindowBuild(primaryStage);
            }
        });

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String userNameInput = userName.getText();
                loggedUser = userName.getText();
                String passwordInput = password.getText();

                //passwort verifizierung
                boolean password = verifyUserdata(userNameInput, passwordInput);
                //boolean password = true;

                // -> true -> menu
                if(password){
                    // Open MainMenu-Window
                    // bis Zeile 200 Main Menu Fenster
                    //
                    GridPane menuLayout = new GridPane();
                    Label mainMenuLabel = new Label("Main Menu");
                    Button addPic = new Button("add Picture");
                    addPic.setMinSize(100,25);
                    addPic.setStyle("-fx-background-color: rgb(136,140,140)");
                    Button showPics = new Button("show Pictures");
                    showPics.setMinSize(100,25);
                    showPics.setStyle("-fx-background-color: rgb(136,140,140)");
                    Button leaveApp = new Button("Quit");
                    leaveApp.setMinSize(100,25);
                    leaveApp.setStyle("-fx-background-color: rgb(136,140,140)");
                    Button showAllPics = new Button("show all Pictures");
                    showAllPics.setMinSize(100,25);
                    showAllPics.setStyle("-fx-background-color: rgb(136,140,140)");
                    Stage menuWindow = new Stage();

                    // button action-handler
                    // Quit Button:
                    leaveApp.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            System.exit(0);
                        }
                    });

                    //  show picures button -> open showPicWindow
                    showPics.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
// Show Pictures Window
                            showChosenPicturesWindow(menuWindow, placeholder);
                        }
                    });

                    // add pic button -> open addPicWindow
                    addPic.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event){
// addPic Window
                            addPicWindowBuild(menuWindow, chosenImagePreview);
                        }
                    });

// show all pictures window
                    //show all pics button -> open show all pictures window
                    showAllPics.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            showAllPicturesWindow(primaryStage,placeholder);
                        }
                    });

                    menuLayout.setMinSize(400,350);
                    menuLayout.setHgap(10);
                    menuLayout.setAlignment(Pos.CENTER);
                    menuLayout.setStyle("-fx-background-color:  rgb(17,120,120)");

                    //Vbox for the buttons
                    VBox vbox1 = new VBox();
                    vbox1.setAlignment(Pos.CENTER);
                    vbox1.setMinSize(250,100);
                    vbox1.setSpacing(10);
                    vbox1.getChildren().add(addPic);
                    vbox1.getChildren().add(showPics);
                    vbox1.getChildren().add(showAllPics);
                    vbox1.getChildren().add(leaveApp);


                    mainMenuLabel.setStyle("-fx-font-family: Courier; -fx-font-size: 30pt; -fx-stroke: white; -fx-stroke-width: 2; -fx-font-color: red");
                    mainMenuLabel.setMinSize(250,100);
                    mainMenuLabel.setAlignment(Pos.CENTER);
                    addPic.setAlignment(Pos.CENTER);

                    menuLayout.add(mainMenuLabel,0,0);
                    menuLayout.add(vbox1,0,1);

                    Scene menuScene = new Scene(menuLayout, 400, 350);

                    menuWindow.setTitle("Main Menu");
                    menuWindow.setScene(menuScene);
                    menuWindow.setX(primaryStage.getX());
                    menuWindow.setY(primaryStage.getY());
                    menuWindow.show();
                    //Close Window
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                }

                // false -> modal window, password falsch...
                else {
                    //Window build for WrongPwWindow
                    pwWrong = pwWrongWindowBuild(primaryStage,pwWrong);
                }
            }
        });

        // adding elements to root-layout (login-window)
        root.add(label, 0, 0, 4, 1);
        root.add(userNameLabel, 0,1);
        root.add(passwordLabel, 0,2);
        root.add(userName,1,1, 2,1);
        root.add(password,1,2,2,1);
        root.add(submitButton, 1,3);
        root.add(cancelButton, 2,3);
        root.add(newUserHBox,0,5,4,1);

        // Key Events / Button Handler

        root.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() ==  KeyCode.ENTER) {
                    submitButton.fire();
                }
            }
        });

        // Login-window scene

        Scene scene = new Scene(root, 600, 500);

        //login-window stage

        primaryStage.setTitle("Blubstagramm!!!");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    // TODO End of Main Code!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public ResultSet sql_abfrage(String sqlQuery){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(dbPath
                    , dbUserName, dbPassword);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            /*
            while (rs.next())
            {
                //System.out.println(rs.getString(1));
                sqlQueryOutput.appendText(rs.getString(1));
            }
            */
            con.close();
            return rs;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void sql_eingabe(String sqlStatement){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(dbPath
                    , dbUserName, dbPassword);

            Statement stmt = con.createStatement();
            stmt.executeUpdate(sqlStatement);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean verifyUserdata(String user, String pw){

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(dbPath
                    , dbUserName, dbPassword);

            Statement stmt = con.createStatement();
            ResultSet passwordFromDb = stmt.executeQuery("select pw from tbl_user where userName='" + user + "'");
            while(passwordFromDb.next()) {
                if (pw.equals(passwordFromDb.getString("pw"))) {
                    return true;
                }
            }
        }catch (Exception e) {
        System.out.println(e);
        }
        return false;
    }

    public Stage pwWrongWindowBuild(Stage parent, Stage pwWrongStage){

        Label passwordWrongWindowLabel = new Label("Password wrong!!!");
        Button closeWindowButton = new Button("Ok");

        closeWindowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });

        closeWindowButton.setStyle("-fx-background-color: rgb(136,140,140)");

        VBox passwordWrongLayout = new VBox();
        passwordWrongLayout.setAlignment(Pos.CENTER);
        passwordWrongLayout.getChildren().add(passwordWrongWindowLabel);
        passwordWrongLayout.getChildren().add(closeWindowButton);
        passwordWrongLayout.setStyle("-fx-background-color: rgb(17,120,120)");

        passwordWrongLayout.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() ==  KeyCode.ENTER) {
                    closeWindowButton.fire();
                }
            }
        });

        Scene passwordWrongScene = new Scene(passwordWrongLayout, 230, 100);

        // New window (Stage)
        //Stage thirdWindow = new Stage();
        pwWrongStage.setTitle("Password wrong");
        pwWrongStage.setScene(passwordWrongScene);

        // Specifies the modality for new window.
        //pwWrong.initModality(Modality.WINDOW_MODAL);
        //Modality.WINDOW_MODAL; //locks the parent window
        // Modality.APPLICATION_MODAL locks all other windows

        // Specifies the owner Window (parent) for new window
        //pwWrong.initOwner(parent);

        // Set position of second window, related to primary window.
        pwWrongStage.setX(parent.getX() + 200);
        pwWrongStage.setY(parent.getY() + 100);

        pwWrongStage.show();
        return pwWrongStage;
    }

    public void addPicWindowBuild(Stage parent, ImageView chosenImagePreview){
        GridPane addPicWindowLayout = new GridPane();
        VBox vBox1 = new VBox();
        VBox vBox2 = new VBox();
        VBox vBox3 = new VBox();

        Label addPicMenuLabel = new Label("Add Picture");
        Text picPathLabel = new Text("Picture Path");
        TextField picPathInfoField = new TextField();
        Text picNameLabel = new Text("Picture Name");
        TextField picNameInput = new TextField();
        Text categoryComboBoxLabel = new Text("Kategorie");
        ComboBox categoryComboBox = new ComboBox();
        String categoryChoice = "";
        Button chooosePicButton = new Button("choose Picture");
        Button submitPictureButton = new Button("submit Picture");

        // child-layout-elements-setup
        submitPictureButton.setStyle("-fx-background-color: rgb(136,140,140)");
        chooosePicButton.setStyle("-fx-background-color: rgb(136,140,140)");
        categoryComboBox.setMinSize(125,25);


        // MainLayout setup

        addPicWindowLayout.setAlignment(Pos.CENTER);
        addPicWindowLayout.setHgap(10);
        addPicWindowLayout.setVgap(5);
        addPicWindowLayout.setMinSize(1000,800);
        addPicWindowLayout.setPadding(new Insets(10,10,10,10));
        addPicWindowLayout.setStyle("-fx-background-color:  rgb(17,120,120)");

        Stage addPictureStage = new Stage();



        // Label setup
        addPicMenuLabel.setStyle("-fx-font-family: Courier; -fx-font-size: 30pt; -fx-stroke: white; -fx-stroke-width: 2; -fx-font-color: red");

        // Combobox setup
        for (String cat:categories) {
            categoryComboBox.getItems().add(cat);
        }

        //getting selected Item in the combobox
        //categoryComboBox.getSelectionModel().getSelectedItem().toString();

        // to enter new Values in the combobox
        // categoryComboBox.setEditable(true);

        // categoryChoice = categoryComboBox.getValue().toString();

        // submitButton setup & action-handler

        chooosePicButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //überprüfung ob Felder ausgefüllt
                // if (cathegoryChoice !=null && picPathInfoField.length() > 0 (regex Bildpfad verifizieren oder auf Bilddatei prüfen))

                FileChooser picChooserFC = new FileChooser();
                File selectedFile = picChooserFC.showOpenDialog(addPictureStage);
                picPathInfoField.setText(selectedFile.toString());
                try{
                    Image chosenImage = new Image(new FileInputStream(selectedFile.toString()));
                    chosenImagePreview.setImage(chosenImage);
                    newPicturePath = selectedFile.toString();
                } catch (FileNotFoundException e){
                    System.out.println("File not found");
                }

            }
        });

        //submitPicture Button setup
        submitPictureButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // erfasste/eingegebene Daten in Variablen schreiben
                newPicCategory = categoryComboBox.getSelectionModel().getSelectedItem().toString();         //
                newPictureName = picNameInput.getText();

                // Bild in Images kopieren und neuen Pfad in Variable schreiben
                File newPictureFile = new File(newPicturePath);
                File newPicturePath = new File(".\\Images\\" + newPictureName + ".jpg");
                try {
                    Files.copy(newPictureFile.toPath(), newPicturePath.toPath());
                    sql_eingabe("insert into pix(userName, category, added, pname) values('" + loggedUser + "', '" + newPicCategory + "', CURRENT_TIMESTAMP(),'" + newPictureName + "')");
                } catch(IOException e){
                    System.out.println("IOException");
                    pictureNameAlreadyTaken(addPictureStage);
                }

                //sql_eingabe("insert into pix(userName, category, added, pname) values('" + loggedUser + "', '" + newPicCategory + "', CURRENT_TIMESTAMP(),'" + newPictureName + "')");

            }

            // TODO Alle eingabefelder zurücksetzen für weitere Eingabe
        });

        // Child layout build

        vBox1.getChildren().add(picPathLabel);
        vBox1.getChildren().add(picPathInfoField);

        vBox2.getChildren().add(categoryComboBoxLabel);
        vBox2.getChildren().add(categoryComboBox);

        vBox3.getChildren().add(picNameLabel);
        vBox3.getChildren().add(picNameInput);

        picPathInfoField.setEditable(false);

        // Main layout build

        addPicWindowLayout.add(addPicMenuLabel,0,0,3,1);
        addPicWindowLayout.add(vBox1,0,2);
        addPicWindowLayout.add(vBox2,2,2);
        addPicWindowLayout.add(vBox3,4,2);
        addPicWindowLayout.add(chooosePicButton,0,6);
        addPicWindowLayout.add(chosenImagePreview,0,11);
        addPicWindowLayout.add(submitPictureButton,0,13);

        addPicWindowLayout.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    submitPictureButton.fire();
                }
            }
        });

        Scene addPictureScene = new Scene(addPicWindowLayout,1000,800);

        addPictureStage.setTitle("add Picture");
        addPictureStage.setScene(addPictureScene);
        addPictureStage.setX(parent.getX());
        addPictureStage.setY(parent.getY());
        addPictureStage.show();

        // Close window
        //((Node)(event.getSource())).getScene().getWindow().hide();

    }

    public void pictureNameAlreadyTaken(Stage parent) {

        Label nameAlreadyTakenLabel = new Label("Name already taken!!!");
        Button closeWindowButton = new Button("Ok");

        closeWindowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });

        closeWindowButton.setStyle("-fx-background-color: rgb(136,140,140)");

        VBox nameAlreadyTakenWindowLayout = new VBox();
        nameAlreadyTakenWindowLayout.setAlignment(Pos.CENTER);
        nameAlreadyTakenWindowLayout.getChildren().add(nameAlreadyTakenLabel);
        nameAlreadyTakenWindowLayout.getChildren().add(closeWindowButton);
        nameAlreadyTakenWindowLayout.setStyle("-fx-background-color: rgb(17,120,120)");

        nameAlreadyTakenWindowLayout.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    closeWindowButton.fire();
                }
            }
        });

        Scene nameAlreadyTakenScene = new Scene(nameAlreadyTakenWindowLayout, 230, 100);

        // New window (Stage)
        Stage nameAlreadyTakenStage = new Stage();
        nameAlreadyTakenStage.setTitle("Password wrong");
        nameAlreadyTakenStage.setScene(nameAlreadyTakenScene);

        // Specifies the modality for new window.
        nameAlreadyTakenStage.initModality(Modality.WINDOW_MODAL);
        //Modality.WINDOW_MODAL; //locks the parent window
        // Modality.APPLICATION_MODAL locks all other windows

        // Specifies the owner Window (parent) for new window
        nameAlreadyTakenStage.initOwner(parent);

        nameAlreadyTakenStage.setX(parent.getX() + 200);
        nameAlreadyTakenStage.setY(parent.getY() + 100);

        nameAlreadyTakenStage.show();
    }

    public void newUserWindowBuild(Stage parent){
        Label newUserWindowLabel = new Label("New User");
        GridPane newUserWindowLayout = new GridPane();
        VBox vBox1 = new VBox();
        VBox vBox2 = new VBox();
        HBox hBox = new HBox();
        HBox labelHBox = new HBox();
        HBox submitButtonHbox = new HBox();
        Text newUserInputLabel = new Text("Username: ");
        TextField newUserInputTextfield = new TextField();
        Text newUserPwInputLabel = new Text("Password: ");
        TextField newUserPwInputTextfield = new TextField();
        Button submitNewUserButton = new Button("register new user");
        //TextArea sqlQueryOutput = new TextArea();

        submitNewUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sql_eingabe("Insert into tbl_user values('" + newUserInputTextfield.getText() + "', '" + newUserPwInputTextfield.getText() + "')");
            }
        });

        // layout setup
        newUserWindowLabel.setAlignment(Pos.CENTER);
        newUserWindowLabel.setStyle("-fx-font-family: Courier; -fx-font-size: 30pt; -fx-stroke: white; -fx-stroke-width: 2; -fx-font-color: red");

        newUserInputTextfield.setMinSize(150,25);
        submitNewUserButton.setStyle("-fx-background-color: rgb(136,140,140)");

        labelHBox.setAlignment(Pos.CENTER);
        labelHBox.getChildren().add(newUserWindowLabel);
        vBox1.setAlignment(Pos.CENTER);
        vBox1.getChildren().add(newUserInputLabel);
        vBox1.getChildren().add(newUserInputTextfield);
        vBox2.setAlignment(Pos.CENTER);
        vBox2.getChildren().add(newUserPwInputLabel);
        vBox2.getChildren().add(newUserPwInputTextfield);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(vBox1);
        hBox.getChildren().add(vBox2);
        submitButtonHbox.setAlignment(Pos.BOTTOM_CENTER);
        submitButtonHbox.getChildren().add(submitNewUserButton);

        newUserWindowLayout.setAlignment(Pos.CENTER);
        newUserWindowLayout.setMinSize(600,500);
        newUserWindowLayout.setVgap(20);
        newUserWindowLayout.setStyle("-fx-background-color:  rgb(17,120,120)");
        newUserWindowLayout.add(labelHBox,0,0);
        newUserWindowLayout.add(hBox,0,1);
        newUserWindowLayout.add(submitButtonHbox,0,3);

        newUserWindowLayout.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                submitNewUserButton.fire();
            }
        });



        Scene newUserWindowScene = new Scene(newUserWindowLayout);

        Stage newUserWindowStage = new Stage();

        newUserWindowStage.setTitle("New User");
        newUserWindowStage.setScene(newUserWindowScene);
        newUserWindowStage.setX(parent.getX());
        newUserWindowStage.setY(parent.getY());

        newUserWindowStage.initModality(Modality.WINDOW_MODAL);
        newUserWindowStage.initOwner(parent);

        newUserWindowStage.show();
    }

    public void showAllPicturesWindow(Stage parent, Image placeholder){
        Label showAllPicturesWindowLabel = new Label("Show all Pics");
        GridPane showAllPicturesWindowLayout = new GridPane();
        VBox vBox1 = new VBox();
        HBox hBox2 = new HBox();
        Button showAllPicturesButton = new Button("Show all pictures");
        ScrollPane pictureScrollPane  = new ScrollPane();

        // layout setup
        showAllPicturesWindowLabel.setAlignment(Pos.CENTER);
        showAllPicturesWindowLabel.setStyle("-fx-font-family: Courier; -fx-font-size: 30pt; -fx-stroke: white; -fx-stroke-width: 2; -fx-font-color: red");


        showAllPicturesButton.setStyle("-fx-background-color: rgb(136,140,140)");
        hBox2.setAlignment(Pos.CENTER);
        hBox2.getChildren().add(showAllPicturesWindowLabel);
        vBox1.setAlignment(Pos.CENTER);

        showAllPicturesWindowLayout.setAlignment(Pos.CENTER);
        showAllPicturesWindowLayout.setMinSize(800,800);
        showAllPicturesWindowLayout.setVgap(20);
        showAllPicturesWindowLayout.setStyle("-fx-background-color:  rgb(17,120,120)");
        showAllPicturesWindowLayout.add(hBox2,0,0);

        showAllPicturesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //sqlQueryOutput.setText(sql_abfrage(sqlInput.getText()));
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    Connection con = DriverManager.getConnection(dbPath
                            , dbUserName, dbPassword);

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from pix where pid>0");
                    Image picture = placeholder;
                    while(rs.next()) {
                        //System.out.println(rs.getString(5));
                        picture = new Image(new FileInputStream(".\\Images\\" + rs.getString(5) + ".jpg"));
                        //System.out.println(".\\Images\\" + rs.getString(5) + ".jpg");
                        ImageView pictureView = new ImageView(picture);
                        vBox1.getChildren().add(pictureView);
                    }
                    con.close();
                }catch(Exception e){
                    System.out.println(e);
                }


            }
        });

        pictureScrollPane.setContent(vBox1);
        pictureScrollPane.setFitToWidth(true);
        pictureScrollPane.setStyle("-fx-background-color: rgb(136,140,140)");
        pictureScrollPane.setMinSize(800,650);
        showAllPicturesWindowLayout.add(showAllPicturesButton,0,1);
        showAllPicturesWindowLayout.add(pictureScrollPane,0,2);


        //sql_abfrage("select * from tbl_user");

        showAllPicturesWindowLayout.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    showAllPicturesButton.fire();
                }
            }
        });



        Scene showPicsWindowScene = new Scene(showAllPicturesWindowLayout);

        Stage showPicsWindowStage = new Stage();

        showPicsWindowStage.setTitle("Show Pics Window");
        showPicsWindowStage.setScene(showPicsWindowScene);
        showPicsWindowStage.setX(parent.getX());
        showPicsWindowStage.setY(parent.getY());
        showPicsWindowStage.show();
    }


    public void showChosenPicturesWindow(Stage parent, Image placeholder){
        Label showChosenPicturesWindowLabel = new Label("Show chosen Pics");
        GridPane showChosenPicturesWindowLayout = new GridPane();
        VBox vBox1 = new VBox();
        HBox hBox2 = new HBox();
        HBox hBox1 = new HBox();
        Text pnameSearchInputLabel = new Text("Search in picturename: ");
        TextField pnameSearchInput = new TextField();
        Text usernameSearchInputLabel = new Text("    username: ");
        TextField usernameSearchInput = new TextField();
        Button showChosenPicturesButton = new Button("Search");
        ScrollPane chosenPictureScrollPane  = new ScrollPane();
        Text categoriesDBLabel = new Text("    category: ");
        ComboBox categoriesCB = new ComboBox();

        // layout setup
        showChosenPicturesWindowLabel.setAlignment(Pos.CENTER);
        showChosenPicturesWindowLabel.setStyle("-fx-font-family: Courier; -fx-font-size: 30pt; -fx-stroke: white; -fx-stroke-width: 2; -fx-font-color: red");

        for (String cat:categories) {
            categoriesCB.getItems().add(cat);
        }

        showChosenPicturesButton.setStyle("-fx-background-color: rgb(136,140,140)");
        hBox2.setAlignment(Pos.CENTER);
        hBox2.getChildren().add(showChosenPicturesWindowLabel);
        hBox1.setAlignment(Pos.CENTER);
        hBox1.getChildren().add(pnameSearchInputLabel);
        hBox1.getChildren().add(pnameSearchInput);
        hBox1.getChildren().add(usernameSearchInputLabel);
        hBox1.getChildren().add(usernameSearchInput);
        hBox1.getChildren().add(categoriesDBLabel);
        hBox1.getChildren().add(categoriesCB);
        vBox1.setAlignment(Pos.CENTER);

        showChosenPicturesWindowLayout.setAlignment(Pos.CENTER);
        showChosenPicturesWindowLayout.setMinSize(800,800);
        showChosenPicturesWindowLayout.setVgap(20);
        showChosenPicturesWindowLayout.setStyle("-fx-background-color:  rgb(17,120,120)");
        showChosenPicturesWindowLayout.add(hBox2,0,0);
        showChosenPicturesWindowLayout.add(hBox1,0,1);


        showChosenPicturesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //sqlQueryOutput.setText(sql_abfrage(sqlInput.getText()));
                try {
                    vBox1.getChildren().remove(0,vBox1.getChildren().size()
                    );

                    Class.forName("com.mysql.cj.jdbc.Driver");

                    Connection con = DriverManager.getConnection(dbPath
                            , dbUserName, dbPassword);

                    Statement stmt = con.createStatement();
                    ResultSet rs;
                    try {
                        rs = stmt.executeQuery("select * from pix where pname like '%" + pnameSearchInput.getText() + "%' " +
                                "and category like '%" + categoriesCB.getSelectionModel().getSelectedItem().toString() + "%' " +
                                "and userName like '%" + usernameSearchInput.getText() + "%'");
                    } catch(Exception e){
                        System.out.println(e);
                        rs = stmt.executeQuery("select * from pix where pname like '%" + pnameSearchInput.getText() + "%' " +
                                "and userName like '%" + usernameSearchInput.getText() + "%'");
                    } finally {
                        System.out.println("Wenn ich ein finally will, bekomme ich ein finally!!!!!!!!!!!!!!1111111111einseinselfelf");
                    }
                    Image picture = placeholder;
                    while(rs.next()) {
                        //System.out.println(rs.getString(5));
                        picture = new Image(new FileInputStream(".\\Images\\" + rs.getString(5) + ".jpg"));
                        //System.out.println(".\\Images\\" + rs.getString(5) + ".jpg");
                        ImageView pictureView = new ImageView(picture);
                        vBox1.getChildren().add(pictureView);
                    }
                    con.close();
                }catch(Exception e){
                    System.out.println(e);
                }


            }
        });

        chosenPictureScrollPane.setContent(vBox1);
        chosenPictureScrollPane.setFitToWidth(true);
        chosenPictureScrollPane.setMinSize(800,650);
        chosenPictureScrollPane.setStyle("-fx-background-color: rgb(136,140,140)");
        showChosenPicturesWindowLayout.add(showChosenPicturesButton,0,2);
        showChosenPicturesWindowLayout.add(chosenPictureScrollPane,0,3);

        showChosenPicturesWindowLayout.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    showChosenPicturesButton.fire();
                }
            }
        });

        //sql_abfrage("select * from tbl_user");



        Scene showChosenPicsWindowScene = new Scene(showChosenPicturesWindowLayout);

        Stage showChosenPicsWindowStage = new Stage();

        showChosenPicsWindowStage.setTitle("Show Pics Window");
        showChosenPicsWindowStage.setScene(showChosenPicsWindowScene);
        showChosenPicsWindowStage.setX(parent.getX());
        showChosenPicsWindowStage.setY(parent.getY());
        showChosenPicsWindowStage.show();
    }

}
