/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.samples;

import java.util.LinkedList;
import java.util.Locale;

import android.R.integer;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshPinnedSectionListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.handmark.pulltorefresh.library.internal.PinnedSectionListView.PinnedSectionListAdapter;

public final class PullToRefreshPinnedListActivity extends ListActivity 
{
	private LinkedList<String> mListItems;
	private PullToRefreshPinnedSectionListView mPullRefreshListView;
	private SimpleAdapter mAdapter;
	private char startChar = (char)('A' + 6);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ptr_pinned_list);
		// 下拉刷新视图
		mPullRefreshListView = (PullToRefreshPinnedSectionListView) findViewById(R.id.pull_refresh_list);

		// 设置下拉刷新视图的刷新监听器
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				Toast.makeText(PullToRefreshPinnedListActivity.this, "重新刷新...!", Toast.LENGTH_SHORT).show();
				startChar = (char)('A' - 1);
				new GetDataTask(true).execute();
			}
		});

		// Add an end-of-list listener
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() 
		{
			@Override
			public void onLastItemVisible() {
				new GetDataTask(false).execute();
				Toast.makeText(PullToRefreshPinnedListActivity.this, "后面添加...!", Toast.LENGTH_SHORT).show();
			}
		});

		ListView actualListView = mPullRefreshListView.getRefreshableView();

		mListItems = new LinkedList<String>();

		mAdapter = new SimpleAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1);

		/**
		 * Add Sound Event Listener
		 */
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(this);
		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
		mPullRefreshListView.setOnPullEventListener(soundListener);

		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		actualListView.setAdapter(mAdapter);
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String> {
		private boolean isClear;
		
		public GetDataTask(boolean isClear)
		{
			this.isClear = isClear;
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			return "1";
		}

		@Override
		protected void onPostExecute(String result) {
//			mListItems.addFirst("Added after refresh...");\
			mAdapter.notifyDataSetChanged();

			generateList(isClear);
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
	
	/**
	 * 生成list
	 * @param isClear false表明是往后面添加数据，true刷新数据
	 */
	private void generateList(boolean isClear)
	{
		if(startChar < 'Z' )
		{
			int endCharInteger = Math.min((int)'Z', startChar + 6);
			mAdapter.generateDataset((char)(startChar + 1),(char)endCharInteger, isClear);
			startChar = (char)(startChar + 6);
		}
	}

	static class Item {

		public static final int ITEM = 0;
		public static final int SECTION = 1;

		public final int type;
		public final String text;

		public int sectionPosition;
		public int listPosition;

		public Item(int type, String text) {
		    this.type = type;
		    this.text = text;
		}

		@Override public String toString() {
			return text;
		}

	}
	
	static class SimpleAdapter extends ArrayAdapter<Item> implements PinnedSectionListAdapter {

        private static final int[] COLORS = new int[] {
            Color.GREEN, Color.YELLOW,
            Color.CYAN, Color.RED };

        public SimpleAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
            generateDataset('A', (char)('A' + 6), false);
        }

        public void generateDataset(char from, char to, boolean clear) {
        	
        	if (clear) clear();
        	
            final int sectionsNumber = to - from + 1;
            prepareSections(sectionsNumber);

            int sectionPosition = 0, listPosition = 0;
            for (char i=0; i<sectionsNumber; i++) {
                Item section = new Item(Item.SECTION, String.valueOf((char)(from + i)));
                section.sectionPosition = sectionPosition;
                section.listPosition = listPosition++;
                onSectionAdded(section, sectionPosition);
                add(section);

                final int itemsNumber = i + 4;
                for (int j=0;j<itemsNumber;j++) {
                    Item item = new Item(Item.ITEM, section.text.toUpperCase(Locale.ENGLISH) + " - " + j);
                    item.sectionPosition = sectionPosition;
                    item.listPosition = listPosition++;
                    add(item);
                }

                sectionPosition++;
            }
            notifyDataSetChanged();
        }
        
        protected void prepareSections(int sectionsNumber) { }
        protected void onSectionAdded(Item section, int sectionPosition) { }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTextColor(Color.DKGRAY);
            view.setBackgroundColor(Color.WHITE);
            view.setTag("" + position);
            Item item = getItem(position);
            if (item.type == Item.SECTION) {
                //view.setOnClickListener(PinnedSectionListActivity.this);
                view.setBackgroundColor(COLORS[item.sectionPosition % COLORS.length]);
            }
            return view;
        }

        @Override public int getViewTypeCount() {
            return 2;
        }

        @Override public int getItemViewType(int position) {
            return getItem(position).type;
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return viewType == Item.SECTION;
        }
    }
}
