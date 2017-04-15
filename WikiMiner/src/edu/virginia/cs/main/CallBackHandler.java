/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.virginia.cs.main;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Wasi
 */
public class CallBackHandler implements PageCallbackHandler {

    private final Pattern pattern = Pattern.compile("\\[\\[.*?\\]\\]");
    private final Pattern xmlTag = Pattern.compile("<!--.*?-->");
    private final Pattern tagFormat = Pattern.compile("<!--|-->");
    private final MaxentTagger tagger;
    private FileWriter titleWriter;
    private FileWriter nounWriter;
    private final Object lock1;
    private final Object lock2;
    private final HashMap<String, Integer> mapTitle;

    public CallBackHandler() {
        tagger = new MaxentTagger("tagger/english-bidirectional-distsim.tagger");
        try {
            titleWriter = new FileWriter("output/wiki_page_titles.txt", true);
            nounWriter = new FileWriter("output/common_nouns.xml", true);
            nounWriter.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n");
            nounWriter.write("<nouns>" + "\n");
            nounWriter.flush();

        } catch (IOException ex) {
            Logger.getLogger(CallBackHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        mapTitle = new HashMap<>();
        loadTitlesInMemory();
        lock1 = new Object();
        lock2 = new Object();
    }

    /**
     * Method to finish writing and close the file writer.
     */
    public void finish() {
        try {
            System.out.println("Total " + mapTitle.size() + " wikipedia pages processed..");
            nounWriter.write("</nouns>" + "\n");
            nounWriter.flush();
            nounWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(CallBackHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method to load all the wiki-titles which are processed.
     */
    private void loadTitlesInMemory() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("output/wiki_page_titles.txt")));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                count++;
                mapTitle.put(line, count);
            }
            System.out.println(mapTitle.size() + " documents title loaded...");
        } catch (Exception ex) {
            Logger.getLogger(CallBackHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method to remove xml special characters.
     * @param str xml attribute value or value in between tag.
     * @return string with special characters removed.
     */
    private String removeSpecialCharacters(String str) {
        String result;
        result = str.replaceAll("[{}]", "");
        Matcher temp = xmlTag.matcher(result);
        if (temp.find()) {
            result = result.replaceAll(temp.group(0), "");
        }
        temp = tagFormat.matcher(result);
        if (temp.find()) {
            result = result.replaceAll("<!--|-->", "");
        }
        result = result.replaceAll("&", "&amp;");
        result = result.replaceAll("<", "&lt;");
        result = result.replaceAll(">", "&gt;");
        result = result.replaceAll("\"", "&quot;");
        result = result.replaceAll("'", "&apos;");
        return result;
    }

    /**
     * The part-of-speech Tag function. uses stanford POS Tagger.
     *
     * @param articleTag wikipedia article tag.
     * @param mention
     * @return true if any of the parameter contains noun(singular or plural).
     * @return false if any of the parameter does not contain noun(singular or
     * plural).
     */
    private boolean POSTag(String articleTag, String mention) throws IOException {
        if (!mention.isEmpty()) {
            String tagged = tagger.tagString(mention);
            if (tagged.contains("_NN") || tagged.contains("_NNS")) {
                return true;
            } else {
                String tag = tagger.tagString(articleTag);
                if (tag.contains("_NN") || tag.contains("_NNS")) {
                    return true;
                }
            }
        } else {
            String tagged = tagger.tagString(articleTag);
            if (tagged.contains("_NN") || tagged.contains("_NNS")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extracts mentions (only nouns) from wikipedia page content.
     *
     * @param pageContent wikipedia page content.
     * @return a list of mention and wikipedia article tag pair.
     */
    private HashMap<String, String> ExtractMentions(String pageContent) throws IOException {
        HashMap<String, String> retVal = new HashMap<>();
        Matcher matcher = pattern.matcher(pageContent);
        while (matcher.find()) {
            String match = matcher.group(0).replaceAll("\\[|\\]", "");
            String[] split = match.split("\\|");
            if (split.length == 2) {
                String url = removeSpecialCharacters(split[0].trim());
                String text = removeSpecialCharacters(split[1].trim());
                if (!url.isEmpty() && !text.isEmpty()) {
                    if (POSTag(url, text)) {
                        url = url.replaceAll("\\s+", "_");
                        retVal.put(text, url);
                    }
                }
            } else if (split.length == 1) {
                String url = removeSpecialCharacters(split[0].trim());
                if (!url.isEmpty()) {
                    if (POSTag(url, "")) {
                        String text = url;
                        url = url.replaceAll("\\s+", "_");
                        retVal.put(text, url);
                    }
                }
            }
        }
        return retVal;
    }

    /**
     * This method which gets called for a wiki page to check whether the page
     * is processed or not.
     *
     * @param pageTitle wikipedia page title
     * @return -1 if the current page is already processed
     * @return number of pages already processed if the current page is not
     * processed yet
     */
    private int checkPage(String pageTitle) {
        int count = 1;
        synchronized (lock1) { // synchronization for multi-threading
            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File("output/wiki_page_titles.txt")));
                String str;
                while ((str = reader.readLine()) != null) {
                    count++;
                    if (str.equals(pageTitle)) {
                        count = -1;
                        break;
                    }
                }
                if (count != -1) {
                    titleWriter = new FileWriter("output/wiki_page_titles.txt", true);
                    titleWriter.write(pageTitle + "\n");
                    titleWriter.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(CallBackHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return count;
    }

    /**
     * This method which gets called for a wiki page to check whether the page
     * is processed or not.
     *
     * @param pageTitle wikipedia page title
     * @return false if the current page is already processed, true otherwise
     */
    private boolean checkPageFromMemory(String title) throws IOException {
        synchronized (lock1) {
            if (mapTitle.containsKey(title)) {
                return false;
            } else {
                mapTitle.put(title, mapTitle.size() + 1);
                titleWriter.write(title + "\n");
                titleWriter.flush();
                return true;
            }
        }
    }

    /**
     * The callback method which gets called for every wikipedia page.
     *
     * @param page wikipedia page
     */
    @Override
    public void process(WikiPage page) {
        String title;
        try {
            title = page.getTitle().trim();
            if (page.getRedirectPage() == null) {
                if (checkPageFromMemory(title)) {
                    HashMap<String, String> retVal = ExtractMentions(page.getWikiText());
                    synchronized (lock2) { // synchronization for multi-threading
                        int articleId = mapTitle.get(title);
                        nounWriter.write("\t" + "<article id=\"" + articleId + "\" title=\"" + removeSpecialCharacters(title) + "\">" + "\n");
                        nounWriter.flush();
                        int nounCount = 0;
                        for (Map.Entry<String, String> pair : retVal.entrySet()) {
                            int tag = 0;
                            nounCount++;
                            if (!pair.getKey().matches(".*[A-Z].*")) {
                                tag = 1;
                            }
                            nounWriter.write("\t\t" + "<row id=\"" + nounCount + "\" tag=\"" + tag + "\">" + "\n");
                            nounWriter.write("\t\t\t" + "<mention>" + pair.getKey() + "</mention>" + "\n");
                            nounWriter.write("\t\t\t" + "<pageTitle>" + pair.getValue() + "</pageTitle>" + "\n");
                            nounWriter.write("\t\t" + "</row>" + "\n");
                            nounWriter.flush();
                        }
                        nounWriter.write("\t" + "</article>" + "\n");
                        nounWriter.flush();
                        if (articleId % 10000 == 0) {
                            System.out.println(articleId + " pages completed...");
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CallBackHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
