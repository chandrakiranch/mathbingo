package com.joshisk.mathbingo.core;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.joshisk.mathbingo.ResourceManager;
import com.mobeyond.mathbingo.R;

public class FacebookManager {
	private static final ResourceManager RM = ResourceManager.getInstance();

	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static String sFirstName;
	private static boolean sUserLoggedIn;

	public FacebookManager() {
		// TODO Auto-generated constructor stub
	}

	private static Session openActiveSession(final Activity pActivity, final boolean pAllowLoginUI, final StatusCallback pCallback, final List<String> pPermissions) {
		final OpenRequest openRequest = new OpenRequest(pActivity).setPermissions(pPermissions).setCallback(pCallback);
		openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		final Session session = new Builder(pActivity.getBaseContext())
			.setApplicationId(RM.activity.getString(R.string.fb_app_id))
			.build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || pAllowLoginUI) {
			Session.setActiveSession(session);
			session.openForPublish(openRequest);
			return session;
		}
		return null;
	}

	public static void checkUserLoggedIn() {
		RM.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				openActiveSession(RM.activity, false, new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state, Exception exception) {
						if (session.isOpened()) {
							Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
								@Override
								public void onCompleted(GraphUser user, Response response) {
									if (user != null) {
										sFirstName = user.getFirstName();
										FacebookManager.sUserLoggedIn = true;
									} else {
										FacebookManager.sUserLoggedIn = false;
									}
								}
							});
						}
					}
				}, PERMISSIONS);
			}
		});
	}

	private static void loginAndPost(final String pData) {
		RM.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				openActiveSession(RM.activity, true, new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state, Exception exception) {
						if (session.isOpened()) {
							Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
								@Override
								public void onCompleted(GraphUser user, Response response) {
									if (user != null) {
										sFirstName = user.getFirstName();
										final Session.OpenRequest openRequest;
										openRequest = new Session.OpenRequest(RM.activity);
										openRequest.setPermissions(PERMISSIONS);
										sUserLoggedIn = true;
										post(user.getFirstName(), pData);
									} else {
										sUserLoggedIn = false;
									}
								}
							});
						}
					}
				}, PERMISSIONS);
			}
		});
	}

	private static void post(final String pFirstName, final String pData) {
		RM.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Bundle params = new Bundle();
				params.putString("name", pFirstName + pData);
				params.putString("caption", "Fun While You Learn!!!");
				params.putString("description", "Click on Get button to get it!");
				params.putString("link", "https://play.google.com/store/apps/details?id=com.mobeyond.mathbingo");
				params.putString("picture", "http://linkToYourIcon");
				JSONObject actions = new JSONObject();
				try {
					actions.put("name", "Get");
					actions.put("link", "https://play.google.com/store/apps/details?id=com.mobeyond.mathbingo");
				} catch (Exception e) {};
				params.putString("actions", actions.toString());
				Request.Callback callback = new Request.Callback() {
					@Override
					public void onCompleted(Response response) {
						try {
							JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
							@SuppressWarnings("unused")
							String postID = null;
							try {
								postID = graphResponse.getString("id");
							} catch (JSONException e) {}
						} catch (NullPointerException e) {
						}
					}
				};
				Request request = new Request(Session.getActiveSession(), "me/feed", params, HttpMethod.POST, callback);
				RequestAsyncTask task = new RequestAsyncTask(request);
				task.execute();
			}
		});
	}

	public static void postScore(final String pScore, final String pGameMode) {
		if (sUserLoggedIn) {
			post(sFirstName, " has achieved " + pScore + " points in " + pGameMode + " in Game Name");
		} else {
			loginAndPost(" has achieved " + pScore + " points in " + pGameMode + " in Game Name");
		}
	}

	public static void postLevelCompletion(final String pLevel, final String pGameMode) {
		if (sUserLoggedIn) {
			post(sFirstName, " has completed Level " + pLevel + " in " + pGameMode + " in Game Name");
		} else {
			loginAndPost(" has completed Level " + pLevel + " in " + pGameMode + " in Game Name");
		}
	}
}
