/**
 * Class Name: PersistenceLayer.java
 * 
 * This class is used as a bean to save the intermediate data for high scores and also used to 
 * perform sorting of the 10 highscores in descending order.
 * 
 */

package com.shobhit.example;

public class ScoreInformationBean implements Comparable<ScoreInformationBean>{
	
	private String name;
	private String score;
	
	public ScoreInformationBean() {
		super();
	}
	
	public ScoreInformationBean(String name, String score) {
		super();
		this.name = name;
		this.score = score;
	}
	
	// Getters and setters for the variables
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}

	@Override
	public int compareTo(ScoreInformationBean another) {
		// TODO Auto-generated method stub
		String compareScore = ((ScoreInformationBean)another).getScore();
		return this.score.compareTo(compareScore);
	}
	/**
	 * Function to enable the objects to be sorted according to the
	 * high scores.
	 */
	public static Comparable<ScoreInformationBean> scoreComparator = new Comparable<ScoreInformationBean>() {
		
		
		@SuppressWarnings("unused")
		public int compare(ScoreInformationBean first, ScoreInformationBean another) {
			// TODO Auto-generated method stub
			return first.getScore().compareTo(another.getScore());
		}

		@Override
		public int compareTo(ScoreInformationBean another) {
			// TODO Auto-generated method stub
			return 0;
		}
	};
	

}
