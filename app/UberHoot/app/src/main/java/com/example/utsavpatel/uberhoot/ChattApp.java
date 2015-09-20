package com.example.utsavpatel.uberhoot;

import android.app.Application;

import com.parse.Parse;

/**
 * The Class ChattApp is the Main Application class of this app. The onCreate
 * method of this class initializes the Parse.
 */
public class ChattApp extends Application
{

	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate()
	{
		super.onCreate();

		Parse.initialize(this, "Cm9ccxez45NH1WfpXr165sKc8we6CXY0aFU7lTkY", "PWr2hGd42CPSMtSBJv9zwnFO5LPAqUyO2nca8DqS");

	}
}
