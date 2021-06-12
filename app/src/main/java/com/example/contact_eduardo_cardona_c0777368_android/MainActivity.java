package com.example.contact_eduardo_cardona_c0777368_android;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_eduardo_cardona_c0777368_android.model.Contact;
import com.example.contact_eduardo_cardona_c0777368_android.util.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener{

    private static final String TAG = "MainActivity";
    public static final int ADD_CONTACT_REQUEST_CODE = 1;
    public static final String CONTACT_ID = "contact_id";


    private RecyclerView recyclerView;

    // declaration of employeeViewModel
    private ContactViewModel contactViewModel;
    //adapter
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ContactViewModel.class);
        recyclerView = findViewById(R.id.contacts_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactViewModel.getAllEmployees().observe(this, contacts -> {
            // set adapter
            recyclerViewAdapter = new RecyclerViewAdapter(contacts, this, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddUpdateActivity.class);
            // the following approach as startActivityForResult is deprecated
            launcher.launch(intent);

        });

        // attach the itemTouchHelper to my recyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    // the following approach instead of onActivityResult as startActivityForResult is deprecated
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
//                if (result.getResultCode() == Activity.RESULT_OK) {
//                    Intent data = result.getData();
//                    String name = data.getStringExtra(AddUpdateContact.NAME_REPLY);
//                    String salary = data.getStringExtra(AddUpdateContact.SALARY_REPLY);
//                    String department = data.getStringExtra(AddUpdateContact.DEPARTMENT_REPLY);
//                    // getting the current date
//                    Calendar cal = Calendar.getInstance();
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
//                    String joiningDate = sdf.format(cal.getTime());
//
//                    Employee employee = new Co(name, department, joiningDate, Double.parseDouble(salary));
//                    employeeViewModel.insert(employee);
//                }
            });

    @Override
    public void onContactClick(int position) {
        Log.d(TAG, "onContactClick: " + position);
        Contact employee = contactViewModel.getAllEmployees().getValue().get(position);
        Intent intent = new Intent(MainActivity.this, AddUpdateActivity.class);
        intent.putExtra(CONTACT_ID, employee.getId());
        startActivity(intent);
    }

    // searchbar event
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item =  menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


}