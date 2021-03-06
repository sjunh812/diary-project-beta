package org.techtown.diary.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import org.techtown.diary.R;

public class CustomUpdateDialog extends Dialog {
    ImageButton cancelButton;
    Button deleteButton;
    Button updateButton;

    public CustomUpdateDialog(@NonNull Context context) {
        super(context);
    }

    public CustomUpdateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog_custom);

        cancelButton = (ImageButton)findViewById(R.id.cancelButton);
        deleteButton = (Button)findViewById(R.id.deleteButton);
        updateButton = (Button)findViewById(R.id.updateButton);
    }

    public void setCancelButtonOnClickListener(View.OnClickListener listener) {
        cancelButton.setOnClickListener(listener);
    }

    public void setDeleteButtonOnClickListener(View.OnClickListener listener) {
        deleteButton.setOnClickListener(listener);
    }

    public void setUpdateButtonOnClickListener(View.OnClickListener listener) {
        updateButton.setOnClickListener(listener);
    }
}
