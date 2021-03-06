package org.techtown.diary.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;

import org.techtown.diary.MainActivity;
import org.techtown.diary.R;
import org.techtown.diary.custom.MyRadioButton;
import org.techtown.diary.helper.MyTheme;
import org.techtown.diary.note.NoteDatabaseCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GraphFragment extends Fragment {
    /* 상수 */
    private static final String LOG = "GraphFragment";

    /* Radio Button UI */
    private RadioGroup radioGroup;
    private MyRadioButton allButton;
    private MyRadioButton yearButton;
    private MyRadioButton monthButton;

    /* 기분별 통계 UI */
    private TextView moodTitleTextView;
    private TextView moodTotalCountTextView;
    private TextView angryCount;
    private TextView coolCount;
    private TextView cryingCount;
    private TextView illCount;
    private TextView laughCount;
    private TextView mehCount;
    private TextView sadCount;
    private TextView smileCount;
    private TextView yawnCount;
    private ImageView angryImageView;
    private ImageView coolImageView;
    private ImageView cryingImageView;
    private ImageView illImageView;
    private ImageView laughImageView;
    private ImageView mehImageView;
    private ImageView sadImageView;
    private ImageView smileImageView;
    private ImageView yawnImageView;
    private ImageView crown;
    private ImageView crown2;
    private ImageView crown3;
    private ImageView crown4;
    private ImageView crown5;
    private ImageView crown6;
    private ImageView crown7;
    private ImageView crown8;
    private ImageView crown9;
    private LinearLayout textView;
    private TextView describeTextView;
    private TextView moodTextView;

    /* 차트 라이브러리 객체 */
    private PieChart chart1;              // 원형 그래프

    /* Helper */
    private NoteDatabaseCallback callback;

    /*  Data */
    int[] moodIconRes = {R.drawable.mood_angry_color, R.drawable.mood_cool_color,  R.drawable.mood_crying_color,
            R.drawable.mood_ill_color, R.drawable.mood_laugh_color, R.drawable.mood_meh_color,
            R.drawable.mood_sad, R.drawable.mood_smile_color, R.drawable.mood_yawn_color};
    private Context context;
    private ArrayList<Integer> colors = new ArrayList<>();      // 색깔 정보를 담은 ArrayList<Integer>
    private int curFontIndex = -1;                              // 현재 사용중인 폰트 종류
    private int selectRadioIndex = 0;                           // 전체보기 : 0, 올해 : 1, 이번달 : 2(default : 전체보기)
    private int maxMoodIndex = -1;                              // 제일 많은 개수를 가진 기분 종류
    private int maxCount = -1;                                  // 제일 많은 개수를 가진 기분의 count 값

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        if(context instanceof NoteDatabaseCallback) {
            callback = (NoteDatabaseCallback)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(callback != null) {
            callback = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);

        /* 휴대폰 내 저장되어있는 폰트 정보를 가져옴(SharedPreferences 이용) */
        SharedPreferences pref = getContext().getSharedPreferences(MyTheme.SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);
        if(pref != null) {
            curFontIndex = pref.getInt(MyTheme.FONT_KEY, 0);
        }

        initChartUI(rootView);       // 차트 초기화
        initUI(rootView);            // UI 초기화

        /* Radio Button 초기화 */
        allButton = (MyRadioButton)rootView.findViewById(R.id.allButton);
        yearButton = (MyRadioButton)rootView.findViewById(R.id.yearButton);
        monthButton = (MyRadioButton)rootView.findViewById(R.id.monthButton);
        radioGroup = (RadioGroup)rootView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                HashMap<Integer, Integer> hashMap = null;

                if(checkedId == R.id.allButton) {
                    moodTitleTextView.setText("전체");
                    selectRadioIndex = 0;
                    hashMap = callback.selectMoodCount(true, false, false);
                    describeTextView.setText("전체 통계 중 제일 많은 기분은 ");
                    //chart1.setCenterText("전체");     // 원형 그래프 가운데 text 표기

                } else if(checkedId == R.id.yearButton) {
                    moodTitleTextView.setText(MainActivity.yearFormat.format(new Date()) + "년");
                    selectRadioIndex = 1;
                    hashMap = callback.selectMoodCount(false, true, false);
                    describeTextView.setText("올해 통계 중 제일 많은 기분은 ");
                    //chart1.setCenterText(MainActivity.yearFormat.format(new Date()) + "년");     // 원형 그래프 가운데 text 표기

                } else if(checkedId == R.id.monthButton) {
                    moodTitleTextView.setText(Integer.parseInt(MainActivity.monthFormat.format(new Date())) + "월");
                    selectRadioIndex = 2;
                    hashMap = callback.selectMoodCount(false, false, true);
                    describeTextView.setText("이번달 통계 중 제일 많은 기분은 ");
                   // chart1.setCenterText(Integer.parseInt(MainActivity.monthFormat.format(new Date())) + "월");      // 원형 그래프 가운데 text 표기
                }

                chart1.setCenterTextTypeface(getCurTypeFace());
                chart1.setCenterTextSize(17f);
                setData1(hashMap);
            }
        });

        setSelectedRadioButton();       // 선택된 라디오버튼 index 에 따라 라디오버튼 Checked 활성화

        return rootView;
    }

    private void initUI(View rootView) {
        /* 기분별 통계 UI */
        moodTotalCountTextView = (TextView)rootView.findViewById(R.id.moodTotalCountTextView);
        moodTitleTextView = (TextView)rootView.findViewById(R.id.moodTitleTextView);
        angryCount = (TextView)rootView.findViewById(R.id.angryCount);
        coolCount = (TextView)rootView.findViewById(R.id.coolCount);
        cryingCount = (TextView)rootView.findViewById(R.id.cryingCount);
        illCount = (TextView)rootView.findViewById(R.id.illCount);
        laughCount = (TextView)rootView.findViewById(R.id.laughCount);
        mehCount = (TextView)rootView.findViewById(R.id.mehCount);
        sadCount = (TextView)rootView.findViewById(R.id.sadCount);
        smileCount = (TextView)rootView.findViewById(R.id.smileCount);
        yawnCount = (TextView)rootView.findViewById(R.id.yawnCount);
        crown = (ImageView)rootView.findViewById(R.id.crown);
        crown2 = (ImageView)rootView.findViewById(R.id.crown2);
        crown3 = (ImageView)rootView.findViewById(R.id.crown3);
        crown4 = (ImageView)rootView.findViewById(R.id.crown4);
        crown5 = (ImageView)rootView.findViewById(R.id.crown5);
        crown6 = (ImageView)rootView.findViewById(R.id.crown6);
        crown7 = (ImageView)rootView.findViewById(R.id.crown7);
        crown8 = (ImageView)rootView.findViewById(R.id.crown8);
        crown9 = (ImageView)rootView.findViewById(R.id.crown9);
        angryImageView = (ImageView)rootView.findViewById(R.id.angryImageView);
        coolImageView = (ImageView)rootView.findViewById(R.id.coolImageView);
        cryingImageView = (ImageView)rootView.findViewById(R.id.cryingImageView);
        illImageView = (ImageView)rootView.findViewById(R.id.illImageView);
        laughImageView = (ImageView)rootView.findViewById(R.id.laughImageView);
        mehImageView = (ImageView)rootView.findViewById(R.id.mehImageView);
        sadImageView = (ImageView)rootView.findViewById(R.id.sadImageView);
        smileImageView = (ImageView)rootView.findViewById(R.id.smileImageView);
        yawnImageView = (ImageView)rootView.findViewById(R.id.yawnImageView);
        textView = (LinearLayout)rootView.findViewById(R.id.textView);
        describeTextView = (TextView)rootView.findViewById(R.id.describeTextView);
        moodTextView = (TextView)rootView.findViewById(R.id.moodTextView);
    }

    private void initChartUI(View rootView) {
        /* 원형 그래프(기분별) */
        chart1 = (PieChart)rootView.findViewById(R.id.chart1);

        chart1.setUsePercentValues(true);
        chart1.getDescription().setEnabled(false);       // 추가 설명란 false
        chart1.setDrawHoleEnabled(false);
        chart1.setExtraOffsets(5,10,5,10);
        chart1.setHighlightPerTapEnabled(false);        // 특정부분 선택시 확대효과 여부

        //chart1.setTransparentCircleColor(getResources().getColor(R.color.white));   // 중간원과 바깥원 사이의 얇은 투명원의 색상 결정
        //chart1.setTransparentCircleAlpha(110);           // 중간원과 바깥원 사이의 얇은 투명원의 알파 값 결정
        //chart1.setTransparentCircleRadius(66f);          // 중간원과 바깥원 사이의 얇은 투명원의 반지름
        //chart1.setHoleRadius(58f);                       // 중간원의 반지름
        //chart1.setHoleColor(getResources().getColor(R.color.azure2));
        //chart1.setDrawCenterText(true);
        Legend legend1 = chart1.getLegend();             // 그래프의 구성요소들을 추가로 명시하는지 여부
        legend1.setEnabled(false);                        // 추가 구성요소 명시 false
        chart1.setEntryLabelColor(Color.WHITE);          // entry label 색상
        //chart1.setEntryLabelTextSize(12f);               // entry 구성요소 label 크기
        chart1.animateXY(1200, 1200);
    }

    private void setData1(HashMap<Integer, Integer> hashMap) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        int totalCount = 0; // 상황에 맞는 총 기분 수를 0으로 초기화
        maxMoodIndex = -1;
        maxCount = -1;
        colors.clear();     // 상황에 맞는 색깔배열을 만들기 위해 초기화

        for(int i = 0; i < 9; i++) {
            int count = 0;

            if(hashMap.containsKey(i)) {
                count = hashMap.get(i);
                setMoodCount(i, count);
                totalCount += count;
                addColor(i);                // 기분 종류에 맞게 색깔 설정
                entries.add(new PieEntry(count, "", resizeDrawable(moodIconRes[i])));
            } else {
                setMoodCount(i, count);     // 개수 0가 경우
            }
        }

        moodTotalCountTextView.setText("(총 " + totalCount + "건 중)");        // 총 기분 개수
        setCrownImage();                                    // 제일 많은 개수를 가진 기분에 왕관이미지를 추가

        PieDataSet dataSet = new PieDataSet(entries, "기분별 비율");
        dataSet.setDrawIcons(true);                             // 아이콘 표시 여부
        dataSet.setSliceSpace(10f);                             // 그래프 간격
        dataSet.setIconsOffset(new MPPointF(0, 55));      // 아이콘 offset
        //dataSet.setSelectionShift(5f);                        // 특정부분 선택시 확대효과 크기
        dataSet.setColors(colors);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f", value) + "%";
            }
        });

        PieData data = new PieData(dataSet);
        data.setValueTextSize(15f);                         // 그래프 내 text 크기
        data.setValueTextColor(Color.WHITE);                // 그래프 내 text 색상
        if(context != null) {                               // 그래프 내 text 폰트
            data.setValueTypeface(getCurTypeFace());
        }

        chart1.setData(data);
        chart1.invalidate();
    }

    private void setSelectedRadioButton() {
        switch(selectRadioIndex) {
            case 0:
                allButton.setChecked(true);
                break;
            case 1:
                yearButton.setChecked(true);
                break;
            case 2:
                monthButton.setChecked(true);
                break;
        }
    }

    private void setMoodCount(int moodIndex, int count) {
        if(maxCount < count) {
            maxCount = count;
            maxMoodIndex = moodIndex;
        } else if(maxCount == count) {      // 중복 값이 있는 max 라면 예외처리
            maxMoodIndex = -1;
        }

        switch(moodIndex) {
            case 0:
                angryCount.setText(String.valueOf(count));
                break;
            case 1:
                coolCount.setText(String.valueOf(count));
                break;
            case 2:
                cryingCount.setText(String.valueOf(count));
                break;
            case 3:
                illCount.setText(String.valueOf(count));
                break;
            case 4:
                laughCount.setText(String.valueOf(count));
                break;
            case 5:
                mehCount.setText(String.valueOf(count));
                break;
            case 6:
                sadCount.setText(String.valueOf(count));
                break;
            case 7:
                smileCount.setText(String.valueOf(count));
                break;
            case 8:
                yawnCount.setText(String.valueOf(count));
                break;
        }
    }

    private void setCrownImage() {
        Animation moodAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.mood_icon_animation);
        Animation crownAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.crown_icon_animation);

        crown.setVisibility(View.INVISIBLE);
        crown2.setVisibility(View.INVISIBLE);
        crown3.setVisibility(View.INVISIBLE);
        crown4.setVisibility(View.INVISIBLE);
        crown5.setVisibility(View.INVISIBLE);
        crown6.setVisibility(View.INVISIBLE);
        crown7.setVisibility(View.INVISIBLE);
        crown8.setVisibility(View.INVISIBLE);
        crown9.setVisibility(View.INVISIBLE);

        crown.clearAnimation();
        crown2.clearAnimation();
        crown3.clearAnimation();
        crown4.clearAnimation();
        crown5.clearAnimation();
        crown6.clearAnimation();
        crown7.clearAnimation();
        crown8.clearAnimation();
        crown9.clearAnimation();

        angryImageView.clearAnimation();
        coolImageView.clearAnimation();
        cryingImageView.clearAnimation();
        illImageView.clearAnimation();
        laughImageView.clearAnimation();
        mehImageView.clearAnimation();
        sadImageView.clearAnimation();
        smileImageView.clearAnimation();
        yawnImageView.clearAnimation();

        if(maxMoodIndex == -1) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }

        switch(maxMoodIndex) {
            case 0:
                crown.setVisibility(View.VISIBLE);
                angryImageView.startAnimation(moodAnimation);
                crown.startAnimation(crownAnimation);

                moodTextView.setText("'화남'");
                moodTextView.setTextColor(getResources().getColor(R.color.red));
                break;
            case 1:
                crown2.setVisibility(View.VISIBLE);
                coolImageView.startAnimation(moodAnimation);
                crown2.startAnimation(crownAnimation);

                moodTextView.setText("'쿨'");
                moodTextView.setTextColor(getResources().getColor(R.color.blue));
                break;
            case 2:
                crown3.setVisibility(View.VISIBLE);
                cryingImageView.startAnimation(moodAnimation);
                crown3.startAnimation(crownAnimation);

                moodTextView.setText("'슬픔'");
                moodTextView.setTextColor(getResources().getColor(R.color.skyblue));
                break;
            case 3:
                crown4.setVisibility(View.VISIBLE);
                illImageView.startAnimation(moodAnimation);
                crown4.startAnimation(crownAnimation);

                moodTextView.setText("'아픔'");
                moodTextView.setTextColor(getResources().getColor(R.color.lightgreen));
                break;
            case 4:
                crown5.setVisibility(View.VISIBLE);
                laughImageView.startAnimation(moodAnimation);
                crown5.startAnimation(crownAnimation);

                moodTextView.setText("'웃음'");
                moodTextView.setTextColor(getResources().getColor(R.color.yellow));
                break;
            case 5:
                crown6.setVisibility(View.VISIBLE);
                mehImageView.startAnimation(moodAnimation);
                crown6.startAnimation(crownAnimation);

                moodTextView.setText("'보통'");
                moodTextView.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 6:
                crown7.setVisibility(View.VISIBLE);
                sadImageView.startAnimation(moodAnimation);
                crown7.startAnimation(crownAnimation);

                moodTextView.setText("'나쁨'");
                moodTextView.setTextColor(getResources().getColor(R.color.black));
                break;
            case 7:
                crown8.setVisibility(View.VISIBLE);
                smileImageView.startAnimation(moodAnimation);
                crown8.startAnimation(crownAnimation);

                moodTextView.setText("'좋음'");
                moodTextView.setTextColor(getResources().getColor(R.color.orange));
                break;
            case 8:
                crown9.setVisibility(View.VISIBLE);
                yawnImageView.startAnimation(moodAnimation);
                crown9.startAnimation(crownAnimation);

                moodTextView.setText("'피곤'");
                moodTextView.setTextColor(getResources().getColor(R.color.pink));
                break;
        }
    }

    private void addColor(int moodIndex) {
        switch(moodIndex) {
            case 0:
                colors.add(getResources().getColor(R.color.pastel_red));
                break;
            case 1:
                colors.add(getResources().getColor(R.color.pastel_blue));
                break;
            case 2:
                colors.add(getResources().getColor(R.color.pastel_skyblue));
                break;
            case 3:
                colors.add(getResources().getColor(R.color.pastel_green));
                break;
            case 4:
                colors.add(getResources().getColor(R.color.pastel_yellow));
                break;
            case 5:
                colors.add(getResources().getColor(R.color.pastel_gray));
                break;
            case 6:
                colors.add(getResources().getColor(R.color.pastel_black));
                break;
            case 7:
                colors.add(getResources().getColor(R.color.pastel_orange));
                break;
            case 8:
                colors.add(getResources().getColor(R.color.pastel_pink));
                break;
        }
    }

    private Typeface getCurTypeFace() {
        Typeface typeface = null;

        switch(curFontIndex) {
            case 0:
                typeface = Typeface.createFromAsset(context.getAssets(), "font1.ttf");
                break;
            case 1:
                typeface = Typeface.createFromAsset(context.getAssets(), "font2.ttf");
                break;
            case 2:
                typeface = Typeface.createFromAsset(context.getAssets(), "font3.ttf");
                break;
            case 3:
                typeface = Typeface.createFromAsset(context.getAssets(), "font4.ttf");
                break;
            case 4:
                typeface = Typeface.createFromAsset(context.getAssets(), "font5.ttf");
                break;
            default:
                typeface = Typeface.createFromAsset(context.getAssets(), "font1.ttf");
                break;
        }

        return typeface;
    }

    private Drawable resizeDrawable(int res) {
        BitmapDrawable drawable = (BitmapDrawable)getResources().getDrawable(res);
        Bitmap bitamp = drawable.getBitmap();
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitamp, 55, 55, true);
        Drawable newDrawable = new BitmapDrawable(newBitmap);

        return newDrawable;
    }
}
