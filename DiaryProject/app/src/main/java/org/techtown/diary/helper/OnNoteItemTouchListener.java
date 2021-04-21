package org.techtown.diary.helper;

import android.view.MotionEvent;
import android.view.View;

import org.techtown.diary.note.NoteViewHolder;

public interface OnNoteItemTouchListener {
    public void onItemTouch(NoteViewHolder holder, View view, int position, MotionEvent event);
}
