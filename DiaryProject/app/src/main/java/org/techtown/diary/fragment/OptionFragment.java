package org.techtown.diary.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.techtown.diary.AlarmActivity;
import org.techtown.diary.DarkModeActivity;
import org.techtown.diary.FontActivity;
import org.techtown.diary.R;
import org.techtown.diary.helper.MyTheme;

public class OptionFragment extends Fragment {
    public static final int REQUEST_FONT_CHANGE = 101;
    public static final int REQUEST_ALARM_SETTING = 102;
    public static final int REQUEST_DARK_MODE = 103;

    private TextView curFontTextView;

    private int curFontIndex = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_option, container, false);

        SharedPreferences pref = getContext().getSharedPreferences(MyTheme.SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);
        if(pref != null) {
            curFontIndex = pref.getInt(MyTheme.FONT_KEY, 0);
        }

        curFontTextView = (TextView)rootView.findViewById(R.id.curFontTextView);
        setCurFontText();

        RelativeLayout fontLayout = (RelativeLayout)rootView.findViewById(R.id.fontLayout);
        fontLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FontActivity.class);
                getActivity().startActivityForResult(intent, REQUEST_FONT_CHANGE);
            }
        });

        RelativeLayout noticeLayout = (RelativeLayout)rootView.findViewById(R.id.noticeLayout);
        noticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AlarmActivity.class);
                getActivity().startActivityForResult(intent, REQUEST_ALARM_SETTING);
            }
        });

        RelativeLayout darkmodLayout = (RelativeLayout)rootView.findViewById(R.id.darkmodeLayout);
        darkmodLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DarkModeActivity.class);
                getActivity().startActivityForResult(intent, REQUEST_DARK_MODE);
            }
        });

        return rootView;
    }

    private void setCurFontText() {
        switch(curFontIndex) {
            case 0:
                curFontTextView.setText("넥슨배찌체");
                break;
            case 1:
                curFontTextView.setText("점꼴체");
                break;
            case 2:
                curFontTextView.setText("다시시작해체");
                break;
            case 3:
                curFontTextView.setText("할아버지의나눔체");
                break;
            case 4:
                curFontTextView.setText("꼬마나비체");
                break;
            default:
                curFontTextView.setText("넥슨배찌체");
                break;
        }
    }
}
