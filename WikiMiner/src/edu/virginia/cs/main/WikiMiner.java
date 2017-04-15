/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.virginia.cs.main;

/**
 *
 * @author Wasi
 */
public class WikiMiner {

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            createThreads(Integer.parseInt(args[0]));
        } else {
            System.out.println("Arugment is not provided correctly...");
        }
    }

    /**
     * The main method that creates and starts threads.
     *
     * @param count number of threads need to be created and started.
     * @return
     */
    private static void createThreads(int count) throws InterruptedException {
        ParseWikiPage[] pwp = new ParseWikiPage[count];
        CallBackHandler handler = new CallBackHandler();
        for (int i = 0; i < count; i++) {
            pwp[i] = new ParseWikiPage("../enwiki-latest-pages-articles.xml.bz2", handler);
            pwp[i].start();
        }
        for (int i = 0; i < count; i++) {
            pwp[i].getThread().join();
        }
        handler.finish(); // all processing completed, now finish
    }

}
