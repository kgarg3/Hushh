package com.hush.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class HushUtils {

	public static void writeToFile(Context context, String data) {
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(Constants.hushNotifsFile, Context.MODE_PRIVATE));
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}

	public static void deleteFile(Context context) {
        context.deleteFile(Constants.hushNotifsFile);
	}
	
	public static ArrayList<String> readFromFile(Context context) {

	    ArrayList<String> lines = new ArrayList<String>();

	    try {
	        InputStream inputStream = context.openFileInput(Constants.hushNotifsFile);

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                lines.add(receiveString);
	            }

	            inputStream.close();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("TAG", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("TAG", "Can not read file: " + e.toString());
	    }

	    return lines;
	}

}
