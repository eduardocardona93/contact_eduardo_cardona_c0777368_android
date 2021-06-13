package com.example.contact_eduardo_cardona_c0777368_android;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_eduardo_cardona_c0777368_android.model.Contact;
import com.example.contact_eduardo_cardona_c0777368_android.util.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener, RecyclerViewAdapter.OnContactLongPressListener{

    private static final String TAG = "MainActivity";
    public static final int ADD_CONTACT_REQUEST_CODE = 1;
    public static final String CONTACT_ID = "contact_id";
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    // TelephonyManager
    private TelephonyManager mTelephonyManager;

    private RecyclerView recyclerView;

    // declaration of contactViewModel
    private ContactViewModel contactViewModel;
    //adapter
    private RecyclerViewAdapter recyclerViewAdapter;


    private Contact deletedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




//        if(!checkPermission(Manifest.permission.SEND_SMS)) {
//            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS},
//                    SEND_SMS_PERMISSION_REQUEST_CODE);
//        }else{
            // initializing the Telephony manager instance
            mTelephonyManager = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);

            contactViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                    .create(ContactViewModel.class);
            recyclerView = findViewById(R.id.contacts_rv);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            getContactsFromDb();

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddUpdateContactActivity.class);
                // the following approach as startActivityForResult is deprecated
                launcher.launch(intent);

            });

            // attach the itemTouchHelper to my recyclerView
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
//            }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }



        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Contact contact = contactViewModel.getAllContacts().getValue().get(position);
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Do you really want to delete this contact?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        deletedContact = contact;
                        contactViewModel.delete(contact);
                        Snackbar.make(recyclerView, deletedContact.getFirstName() + deletedContact.getLastName() + " is deleted!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", v -> contactViewModel.insert(deletedContact)).show();
                    });
                    builder.setNegativeButton("No", (dialog, which) -> recyclerViewAdapter.notifyDataSetChanged());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    break;
                case ItemTouchHelper.RIGHT:
                    Intent intent = new Intent(MainActivity.this, AddUpdateContactActivity.class);
                    intent.putExtra(CONTACT_ID, contact.getId());
                    startActivity(intent);
                    break;
            }
        }


        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .setIconHorizontalMargin(1, 1)
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .addSwipeRightActionIcon(R.drawable.ic_update)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    // the following approach instead of onActivityResult as startActivityForResult is deprecated
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String firstName = data.getStringExtra(AddUpdateContactActivity.FIRST_NAME_REPLY);
                    String lastName = data.getStringExtra(AddUpdateContactActivity.LAST_NAME_REPLY);
                    String phone = data.getStringExtra(AddUpdateContactActivity.PHONE_REPLY);
                    String email = data.getStringExtra(AddUpdateContactActivity.EMAIL_REPLY);
                    String address = data.getStringExtra(AddUpdateContactActivity.ADDRESS_REPLY);
                    Contact contact = new Contact(firstName, lastName, phone, email, address);
                    contactViewModel.insert(contact);
                }
            });

    @Override
    public void onContactClick(int position) {
        Log.d(TAG, "onContactClick: " + position);
        Contact contact = contactViewModel.getAllContacts().getValue().get(position);
        Intent intent = new Intent(MainActivity.this, AddUpdateContactActivity.class);
        intent.putExtra(CONTACT_ID, contact.getId());
        startActivity(intent);
    }

    @Override
    public void onContactLongPress(int position) {
        Contact contact = contactViewModel.getAllContacts().getValue().get(position);
        // alert builder
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.action_layout, null);
        builder.setView(view);
        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //
        final EditText subjectET = view.findViewById(R.id.subjectET);
        final EditText messageET = view.findViewById(R.id.messageET);
        final TextView tvlabel1 = view.findViewById(R.id.tvlabel1);
        final TextView tvlabel2 = view.findViewById(R.id.tvlabel2);
        final Spinner spinner_action = view.findViewById(R.id.spinner_action);
        final Button actionBtn = view.findViewById(R.id.btn_action);

        spinner_action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] buttonLabels = getBaseContext().getResources().getStringArray(R.array.action_button_label);


                actionBtn.setText(buttonLabels[position]);
                switch (position){
                    case 1:
                        tvlabel1.setVisibility(view.GONE);
                        tvlabel2.setVisibility(view.VISIBLE);
                        subjectET.setVisibility(view.GONE);
                        messageET.setVisibility(view.VISIBLE);
                        break;
                    case 2:
                        tvlabel1.setVisibility(view.VISIBLE);
                        tvlabel2.setVisibility(view.VISIBLE);
                        subjectET.setVisibility(view.VISIBLE);
                        messageET.setVisibility(view.VISIBLE);
                        break;
                    default:
                        tvlabel1.setVisibility(view.GONE);
                        tvlabel2.setVisibility(view.GONE);
                        subjectET.setVisibility(view.GONE);
                        messageET.setVisibility(view.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                actionBtn.setText(Arrays.asList(R.array.action_button_label).get(0));
                tvlabel1.setVisibility(view.GONE);
                tvlabel2.setVisibility(view.GONE);
                subjectET.setVisibility(view.GONE);
                messageET.setVisibility(view.GONE);
            }
        });

        actionBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = spinner_action.getSelectedItemPosition();
                switch (position){
                    case 1:
                        if (messageET.getText().toString().isEmpty()){
                            messageET.setError("Message field cannot be empty");
                            messageET.requestFocus();
                        }else{
                            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + contact.getPhoneNumber()));
                            smsIntent.putExtra("sms_body", messageET.getText().toString().isEmpty());
                            startActivity(smsIntent);
                            alertDialog.dismiss();
                        }
                        break;
                    case 2:
                        if (subjectET.getText().toString().isEmpty()){
                            subjectET.setError("Subject field cannot be empty");
                            subjectET.requestFocus();
                        }else if(messageET.getText().toString().isEmpty()){
                            messageET.setError("Message field cannot be empty");
                            messageET.requestFocus();
                        }else{
                            Intent email = new Intent(Intent.ACTION_SEND);
                            email.putExtra(Intent.EXTRA_EMAIL, new String[]{contact.getEmail()});
                            email.putExtra(Intent.EXTRA_SUBJECT, subjectET.getText().toString());
                            email.putExtra(Intent.EXTRA_TEXT, messageET.getText().toString());

                            //need this to prompts email client only
                            email.setType("message/rfc822");
                            alertDialog.dismiss();
                            startActivity(Intent.createChooser(email, "Choose an Email client :"));
                        }
                        break;
                    default:
                        String dial = "tel:" + contact.getPhoneNumber();
                        alertDialog.dismiss();
                        // an intent to dial a number
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));

                        break;
                }
            }
        });
    };
    // searchbar event
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item =  menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    getContactsFileredFromDb(query);
                }else{
                    getContactsFromDb();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    getContactsFileredFromDb(newText);
                }else{
                    getContactsFromDb();
                }
                return true;
            }


        });
        return super.onCreateOptionsMenu(menu);
    }
    private void getContactsFileredFromDb(String searchText ) {

        contactViewModel.getSearchedAllContacts(searchText).observe(this, contacts -> {
            // set adapter
            recyclerViewAdapter = new RecyclerViewAdapter(contacts, this, this,this);
            recyclerView.setAdapter(recyclerViewAdapter);
        });

    }
    private void getContactsFromDb() {
        TextView usersCountTV = findViewById(R.id.usersCountTV);
        contactViewModel.getAllContacts().observe(this, contacts -> {
            // set adapter
            recyclerViewAdapter = new RecyclerViewAdapter(contacts, this, this,this);
            recyclerView.setAdapter(recyclerViewAdapter);
            usersCountTV.setText(String.format("Total Contacts: %d", contacts.size()));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContactsFromDb();
    }

    private boolean checkPermission(String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(this, permission);
        return (checkPermission == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQUEST_CODE: {
                if (grantResults.length == 0 || (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS},
                            SEND_SMS_PERMISSION_REQUEST_CODE);
                }
                return;
            }
        }
    }
}