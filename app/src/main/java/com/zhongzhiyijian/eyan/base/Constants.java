package com.zhongzhiyijian.eyan.base;

import android.os.Environment;

public interface Constants {

	String APP_ID = "wx4dbc914b50fd5021";

	String signAlarm = "signAlarm";

	String SP_NAME = "sp_bob";
	boolean isShowLog = true;
	String localPath = Environment.getExternalStorageDirectory().getPath() + "/BOB";
	String avatarPath = localPath + "/avatar";
	/**
	 * 音乐播放状态改变
	 */
	String MUSIC_HAS_CHANGED = "musicHasChanged";


	/**
	 * Log TAG
	 */
	public static final String TAG = "cn.com.tarena.music_player";
	/**
	 * 播放模式：顺序播放
	 */
	int PLAY_MODE_ORDERED = 0;
	/**
	 * 播放模式：随机播放
	 */
	int PLAY_MODE_RANDOM = 1;
	/**
	 * 播放模式：随机播放
	 */
	int PLAY_MODE_ONLY = 2;
	//
	//
	// 以下是Activity发出的广播
	//
	//
	/**
	 * Activity发出的，播放按钮被点击
	 */
	String ACTIVITY_PLAY_BUTTON_CLICK = "cn.com.tarena.music_player.intent.action.PLAY_BUTTON_CLICK";
	/**
	 * Activity发出的，当前歌曲被点击时未播放
	 */
	String ACTIVITY_CURRENT_MUSIC_CLICK = "currentMusicClick";
	/**
	 * Activity发出的，当前歌曲播放是被删除
	 */
	String ACTIVITY_CURRENT_MUSIC_DELETE = "currentMusicDelete";
	/**
	 * Activity发出的，上一首按钮被点击
	 */
	String ACTIVITY_PREVIOUS_BUTTON_CLICK = "cn.com.tarena.music_player.intent.action.PREVIOUS_BUTTON_CLICK";
	/**
	 * Activity发出的，下一首按钮被点击
	 */
	String ACTIVITY_NEXT_BUTTON_CLICK = "cn.com.tarena.music_player.intent.action.NEXT_BUTTON_CLICK";
	/**
	 * Activity发出的，播放的歌曲发生变化
	 */
	String ACTIVITY_MUSIC_INDEX_CHANGED = "cn.com.tarena.music_player.intent.action.MUSIC_INDEX_CHANGED";
	/**
	 * Activity发出的，进度条被拖拽
	 */
	String ACTIVITY_SEEKBAR_CHANGED = "cn.com.tarena.music_player.intent.action.SEEKBAR_CHANGED";
	//
	//
	// 以下是play music Service发出的广播
	//
	//
	/**
	 * Service发出的，播放歌曲
	 */
	String SERVICE_PLAYER_PLAY = "cn.com.tarena.music_player.intent.action.PLAYER_PLAY";
	/**
	 * Service发出的，暂停播放歌曲
	 */
	String SERVICE_PLAYER_PAUSE = "cn.com.tarena.music_player.intent.action.PLYAER_PAUSE";
	/**
	 * Service发出的，更新进度播放歌曲
	 */
	String SERVICE_UPDATE_PROGRESS = "cn.com.tarena.music_player.intent.action.UPDATE_PROGRESS";




	//
	//
	//一下是BTService发出的广播
	//
	//
	String START_BT = "startBt";
	String STOP_BT = "stopBt";

	String STOPED_BT = "stopedBt";
	String STARTED_BT = "startedBt";

	String START_DISCOVERY = "startDiscovery";

	//search界面的action
	String SEARCH_START_CONNECT = "searchStartConnect";
	String SEARCH_DEVICE_CONNECT_SUCCESS = "searchDeviceConnectSuccess";
	String SEARCH_DEVICE_CONNECT_FAILED = "searchDeviceConnectFailed";
	//删除当前连接
	String SEARCH_DEVICE_CONNECT_DELETE = "searchDeviceConnectDelete";
	String SEARCH_DEVICE_CONNECT_STATUS_CHANGED = "searchDeviceConnectStatusChanged";
	

	//bob界面的action
	String BOB_START_CONNECT = "bobDtartConnect";
	String BOB_DEVICE_CONNECT_SUCCESS = "bobDeviceConnectSuccess";
	String BOB_DEVICE_CONNECT_FAILED = "bobDeviceConnectFailed";
	//删除当前连接
	String BOB_DEVICE_CONNECT_DELETE = "bobDeviceConnectDelete";
	String BOB_DEVICE_CONNECT_STATUS_CHANGED = "bobDeviceConnectStatusChanged";
	



	String BTSOCKET_SENG_MSG = "btSocketSendMsg";

	String DEVICE_STATUS_CHANGED = "deviceStatusChanged";

	String DELETE_DEVICE = "deleteDevice";


	/**
	 * 未知错误(服务器错误)
	 */
	String UNKNOWERROR = "0000";
	/**
	 * 成功
	 */
	String SUCCESS = "1000";
	/**
	 * 无访问权限(token错误)
	 */
	String ILLEGALOPERATION = "0002";
	/**
	 * 此帐号不存在
	 */
	String NOACCESS = "0003";
	/**
	 * 密保邮箱已经绑定过邮箱
	 */
	String ALREADEMAIL = "0008";
	/**
	 * 密码错误
	 */
	String PWDERROR = "0004";
	/**
	 * 验证错误
	 */
	String CODEERROR = "0005";
	/**
	 * 重复提交
	 */
	String RESUBMIT = "0006";
	/**
	 * 账号已存在
	 */
	String ALREADYSHOW = "0007";
	/**
	 * 帐号在别处登陆（token 失效）
	 */
	String ALREADYLOGIN = "0100";
	/**
	 * 验证码发送失败请检查邮箱
	 */
	String CODE_SEND_ERROR = "0009";

	String xieyi = "用户注册协议\n" +
			"1. 特别提示\n" +
			"1.1\n" +
			"中智益健（深圳）科技有限公司（以下称\"中智益健\"）同意按照本协议的规定及其不时发布的操作规则提供设备服务（以下称\"设备服务\"），为获得设备服务，服务使用人（以下称\"用户\"）注册前应当确保了解本协议全部内容，注册即表示在独立思考的基础上认可、同意本协议的全部条款。\n" +
			"1.2\n" +
			"用户注册成功后，中智益健将为用户基于设备服务使用的客观需要而在申请、注册设备服务时，按照注册要求提供的帐号开通服务，用户有权在中智益健为其开通、并同意向其提供设备服务的基础上使用服务。该用户帐号和密码由用户负责保管。 \n" +
			"1.3\n" +
			"为提高用户的设备服务使用感受和满意度，用户同意中智益健将基于用户的操作行为对用户数据进行调查研究和分析，从而进一步优化服务。\n" +
			"2. 服务内容\n" +
			"2.1\n" +
			"设备服务的具体内容由中智益健根据实际情况提供，包括但不限于授权用户通过其帐号，使用积分、优惠券、使用记录等服务，中智益健有权对其提供的服务或产品形态进行升级或其他调整，并将及时更新页面/告知用户。\n" +
			"2.2\n" +
			"用户理解，中智益健仅提供与设备服务相关的技术服务等，除此之外与相关网络服务有关的设备（如个人电脑、手机、及其他与接入互联网或移动网有关的装置）及所需的费用（如为接入互联网而支付的电话费及上网费、为使用移动网而支付的手机费）均应由用户自行负担。\n" +
			"3. 服务变更、中断或终止\n" +
			"3.1\n" +
			"鉴于设备联网服务的特殊性（包括但不限于服务器的稳定性问题、恶意的网络攻击等行为的存在及其他中智益健无法控制的情形），用户同意中智益健有权随时中断或终止部分或全部的设备服务，若发生该等中断或中止设备服务的情形，中智益健将尽可能及时通过网页公告、系统通知、私信、短信邮箱提醒或其他合理方式通知受到影响的用户。\n" +
			"3.2\n" +
			"用户理解，中智益健需要定期或不定期地对提供设备服务的平台及相关的设备进行检修或者维护，如因此类情况而造成服务在合理时间内的中断，中智益健无需为此承担任何责任，但中智益健应尽可能事先进行通告。\n" +
			"3.3\n" +
			"如发生下列任何一种情形，中智益健有权随时中断或终止向用户提供本协议项下的设备服务（包括收费服务）而无需对用户或任何第三方承担任何责任：\n" +
			"3.3.1 用户提供的个人资料不真实；\n" +
			"3.3.2 用户违反法律法规国家政策或本协议中规定的使用规则；\n" +
			"4. 使用规则\n" +
			"4.1\n" +
			"用户注册账号时，应当使用真实身份信息及个人资料，若用户的个人资料有任何变动，用户应及时更新。\n" +
			"4.2\n" +
			"用户可自行编辑注册信息中的账号名称、头像等，但应遵守“七条底线”以及相关管理规定，不得含有违法和不良信息。\n" +
			"4.3\n" +
			"如用户违反前述约定，依据相关法律、法规及国家政策要求，中智益健有权随时采取不予注册、通知限期改正、注销登记用户账号、中止或终止用户对设备服务的使用等措施。\n" +
			"4.4\n" +
			"中智益健将建立健全用户信息安全管理制度、落实技术安全防控措施。中智益健将对用户使用设备服务过程中涉及的用户隐私内容加以保护。\n" +
			"4.5\n" +
			"由于设备服务的存在前提是用户在申请开通设备服务的过程中所提供的帐号，则用户不应将其帐号、密码转让或出借予他人使用。如用户发现其帐号或设备服务遭他人非法使用，应立即通知中智益健。因黑客行为或用户的保管疏忽导致帐号、密码及设备服务遭他人非法使用，中智益健有权拒绝承担任何责任。\n" +
			"4.6\n" +
			"用户同意中智益健在提供设备服务过程中以各种方式投放各种商业性广告或其他任何类型的商业信息（包括但不限于在中智益健网站的任何页面上投放广告），并且，用户同意接受中智益健通过电子邮件或其他方式向用户发送商品促销或其他相关商业信息。\n" +
			"4.7\n" +
			"中智益健针对某些特定的设备服务的使用通过各种方式（包括但不限于网页公告、系统通知、私信、短信提醒等）作出的任何声明、通知、警示等内容视为本协议的一部分，用户如使用该等设备服务，视为用户同意该等声明、通知、警示的内容。\n" +
			"5. 知识产权\n" +
			"5.1\n" +
			"中智益健是设备平台的所有权及知识产权权利人。\n" +
			"5.2\n" +
			"中智益健是设备平台产品的所有权及知识产权权利人。上述设备产品指的是中智益健通过设备平台为用户提供的包括但不限于平台应用程序、软件、服务等。\n" +
			"5.3\n" +
			"中智益健是设备平台及产品中所有信息内容的所有权及知识产权权利人。前述信息内容包括但不限于程序代码、界面设计、版面框架、数据资料、账号、文字、图片、图形、图表、音频、视频等，除按照法律法规规定应由相关权利人享有权利的内容以外。\n" +
			"5.4\n" +
			"鉴于以上，用户理解并同意：\n" +
			"5.4.1 \n" +
			"未经中智益健及相关权利人同意，用户不得对上述功能、软件、服务进行反向工程 （reverse engineer）、反向编译（decompile）或反汇编（disassemble）等；同时，不得将上述内容或资料在任何媒体直接或间接发布、播放、出于播放或发布目的而改写或再发行，或者用于其他任何目的。\n" +
			"5.4.2 \n" +
			"在尽商业上的合理努力的前提下，中智益健并不就上述功能、软件、服务及其所包含内容的延误、不准确、错误、遗漏或由此产生的任何损害，以任何形式向用户或任何第三方承担任何责任；\n" +
			"5.4.3\n" +
			"为更好地维护设备服务，中智益健保留在任何时间内以任何方式处置上述由中智益健享受所有权及知识产权的产品或内容，包括但不限于修订、屏蔽、删除或其他任何法律法规允许的处置方式。\n" +
			"6. 隐私保护\n" +
			"6.1\n" +
			"本协议所指的“隐私”包括《电信和互联网用户个人信息保护规定》第4条关于个人信息、《最高人民法院关于审理利用信息网络侵害人身权益民事纠纷案件适用法律若干问题的规定》第12条关于个人隐私、以及未来不时制定或修订的法律法规中明确规定的隐私应包括的内容。\n" +
			"6.2\n" +
			"保护用户隐私和其他个人信息是中智益健的一项基本政策，中智益健司保证不会将单个用户的注册资料及用户在使用设备服务时存储在中智益健的非公开内容用于任何非法的用途，且保证将单个用户的注册资料进行商业上的利用时应事先获得用户的同意，但下列情况除外：\n" +
			"6.2.1 事先获得用户的明确授权；\n" +
			"6.2.2 为维护社会公共利益；\n" +
			"6.2.3 学校、科研机构等基于公共利益为学术研究或统计的目的，经自然人用户书面同意，且公开方式不足以识别特定自然人；\n" +
			"6.2.4 用户自行在网络上公开的信息或其他已合法公开的个人信息；\n" +
			"6.2.5 以合法渠道获取的个人信息；\n" +
			"6.2.6 用户侵害中智益健合法权益，为维护前述合法权益且在必要范围内；\n" +
			"6.2.7 根据相关政府主管部门的要求；\n" +
			"6.2.8 根据相关法律法规或政策的要求；\n" +
			"6.2.9 其他必要情况。\n" +
			"7. 免责声明\n" +
			"7.1\n" +
			"用户在使用设备服务的过程中应遵守国家法律法规及政策规定，因其使用设备服务而产生的行为后果由用户自行承担。\n" +
			"7.2\n" +
			"对于因不可抗力或中智益健不能控制的原因造成的设备服务中断或其它缺陷，中智益健不承担任何责任，但将尽力减少因此而给用户造成的损失和影响。\n" +
			"7.3\n" +
			"用户同意，对于中智益健向用户提供的下列产品或者服务的质量缺陷本身及其引发的任何损失，中智益健无需承担任何责任：\n" +
			"7.3.1 中智益健向用户免费提供的设备服务；\n" +
			"7.3.2 中智益健向用户赠送的任何产品或者服务；\n" +
			"7.4\n" +
			"设备平台上提供的产品或服务（包括但不限于积分及优惠券），如未标明使用期限、或者其标明的使用期限为“永久”、“无限期”或“无限制”的，则其使用期限为自用户获得该积分或优惠券之日起至该产品或服务在设备平台下线之日为止。\n" +
			"8. 违约赔偿\n" +
			"8.1\n" +
			"如因中智益健违反有关法律、法规或本协议项下的任何条款而给用户造成损失，中智益健同意承担由此造成的损害赔偿责任。\n" +
			"8.2\n" +
			"用户同意保障和维护中智益健及其他用户的利益，如因用户违反有关法律、法规或本协议项下的任何条款而给中智益健或任何其他第三人造成损失，用户同意承担由此造成的损害赔偿责任。\n" +
			"9. 协议修改\n" +
			"9.1\n" +
			"中智益健有权随时修改本协议的任何条款，一旦本协议的内容发生变动，中智益健将会在通过各种方式（包括但不限于网页公告、系统通知、私信、短信提醒等）公布修改之后的协议内容，若用户不同意上述修改，则可以选择停止使用设备服务。\n" +
			"9.2\n" +
			"如果不同意中智益健对本协议相关条款所做的修改，用户有权停止使用设备服务。如果用户继续使用设备服务，则视为用户接受中智益健对本协议相关条款所做的修改。\n" +
			"10. 通知送达\n" +
			"10.1\n" +
			"本协议项下中智益健对于用户所有的通知均可通过网页公告、电子邮件、系统通知、设备管理帐号主动联系、私信、手机短信或常规的信件传送等方式进行；该等通知于发送之日视为已送达收件人。\n" +
			"10.2\n" +
			"用户对于中智益健的通知应当通过中智益健对外正式公布的通信地址、传真号码、电子邮件地址等联系信息进行送达。\n" +
			"11. 法律适用\n" +
			"11.1\n" +
			"设备平台依据并贯彻中华人民共和国法律法规、政策规章及司法解释之要求，包括但不限于《全国人民代表大会常务委员会关于加强网络信息保护的决定》、《最高人民法院最高人民检察院适用法律若干问题的解释》等文件精神，制定《用户注册协议》。\n" +
			"11.2\n" +
			"本协议的订立、执行和解释及争议的解决均应适用中国法律并受中国法院管辖。\n" +
			"11.3\n" +
			"如双方就本协议内容或其执行发生任何争议，双方应尽量友好协商解决；协商不成时，任何一方均可向中智益健所在地的人民法院提起诉讼。\n" +
			"12. 其他规定\n" +
			"12.1\n" +
			"本协议构成双方对本协议之约定事项及其他有关事宜的完整协议，除本协议规定的之外，未赋予本协议各方其他权利。\n" +
			"12.2\n" +
			"如本协议中的任何条款无论因何种原因完全或部分无效或不具有执行力，本协议的其余条款仍应有效并且有约束力。\n" +
			"12.3\n" +
			"本协议中的标题仅为方便而设，在解释本协议时应被忽略。";
	String jiangli = "会员可将个人专属邀请码发送于其他人进行邀请，当受邀人填写邀请人提供的个人专属邀请码并成功注册后，邀请人可获得200积分，同时受邀人可获得30元优惠券。";



	/*
         * 命令
         */
	public class MusicPlayControl {
		// 播放命令
		public static final int MUSIC_CONTROL_PLAY = 0;
		// 暂停命令
		public static final int MUSIC_CONTROL_PAUSE = 1;
		// 上一首命令
		public static final int MUSIC_CONTROL_PREVIOUS = 2;
		// 下一首命令
		public static final int MUSIC_CONTROL_NEXT = 3;
		// 进度条点击命令
		public static final int MUSIC_CONTROL_SEEKBAR = 4;

		// 蓝牙控制
		public static final String SERVICECMD = "com.bluetooth.music.musicservicecommand";
		public static final String TOGGLEPAUSE_ACTION = "com.bluetooth.music.togglepause";
		public static final String PAUSE_ACTION = "com.bluetooth.music.pause";
		public static final String PREVIOUS_ACTION = "com.bluetooth.music.previous";
		public static final String NEXT_ACTION = "com.bluetooth.music.next";
	}

	String VOLUME_UP = "volumeUp";
	String VOLUME_DOWN = "volumeDown";

	/**
	 * 心跳反馈：当前工作状态
	 */
	int WORK_STATUS_DIANJI_OFF_GAOYA_OFF = 0;
	int WORK_STATUS_DIANJI_ON_GAOYA_OFF = 1;
	int WORK_STATUS_DIANJI_OFF_GAOYA_ON = 2;
	int WORK_STATUS_DIANJI_ON_GAOYA_ON = 3;
	int WORK_STATUS_UNXINTIAO = 10;

	/**
	 * 心跳service广播
	 */
	String SEND_MSG_TO_DEVICE = "SEND_MSG_TO_DEVICE";

	String XINTIAO_DISCONNECTED = "XINTIAO_DISCONNECTED";

	String WORK_TYPE_CHANGED = "WORK_TYPE_CHANGED";

	String BATTERY_CHANGED = "BATTERY_CHANGED";

	String BATTERY_CHANGED_BL = "BATTERY_CHANGED_BL";

}
