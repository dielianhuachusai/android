package com.example.ireader;

import java.net.ContentHandler;
import java.util.List;

import android.content.Context;
import android.database.sqlite.*;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TxtItem extends BaseAdapter {
	LayoutInflater layoutInflater;
	List<Book> list;
	boolean isDelete;//是否显示删除页面
	private Context context;
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	public TxtItem(Context context,List<Book> list){
		this.context = context;
		this.list=list;
		layoutInflater=LayoutInflater.from(context);
	}
	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return list.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int pos, View curretView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view;
		Book book=(Book)getItem(pos);
		ViewHolder viewHolder=new ViewHolder();
		if(curretView==null){
			view=layoutInflater.inflate(R.layout.txtitem, null);
			viewHolder.deleteImage=(ImageView)view.findViewById(R.id.delete_markView);
			viewHolder.Name=(TextView)view.findViewById(R.id.itemName);
			view.setTag(viewHolder);
		}
		else{
			view=curretView;
			viewHolder=(ViewHolder)view.getTag();
		}
		viewHolder.Name.setText(book.name);
		viewHolder.deleteImage.setVisibility(isDelete?view.VISIBLE:view.GONE);
		if(isDelete){
			//如果显示删除按钮
			viewHolder.deleteImage.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//点击删除图标删除Item
					try{
						SQLiteDatabase sql = SQLiteDatabase.openOrCreateDatabase("/data/data/com.example.ireader/databases/test.db", null);
						sql.delete("book", "path=?",  new String[]{list.get(pos).path});
					}
					catch(SQLiteException e){
						Log.e("SqliteERR", e.getMessage());
					}
					list.remove(pos);
					setIsShowDelete(false);
					
				}
			});
		}
		return view;
	}
	public class ViewHolder {
		ImageView deleteImage;
		TextView Name;

	}

	public void setIsShowDelete(boolean isShowDelete) {
        this.isDelete = isShowDelete;
        notifyDataSetChanged();
    }
}
