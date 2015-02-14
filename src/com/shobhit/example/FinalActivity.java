/**
 * Class Name: FinalActivity.java
 * Date : 27th November 2014
 * @author 
 * This activity shows the Final scores whether the
 *         user lost or won. Shows the highscore.
 * 
 */


package com.shobhit.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class FinalActivity extends Activity implements OnClickListener {

	private Button replay;
	private Button home;
	//	private Button exit;

	String winString = "Congrats!!";
	String loose = "Game Over!!!";
	public String name;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final);

		replay = (Button) findViewById(R.id.replay);
		replay.setOnClickListener(this);

		home = (Button) findViewById(R.id.mainmenu);
		home.setOnClickListener(this);

		//	exit = (Button) findViewById(R.id.buttonexit);
		//exit.setOnClickListener(this);

		TextView txt1 = (TextView) findViewById(R.id.textView1);
		Typeface font = Typeface.createFromAsset(getAssets(),"journal.ttf");
		txt1.setTypeface(font, Typeface.BOLD);

		TextView scoreTextView = (TextView) findViewById(R.id.scoreTextView);
		scoreTextView.setTypeface(font, Typeface.BOLD);
		final String time = (String) getIntent().getExtras().get("time");		//time is the time that is the final time of the barrel running

		TextView highString=(TextView) findViewById(R.id.highString);
		highString.setTypeface(font, Typeface.BOLD);

		TextView high = (TextView) findViewById(R.id.highTextView);
		high.setTypeface(font, Typeface.BOLD);
		/**
		 * SHould read from the file and check the time and not just from the shared pref
		 */
		SharedPreferences highPref = getSharedPreferences("findhighscore", 0);

		final String score = highPref.getString("highscore", "NO SCORE AVAILABLE");		//this gets updated only when the user wins
		high.setText(score);


		/**
		 * Checks whether the user lost or won and then the user enters the name if the user won.
		 */
		if(((boolean) getIntent().getExtras().get("win")))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Enter your name : ");

			final EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_CLASS_TEXT);
			builder.setView(input);

			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String m_Text = input.getText().toString();
					name = m_Text;
					System.out.println(name);
					new PersistenceLayer().saveText(new ScoreInformationBean(name,time));
				}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

			builder.show();



		}


		TextView resultView = (TextView) findViewById(R.id.result);
		resultView.setTypeface(font, Typeface.BOLD);


	}

	/**
	 * this function is used to handle click events on the action bar 
	 * buttons and icons
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.replay:

			startActivity(new Intent(FinalActivity.this,
					BarrelRaceActivity.class));
			finish();

			break;

		case R.id.mainmenu:

			startActivity(new Intent(FinalActivity.this,
					Home.class));
			finish();
		}


	}
	/**
	 * Function to override the back press
	 */
	@Override
	public void onBackPressed() {

		startActivity(new Intent(this, Home.class));
		finish();
	}
}
