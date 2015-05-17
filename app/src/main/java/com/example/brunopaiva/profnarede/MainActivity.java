package com.example.brunopaiva.profnarede;

import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements View.OnTouchListener, View.OnDragListener {
    private static String TAG = "narede";

    private LinearLayout lessonPlan;
    private LinearLayout planelements;

    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        planelements = (LinearLayout) findViewById(R.id.plan_elements);
        lessonPlan = (LinearLayout) findViewById(R.id.lesson_plan);
        lessonPlan.setOnDragListener(this);
        for (int i = 0 ; i < planelements.getChildCount() ; i++) {
            planelements.getChildAt(i).setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            v.startDrag(null, new View.DragShadowBuilder(v), v, 0);
            setVisibilitySafe(v, View.INVISIBLE);
        }
        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        View draggedView = (View) event.getLocalState();
        switch(event.getAction()) {

            case DragEvent.ACTION_DRAG_ENDED:

                if (isFirst) {
                    isFirst = false;
                    lessonPlan.removeAllViews();
                }
                Log.i(TAG, "Drag Ended");
                planelements.removeView(draggedView);
                lessonPlan.addView(draggedView);
                draggedView.setOnTouchListener(null);
                setVisibilitySafe(draggedView, View.VISIBLE);

                break;
            case DragEvent.ACTION_DROP:
                break;
            default:
                break;
        }
        return true;
    }

    protected void setVisibilitySafe(final View v, final int visibility) {
        v.post(new Runnable(){

            @Override
            public void run() {
                v.setVisibility(visibility);
            }
        });
    }
}
