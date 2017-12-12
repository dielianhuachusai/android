package com.example.ireader;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


public class MainActivity extends Activity {
	final int FILE_SELECT=9001;
	final private int CURRENT_READ=1001;
	private SQLiteDatabase sqliteDatabase;
	
	GridView gridView;
	TxtItem txtItem;
	boolean isShowDelete;
	List<Book> list=new ArrayList<Book>();
    @SuppressLint("SdCardPath") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
        gridView=(GridView)findViewById(R.id.bookShelf);
        sqliteDatabase=openOrCreateDatabase("/data/data/com.example.ireader/databases/test.db",MODE_PRIVATE, null);
        /*
        try{
        	sqliteDatabase=openOrCreateDatabase("/data/data/com.example.ireader/databases/test.db",MODE_PRIVATE, null);
        }
        catch(Exception e){
        	Toast.makeText(getApplicationContext(), e.getMessage(), 0).show();
        }
        */
        initData();
        
        txtItem=new TxtItem(getApplicationContext(),list);
        gridView.setAdapter(txtItem);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				//点击进入阅读界面
				Intent intent=new Intent(MainActivity.this,Reader.class);
				intent.putExtra("path", list.get(pos).path);
				intent.putExtra("pageNum", list.get(pos).pageNum);
				Log.d("StartRead", "list pageNum:"+Integer.toString(list.get(pos).pageNum));
				startActivityForResult(intent, CURRENT_READ);
				
			}
		});
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				//长按来显示或者隐藏删除图标
				if(isShowDelete){
					isShowDelete=false;
					txtItem.setIsShowDelete(isShowDelete);
				}else{
					isShowDelete=true;
					txtItem.setIsShowDelete(isShowDelete);
				}
				// TODO Auto-generated method stub
				return true;
			}
		});
//        txtItem=new TxtItem(this, list);
    }
    public void onPlusButtonClick(View view){
    	//添加txt文件
    	
    	Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
    	intent.setType("text/plain");
    	intent.addCategory(Intent.CATEGORY_OPENABLE); 
    	try{
    		startActivityForResult(intent,FILE_SELECT);
    	}
    	catch(Exception e){
    		Toast.makeText(getApplicationContext(), e.getMessage(), 0).show();
    	}
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	switch(requestCode){
    	case FILE_SELECT:
    		if(resultCode == RESULT_OK){
    			Uri uri=data.getData();
//    			Log.d("FILE", "File uri:"+uri);
    			String path=uri.getPath().toString();
    			try{
    				//如果路径存在则插入数据
    				if(path!=null){
    					File file=new File(path);
    					if(!file.exists()){
    						Toast.makeText(getApplicationContext(), "文件不存在",0).show();
    						return;
    					}
    					String name=file.getName();
    					Log.d("File","File path:"+path);
    					Log.d("File", "File name:"+name);
    					sqliteDatabase.execSQL("insert into book(id,name,path,pageNum)values(?,?,?,?)",new Object[]{list.size()+1,name,path,0});
    					Log.d("Sql", "执行sql结束");
//    					ContentValues values=new ContentValues();
//    					values.put("id", txtItem.getCount()+1);
//    					values.put("name", name);
//    					values.put("path", path);
//    					values.put("pageNum", 0);
//    					long rowid=sqliteDatabase.insert("book",null, values(?,?));
    					
    					Book book=new Book(name,path,0);
    					
//    					book.name=name;
//    					book.pageNum=0;
//    					book.path=path;
    					
//    					List<Book> newList=new ArrayList<Book>();
//    					newList.addAll(list);
//    					newList.add(book);
//    					list.clear();
//    					list.addAll(newList);
//    					txtItem=new TxtItem(getApplicationContext(), list);
    					list.add(book);
    					txtItem.notifyDataSetChanged();
//    					txtItem.notifyDataSetInvalidated();
    					Toast.makeText(getApplication(), Integer.toString(txtItem.getCount()), 10).show();
    				}
    				else
    					Log.e("File","路径不存在");
    			}
    			catch(Exception e){
    				Log.e("Sql",e.getMessage());
    				Toast.makeText(this, "失败："+e.getMessage(), 0).show();
    			}
    		}
    		break;
    	case CURRENT_READ:
    		
    		if(resultCode == 88){
    			try{
    	    		
    				String path=data.getStringExtra("path");
    				int pageNum=data.getIntExtra("pageNum", 0);
    				Log.d("RETURN", Integer.toString(pageNum));
    				//Toast.makeText(getBaseContext(), path, 0).show();
    				int index=0;
    				for(int i=0;i<list.size();i++){
    					if(list.get(i).path.equals(path)){
    						Book book=list.get(i);
    						book.pageNum=pageNum;
    						list.set(i, book);
    						index=i;
    					}
    				}
    				Log.d("SAVEPAGE","pageNum:"+Integer.toString(pageNum)+"List pageNum:"+Integer.toString( list.get(index).pageNum));
    				txtItem.notifyDataSetChanged();
    				//修改数据库
    				sqliteDatabase.execSQL("update book set pageNum=? where path=? ",new Object[]{pageNum,path});
    				}
    				catch(Exception e){
    					Log.e("SAVE",e.getMessage());
    				}
    		}
    		break;
    	}
    }

    private void initData(){
    	//初始化数据
    	Log.d("INIT","初始化数据");
    	if(sqliteDatabase==null)
    		return;
    	try{
    		sqliteDatabase.execSQL("create table if not exists book( id int auto_increment primary key not null,name varchar(20),path varchar(50),pageNum int )");
    		Cursor cursor= sqliteDatabase.rawQuery("select * from book",null);
    		if(cursor!=null){
    			while(cursor.moveToNext()){
    				
					String name=cursor.getString(cursor.getColumnIndex("name"));
					String path=cursor.getString(cursor.getColumnIndex("path"));
					int pageNum=cursor.getInt(cursor.getColumnIndex("pageNum"));
					Book itemBook=new Book(name,path,pageNum);
					list.add(itemBook);
    			}
    			
    			Log.d("LIST",Integer.toString(list.size()));
    		}
    		
    		//Toast.makeText(getBaseContext(), list.size(), 0).show();
    	}
    	catch(SQLiteException e){
    		Toast.makeText(this, e.getMessage(), 0).show();
    	}
    	catch(Exception e){
    		Log.e("INIT", e.getMessage());
    		Toast.makeText(this, e.getMessage(), 0).show();
    	}
    	
    }
}
