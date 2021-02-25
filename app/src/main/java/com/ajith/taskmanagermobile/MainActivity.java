package com.ajith.taskmanagermobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.ajith.taskmanagermobile.Adapter.TaskAdapter;
import com.ajith.taskmanagermobile.Model.TaskModel;
import com.ajith.taskmanagermobile.utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    //Class variables
    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private List<TaskModel> tasklist;
    private DatabaseHandler db;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hiding the actionbar
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        //Instantating the task list
        tasklist = new ArrayList<>();

        taskRecyclerView = findViewById(R.id.recycler_view);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(db, this);
        taskRecyclerView.setAdapter(taskAdapter);

        fab = findViewById(R.id.add_btn);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        tasklist = db.getAllTasks();
        Collections.reverse(tasklist);
        taskAdapter.setTask(tasklist);

        fab.setOnClickListener(v -> NewTaskFragment.newInstance().show(getSupportFragmentManager(), NewTaskFragment.TAG));
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        tasklist = db.getAllTasks();
        Collections.reverse(tasklist);
        taskAdapter.setTask(tasklist);
        taskAdapter.notifyDataSetChanged();
    }
}