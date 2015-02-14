/**
 * Class Name: PersistenceLayer.java
 * This class is used to save the highscores in the text file on device
 * it checks for permission first and then has functions to read,write and delete
 * records from the file.
 * 
 */

package com.shobhit.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.os.Environment;

public class PersistenceLayer {

	private ArrayList<ScoreInformationBean> beanList = new ArrayList<ScoreInformationBean>();
	private ArrayList<ScoreInformationBean> beanFinal = new ArrayList<ScoreInformationBean>();
	//	private List<ContactManagerBean> bean;	

	String fileName = "Records.txt";	//setting the name of the file as Record.txt
	String path = Environment.getExternalStorageDirectory().getAbsolutePath();


	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() 
	{
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() 
	{
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Performs save function by taking object of score information bean class
	 * and then checking whether the app has permission to write on the 
	 * SD card and also if the SD card is mounted or not.
	 * 
	 * 
	 */

	public String saveText(ScoreInformationBean bean)
	{
		try
		{

			if((isExternalStorageReadable()) && (isExternalStorageWritable()))
			{
				File myFile = new File(path, fileName);
				if(!myFile.exists())
				{
					myFile.createNewFile();
				}
				FileWriter fw = new FileWriter(myFile.getAbsoluteFile(),true);
				BufferedWriter bw = new BufferedWriter(fw);				
				String name = bean.getName();
				String score = bean.getScore();

				/*if(name.equalsIgnoreCase(""))
				{
					bw.close();
					fw.close();
					return "empty";

				}*/

				bw.write(name + "|" + score);
				bw.newLine();
				bw.close();
				fw.close();

				return "success";
			}
			else
			{
				return "failure";
			}

		}
		catch(Exception e)
		{
			return "exception";
		}
	}
	/**
	 * Reads the file
	 * using delimeter
	 * @param Activity
	 */
	public ArrayList<ScoreInformationBean> callRead()
	{
		try {
			File myFile = new File(path, fileName);
			FileReader fr = new FileReader(myFile);
			BufferedReader br = new BufferedReader(fr);
			String s;
			//			String[] names = new String[10];
			//			String[] scores = new String[10];
			//reading the file by the help of the delimiter that we push during save

			while ((s = br.readLine())!= null)
			{
				String[] delimeter = s.split("\\|");
				//				Arrays.sort(delimeter,Collections.reverseOrder());
				//				for (int i=0; i<delimeter.length;i++)
				//				{
				//					names[i] = delimeter[i];
				//					scores[i] = delimeter[i+1];
				//					
				//				}
				//				Arrays.sort
				//				ScoreInformationBean beanRead = new ScoreInformationBean(names[0],scores[0]);
				ScoreInformationBean beanRead = new ScoreInformationBean(delimeter[0],delimeter[1]);
				beanList.add(beanRead);
			}
			/**
			 * Logic to restrict the no of items to 10 in the list
			 */
			int count = 0;
			Collections.sort(beanList);
			Iterator<ScoreInformationBean> it = beanList.iterator();
			while(it.hasNext() && (count < 10))
			{
				beanFinal.add(it.next());
				count++;
			}


			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return beanFinal;

	}

	/**
	 * Function to delete the contact
	 * @param rowIndex
	 * @param act
	 * @return An arraylist containing score information beans
	 */

	public ArrayList<ScoreInformationBean> deleteRecord(int rowIndex)
	{
		ArrayList<ScoreInformationBean> deleteList = new ArrayList<ScoreInformationBean>();
		try 
		{
			deleteList = callRead();
			deleteList.remove(rowIndex);
			File myFile = new File(path, fileName);
			PrintWriter pw = new PrintWriter(myFile);
			pw.append("");
			pw.flush();
			pw.close();

			for(ScoreInformationBean b: deleteList)
			{
				saveText(b);
			}

		} catch (Exception e) {
		}
		return deleteList;
	}
}



