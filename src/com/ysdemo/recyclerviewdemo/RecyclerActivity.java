package com.ysdemo.recyclerviewdemo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Toast;

import com.ysdemo.recyclerviewdemo.RecycleAdapter.OnItemClickLitener;

/**
 * @author 尘笑ys
 *	RecyclerView 横竖屏切换demo
 */
public class RecyclerActivity extends Activity{
	private final static String TAG = "RecyclerActivity";
	private RecyclerView mRecyclerView;
	private ArrayList<String> mDataList = new ArrayList<String>();
	private LinearLayoutManager mLlayoutmanager;
	private GridLayoutManager mGlayoutmanager;
	RecycleAdapter mAdapter;
	private int mLastKeyCode;
	protected int mLastFocusIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
		//切换屏幕时保存数据，如有数据，则不再进行数据的初始化
		if (savedInstanceState != null && savedInstanceState.getStringArrayList("data") != null) {  
			mDataList = savedInstanceState.getStringArrayList("data");
	    }else {
			initData();
		}
		//竖屏线性展示
		mLlayoutmanager = new LinearLayoutManager(this);
		//横屏每行显示3个
		mGlayoutmanager = new GridLayoutManager(this,5);
		reviewonScreenChanged(getResources().getConfiguration());
		
		mRecyclerView.setAdapter(mAdapter = new RecycleAdapter(this, mDataList, mRecyclerView));
//		setOnScrollEvent();
//		setOnClickEvent();
		
	}

	private void setOnClickEvent() {
		mAdapter.setOnItemClickLitener(new OnItemClickLitener() {
			
			@Override
			public void onItemLongClick(View view, int position) {
				//这里要先使用这个数据再删除，因为一旦删除，就获取不到这个数据了，获取的就是下一个数据了
				Toast.makeText(RecyclerActivity.this, mDataList.get(position)+'\t'+"delete...", Toast.LENGTH_SHORT).show();
				mAdapter.removeData(position);
			}
			
			@Override
			public void onItemClick(View view, int position) {
				Toast.makeText(RecyclerActivity.this, mDataList.get(position), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void setOnScrollEvent() {
		mRecyclerView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				int index = recyclerView.getChildPosition(recyclerView.getFocusedChild() );
				Log.e(TAG, TAG + "-->onScrolled ----- (" + newState + ")"  + ",index = " + index);
				switch(newState){
				case RecyclerView.SCROLL_STATE_IDLE:
					int span = mGlayoutmanager.getSpanCount();
					int count = recyclerView.getChildCount();
					if(mLastKeyCode == KeyEvent.KEYCODE_DPAD_DOWN){
						if(mLastFocusIndex % span != index % span){
							recyclerView.getChildAt(count - (Math.abs(index % span - mLastFocusIndex % span)) - 1).requestFocus();
						}
					} else 	if(mLastKeyCode == KeyEvent.KEYCODE_DPAD_UP){
						if(mLastFocusIndex % span != index % span){
							recyclerView.getChildAt(Math.abs(index % span - mLastFocusIndex % span)).requestFocus();
						}
					}
					break;
				case RecyclerView.SCROLL_STATE_SETTLING:
					mLastFocusIndex = index;
					break;
				}
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				Log.e(TAG, TAG + "-->onScrolled ----- (" + dx + "," + dy + ")" );
				super.onScrolled(recyclerView, dx, dy);
			}
			
		});
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getAction() != KeyEvent.ACTION_DOWN){
			return super.dispatchKeyEvent(event);
		}
		mLastKeyCode = event.getKeyCode();
		Log.e(TAG, TAG + "-->dispatchKeyEvent ----- mLastKeyCode = " + mLastKeyCode);
		return super.dispatchKeyEvent(event);
	}
	
	private void reviewonScreenChanged(Configuration newConfig) {
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			//横屏
			mRecyclerView.setLayoutManager(mGlayoutmanager);
		}else {
			//竖屏
			mRecyclerView.setLayoutManager(mLlayoutmanager);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		reviewonScreenChanged(newConfig);
	}

	private void initData() {
		for(int j = 0; j < 1000; j++){
//			for (int i = 'A'; i <= 'z'; i++) {
				mDataList.add(j + "");
//			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putStringArrayList("data", mDataList);
	}

}
