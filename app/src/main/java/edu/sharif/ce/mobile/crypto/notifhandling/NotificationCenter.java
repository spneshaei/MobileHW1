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
    private static HashMap<Integer, ArrayList<Handler>> subscribers = new HashMap<>();

    public static void registerForNotification(Handler handler, int id) {
        if (!subscribers.containsKey(id) || subscribers.get(id) == null) {
            ArrayList<Handler> newList = new ArrayList<>();
            newList.add(handler);
            subscribers.put(id, newList);
            return;
        }
        Objects.requireNonNull(subscribers.get(id)).add(handler);
    }

    public static void notify(int id) {
        if (!subscribers.containsKey(id)) return;
        ArrayList<Handler> arrayList = subscribers.get(id);
        if (arrayList == null) return;
        for (Handler handler : arrayList) {
            handler.sendEmptyMessage(id);
        }
    }
}
