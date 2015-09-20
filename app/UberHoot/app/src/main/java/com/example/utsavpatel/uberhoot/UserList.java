package com.example.utsavpatel.uberhoot;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utsavpatel.uberhoot.ParseUtils.ChatUtils;
import com.example.utsavpatel.uberhoot.custom.CustomActivity;
import com.example.utsavpatel.uberhoot.utils.Const;
import com.example.utsavpatel.uberhoot.utils.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * The Class UserList is the Activity class. It shows a list of all users of
 * this app. It also shows the Offline/Online status of users.
 */
public class UserList extends CustomActivity
{
	ProgressDialog dia;

	/** The Chat list. */
	private ArrayList<ParseUser> uList;

	/** The user. */
	public static ParseUser user;
	Button chatbutton;
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_list);
		chatbutton = (Button) findViewById(R.id.btchat);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		updateUserStatus(true);
		chatbutton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {

				// write code to fetch the random user id when the user wants to talk to a random person
				CharSequence colors[] = new CharSequence[] {"Random", "Tech", "Movies", "Music"};

				AlertDialog.Builder builder = new AlertDialog.Builder(UserList.this);
				builder.setTitle("Chat about:");
				builder.setItems(colors, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//startDia();
						ChatUtils chat = new ChatUtils(UserList.this);
						chat.startNewChat(ParseUser.getCurrentUser(), "random", true, UserList.this);
					}
				});
				builder.show();


				//startActivity(new Intent(UserList.this,Chat.class).putExtra(Const.EXTRA_DATA, "variable")); // insert the username you got here in place of "variable"
			}
		});
	}

	public void StartChatActivity(String friendId) {
		if(dia!=null)
			dia.dismiss();
		Intent mainIntent = new Intent(getApplicationContext(), Chat.class).putExtra(Const.EXTRA_DATA, friendId);
		startActivity(mainIntent);
		finish();
	}

	public void startDia(){
		dia = ProgressDialog.show(this, null,
				getString(R.string.alert_wait));
	}

	public void stopDia(){
		if(dia!=null)
			dia.dismiss();
		dia = null;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		updateUserStatus(false);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		loadUserList();

	}

	/**
	 * Update user status.
	 * 
	 * @param online
	 *            true if user is online
	 */
	private void updateUserStatus(boolean online)
	{
		user.put("online", online);
		user.saveEventually();
	}

	/**
	 * Load list of users.
	 */
	private void loadUserList()
	{
		final ProgressDialog dia = ProgressDialog.show(this, null,
				getString(R.string.alert_loading));					//	We get the list of users in li. to get a list of users we want just change the query
		ParseUser.getQuery().whereNotEqualTo("username", user.getUsername())
				.findInBackground(new FindCallback<ParseUser>() {

					@Override
					public void done(List<ParseUser> li, ParseException e) {
						dia.dismiss();
						if (li != null) {
							if (li.size() == 0)
								Toast.makeText(UserList.this,
										R.string.msg_no_user_found,
										Toast.LENGTH_SHORT).show();

							uList = new ArrayList<ParseUser>(li);
							ListView list = (ListView) findViewById(R.id.list);
							list.setAdapter(new UserAdapter());
							list.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
														View arg1, int pos, long arg3) {
									startActivity(new Intent(UserList.this,
											Chat.class).putExtra(
											Const.EXTRA_DATA, uList.get(pos)
													.getUsername()));
								}
							});
						} else {
							Utils.showDialog(
									UserList.this,
									getString(R.string.err_users) + " "
											+ e.getMessage());
							e.printStackTrace();

						}
					}
				});
	}



	/**
	 * The Class UserAdapter is the adapter class for User ListView. This
	 * adapter shows the user name and it's only online status for each item.
	 */
	public void createToast(String msg) {
		Toast.makeText(UserList.this, msg, Toast.LENGTH_SHORT).show();
	}

	private class UserAdapter extends BaseAdapter
	{

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return uList.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public ParseUser getItem(int arg0)
		{
			return uList.get(arg0);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int arg0)
		{
			return arg0;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int pos, View v, ViewGroup arg2)
		{
			if (v == null)
				v = getLayoutInflater().inflate(R.layout.chat_item, null);

			ParseUser c = getItem(pos);
			TextView lbl = (TextView) v;
			lbl.setText(c.getUsername());
			lbl.setCompoundDrawablesWithIntrinsicBounds(
					c.getBoolean("online") ? R.drawable.ic_online
							: R.drawable.ic_offline, 0, R.drawable.arrow, 0);

			return v;
		}

	}
}
