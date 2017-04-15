/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.virginia.cs.main;

import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wasi
 */
public class ParseWikiPage implements Runnable {

    private WikiXMLParser wxsp = null;
    private CallBackHandler handler = null;
    private Thread t = null;

    public ParseWikiPage(String dump, CallBackHandler handler) {
        wxsp = WikiXMLParserFactory.getSAXParser(dump);
        this.handler = handler;
    }

    /**
     * Overriding the run method of the Thread class.
     */
    @Override
    public void run() {
        try {
            if (wxsp != null && handler != null) {
                wxsp.setPageCallback(handler);
                wxsp.parse();
            } else {
                System.err.println("Null value of parser and callback handler");
            }
        } catch (Exception e) {
            Logger.getLogger(ParseWikiPage.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Method to start the thread.
     */
    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
    
    /**
     * Method to return the thread object.
     * @return thread object
     */
    public Thread getThread() {
        return t;
    }
}
