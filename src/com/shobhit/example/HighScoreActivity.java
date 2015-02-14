/**
 * Class Name: HighScoreActivity.java
 * @author 
 * 
 * This activity shows the shows the highscores
 * in a list view and uses a custom adapter and
 * arraylists to do it.
 * 
 */
package com.shobhit.example;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class HighScoreActivity extends Activity {

	/**
	 * Calling the read function and then setting the adapter
	 * on the arraylist recieved.
	 * the arraylist recieved is sorted.
	 */
	MyAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_high_score);
		
		final ListView listView = (ListView) findViewById(R.id.highScoreList);
		
		ArrayList<ScoreInformationBean> listForDisplay = new ArrayList<ScoreInformationBean>();
		listForDisplay = new PersistenceLayer().callRead();
		Collections.sort(listForDisplay);
		adapter = new MyAdapter(this,listForDisplay);
		listView.setAdapter(adapter);
		
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
		startActivity(new Intent(this,Home.class));
	}
}
/**
 * This class is used to make custom adapter so that we can customise the 
 * list view other than the stock experience.
 * THis class inflates list_item.xml file
 *
 */

class MyAdapter extends BaseAdapter 
{
	private Context mContext;
	ArrayList<ScoreInformationBean> dataForDisplay;

	public MyAdapter()
	{

	}
	public MyAdapter(Context context, ArrayList<ScoreInformationBean> dataForDisplay)
	{
		super();
		mContext = context;
		this.dataForDisplay = dataForDisplay;
	}
	@Override
	public int getCount() 
	{
		return dataForDisplay.size();
	}

	@Override
	public ScoreInformationBean getItem(int position) 
	{
		return dataForDisplay.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item,parent, false);
		}
				

		TextView txtViewName = (TextView) convertView.findViewById(R.id.nameHighScoreList);
		TextView txtViewNumber = (TextView) convertView.findViewById(R.id.scoreHighScoreList);
				
		ScoreInformationBean score = dataForDisplay.get(position);
		
		txtViewName.setText(score.getName());
		txtViewNumber.setText(score.getScore());
				
		/*convertView.setOnClickListener(new View.OnClickListener() {
			
		@Override
			public void onClick(View v) {
				ScoreInformationBean cb = dataForDisplay.get(position);
				Intent intent = new Intent(mContext, EditActivity.class);
				intent.putExtra("Bean", cb);
				intent.putExtra("Position", position);
				mContext.startActivity(intent);
				
			}
		});*/
		return convertView;
		
	}
	
	


}