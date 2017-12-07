package com.example.ireader;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BookFragment extends Fragment {
	int PageNum;//页码
	String currentText;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		PageNum=getArguments()!=null?getArguments().getInt("pageNum"):1;
		currentText=getActivity()!=null?getArguments().getString("Text"):"空页面";
	}
	public static BookFragment newInstance(int num,String sb){
		BookFragment bookFragment=new BookFragment();
		Bundle bundle=new Bundle();
		bundle.putInt("pageNum", num);
		bundle.putString("Text", sb);
		bookFragment.setArguments(bundle);
		Log.d("BookFragment", "创建页面");
		return bookFragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		View view = inflater.inflate(R.layout.readpage, null);
		TextView tv=(TextView)view.findViewById(R.id.article);
		tv.setText(currentText);
//		Toast.makeText(getActivity(),Integer.toString( tv.getLineCount()), 0).show();
		return view;
	}
}
