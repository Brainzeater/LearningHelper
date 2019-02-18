package com.kushnarev.learnproject;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kushnarev.learnproject.utilities.TextbookUtils;

public class ContentActivity extends AppCompatActivity {

    String url;
    TextView tvContent;
    Button bGo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        tvContent = findViewById(R.id.tv_content);
        bGo = findViewById(R.id.b_go);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("main")) {
            int id = intent.getIntExtra("main", -1);
            url = TextbookUtils.webpages[id];
            tvContent.setText(TextbookUtils.data[id]);
        }

    }

    public void goToMain(View view) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        /* Verify that this intent can be launched and then call start Activity */
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
