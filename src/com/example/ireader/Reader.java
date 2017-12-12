package com.example.ireader;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class Reader extends FragmentActivity {
	final private int CURRENT_READ=1001;
	
	private ViewPager pageView;
	private BookFragmentPagerAdapter bAdapter;
	String filepath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_reader);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.reader_title);
		pageView=(ViewPager)findViewById(R.id.ReaderPager);
		FragmentManager fm=getSupportFragmentManager();
		bAdapter=new BookFragmentPagerAdapter(fm);
		Intent intent=getIntent();
		String path=intent.getStringExtra("path");
		filepath=path;
		((TextView)findViewById(R.id.readerTitle)).setText((new File(path)).getName());
		final TextView currentPageNumTV=(TextView)findViewById(R.id.pageNum);
		int pageNum=intent.getIntExtra("pageNum",0);
		Log.d("Item","Path:"+path);
		Log.d("LoadItemPageNum", Integer.toString(pageNum));
		
		if(bAdapter.setArgs(path, pageNum, getApplication())){
			pageView.setAdapter(bAdapter);
			pageView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int pos) {
					// TODO Auto-generated method stub
					currentPageNumTV.setText("第"+(pos+1)+"页");
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			pageView.setCurrentItem(pageNum);
			Log.d("SetReader", "设置ViewPager成功");
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent intent=getIntent();
		String path=intent.getStringExtra("path");
		filepath=path;
		int pageNum=intent.getIntExtra("pageNum",0);
		Log.d("Resume Item","Path:"+path);
		Log.d("Resume LoadItemPageNum", Integer.toString(pageNum));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reader, menu);
		return true;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent intent=new Intent(this,MainActivity.class);
			intent.putExtra("path", filepath);
			Log.d("RETURN_FILE_PATH",filepath);
			intent.putExtra("pageNum", pageView.getCurrentItem());
			setResult(88,intent);//1001代表
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//保存信息
//		Toast.makeText(getApplicationContext(), "当前页面："+pageView.getCurrentItem(), 0).show();
		
		super.onDestroy();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
