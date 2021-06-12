package com.example.contact_eduardo_cardona_c0777368_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contact_eduardo_cardona_c0777368_android.model.Contact;
import com.example.contact_eduardo_cardona_c0777368_android.util.ContactViewModel;

public class AddUpdateContactActivity extends AppCompatActivity {


    public static final String FIRST_NAME_REPLY = "firstname_reply";
    public static final String LAST_NAME_REPLY = "lastname_reply";
    public static final String PHONE_REPLY = "phone_reply";
    public static final String EMAIL_REPLY = "email_reply";
    public static final String ADDRESS_REPLY = "address_reply";

    private EditText etFirstname, etLastName, etPhone,etEmail,etAddress;

    private boolean isEditing = false;
    private int contactId = 0;
    private Contact contactToBeUpdated;

    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_contact);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ContactViewModel.class);

        etFirstname = findViewById(R.id.firstname_et);
        etLastName = findViewById(R.id.lastname_et);
        etPhone = findViewById(R.id.phone_et);
        etEmail = findViewById(R.id.email_et);
        etAddress = findViewById(R.id.address_et);

        Button addUpdateButton = findViewById(R.id.btn_add_update_contact);

        addUpdateButton.setOnClickListener(v -> {
            addUpdateContact();
        });

        if (getIntent().hasExtra(MainActivity.CONTACT_ID)) {
            contactId = getIntent().getIntExtra(MainActivity.CONTACT_ID, 0);
            Log.d("TAG", "onCreate: " + contactId);

            contactViewModel.getContact(contactId).observe(this, contact -> {
                if (contact != null) {
                    etFirstname.setText(contact.getFirstName());
                    etLastName.setText(String.valueOf(contact.getLastName()));
                    etPhone.setText(String.valueOf(contact.getPhoneNumber()));
                    etEmail.setText(String.valueOf(contact.getEmail()));
                    etAddress.setText(String.valueOf(contact.getAddress()));

                    contactToBeUpdated = contact;
                }
            });
            TextView label = findViewById(R.id.addContacTV);
            isEditing = true;
            label.setText(R.string.update_label);
            addUpdateButton.setText(R.string.update);
        }
    }

    private void addUpdateContact() {
        String firstName = etFirstname.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String  address = etAddress.getText().toString().trim();
        if (firstName.isEmpty()) {
            etFirstname.setError("first name field cannot be empty");
            etFirstname.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            etLastName.setError("last name field cannot be empty");
            etLastName.requestFocus();
            return;
        }

        if (isEditing) {
            Contact contact = new Contact();
            contact.setId(contactId);
            contact.setFirstName(firstName);
            contact.setLastName(lastName);

            contactViewModel.update(contact);
        } else {
            Intent replyIntent = new Intent();
            replyIntent.putExtra(FIRST_NAME_REPLY, firstName);
            replyIntent.putExtra(LAST_NAME_REPLY, lastName);
            replyIntent.putExtra(PHONE_REPLY, phone);
            replyIntent.putExtra(EMAIL_REPLY, email);
            replyIntent.putExtra(ADDRESS_REPLY, address);
            setResult(RESULT_OK, replyIntent);

            Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}