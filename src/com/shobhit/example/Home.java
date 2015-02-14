/**
 * Class Name: Home.java
 * 
 * 	This activity shows the menu of the Game
 * 	With menu Play, Settings and Highscores.
 */

package com.shobhit.example;

import java.io.IOException;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class Home extends Activity implements OnClickListener {

	private ImageButton startBtn;
//	private Button btnStart;
	
//		private ImageButton highscore;
	//	private ImageButton settingsBtn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		startBtn = (ImageButton) findViewById(R.id.start_button);
		// startBtn.setBackgroundDrawable(R.drawable.ic_start_button);
		startBtn.setOnClickListener(this);
		
//		btnStart = (Button) findViewById(R.id.buttonStart);
//		btnStart.setOnClickListener(this);
//		
	

		/*highscore = (ImageButton) findViewById(R.id.highscore);
		highscore.setOnClickListener(this);

		settingsBtn = (ImageButton) findViewById(R.id.settings_button);
		settingsBtn.setOnClickListener(this);
				Dialog dialog = new Dialog(this);*/
		LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout);

		try {
			Drawable d = Drawable.createFromStream(
					getAssets().open("background.png"), null);
			layout.setBackgroundColor(color.holo_blue_bright);
			layout.setBackground(d);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		/**
		 * Function calls the settings activity as soon as the button in the menu is pressed
		 */

		if(id==R.id.home_settings)
		{
			startActivity(new Intent(this,SettingsActivity.class));
		}
		if (id == R.id.home_highScore)
		{
			/*SharedPreferences high = getSharedPreferences("findhighscore", 0);
			String score = high.getString("highscore", "NO SCORE AVAILABLE");
			Toast.makeText(this, score, Toast.LENGTH_SHORT).show();*/
			startActivity(new Intent(this,HighScoreActivity.class));
		}
		if(id == R.id.helpHome)
		{
			startActivity(new Intent(this,HelpActivity.class));
		}

		return super.onOptionsItemSelected(item);
	}


	

		public void onClick(View v) {
			
	
	switch (v.getId()) {
		case R.id.start_button:
			startActivity(new Intent(Home.this, BarrelRaceActivity.class));

			break;
		/*case R.id.buttonStart:
			startActivity(new Intent(Home.this,BarrelRaceActivity.class));
			break;

		/*case R.id.highscore:

			SharedPreferences high = getSharedPreferences("findhighscore", 0);
			String score = high.getString("highscore", "NO SCORE AVAILABLE");
			Toast.makeText(this, score, Toast.LENGTH_SHORT).show();
			break;

		case R.id.settings_button:

			startActivity(new Intent(Home.this, SettingsActivity.class));*/
		}
	}

	@Override
	public void onBackPressed()
	{
		System.exit(0);
//		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
