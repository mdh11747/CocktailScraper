package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
        FileWriter cocktails = new FileWriter(new File("file://Cocktails.txt"));
        cocktails.append("Hello");
        Document doc = Jsoup.connect("https://en.wikipedia.org/").get();
        System.out.println(doc.title());
        Elements sideBar = doc.select("#n-randompage");
        for (Element sideItem : sideBar) {
            System.out.println(sideItem.child(0).attr("href"));
        }
        doc = Jsoup.connect("https://en.wikipedia.org/" + sideBar.get(0).child(0).attr("href")).get();
        System.out.println(doc.location());
    }
}
