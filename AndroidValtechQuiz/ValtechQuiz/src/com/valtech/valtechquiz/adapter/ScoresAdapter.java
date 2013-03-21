package com.valtech.valtechquiz.adapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.valtech.valtechquiz.R;
import com.valtech.valtechquiz.model.Score;

public class ScoresAdapter extends BaseAdapter {

	protected LayoutInflater mLayoutInflater;
	protected List<Score> scores;

	public ScoresAdapter(Context context, List<Score> scores) {
		mLayoutInflater = LayoutInflater.from(context);
		this.scores = scores;
	}

	@Override
	public int getCount() {
		return scores.size();
	}

	@Override
	public Object getItem(int position) {
		return scores.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {

		View item = null;
		// holder used to avoid unnecessary calls to findViewById
		ViewHolder holder;

		if (convertView == null) {
			item = mLayoutInflater.inflate(R.layout.score_item, null);
			holder = new ViewHolder();
			holder.themeTextView = (TextView) item.findViewById(R.id.score_theme);
			holder.scoreTextView = (TextView) item.findViewById(R.id.score_score);
			holder.timeTextView = (TextView) item.findViewById(R.id.score_time);
			holder.emailTextView = (TextView) item.findViewById(R.id.score_email);
			item.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			item = convertView;
		}

		if (position == 0) {
			item.setBackgroundColor(Color.parseColor("#FFE8bb1E"));
		} else if (position == 1) {
			item.setBackgroundColor(Color.parseColor("#FF999999"));
		} else if (position == 2) {
			item.setBackgroundColor(Color.parseColor("#FF7A3C13"));
		}else{
			item.setBackgroundColor(Color.TRANSPARENT);
		}

		String score = String.valueOf(scores.get(position).getScore());
		String email = scores.get(position).getEmail();
		String theme = scores.get(position).getTheme();
		
		holder.themeTextView.setText(theme);
		holder.scoreTextView.setText(score);
		holder.timeTextView.setText(convertTime(scores.get(position).getTime()));
		holder.emailTextView.setText(email);
		return item;
	}
	
	@SuppressLint("NewApi")
	private String convertTime(int time) {
		return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(time), TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
	}

	class ViewHolder {
		TextView themeTextView;
		TextView scoreTextView;
		TextView timeTextView;
		TextView emailTextView;
	}
}