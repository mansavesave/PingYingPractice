package com.wells.pingyingpractice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;

import com.wells.pingyingpractice.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements
		TextView.OnEditorActionListener, TextWatcher {
	private int rawFileID = R.raw.pingyingdata;
	private TextView mQuestionView;
	private TextView mCorrectView;
	private EditText mAnswerEditText;
	private HashMap<Object, String> mAllData = new HashMap<Object, String>();
	private Object[] mKeys;
	private String mCurrentQuestion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));

		setContentView(R.layout.activity_main);
		parserData();
		init();
	}

	public void init() {
		mQuestionView = (TextView) this.findViewById(R.id.question);
		mCorrectView = (TextView) this.findViewById(R.id.correct_view);
		mAnswerEditText = (EditText) this.findViewById(R.id.answer);
		mAnswerEditText.setOnEditorActionListener(this);
		mAnswerEditText.addTextChangedListener(this);

		showNextRandomQuestionValue();
	}

	public void parserData() {
		readRawTextFile(this, rawFileID);
		mKeys = mAllData.keySet().toArray();
	}

	public String readRawTextFile(Context ctx, int resId) {
		StringBuilder text = new StringBuilder();
		try {
			InputStream inputStream = ctx.getResources().openRawResource(resId);
			InputStreamReader inputreader = new InputStreamReader(inputStream,
					"UTF-8");
			BufferedReader buffreader = new BufferedReader(inputreader);
			String line;

			while ((line = buffreader.readLine()) != null) {
				String[] splitStr = line.split("\\s+");
				// String[] splitStr = line.split(" ");
				for (int i = 0; i < splitStr.length; i = i + 2) {
					String key = splitStr[i];
					String value = splitStr[i + 1];
					mAllData.put(key, value);
				}

				text.append(line);
				text.append('\n');
			}

			if (inputStream != null) {
				inputStream.close();
			}

		} catch (IOException e) {
			return null;
		}

		return text.toString();
	}

	public void showNextRandomQuestionValue() {
		CharSequence correctedMessage = null;
		if (mCurrentQuestion != null && !mCurrentQuestion.equals("")) {
			String correctAnser = mAllData.get(mCurrentQuestion);
			String userAnser = mAnswerEditText.getText().toString().trim();
			if (correctAnser.equals(userAnser)) {
				correctedMessage = Html
						.fromHtml("<font color=\"#041cec\"><b><BIG>正確</BIG></b></font> "
								+ mCurrentQuestion
								+ " 為 <b><BIG>"
								+ correctAnser + "</BIG></b>");
			} else {
				correctedMessage = Html
						.fromHtml("<font color=\"#de1217\"><b><BIG>錯誤!</BIG></b></font> "
								+ mCurrentQuestion
								+ " 為 <b><BIG>"
								+ correctAnser + "</BIG></b> 而不是" + userAnser);
			}
		}
		if (correctedMessage != null) {
			mCorrectView.setText(correctedMessage);
		}

		int totalsize = mKeys.length;
		Random rand = new Random();
		int n = rand.nextInt(totalsize);
		mCurrentQuestion = mKeys[n].toString();

		mQuestionView.setText(mCurrentQuestion);
		mAnswerEditText.setText("");
	}

	//
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// showNextRandomQuestionValue();
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	@Override
	public boolean onEditorAction(TextView v, int keyCode, KeyEvent event) {
		if (keyCode == EditorInfo.IME_ACTION_DONE) {
			showNextRandomQuestionValue();
			return true;
		}
		return false;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		CharSequence current = s.subSequence(start, start + count);
		if (current.toString().equals(" ")) {
			showNextRandomQuestionValue();
		}

	}
}
