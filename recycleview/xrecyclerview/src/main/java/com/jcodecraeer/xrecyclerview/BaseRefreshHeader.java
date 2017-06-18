package com.jcodecraeer.xrecyclerview;

/**
 * Q164454216
 * Created by xiaoke on 2017/1/2.
 */
interface BaseRefreshHeader {

	int STATE_NORMAL = 0;
	int STATE_RELEASE_TO_REFRESH = 1;
	int STATE_REFRESHING = 2;
	int STATE_DONE = 3;

	void onMove(float delta);

	boolean releaseAction();

	void refreshComplete();

}