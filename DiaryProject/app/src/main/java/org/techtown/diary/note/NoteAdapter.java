package org.techtown.diary.note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.diary.R;
import org.techtown.diary.helper.OnNoteItemClickListener;
import org.techtown.diary.helper.OnNoteItemLongClickListener;
import org.techtown.diary.helper.OnNoteItemTouchListener;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> implements OnNoteItemClickListener, OnNoteItemTouchListener
, OnNoteItemLongClickListener{
    private ArrayList<Note> items = new ArrayList<>();
    private Context context;
    private OnNoteItemClickListener clickListener;
    private OnNoteItemTouchListener touchListener;
    private OnNoteItemLongClickListener longClickListener;

    private int layoutType = 0;
    private long duration = 1000;
    private boolean isStar = false;

    public NoteAdapter(Context context) {
        this.context = context;
    }

    public void addItem(Note item) {
        if(items.size() != 0) {
            items.add(items.get(items.size() - 1));

            for(int i = items.size() - 2; i >= 0; i--) {
                items.set(i + 1, items.get(i));
            }

            items.set(0, item);
        } else {
            items.add(item);
        }
    }

    public ArrayList<Note> getItems() {
        return items;
    }

    public void setItems(ArrayList<Note> items) {
        this.items = items;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public void setStar() {
        for(int i = 0; i < items.size(); i++) {
            Note item = items.get(i);
            if(item.getStarIndex() == 0) {
                items.remove(i);
                i--;
            }
        }
    }

    public void setIsStar(boolean isStar) {
        this.isStar = isStar;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.list_item, parent, false);

        return new NoteViewHolder(itemView, layoutType, context);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note item = items.get(position);

        holder.setItem(item);
        holder.setOnItemClickListener(clickListener);
        holder.setOnItemTouchListener(touchListener);
        holder.setOnItemLongClickListener(longClickListener);
        holder.setLayoutType(layoutType);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Note getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnNoteItemClickListener listener) {
        clickListener = listener;
    }

    public void setOnItemTouchListener(OnNoteItemTouchListener listener) {
        touchListener = listener;
    }

    public void setOnItemLongClickListener(OnNoteItemLongClickListener listener) {
        longClickListener = listener;
    }

    @Override
    public void onItemClick(NoteViewHolder holder, View view, int position) {
        if(clickListener != null) {
            clickListener.onItemClick(holder, view, position);
        }
    }

    @Override
    public void onItemTouch(NoteViewHolder holder, View view, int position, MotionEvent event) {
        if(touchListener != null) {
            touchListener.onItemTouch(holder, view, position, event);
        }
    }

    @Override
    public void onLongClick(NoteViewHolder holder, View view, int position) {
        if(longClickListener != null) {
            longClickListener.onLongClick(holder, view, position);
        }
    }
}
