package com.noahsyn.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    EditText udItem;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        udItem = findViewById(R.id.udItem);
        btnUpdate = findViewById(R.id.btnUpdate);

        getSupportActionBar().setTitle("Edit Item");

        udItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // When user is done editing, they click save button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent containing results
                Intent intent = new Intent();

                // Pass results
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, udItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                // Set result of the intent
                setResult(RESULT_OK, intent);

                // Finish the activity, close screen and go back
                finish();
            }
        });
    }
}