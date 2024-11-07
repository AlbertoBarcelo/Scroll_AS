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
    private EditText editScrollingText; // EditText temporal para editar el texto principal
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

        // Ocultar los botones inicialmente
        addCommentButton.setVisibility(View.GONE);
        editTextButton.setVisibility(View.GONE);

        // Mostrar los botones solo cuando el usuario llega al final del ScrollView
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
            if (diff <= 0) { // Si diff es 0 o menos, significa que el usuario ha llegado al final del scroll
                addCommentButton.setVisibility(View.VISIBLE);
                editTextButton.setVisibility(View.VISIBLE);
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

        // Configurar el botón "Edit Text" para permitir la edición del texto principal
        editTextButton.setOnClickListener(view -> {
            // Crear un EditText temporal para editar el texto principal
            editScrollingText = new EditText(this);
            editScrollingText.setText(scrollingText.getText());
            editScrollingText.setLayoutParams(scrollingText.getLayoutParams());
            editScrollingText.setTextSize(16);
            editScrollingText.setTextColor(scrollingText.getCurrentTextColor());
            editScrollingText.setImeOptions(EditorInfo.IME_ACTION_DONE);

            // Reemplazar el TextView `scrolling_text` con el EditText `editScrollingText`
            int index = commentsContainer.indexOfChild(scrollingText);
            commentsContainer.removeView(scrollingText);
            commentsContainer.addView(editScrollingText, index);

            // Manejar el "Done" en el teclado para guardar los cambios
            editScrollingText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Actualizar el texto principal con el texto editado
                    scrollingText.setText(editScrollingText.getText().toString());
                    commentsContainer.removeView(editScrollingText); // Eliminar el EditText temporal
                    commentsContainer.addView(scrollingText, index); // Volver a agregar el TextView original
                    return true;
                }
                return false;
            });
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
