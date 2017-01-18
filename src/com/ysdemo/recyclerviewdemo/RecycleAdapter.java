package com.ysdemo.recyclerviewdemo;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author 尘笑ys
 * 
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder>/* implements OnFocusChangeListener */{
	// 类型用自定义的ViewHolder

	private List<String> mDataList;
	private LayoutInflater mInflater;
	RecyclerView mRecyclerView;

	// RecyclerView目前需要自己来定义接口
	public interface OnItemClickLitener {
		// 点击事件
		void onItemClick(View view, int position);

		// 长按事件
		void onItemLongClick(View view, int position);
	}

	private OnItemClickLitener mOnItemClickLitener;
	private String TAG = "RecycleAdapter";

	// 设置监听的方法，注意参数是CharAdapter.OnItemClickLitener而不是View.OnItemClickLitener
	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	// 构造时传入数据，如果考虑数据解耦，可将数据赋值放到setData()中
	public RecycleAdapter(Context context, List<String> datas, RecyclerView view) {
		mInflater = LayoutInflater.from(context);
		mDataList = datas;
		mRecyclerView = view;
	}

	@Override
	// 视图缓存创建
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// 视图缓存，在ViewHolder构造时获取视图控件
		MyViewHolder holder = new MyViewHolder(mInflater.inflate(R.layout.recycle_item, parent, false));
		return holder;
	}

	@Override
	// 视图缓存的数据绑定
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		holder.tv.setText(mDataList.get(position));
		holder.iv.setImageResource(R.drawable.ic_launcher);

		// 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null) {
			holder.itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int pos = holder.getPosition();
					// 尽量在回调时处理逻辑，此处不进行处理，增加Adapter复用的可能
					mOnItemClickLitener.onItemClick(holder.itemView, pos);
				}
			});

			holder.itemView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					int pos = holder.getPosition();
					// 尽量在回调时处理逻辑，此处不进行处理，增加Adapter复用的可能
					mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
					// removeData(pos);
					return false;
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return mDataList.size();
	}

	// 需要自己定义，添加数据后需要调用notifyItemInserted(position),效果类似于ListView的notifyDataSetChanged()
	// 但是这里会调用预设或者自定义的增加item动画
	public void addData(int position) {
		mDataList.add(position, "Insert One");
		notifyItemInserted(position);
	}

	// 需要自己定义，添加数据后需要调用notifyItemRemoved(position),效果类似于ListView的notifyDataSetChanged()
	// 但是这里会调用预设或者自定义的删除item动画
	public void removeData(int position) {
		mDataList.remove(position);
		notifyItemRemoved(position);
	}

	// 视图缓存
	class MyViewHolder extends ViewHolder {
		ImageView iv;
		TextView tv;

		// 在构造方法中获取控件
		public MyViewHolder(View view) {
			super(view);
//			view.setOnFocusChangeListener(RecycleAdapter.this);
			iv = (ImageView) view.findViewById(R.id.id_img);
			tv = (TextView) view.findViewById(R.id.id_txt);

		}
	}
/*
	public void onFocusChange(View v, boolean hasFocus) {
		if (!v.hasFocus()) {
			Log.e(TAG, "View v did not have focus");
			return;
		}

		final int index = mRecyclerView.getChildPosition(v); // adapter pos
		if (index == -1) {
			Log.e(TAG, "Recycler view did not have view");
			return;
		}
		int position = mRecyclerView.indexOfChild(v); // layout pos
		int lastPos = mRecyclerView.getChildCount(); // layout pos
		int span = ((GridLayoutManager) mRecyclerView.getLayoutManager()).getSpanCount();
		int threshold = 2 * span;
		Log.e(TAG, String.format("Position: %1$d. lastPos: %2$d. span: %3$d. threshold: %4$d", position, lastPos, span, threshold));
		if(position < span){
			return;
		}
		
		if (position >= (lastPos - span)) {
			// scroll down if possible
			int bottomIndex = mRecyclerView.getChildPosition(mRecyclerView.getChildAt(lastPos));
			if (bottomIndex < getItemCount()) {
				// scroll down
				int scrollBy = v.getHeight();
				mRecyclerView.scrollBy(0, scrollBy);
				Log.e(TAG, String.format("Scrolling down by %d", scrollBy));
			}

		} else if (position < span) {
			// scroll up if possible
			int topIndex = mRecyclerView.getChildPosition(mRecyclerView.getChildAt(0));
			if (topIndex > 0) {
				// scroll up
				int scrollBy = v.getHeight();
				mRecyclerView.scrollBy(0, -scrollBy);
				Log.e(TAG, String.format("Scrolling up by %d", -scrollBy));
			}
		}
	}*/
}
