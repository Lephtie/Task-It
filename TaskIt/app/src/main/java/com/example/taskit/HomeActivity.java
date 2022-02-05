package com.example.taskit;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.taskit.Utils.DatabaseHandler;
import com.example.taskit.adapter.taskit_adapter;
import com.example.taskit.model.taskit_model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView tasksrecyclerView;
    private taskit_adapter taskitAdapter;
    private FloatingActionButton fab;
    private List<taskit_model> taskitList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        taskitList = new ArrayList<>();

        tasksrecyclerView = findViewById(R.id.idRecycler);
        tasksrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskitAdapter = new taskit_adapter(db,this);
        tasksrecyclerView.setAdapter(taskitAdapter);


        fab = findViewById(R.id.idFab);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(taskitAdapter));
        itemTouchHelper.attachToRecyclerView(tasksrecyclerView);


        taskitList = findViewById(R.id.idRecycler);
        taskitList = db.getAllTasks();
        Collections.reverse(taskitList);
        taskitAdapter.setTaskitList(taskitList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskitList = db.getAllTasks();
        Collections.reverse(taskitList);
        taskitAdapter.setTaskitList(taskitList);
        taskitAdapter.notifyDataSetChanged();

    }
}
