package com.valtech.valtechquiz.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.valtech.valtechquiz.model.Score;

public class ScoresDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_SCORE, MySQLiteHelper.COLUMN_TIME, MySQLiteHelper.COLUMN_EMAIL};

	public ScoresDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Score createScore(Score score) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_SCORE, score.getScore());
		values.put(MySQLiteHelper.COLUMN_TIME, score.getTime());
		values.put(MySQLiteHelper.COLUMN_EMAIL, score.getEmail());
		long insertId = database.insert(MySQLiteHelper.TABLE_SCORES, null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_SCORES, allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Score newScore = cursorToScore(cursor);
		cursor.close();
		return newScore;
	}

	public void deleteScore(Score score) {
		long id = score.getId();
		System.out.println("Score deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_SCORES, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	public List<Score> getAllScores() {
		List<Score> scores = new ArrayList<Score>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_SCORES, allColumns, null, null, null, null, MySQLiteHelper.COLUMN_SCORE+" DESC, "+MySQLiteHelper.COLUMN_TIME);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Score score = cursorToScore(cursor);
			scores.add(score);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return scores;
	}

	private Score cursorToScore(Cursor cursor) {
		Score score = new Score();
		score.setId(cursor.getLong(0));
		score.setScore(cursor.getInt(1));
		score.setTime(cursor.getInt(2));
		score.setEmail(cursor.getString(3));
		return score;
	}
}
