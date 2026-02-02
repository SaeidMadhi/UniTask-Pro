package com.mytest.mytasks;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    Context context;
    ArrayList<Task> taskList;
    DatabaseHelper dbHelper;


    public TaskAdapter(Context context, ArrayList<Task> taskList, DatabaseHelper dbHelper) {
        this.context = context;
        this.taskList = taskList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.tvBaslik.setText(task.getBaslik());
        holder.tvTarih.setText(task.getTarih());


        holder.cbDurum.setOnCheckedChangeListener(null);
        holder.cbDurum.setChecked(false);


        int onem = task.getOnem();
        if (onem == 3) holder.viewOnem.setBackgroundColor(Color.RED);
        else if (onem == 2) holder.viewOnem.setBackgroundColor(Color.parseColor("#FFA500"));
        else holder.viewOnem.setBackgroundColor(Color.LTGRAY);


        holder.cbDurum.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Görevi Sil")
                    .setMessage("Bu görevi tamamladın mı? Silinecek.")
                    .setPositiveButton("Evet, Sil", (dialog, which) -> {

                        dbHelper.gorevSil(task.getId());


                        taskList.remove(position);


                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, taskList.size());

                        Toast.makeText(context, "Görev Tamamlandı ve Silindi!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("İptal", (dialog, which) -> {

                        holder.cbDurum.setChecked(false);
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvBaslik, tvTarih;
        CheckBox cbDurum;
        View viewOnem;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBaslik = itemView.findViewById(R.id.tvTaskTitle);
            tvTarih = itemView.findViewById(R.id.tvTaskDate);
            cbDurum = itemView.findViewById(R.id.cbTaskDone);
            viewOnem = itemView.findViewById(R.id.viewPriorityColor);
        }
    }
}