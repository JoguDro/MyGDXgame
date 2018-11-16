package hr.algebra.mygdx.game;

import android.app.Activity;
import android.widget.Toast;

public class Methods implements MyMethods {

    private final Activity context;

    public Methods(Activity context) {
        this.context = context;
    }

    @Override
    public void showToast() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Vrijeme je Isteklo!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void finish() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });

    }


}
