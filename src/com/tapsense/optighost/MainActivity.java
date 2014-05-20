package com.tapsense.optighost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.tapsense.optighost.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	private GridView mLettersGridView;
	private ProgressDialog mProgressDialog;
	private final MainActivity mContext = this;
	private String mGameWord;
	private StringBuilder charEntered;
	private TextView gameTextEntered, restartGame;
	private int charactersEntered = 0;
	private TrieDataStructure buildingDataStructure;
	private TrieNode rootNode, currNode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		charEntered = new StringBuilder();
		mLettersGridView = (GridView) findViewById(R.id.lettersGridview);
		gameTextEntered = (TextView) findViewById(R.id.textEntered);
		restartGame = (TextView) findViewById(R.id.textViewRestart);

		restartGame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("MAIN ACTIVITY", "restart the game");
				ResetTheGame(100);

			}
		});

		mLettersGridView.setAdapter(new LettersAdapter(this));

		mLettersGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if (position == 26) {
					// do nothing
				} else {
					Log.d("MAIN ACTIVITY", "CHARACTER SELECTED: "
							+ (char) (position + 97));
					charEntered.append((char) (position + 97));
					mGameWord = gameTextEntered.getText().toString()
							+ ((char) (position + 97));
					gameTextEntered.setText(mGameWord.toUpperCase());
					charactersEntered++;
					if (!CheckIfGameHasEnded(mGameWord.toLowerCase()))
						GetMeAllTheWords(mGameWord.toLowerCase());

				}

			}
		});

		mProgressDialog = new ProgressDialog(MainActivity.this);
		mProgressDialog.setTitle("Please wait");
		mProgressDialog.setMessage("Loading Dictionary....");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		new LoadTrieDataStructure().execute();

	}

	private class LoadTrieDataStructure extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			Log.d("MAIN ACTIVITY", "entered the asynctask");
			String[] dictionary = null;
			Log.d("MAIN ACTIVITY", "entered to read the file");

			AssetManager assetManager = mContext.getAssets();
			String[] txtFiles = null;
			String word;
			try {
				txtFiles = assetManager.list("TextFiles");
			} catch (IOException e) {
				e.printStackTrace();
			}

			buildingDataStructure = new TrieDataStructure();
			rootNode = buildingDataStructure.createTree();
			buildingDataStructure.clearWordsList();
			// StringBuilder dictionaryBuilder = new StringBuilder();

			try {
				BufferedReader wordsReader = new BufferedReader(
						new InputStreamReader(
								assetManager.open("WORD_LIST.txt")));
				while ((word = wordsReader.readLine()) != null) {
					buildingDataStructure.insertWord(rootNode, word);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}


			mProgressDialog.dismiss();
			return null;
		}

	}

	public void GetMeAllTheWords(String enteredWord) {
		Log.d("MAIN ACTIVITY", "entered word: " + enteredWord);
		char[] branch = new char[50];
		int level = 0;
		currNode = rootNode;
		for (int i = 0; i < enteredWord.length(); i++) {
			Log.d("MAIN ACTIVITY", "character: " + enteredWord.charAt(i));
			branch[i] = enteredWord.charAt(i);
			currNode = currNode.links[branch[i] - 'a'];
			level = i;
		}
		buildingDataStructure.printTree(currNode, level, branch, enteredWord);

		for (String str : buildingDataStructure.getAllWordsList())
			Log.d("MAIN ACTIVITY", str);
		PickAWordFromTheList(buildingDataStructure.getAllWordsList());

	}

	public void PickAWordFromTheList(ArrayList<String> wordsList) {
		charactersEntered++;
		if (wordsList.isEmpty() && (charactersEntered % 2 != 0))
			ResetTheGame(103);
		else {

			int randomPick = (int) (Math.random() * (wordsList.size()));
			String wordPicked = wordsList.get(randomPick);
			Log.d("MAIN ACTIVITY",
					"number of words in the list: " + wordsList.size());
			Log.d("MAIN ACTIVITY", "Random Number: " + randomPick);
			Log.d("MAIN ACTIVITY", "Random word: " + wordPicked);
			Log.d("MAIN ACTIVITY",
					"Word to be set: "
							+ wordsList.get(randomPick).substring(0,
									charactersEntered));
			Log.d("MAIN ACTIVITY", "Number of characters entered: "
					+ charactersEntered);

			Log.d("MAIN ACTIVITY", "Number of characters entered: "
					+ charactersEntered);
			buildingDataStructure.clearWordsList();
			if (!CheckIfGameHasEnded(wordPicked.substring(0, charactersEntered)))
				gameTextEntered.setText(wordPicked.substring(0,
						charactersEntered).toUpperCase());

		}

	}

	public boolean CheckIfGameHasEnded(String enteredWord) {
		Log.d("MAIN ACTIVITY", "CHECK WHO WON (entry point): " + enteredWord);
		char[] branch = new char[50];
		int level = 0;
		currNode = rootNode;
		for (int i = 0; i < enteredWord.length(); i++) {
			branch[i] = enteredWord.charAt(i);
			currNode = currNode.links[branch[i] - 'a'];
			level = i;
		}

		buildingDataStructure.printTree(currNode, level, branch, enteredWord);

		for (String str : buildingDataStructure.getAllWordsList())
			Log.d("MAIN ACTIVITY", "CHECK WHO WON: " + str);

		if (buildingDataStructure.getAllWordsList().isEmpty()) {
			if (charactersEntered % 2 == 0) {
				ResetTheGame(103);
				buildingDataStructure.clearWordsList();
				return true;
			} else {
				ResetTheGame(102);
				buildingDataStructure.clearWordsList();
				return true;
			}
		}

		buildingDataStructure.clearWordsList();
		return false;
	}

	public void ResetTheGame(int exitMode) {
		String dialogMsg = "";
		String titleMsg = "";
		switch (exitMode) {
		case 100:
			dialogMsg = "The game will be restarted.";
			titleMsg = "Restart";
			break;
		case 101:
			dialogMsg = mGameWord.toUpperCase()
					+ " is not a meaningful word. \nRestarting the game.";
			titleMsg = "Oops!";
			break;
		case 102:
			dialogMsg = "Computer wins";
			titleMsg = "Try again!";
			break;
		case 103:
			dialogMsg = "You Win";
			titleMsg = "Congratulations!";
		default:
			break;
		}

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Your Title");

		// set dialog message
		alertDialogBuilder.setMessage(dialogMsg).setTitle(titleMsg)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

		mGameWord = "";
		gameTextEntered.setText(mGameWord);
		charactersEntered = 0;
	}

}
