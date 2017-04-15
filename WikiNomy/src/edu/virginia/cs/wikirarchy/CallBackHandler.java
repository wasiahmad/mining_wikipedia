/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.virginia.cs.wikirarchy;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wasi
 */
public class CallBackHandler implements PageCallbackHandler {

    private FileWriter writer;
    private FileWriter writer2;
    private int totalPages;
    private int totalCategoryPage;
    private int totalRedirectedPage;
    private int totalArticle;

    public CallBackHandler() {
        totalPages = 0;
        totalCategoryPage = 0;
        totalRedirectedPage = 0;
        totalArticle = 0;
        try {
            writer = new FileWriter("enwiki-latest-categories.xml");
            writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n");
            writer.write("<category-pages>" + "\n");
            writer.flush();

            writer2 = new FileWriter("enwiki-latest-articles.xml");
            writer2.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n");
            writer2.write("<article-pages>" + "\n");
            writer2.flush();
        } catch (IOException ex) {
            Logger.getLogger(CallBackHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method to remove xml special characters.
     *
     * @param str xml attribute value or value in between tag.
     * @return string with special characters removed.
     */
    private String HandleSpecialCharacters(String str) {
        String result;
        result = str.replaceAll("&", "&amp;");
        result = result.replaceAll("<", "&lt;");
        result = result.replaceAll(">", "&gt;");
        result = result.replaceAll("\"", "&quot;");
        result = result.replaceAll("'", "&apos;");
        return result;
    }

    /**
     * Method to finish writing and close the file writer.
     */
    public void finish() {
        try {
            System.out.println("Total pages = " + totalPages);
            System.out.println("Total category pages = " + totalCategoryPage);
            System.out.println("Total redirected pages = " + totalRedirectedPage);
            System.out.println("Total articles = " + totalArticle);

            writer.write("</category-pages>" + "\n");
            writer.flush();
            writer.close();

            writer2.write("</article-pages>" + "\n");
            writer2.flush();
            writer2.close();
        } catch (IOException ex) {
            Logger.getLogger(CallBackHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The callback method which gets called for every Wikipedia page.
     *
     * @param page Wikipedia page
     */
    @Override
    public void process(WikiPage page) {
        if (page.getRedirectPage() != null) {
            // avoiding the redirected pages
            totalRedirectedPage++;
            return;
        }
        totalPages++;
        try {
            String title = page.getTitle().trim();
            if (title.startsWith("Category:")) {
                totalCategoryPage++;
                title = title.replace("Category:", "");
                writer.write("\t" + "<page id=\"" + page.getID() + "\" title=\"" + HandleSpecialCharacters(title) + "\">" + "\n");
                String categories = page.getCategories().toString();
                writer.write("\t\t" + "<categories>" + HandleSpecialCharacters(categories.substring(1, categories.length() - 1)) + "</categories>" + "\n");
                writer.write("\t" + "</page>" + "\n");
                writer.flush();
            } else if (!page.isSpecialPage() && !page.isDisambiguationPage()) {
                totalArticle++;
                title = title.replace("Category:", "");
                writer2.write("\t" + "<page id=\"" + page.getID() + "\" title=\"" + HandleSpecialCharacters(title) + "\">" + "\n");
                String categories = page.getCategories().toString();
                writer2.write("\t\t" + "<categories>" + HandleSpecialCharacters(categories.substring(1, categories.length() - 1)) + "</categories>" + "\n");
                writer2.write("\t" + "</page>" + "\n");
                writer2.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(CallBackHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (totalPages % 100000 == 0) {
            System.out.println(totalPages + " pages completed..");
        }
    }

}
