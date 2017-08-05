package com.zhongzhiyijian.eyan.net;

/**
 * Created by Administrator on 2016/9/7.
 */
public interface Urls {
    String BASE_URL = "http://114.55.233.234:8080/xaly/";;
//    String BASE_URL = "http://192.168.31.219:8080/xaly/";
    /**
     * 获取积分
     */
    String share = BASE_URL + "yaoqing.html?code=";
    /**
     * 获取积分
     */
    String getIntegral = BASE_URL + "getintegral.do";
    /**
     * 获取积分记录
     */
    String getIntegralRecords = BASE_URL + "getintegralrecords.do";
    /**
     * 获取消息列表
     */
    String getNewsList = BASE_URL + "getnewslist.do";
    /**
     * 修改消息状态
     */
    String modifyNews = BASE_URL + "modifynews.do";
    /**
     * 获取优惠卷列表
     */
    String getCoupon = BASE_URL + "getcoupon.do";
    /**
     * 签到
     */
    String addSign = BASE_URL + "addsign.do";
    /**
     * 签到信息
     */
    String getSign = BASE_URL + "getsign.do";
    /**
     * 修改密码
     */
    String modifyPassword = BASE_URL + "modifypassword.do";
    /**
     * 意见反馈
     */
    String addFeedBack = BASE_URL + "addfeedback.do";
    /**
     * 设备使用记录
     */
    String getEquipmentRecord = BASE_URL + "getequipmentrecord.do";
    /**
     * 上传设备使用记录
     */
    String addEquipmentRecord = BASE_URL + "addequipmentrecord.do";
    /**
     * 登陆
     */
    String login = BASE_URL + "login.do";
    /**
     * 获取个人资料
     */
    String getData = BASE_URL + "getdata.do";
    /**
     * 修改个人资料
     */
    String addData = BASE_URL + "adddata.do";
    /**
     * 上传图片
     */
    String addPicture = BASE_URL + "addpicture.do";


    /**
     * 获取积分商城信息
     */
    String getcouponintegral = BASE_URL + "getcouponintegral.do";
    /**
     * 积分兑换优惠卷
     */
    String addexchange = BASE_URL + "addexchange.do";



    /**
     * 邮箱改密码发验证码
     */
    String emailGetCode = BASE_URL + "codepassword.do";
    /**
     * 邮箱改密码
     */
    String emailChangePwd = BASE_URL + "codepassword.do";
    /**
     * 邮箱改密码
     */
    String getEmail = BASE_URL + "wjmm.do";



    /**
     * 绑定邮箱发验证码
     */
    String modifymailbox1 = BASE_URL + "modifymailbox.do";
    /**
     * 绑定邮箱
     */
    String modifymailbox2 = BASE_URL + "modifymailbox.do";
    /**
     * 换邮箱  旧发送验证码
     */
    String modifymailbox3 = BASE_URL + "modifymailbox.do";
    /**
     * 验证旧验证码  发新验证码
     */
    String modifymailbox5 = BASE_URL + "modifymailbox.do";
    /**
     * 验证新验证码更换邮箱
     */
    String modifymailbox6 = BASE_URL + "modifymailbox.do";
    /**
     * 换邮箱  验证旧邮箱验证码
     */
    String modifymailbox4 = BASE_URL + "modifymailbox.do";




    /**
     * 更换手机发送旧手机验证码
     */
    String replacemobile1 = BASE_URL + "replacemobile.do";
    /**
     * 验证旧手机验证码
     */
    String replacemobile2 = BASE_URL + "replacemobile.do";
    /**
     * 更换手机验证旧验证码发送新验证码
     */
    String replacemobile3 = BASE_URL + "replacemobile.do";
    /**
     * 验证验证码更换手机号
     */
    String replacemobile4 = BASE_URL + "replacemobile.do";

}
