/**
 * Class Name: SettingsActivity.java
 * 
 *  This class shows the user the game settings such as changing
 *  the color of the horse, barrel, background and also set difficulty levels 
 *  This class use shared preferences to set the values of the variables here.
 */

package com.shobhit.example;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.shobhit.example.R;

public class SettingsActivity extends Activity implements
OnItemSelectedListener {

	private Spinner horseColorSelect;
	private Spinner barrelColorSelect;
	private Spinner backGroundSelect;
	private Spinner difficultyLevelSelect;
	private Editor editor;
	static final int[] levels= new int[]{6,8,12,50};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		backGroundSelect = (Spinner) findViewById(R.id.spinnerbg);
		barrelColorSelect = (Spinner) findViewById(R.id.spinnerbarrel);
		horseColorSelect = (Spinner) findViewById(R.id.spinnerhorse);
		difficultyLevelSelect = (Spinner) findViewById(R.id.spinnerDifficulty);


		ArrayAdapter<CharSequence> adapterBarrel = ArrayAdapter
				.createFromResource(this, R.array.barrel_color_array,
						android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapterBarrel
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		barrelColorSelect.setAdapter(adapterBarrel);

		ArrayAdapter<CharSequence> adapterHorse = ArrayAdapter
				.createFromResource(this, R.array.barrel_color_array,
						android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapterHorse
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		horseColorSelect.setAdapter(adapterHorse);

		ArrayAdapter<CharSequence> adapterBg = ArrayAdapter.createFromResource(
				this, R.array.bg_color_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapterBg
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		backGroundSelect.setAdapter(adapterBg);

		// Specify the layout to use when the list of choices appears		
		ArrayAdapter<CharSequence> adapterDifficulty = ArrayAdapter.createFromResource(this, R.array.difficuty_array, android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		difficultyLevelSelect.setAdapter(adapterDifficulty);

		backGroundSelect.setOnItemSelectedListener(this);
		barrelColorSelect.setOnItemSelectedListener(this);
		horseColorSelect.setOnItemSelectedListener(this);
		difficultyLevelSelect.setOnItemSelectedListener(this);


		SharedPreferences settingsPref = getSharedPreferences("settingColor", 0);
		editor = settingsPref.edit();
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	 */
/**
 * Function to handle the clicks on the menus for different settings
 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// An item was selected. You can retrieve the selected item using
		switch (parent.getId()) {

		case R.id.spinnerbarrel:
			editor.putString("barrelColor", parent.getItemAtPosition(pos).toString());
			editor.commit();

			break;
		case R.id.spinnerhorse:

			editor.putString("horseColor", parent.getItemAtPosition(pos)
					.toString());
			editor.commit();
			break;
		case R.id.spinnerbg:
			editor.putString("bgColor", parent.getItemAtPosition(pos)
					.toString());
			editor.commit();
			break;
		case R.id.spinnerDifficulty:
			BarrelRaceModel.pixelsPerMeter = ((float) levels[difficultyLevelSelect.getSelectedItemPosition()]);	//changing the value to increase the speed of the ball
			break;
		default:
			break;
		}
	}
		

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
