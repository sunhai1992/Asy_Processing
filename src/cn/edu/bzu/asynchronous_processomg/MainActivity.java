package cn.edu.bzu.asynchronous_processomg;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 学习通过Thread+Handler实现非UI线程更新UI组件
 * 学习异步加载的使用
 * @author monster
 *  date:2015-05-28
 *  introduce : Android只允许UI线程修改Activity里的UI组件
 *  Aim：点击按钮读取CSDN网站的LOGO
 */
public class MainActivity extends Activity {
   private Button mButton;
   private ImageView mImageView; 
   
   private final static int MSG_SUCCESS = 0; //获取图片成功的标识
   private final static int MSG_FAILURE = 1; //获取图片失败的标识
   
   private Thread mThread;
   
   private Handler mHandler=new Handler(){
	   public void handleMessage(Message msg){  //此方法在UI线程中运行
		   switch(msg.what){
			   case MSG_SUCCESS:
				   mImageView.setImageBitmap((Bitmap)msg.obj); //得到obj，并且强制类型转换成Bitmap类型--->>>obj含有图片的信息
				   Toast.makeText(getApplication(), "success", Toast.LENGTH_LONG).show();
				   break;	
			   case MSG_FAILURE:
				   Toast.makeText(getApplication(), "error", Toast.LENGTH_LONG).show();
				   break;
		   }
	   }
	   
   };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实例化控件的时候，如果为当前上下文的时候，则不需要写上下文
        mButton=(Button)findViewById(R.id.mButton);
        mImageView=(ImageView)findViewById(R.id.mImageView);
        
        mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mThread==null){
					mThread=new Thread(runnable);
					mThread.start();
				}
			}
		});
    }
		    Runnable runnable=new Runnable() {
				
				@Override
				public void run() {
					 //run()在新的线程中运行 
					HttpClient hc=new DefaultHttpClient();
					HttpGet hg=new HttpGet("http://csdnimg.cn/www/images/csdnindex_logo.gif"); //csdn 的logo
					final Bitmap bm;
					try {
						HttpResponse hr=hc.execute(hg);
						bm=BitmapFactory.decodeStream(hr.getEntity().getContent());
					} catch (Exception e) {
						mHandler.obtainMessage(MSG_FAILURE).sendToTarget();//获取图片失败
						return ;
					}
					mHandler.obtainMessage(MSG_SUCCESS,bm).sendToTarget();//获取图片成功,穿入图片流
				}
			};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
