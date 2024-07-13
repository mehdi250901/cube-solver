package com.example.pa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CubeFacePagerAdapter extends RecyclerView.Adapter<CubeFacePagerAdapter.ViewHolder> {

    private Context context;
    private char[][][] inputColors;
    private char selectedColor = 'w';
    private static final String[] faceTitles = {"Up - White", "Right - Orange", "Front - Green", "Down - Yellow", "Left - Red", "Back - Blue"};

    public CubeFacePagerAdapter(Context context, char[][][] inputColors) {
        this.context = context;
        this.inputColors = inputColors;
    }

    public void setSelectedColor(char selectedColor) {
        this.selectedColor = selectedColor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_cube_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.faceTitle.setText(faceTitles[position]);
        holder.gridLayout.removeAllViews();
        char[][] face = inputColors[position];
        for (int i = 0; i < face.length; i++) {
            for (int j = 0; j < face[i].length; j++) {
                final int row = i;
                final int col = j;
                View colorView = new View(context);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 100;
                params.height = 100;
                params.setMargins(5, 5, 5, 5);
                colorView.setLayoutParams(params);
                colorView.setBackgroundColor(getColor(face[row][col]));
                colorView.setOnClickListener(v -> {
                    face[row][col] = selectedColor;
                    colorView.setBackgroundColor(getColor(selectedColor));
                });
                holder.gridLayout.addView(colorView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return inputColors.length;
    }

    private int getColor(char colorChar) {
        switch (colorChar) {
            case 'w':
                return context.getResources().getColor(android.R.color.white);
            case 'r':
                return context.getResources().getColor(android.R.color.holo_red_dark);
            case 'g':
                return context.getResources().getColor(android.R.color.holo_green_dark);
            case 'b':
                return context.getResources().getColor(android.R.color.holo_blue_dark);
            case 'o':
                return context.getResources().getColor(android.R.color.holo_orange_dark);
            case 'y':
                return context.getResources().getColor(android.R.color.holo_orange_light);
            default:
                return context.getResources().getColor(android.R.color.black);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView faceTitle;
        GridLayout gridLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            faceTitle = itemView.findViewById(R.id.faceTitle);
            gridLayout = itemView.findViewById(R.id.cubeGrid);
        }
    }
}
