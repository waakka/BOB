package com.zhongzhiyijian.eyan.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.activity.AboutActivity;
import com.zhongzhiyijian.eyan.activity.LoginActivity;
import com.zhongzhiyijian.eyan.activity.MainActivity;
import com.zhongzhiyijian.eyan.activity.RecordActivity;
import com.zhongzhiyijian.eyan.activity.RegistActivity;
import com.zhongzhiyijian.eyan.activity.SafeActivity;
import com.zhongzhiyijian.eyan.activity.SettingActivity;
import com.zhongzhiyijian.eyan.activity.UserInfoActivity;
import com.zhongzhiyijian.eyan.activity.friend.FriendActivity;
import com.zhongzhiyijian.eyan.activity.menu.CouponActivity;
import com.zhongzhiyijian.eyan.activity.menu.IntegralActivity;
import com.zhongzhiyijian.eyan.activity.msg.MsgActivity;
import com.zhongzhiyijian.eyan.base.BaseFragment;
import com.zhongzhiyijian.eyan.entity.Msg;
import com.zhongzhiyijian.eyan.entity.User;
import com.zhongzhiyijian.eyan.entity.UserInfo;
import com.zhongzhiyijian.eyan.net.Urls;
import com.zhongzhiyijian.eyan.user.UserInfoPersist;
import com.zhongzhiyijian.eyan.user.UserPersist;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by Administrator on 2016/8/8.
 */
public class FragUser extends BaseFragment implements View.OnClickListener{
    private View view;
    private MainActivity mainActivity;
    private UserPersist userPersist;
    private User user;
    private UserInfo info;
    private UserInfoPersist infoPersist;

    private RelativeLayout rlInfo;
    private RelativeLayout rlFriend;
    private RelativeLayout rlData;
    private RelativeLayout rlPower;
    private RelativeLayout rlAbout;
    private RelativeLayout rlSafe;
    private RelativeLayout rlSetting;

    private ImageView ivAvatar;
    private ImageButton ibMsg;
    private RelativeLayout rlHasMsg;
    private TextView tvMsgCount;

    private LinearLayout llJifen;
    private LinearLayout llYouhui;
    private LinearLayout llQiandao;
    private TextView tvSign;
    private ImageView ivSign;
    private TextView tvIntegral;
    private ImageView ivIntegral;
    private TextView tvCoupon;
    private ImageView ivCoupon;

    private LinearLayout llBtn;
    private LinearLayout llLogin;
    private LinearLayout llRegist;
    private TextView tvName;
    private TextView tvPower;
    private ImageView ivPower;
    private TextView tvUnSucc;

    private boolean isLogin = false;
    private boolean isSign = false;


    private int integral;
    private int coupon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_user, null);
        mainActivity = (MainActivity) getActivity();
        userPersist = UserPersist.getInstance();
        infoPersist = UserInfoPersist.getInstance();


        initView();
        initEvent();

        llQiandao.setClickable(false);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            onPause();
        }else{
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        user = userPersist.getUser(mContext);
        info = infoPersist.getUser(mContext);
        if (user.getToken()!=null&&!user.getToken().equals("")){
            isLogin = true;
        }else{
            isLogin = false;
        }
        setData();


    }

    /**
     * 获取签到状态
     */
    private void getSignState() {
        RequestParams params = new RequestParams(Urls.getSign);
        params.addBodyParameter("token",user.getToken());
        params.addBodyParameter("sid",user.getSid()+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
                        int code = object.getInt("state");
                        if (code == 1){
                            isSign = false;
                        }else{
                            isSign = true;
                        }
                        setSign();
                    }else if(returnCode.equals(ALREADYLOGIN)){
                        showToast("您的登录信息已失效或在其他客户端登录，请重新登录");
                        user = new User();
                        userPersist.setUser(mContext,user);
                        onResume();
                        intent2Activity(LoginActivity.class);
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
            }
        });
    }

    private void setData() {
        if (isLogin){
            llBtn.setVisibility(View.GONE);
            tvName.setVisibility(View.VISIBLE);
            tvIntegral.setVisibility(View.VISIBLE);
            tvCoupon.setVisibility(View.VISIBLE);
            ivIntegral.setVisibility(View.GONE);
            ivCoupon.setVisibility(View.GONE);

            String name = info.getName();
            if (name==null||name.equals("")){
                name = user.getPhone();
            }
            tvName.setText(name);
            ImageOptions options = new ImageOptions.Builder()
                    .setCircular(true)
                    .setFailureDrawableId(R.mipmap.avatar_defoult)
                    .build();
            x.image().loadDrawable(info.getUrl().startsWith("http") ? info.getUrl() : Urls.BASE_URL + info.getUrl(), options, new Callback.CommonCallback<Drawable>() {
                @Override
                public void onSuccess(Drawable result) {
                    ivAvatar.setImageDrawable(result);
                }
                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    ivAvatar.setImageResource(R.mipmap.avatar_defoult);
                }
                @Override
                public void onCancelled(CancelledException cex) {
                }
                @Override
                public void onFinished() {

                }
            });

            if (isPerfect()){
                tvUnSucc.setVisibility(View.GONE);
            }else{
                tvUnSucc.setVisibility(View.VISIBLE);
            }

            getSignState();
            getIntegral();
            getCoupon();
            getMsg();
        }else{
            tvIntegral.setVisibility(View.GONE);
            tvCoupon.setVisibility(View.GONE);
            ivIntegral.setVisibility(View.VISIBLE);
            ivCoupon.setVisibility(View.VISIBLE);

            llBtn.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.GONE);
            ivAvatar.setBackgroundResource(R.mipmap.avatar_defoult);
            ivAvatar.setImageResource(R.mipmap.avatar_defoult);
        }
        if (app.isDeviceConnection){
            showLog("设备已经连接，显示电量");
            tvPower.setVisibility(View.GONE);
            ivPower.setVisibility(View.VISIBLE);
            if (app.incharge){
                ivPower.setImageResource(R.mipmap.ioc_batteryincharge_0);
            }else{
                switch (app.devicePower){
                    case 1:
                        ivPower.setImageResource(R.mipmap.ioc_battery_0);
                        break;
                    case 2:
                        ivPower.setImageResource(R.mipmap.ioc_battery_25);
                        break;
                    case 3:
                        ivPower.setImageResource(R.mipmap.ioc_battery_50);
                        break;
                    case 4:
                        ivPower.setImageResource(R.mipmap.ioc_battery_75);
                        break;
                    case 5:
                        ivPower.setImageResource(R.mipmap.ioc_battery_100);
                        break;
                }
            }
        }else{
            showLog("设备断开连接");
            tvPower.setVisibility(View.VISIBLE);
            ivPower.setVisibility(View.GONE);
        }
    }



    private void initView() {
        rlInfo = (RelativeLayout) view.findViewById(R.id.rl_user_info);
        rlFriend = (RelativeLayout) view.findViewById(R.id.rl_friend);
        rlData = (RelativeLayout) view.findViewById(R.id.rl_data);
        rlPower = (RelativeLayout) view.findViewById(R.id.rl_powor);
        rlAbout = (RelativeLayout) view.findViewById(R.id.rl_about);
        rlSafe = (RelativeLayout) view.findViewById(R.id.rl_safe);
        rlSetting = (RelativeLayout) view.findViewById(R.id.rl_setting);

        ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
        ibMsg = (ImageButton) view.findViewById(R.id.ib_message);
        rlHasMsg = (RelativeLayout) view.findViewById(R.id.rl_has_msg);
        tvMsgCount = (TextView) view.findViewById(R.id.tv_msg_count);


        llJifen = (LinearLayout) view.findViewById(R.id.ll_jifen);
        llYouhui = (LinearLayout) view.findViewById(R.id.ll_youhui);
        llQiandao = (LinearLayout) view.findViewById(R.id.ll_qiandao);
        tvSign = (TextView) view.findViewById(R.id.tv_sign);
        ivSign = (ImageView) view.findViewById(R.id.iv_sign);
        tvIntegral = (TextView) view.findViewById(R.id.tv_integral);
        ivIntegral = (ImageView) view.findViewById(R.id.iv_integral);
        tvCoupon = (TextView) view.findViewById(R.id.tv_coupon);
        ivCoupon = (ImageView) view.findViewById(R.id.iv_coupon);

        llBtn = (LinearLayout) view.findViewById(R.id.ll_btn);
        llLogin = (LinearLayout) view.findViewById(R.id.ll_login);
        llRegist = (LinearLayout) view.findViewById(R.id.ll_regist);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvPower = (TextView) view.findViewById(R.id.tv_power);
        ivPower = (ImageView) view.findViewById(R.id.iv_power);

        tvUnSucc = (TextView) view.findViewById(R.id.tv_unsucc);

        rlHasMsg.setVisibility(View.GONE);
    }

    private void initEvent() {
        rlInfo.setOnClickListener(this);
        rlFriend.setOnClickListener(this);
        rlData.setOnClickListener(this);
        rlPower.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
        rlSafe.setOnClickListener(this);
        rlSetting.setOnClickListener(this);

        llLogin.setOnClickListener(this);
        llRegist.setOnClickListener(this);
        ibMsg.setOnClickListener(this);

        llJifen.setOnClickListener(this);
        llYouhui.setOnClickListener(this);
        llQiandao.setOnClickListener(this);

        ivAvatar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_user_info:
                if (!isLogin){
                    showToast(mainActivity.getString(R.string.unlogin));
                    intent2Activity(LoginActivity.class);
                }else{
                    intent2Activity(UserInfoActivity.class);
                }
                break;
            case R.id.iv_avatar:
                if (!isLogin){
                    showToast(mainActivity.getString(R.string.unlogin));
                    intent2Activity(LoginActivity.class);
                }else{
                    intent2Activity(UserInfoActivity.class);
                }
                break;
            case R.id.rl_friend:
                if (!isLogin){
                    showToast(mainActivity.getString(R.string.unlogin));
                    intent2Activity(LoginActivity.class);
                }else{
                    intent2Activity(FriendActivity.class);
                }
                break;
            case R.id.rl_data:
                if (!isLogin){
                    showToast(mainActivity.getString(R.string.unlogin));
                    intent2Activity(LoginActivity.class);
                }else{
                    intent2Activity(RecordActivity.class);
                }
                break;
            case R.id.rl_powor:
                break;
            case R.id.rl_about:
                intent2Activity(AboutActivity.class);
                break;
            case R.id.rl_safe:
                if (!isLogin){
                    showToast(mainActivity.getString(R.string.unlogin));
                    intent2Activity(LoginActivity.class);
                }else{
                    intent2Activity(SafeActivity.class);
                }
                break;
            case R.id.rl_setting:
                intent2Activity(SettingActivity.class);
                break;

            case R.id.ll_login:
                intent2Activity(LoginActivity.class);
                break;
            case R.id.ll_regist:
                intent2Activity(RegistActivity.class);
                break;
            case R.id.ib_message:
                if (!isLogin){
                    showToast(mainActivity.getString(R.string.unlogin));
                    intent2Activity(LoginActivity.class);
                }else{
                    intent2Activity(MsgActivity.class);
                }
                break;

            case R.id.ll_jifen:
                if (!isLogin){
                    showToast(mainActivity.getString(R.string.unlogin));
                    intent2Activity(LoginActivity.class);
                }else{
                    Intent it = new Intent(mContext,IntegralActivity.class);
                    it.putExtra("integral",integral);
                    startActivity(it);
                }
                break;
            case R.id.ll_youhui:
                if (!isLogin){
                    showToast(mainActivity.getString(R.string.unlogin));
                    intent2Activity(LoginActivity.class);
                }else{
                    intent2Activity(CouponActivity.class);
                }
                break;
            case R.id.ll_qiandao:
                if (!isLogin){
                    showToast(mainActivity.getString(R.string.unlogin));
                    intent2Activity(LoginActivity.class);
                }else{
                    if (isSign){
                        showToast(mainActivity.getString(R.string.Already_sign_in));
                    }else{
                        addsign();
                    }
//                intent2Activity(SignActivity.class);
                }
                break;
        }
    }

    /**
     * 签到
     */
    private void addsign() {
        RequestParams params = new RequestParams(Urls.addSign);
        params.addBodyParameter("token",user.getToken());
        params.addBodyParameter("sid",user.getSid()+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
                        isSign = true;
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("isSign",isSign);
                        editor.commit();
                        setSign();
                        getIntegral();
                    }else if(returnCode.equals(ALREADYLOGIN)){
                        showToast("您的登录信息已失效或在其他客户端登录，请重新登录");
                        user = new User();
                        userPersist.setUser(mContext,user);
                        onResume();
                        intent2Activity(LoginActivity.class);
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
            }
        });
    }

    /**
     * 设置签到UI
     */
    private void setSign() {
//        isSign = sp.getBoolean("isSign",false);
        llQiandao.setClickable(true);
        if (isLogin&&isSign){
            tvSign.setText(getString(R.string.signed));
            ivSign.setBackgroundResource(R.mipmap.icon_sign);
        }else{
            tvSign.setText(getString(R.string.sign));
            ivSign.setBackgroundResource(R.mipmap.icon_qiandao);
        }

    }

    /**
     * 获取积分
     */
    public void getIntegral() {
        Logger.e(user.toString());
        RequestParams params = new RequestParams(Urls.getIntegral);
        params.addBodyParameter("token",user.getToken());
        params.addBodyParameter("sid",user.getSid()+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
                        integral = object.getInt("integral");
                        tvIntegral.setText(integral+"");
                    }else if(returnCode.equals(ALREADYLOGIN)){
                        showToast("您的登录信息已失效或在其他客户端登录，请重新登录");
                        user = new User();
                        userPersist.setUser(mContext,user);
                        onResume();
                        intent2Activity(LoginActivity.class);
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
            }
        });
    }

    /**
     * 获取优惠券
     */
    public void getCoupon() {
        final RequestParams params = new RequestParams(Urls.getCoupon);
        params.addBodyParameter("token",user.getToken());
        params.addBodyParameter("sid",user.getSid()+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String return_code = object.getString("return_code");
                    if(return_code.equals(SUCCESS)){
                        org.json.JSONArray array = object.getJSONArray("listc");
                        coupon = array.length();
                        tvCoupon.setText(coupon+"");
                    }else if(return_code.equals(ALREADYLOGIN)){
                        showToast("您的登录信息已失效或在其他客户端登录，请重新登录");
                        user = new User();
                        userPersist.setUser(mContext,user);
                        onResume();
                        intent2Activity(LoginActivity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e(ex.getLocalizedMessage());
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {

            }
        });
    }


    private boolean isPerfect(){
        if (info.getUrl() == null || info.getUrl().equals("")){
            return false;
        }
        if (info.getAddress() == null || info.getAddress().equals("")){
            return false;
        }
        if (info.getBirthday() <= 0){
            return false;
        }
        if (info.getBlood_type() == null || info.getBlood_type().equals("")){
            return false;
        }
        if (info.getName() == null || info.getName().equals("")){
            return false;
        }
        if (info.getOccupation() == null || info.getOccupation().equals("")){
            return false;
        }
        return true;
    }


    /**
     * 获取消息
     */
    private void getMsg() {
        RequestParams params = new RequestParams(Urls.getNewsList);
        params.addBodyParameter("token", user.getToken());
        params.addBodyParameter("sid", user.getSid() + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                int count = 0;
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals("1000")) {
                        org.json.JSONArray array = object.getJSONArray("listn");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Msg msg = new Msg();
                            msg.setValue(obj.getString("value"));
                            msg.setType(obj.getInt("type"));
                            msg.setName(obj.getString("name"));
                            msg.setTime(obj.getLong("time"));
                            msg.setSid(obj.getInt("sid"));
                            if (msg.getType() != 1) {
                                count ++;
                            }
                        }
                        if (count > 0){
                            rlHasMsg.setVisibility(View.VISIBLE);
                            if (count > 99){
                                tvMsgCount.setText("...");
                            }else{
                                tvMsgCount.setText(count + "");
                            }
                        }else{
                            rlHasMsg.setVisibility(View.GONE);
                        }
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
            }
        });
    }

}
