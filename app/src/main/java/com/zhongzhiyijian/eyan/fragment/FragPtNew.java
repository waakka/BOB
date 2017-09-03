package com.zhongzhiyijian.eyan.fragment;


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
import com.zhongzhiyijian.eyan.base.BaseFragment;
import com.zhongzhiyijian.eyan.entity.PTStatus;
import com.zhongzhiyijian.eyan.entity.PTStatusUtil;
import com.zhongzhiyijian.eyan.user.UserPersist;
import com.zhongzhiyijian.eyan.util.DataUtil;
import com.zhongzhiyijian.eyan.util.MsgUtil;
import com.zhongzhiyijian.eyan.util.TimeUtil;
import com.zhongzhiyijian.eyan.widget.KnobView;


public class FragPtNew extends BaseFragment implements OnCheckedChangeListener,OnClickListener{

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
	/**
	 * 当前倒计时时间
	 */
	private int time = 0;

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



	private DataUtil dataUtil;

	private boolean isBobClosed = false;

	private TextView tvClockStr;

	private TimeThread timeThread;
	private boolean isTimeThreadRun;


	private boolean startWork = false;
	private boolean endWork = false;


	/**
	 * 接受蓝牙service消息
	 */
	private InnerReceiver receiver;
	private IntentFilter filter;

	private boolean isUpClose = false;

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

		dataUtil = DataUtil.getInstance();

		isTimeThreadRun = true;
		timeThread = new TimeThread();
		timeThread.start();

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
					if(isUpClose){
						isUpClose = false;
						ptStatus.setStatusZhenJiuIsOpen(false);
					}else{
						ptStatus.setStatusZhenJiuIsOpen(true);
					}
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
					if(isUpClose){
						isUpClose = false;
						ptStatus.setStatusAnMoIsOpen(false);
					}else{
						ptStatus.setStatusAnMoIsOpen(true);
					}
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
					if(isUpClose){
						isUpClose = false;
						ptStatus.setStatusLiLiaoIsOpen(false);
					}else{
						ptStatus.setStatusLiLiaoIsOpen(true);
					}
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
					if(isUpClose){
						isUpClose = false;
						ptStatus.setStatusYueLiaoIsOpen(false);
					}else{
						ptStatus.setStatusYueLiaoIsOpen(true);
					}
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
				reSetLayout();
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
				//TODO 发送广播，发送消息变更时间
				sengMsgToDevice(MsgUtil.setTime("8",time));
			}
		});

	}

	/**
	 * 根据当前type刷新界面
	 * 强度
	 * 状态按钮
	 * 加减按钮
	 */
	private void reSetLayout(){
		if(isBobClosed){
			return;
		}
		reSetIntensity();
		switch (curType) {
			case TYPE_ZHENJIU:
				tvType.setText(getString(R.string.unit_zhenjiu));
				if(ptStatus.isStatusZhenJiuIsOpen()){
                    workTypeOpenBG();
                    tvIntensity.setText(ptStatus.getStatusZhenJiuIntensity()+"  级");
                    setIntensityBG(ptStatus.getStatusZhenJiuIntensity());
                    time = ptStatus.getStatusZhenJiuClockTime();
                }else{
                    workTypeCloseBG();
                    tvIntensity.setText("0  级");
                    time = 0;
                }
                setTimeAndProgress();
				break;
			case TYPE_ANMO:
				tvType.setText(getString(R.string.unit_anmo));
				tvIntensity.setText(ptStatus.getStatusAnMoIntensity()+"  级");
				if(ptStatus.isStatusAnMoIsOpen()){
					workTypeOpenBG();
					setIntensityBG(ptStatus.getStatusAnMoIntensity());
                    time = ptStatus.getStatusAnMoClockTime();
                }else{
                    workTypeCloseBG();
                    tvIntensity.setText("0  级");
                    time = 0;
                }
				setTimeAndProgress();
				break;
			case TYPE_LILIAO:
				tvType.setText(getString(R.string.unit_liliao));
				tvIntensity.setText(ptStatus.getStatusLiLiaoIntensity()+"  级");
				if(ptStatus.isStatusLiLiaoIsOpen()){
					workTypeOpenBG();
					setIntensityBG(ptStatus.getStatusLiLiaoIntensity());
                    time = ptStatus.getStatusLiLiaoClockTime();
                }else{
                    workTypeCloseBG();
                    tvIntensity.setText("0  级");
                    time = 0;
                }
				setTimeAndProgress();
				break;
			case TYPE_YUELIAO:
				tvType.setText(getString(R.string.unit_yueliao));
				tvIntensity.setText(ptStatus.getStatusYueLiaoIntensity()+"  级");
				if(ptStatus.isStatusYueLiaoIsOpen()){
					workTypeOpenBG();
					setIntensityBG(ptStatus.getStatusYueLiaoIntensity());
					time = ptStatus.getStatusYueLiaoClockTime();
				}else{
					workTypeCloseBG();
					tvIntensity.setText("0  级");
					time = 0;
				}
				setTimeAndProgress();
				break;

			default:
				break;
		}
	}

	/**
	 * 根据当前状态的定时时间设置界面显示及旋钮状态
	 */
	private void setTimeAndProgress() {
		tvTime.setText(TimeUtil.getStrFromInt(mContext,time));
//				tvTime.setText(time/60 + getString(R.string.min));
		knobView.setProgress(time);
	}

	/**
	 * 当前模式关闭时控件背景及设置按钮不可被点击
	 */
	private void workTypeCloseBG() {
		ibTypeStatus.setImageResource(R.mipmap.tb_off);
		ibPlus.setImageResource(R.mipmap.btn_jia_nomal);
		ibSubtract.setImageResource(R.mipmap.btn_jian_nomal);
		btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
		knobView.setTouchable(false);
		btnClock.setClickable(false);
		ibSubtract.setClickable(false);
		ibPlus.setClickable(false);
		btnClock.setText("关闭");
	}
	/**
	 * 当前模式打开时控件背景及设置按钮可被点击
	 */
	private void workTypeOpenBG() {
		ibTypeStatus.setImageResource(R.mipmap.tb_on);
		ibPlus.setImageResource(R.mipmap.btn_jia_checked);
		ibSubtract.setImageResource(R.mipmap.btn_jian_checked);
		btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
		btnClock.setClickable(true);
		ibSubtract.setClickable(true);
		ibPlus.setClickable(true);
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

	/**
	 * 设置打开模式时强度背景图片
	 * @param i
	 */
	private void setIntensityBG(int i){
		switch (i) {
			case 1:
				ibS1.setImageResource(R.mipmap.y1);
				break;
			case 2:
				ibS3.setImageResource(R.mipmap.y3);
				break;
			case 3:
				ibS5.setImageResource(R.mipmap.y5);
				break;
			case 4:
				ibS7.setImageResource(R.mipmap.y7);
				break;
			case 5:
				ibS9.setImageResource(R.mipmap.y9);
				break;
			case 6:
				ibS11.setImageResource(R.mipmap.y11);
				break;

			default:
				break;
		}
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.rb_zhenjiu:
				curType = TYPE_ZHENJIU;
				reSetLayout();
				break;
			case R.id.rb_anmo:
				curType = TYPE_ANMO;
				reSetLayout();
				break;
			case R.id.rb_liliao:
				curType = TYPE_LILIAO;
				reSetLayout();
				break;
			case R.id.rb_yueliao:
				curType = TYPE_YUELIAO;
				reSetLayout();
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
	 * 状态按钮
	 * 当前状态打开时：
	 * 			工作中：点击发送关闭当前
	 * 			关闭中：点击发送打开当前
	 *
	 */
	private void setClock() {
		switch (curType){
			case TYPE_ZHENJIU:
				if(ptStatus.isStatusZhenJiuIsClock()){
					//当前模式已打开
					if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON || app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_ON){
						//工作状态，点击按钮关闭
						sengMsgToDevice(MsgUtil.close1());
					}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
						//就绪状态，点击按钮不响应
					}
				}else{
					if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_OFF){
						//当前模式未打开，设备已穿戴，点击执行打开命令，直接开始
						sengMsgToDevice(MsgUtil.openWork(
								"01",ptStatus.getStatusZhenJiuIntensity(),time,MsgUtil.TYPE_ZHENJIU));
					}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
						//当前模式未打开,设备未穿戴，点击执行打开命令，进入准备状态
						sengMsgToDevice(MsgUtil.openWork(
								"01",ptStatus.getStatusZhenJiuIntensity(),time,MsgUtil.TYPE_ZHENJIU));
					}
				}
				break;
			case TYPE_ANMO:
				if(ptStatus.isStatusAnMoIsClock()){
					//当前模式已打开
					if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON || app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_ON){
						//工作状态，点击按钮关闭
						sengMsgToDevice(MsgUtil.close2());
					}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
						//就绪状态，点击按钮不响应
					}
				}else{
					btnClock.setText("准备");
					if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_OFF){
						//当前模式未打开，设备已穿戴，点击执行打开命令，直接开始
						sengMsgToDevice(MsgUtil.openWork(
								"02",ptStatus.getStatusAnMoIntensity(),time,MsgUtil.TYPE_ANMO));
					}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
						//当前模式未打开,设备未穿戴，点击执行打开命令，进入准备状态
						sengMsgToDevice(MsgUtil.openWork(
								"02",ptStatus.getStatusAnMoIntensity(),time,MsgUtil.TYPE_ANMO));
					}
				}
				break;
			case TYPE_LILIAO:
				if(ptStatus.isStatusLiLiaoIsClock()){
					//当前模式已打开
					if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON || app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_ON){
						//工作状态，点击按钮关闭
						sengMsgToDevice(MsgUtil.close3());
					}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
						//就绪状态，点击按钮不响应
					}
				}else{
					btnClock.setText("准备");
					if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_OFF){
						//当前模式未打开，设备已穿戴，点击执行打开命令，直接开始
						sengMsgToDevice(MsgUtil.openWork(
								"03",ptStatus.getStatusLiLiaoIntensity(),time,MsgUtil.TYPE_LILIAO));
					}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
						//当前模式未打开,设备未穿戴，点击执行打开命令，进入准备状态
						sengMsgToDevice(MsgUtil.openWork(
								"03",ptStatus.getStatusLiLiaoIntensity(),time,MsgUtil.TYPE_LILIAO));
					}
				}
				break;
			case TYPE_YUELIAO:
				if(ptStatus.isStatusYueLiaoIsClock()){
					//当前模式已打开
					if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON || app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_ON){
						//工作状态，点击按钮关闭
						sengMsgToDevice(MsgUtil.close4());
					}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
						//就绪状态，点击按钮不响应
					}
				}else{
					btnClock.setText("准备");
					if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_OFF){
						//当前模式未打开，设备已穿戴，点击执行打开命令，直接开始
						sengMsgToDevice(MsgUtil.openWork(
								"04",ptStatus.getStatusYueLiaoIntensity(),time,MsgUtil.TYPE_YUELIAO));
					}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
						//当前模式未打开,设备未穿戴，点击执行打开命令，进入准备状态
						sengMsgToDevice(MsgUtil.openWork(
								"04",ptStatus.getStatusYueLiaoIntensity(),time,MsgUtil.TYPE_YUELIAO));
					}
				}
				break;
		}
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
					reSetLayout();
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
					reSetLayout();
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
					reSetLayout();
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
					reSetLayout();
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
						setIntensity(i);
					}
				}
				break;
			case TYPE_ANMO:
				if(ptStatus.isStatusAnMoIsOpen()){
					i = ptStatus.getStatusAnMoIntensity();
					if(i == 1){
						showToast(mContext.getResources().getString(R.string.intensity_min));
					}else{
						i -= 1;
						setIntensity(i);
					}
				}
				break;
			case TYPE_LILIAO:
				if(ptStatus.isStatusLiLiaoIsOpen()){
					i = ptStatus.getStatusLiLiaoIntensity();
					if(i == 1){
						showToast(mContext.getResources().getString(R.string.intensity_min));
					}else{
						i -= 1;
						setIntensity(i);
					}
				}
				break;
			case TYPE_YUELIAO:
				if(ptStatus.isStatusYueLiaoIsOpen()){
					i = ptStatus.getStatusYueLiaoIntensity();
					if(i == 1){
						showToast(mContext.getResources().getString(R.string.intensity_min));
					}else{
						i -= 1;
						setIntensity(i);
					}
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
						setIntensity(i);
					}
				}
				break;
			case TYPE_ANMO:
				if(ptStatus.isStatusAnMoIsOpen()){
					i = ptStatus.getStatusAnMoIntensity();
					if(i == 6){
						showToast(mContext.getResources().getString(R.string.intensity_max));
					}else{
						i += 1;
						setIntensity(i);
					}
				}
				break;
			case TYPE_LILIAO:
				if(ptStatus.isStatusLiLiaoIsOpen()){
					i = ptStatus.getStatusLiLiaoIntensity();
					if(i == 6){
						showToast(mContext.getResources().getString(R.string.intensity_max));
					}else{
						i += 1;
						setIntensity(i);
					}
				}
				break;
			case TYPE_YUELIAO:
				if(ptStatus.isStatusYueLiaoIsOpen()){
					i = ptStatus.getStatusYueLiaoIntensity();
					if(i == 6){
						showToast(mContext.getResources().getString(R.string.intensity_max));
					}else{
						i += 1;
						setIntensity(i);
					}
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
					//发送指令关闭当前单元
					isUpClose = true;
					sengMsgToDevice(MsgUtil.closeWork("01",1));
					dataUtil.stopData();
				}else{
					sengMsgToDevice(MsgUtil.openWork(
							"01",ptStatus.getStatusZhenJiuIntensity(),time,MsgUtil.TYPE_ZHENJIU));
				}
				break;
			case TYPE_ANMO:
				if(ptStatus.isStatusAnMoIsOpen()){
					isUpClose = true;
					sengMsgToDevice(MsgUtil.closeWork("02",2));
					dataUtil.stopData();
				}else{
					sengMsgToDevice(MsgUtil.openWork(
							"02",ptStatus.getStatusAnMoIntensity(),time,MsgUtil.TYPE_ANMO));
				}
				break;
			case TYPE_LILIAO:
				if(ptStatus.isStatusLiLiaoIsOpen()){
					isUpClose = true;
					sengMsgToDevice(MsgUtil.closeWork("03",3));
					dataUtil.stopData();
				}else{
					sengMsgToDevice(MsgUtil.openWork(
							"03",ptStatus.getStatusLiLiaoIntensity(),time,MsgUtil.TYPE_LILIAO));
				}
				break;
			case TYPE_YUELIAO:
				if(ptStatus.isStatusYueLiaoIsOpen()){
					isUpClose = true;
					sengMsgToDevice(MsgUtil.closeWork("04",4));
					dataUtil.stopData();
				}else{
					sengMsgToDevice(MsgUtil.openWork(
							"04",ptStatus.getStatusYueLiaoIntensity(),time,MsgUtil.TYPE_YUELIAO));
				}
				break;

			default:
				break;
		}
	}

	/**
	 * 倒计时线程
	 */
	private class TimeThread extends Thread{
		@Override
		public void run() {
			while (isTimeThreadRun){
				try {
					sleep(1000);
					TimeHandler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Handler TimeHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {

			//工作状态
			switch (curType){
				case TYPE_ZHENJIU:
					if(ptStatus.isStatusZhenJiuIsClock()){
						//当前模式已打开
						if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON || app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_ON){

							if(time -1 <= 0){
								sengMsgToDevice(MsgUtil.closeWork("1",1));
								dataUtil.stopData();
							}else{
								time -= 1;
								ptStatus.setStatusZhenJiuClockTime(time);
								statusUtil.setPTStatus(mContext,ptStatus);
								setTimeAndProgress();
							}
							startWork = true;
							if(startWork&&endWork){
								Logger.e("开始记录设备使用情况");
								startWork = false;
								endWork = false;
								dataUtil.startData("1",ptStatus.getStatusZhenJiuIntensity(),user.getToken(),user.getSid(),"-1");
								sengMsgToDevice(MsgUtil.getCurWorkType());
							}
//							reSetLayout();
							//工作状态，点击按钮关闭
							btnClock.setText("关闭");
							knobView.setTouchable(false);
						}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
							//就绪状态，点击按钮不响应
							btnClock.setText("已准备");
							knobView.setTouchable(true);
							endWork = true;
							dataUtil.stopData();
						}
					}else{
						endWork = true;
						if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_OFF){
							//当前模式未打开，设备已穿戴，点击执行打开命令，直接开始
							btnClock.setText("开始");
						}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
							//当前模式未打开,设备未穿戴，点击执行打开命令，进入准备状态
							btnClock.setText("准备");
						}
						if (ptStatus.isStatusZhenJiuIsOpen()){
							knobView.setTouchable(true);
						}else{
							knobView.setTouchable(false);
							btnClock.setText("关闭");
						}
					}
					break;
				case TYPE_ANMO:
					if(ptStatus.isStatusAnMoIsClock()){
						//当前模式已打开
						if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON || app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_ON){

							if(time -1 <= 0){
								sengMsgToDevice(MsgUtil.closeWork("1",1));
								dataUtil.stopData();
							}else{
								time -= 1;
								ptStatus.setStatusAnMoClockTime(time);
								statusUtil.setPTStatus(mContext,ptStatus);
								setTimeAndProgress();
							}
							startWork = true;
							if(startWork&&endWork){
								startWork = false;
								endWork = false;
								dataUtil.startData("2",ptStatus.getStatusAnMoIntensity(),user.getToken(),user.getSid(),"-1");
								sengMsgToDevice(MsgUtil.getCurWorkType());
							}
//							reSetLayout();
							//工作状态，点击按钮关闭
							btnClock.setText("关闭");
							knobView.setTouchable(false);
						}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
							//就绪状态，点击按钮不响应
							btnClock.setText("已准备");
							knobView.setTouchable(true);
							endWork = true;
							dataUtil.stopData();
						}
					}else{
						endWork = true;
						if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_OFF){
							//当前模式未打开，设备已穿戴，点击执行打开命令，直接开始
							btnClock.setText("开始");
						}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
							//当前模式未打开,设备未穿戴，点击执行打开命令，进入准备状态
							btnClock.setText("准备");
						}
						if (ptStatus.isStatusAnMoIsOpen()){
							knobView.setTouchable(true);
						}else{
							knobView.setTouchable(false);
							btnClock.setText("关闭");
						}
					}
					break;
				case TYPE_LILIAO:
					if(ptStatus.isStatusLiLiaoIsClock()){
						//当前模式已打开
						if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON || app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_ON){

							if(time -1 <= 0){
								sengMsgToDevice(MsgUtil.closeWork("1",1));
								dataUtil.stopData();
							}else{
								time -= 1;
								ptStatus.setStatusLiLiaoClockTime(time);
								statusUtil.setPTStatus(mContext,ptStatus);
								setTimeAndProgress();
							}
							startWork = true;
							if(startWork&&endWork){
								startWork = false;
								endWork = false;
								dataUtil.startData("3",ptStatus.getStatusLiLiaoIntensity(),user.getToken(),user.getSid(),"-1");
								sengMsgToDevice(MsgUtil.getCurWorkType());
							}
//							reSetLayout();
							//工作状态，点击按钮关闭
							btnClock.setText("关闭");
							knobView.setTouchable(false);
						}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
							//就绪状态，点击按钮不响应
							btnClock.setText("已准备");
							knobView.setTouchable(true);
							endWork = true;
							dataUtil.stopData();
						}
					}else{
						endWork = true;
						if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_OFF){
							//当前模式未打开，设备已穿戴，点击执行打开命令，直接开始
							btnClock.setText("开始");
						}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
							//当前模式未打开,设备未穿戴，点击执行打开命令，进入准备状态
							btnClock.setText("准备");
						}
						if (ptStatus.isStatusLiLiaoIsOpen()){
							knobView.setTouchable(true);
						}else{
							knobView.setTouchable(false);
							btnClock.setText("关闭");
						}
					}
					break;
				case TYPE_YUELIAO:
					if(ptStatus.isStatusYueLiaoIsClock()){
						//当前模式已打开
						if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_ON || app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_ON){
							startWork = true;

							if(time -1 <= 0){
								sengMsgToDevice(MsgUtil.closeWork("1",1));
								dataUtil.stopData();
							}else{
								time -= 1;
								ptStatus.setStatusYueLiaoClockTime(time);
								statusUtil.setPTStatus(mContext,ptStatus);
								setTimeAndProgress();
							}
							if(startWork&&endWork){
								startWork = false;
								endWork = false;
								dataUtil.startData("2",ptStatus.getStatusYueLiaoIntensity(),user.getToken(),user.getSid(),"-1");
								sengMsgToDevice(MsgUtil.getCurWorkType());
							}
//							reSetLayout();
							//工作状态，点击按钮关闭
							btnClock.setText("关闭");
							knobView.setTouchable(false);
						}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
							//就绪状态，点击按钮不响应
							btnClock.setText("已准备");
							knobView.setTouchable(true);
							endWork = true;
							dataUtil.stopData();
						}
					}else{
						endWork = true;
						if(app.workStatus == WORK_STATUS_DIANJI_ON_GAOYA_OFF){
							//当前模式未打开，设备已穿戴，点击执行打开命令，直接开始
							btnClock.setText("开始");
						}else if(app.workStatus == WORK_STATUS_DIANJI_OFF_GAOYA_OFF){
							//当前模式未打开,设备未穿戴，点击执行打开命令，进入准备状态
							btnClock.setText("准备");
						}
						if (ptStatus.isStatusYueLiaoIsOpen()){
							knobView.setTouchable(true);
						}else{
							knobView.setTouchable(false);
							btnClock.setText("关闭");
						}
					}
					break;
			}
		}
	};



	@Override
	public void onDestroy() {
		isBobClosed = true;
		dataUtil.destroyUtil();
		isTimeThreadRun = false;
		Logger.e("close");
		dataUtil.stopData();

		getActivity().unregisterReceiver(receiver);

		super.onDestroy();
	}

	private void sengMsgToDevice(byte[] bs){
		Intent intent = new Intent();
		intent.setAction(SEND_MSG_TO_DEVICE);
		intent.putExtra("byte",bs);
		getActivity().sendBroadcast(intent);
	}



}
