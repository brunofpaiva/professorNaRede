package com.example.brunopaiva.profnarede;

import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

            final EditText edittext = (EditText) planelements.getChildAt(i).findViewById(R.id.inputEditText);
            edittext.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        // Perform action on key press
                        onEnterPressed(edittext);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void onEnterPressed(final EditText editText) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String text = editText.getText().toString();
                View parentView = ((View) editText.getParent().getParent());

                parentView.findViewById(R.id.commentLayout).setVisibility(View.VISIBLE);
                ((TextView) parentView.findViewById(R.id.comentario)).setText(text);
                editText.setText("");
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            setVisibilitySafe(v, View.INVISIBLE);
            v.startDrag(null, new View.DragShadowBuilder(v), v, 0);
        }
        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        Log.i(TAG, "onDrag");
        final View draggedView = (View) event.getLocalState();
        switch(event.getAction()) {

            case DragEvent.ACTION_DRAG_ENDED:
                break;
            case DragEvent.ACTION_DROP:
                Log.i(TAG, "Drop");

                if (isFirst) {
                    isFirst = false;
                    lessonPlan.removeAllViews();
                }
                planelements.removeView(draggedView);
                lessonPlan.addView(draggedView);
                draggedView.setOnTouchListener(null);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        draggedView.setVisibility(View.VISIBLE);
                        draggedView.findViewById(R.id.inputLayout).setVisibility(View.VISIBLE);
                    }
                });
                break;
            default:
                break;
        }
        return true;
    }

    protected void setVisibilitySafe(final View v, final int visibility) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                v.setVisibility(visibility);
            }
        });
    }
}
