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
    private EditText commentEditText;
    private LinearLayout commentsContainer;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addCommentButton = findViewById(R.id.add_comment_button);
        commentEditText = findViewById(R.id.comment_edit_text);
        commentsContainer = findViewById(R.id.comments_container);
        scrollView = findViewById(R.id.scrolling_area);

        // Ocultar el botón "Add Comment" inicialmente
        addCommentButton.setVisibility(View.GONE);

        // Mostrar el botón "Add Comment" solo cuando el usuario llegue al final del ScrollView
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
            if (diff <= 0) { // Si diff es 0 o menos, significa que el usuario ha llegado al final del scroll
                addCommentButton.setVisibility(View.VISIBLE);
            }
        });

        // Configurar el botón "Add Comment" para mostrar el EditText para comentarios
        addCommentButton.setOnClickListener(view -> {
            commentEditText.setVisibility(View.VISIBLE); // Mostrar el campo de entrada para el comentario
            commentEditText.requestFocus();
        });

        // Configurar el EditText para agregar el comentario cuando el usuario presiona "Done"
        commentEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String comment = commentEditText.getText().toString().trim();
                if (!comment.isEmpty()) {
                    addCommentToContainer(comment); // Agrega el comentario al contenedor de comentarios
                    commentEditText.setText(""); // Limpia el campo de texto
                    commentEditText.setVisibility(View.GONE); // Oculta el EditText después de enviar el comentario
                }
                return true;
            }
            return false;
        });
    }

    // Método para añadir el comentario dinámicamente al contenedor
    private void addCommentToContainer(String comment) {
        TextView commentTextView = new TextView(this);
        commentTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        commentTextView.setText(comment);
        commentTextView.setTextSize(16);
        commentTextView.setPadding(8, 8, 8, 8);
        commentsContainer.addView(commentTextView); // Agrega el comentario al LinearLayout
    }
}
