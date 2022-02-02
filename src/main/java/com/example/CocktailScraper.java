package com.example;

import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator.Class;

public class CocktailScraper 
{
    private FileWriter cocktails;
    private int drinkNumber;
    private int spiritNumber = 1;

    public static void main( String[] args ) throws IOException
    {
        CocktailScraper scraper = new CocktailScraper();
        Document doc = Jsoup.connect("https://www.liquor.com/cocktail-by-spirit-4779438").get();
        Elements spirits = doc.select(new Class("truncated-list__item"));
        for (Element spirit : spirits) {
            scraper.scrape(spirit.child(0).attr("href"));
        }
    }

    public void scrape(String sUrl) {
        try {
            cocktails = new FileWriter("Cocktails.txt",true);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        drinkNumber = 1;
        Document doc;
        try {
            doc = Jsoup.connect(sUrl).get();
            String spiritName = doc.select(new Class("heading__title")).get(0).text();
            cocktails.write("Spirit" + (spiritNumber++) + ": " + spiritName + "\n");
                Elements drinks = doc.select(new Class("comp card-list__item mntl-block"));
                for (Element drink : drinks) {
                    doc = Jsoup.connect(drink.child(0).attr("href")).get();
                    String title = doc.select(new Class("heading__title")).get(0).text();
                    Elements ingredients = doc.select(new Class("simple-list__item js-checkbox-trigger ingredient text-passage"));
                    Elements instructions = doc.select("#mntl-sc-block_3-0");
                    Element img = doc.select("img[src]").first();
                    if (ingredients.size() == 0) {
                        ingredients = doc.select(new Class("structured-ingredients__list-item"));
                    }
                    if (ingredients.size() > 0 && !instructions.get(0).child(0).text().contains("Serves")) {
                        cocktails.write("Title" + (drinkNumber++) + ": " + title + "\n");
                        for (int i = 0; i < ingredients.size(); i++) {
                            cocktails.write("Ingredients" + (i+1) + ": " + ingredients.get(i).text() + "\n");
                        }
                        for (int i = 0; i < instructions.size(); i++) {

                            for (int n = 0; n < instructions.get(i).childrenSize(); n++) {
                                cocktails.write("Instructions" + (n+1) + ": " + instructions.get(i).child(n).text() + "\n");
                            }
                        }
                        if (img.absUrl("src").contains(title.toLowerCase().substring(0,3))) {
                            cocktails.write("Image: " + img.absUrl("src") + "\n");
                        } else {
                            cocktails.write("Image: " + doc.select("img[src]").get(1).absUrl("src") + "\n");
                        }

                    }
                }

                cocktails.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" +
                                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
                cocktails.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}