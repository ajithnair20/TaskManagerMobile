package com.ajith.taskmanagermobile.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajith.taskmanagermobile.MainActivity;
import com.ajith.taskmanagermobile.Model.TaskModel;
import com.ajith.taskmanagermobile.NewTaskFragment;
import com.ajith.taskmanagermobile.R;
import com.ajith.taskmanagermobile.utils.DatabaseHandler;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<TaskModel> tasksList;
    private MainActivity mainActivity;
    private DatabaseHandler db;


    public TaskAdapter(DatabaseHandler db, MainActivity activity){
        this.mainActivity = activity;
        this.db = db;
    }

    public Context getContext(){
        return mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        db.openDatabase();
        final  TaskModel item  = tasksList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(),1);
                }else{
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    public void setTask(List<TaskModel> tasksList){
        this.tasksList = tasksList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    public void deleteItem(int position){
        TaskModel item  = tasksList.get(position);
        db.deleteTask(item.getId());
        tasksList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        TaskModel item  = tasksList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        NewTaskFragment fragment = new NewTaskFragment();
        fragment.setArguments(bundle);
        fragment.show(mainActivity.getSupportFragmentManager(), NewTaskFragment.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox task;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.chk_done);
        }
    }
}
