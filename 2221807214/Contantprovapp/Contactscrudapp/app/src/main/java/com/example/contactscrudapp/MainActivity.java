package com.example.contactscrudapp;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText nameInput, phoneInput;
    Button addBtn, readBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        addBtn = findViewById(R.id.addBtn);
        readBtn = findViewById(R.id.readBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();
                String phone = phoneInput.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter name and phone", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues values = new ContentValues();
                values.put(ContactsProvider.NAME, name);
                values.put(ContactsProvider.PHONE, phone);

                getContentResolver().insert(ContactsProvider.CONTENT_URI, values);
                Toast.makeText(MainActivity.this, "Contact added", Toast.LENGTH_SHORT).show();

                // Clear inputs
                nameInput.setText("");
                phoneInput.setText("");
            }
        });

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(ContactsProvider.CONTENT_URI, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsProvider.NAME));
                        @SuppressLint("Range") String contactPhone = cursor.getString(cursor.getColumnIndex(ContactsProvider.PHONE));
                        Toast.makeText(MainActivity.this, contactName + ": " + contactPhone, Toast.LENGTH_SHORT).show();
                    } while (cursor.moveToNext());
                    cursor.close();
                } else {
                    Toast.makeText(MainActivity.this, "No contacts found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
