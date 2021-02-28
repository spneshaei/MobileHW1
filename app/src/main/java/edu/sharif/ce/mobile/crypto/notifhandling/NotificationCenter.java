package edu.sharif.ce.mobile.crypto.notifhandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Seyyed Parsa Neshaei on 2/28/21
 * All Rights Reserved
 */
public class NotificationCenter {
    private static HashMap<Integer, ArrayList<Subscriber>> subscribers;

    public static void registerForNotification(Subscriber subscriber, int id) {
        if (!subscribers.containsKey(id) || subscribers.get(id) == null) {
            ArrayList<Subscriber> newList = new ArrayList<>();
            newList.add(subscriber);
            subscribers.put(id, newList);
            return;
        }
        Objects.requireNonNull(subscribers.get(id)).add(subscriber);
    }
}
