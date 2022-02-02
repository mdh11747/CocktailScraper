package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class CocktailApp extends Application {

    VBox vbox;
    TextField ingredients;
    Button searchIngredientsButton;
    Button randomDrinkButton;
    HBox ingredientSearch;
    HBox cocktailInfo;
    ImageView cocktailImage;
    Text cocktailName;
    Label ingredientsList;
    Label instructions;
    VBox searchOptions;
    CheckBox alcoholic;

    @Override
    public void init() {
        vbox = new VBox();
        ingredients = new TextField();
        searchIngredientsButton = new Button("Search by Ingredients");
        randomDrinkButton = new Button("Random Drink");
        randomDrinkButton.setOnAction(e -> randomDrink());
        ingredientSearch = new HBox(ingredients);
        HBox.setHgrow(ingredients, Priority.ALWAYS);

        cocktailImage = new ImageView();
        cocktailName = new Text();
        cocktailName.setFont(new Font(40));
        ingredientsList = new Label();
        ingredientsList.setMinWidth(200);
        ingredientsList.setPadding(new Insets(10));
        instructions = new Label();
        instructions.setWrapText(true);
        instructions.setMaxWidth(200);
        instructions.setPadding(new Insets(10));

        randomDrinkButton.setMaxWidth(Double.MAX_VALUE);
        searchOptions = new VBox(searchIngredientsButton,randomDrinkButton);
        ingredientSearch.getChildren().add(searchOptions);

        cocktailInfo = new HBox(cocktailImage,ingredientsList,instructions);
        vbox.getChildren().addAll(ingredientSearch,cocktailName,cocktailInfo);
    }

    @Override
    public void start(Stage primaryStage) {

        Scene scene = new Scene(vbox,1150,1002);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gimme Drink");
        primaryStage.show();
    }

    
    private void randomDrink() {
        int spiritNumber = (int)(Math.random() * 10 + 1);
        try{
            Scanner cocktails = new Scanner(new File("Cocktails.txt"));
            while (cocktails.hasNextLine()) {
                String nextLine = cocktails.nextLine();
                boolean next = nextLine.contains("Spirit" + spiritNumber);
                if (next) {
                    break;
                }
            }
            int numberOfDrinks = 0;
            while (cocktails.hasNextLine()) {
                String nextLine = cocktails.nextLine();
                if (nextLine.contains("~")) {
                    break;
                }
                boolean next = nextLine.contains("Title");
                if (next) {
                    numberOfDrinks++;
                }
            }
            int drinkNumber = (int)(Math.random() * numberOfDrinks + 1);
            cocktails = new Scanner(new File("Cocktails.txt"));
            while (cocktails.hasNextLine()) {
                String nextLine = cocktails.nextLine();
                boolean next = nextLine.contains("Spirit" + spiritNumber);
                if (next) {
                    break;
                }
            }
            while (cocktails.hasNextLine()) {
                String nextLine = cocktails.nextLine();
                boolean next = nextLine.contains("Title" + drinkNumber);
                if (next) {
                    cocktailName.setText(nextLine.substring(nextLine.indexOf(":") + 2));
                    break;
                }
            }
            ingredientsList.setText("");
            instructions.setText("");
            while (cocktails.hasNextLine()) {
                String nextLine = cocktails.nextLine();
                if (nextLine.contains("Ingredient")) {
                    ingredientsList.setText(ingredientsList.getText() + nextLine.substring(nextLine.indexOf(" ") + 1) + "\n");
                }
                if (nextLine.contains("Instruction")) {
                    instructions.setText(instructions.getText() + nextLine.substring(nextLine.indexOf(" ") + 1) + "\n");
                }
                if (nextLine.contains("Image")) {
                    cocktailImage.setImage(new Image(nextLine.substring(nextLine.indexOf(" ") + 1)));
                }
                if (nextLine.contains("Title") || nextLine.contains("~")) {
                    break;
                }
            }
            cocktails.close();
            System.out.println("Done");
        } catch (FileNotFoundException fnfe) {
            System.err.println("File Not Found");
        }
    }

    

}
