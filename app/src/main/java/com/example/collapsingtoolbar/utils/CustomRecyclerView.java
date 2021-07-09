package com.example.collapsingtoolbar.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomRecyclerView  extends androidx.recyclerview.widget.RecyclerView {

    /*===============================================================   CONSTRUCTOR   ===============================================================*/

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /*===============================================================   UTILITY METHOD   ===============================================================*/

    @Override
    public boolean fling(int velocityX, int velocityY) {

                    // This method sets the speed of RecyclerView scroll speed.

        velocityY *= 0.2; // keep it less than 1.0 to slowdown
        return super.fling(velocityX, velocityY);
    }

}
