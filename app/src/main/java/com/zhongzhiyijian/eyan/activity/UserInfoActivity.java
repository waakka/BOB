package com.zhongzhiyijian.eyan.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.orhanobut.logger.Logger;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.adapter.DeviceNumAdapter;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.base.BaseApplication;
import com.zhongzhiyijian.eyan.entity.DeviceNum;
import com.zhongzhiyijian.eyan.net.Urls;
import com.zhongzhiyijian.eyan.service.LocationService;
import com.zhongzhiyijian.eyan.util.XUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import de.hdodenhof.circleimageview.CircleImageView;


public class UserInfoActivity extends BaseActivity implements DeviceNumAdapter.CallBack{

    @ViewInject(R.id.ib_back)
    private ImageButton ibBack;
    @ViewInject(R.id.iv_avatar)
    private CircleImageView ivAvatar;
    @ViewInject(R.id.et_name)
    private EditText etName;
    @ViewInject(R.id.rg_sex)
    private RadioGroup rgSex;
    @ViewInject(R.id.rb_male)
    private RadioButton rbMale;
    @ViewInject(R.id.rb_female)
    private RadioButton rbFemale;
    @ViewInject(R.id.et_birthday)
    private EditText etBirthday;
    @ViewInject(R.id.et_occupation)
    private EditText etOccupation;
    @ViewInject(R.id.et_blood_type)
    private EditText etBloodType;
    @ViewInject(R.id.et_area)
    private EditText etArea;
    @ViewInject(R.id.et_device_id)
    private EditText etDeviceId;
    @ViewInject(R.id.tv_add_device)
    private TextView tvAddDevice;
    @ViewInject(R.id.lv_device)
    private ListView lvDevices;
    @ViewInject(R.id.btn_commit)
    private Button btnCommit;
    @ViewInject(R.id.iv_scan)
    private ImageView ivScan;



    public static int CAMERA_REQUEST_CODE = 1;
    public static int GALLERY_REQUEST_CODE = 2;
    public static int CROP_REQUEST_CODE = 3;
    public static int SCAN_REQUEST_CODE = 4;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private int yearS,mouthS,dayS;

    private AlertDialog avatarDialog;

    private String [] occupations;
    private String [] bloodGroup;

    private List<DeviceNum> deviceNums;
    private DeviceNumAdapter numAdapter;

    private String name;
    private int sex;
    private String url = "";
    private long birthday;
    private String blood_type;
    private String occupation;
    private String address;

    private LocationService locationService;

    private DbManager db;

    int curOccupation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_info);

        x.view().inject(this);

        occupations = new String[]{getString(R.string.o1),getString(R.string.o2)
                ,getString(R.string.o3),getString(R.string.o4),getString(R.string.o5),getString(R.string.o6)
                ,getString(R.string.o7),getString(R.string.o8),getString(R.string.o9),getString(R.string.o10)
                ,getString(R.string.o11),getString(R.string.o12)};



        bloodGroup = new String []{"A","B","O","AB"};

        setData();

        deviceNums = new ArrayList<>();
        db = x.getDb(XUtil.getDaoConfig());
        try {
            List<DeviceNum> all = db.findAll(DeviceNum.class);
            if (all!=null&&all.size()>0){
                deviceNums.addAll(all);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        numAdapter = new DeviceNumAdapter(mContext,deviceNums,UserInfoActivity.this);
        lvDevices.setAdapter(numAdapter);
        setListViewHeightBasedOnChildren(lvDevices);

        initEvent();

        initLocationService();
    }

    private void initLocationService() {
        // -----------location config ------------
        locationService = ((BaseApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
    }

    private void initEvent() {
        rbMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sex = 1;
                }else{
                    sex = 0;
                }
            }
        });
        lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                AlertDialog dialog;
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setTitle("删除").setMessage("是否删除当前设备？")
//                        .setPositiveButton(getString(R.string.dialog_done_device_delete), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                deviceNums.remove(position);
//                                numAdapter.notifyDataSetChanged();
//                            }
//                        }).setNegativeButton(getString(R.string.dialog_cancle_device_delete), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog = builder.create();
//                dialog.show();
            }
        });
    }

    private void setData() {
        name = info.getName();
        sex = info.getSex();
        url = info.getUrl();
        birthday = info.getBirthday();
        blood_type = info.getBlood_type();
        occupation = info.getOccupation();
        Logger.e("当前用户信息==" + info.toString());

        for(int i = 0 ; i < occupations.length ; i ++){
            if(occupations[i].equals(occupation)){
                curOccupation = i;
            }

        }

        address = info.getAddress();
        if (address.equals("-1")){
            address = "";
            info.setAddress(address);
        }
        if (occupation.equals("-1")){
            occupation = "";
            info.setOccupation(occupation);
        }
        if (blood_type.equals("-1")){
            blood_type = "";
            info.setBlood_type(blood_type);
        }
        if (blood_type.contains("型")){
            blood_type = blood_type.substring(0,blood_type.length() - 1);
        }
        if (name.equals("-1")){
            name = "";
            info.setName(name);
        }

        etName.setText(name);
        if (sex==0){
            rbFemale.setChecked(true);
        }else{
            rbMale.setChecked(true);
        }
        String birthdayStr = sdf.format(new Date(birthday*1000));
        String[] split = birthdayStr.split("-");
        if (yearS <= 1910){
            yearS = Integer.parseInt(split[0]);
            mouthS = Integer.parseInt(split[1]);
            dayS = Integer.parseInt(split[2]);
        }

        if(birthday == 0){
            birthdayStr = "";
        }
        etBirthday.setText(birthdayStr);

        etBloodType.setText(blood_type);
        etOccupation.setText(occupations[curOccupation]);
        etArea.setText(address);
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

    }


    @Event(type = View.OnClickListener.class,value = R.id.ib_back)
    private void backClick(View view) {
        finish();
    }

    /**
     * 设置头像
     * @param view
     */
    @Event(type = View.OnClickListener.class,value = R.id.iv_avatar)
    private void avatarClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_avatar,null);
        TextView tvCamera = (TextView) v.findViewById(R.id.tv_camera);
        TextView tvGrllary = (TextView) v.findViewById(R.id.tv_gallary);

        builder.setView(v);
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarDialog.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });
        tvGrllary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_REQUEST_CODE);
            }
        });
        avatarDialog = builder.create();
        avatarDialog.show();
    }


    /**
     * 选择生日
     * @param view
     */
    @Event(type = View.OnClickListener.class,value = R.id.et_birthday)
    private void choseBirthday(View view){
        DatePicker picker  = new DatePicker(UserInfoActivity.this,DatePicker.YEAR_MONTH_DAY);
        Calendar calendar = Calendar.getInstance();
        final int y = calendar.get(Calendar.YEAR);
        final int m = calendar.get(Calendar.MONTH);
        final int d = calendar.get(Calendar.DAY_OF_MONTH);
        picker.setRange(1900,y);
        picker.setSelectedItem(yearS,mouthS,dayS);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                if(y < Integer.parseInt(year)){
                    showToast("您选择的日期已超出当前日期，请重新选择");
                    return;
                }else if(y == Integer.parseInt(year)){
                    if(m < Integer.parseInt(month)){
                        showToast("您选择的日期已超出当前日期，请重新选择");
                        return;
                    }else if(m == Integer.parseInt(month)){
                        if(d < Integer.parseInt(day)){
                            showToast("您选择的日期已超出当前日期，请重新选择");
                            return;
                        }
                    }
                }
                yearS = Integer.parseInt(year);
                mouthS = Integer.parseInt(month);
                dayS = Integer.parseInt(day);
                String birthdayStr = year + "-" + month + "-" + day;
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR,yearS);
                c.set(Calendar.MONTH,mouthS-1);
                c.set(Calendar.DAY_OF_MONTH,dayS);
                etBirthday.setText(birthdayStr);
                birthday = c.getTimeInMillis()/1000;
            }
        });
        picker.show();
    }

    /**
     * 选择地区
     * @param view
     */
    @Event(type = View.OnClickListener.class,value = R.id.et_area)
    private void choseArea(View view){
        try {
            ArrayList<AddressPicker.Province> data = new ArrayList<AddressPicker.Province>();
            String json = ConvertUtils.toString(getAssets().open("city.json"));
            data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
            AddressPicker picker = new AddressPicker(this, data);
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(String province, String city, String county) {
                    address = province+" "+city+ " " +county;
                    etArea.setText(address);
                }
            });
            picker.show();
        } catch (Exception e) {
            showToast(e.toString());
        }
    }

    /**
     * 选择职业
     * @param view
     */
    @Event(type = View.OnClickListener.class,value = R.id.et_occupation)
    private void choseOccupation(View view){
        OptionPicker picker = new OptionPicker(UserInfoActivity.this,occupations);
        picker.setOffset(2);
        picker.setSelectedIndex(1);
        picker.setTextSize(16);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                occupation = option;
                etOccupation.setText(occupation);
            }
        });
        picker.show();
    }

    /**
     * 选择血型
     * @param view
     */
    @Event(type = View.OnClickListener.class,value = R.id.et_blood_type)
    private void choseBloodGroup(View view){
        OptionPicker picker = new OptionPicker(UserInfoActivity.this,bloodGroup);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                blood_type = option;
                etBloodType.setText(blood_type);
            }
        });
        picker.show();
    }

    /**
     * 添加设备
     * @param view
     */
    @Event(type = View.OnClickListener.class,value = R.id.tv_add_device)
    private void addDevice(View view){
        String num = etDeviceId.getText().toString();
        if (num != null && !num.equals("")){
            DeviceNum n = new DeviceNum();
            n.setName(num);
            deviceNums.add(n);
            numAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(lvDevices);
            etDeviceId.setText("");
        }
    }

    /**
     * 扫描二维码
     * @param view
     */
    @Event(type = View.OnClickListener.class,value = R.id.iv_scan)
    private void scanCode(View view){
        startActivityForResult(new Intent(this, CaptureActivity.class),SCAN_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE){
            if(data == null){
                return;
            }else{
                Bundle extras = data.getExtras();
                if (extras!=null){
                    Bitmap bm = extras.getParcelable("data");
                    startImageZoom(saveBitmap(bm));
                }
            }
        }else if(requestCode == GALLERY_REQUEST_CODE){
            if(data == null){
                return;
            }else{
                Uri uri;
                uri = data.getData();
                startImageZoom(convertUri(uri));
            }
        }else if(requestCode == CROP_REQUEST_CODE){
            if(data == null){
                return;
            }else{
                Bundle extras = data.getExtras();
                Bitmap bm = extras.getParcelable("data");
                ivAvatar.setImageBitmap(bm);
                sendImg();
            }
        }else if(requestCode == SCAN_REQUEST_CODE){
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                String result=bundle.getString("result");
                etDeviceId.setText(result);
            }
        }
    }

    private Uri convertUri(Uri uri){
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);
            Bitmap bm = BitmapFactory.decodeStream(is);
            is.close();
            return saveBitmap(bm);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存图片
     * @param bm
     * @return
     */
    private Uri saveBitmap(Bitmap bm){
        File file = new File(avatarPath);
        if (!file.exists()){
            file.mkdirs();
        }
        File img = new File(file,"avatar.png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.JPEG,85,fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 裁剪图片
     * @param uri
     */
    private void startImageZoom(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX",150);
        intent.putExtra("outputY",150);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG);
        intent.putExtra("return-data",true);
        startActivityForResult(intent,CROP_REQUEST_CODE);
    }

    /**
     * 上传图片
     */
    private void sendImg(){
        File file = new File(avatarPath);
        File img = new File(file,"avatar.png");
        RequestParams params = new RequestParams(Urls.addPicture);
        params.addParameter("file",img);
        params.addBodyParameter("file",img);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
                        org.json.JSONArray list = object.getJSONArray("list");
                        url = list.getJSONObject(0).getString("url");
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
     * 自动定位（百度地图）
     * @param view
     */
    @Event(type = View.OnClickListener.class,value = R.id.ll_auto_location)
    private void location(View view){
        pd.setTitle("定位");
        pd.setMessage("正在定位中。。。");
        pd.show();
        locationService.start();
    }


    /**
     * 提交信息
     * @param view
     */
    @Event(type = View.OnClickListener.class,value = R.id.btn_commit)
    private void commit(View view){
        String devicesStr = "";
        if (deviceNums.size() <= 0){
            Logger.e("当前无设备");
            devicesStr = "-1";
        }
        for (int i = 0 ; i < deviceNums.size() ; i++){
            devicesStr += deviceNums.get(i).getName() + ",";
        }
        name = etName.getText().toString();

        if (name==null||name.equals("")){
            showToast("昵称不能为空！");
            return;
        }

        if (blood_type.contains("型")){
            blood_type = blood_type.substring(0,blood_type.length() - 1);
        }
        RequestParams params = new RequestParams(Urls.addData);
        params.addBodyParameter("token",user.getToken());
        params.addBodyParameter("sid",user.getSid()+"");
        params.addQueryStringParameter("name",name);
        params.addParameter("sex",sex);
        params.addParameter("url",url);
        params.addBodyParameter("birthday",birthday+"");
        params.addQueryStringParameter("blood_type",blood_type);
        params.addQueryStringParameter("occupation",occupation);
        params.addQueryStringParameter("address",address);
        params.addQueryStringParameter("device",devicesStr);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
                        info.setName(name);
                        info.setAddress(address);
                        info.setBirthday(birthday);
                        info.setBlood_type(blood_type+"型");
                        info.setOccupation(occupation);
                        info.setSex(sex);
                        info.setUrl(url);
                        infoPersist.setUserInfo(mContext,info);
                        db.delete(DeviceNum.class);
                        db.save(deviceNums);
                        showToast("修改成功！");
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (DbException e) {
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


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 百度地图定位回调
     */
    private BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                locationService.stop();
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType定位类型 : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nCountry国家名称 : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode城市编码 : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity城市 : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict区 : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet街道 : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr地址信息 : ");// 地址信息
                sb.append(location.getAddrStr());
                if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }

                if (pd.isShowing()){
                    pd.dismiss();
                }

//                showToast(sb.toString());
//                showLog(sb.toString());
                String province = location.getProvince();
                String city = location.getCity();
                String district = location.getDistrict();
                String add = province + city + district;
                if (add == null||add.equals("")||add.contains("null")){
                    showToast("定位失败，请重新定位或手动添加");
                }else{
                    address = province + "" + city +  "" + district;
                    etArea.setText(address);
                }
            }
        }

    };

    @Override
    public void del(final int position) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("删除").setMessage("是否删除当前设备？")
                .setPositiveButton(getString(R.string.dialog_done_device_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deviceNums.remove(position);
                        numAdapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(lvDevices);
                    }
                }).setNegativeButton(getString(R.string.dialog_cancle_device_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }
}
