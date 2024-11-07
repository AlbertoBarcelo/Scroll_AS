package com.example.as_task13;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button addCommentButton;
    private Button editTextButton;
    private EditText commentEditText;
    private EditText editScrollingText;
    private LinearLayout commentsContainer;
    private ScrollView scrollView;
    private TextView scrollingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addCommentButton = findViewById(R.id.add_comment_button);
        editTextButton = findViewById(R.id.edit_text_button);
        commentEditText = findViewById(R.id.comment_edit_text);
        commentsContainer = findViewById(R.id.comments_container);
        scrollView = findViewById(R.id.scrolling_area);
        scrollingText = findViewById(R.id.scrolling_text);

        addCommentButton.setVisibility(View.GONE);
        editTextButton.setVisibility(View.GONE);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
            if (diff <= 0) {
                addCommentButton.setVisibility(View.VISIBLE);
                editTextButton.setVisibility(View.VISIBLE);
            }
        });


        addCommentButton.setOnClickListener(view -> {
            commentEditText.setVisibility(View.VISIBLE);
            commentEditText.requestFocus();
        });

        commentEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String comment = commentEditText.getText().toString().trim();
                if (!comment.isEmpty()) {
                    addCommentToContainer(comment);
                    commentEditText.setText("");
                    commentEditText.setVisibility(View.GONE);                 }
                return true;
            }
            return false;
        });


        editTextButton.setOnClickListener(view -> {
            editScrollingText = new EditText(this);
            editScrollingText.setText(scrollingText.getText());
            editScrollingText.setLayoutParams(scrollingText.getLayoutParams());
            editScrollingText.setTextSize(16);
            editScrollingText.setTextColor(scrollingText.getCurrentTextColor());
            editScrollingText.setImeOptions(EditorInfo.IME_ACTION_DONE);


            int index = commentsContainer.indexOfChild(scrollingText);
            commentsContainer.removeView(scrollingText);
            commentsContainer.addView(editScrollingText, index);

            editScrollingText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    scrollingText.setText(editScrollingText.getText().toString());
                    commentsContainer.removeView(editScrollingText);
                    commentsContainer.addView(scrollingText, index);
                    return true;
                }
                return false;
            });
        });
    }

    private void addCommentToContainer(String comment) {
        TextView commentTextView = new TextView(this);
        commentTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        commentTextView.setText(comment);
        commentTextView.setTextSize(16);
        commentTextView.setPadding(8, 8, 8, 8);
        commentsContainer.addView(commentTextView);
    }
}
