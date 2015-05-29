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
 * ѧϰͨ��Thread+Handlerʵ�ַ�UI�̸߳���UI���
 * ѧϰ�첽���ص�ʹ��
 * @author monster
 *  date:2015-05-28
 *  introduce : Androidֻ����UI�߳��޸�Activity���UI���
 *  Aim�������ť��ȡCSDN��վ��LOGO
 */
public class MainActivity extends Activity {
   private Button mButton;
   private ImageView mImageView; 
   
   private final static int MSG_SUCCESS = 0; //��ȡͼƬ�ɹ��ı�ʶ
   private final static int MSG_FAILURE = 1; //��ȡͼƬʧ�ܵı�ʶ
   
   private Thread mThread;
   
   private Handler mHandler=new Handler(){
	   public void handleMessage(Message msg){  //�˷�����UI�߳�������
		   switch(msg.what){
			   case MSG_SUCCESS:
				   mImageView.setImageBitmap((Bitmap)msg.obj); //�õ�obj������ǿ������ת����Bitmap����--->>>obj����ͼƬ����Ϣ
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
        //ʵ�����ؼ���ʱ�����Ϊ��ǰ�����ĵ�ʱ������Ҫд������
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
					 //run()���µ��߳������� 
					HttpClient hc=new DefaultHttpClient();
					HttpGet hg=new HttpGet("http://csdnimg.cn/www/images/csdnindex_logo.gif"); //csdn ��logo
					final Bitmap bm;
					try {
						HttpResponse hr=hc.execute(hg);
						bm=BitmapFactory.decodeStream(hr.getEntity().getContent());
					} catch (Exception e) {
						mHandler.obtainMessage(MSG_FAILURE).sendToTarget();//��ȡͼƬʧ��
						return ;
					}
					mHandler.obtainMessage(MSG_SUCCESS,bm).sendToTarget();//��ȡͼƬ�ɹ�,����ͼƬ��
				}
			};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
