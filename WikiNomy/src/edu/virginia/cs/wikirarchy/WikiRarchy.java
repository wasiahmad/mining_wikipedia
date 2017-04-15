/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.virginia.cs.wikirarchy;

import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wasi
 */
public class WikiRarchy {

    public static void main(String[] args) throws Exception {
        CallBackHandler handler = new CallBackHandler();
        WikiXMLParser wxsp;
        try {
            wxsp = WikiXMLParserFactory.getSAXParser("../enwiki-latest-pages-articles.xml.bz2");
            wxsp.setPageCallback(handler);
            wxsp.parse();
        } catch (Exception ex) {
            Logger.getLogger(WikiRarchy.class.getName()).log(Level.SEVERE, null, ex);
        }
        handler.finish();
    }
}
