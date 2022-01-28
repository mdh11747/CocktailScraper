package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator.Class;

public class App 
{
    static FileWriter cocktails;

    public static void main( String[] args ) throws IOException
    {
        Document doc = Jsoup.connect("https://www.liquor.com/cocktail-by-spirit-4779438").get();
        Elements spirits = doc.select(new Class("truncated-list__item"));
        for (Element spirit : spirits) {
            scrape(spirit.child(0).attr("href"));
        }
       //scrape("https://www.liquor.com/vodka-cocktails-4779437");
       /* cocktails.write(scrape());
        Document doc = Jsoup.connect("https://en.wikipedia.org/").get();
        System.out.println(doc.title());
        Elements sideBar = doc.select("#n-randompage");
        for (Element sideItem : sideBar) {
            System.out.println(sideItem.child(0).attr("href"));
        }
        doc = Jsoup.connect("https://en.wikipedia.org/" + sideBar.get(0).child(0).attr("href")).get();
        System.out.println(doc.location());*/
    }

    private static void scrape(String sUrl) {
        try {
            cocktails = new FileWriter("Cocktails.txt",true);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Document doc;
        try {
            doc = Jsoup.connect(sUrl).get();
            String spiritName = doc.select(new Class("heading__title")).get(0).text();
            cocktails.write("Spirit: " + spiritName + "\n");
                Elements drinks = doc.select(new Class("comp card-list__item mntl-block"));
                for (Element drink : drinks) {
                    doc = Jsoup.connect(drink.child(0).attr("href")).get();
                    String title = doc.select(new Class("heading__title")).get(0).text();
                    Elements ingredients = doc.select(new Class("simple-list__item js-checkbox-trigger ingredient text-passage"));
                    Elements instructions = doc.select("#mntl-sc-block_3-0");
                    if (ingredients.size() == 0) {
                        ingredients = doc.select(new Class("structured-ingredients__list-item"));
                    }
                    if (ingredients.size() > 0) {
                        cocktails.write("Title: " + title + "\n");
                        for (Element ingredient : ingredients) {
                            cocktails.write("Ingredients: " + ingredient.text() + "\n");
                        }
                        for (int i = 0; i < instructions.size(); i++) {

                            for (int n = 0; n < instructions.get(i).childrenSize(); n++) {
                                cocktails.write("Instructions" + (n+1) + ": " + instructions.get(i).child(n).text() + "\n");
                            }
                        }

                    }
                }

                cocktails.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" +
                                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                cocktails.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
