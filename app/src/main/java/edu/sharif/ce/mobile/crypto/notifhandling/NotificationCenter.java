package edu.sharif.ce.mobile.crypto.notifhandling;

import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Seyyed Parsa Neshaei on 2/28/21
 * All Rights Reserved
 */
public class NotificationCenter {
    private static HashMap<Integer, ArrayList<Subscriber>> subscribers = new HashMap<>();

    public static void registerForNotification(Subscriber subscriber, int id) {
        if (!subscribers.containsKey(id) || subscribers.get(id) == null) {
            ArrayList<Subscriber> newList = new ArrayList<>();
            newList.add(subscriber);
            subscribers.put(id, newList);
            return;
        }
        if (!Objects.requireNonNull(subscribers.get(id)).contains(subscriber)) {
            Objects.requireNonNull(subscribers.get(id)).add(subscriber);
        }
    }

    public static void notify(int id) {
        if (!subscribers.containsKey(id)) return;
        ArrayList<Subscriber> arrayList = subscribers.get(id);
        if (arrayList == null) return;
        for (Subscriber subscriber : arrayList) {
            subscriber.sendEmptyMessage(id);
        }
    }
}
