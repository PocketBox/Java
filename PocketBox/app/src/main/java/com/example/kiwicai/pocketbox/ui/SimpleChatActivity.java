package com.example.kiwicai.pocketbox.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiwicai.pocketbox.R;
import com.example.kiwicai.pocketbox.chat.ChatMessage;
import com.example.kiwicai.pocketbox.adapter.MessagesAdapter;
import com.example.kiwicai.pocketbox.exception.PocketBoxException;
import com.example.kiwicai.pocketbox.util.PocketConstants;
import com.smartfoxserver.v2.exceptions.SFSException;

import java.util.Date;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.Room;
import sfs2x.client.entities.User;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.requests.PublicMessageRequest;

public class SimpleChatActivity extends Activity implements IEventListener, PocketConstants {

	private final String TAG = this.getClass().getSimpleName();
	private final static String TAB_TAG_CHAT = "tChat";
	private final static String TAB_TAG_USERS = "tUsers";

	private final static boolean DEBUG_SFS = true;

	private final static int COLOR_GREEN = Color.parseColor("#99FF99");
	private final static int COLOR_RED = Color.parseColor("#FF0000");

//	private BaseEvent globalEvent;

	private enum Status {
		DISCONNECT, CONNECTED, CONNECTING, CONNECTION_ERROR, CONNECTION_LOST, LOGGED, IN_ROOM
	}

	Status currentStatus = null;

	SmartFox sfsClient;

	EditText inputServerAddress, inputServerPort, inputUserNick, inputChatMessage;
	View buttonConnect, buttonChatSend, layoutConnector, layoutLogin, layoutChat;
	TextView labelStatus, labelTagUsers;
	ListView listUsers, listMessages;
	ArrayAdapter<String> adapterUsers;
	MessagesAdapter adapterMessages;
	TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_main);
		System.setProperty("java.net.preferIPv6Addresses", "false");
		initSmartFox();
		initUI();
		setStatus(Status.DISCONNECT);
	}

	private void initSmartFox() {

		// Instantiate SmartFox client
		sfsClient = new SmartFox(DEBUG_SFS);

		// Add event listeners
		sfsClient.addEventListener(SFSEvent.CONNECTION, this);
		sfsClient.addEventListener(SFSEvent.CONNECTION_LOST, this);
		sfsClient.addEventListener(SFSEvent.LOGIN, this);
		sfsClient.addEventListener(SFSEvent.LOGIN_ERROR, this);
		sfsClient.addEventListener(SFSEvent.ROOM_JOIN, this);
		sfsClient.addEventListener(SFSEvent.USER_ENTER_ROOM, this);
		sfsClient.addEventListener(SFSEvent.USER_EXIT_ROOM, this);
		sfsClient.addEventListener(SFSEvent.PUBLIC_MESSAGE, this);

	}

	private void initUI() {
		// Load the view references
		inputServerAddress = (EditText) findViewById(R.id.edit_server_address);
		inputServerPort = (EditText) findViewById(R.id.edit_server_port);
		buttonConnect = findViewById(R.id.button_connect);
		labelStatus = (TextView) findViewById(R.id.label_status);
		layoutConnector = findViewById(R.id.container_connection);
		layoutChat = findViewById(R.id.container_chat);
		inputUserNick = (EditText) findViewById(R.id.edit_user_nick);
		listUsers = (ListView) findViewById(R.id.list_users);
		listMessages = (ListView) findViewById(R.id.list_chat);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		inputChatMessage = (EditText) findViewById(R.id.input_chat_message);
		buttonChatSend = findViewById(R.id.button_chat_send);

		// Init the views
		inputServerAddress.setText(DEFAULT_SERVER_ADDRESS);
		inputServerPort.setText(DEFAULT_SERVER_PORT);
		buttonConnect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isConnected = connect(inputServerAddress.getText().toString(), inputServerPort.getText()
						.toString());
				try {
					if (!isConnected) {
						throw new PocketBoxException(5);
					}
				} catch (PocketBoxException e) {
					e.fix(e.getErrno());
					e.createExceptionLog(SimpleChatActivity.this);
					return;
				}

				String userNick = inputUserNick.getText().toString();
				if (userNick.length() <= 0) {
					Toast.makeText(SimpleChatActivity.this, "Pick a nickname before entering the room", Toast.LENGTH_SHORT).show();
					return;
				}
				setStatus(Status.CONNECTING);
				try {
					Thread.sleep(1500);
				} catch (Exception e) {
					return;
				}
				sfsClient.setUseBlueBox(true);

				String zoneName = getString(R.string.example_zone);

				LoginRequest loginRequest = new LoginRequest(userNick, "", zoneName);

				sfsClient.send(loginRequest);
			}
		});

		buttonChatSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String message = inputChatMessage.getText().toString();
				if (message.length() > 0) {
					// As long as message is non-blank create a new
					// PublicMessage and send to the server
					sfsClient.send(new PublicMessageRequest(message));
					inputChatMessage.setText("");
				} else {
					Toast.makeText(SimpleChatActivity.this, "message cannot be empty", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// The list of users
		adapterUsers = new ArrayAdapter<String>(this, R.layout.row_user);
		listUsers.setAdapter(adapterUsers);
		adapterMessages = new MessagesAdapter(this);
		listMessages.setAdapter(adapterMessages);
		// Enable auto scroll
		listMessages.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listMessages.setStackFromBottom(true);

		tabHost.setup();
		tabHost.addTab(newTab(TAB_TAG_CHAT, R.string.chat, R.id.tab1));
		tabHost.addTab(newTab(TAB_TAG_USERS, R.string.users, R.id.tab2));
		showLayout(layoutConnector);
	}

	private TabSpec newTab(String tag, int labelId, int tabContentId) {
		View indicator = LayoutInflater.from(this).inflate(R.layout.tab_header,
				(ViewGroup) findViewById(android.R.id.tabs), false);
		TextView label = (TextView) indicator.findViewById(android.R.id.title);
		label.setText(labelId);
		if (TAB_TAG_USERS.equals(tag)) {
			labelTagUsers = label;
		}
		TabSpec tabSpec = tabHost.newTabSpec(tag);
		tabSpec.setIndicator(indicator);
		tabSpec.setContent(tabContentId);
		return tabSpec;
	}

	private void updateUsersTabLabel() {
		labelTagUsers.setText(getString(R.string.users) + " (" + adapterUsers.getCount() + ")");
	}

	@Override
	public void dispatch(final BaseEvent event) throws SFSException {
		//globalEvent = event;
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (event.getType().equalsIgnoreCase(SFSEvent.CONNECTION)) {
					if (event.getArguments().get("success").equals(true)) {
						setStatus(Status.CONNECTED, sfsClient.getConnectionMode());
						// Login as guest in current zone
						showLayout(layoutLogin);
					} else {
						setStatus(Status.CONNECTION_ERROR);
						showLayout(layoutConnector);
					}
				} else if (event.getType().equalsIgnoreCase(SFSEvent.CONNECTION_LOST)) {
					setStatus(Status.CONNECTION_LOST);
					disconnect();
					adapterMessages.clear();
					adapterUsers.clear();
					showLayout(layoutConnector);

				} else if (event.getType().equalsIgnoreCase(SFSEvent.LOGIN)) {
					setStatus(Status.LOGGED, sfsClient.getCurrentZone());
					sfsClient.send(new JoinRoomRequest(getString(R.string.example_lobby)));
				} else if (event.getType().equalsIgnoreCase(SFSEvent.ROOM_JOIN)) {
					setStatus(Status.IN_ROOM, sfsClient.getLastJoinedRoom().getName());
					showLayout(layoutChat);
					Room room = (Room) event.getArguments().get("room");
					for (User user : room.getUserList()) {
						adapterUsers.add(user.getName());
						updateUsersTabLabel();
					}

				}// When a user enter the room the user list is updated
				else if (event.getType().equals(SFSEvent.USER_ENTER_ROOM)) {
					final User user = (User) event.getArguments().get("user");
					adapterUsers.add(user.getName());
					updateUsersTabLabel();
					adapterMessages.add(new ChatMessage("User '" + user.getName() + "' joined the room"));
				}
				// When a user leave the room the user list is updated
				else if (event.getType().equals(SFSEvent.USER_EXIT_ROOM)) {
					final User user = (User) event.getArguments().get("user");
					adapterUsers.remove(user.getName());
					updateUsersTabLabel();
					adapterMessages.add(new ChatMessage("User '" + user.getName() + "' left the room"));
				}
				// When public message is received it's added to the chat history
				else if (event.getType().equals(SFSEvent.PUBLIC_MESSAGE)) {
					ChatMessage message = new ChatMessage();
					User sender = (User) event.getArguments().get("sender");
					message.setUsername(sender.getName());
					message.setMessage(event.getArguments().get("message").toString());
					message.setDate(new Date());
					// If my id and the sender id are different is a incoming message
					message.setIncomingMessage(sender.getId() != sfsClient.getMySelf().getId());
					adapterMessages.add(message);
				}
			}
		});

	}

	private boolean connect(String serverIP, String serverPort) {
		// if the user have entered port number it uses it...
		if (serverIP.length() <= 0) {
			Toast.makeText(this, "server ip wrong", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (serverPort.length() > 0) {
			int serverPortValue = 0;
			try {
				serverPortValue = Integer.parseInt(serverPort);
			} catch (Exception e) {
				Toast.makeText(this, "server port wrong", Toast.LENGTH_SHORT).show();
				return false;
			}
			if (serverPortValue <= 0) {
				Toast.makeText(this, "server port wrong", Toast.LENGTH_SHORT).show();
				return false;
			}
			// tries to connect to the server
			// connectToServer(serverIP, serverPortValue);
			sfsClient.connect(serverIP, serverPortValue);
			return true;
		}
		// otherwise uses the default port number
		else {
			// tries to connect to the server
//			sfsClient.connect(serverIP);
			Toast.makeText(this, "server port wrong", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	/**
	 * Frees the resources.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		disconnect();
		sfsClient.removeAllEventListeners();
	}

	private void disconnect() {
		if (sfsClient.isConnected()) {
			sfsClient.disconnect();
		}
	}

	private void setStatus(Status status, String... params) {
		if (status == currentStatus) {
			// If there is no status change ignore it
			return;
		}
		currentStatus = status;
		final String message;
		final int messageColor;
		final boolean connectButtonEnabled;
		switch (status) {
			case DISCONNECT:
				message = getString(R.string.disconnected);
				messageColor = COLOR_RED;
				connectButtonEnabled = true;
				break;
			case CONNECTING:
				message = getString(R.string.connecting);
				messageColor = COLOR_GREEN;
				connectButtonEnabled = false;
				break;
			case CONNECTED:
				message = getString(R.string.connected);
				messageColor = COLOR_GREEN;
				connectButtonEnabled = false;
				break;
			case CONNECTION_ERROR:
				message = getString(R.string.connection_error);
				messageColor = COLOR_RED;
				connectButtonEnabled = true;
				break;
			case LOGGED:
				message = getString(R.string.logged_into);
				messageColor = COLOR_GREEN;
				connectButtonEnabled = false;
				break;
			case CONNECTION_LOST:
				message = getString(R.string.connection_lost);
				messageColor = COLOR_RED;
				connectButtonEnabled = true;
				break;

			case IN_ROOM:
				message = getString(R.string.joined_to_room);
				messageColor = COLOR_GREEN;
				connectButtonEnabled = false;
				break;
			default:
				connectButtonEnabled = true;
				messageColor = 0;
				message = null;
		}
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				setStatusMessage(message, messageColor);
				buttonConnect.setEnabled(connectButtonEnabled);
			}
		});

	}

	private void setStatusMessage(final String message, final int color) {
		labelStatus.setText(message);
		if (color != 0) {
			labelStatus.setTextColor(color);
		}
	}

	private void showLayout(final View layoutToShow) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// Show the layout selected and hide the others
				for (View layout : new View[]{layoutChat, layoutConnector}) {
					if (layoutToShow != layout) {
						layout.setVisibility(View.GONE);
					} else {
						layout.setVisibility(View.VISIBLE);
					}
				}
			}
		});
	}

	public void jumpToMain(View view) {
		disconnect();
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("from", "chat");
		try {
			if (intent == null) {
				throw new PocketBoxException(3);
			}
		} catch (PocketBoxException e) {
			e.fix(e.getErrno());
			e.createExceptionLog(SimpleChatActivity.this);
            return;
		}
		startActivity(intent);
	}

}
