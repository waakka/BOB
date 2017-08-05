package com.zhongzhiyijian.eyan.fragment;


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

import com.actions.ibluz.manager.BluzManager;
import com.actions.ibluz.manager.BluzManagerData;
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
import com.zhongzhiyijian.eyan.widget.MyView;

import static com.baidu.location.h.a.i;


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
	/**
	 * 当前正在工作的type
	 */
	private int curOnType = 0;

	//底部TAB
	private RadioGroup rgBottom;
	private RadioButton rbZhenjiu;
	private RadioButton rbAnmo;
	private RadioButton rbLiliao;
	private RadioButton rbYueliao;

	//强度设置
	private MyView myView;
	private TextView tvIntensity;

	//title
	private TextView tvType;
	private ImageButton ibTypeStatus;
	//旋钮
	private KnobView knobView;

	//计时
	private TextView tvTime;
	private Button btnClock;
	//初始化计时时间
	private int time = 0;

	private int zhenjiuTime;
	private int anmoTime;
	private int liliaoTime;
	private int yueliaoTime;


	//频率设置
	private LinearLayout llFrequency;
	private TextView tvFrequency;
	private MyView myViewF;
	private int curFrequency;

	private ZhenjiuThread zhenjiuThread;
	private boolean zhenjiuIsRun = false;
	private AnmoThread anmoThread;
	private boolean anmoIsRun = false;
	private LiliaoThread liliaoThread;
	private boolean liliaoIsRun = false;
	private YueliaoThread yueliaoThread;
	private boolean yueliaoIsRun = false;



	private BluzManager bluzManager;
	private int keySend;
	private int keyAnswer;

	private DataUtil dataUtil;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_pt_new, container,false);
		userPersist = UserPersist.getInstance();
		user = userPersist.getUser(mContext);
		statusUtil = PTStatusUtil.getInstance();
		ptStatus = statusUtil.getPTStatus(mContext);

		initView();
		initEvent();

		//获取bluzManager 用来发送消息
		DeviceDetailActivity mActivity = (DeviceDetailActivity)getActivity();
		bluzManager = mActivity.getBluzManager();
		keySend = BluzManager.buildKey(BluzManagerData.CommandType.QUE, 0x81);
		keyAnswer = BluzManager.buildKey(BluzManagerData.CommandType.ANS, 0x81);

		byte[] bytes = MsgUtil.zhenjiuMode();
		curType = TYPE_ZHENJIU;
		if (ptStatus.isStatusZhenJiuIsOpen()){
			curType = TYPE_ZHENJIU;
			bytes = MsgUtil.zhenjiuMode();
			rbZhenjiu.performClick();
		}
		if (ptStatus.isStatusAnMoIsOpen()){
			curType = TYPE_ANMO;
			bytes = MsgUtil.anmoMode();
			rbAnmo.performClick();
		}
		if (ptStatus.isStatusLiLiaoIsOpen()){
			curType = TYPE_LILIAO;
			bytes = MsgUtil.liliaoMode();
			rbLiliao.performClick();
		}
		if (ptStatus.isStatusYueLiaoIsOpen()){
			curType = TYPE_YUELIAO;
			bytes = MsgUtil.yueliaoMode();
			rbYueliao.performClick();
		}
		ptStatus.setStatusZhenJiuIsOpen(false);
		ptStatus.setStatusAnMoIsOpen(false);
		ptStatus.setStatusLiLiaoIsOpen(false);
		ptStatus.setStatusYueLiaoIsOpen(false);

		ptStatus.setStatusZhenJiuIsClock(false);
		ptStatus.setStatusAnMoIsClock(false);
		ptStatus.setStatusLiLiaoIsClock(false);
		ptStatus.setStatusYueLiaoIsClock(false);
		reSetLayout(curType);



		zhenjiuThread = new ZhenjiuThread();
		anmoThread = new AnmoThread();
		liliaoThread = new LiliaoThread();
		yueliaoThread = new YueliaoThread();

		sengMsgToDevice(MsgUtil.getId());
		new Thread(){
			@Override
			public void run() {
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
		//初始化为针灸模式 默认打开上次模式
		sengMsgToDevice(bytes);

		dataUtil = DataUtil.getInstance();

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		userPersist = UserPersist.getInstance();
		user = userPersist.getUser(mContext);
	}

	private void initView() {
		rgBottom = (RadioGroup) view.findViewById(R.id.rg_pt);
		rbZhenjiu = (RadioButton) view.findViewById(R.id.rb_zhenjiu);
		rbAnmo = (RadioButton) view.findViewById(R.id.rb_anmo);
		rbLiliao = (RadioButton) view.findViewById(R.id.rb_liliao);
		rbYueliao = (RadioButton) view.findViewById(R.id.rb_yueliao);

		tvType = (TextView) view.findViewById(R.id.tv_type);
		tvIntensity = (TextView) view.findViewById(R.id.tv_intensity);
		ibTypeStatus = (ImageButton) view.findViewById(R.id.ib_type_status);

		View rootView = view.findViewById(R.id.myview);
		myView = MyView.getInstance(rootView);

		View rootViewF = view.findViewById(R.id.myviewf);
		myViewF = MyView.getInstance(rootViewF);

		llFrequency = (LinearLayout) view.findViewById(R.id.ll_frequency);
		tvFrequency = (TextView) view.findViewById(R.id.tv_frequency);

		btnClock = (Button) view.findViewById(R.id.btn_clock);
		tvTime = (TextView) view.findViewById(R.id.tv_time);
		knobView = (KnobView) view.findViewById(R.id.knobview);
		knobView.setMax(1800);
	}

	private void initEvent() {
		rgBottom.setOnCheckedChangeListener(this);
		ibTypeStatus.setOnClickListener(this);


		btnClock.setOnClickListener(this);

		myView.setOnItemCheckedChangedListener(new MyView.OnItemCheckedChangedListener() {
			@Override
			public void itemCheckedChangedListener(int position) {
				setIntensity(position*10);
				showToast("设置强度" + position*10);
			}
		});

		myViewF.setOnItemCheckedChangedListener(new MyView.OnItemCheckedChangedListener() {
			@Override
			public void itemCheckedChangedListener(int position) {
				setHz(position*100);
			}
		});

		knobView.setProgressChangeListener(new KnobView.OnProgressChangeListener() {
			@Override
			public void onProgressChanged(boolean fromUser, int progress) {
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
			@Override
			public void onStartTrackingTouch(KnobView view, int progress) {
			}
			@Override
			public void onStopTrackingTouch(KnobView view, int progress) {

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
//		Logger.e("刷新界面,type=" + type);
		curType = type;
		switch (type) {
			case TYPE_ZHENJIU:
				tvType.setText(getString(R.string.unit_zhenjiu));
				tvIntensity.setText(ptStatus.getStatusZhenJiuIntensity()+"");
				llFrequency.setVisibility(View.GONE);
				if(ptStatus.isStatusZhenJiuIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					myView.setOpen(true);
					knobView.setTouchable(true);
					btnClock.setClickable(true);
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					myView.setOpen(false);
					knobView.setTouchable(false);
					btnClock.setClickable(false);
				}
				time = ptStatus.getStatusZhenJiuClockTime();
				tvTime.setText(TimeUtil.getStrFromInt(mContext,time));
				knobView.setProgress(time);
				if(ptStatus.isStatusZhenJiuIsClock()){
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
				}else{
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
				}

				//设置强度的position
				myView.setPosition(ptStatus.getStatusZhenJiuIntensity()/10);

				break;

			case TYPE_ANMO:
//				showToast("当前状态：按摩" + (ptStatus.isStatusAnMoIsOpen() ?
//						("打开 强度为：" + ptStatus.getStatusAnMoIntensity()):"关闭"));
//				showLog("当前状态：按摩" + (ptStatus.isStatusAnMoIsOpen() ?
//						("打开 强度为：" + ptStatus.getStatusAnMoIntensity()):"关闭"));
				tvType.setText(getString(R.string.unit_anmo));
				tvIntensity.setText(ptStatus.getStatusAnMoIntensity()+"");
				llFrequency.setVisibility(View.GONE);
				if(ptStatus.isStatusAnMoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					myView.setOpen(true);
					knobView.setTouchable(true);
					btnClock.setClickable(true);
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					myView.setOpen(false);
					knobView.setTouchable(false);
					btnClock.setClickable(false);
				}
				time = ptStatus.getStatusAnMoClockTime();
				tvTime.setText(TimeUtil.getStrFromInt(mContext,time));
				knobView.setProgress(time);
				if(ptStatus.isStatusAnMoIsClock()){
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
				}else{
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
				}

				//设置强度的position
				myView.setPosition(ptStatus.getStatusAnMoIntensity()/10);

				break;

			case TYPE_LILIAO:
//				showToast("当前状态：理疗" + (ptStatus.isStatusLiLiaoIsOpen() ?
//						("打开 强度为：" + ptStatus.getStatusLiLiaoIntensity()):"关闭"));
//				showLog("当前状态：理疗" + (ptStatus.isStatusLiLiaoIsOpen() ?
//						("打开 强度为：" + ptStatus.getStatusLiLiaoIntensity()):"关闭"));
				tvType.setText(getString(R.string.unit_liliao));
				tvIntensity.setText(ptStatus.getStatusLiLiaoIntensity()+"");
				llFrequency.setVisibility(View.GONE);
				if(ptStatus.isStatusLiLiaoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					myView.setOpen(true);
					knobView.setTouchable(true);
					btnClock.setClickable(true);
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					myView.setOpen(false);
					knobView.setTouchable(false);
					btnClock.setClickable(false);
				}
				time = ptStatus.getStatusLiLiaoClockTime();
				tvTime.setText(TimeUtil.getStrFromInt(mContext,time));
				knobView.setProgress(time);
				if(ptStatus.isStatusLiLiaoIsClock()){
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
				}else{
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
				}

				//设置强度的position
				myView.setPosition(ptStatus.getStatusLiLiaoIntensity()/10);

				break;

			case TYPE_YUELIAO:
//				showToast("当前状态：乐疗" + (ptStatus.isStatusYueLiaoIsOpen() ?
//						("打开 强度为：" + ptStatus.getStatusYueLiaoIntensity() + "频率为：" + ptStatus.getStatusYueLiaoFrequency()):"关闭"));
//				showLog("当前状态：乐疗" + (ptStatus.isStatusYueLiaoIsOpen() ?
//						("打开 强度为：" + ptStatus.getStatusYueLiaoIntensity() + "频率为：" + ptStatus.getStatusYueLiaoFrequency()):"关闭"));
				tvType.setText(getString(R.string.unit_yueliao));
				tvIntensity.setText(ptStatus.getStatusYueLiaoIntensity()+"");
				llFrequency.setVisibility(View.VISIBLE);
				curFrequency = ptStatus.getStatusYueLiaoFrequency();
				tvFrequency.setText(curFrequency+"HZ");
				if(ptStatus.isStatusYueLiaoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_on);

					myView.setOpen(true);

					myViewF.setOpen(true);

					knobView.setTouchable(true);
					btnClock.setClickable(true);
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_off);

					myView.setOpen(false);

					myViewF.setOpen(false);

					knobView.setTouchable(false);
					btnClock.setClickable(false);
				}
				time = ptStatus.getStatusYueLiaoClockTime();
				tvTime.setText(TimeUtil.getStrFromInt(mContext,time));
				knobView.setProgress(time);
				if(ptStatus.isStatusYueLiaoIsClock()){
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
				}else{
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
				}

				//设置强度的position
				myView.setPosition(ptStatus.getStatusYueLiaoIntensity()/10);

				//设置频率的position
				myViewF.setPosition(ptStatus.getStatusYueLiaoFrequency()/10);

				break;

			default:
				break;
		}
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
					showToast("关闭定时");
					ptStatus.setStatusZhenJiuIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!zhenjiuThread.isInterrupted()){
						zhenjiuIsRun = false;
					}
				}else{
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					ptStatus.setStatusZhenJiuIsClock(true);
					ptStatus.setStatusZhenJiuClockTime(time);
					showToast("开始定时，针灸" + TimeUtil.getStrFromInt(mContext,time) + "后关闭");
					zhenjiuIsRun = true;
					zhenjiuThread = new ZhenjiuThread();
					zhenjiuThread.start();
					zhenjiuTime = time+1;
				}
				break;
			case TYPE_ANMO:
				if (ptStatus.isStatusAnMoIsClock()){
					showToast("关闭定时");
					ptStatus.setStatusAnMoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!anmoThread.isInterrupted()){
						anmoIsRun = false;
					}
				}else{
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					ptStatus.setStatusAnMoIsClock(true);
					ptStatus.setStatusAnMoClockTime(time);
					showToast("开始定时，按摩" +  TimeUtil.getStrFromInt(mContext,time) + "后关闭");
					anmoIsRun = true;
					anmoThread = new AnmoThread();
					anmoThread.start();
					anmoTime = time+1;
				}
				break;
			case TYPE_LILIAO:
				if (ptStatus.isStatusLiLiaoIsClock()){
					showToast("关闭定时");
					ptStatus.setStatusLiLiaoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!liliaoThread.isInterrupted()){
						liliaoIsRun = false;
					}
				}else{
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					ptStatus.setStatusLiLiaoIsClock(true);
					ptStatus.setStatusLiLiaoClockTime(time);
					showToast("开始定时，理疗" +  TimeUtil.getStrFromInt(mContext,time) + "后关闭");
					liliaoIsRun = true;
					liliaoThread = new LiliaoThread();
					liliaoThread.start();
					liliaoTime = time+1;
				}
				break;
			case TYPE_YUELIAO:
				if (ptStatus.isStatusYueLiaoIsClock()){
					showToast("关闭定时");
					ptStatus.setStatusYueLiaoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!yueliaoThread.isInterrupted()){
						yueliaoIsRun = false;
					}
				}else{
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					ptStatus.setStatusYueLiaoIsClock(true);
					ptStatus.setStatusYueLiaoClockTime(time);
					showToast("开始定时，乐疗" +  TimeUtil.getStrFromInt(mContext,time) + "后关闭");
					yueliaoIsRun = true;
					yueliaoThread = new YueliaoThread();
					yueliaoThread.start();
					yueliaoTime = time+1;
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
					sengMsgToDevice(MsgUtil.setIntensity(intensity));
					dataUtil.changeStrength(intensity);
					reSetLayout(TYPE_ZHENJIU);
				}
				break;
			case TYPE_ANMO:
				if(ptStatus.isStatusAnMoIsOpen()){
					ptStatus.setStatusAnMoIntensity(intensity);
					statusUtil.setPTStatus(mContext, ptStatus);
					sengMsgToDevice(MsgUtil.setIntensity(intensity));
					dataUtil.changeStrength(intensity);
					reSetLayout(TYPE_ANMO);
				}
				break;
			case TYPE_LILIAO:
				if(ptStatus.isStatusLiLiaoIsOpen()){
					ptStatus.setStatusLiLiaoIntensity(intensity);
					statusUtil.setPTStatus(mContext, ptStatus);
					sengMsgToDevice(MsgUtil.setIntensity(intensity));
					dataUtil.changeStrength(intensity);
					reSetLayout(TYPE_LILIAO);
				}
				break;
			case TYPE_YUELIAO:
				if(ptStatus.isStatusYueLiaoIsOpen()){
					ptStatus.setStatusYueLiaoIntensity(intensity);
					statusUtil.setPTStatus(mContext, ptStatus);
					sengMsgToDevice(MsgUtil.setIntensity(intensity));
					dataUtil.changeStrength(intensity);
					reSetLayout(TYPE_YUELIAO);
				}
				break;

			default:
				break;
		}
	}

	/**
	 * 设置频率
	 * @param hz
	 */
	private void setHz(int hz){
		if(ptStatus.isStatusYueLiaoIsOpen()){
			ptStatus.setStatusYueLiaoFrequency(hz);
			statusUtil.setPTStatus(mContext, ptStatus);
			//设置当前频率为
			sengMsgToDevice(MsgUtil.setFrequency(i));
			reSetLayout(TYPE_YUELIAO);
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
					ptStatus.setStatusZhenJiuIsClock(false);

					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					//发送指令关闭当前单元
					sengMsgToDevice(MsgUtil.closeMode());
					Logger.e("close");
					//关闭当前定时线程
					if (!zhenjiuThread.isInterrupted()){
						zhenjiuIsRun = false;
					}
					dataUtil.stopData();
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					//选中单元非当前单元时，关闭并切换为当前单元
					if (curOnType != curType){
						openZhenJiu();
						curOnType = curType;
					}else{
						new Thread(){
							@Override
							public void run() {
								try {
									sengMsgToDevice(MsgUtil.openMode());
									showLog("打开针灸");
									sleep(500);
									sengMsgToDevice(MsgUtil.setIntensity(ptStatus.getStatusZhenJiuIntensity()));
									showLog("设置针灸强度为"+ptStatus.getStatusZhenJiuIntensity());
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
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
					dataUtil.startData("1",ptStatus.getStatusZhenJiuIntensity(),user.getToken(),user.getSid(),"-1");
				}
				reSetLayout(TYPE_ZHENJIU);
				statusUtil.setPTStatus(mContext, ptStatus);
				break;
			case TYPE_ANMO:
				if(ptStatus.isStatusAnMoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ptStatus.setStatusAnMoIsOpen(false);
					ptStatus.setStatusAnMoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					sengMsgToDevice(MsgUtil.closeMode());
					Logger.e("close");
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!anmoThread.isInterrupted()){
						anmoIsRun = false;
					}
					dataUtil.stopData();
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					if (curOnType != curType){
						openAnMo();
						curOnType = curType;
					}else{
						new Thread(){
							@Override
							public void run() {
								try {
									sengMsgToDevice(MsgUtil.openMode());
									showLog("打开按摩");
									sleep(500);
									sengMsgToDevice(MsgUtil.setIntensity(ptStatus.getStatusAnMoIntensity()));
									showLog("设置按摩强度为"+ptStatus.getStatusAnMoIntensity());
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
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
					dataUtil.startData("2",ptStatus.getStatusAnMoIntensity(),user.getToken(),user.getSid(),"-1");
				}
				reSetLayout(TYPE_ANMO);
				statusUtil.setPTStatus(mContext, ptStatus);
				break;
			case TYPE_LILIAO:
				if(ptStatus.isStatusLiLiaoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ptStatus.setStatusLiLiaoIsOpen(false);
					ptStatus.setStatusLiLiaoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					sengMsgToDevice(MsgUtil.closeMode());
					Logger.e("close");
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!liliaoThread.isInterrupted()){
						liliaoIsRun = false;
					}
					dataUtil.stopData();
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					if (curOnType != curType){
						openLiLiao();
						curOnType = curType;
					}else{
						new Thread(){
							@Override
							public void run() {
								try {
									sengMsgToDevice(MsgUtil.openMode());
									showLog("打开理疗");
									sleep(500);
									sengMsgToDevice(MsgUtil.setIntensity(ptStatus.getStatusLiLiaoIntensity()));
									showLog("设置理疗强度为"+ptStatus.getStatusLiLiaoIntensity());
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
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
					dataUtil.startData("3",ptStatus.getStatusAnMoIntensity(),user.getToken(),user.getSid(),"-1");
				}
				reSetLayout(TYPE_LILIAO);
				statusUtil.setPTStatus(mContext, ptStatus);
				break;
			case TYPE_YUELIAO:
				if(ptStatus.isStatusYueLiaoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ptStatus.setStatusYueLiaoIsOpen(false);
					ptStatus.setStatusYueLiaoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					sengMsgToDevice(MsgUtil.closeMode());
					Logger.e("close");
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!yueliaoThread.isInterrupted()){
						yueliaoIsRun = false;
					}
					dataUtil.stopData();
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					if (curOnType != curType){
						openYueLiao();
						curOnType = curType;
					}else{
						new Thread(){
							@Override
							public void run() {
								try {
									sengMsgToDevice(MsgUtil.openMode());
									showLog("打开乐疗");
									sleep(500);
									sengMsgToDevice(MsgUtil.setIntensity(ptStatus.getStatusYueLiaoIntensity()));
									showLog("设置乐疗强度为"+ptStatus.getStatusYueLiaoIntensity());
									sleep(500);
									sengMsgToDevice(MsgUtil.setFrequency(ptStatus.getStatusYueLiaoFrequency()));
									showLog("设置乐疗频率为"+ptStatus.getStatusYueLiaoFrequency());
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
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
					dataUtil.startData("4",ptStatus.getStatusAnMoIntensity(),user.getToken(),user.getSid(),"-1");
				}
				reSetLayout(TYPE_YUELIAO);
				statusUtil.setPTStatus(mContext, ptStatus);
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
				tvTime.setText("0" + getString(R.string.s));
				sengMsgToDevice(MsgUtil.closeMode());
				dataUtil.stopData();
			}else{
				ptStatus.setStatusZhenJiuClockTime(t - 1);
				statusUtil.setPTStatus(mContext,ptStatus);
				showLog("当前时间为：" + t + "，具体为：" + TimeUtil.getStrFromInt(mContext,t));
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
				tvTime.setText("0" + getString(R.string.s));
				sengMsgToDevice(MsgUtil.closeMode());
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
				tvTime.setText("0" + getString(R.string.s));
				sengMsgToDevice(MsgUtil.closeMode());
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
				tvTime.setText("0" + getString(R.string.s));
				sengMsgToDevice(MsgUtil.closeMode());
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

	private void sengMsgToDevice(byte[] bs){
		bluzManager.sendCustomCommand(keySend, 0, 0, bs);
	}

	private void openZhenJiu(){
		new Thread(){
			@Override
			public void run() {
				try {
					sengMsgToDevice(MsgUtil.closeMode());
					sleep(500);
					sengMsgToDevice(MsgUtil.zhenjiuMode());
					sleep(500);
					sengMsgToDevice(MsgUtil.openMode());
					sleep(500);
					sengMsgToDevice(MsgUtil.setIntensity(ptStatus.getStatusZhenJiuIntensity()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();;
	}
	private void openAnMo(){
		new Thread(){
			@Override
			public void run() {
				try {
					sengMsgToDevice(MsgUtil.closeMode());
					sleep(500);
					sengMsgToDevice(MsgUtil.anmoMode());
					sleep(500);
					sengMsgToDevice(MsgUtil.openMode());
					sleep(500);
					sengMsgToDevice(MsgUtil.setIntensity(ptStatus.getStatusAnMoIntensity()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();;
	}
	private void openLiLiao(){
		new Thread(){
			@Override
			public void run() {
				try {
					sengMsgToDevice(MsgUtil.closeMode());
					sleep(500);
					sengMsgToDevice(MsgUtil.liliaoMode());
					sleep(500);
					sengMsgToDevice(MsgUtil.openMode());
					sleep(500);
					sengMsgToDevice(MsgUtil.setIntensity(ptStatus.getStatusLiLiaoIntensity()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();;
	}
	private void openYueLiao(){
		new Thread(){
			@Override
			public void run() {
				try {
					sengMsgToDevice(MsgUtil.closeMode());
					sleep(500);
					sengMsgToDevice(MsgUtil.yueliaoMode());
					sleep(500);
					sengMsgToDevice(MsgUtil.openMode());
					sleep(500);
					sengMsgToDevice(MsgUtil.setIntensity(ptStatus.getStatusYueLiaoIntensity()));
					sleep(500);
					sengMsgToDevice(MsgUtil.setFrequency(ptStatus.getStatusYueLiaoFrequency()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}


	@Override
	public void onDestroy() {
		dataUtil.destroyUtil();
		super.onDestroy();
	}
}
