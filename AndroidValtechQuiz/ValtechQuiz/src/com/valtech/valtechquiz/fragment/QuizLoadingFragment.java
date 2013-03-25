//package com.valtech.valtechquiz;
//
//import java.util.ArrayList;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//public class QuizLoadingFragment extends BaseFragment {
//	private static final String ARG_ARTICLE = "article";
//
//	private QuizService quizService;
//	// Data.
//	private ArrayList<Question> questionsToDownload;
//
//	public static final QuizLoadingFragment newInstance(ArrayList<Question> questions) {
//		QuizLoadingFragment answerFragment = new QuizLoadingFragment();
//		Bundle arguments = new Bundle();
//		arguments.putParcelableArrayList(ARG_ARTICLE, questions);
//		answerFragment.setArguments(arguments);
//		return answerFragment;
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
//		// Services.
//		quizService = new QuizService();
//
//		// UI.
//		View view = inflater.inflate(R.layout.quiz_loading, container, false);
//		onRestoreInstanceState(bundle);
//		return view;
//	}
//
//	public void onRestoreInstanceState(Bundle bundle) {
//		if (bundle == null) {
//			questionsToDownload = getArguments().getParcelableArrayList(ARG_ARTICLE);
//		} else {
//			questionsToDownload = bundle.getParcelableArrayList(ARG_ARTICLE);
//		}
//		onDownloadQuiz();
//	}
//
//	@Override
//	public void onSaveInstanceState(Bundle bundle) {
//		// Note: It may look inefficient and risky (if the model evolves, e.g.
//		// with links between
//		// articles, the whole model in
//		// memory could be serialized) to save the whole article here. However,
//		// since saving Quiz in
//		// database is not implemented
//		// yet, we just can't store only the article id to get it later from
//		// database when fragment
//		// is restored...
//		bundle.putParcelableArrayList(ARG_ARTICLE, questionsToDownload);
//	}
//
//	private void onDownloadQuiz() {
//		//EventQuizLoaded.success(quizService.download());
//		getEventBus().dispatch(EventQuizLoaded.success(quizService.download()));
//
//	}
//}
