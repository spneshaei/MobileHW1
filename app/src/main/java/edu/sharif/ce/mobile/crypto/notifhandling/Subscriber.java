package edu.sharif.ce.mobile.crypto.notifhandling;

import android.os.Message;

/**
 * Created by Seyyed Parsa Neshaei on 2/28/21
 * All Rights Reserved
 */
public interface Subscriber {
    void handleMessage(Message msg);
}
