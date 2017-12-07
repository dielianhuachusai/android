package com.example.ireader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.w3c.dom.Text;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BookFragmentPagerAdapter extends FragmentPagerAdapter {
	File file;
	int pageNum;//页数
	String body;//内容
	StringBuffer stringbuffer;
	private Context context;
	public BookFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		stringbuffer=new StringBuffer();
	}
	public boolean setArgs(String path,int num,Context context){
		this.context=context;
		this.file=new File(path);
		pageNum=num;
		try{
			//开始读取文件
			FileInputStream in = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(in,"GB2312"));
			
			String line=null;
//			StringBuffer bufferRead=new StringBuffer();
			while((line=br.readLine())!=null){
//				System.out.println(line);
				stringbuffer.append(line);
			}
//			stringbuffer.append(bufferRead);
			br.close();
			Log.d("FileReader","读取文件成功");
			return true;
		}
		catch(IOException e){
			Toast.makeText(context, e.getMessage(), 0).show();
		}
		catch(Exception e){
			Log.e("LoadFile", e.getMessage());
		}
		return false;
	}
	@Override
	public Fragment getItem(int pos) {
		// TODO Auto-generated method stub
		String indexBuffer= stringbuffer.substring(pos*310, (pos+1)*310);
		Log.d("PageShow","这是第"+pos+"页");
		//设置当前页面的页码
//		View view=LayoutInflater.from(context).inflate(R.layout.reader_title, null);
//		((TextView)view.findViewById(R.id.pageNUm)).setText("第"+(pos+1)+"页面");
		return BookFragment.newInstance(pos, indexBuffer);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return stringbuffer.length()/310;
	}

}
