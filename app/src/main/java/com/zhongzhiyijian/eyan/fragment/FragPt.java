package com.zhongzhiyijian.eyan.fragment;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.activity.DeviceDetailActivity;
import com.zhongzhiyijian.eyan.base.BaseFragment;
import com.zhongzhiyijian.eyan.entity.PTStatus;
import com.zhongzhiyijian.eyan.entity.PTStatusUtil;
import com.zhongzhiyijian.eyan.user.UserPersist;
import com.zhongzhiyijian.eyan.util.DataUtil;
import com.zhongzhiyijian.eyan.util.MsgUtil;
import com.zhongzhiyijian.eyan.util.TimeUtil;
import com.zhongzhiyijian.eyan.widget.KnobView;


public class FragPt extends BaseFragment implements OnCheckedChangeListener,OnClickListener{

	private View view;

	private PTStatus ptStatus;
	private PTStatusUtil statusUtil;

	public static final int TYPE_ZHENJIU = 1;
	public static final int TYPE_ANMO = 2;
	public static final int TYPE_LILIAO = 3;
	public static final int TYPE_YUELIAO = 4;
	/**
	 * 当前选中的type
	 */
	private int curType;

	private RadioGroup rgBottom;
	private RadioButton rbZhenjiu;
	private RadioButton rbAnmo;
	private RadioButton rbLiliao;
	private RadioButton rbYueliao;

	private TextView tvType;
	private TextView tvIntensity;
	private TextView tvIntensityStr;
	private ImageButton ibTypeStatus;
	private ImageButton ibPlus;
	private ImageButton ibSubtract;

	private KnobView knobView;
	private TextView tvTime;
	private Button btnClock;
	private int time = 0;
	private int zhenjiuTime;
	private int anmoTime;
	private int liliaoTime;
	private int yueliaoTime;

	private ImageButton ibS1;
	private ImageButton ibS3;
	private ImageButton ibS5;
	private ImageButton ibS7;
	private ImageButton ibS9;
	private ImageButton ibS11;
	private LinearLayout llS1;
	private LinearLayout llS3;
	private LinearLayout llS5;
	private LinearLayout llS7;
	private LinearLayout llS9;
	private LinearLayout llS11;



	private ZhenjiuThread zhenjiuThread;
	private boolean zhenjiuIsRun = false;
	private AnmoThread anmoThread;
	private boolean anmoIsRun = false;
	private LiliaoThread liliaoThread;
	private boolean liliaoIsRun = false;
	private YueliaoThread yueliaoThread;
	private boolean yueliaoIsRun = false;




	private DataUtil dataUtil;

	private boolean isBobClosed = false;

	private TextView tvClockStr;


	private ProgressDialog pd;


    private InnerReceiver receiver;
    private IntentFilter filter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_pt, container,false);
		userPersist = UserPersist.getInstance();
		user = userPersist.getUser(mContext);
		statusUtil = PTStatusUtil.getInstance();
		ptStatus = statusUtil.getPTStatus(mContext);
		Logger.e("ptStatus = " + ptStatus.toString());
		initView();
		initEvent();

		zhenjiuThread = new ZhenjiuThread();
		anmoThread = new AnmoThread();
		liliaoThread = new LiliaoThread();
		yueliaoThread = new YueliaoThread();

		dataUtil = DataUtil.getInstance();


        initReceiver();

		return view;
	}

    private void initReceiver() {
        receiver = new InnerReceiver();
        filter = new IntentFilter();
        filter.addAction(WORK_TYPE_CHANGED);
        getActivity().registerReceiver(receiver,filter);
    }


    private class InnerReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(WORK_TYPE_CHANGED.equals(action)){
                //工作状态改变
                resetData();
                String type = intent.getStringExtra("type");
                int qiangdu = intent.getIntExtra("qiangdu", 0);
                int time = intent.getIntExtra("time", 0);
                if ("1".equals(type)){
                    //工作界面：搓揉  工作状态：关闭
                    curType = TYPE_ZHENJIU;
                    ptStatus.setStatusZhenJiuIsOpen(true);
                    ptStatus.setStatusZhenJiuClockTime(time);
                    ptStatus.setStatusZhenJiuIntensity(qiangdu);
                    ptStatus.setStatusZhenJiuIsClock(false);
                    rbZhenjiu.performClick();
                }else if("f1".equals(type)){
                    //工作界面：搓揉  工作状态：打开
                    curType = TYPE_ZHENJIU;
                    ptStatus.setStatusZhenJiuIsOpen(true);
                    ptStatus.setStatusZhenJiuClockTime(time);
                    ptStatus.setStatusZhenJiuIntensity(qiangdu);
                    ptStatus.setStatusZhenJiuIsClock(true);
                    rbZhenjiu.performClick();
                }else if ("2".equals(type)){
                    //工作界面：推压  工作状态：关闭
                    curType = TYPE_ANMO;
                    ptStatus.setStatusAnMoIsOpen(true);
                    ptStatus.setStatusAnMoClockTime(time);
                    ptStatus.setStatusAnMoIntensity(qiangdu);
                    ptStatus.setStatusAnMoIsClock(false);
                    rbAnmo.performClick();
                }else if("f2".equals(type)){
                    //工作界面：推压  工作状态：打开
                    curType = TYPE_ANMO;
                    ptStatus.setStatusAnMoIsOpen(true);
                    ptStatus.setStatusAnMoClockTime(time);
                    ptStatus.setStatusAnMoIntensity(qiangdu);
                    ptStatus.setStatusAnMoIsClock(true);
                    rbAnmo.performClick();
                }else if ("3".equals(type)){
                    //工作界面：叩击  工作状态：关闭
                    curType = TYPE_LILIAO;
                    ptStatus.setStatusLiLiaoIsOpen(true);
                    ptStatus.setStatusLiLiaoClockTime(time);
                    ptStatus.setStatusLiLiaoIntensity(qiangdu);
                    ptStatus.setStatusLiLiaoIsClock(false);
                    rbLiliao.performClick();
                }else if("f3".equals(type)){
                    //工作界面：叩击  工作状态：打开
                    curType = TYPE_LILIAO;
                    ptStatus.setStatusLiLiaoIsOpen(true);
                    ptStatus.setStatusLiLiaoClockTime(time);
                    ptStatus.setStatusLiLiaoIntensity(qiangdu);
                    ptStatus.setStatusLiLiaoIsClock(true);
                    rbLiliao.performClick();
                }else if ("4".equals(type)){
                    //工作界面：自动  工作状态：关闭
                    curType = TYPE_YUELIAO;
                    ptStatus.setStatusYueLiaoIsOpen(true);
                    ptStatus.setStatusYueLiaoClockTime(time);
                    ptStatus.setStatusYueLiaoIntensity(qiangdu);
                    ptStatus.setStatusYueLiaoIsClock(false);
                    rbYueliao.performClick();
                }else if("f4".equals(type)){
                    //工作界面：自动  工作状态：打开
                    curType = TYPE_YUELIAO;
                    ptStatus.setStatusYueLiaoIsOpen(true);
                    ptStatus.setStatusYueLiaoClockTime(time);
                    ptStatus.setStatusYueLiaoIntensity(qiangdu);
                    ptStatus.setStatusYueLiaoIsClock(true);
                    rbYueliao.performClick();
                }
                statusUtil.setPTStatus(mContext,ptStatus);
                //更新界面
                reSetLayout(curType);
            }
        }
	}



    /**
     * 所有状态及计时归零
     */
    private void resetData() {
        ptStatus.setStatusZhenJiuIsOpen(false);
        ptStatus.setStatusAnMoIsOpen(false);
        ptStatus.setStatusLiLiaoIsOpen(false);
        ptStatus.setStatusYueLiaoIsOpen(false);

        ptStatus.setStatusZhenJiuIsClock(false);
        ptStatus.setStatusAnMoIsClock(false);
        ptStatus.setStatusLiLiaoIsClock(false);
        ptStatus.setStatusYueLiaoIsClock(false);
    }

    @Override
	public void onResume() {
		super.onResume();
		userPersist = UserPersist.getInstance();
		user = userPersist.getUser(mContext);

		tvIntensityStr.setText(getString(R.string.intensity));
		tvClockStr.setText(getString(R.string.clock));
		rbZhenjiu.setText(getString(R.string.rb_zhenjiu));
		rbAnmo.setText(getString(R.string.rb_anmo));
		rbLiliao.setText(getString(R.string.rb_liliao));
		rbYueliao.setText(getString(R.string.rb_yueliao));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //查询当前工作状态
        sengMsgToDevice(MsgUtil.getCurWorkType());

	}

	private void initView() {
		rgBottom = (RadioGroup) view.findViewById(R.id.rg_pt);
		rbZhenjiu = (RadioButton) view.findViewById(R.id.rb_zhenjiu);
		rbAnmo = (RadioButton) view.findViewById(R.id.rb_anmo);
		rbLiliao = (RadioButton) view.findViewById(R.id.rb_liliao);
		rbYueliao = (RadioButton) view.findViewById(R.id.rb_yueliao);

		tvType = (TextView) view.findViewById(R.id.tv_type);
		tvIntensity = (TextView) view.findViewById(R.id.tv_intensity);
		tvIntensityStr = (TextView) view.findViewById(R.id.tv_intensity_str);
		ibTypeStatus = (ImageButton) view.findViewById(R.id.ib_type_status);
		ibPlus = (ImageButton) view.findViewById(R.id.ib_plus);
		ibSubtract = (ImageButton) view.findViewById(R.id.ib_subtract);

		ibS1 = (ImageButton) view.findViewById(R.id.ib_s1);
		ibS3 = (ImageButton) view.findViewById(R.id.ib_s3);
		ibS5 = (ImageButton) view.findViewById(R.id.ib_s5);
		ibS7 = (ImageButton) view.findViewById(R.id.ib_s7);
		ibS9 = (ImageButton) view.findViewById(R.id.ib_s9);
		ibS11 = (ImageButton) view.findViewById(R.id.ib_s11);

		llS1 = (LinearLayout) view.findViewById(R.id.ll_s1);
		llS3 = (LinearLayout) view.findViewById(R.id.ll_s3);
		llS5 = (LinearLayout) view.findViewById(R.id.ll_s5);
		llS7 = (LinearLayout) view.findViewById(R.id.ll_s7);
		llS9 = (LinearLayout) view.findViewById(R.id.ll_s9);
		llS11 = (LinearLayout) view.findViewById(R.id.ll_s11);

		btnClock = (Button) view.findViewById(R.id.btn_clock);
		tvTime = (TextView) view.findViewById(R.id.tv_time);
		knobView = (KnobView) view.findViewById(R.id.knobview);
		knobView.setMax(15*60+1);
		knobView.setProgress(0);

		tvClockStr = (TextView) view.findViewById(R.id.tv_clock_str);
	}

	private void initEvent() {
		rgBottom.setOnCheckedChangeListener(this);
		ibTypeStatus.setOnClickListener(this);
		ibPlus.setOnClickListener(this);
		ibSubtract.setOnClickListener(this);

		llS1.setOnClickListener(this);
		llS3.setOnClickListener(this);
		llS5.setOnClickListener(this);
		llS7.setOnClickListener(this);
		llS9.setOnClickListener(this);
		llS11.setOnClickListener(this);

		btnClock.setOnClickListener(this);

		knobView.setProgressChangeListener(new KnobView.OnProgressChangeListener() {
			@Override
			public void onProgressChanged(boolean fromUser, int progress) {
				tvTime.setText(TimeUtil.getStrFromInt(mContext,progress));
			}
			@Override
			public void onStartTrackingTouch(KnobView view, int progress) {
			}
			@Override
			public void onStopTrackingTouch(KnobView view, int progress) {
				time = progress;
				tvTime.setText(TimeUtil.getStrFromInt(mContext,time));
				switch (curType){
					case TYPE_ZHENJIU:
						ptStatus.setStatusZhenJiuClockTime(time);
						break;
					case TYPE_ANMO:
						ptStatus.setStatusAnMoClockTime(time);
						break;
					case TYPE_LILIAO:
						ptStatus.setStatusLiLiaoClockTime(time);
						break;
					case TYPE_YUELIAO:
						ptStatus.setStatusYueLiaoClockTime(time);
						break;
				}
				statusUtil.setPTStatus(mContext,ptStatus);
			}
		});

	}

	/**
	 * 根据当前type刷新界面
	 * 强度
	 * 状态按钮
	 * 加减按钮
	 * @param type
	 */
	private void reSetLayout(int type){
		if(isBobClosed){
			return;
		}
//		Logger.e("刷新界面,type=" + type);
		curType = type;
		reSetIntensity();
		switch (type) {
			case TYPE_ZHENJIU:
				tvType.setText(getString(R.string.unit_zhenjiu));
				tvIntensity.setText(ptStatus.getStatusZhenJiuIntensity()+"  级");
				if(ptStatus.isStatusZhenJiuIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					ibPlus.setImageResource(R.mipmap.btn_jia_checked);
					ibSubtract.setImageResource(R.mipmap.btn_jian_checked);
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					knobView.setTouchable(true);
					btnClock.setClickable(true);
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ibPlus.setImageResource(R.mipmap.btn_jia_nomal);
					ibSubtract.setImageResource(R.mipmap.btn_jian_nomal);
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					knobView.setTouchable(false);
					btnClock.setClickable(false);
                    btnClock.setText("关闭");
				}
				time = ptStatus.getStatusZhenJiuClockTime();
				tvTime.setText(TimeUtil.getStrFromInt(mContext,time));
//				tvTime.setText(time/60 + getString(R.string.min));
				knobView.setProgress(time);
				switch (ptStatus.getStatusZhenJiuIntensity()) {
					case 1:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS1.setImageResource(R.mipmap.y1);
						}else{
							ibS1.setImageResource(R.mipmap.l1);
						}
						break;
					case 2:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS3.setImageResource(R.mipmap.y3);
						}else{
							ibS3.setImageResource(R.mipmap.l3);
						}
						break;
					case 3:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS5.setImageResource(R.mipmap.y5);
						}else{
							ibS5.setImageResource(R.mipmap.l5);
						}
						break;
					case 4:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS7.setImageResource(R.mipmap.y7);
						}else{
							ibS7.setImageResource(R.mipmap.l7);
						}
						break;
					case 5:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS9.setImageResource(R.mipmap.y9);
						}else{
							ibS9.setImageResource(R.mipmap.l9);
						}
						break;
					case 6:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS11.setImageResource(R.mipmap.y11);
						}else{
							ibS11.setImageResource(R.mipmap.l11);
						}
						break;

					default:
						break;
				}
				break;
			case TYPE_ANMO:
				tvType.setText(getString(R.string.unit_anmo));
				tvIntensity.setText(ptStatus.getStatusAnMoIntensity()+"  级");
				if(ptStatus.isStatusAnMoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					ibPlus.setImageResource(R.mipmap.btn_jia_checked);
					ibSubtract.setImageResource(R.mipmap.btn_jian_checked);
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					knobView.setTouchable(true);
					btnClock.setClickable(true);
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ibPlus.setImageResource(R.mipmap.btn_jia_nomal);
					ibSubtract.setImageResource(R.mipmap.btn_jian_nomal);
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					knobView.setTouchable(false);
					btnClock.setClickable(false);
                    btnClock.setText("关闭");
				}
				time = ptStatus.getStatusAnMoClockTime();
				tvTime.setText(TimeUtil.getStrFromInt(mContext,time));
//				tvTime.setText(time/60 + getString(R.string.min));
				knobView.setProgress(time);
				switch (ptStatus.getStatusAnMoIntensity()) {
					case 1:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS1.setImageResource(R.mipmap.y1);
						}else{
							ibS1.setImageResource(R.mipmap.l1);
						}
						break;
					case 2:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS3.setImageResource(R.mipmap.y3);
						}else{
							ibS3.setImageResource(R.mipmap.l3);
						}
						break;
					case 3:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS5.setImageResource(R.mipmap.y5);
						}else{
							ibS5.setImageResource(R.mipmap.l5);
						}
						break;
					case 4:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS7.setImageResource(R.mipmap.y7);
						}else{
							ibS7.setImageResource(R.mipmap.l7);
						}
						break;
					case 5:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS9.setImageResource(R.mipmap.y9);
						}else{
							ibS9.setImageResource(R.mipmap.l9);
						}
						break;
					case 6:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS11.setImageResource(R.mipmap.y11);
						}else{
							ibS11.setImageResource(R.mipmap.l11);
						}
						break;

					default:
						break;
				}
				break;
			case TYPE_LILIAO:
				tvType.setText(getString(R.string.unit_liliao));
				tvIntensity.setText(ptStatus.getStatusLiLiaoIntensity()+"  级");
				if(ptStatus.isStatusLiLiaoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					ibPlus.setImageResource(R.mipmap.btn_jia_checked);
					ibSubtract.setImageResource(R.mipmap.btn_jian_checked);
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					knobView.setTouchable(true);
					btnClock.setClickable(true);
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ibPlus.setImageResource(R.mipmap.btn_jia_nomal);
					ibSubtract.setImageResource(R.mipmap.btn_jian_nomal);
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					knobView.setTouchable(false);
					btnClock.setClickable(false);
                    btnClock.setText("关闭");
				}
				time = ptStatus.getStatusLiLiaoClockTime();
				tvTime.setText(TimeUtil.getStrFromInt(mContext,time));
//				tvTime.setText(time/60 + getString(R.string.min));
				knobView.setProgress(time);
				switch (ptStatus.getStatusLiLiaoIntensity()) {
					case 1:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS1.setImageResource(R.mipmap.y1);
						}else{
							ibS1.setImageResource(R.mipmap.l1);
						}
						break;
					case 2:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS3.setImageResource(R.mipmap.y3);
						}else{
							ibS3.setImageResource(R.mipmap.l3);
						}
						break;
					case 3:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS5.setImageResource(R.mipmap.y5);
						}else{
							ibS5.setImageResource(R.mipmap.l5);
						}
						break;
					case 4:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS7.setImageResource(R.mipmap.y7);
						}else{
							ibS7.setImageResource(R.mipmap.l7);
						}
						break;
					case 5:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS9.setImageResource(R.mipmap.y9);
						}else{
							ibS9.setImageResource(R.mipmap.l9);
						}
						break;
					case 6:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS11.setImageResource(R.mipmap.y11);
						}else{
							ibS11.setImageResource(R.mipmap.l11);
						}
						break;

					default:
						break;
				}
				break;
			case TYPE_YUELIAO:
				tvType.setText(getString(R.string.unit_yueliao));
				tvIntensity.setText(ptStatus.getStatusYueLiaoIntensity()+"  级");
				if(ptStatus.isStatusYueLiaoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					ibPlus.setImageResource(R.mipmap.btn_jia_checked);
					ibSubtract.setImageResource(R.mipmap.btn_jian_checked);
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					knobView.setTouchable(true);
					btnClock.setClickable(true);
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ibPlus.setImageResource(R.mipmap.btn_jia_nomal);
					ibSubtract.setImageResource(R.mipmap.btn_jian_nomal);
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					knobView.setTouchable(false);
					btnClock.setClickable(false);
                    btnClock.setText("关闭");
				}
				time = ptStatus.getStatusYueLiaoClockTime();
				tvTime.setText(TimeUtil.getStrFromInt(mContext,time));
//				tvTime.setText(time/60 + getString(R.string.min));
				knobView.setProgress(time);
				//判断强度并高亮当前频率
				switch (ptStatus.getStatusYueLiaoIntensity()) {
					case 1:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS1.setImageResource(R.mipmap.y1);
						}else{
							ibS1.setImageResource(R.mipmap.l1);
						}
						break;
					case 2:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS3.setImageResource(R.mipmap.y3);
						}else{
							ibS3.setImageResource(R.mipmap.l3);
						}
						break;
					case 3:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS5.setImageResource(R.mipmap.y5);
						}else{
							ibS5.setImageResource(R.mipmap.l5);
						}
						break;
					case 4:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS7.setImageResource(R.mipmap.y7);
						}else{
							ibS7.setImageResource(R.mipmap.l7);
						}
						break;
					case 5:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS9.setImageResource(R.mipmap.y9);
						}else{
							ibS9.setImageResource(R.mipmap.l9);
						}
						break;
					case 6:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS11.setImageResource(R.mipmap.y11);
						}else{
							ibS11.setImageResource(R.mipmap.l11);
						}
						break;

					default:
						break;
				}
				break;

			default:
				break;
		}
	}

	/**
	 * 重置强度等级背景图片
	 */
		private void reSetIntensity() {
		ibS1.setImageResource(R.mipmap.g1);
		ibS3.setImageResource(R.mipmap.g3);
		ibS5.setImageResource(R.mipmap.g5);
		ibS7.setImageResource(R.mipmap.g7);
		ibS9.setImageResource(R.mipmap.g9);
		ibS11.setImageResource(R.mipmap.g11);

	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.rb_zhenjiu:
				reSetLayout(TYPE_ZHENJIU);
				break;
			case R.id.rb_anmo:
				reSetLayout(TYPE_ANMO);
				break;
			case R.id.rb_liliao:
				reSetLayout(TYPE_LILIAO);
				break;
			case R.id.rb_yueliao:
				reSetLayout(TYPE_YUELIAO);
				break;

			default:
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ib_type_status://设置当前单元开关状态
				setTypeButton();
				break;
			case R.id.ib_plus://增加强度

				plusIntensity();
				break;
			case R.id.ib_subtract://减少强度

				subtratIntensity();
				break;

			case R.id.ll_s1:
				setIntensity(1);
				break;
			case R.id.ll_s3:
				setIntensity(2);
				break;
			case R.id.ll_s5:
				setIntensity(3);
				break;
			case R.id.ll_s7:
				setIntensity(4);
				break;
			case R.id.ll_s9:
				setIntensity(5);
				break;
			case R.id.ll_s11:
				setIntensity(6);
				break;




			case R.id.btn_clock:
				setClock();
				break;




			default:
				break;
		}
	}

	/**
	 * 设置定时器
	 */
	private void setClock() {
		switch (curType){
			case TYPE_ZHENJIU:
				if (ptStatus.isStatusZhenJiuIsClock()){
                    sengMsgToDevice(MsgUtil.closeWork("01",1));

                    showToast(getString(R.string.closeclock));
                    ptStatus.setStatusZhenJiuIsClock(false);
                    btnClock.setText(getString(R.string.btn_open));
                    btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
                    if (!zhenjiuThread.isInterrupted()){
                        zhenjiuIsRun = false;
                    }
//					Logger.e("close clock");
					dataUtil.stopData();
					knobView.setTouchable(true);
				}else{
                    sengMsgToDevice(MsgUtil.openWork(
                            "01",ptStatus.getStatusZhenJiuIntensity(),time,MsgUtil.TYPE_ZHENJIU));

                    btnClock.setText(getString(R.string.btn_close));
                    btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
                    ptStatus.setStatusZhenJiuIsClock(true);
                    ptStatus.setStatusZhenJiuClockTime(time);
                    showToast(getString(R.string.startclock)+getString(R.string.rb_zhenjiu) + TimeUtil.getStrFromInt(mContext,time) + getString(R.string.clockend));
                    zhenjiuIsRun = true;
                    zhenjiuThread = new ZhenjiuThread();
                    zhenjiuThread.start();
                    zhenjiuTime = time+1;
					knobView.setTouchable(false);
					//开始本地数据库记录数据
					dataUtil.startData("1",ptStatus.getStatusZhenJiuIntensity(),user.getToken(),user.getSid(),"-1");
				}
				break;
			case TYPE_ANMO:
				if (ptStatus.isStatusAnMoIsClock()){
					showToast(getString(R.string.closeclock));
					ptStatus.setStatusAnMoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!anmoThread.isInterrupted()){
						anmoIsRun = false;
					}
					sengMsgToDevice(MsgUtil.closeWork("02",2));
//					Logger.e("close clock");
					dataUtil.stopData();
					knobView.setTouchable(true);
				}else{
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					ptStatus.setStatusAnMoIsClock(true);
					ptStatus.setStatusAnMoClockTime(time);
					showToast(getString(R.string.startclock)+getString(R.string.rb_anmo) +  TimeUtil.getStrFromInt(mContext,time) + getString(R.string.clockend));
					anmoIsRun = true;
					anmoThread = new AnmoThread();
					anmoThread.start();
					anmoTime = time+1;
					sengMsgToDevice(MsgUtil.openWork(
							"02",ptStatus.getStatusAnMoIntensity(),time,MsgUtil.TYPE_ANMO));
					knobView.setTouchable(false);
					//开始本地数据库记录数据
					dataUtil.startData("2",ptStatus.getStatusAnMoIntensity(),user.getToken(),user.getSid(),"-1");
				}
				break;
			case TYPE_LILIAO:
				if (ptStatus.isStatusLiLiaoIsClock()){
					showToast(getString(R.string.closeclock));
					ptStatus.setStatusLiLiaoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!liliaoThread.isInterrupted()){
						liliaoIsRun = false;
					}
					sengMsgToDevice(MsgUtil.closeWork("03",3));
//					Logger.e("close clock");
					dataUtil.stopData();
					knobView.setTouchable(true);
				}else{
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					ptStatus.setStatusLiLiaoIsClock(true);
					ptStatus.setStatusLiLiaoClockTime(time);
					showToast(getString(R.string.startclock)+getString(R.string.rb_liliao) +  TimeUtil.getStrFromInt(mContext,time) + getString(R.string.clockend));
					liliaoIsRun = true;
					liliaoThread = new LiliaoThread();
					liliaoThread.start();
					liliaoTime = time+1;
					sengMsgToDevice(MsgUtil.openWork(
							"03",ptStatus.getStatusLiLiaoIntensity(),time,MsgUtil.TYPE_LILIAO));
					knobView.setTouchable(false);
					//开始本地数据库记录数据
					dataUtil.startData("3",ptStatus.getStatusLiLiaoIntensity(),user.getToken(),user.getSid(),"-1");
				}
				break;
			case TYPE_YUELIAO:
				if (ptStatus.isStatusYueLiaoIsClock()){
					showToast(getString(R.string.closeclock));
					ptStatus.setStatusYueLiaoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!yueliaoThread.isInterrupted()){
						yueliaoIsRun = false;
					}
					sengMsgToDevice(MsgUtil.closeWork("04",4));
//					Logger.e("close clock");
					dataUtil.stopData();
					knobView.setTouchable(true);
				}else{
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					ptStatus.setStatusYueLiaoIsClock(true);
					ptStatus.setStatusYueLiaoClockTime(time);
					showToast(getString(R.string.startclock)+getString(R.string.rb_yueliao) +  TimeUtil.getStrFromInt(mContext,time) + getString(R.string.clockend));
					yueliaoIsRun = true;
					yueliaoThread = new YueliaoThread();
					yueliaoThread.start();
					yueliaoTime = time+1;
					sengMsgToDevice(MsgUtil.openWork(
							"04",ptStatus.getStatusYueLiaoIntensity(),time,MsgUtil.TYPE_YUELIAO));
					knobView.setTouchable(false);
					//开始本地数据库记录数据
					dataUtil.startData("4",ptStatus.getStatusYueLiaoIntensity(),user.getToken(),user.getSid(),"-1");
				}
				break;
		}
		statusUtil.setPTStatus(mContext,ptStatus);
//		reSetLayout(curType);
		return;
	}

	/**
	 * 设置强度
	 * @param intensity
	 */
	private void setIntensity(int intensity){
		switch (curType) {
			case TYPE_ZHENJIU:
				if(ptStatus.isStatusZhenJiuIsOpen()){
					ptStatus.setStatusZhenJiuIntensity(intensity);
					statusUtil.setPTStatus(mContext, ptStatus);
                    sengMsgToDevice(MsgUtil.setIntensity("F5" , intensity));
                    if (app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON){
						dataUtil.changeStrength(intensity);
					}
					reSetLayout(TYPE_ZHENJIU);
				}
				break;
			case TYPE_ANMO:
				if(ptStatus.isStatusAnMoIsOpen()){
					ptStatus.setStatusAnMoIntensity(intensity);
					statusUtil.setPTStatus(mContext, ptStatus);
                    sengMsgToDevice(MsgUtil.setIntensity("F5" , intensity));
                    if (app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON){
						dataUtil.changeStrength(intensity);
					}
					reSetLayout(TYPE_ANMO);
				}
				break;
			case TYPE_LILIAO:
				if(ptStatus.isStatusLiLiaoIsOpen()){
					ptStatus.setStatusLiLiaoIntensity(intensity);
					statusUtil.setPTStatus(mContext, ptStatus);
                    sengMsgToDevice(MsgUtil.setIntensity("F5" , intensity));
                    if (app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON){
						dataUtil.changeStrength(intensity);
					}
					reSetLayout(TYPE_LILIAO);
				}
				break;
			case TYPE_YUELIAO:
				if(ptStatus.isStatusYueLiaoIsOpen()){
					ptStatus.setStatusYueLiaoIntensity(intensity);
					statusUtil.setPTStatus(mContext, ptStatus);
                    sengMsgToDevice(MsgUtil.setIntensity("F5" , intensity));
                    if (app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON){
						dataUtil.changeStrength(intensity);
					}
					reSetLayout(TYPE_YUELIAO);
				}
				break;

			default:
				break;
		}
	}


	/**
	 * 减少强度并刷新页面
	 */
	private void subtratIntensity() {
		int i ;
		switch (curType) {
			case TYPE_ZHENJIU:
				if(ptStatus.isStatusZhenJiuIsOpen()){
					i = ptStatus.getStatusZhenJiuIntensity();
					if(i == 1){
						showToast(mContext.getResources().getString(R.string.intensity_min));
					}else{
						i -= 1;
						ptStatus.setStatusZhenJiuIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
                        sengMsgToDevice(MsgUtil.setIntensity("F5" , i));
                        if (app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON){
							dataUtil.changeStrength(i);
						}
					}
					reSetLayout(TYPE_ZHENJIU);
				}
				break;
			case TYPE_ANMO:
				if(ptStatus.isStatusAnMoIsOpen()){
					i = ptStatus.getStatusAnMoIntensity();
					if(i == 1){
						showToast(mContext.getResources().getString(R.string.intensity_min));
					}else{
						i -= 1;
						ptStatus.setStatusAnMoIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
                        sengMsgToDevice(MsgUtil.setIntensity("F5" , i));
                        if (app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON){
							dataUtil.changeStrength(i);
						}
					}
					reSetLayout(TYPE_ANMO);
				}
				break;
			case TYPE_LILIAO:
				if(ptStatus.isStatusLiLiaoIsOpen()){
					i = ptStatus.getStatusLiLiaoIntensity();
					if(i == 1){
						showToast(mContext.getResources().getString(R.string.intensity_min));
					}else{
						i -= 1;
						ptStatus.setStatusLiLiaoIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
                        sengMsgToDevice(MsgUtil.setIntensity("F5" , i));
                        if (app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON){
							dataUtil.changeStrength(i);
						}
					}
					reSetLayout(TYPE_LILIAO);
				}
				break;
			case TYPE_YUELIAO:
				if(ptStatus.isStatusYueLiaoIsOpen()){
					i = ptStatus.getStatusYueLiaoIntensity();
					if(i == 1){
						showToast(mContext.getResources().getString(R.string.intensity_min));
					}else{
						i -= 1;
						ptStatus.setStatusYueLiaoIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
                        sengMsgToDevice(MsgUtil.setIntensity("F5" , i));
                        if (app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON){
							dataUtil.changeStrength(i);
						}
					}
					reSetLayout(TYPE_YUELIAO);
				}
				break;

			default:
				break;
		}
	}

	/**
	 * 增加强度并刷新页面
	 */
	private void plusIntensity() {
		int i ;
		switch (curType) {
			case TYPE_ZHENJIU:
				if(ptStatus.isStatusZhenJiuIsOpen()){


					i = ptStatus.getStatusZhenJiuIntensity();
					if(i == 6){
						showToast(mContext.getResources().getString(R.string.intensity_max));
					}else{
						i += 1;
						ptStatus.setStatusZhenJiuIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
                        sengMsgToDevice(MsgUtil.setIntensity("F5" , i));
                        if (app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON){
							dataUtil.changeStrength(i);
						}
					}
					reSetLayout(TYPE_ZHENJIU);
				}
				break;
			case TYPE_ANMO:
				if(ptStatus.isStatusAnMoIsOpen()){
					i = ptStatus.getStatusAnMoIntensity();
					if(i == 6){
						showToast(mContext.getResources().getString(R.string.intensity_max));
					}else{
						i += 1;
						ptStatus.setStatusAnMoIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
                        sengMsgToDevice(MsgUtil.setIntensity("F5" , i));
                        if (app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON){
							dataUtil.changeStrength(i);
						}
					}
					reSetLayout(TYPE_ANMO);
				}
				break;
			case TYPE_LILIAO:
				if(ptStatus.isStatusLiLiaoIsOpen()){
					i = ptStatus.getStatusLiLiaoIntensity();
					if(i == 6){
						showToast(mContext.getResources().getString(R.string.intensity_max));
					}else{
						i += 1;
						ptStatus.setStatusLiLiaoIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
                        sengMsgToDevice(MsgUtil.setIntensity("F5" , i));
                        if (app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON){
							dataUtil.changeStrength(i);
						}
					}
					reSetLayout(TYPE_LILIAO);
				}
				break;
			case TYPE_YUELIAO:
				if(ptStatus.isStatusYueLiaoIsOpen()){
					i = ptStatus.getStatusYueLiaoIntensity();
					if(i == 6){
						showToast(mContext.getResources().getString(R.string.intensity_max));
					}else{
						i += 1;
						ptStatus.setStatusYueLiaoIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
                        sengMsgToDevice(MsgUtil.setIntensity("F5" , i));
                        if (app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON){
							dataUtil.changeStrength(i);
						}
					}
					reSetLayout(TYPE_YUELIAO);
				}
				break;

			default:
				break;
		}
	}



	/**
	 * 根据type类型设置开关状态
	 */
	private void setTypeButton() {
		switch (curType) {
			case TYPE_ZHENJIU:
				if(ptStatus.isStatusZhenJiuIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					//关闭当前单元及定时
					ptStatus.setStatusZhenJiuIsOpen(false);
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					//发送指令关闭当前单元
					if(ptStatus.isStatusZhenJiuIsClock()){
						ptStatus.setStatusZhenJiuIsClock(false);
						sengMsgToDevice(MsgUtil.closeWork("01",1));
						Logger.e("close");
						//关闭当前定时线程
						if (!zhenjiuThread.isInterrupted()){
							zhenjiuIsRun = false;
						}
						dataUtil.stopData();
					}
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					//选中单元非当前单元时，关闭其他单元
					closeOtherMode(1);
					//设置其他单元状态为未打开
					ptStatus.setStatusZhenJiuIsOpen(true);
					ptStatus.setStatusAnMoIsOpen(false);
					ptStatus.setStatusLiLiaoIsOpen(false);
					ptStatus.setStatusYueLiaoIsOpen(false);
					//设置其他单元定时状态为关闭
					ptStatus.setStatusAnMoIsClock(false);
					ptStatus.setStatusLiLiaoIsClock(false);
					ptStatus.setStatusYueLiaoIsClock(false);
					//关闭其他单元定时器
					if (!anmoThread.isInterrupted()){
						anmoIsRun = false;
					}
					if (!liliaoThread.isInterrupted()){
						liliaoIsRun = false;
					}
					if (!yueliaoThread.isInterrupted()){
						yueliaoIsRun = false;
					}
				}
				statusUtil.setPTStatus(mContext, ptStatus);
				reSetLayout(TYPE_ZHENJIU);
				break;
			case TYPE_ANMO:
				if(ptStatus.isStatusAnMoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ptStatus.setStatusAnMoIsOpen(false);
					btnClock.setText(getString(R.string.btn_open));
					if(ptStatus.isStatusAnMoIsClock()){
						ptStatus.setStatusAnMoIsClock(false);
						sengMsgToDevice(MsgUtil.closeWork("02",2));
						Logger.e("close");
						btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
						if (!anmoThread.isInterrupted()){
							anmoIsRun = false;
						}
						dataUtil.stopData();
					}
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					//选中单元非当前单元时，关闭其他单元
					closeOtherMode(2);
					ptStatus.setStatusZhenJiuIsOpen(false);
					ptStatus.setStatusAnMoIsOpen(true);
					ptStatus.setStatusLiLiaoIsOpen(false);
					ptStatus.setStatusYueLiaoIsOpen(false);
					//设置其他单元定时状态为关闭
					ptStatus.setStatusZhenJiuIsClock(false);
					ptStatus.setStatusLiLiaoIsClock(false);
					ptStatus.setStatusYueLiaoIsClock(false);
					//关闭其他单元定时器
					if (!zhenjiuThread.isInterrupted()){
						zhenjiuIsRun = false;
					}
					if (!liliaoThread.isInterrupted()){
						liliaoIsRun = false;
					}
					if (!yueliaoThread.isInterrupted()){
						yueliaoIsRun = false;
					}
				}
				statusUtil.setPTStatus(mContext, ptStatus);
				reSetLayout(TYPE_ANMO);
				break;
			case TYPE_LILIAO:
				if(ptStatus.isStatusLiLiaoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ptStatus.setStatusLiLiaoIsOpen(false);
					btnClock.setText(getString(R.string.btn_open));
					if(ptStatus.isStatusLiLiaoIsClock()){
						ptStatus.setStatusLiLiaoIsClock(false);
						sengMsgToDevice(MsgUtil.closeWork("03",3));
						Logger.e("close");
						btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
						if (!liliaoThread.isInterrupted()){
							liliaoIsRun = false;
						}
						dataUtil.stopData();
					}
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					//选中单元非当前单元时，关闭其他单元
					closeOtherMode(3);
					ptStatus.setStatusZhenJiuIsOpen(false);
					ptStatus.setStatusAnMoIsOpen(false);
					ptStatus.setStatusLiLiaoIsOpen(true);
					ptStatus.setStatusYueLiaoIsOpen(false);
					//设置其他单元定时状态为关闭
					ptStatus.setStatusZhenJiuIsClock(false);
					ptStatus.setStatusAnMoIsClock(false);
					ptStatus.setStatusYueLiaoIsClock(false);
					//关闭其他单元定时器
					if (!anmoThread.isInterrupted()){
						anmoIsRun = false;
					}
					if (!zhenjiuThread.isInterrupted()){
						zhenjiuIsRun = false;
					}
					if (!yueliaoThread.isInterrupted()){
						yueliaoIsRun = false;
					}
				}
				statusUtil.setPTStatus(mContext, ptStatus);
				reSetLayout(TYPE_LILIAO);
				break;
			case TYPE_YUELIAO:
				if(ptStatus.isStatusYueLiaoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ptStatus.setStatusYueLiaoIsOpen(false);
					btnClock.setText(getString(R.string.btn_open));
					if(ptStatus.isStatusYueLiaoIsClock()){
						ptStatus.setStatusYueLiaoIsClock(false);
						sengMsgToDevice(MsgUtil.closeWork("04",4));
						Logger.e("close");
						btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
						if (!yueliaoThread.isInterrupted()){
							yueliaoIsRun = false;
						}
						dataUtil.stopData();
					}
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					//选中单元非当前单元时，关闭其他单元
					closeOtherMode(1);
					ptStatus.setStatusZhenJiuIsOpen(false);
					ptStatus.setStatusAnMoIsOpen(false);
					ptStatus.setStatusLiLiaoIsOpen(false);
					ptStatus.setStatusYueLiaoIsOpen(true);
					//设置其他单元定时状态为关闭
					ptStatus.setStatusZhenJiuIsClock(false);
					ptStatus.setStatusAnMoIsClock(false);
					ptStatus.setStatusLiLiaoIsClock(false);
					//关闭其他单元定时器
					if (!anmoThread.isInterrupted()){
						anmoIsRun = false;
					}
					if (!liliaoThread.isInterrupted()){
						liliaoIsRun = false;
					}
					if (!zhenjiuThread.isInterrupted()){
						zhenjiuIsRun = false;
					}
				}
				statusUtil.setPTStatus(mContext, ptStatus);
				reSetLayout(TYPE_YUELIAO);
				break;

			default:
				break;
		}
	}




	public class ZhenjiuThread extends Thread{
		@Override
		public void run() {
			while (zhenjiuIsRun){
				try {
					sleep(1000);
					Message message = new Message();
					message.what = 1;
					zhenjiuHandler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private Handler zhenjiuHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int t = ptStatus.getStatusZhenJiuClockTime();
			if(t - 1 <= 0){
				ptStatus.setStatusZhenJiuClockTime(0);
				if (!zhenjiuThread.isInterrupted()){
					zhenjiuIsRun = false;
				}
				ptStatus.setStatusZhenJiuIsClock(false);
				ptStatus.setStatusZhenJiuIsOpen(false);
				ptStatus.setStatusZhenJiuClockTime(zhenjiuTime);
				knobView.setProgress(zhenjiuTime);
				statusUtil.setPTStatus(mContext,ptStatus);
				showToast("计时结束，停止针灸");
				tvTime.setText("0" + getString(R.string.min));
				sengMsgToDevice(MsgUtil.closeWork("1",1));
				dataUtil.stopData();
			}else{
				t -= 1;
				ptStatus.setStatusZhenJiuClockTime(t);
				statusUtil.setPTStatus(mContext,ptStatus);
//				showLog("当前时间为：" + t + "，具体为：" + TimeUtil.getStrFromInt(mContext,t));
				showLog("当前时间为：" + t + "，具体为：" + t/60 + "分钟");
			}
			if(curType == TYPE_ZHENJIU){
				reSetLayout(TYPE_ZHENJIU);
			}
		}
	};


	public class AnmoThread extends Thread{
		@Override
		public void run() {
			while (anmoIsRun){
				try {
					sleep(1000);
					Message message = new Message();
					message.what = 1;
					anmoHandler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private Handler anmoHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int t = ptStatus.getStatusAnMoClockTime();
			if(t - 1 <= 0){
				ptStatus.setStatusAnMoClockTime(0);
				if (!anmoThread.isInterrupted()){
					anmoIsRun = false;
				}
				ptStatus.setStatusAnMoIsClock(false);
				ptStatus.setStatusAnMoIsOpen(false);
				ptStatus.setStatusAnMoClockTime(anmoTime);
				knobView.setProgress(anmoTime);
				statusUtil.setPTStatus(mContext,ptStatus);
				showToast("计时结束，停止按摩");
				tvTime.setText("0" + getString(R.string.min));
				sengMsgToDevice(MsgUtil.closeWork("2",2));
				dataUtil.stopData();
			}else{
				ptStatus.setStatusAnMoClockTime(t - 1);
				statusUtil.setPTStatus(mContext,ptStatus);
				showLog("当前时间为：" + t + "，具体为：" + TimeUtil.getStrFromInt(mContext,t));
			}
			if(curType == TYPE_ANMO){
				reSetLayout(TYPE_ANMO);
			}
		}
	};


	public class LiliaoThread extends Thread{
		@Override
		public void run() {
			while (liliaoIsRun){
				try {
					sleep(1000);
					Message message = new Message();
					message.what = 1;
					liliaoHandler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private Handler liliaoHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int t = ptStatus.getStatusLiLiaoClockTime();
			if(t - 1 <= 0){
				ptStatus.setStatusLiLiaoClockTime(0);
				if (!liliaoThread.isInterrupted()){
					liliaoIsRun = false;
				}
				ptStatus.setStatusLiLiaoIsClock(false);
				ptStatus.setStatusLiLiaoIsOpen(false);
				ptStatus.setStatusLiLiaoClockTime(liliaoTime);
				knobView.setProgress(liliaoTime);
				statusUtil.setPTStatus(mContext,ptStatus);
				showToast("计时结束，停止理疗");
				tvTime.setText("0" + getString(R.string.min));
				sengMsgToDevice(MsgUtil.closeWork("3",3));
				dataUtil.stopData();
			}else{
				ptStatus.setStatusLiLiaoClockTime(t - 1);
				statusUtil.setPTStatus(mContext,ptStatus);
				showLog("当前时间为：" + t + "，具体为：" + TimeUtil.getStrFromInt(mContext,t));
			}
			if(curType == TYPE_LILIAO){
				reSetLayout(TYPE_LILIAO);
			}
		}
	};


	public class YueliaoThread extends Thread{
		@Override
		public void run() {
			while (yueliaoIsRun){
				try {
					sleep(1000);
					Message message = new Message();
					message.what = 1;
					yueliaoHandler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private Handler yueliaoHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int t = ptStatus.getStatusYueLiaoClockTime();
			if(t - 1 <= 0){
				ptStatus.setStatusYueLiaoClockTime(0);
				if (!yueliaoThread.isInterrupted()){
					yueliaoIsRun = false;
				}
				ptStatus.setStatusYueLiaoIsClock(false);
				ptStatus.setStatusYueLiaoIsOpen(false);
				ptStatus.setStatusYueLiaoClockTime(yueliaoTime);
				knobView.setProgress(yueliaoTime);
				statusUtil.setPTStatus(mContext,ptStatus);
				showToast("计时结束，停止乐疗");
				tvTime.setText("0" + getString(R.string.min));
				sengMsgToDevice(MsgUtil.closeWork("4",4));
				dataUtil.stopData();
			}else{
				ptStatus.setStatusYueLiaoClockTime(t - 1);
				statusUtil.setPTStatus(mContext,ptStatus);
				showLog("当前时间为：" + t + "，具体为：" + TimeUtil.getStrFromInt(mContext,t));
			}
			if(curType == TYPE_YUELIAO){
				reSetLayout(TYPE_YUELIAO);
			}
		}
	};



	@Override
	public void onDestroy() {
		isBobClosed = true;
		dataUtil.destroyUtil();
		if (!anmoThread.isInterrupted()){
			anmoIsRun = false;
		}
		if (!zhenjiuThread.isInterrupted()){
			zhenjiuIsRun = false;
		}
		if (!yueliaoThread.isInterrupted()){
			yueliaoIsRun = false;
		}
		if (!liliaoThread.isInterrupted()){
			liliaoIsRun = false;
		}

		//关闭当前正在运行的模式
		closeOtherMode(0);
		Logger.e("close");
		dataUtil.stopData();

        getActivity().unregisterReceiver(receiver);

		super.onDestroy();
	}

	private void closeOtherMode(int t){
		if (ptStatus.isStatusZhenJiuIsOpen()){
			if (t != TYPE_ZHENJIU){
				sengMsgToDevice(MsgUtil.closeWork("1",1));
			}
		}
		if (ptStatus.isStatusAnMoIsOpen()){
			if (t != TYPE_ANMO){
				sengMsgToDevice(MsgUtil.closeWork("2",2));
			}
		}
		if (ptStatus.isStatusLiLiaoIsOpen()){
			if (t != TYPE_LILIAO){
				sengMsgToDevice(MsgUtil.closeWork("3",3));
			}
		}
		if (ptStatus.isStatusYueLiaoIsOpen()){
			if (t != TYPE_YUELIAO){
				sengMsgToDevice(MsgUtil.closeWork("4",4));
			}
		}
	}


	private void sengMsgToDevice(byte[] bs){
//		bluzManager.sendCustomCommand(keySend, 0, 0, bs);
		Intent intent = new Intent();
		intent.setAction(SEND_MSG_TO_DEVICE);
		intent.putExtra("byte",bs);
		getActivity().sendBroadcast(intent);
	}



}
