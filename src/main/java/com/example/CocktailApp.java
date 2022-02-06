package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
        try {
            vbox.setBackground(new Background(new BackgroundImage(new Image(new FileInputStream(new File("WoodBackground.png"))),
                                BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ingredients = new TextField();
        searchIngredientsButton = new Button("Search by Ingredients");
        searchIngredientsButton.setOnAction(e -> searchByIngredients());
        randomDrinkButton = new Button("Random Drink");
        randomDrinkButton.setOnAction(e -> randomDrink());
        ingredientSearch = new HBox(ingredients);
        HBox.setHgrow(ingredients, Priority.ALWAYS);

        cocktailImage = new ImageView();
        cocktailImage.setPreserveRatio(true);
        cocktailImage.setFitWidth(700);
        cocktailName = new Text();
        cocktailName.setFont(new Font(40));
        ingredientsList = new Label();
        ingredientsList.setTextFill(Color.SKYBLUE);
        ingredientsList.setWrapText(true);
        ingredientsList.setMinWidth(200);
        ingredientsList.setMaxWidth(200);
        ingredientsList.setPadding(new Insets(10));
        instructions = new Label();
        instructions.setWrapText(true);
        instructions.setMinWidth(200);
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

    private void searchByIngredients() {
        HashSet<String> possibleDrinks = new HashSet<>();
        HashSet<String> validDrinks = new HashSet<>();
        try {
            File cocktailsFile = new File("Cocktails.txt");
            Scanner cocktails;
            String nextLine, title = "", ingredient;
            String ingredientsText = ingredients.getText() + ",";
            while (ingredientsText.length() > 0 && !Character.isLetterOrDigit((int)ingredientsText.charAt(0))) {
                ingredientsText = ingredientsText.substring(1);
            } if (ingredientsText.length() > 0) {
                cocktails = new Scanner(cocktailsFile,"UTF-8");
                title = cocktails.nextLine();
                while (cocktails.hasNextLine()) {
                    nextLine = cocktails.nextLine();
                    if (nextLine.contains("Title")) {
                        title = nextLine;
                    }
                    ingredient = ingredientsText.toLowerCase().substring(0,ingredientsText.indexOf(","));
                    if (nextLine.toLowerCase().contains(ingredient)) {
                        possibleDrinks.add(title);
                    }
                }
                ingredient = ingredients.getText().toLowerCase().substring(ingredients.getText().indexOf(",") + 1);
                cocktails.close();
            }
            while (ingredientsText.length() > 0) {
                if (!Character.isLetterOrDigit((int)ingredientsText.charAt(0))) {
                    ingredientsText = ingredientsText.substring(1);
                } else {
                    validDrinks = new HashSet<>();
                    cocktails = new Scanner(cocktailsFile,"UTF-8");
                    title = cocktails.nextLine();
                    while (cocktails.hasNextLine()) {
                        nextLine = cocktails.nextLine();
                        if (nextLine.contains("Title")) {
                            title = nextLine;
                        }
                        ingredient = ingredientsText.toLowerCase().substring(0,ingredientsText.indexOf(","));
                        if (nextLine.toLowerCase().contains(ingredient)) {
                            boolean valid = possibleDrinks.add(title);
                            if (!valid) {
                                validDrinks.add(title);
                            } else {
                                possibleDrinks.remove(title);
                            }
                        }
                    }
                    ingredientsText = ingredientsText.toLowerCase().substring(ingredientsText.indexOf(",") + 1);
                    cocktails.close();
                    possibleDrinks = validDrinks;
                }
            }
        
        int drinkNumber = (int)(Math.random() * possibleDrinks.size());
        String drink = (String)(possibleDrinks.toArray()[drinkNumber]);
            File cocktailsText = new File("Cocktails.txt");
            cocktails = new Scanner(cocktailsText,"UTF-8");
            while (cocktails.hasNextLine()) {
                nextLine = cocktails.nextLine();
                if (nextLine.contains(drink)) {
                    break;
                }
            }  
            drink = drink.substring(drink.indexOf(" ") + 1);
            System.out.println(drink);
            cocktailName.setText(drink);
            updateInfo(cocktails);
            ingredientsList.setText(ingredientsList.getText() + "1 shot of Joey's cum");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void searchByTitle() {
        String title = ingredients.getText();
    }

    private void randomDrink() {
        int spiritNumber = (int)(Math.random() * 10 + 1);
        try{
            Scanner cocktails = new Scanner(new File("Cocktails.txt"),"UTF-8");
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
            cocktails = new Scanner(new File("Cocktails.txt"),"UTF-8");
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
            updateInfo(cocktails);
            ingredientsList.setText(ingredientsList.getText() + "1 shot of Joey's cum");
            System.out.println("Random Drink Produced");
        } catch (FileNotFoundException fnfe) {
            System.err.println("File Not Found");
        }
    }    

    private void updateInfo(Scanner input) {
        ingredientsList.setText("");
        instructions.setText("");
        while (input.hasNextLine()) {
            String nextLine = input.nextLine();
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
        input.close();
    }

}