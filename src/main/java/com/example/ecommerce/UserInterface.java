package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.security.auth.callback.LanguageCallback;

public class UserInterface {

    GridPane loginPage;
    HBox headerBar;
    Button signInButton;
    Label welcomeLabel;
    VBox body;
    HBox footerBar;
    Customer loggedInCustomer;
    ProductList productList = new ProductList();
    VBox productPage;

    Button placeOrderButton = new Button("Place Order");
    ObservableList<Product> itemsInCart = FXCollections.observableArrayList();

    public BorderPane createContent(){
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);
        //root.getChildren().add(loginPage);
        root.setTop(headerBar);
        body = new VBox();
        body.setPadding(new Insets(10));
        body.setAlignment(Pos.CENTER);
        root.setBottom(footerBar);
        root.setCenter(body);
        productPage = productList.getAllProducts();
        body.getChildren().add(productPage);
        //root.setCenter(loginPage);

        return root;
    }

    public UserInterface(){
        createLoginPage();
        createHeaderBar();
        createFooter();
    }

    private void createLoginPage(){
        Text userNameText = new Text("User Name");
        Text passwordText = new Text("Password");


        TextField userName = new TextField("soumya@gmail.com");
        userName.setPromptText("Type your user name here");
        PasswordField password = new PasswordField();
        password.setText("soumya@1999");
        password.setPromptText("Type your password here");
        Label messageLabel = new Label("Hi");

        Button loginButton = new Button("Login");

        loginPage = new GridPane();
        //loginPage.setStyle("-fx-background-color: #2f4f4f;");
        loginPage.setAlignment(Pos.CENTER);
        loginPage.setHgap(10);
        loginPage.setVgap(10);
        loginPage.add(userNameText, 0, 0);
        loginPage.add(userName, 1, 0);
        loginPage.add(passwordText, 0, 1);
        loginPage.add(password, 1, 1);
        loginPage.add(messageLabel, 0, 2);
        loginPage.add(loginButton, 1, 2);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name = userName.getText();
                //messageLabel.setText(name);
                String pass = password.getText();
                Login login = new Login();
                loggedInCustomer = login.customerLogin(name, pass);

                if (loggedInCustomer != null){
                    messageLabel.setText("Welcome : " + loggedInCustomer.getName() );
                    welcomeLabel.setText("Welcome : " + loggedInCustomer.getName());
                    headerBar.getChildren().add(welcomeLabel);
                    body.getChildren().clear();
                    body.getChildren().add(productPage);
                }
                else {
                    messageLabel.setText("LogIn Failed please provide current credentials");
                }

            }
        });

    }

    private void createHeaderBar(){
        Button homeButton = new Button();
        Image image = new Image("C:\\Users\\soumy\\IdeaProjects\\ECommerce\\src\\img.png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(80);
        homeButton.setGraphic(imageView);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search here");
        searchBar.setPrefWidth(280);

        Button searchButton = new Button("Search");
        signInButton = new Button("Sign In");
        welcomeLabel = new Label();

        Button cartListButton = new Button(("Cart"));

        Button orderButton = new Button("Orders");


        headerBar = new HBox();
        //headerBar.setStyle("-fx-background-color: #2f4f4f;");
        headerBar.setPadding(new Insets(10));
        headerBar.setSpacing(10);
        headerBar.setAlignment(Pos.CENTER);
        headerBar.getChildren().addAll(homeButton, searchBar, searchButton,signInButton, cartListButton, orderButton);


        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();  //remove everything
                body.getChildren().add(loginPage); //put login page
                headerBar.getChildren().remove(signInButton);
            }
        });

        cartListButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                VBox prodPage = productList.getProductsInCart(itemsInCart);
                prodPage.setAlignment(Pos.CENTER);
                prodPage.setSpacing(10);
                prodPage.getChildren().add(placeOrderButton);
                body.getChildren().add(prodPage);
                footerBar.setVisible(false);
            }
        });

        placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Need list of product and a customer
                if (itemsInCart == null){
                    showDialog("Please add some products in the cart to place order!");
                    return;
                }
                if (loggedInCustomer == null){
                    showDialog("Please login first to place order!");
                    return;
                }
                int count = Order.placeMultipleOrder(loggedInCustomer, itemsInCart);
                if (count != 0){
                    showDialog("Order for "+count+" products place successfull");
                }
                else {
                    showDialog("Order failed!!");
                }
            }
        });

        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(productPage);
                footerBar.setVisible(true);
                if (loggedInCustomer == null && headerBar.getChildren().indexOf(signInButton) == -1){
                    headerBar.getChildren().add(signInButton);
                }
            }
        });
    }


    private void createFooter(){
        Button buyNowButton = new Button("BuyNow");
        Button Addtocart = new Button("Add to cart");

        footerBar = new HBox();
        footerBar.setAlignment(Pos.CENTER);
        footerBar.setSpacing(10);
        footerBar.setPadding(new Insets(10));
        footerBar.getChildren().addAll(buyNowButton, Addtocart);


        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                if (product == null){
                    showDialog("Please select a product to place order!");
                    return;
                }
                if (loggedInCustomer == null){
                    showDialog("Please login first to place order!");
                    return;
                }
                boolean status = Order.placeOrder(loggedInCustomer, product);
                if (status == true){
                    showDialog("Order place successfull");
                }
                else {
                    showDialog("Order failed!!");
                }
            }
        });
        Addtocart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                if (product == null){
                    showDialog("Please select a product first to add it to Cart!");
                    return;
                }
                itemsInCart.add(product);
                showDialog("Selected item added to the cart successfully!");
            }
        });
    }

    private void showDialog(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setTitle("message");
        alert.showAndWait();
    }
}