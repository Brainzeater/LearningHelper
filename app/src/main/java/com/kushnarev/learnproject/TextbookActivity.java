package com.kushnarev.learnproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class TextbookActivity extends AppCompatActivity{
    TextView mainPage;
    TextView whatis;
    TextView pomodoro;
    TextView chunk;
    TextView howTo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textbook);
        mainPage = findViewById(R.id.main_page);
        whatis = findViewById(R.id.what_is);
        pomodoro = findViewById(R.id.pomodoro);
        chunk = findViewById(R.id.chunk);
        howTo = findViewById(R.id.how_to);



    }

    public void openMainPage(View view) {

        Intent intent = new Intent(TextbookActivity.this, ContentActivity.class);
        intent.putExtra("main", 0);
        startActivity(intent);
    }

    public void openWhat(View v){
        Intent intent = new Intent(TextbookActivity.this, ContentActivity.class);
        intent.putExtra("main", 1);
        startActivity(intent);
    }
    public void openPomodoro(View view) {

        Intent intent = new Intent(TextbookActivity.this, ContentActivity.class);
        intent.putExtra("main", 2);
        startActivity(intent);
    }
    public void openChunk(View view) {

        Intent intent = new Intent(TextbookActivity.this, ContentActivity.class);
        intent.putExtra("main", 3);
        startActivity(intent);
    }
    public void openHowToFormChunk(View view) {

        Intent intent = new Intent(TextbookActivity.this, ContentActivity.class);
        intent.putExtra("main", 4);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.textbook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_main) {

            Intent intent = new Intent(TextbookActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
