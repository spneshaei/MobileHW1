package edu.sharif.ce.mobile.crypto.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Seyyed Parsa Neshaei on 2/28/21
 * All Rights Reserved
 */
public class Rester {
    private static final Rester ourInstance = new Rester();
    private ThreadPoolExecutor executor;
    static Rester getInstance() {
        return ourInstance;
    }

    private Rester() {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    }
}
