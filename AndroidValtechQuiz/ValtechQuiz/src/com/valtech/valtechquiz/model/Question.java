package com.valtech.valtechquiz.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

	public String title;
	public String image;
	public ArrayList<String> answers; // format : [0/1];[question]

	public Question() {
		title = "";
		answers = new ArrayList<String>();
	}

	public String getImagePath() {
		return "images/" + image;
	}

	public String getAnswerLabel(int position) {
		String answer = answers.get(position);
		String[] tmp = answer.split(";");
		if (tmp.length > 1)
			return tmp[1];
		return answer;
	}

	public boolean isAnswerCorrect(int position) {
		String answer = answers.get(position);
		String[] tmp = answer.split(";");
		if (tmp.length > 1)
			if (tmp[0].equals("1"))
				return true;
		return false;
	}

	public Question(Parcel in) {
		this();
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
		@Override
		public Question createFromParcel(Parcel source) {
			return new Question(source);
		}

		@Override
		public Question[] newArray(int size) {
			return new Question[size];
		}
	};

	public void readFromParcel(Parcel in) {
		title = in.readString();
		image = in.readString();
		in.readStringList(answers);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(image);
		dest.writeStringList(answers);
	}
}
