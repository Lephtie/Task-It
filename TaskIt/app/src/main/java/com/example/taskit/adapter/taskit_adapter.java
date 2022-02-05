package com.example.taskit.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskit.AddNewTask;
import com.example.taskit.HomeActivity;
import com.example.taskit.R;
import com.example.taskit.Utils.DatabaseHandler;
import com.example.taskit.model.taskit_model;

import java.util.List;

public class taskit_adapter extends RecyclerView.Adapter<taskit_adapter.ViewHolder> {

    private List<taskit_model> taskitList;
    private HomeActivity homeActivity;
    private DatabaseHandler db;

    public taskit_adapter(DatabaseHandler db, HomeActivity homeActivity) {
        this.db = this.db;
        this.homeActivity = homeActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        final taskit_model item = taskitList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   db.updateStatus(item.getId(),1);
               }
               else{
                   db.updateStatus(item.getId(),0);
               }
            }
        });
    }


    @Override
    public int getItemCount() {
        return taskitList.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }


    public void setTaskitList(List<taskit_model> taskitList){
        this.taskitList = taskitList;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return homeActivity;
    }

    public void deleteItem(int position){
        taskit_model item = taskitList.get(position);
        db.deleteTask(item.getId());
        taskitList.remove(position);
        notifyItemRemoved(position);
    }

    public void setTasks(List<taskit_model> taskitList){
        this.taskitList = taskitList;
    }


    public void editItem(int position){
        taskit_model item = taskitList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(homeActivity.getSupportFragmentManager(), AddNewTask.TAG);

    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.idTaskCheckBox);
        }

    }

}

