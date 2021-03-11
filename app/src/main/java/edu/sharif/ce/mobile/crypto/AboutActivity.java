package edu.sharif.ce.mobile.crypto;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AnimationSet set = new AnimationSet(true);
        set.setFillAfter(true);

        Animation a = new RotateAnimation(-135.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        a.setRepeatCount(0);
        a.setDuration(1000);
        set.addAnimation(a);

        Animation b = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 100.0f);
        b.setRepeatCount(0);
        b.setDuration(1000);
        set.addAnimation(b);
        ImageView icon = findViewById(R.id.app_icon);
        icon.startAnimation(set);
    }
}