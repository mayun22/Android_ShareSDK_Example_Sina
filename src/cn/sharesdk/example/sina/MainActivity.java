package cn.sharesdk.example.sina;


import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;


/**ShareSDK 官网地址 ： http://www.sharesdk.cn </br>
/1、这是用2.11版本的sharesdk，一定注意  </br>
/2、如果要咨询客服，请加企业QQ 4006852216 </br>
/3、咨询客服时，请把问题描述清楚，最好附带错误信息截图 </br>
/4、一般问题，集成文档中都有，请先看看集成文档；减少客服压力，多谢合作  ^_^
*/
public class MainActivity extends Activity implements PlatformActionListener,Callback{

	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;

	private static final String FILE_NAME = "/share_pic.jpg";
	public static String TEST_IMAGE;
	
	/**ShareSDK集成方法有两种</br>
	 * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目在引用mainlibs库</br>
	 * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br>
	 * 请看“ShareSDK 使用说明文档”，SDK下载目录中 </br>
	 * 或者看网络集成文档 http://wiki.sharesdk.cn/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
	 * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
	 * 
	 * 
	 * 平台配置信息有三种方式：
	 * 1、在我们后台配置各个微博平台的key
	 * 2、在代码中配置各个微博平台的key，http://sharesdk.cn/androidDoc/cn/sharesdk/framework/ShareSDK.html
	 * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf	 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//初始化ShareSDK
		ShareSDK.initSDK(this);		
	
		/**新浪分享要注意的问题</br>
		 * 1、新浪开发测试，需要测试账号；审核通过后，任何账号都可以用；
		 *    否则会报错21321的错误，登陆失败
		 * 
		 *    我们的应用已经审核过了，可以用ShareSDK.conf的新浪平台的key做测试
		 *    注册测试账号：新浪开放平台-你的应用中-应用信息-测试账号
		 *    
		 * 2、ShareSDK.conf中新浪配置信息包括AppKey，AppSecret，RedirectUrl
		 *    前两个参数，你注册应用时，就可以得到；
		 *    
		 *    RedirectUrl是用于授权登陆用的，一定要与开放平台填写的一致，
		 *    任何网址都行，如www.baidu.com;如果不一致或者不填写，就会报错，授权不成功，分享失败
		 *    在新浪开放平台-你的应用-应用信息-高级信息-授权回调页，里面设置
		 *   
		 * 3、新浪分享网络图片，需要申请高级权限；本地图片就可以直接分享
		 *    权限申请：新浪开放平台-你的应用中-接口管理-权限申请-微博高级写入接口-statuses/upload_url_text
		 * 
		 * 4、一般问题，文档中都有写，请查看；也可以网络文档http://wiki.sharesdk.cn/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
		 *   跟客服联系的话，不要磨磨唧唧的，直接说问题，最好有错误信息截图，特别讨厌：在吗？在？ 这太浪费时间啦  ^_^
		 */
		
		Button button1 =(Button) findViewById(R.id.button1);
		button1.setText("新浪快捷分享，有编辑界面");
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//快捷分享，没有九宫格，只有编辑页
				OnekeyShare oks = new OnekeyShare();
				
				// 分享时Notification的图标和文字
				oks.setNotification(R.drawable.ic_launcher, "Gtpass");
				
				//设置默认微博平台，例如腾讯微博的，可以是TencentWeibo.NAME
				oks.setPlatform(SinaWeibo.NAME);
				
				//分享文本
				oks.setText("ShareSDK测试能否分享");
				
				//分享本地图片，只能是sdcard中的图片地址
				oks.setImagePath(TEST_IMAGE);
				
				//分享网络图片，新浪分享网络图片，需要申请高级权限,否则会报10014的错误
				//权限申请：新浪开放平台-你的应用中-接口管理-权限申请-微博高级写入接口-statuses/upload_url_text
				//注意：本地图片和网络图片，同时设置时，只分享本地图片
				//oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
				
				oks.setSilent(false);
				oks.show(MainActivity.this);				
			}
		});
		
		
		Button button2 =(Button) findViewById(R.id.button2);
		button2.setText("新浪直接图文分享网络图片，没有界面");
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//新浪直接分享
				SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
				//设置分享内容
				sp.text = "ShareSDK测试标题";
				//分享网络图片，新浪分享网络图片，需要申请高级权限,否则会报10014的错误
				//权限申请：新浪开放平台-你的应用中-接口管理-权限申请-微博高级写入接口-statuses/upload_url_text
				//注意：本地图片和网络图片，同时设置时，只分享本地图片
				sp.imageUrl = "http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg";
				
				//初始化新浪分享平台
				Platform pf = ShareSDK.getPlatform(MainActivity.this, SinaWeibo.NAME);
				//设置分享监听
				pf.setPlatformActionListener(MainActivity.this);
				//执行分享
				pf.share(sp);
				
			}
		});
		
		/**快捷分享，有九格宫</br>
		 * 删除九格宫不要的平台，只要删除对应平台的jar就行
		 */				
		Button button3 =(Button) findViewById(R.id.button3);
		button3.setText("快捷分享，有九格宫和编辑界面");
		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				

				OnekeyShare oks = new OnekeyShare();

				// 分享时Notification的图标和文字
				oks.setNotification(R.drawable.ic_launcher, "ShareSDK 集成例子");
				// address是接收人地址，仅在信息和邮件使用
				oks.setAddress("12345678901");
				// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
				oks.setTitle("ShareSDK 例子标题title");
				// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
				oks.setTitleUrl("http://sharesdk.cn");
				// text是分享文本，所有平台都需要这个字段
				oks.setText("ShareSDK 例子文本text");
				// imagePath是图片的本地路径，sdcard卡中图片地址
				oks.setImagePath(MainActivity.TEST_IMAGE);
				// imageUrl是图片的网络路径，新浪微博、人人网、微信
				oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
				// url仅在微信（包括好友和朋友圈）中使用
				oks.setUrl("http://sharesdk.cn");
				// comment是我对这条分享的评论，仅在人人网和QQ空间使用
				oks.setComment("人人分享需要的评论");
				// site是分享此内容的网站名称，仅在QQ空间使用
				oks.setSite("qq空间分享的网站名称");
				// siteUrl是分享此内容的网站地址，仅在QQ空间使用
				oks.setSiteUrl("http://sharesdk.cn");
				// latitude是维度数据，仅在新浪微博、腾讯微博和Foursquare使用
				oks.setLatitude(23.122619f);
				// longitude是经度数据，仅在新浪微博、腾讯微博和Foursquare使用
				oks.setLongitude(113.372338f);
				// 是否直接分享（true则直接分享），false是有九格宫，true没有
				oks.setSilent(false);

				oks.show(MainActivity.this);
			}
		});
		

		//初始化本地图片，把图片从drawable复制到sdcard中
		new Thread() {
			public void run() {
				initImagePath();
			}
		}.start();
	}

	//把图片从drawable复制到sdcard中
	private void initImagePath() {
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				TEST_IMAGE = Environment.getExternalStorageDirectory().getAbsolutePath() + FILE_NAME;
			}
			else {
				TEST_IMAGE = getApplication().getFilesDir().getAbsolutePath() + FILE_NAME;
			}
			File file = new File(TEST_IMAGE);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.sina_pic);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch(Throwable t) {
			t.printStackTrace();
			TEST_IMAGE = null;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}

	//设置监听http://sharesdk.cn/androidDoc/cn/sharesdk/framework/PlatformActionListener.html
    //监听是子线程，不能Toast，要用handler处理，不要犯这么二的错误
	@Override
	public void onCancel(Platform platform, int action) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> arg2) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		t.printStackTrace();

		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);		
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch(msg.what) {
		case MSG_TOAST: {
			String text = String.valueOf(msg.obj);
			Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
		}
		break;
		case MSG_ACTION_CCALLBACK: {
			switch (msg.arg1) {
				case 1: { // 成功
					showNotification(2000, getString(R.string.share_completed));
				}
				break;
				case 2: { // 失败
					String expName = msg.obj.getClass().getSimpleName();
					if ("WechatClientNotExistException".equals(expName)
							|| "WechatTimelineNotSupportedException".equals(expName)) {
						showNotification(2000, getString(R.string.wechat_client_inavailable));
					}
					else if ("GooglePlusClientNotExistException".equals(expName)) {
						showNotification(2000, getString(R.string.google_plus_client_inavailable));
					}
					else if ("QQClientNotExistException".equals(expName)) {
						showNotification(2000, getString(R.string.qq_client_inavailable));
					}
					else {
						showNotification(2000, getString(R.string.share_failed));
					}
				}
				break;
				case 3: { // 取消
					showNotification(2000, getString(R.string.share_canceled));
				}
				break;
			}
		}
		break;
		case MSG_CANCEL_NOTIFY: {
			NotificationManager nm = (NotificationManager) msg.obj;
			if (nm != null) {
				nm.cancel(msg.arg1);
			}
		}
		break;
	}
	return false;
	}

	// 在状态栏提示分享操作
	private void showNotification(long cancelTime, String text) {
		try {
			Context app = getApplicationContext();
			NotificationManager nm = (NotificationManager) app
					.getSystemService(Context.NOTIFICATION_SERVICE);
			final int id = Integer.MAX_VALUE / 13 + 1;
			nm.cancel(id);

			long when = System.currentTimeMillis();
			Notification notification = new Notification(R.drawable.ic_launcher, text, when);
			PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(), 0);
			notification.setLatestEventInfo(app, "sharesdk test", text, pi);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			nm.notify(id, notification);

			if (cancelTime > 0) {
				Message msg = new Message();
				msg.what = MSG_CANCEL_NOTIFY;
				msg.obj = nm;
				msg.arg1 = id;
				UIHandler.sendMessageDelayed(msg, cancelTime, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
