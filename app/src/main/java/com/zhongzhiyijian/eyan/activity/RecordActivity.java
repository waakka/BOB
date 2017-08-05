package com.zhongzhiyijian.eyan.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.entity.Recorder;
import com.zhongzhiyijian.eyan.net.Urls;
import com.zhongzhiyijian.eyan.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

import static com.zhongzhiyijian.eyan.R.id.chart;

public class RecordActivity extends BaseActivity {

    private ImageButton ibClose;

    private ImageView ivLeft,ivRight;
    private TextView tvBtns;
    private TextView tvTitle;

    private LineChartView chartView;
    private LineChartData data;

    private int year,month,day;
    private int curYear,curMonth,curDay;

    private Calendar calendar;
    private long time;
    private int dayCount;

    private TextView tvAll;
    private String allStr;
    private String ljStr = "";
    private String pjStr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        calendar = Calendar.getInstance();

        initViews();
        initEvent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        curYear = year;
        curMonth = month;
        curDay = day;
        tvTitle.setText(year + "-" + month);
        dayCount = calendar.getActualMaximum(Calendar.DATE);
        time = calendar.getTimeInMillis()/1000;
        setAllTextview("0%","0","0",0,0);
        getData(time);
    }


    private void initViews() {
        ibClose = (ImageButton) findViewById(R.id.ib_close);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivRight = (ImageView) findViewById(R.id.iv_right);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvBtns = (TextView) findViewById(R.id.tv_to_btns);
        chartView = (LineChartView) findViewById(chart);


        tvAll = (TextView) findViewById(R.id.tv_all);
    }

    private void initEvent() {
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBtns.setVisibility(View.GONE);
                ivLeft.setVisibility(View.VISIBLE);
                ivRight.setVisibility(View.VISIBLE);
            }
        });
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousData();
            }
        });
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextData();
            }
        });
    }

    private void previousData() {
        if (month - 1 <= 0){
            year -= 1;
            month = 12;
        }else{
            month -= 1;
        }
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month -1);
        dayCount = calendar.getActualMaximum(Calendar.DATE);
        time = calendar.getTimeInMillis()/1000;
        if (year == curYear && month == curMonth){
            day = curDay;
        }else{
            day = dayCount;
        }
        getData(time);
    }

    private void nextData() {
        if (year>=curYear&&month>=curMonth){
            showToast("已经是最近一次的统计数据");
            year = curYear;
            month = curMonth;
            return;
        }
        if (month + 1 > 12){
            year += 1;
            month = 1;
        }else{
            month += 1;
        }
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month -1);
        dayCount = calendar.getActualMaximum(Calendar.DATE);
        time = calendar.getTimeInMillis()/1000;
        if (year == curYear && month == curMonth){
            day = curDay;
        }else{
            day = dayCount;
        }
        getData(time);
    }

    private void generateData(Recorder recorder) {

        List<Recorder.ListBean> list = recorder.getList();
        List<Line> lines = new ArrayList<Line>();

        List<PointValue> values = new ArrayList<>();
        List<PointValue> badValue = new ArrayList<>();
//        Logger.e("当月天数：" + dayCount);
        for (int j = 1; j <= day; j++) {
            String dayStr = "";
            String monthStr = "";
            String date = "";
            if (j < 10){
                dayStr = "0" + j;
            }else{
                dayStr = "" + j;
            }
            if (month < 10){
                monthStr = "0" + month;
            }else{
                monthStr = "" + month;
            }
            date = year + "" + monthStr + dayStr;
            PointValue pv = new PointValue(j,0);
            if(list != null){
                for (Recorder.ListBean lb :list){
                    String lbd = lb.getDate() + "";
                    float duration = lb.getDuration();
                    duration = duration/60/60;
                    if (lbd.equals(date)){
                        pv = new PointValue(j,duration);
                        showLog(date + "发现有数据，数据为 ：" + duration + "小时");

                    }
                }
            }
            values.add(pv);
        }
        for (int j = 1; j <= dayCount; j++){
            badValue.add(new PointValue(j,24));
        }

        Line line = new Line(values);
        Line line2 = new Line(badValue);

        //线条颜色
//        line.setColor(ChartUtils.COLORS[0]);
        line.setColor(Color.parseColor("#00d1db"));
        line2.setColor(Color.parseColor("#00000000"));
        //线条粗细
        line.setStrokeWidth(2);
        //原点形状
        line.setShape(ValueShape.CIRCLE);
        //原点大小
        line.setPointRadius(3);
        //是否填满区域
        line.setFilled(false);
        //是否有标签
        line.setHasLabels(true);
        //标签是否只有选中时才展示
        line.setHasLabelsOnlyForSelected(true);
        //是否有连线
        line.setHasLines(true);
        //是否有点
        line.setHasPoints(true);
        lines.add(line);
        lines.add(line2);




        for (int i = 0 ; i <= 24 ; i++){
            badValue.add(new PointValue(i,i));
        }



        data = new LineChartData(lines);

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
//        axisX.setName("日期");
//        axisY.setName("时长(小时)");

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chartView.setLineChartData(data);

        chartView.setViewportCalculationEnabled(false);
        chartView.setZoomEnabled(true);
        chartView.setOnValueTouchListener(new ValueTouchListener());
    }


    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            int date = (int) value.getX();
            float count = (float) value.getY();
            showToast("本月" + date+"日使用时长为：" + TimeUtil.getHourStrFromInt(mContext,count));
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    private void getData(long time){
        Logger.e("请求的时间戳为-->" + time);
        pd.show();
        RequestParams params = new RequestParams(Urls.getEquipmentRecord);
        params.addParameter("token",user.getToken());
        params.addParameter("sid",user.getSid());
        params.addParameter("time",time);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e("日期" + year + "-" + month + "的数据为：" +result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
                        Gson gson = new Gson();
                        Recorder recorder = gson.fromJson(result, Recorder.class);
                        if(recorder.getLj() > 0 ){
                            generateData(recorder);
                            setAllTextview(recorder.getPm()+"%",recorder.getLx()+"",recorder.getLj()+""
                                    ,recorder.getPjsc(),recorder.getLjsc());
                        }else{
                            showToast("无统计数据！");
                            generateData(recorder);
                        }
                        tvTitle.setText(year + "-" + month);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e(ex.getMessage());
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                if (pd.isShowing()){
                    pd.dismiss();
                }
            }
        });
    }



    private void setAllTextview(String tvpm,String tvlx,String tvlj,int pj,int lj){
        pj /= 60;
        lj /= 60;
        if (pj > 60){
            pjStr = "<font color='#FF0000'>" + pj/60 + "</font>" + getString(R.string.hour) + "<font color='#FF0000'>" + pj%60 + "</font>" + getString(R.string.min);
            showLog("小时分钟");
        }else{
            pjStr = "<font color='#FF0000'>" + pj + "</font>" + getString(R.string.min);
            showLog("分钟");
        }

        if (lj > 60){
            ljStr = "<font color='#FF0000'>" + lj/60 + "</font>" + getString(R.string.hour) + "<font color='#FF0000'>" + lj%60 + "</font>" + getString(R.string.min);
            showLog("小时分钟");
        }else{
            ljStr = "<font color='#FF0000'>" + lj + "</font>" + getString(R.string.min);
            showLog("分钟");
        }
        allStr = getString(R.string.record1) + "<font color='#FF0000'>" + tvpm + "</font>" + getString(R.string.record2)
                + getString(R.string.record3) + "<font color='#FF0000'>" + tvlx + "</font>" + getString(R.string.record4) + "<font color='#FF0000'>" + tvlj + "</font>" + getString(R.string.record7)
                + getString(R.string.record5) + ljStr
                + getString(R.string.record6) + pjStr;
        tvAll.setText(Html.fromHtml(allStr));
    }

}
