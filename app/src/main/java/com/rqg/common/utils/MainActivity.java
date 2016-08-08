package com.rqg.common.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.rqg.common.view.SideView;
import com.rqg.common.view.SnackView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SideView sideView = (SideView) findViewById(R.id.refresh_content);

        if (sideView == null)
            return;

        sideView.setListener(new SideView.OnSideViewClick() {
            @Override
            public void onSideClick(SideView sideView, boolean isLeft) {

            }
        });

        sideView.checkOneSide(false);
    }

    public void Hello(View view) {
        SnackView snackView = new SnackView(View.inflate(view.getContext(), R.layout.snack_view, null), (ViewGroup) findViewById(R.id.root_view));
        snackView.show();

    }
}
