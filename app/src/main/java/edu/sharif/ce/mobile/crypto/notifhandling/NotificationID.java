package edu.sharif.ce.mobile.crypto.notifhandling;

/**
 * Created by Seyyed Parsa Neshaei on 2/28/21
 * All Rights Reserved
 */
public class NotificationID {
    public static class Crypto {
        public static int DATA_LOADED_FROM_CACHE = 1;
        public static int NO_INTERNET_CONNECTION = 2;
        public static int NEW_DATA_LOADED_FOR_RESTER = 3;
        public static int NEW_DATA_LOADED_FOR_UI = 4;
    }

    public static class Candle {
        public static int NEW_DATA_LOADED_FOR_UI = 5;
        public static int DATA_LOADED_FROM_CACHE = 6;
        public static int NO_INTERNET_CONNECTION = 7;
        public static int NEW_DATA_LOADED_FOR_RESTER = 8;
        public static int NO_DATA_LOADED_FOR_UI = 9;
    }
}
