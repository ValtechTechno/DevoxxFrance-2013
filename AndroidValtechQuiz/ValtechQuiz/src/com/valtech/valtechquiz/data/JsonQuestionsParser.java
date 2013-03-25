package com.valtech.valtechquiz.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.valtech.androidtoolkit.utils.JSONUtil;
import com.valtech.valtechquiz.model.Question;

public class JsonQuestionsParser
{

    public List<Question> parseFeed(InputStream inputStream) {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonText = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                jsonText.append(line);
            }
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
        return parseQuestions(jsonText.toString());
    }

    public ArrayList<Question> parseQuestions(String text) {
    	ArrayList<Question> questions = new ArrayList<Question>();
        JSONObject JSONRoot = null;
        try {
            JSONRoot = new JSONObject(text);
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "JSON not valid");
        }

        if (JSONRoot != null) {
            JSONArray feed = JSONUtil.getJSONArray(JSONRoot, "questions");

            for (int i = 0; i < feed.length(); i++) {
                JSONObject object;
                try {
                    object = feed.getJSONObject(i);
                    Question question = parseQuestion(object);
                    	questions.add(question);
                } catch (JSONException e) {
                    Log.e(getClass().getSimpleName(), "Article not recovered");
                }

            }
            if (questions.size() == 0) {
            	questions.add(parseQuestion(JSONRoot));
            }
        }
        return questions;
    }

    private Question parseQuestion(JSONObject object) {
    	Question question = new Question();
    	question.title = object.optString("title", "");
    	question.image = object.optString("image", "");
    	question.answers = (ArrayList<String>) parseAnswers(object);
        return question;
    }
    
    private List<String> parseAnswers(JSONObject object) {
        List<String> answers = new ArrayList<String>();
        JSONArray answersArray = JSONUtil.getJSONArray(object, "answers");
        for (int i = 0; i < answersArray.length(); i++) {
            JSONObject answerObject;
            try {
            	answerObject = answersArray.getJSONObject(i);
                String answer = answerObject.optString("answer", "");
                answers.add(answer);
            } catch (JSONException e) {
                Log.e(getClass().getSimpleName(), "Link not recovered");
            }

        }
        return answers;
    }
}
