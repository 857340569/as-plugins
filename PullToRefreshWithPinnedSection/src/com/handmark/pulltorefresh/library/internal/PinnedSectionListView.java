/*
 * Copyright (C) 2013 Sergej Shafarenka, halfbit.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file kt in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.handmark.pulltorefresh.library.internal;

import com.handmark.pulltorefresh.samples.BuildConfig;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

/**
 * 使用方法，在对应的adapter中添加主要的方法
 * <br/>可参考PaymentDetailsAdapter.class
 * <br/>ListView, which is capable to pin section views at its top while the rest is still scrolled.
 */
public class PinnedSectionListView extends ListView implements EmptyViewMethodAccessor {

    //-- inner classes

	/** List adapter to be implemented for being used with PinnedSectionListView adapter. */
	public static interface PinnedSectionListAdapter extends ListAdapter {
		/** This method shall return 'true' if views of given type has to be pinned. */
		boolean isItemViewTypePinned(int viewType);
	}

	/** Wrapper class for pinned section view and its position in the list. */
	static class PinnedSection {
		public View view;
		public int position;
		public long id;
	}

	//-- class fields

    // fields used for handling touch events
    private final Rect mTouchRect = new Rect();
    private final PointF mTouchPoint = new PointF();
    private int mTouchSlop;
    private View mTouchTarget;
    private MotionEvent mDownEvent;

    // fields used for drawing shadow under a pinned section
    private GradientDrawable mShadowDrawable;
    private int mSectionsDistanceY;
    private int mShadowHeight;

    /** Delegating listener, can be null. */
    OnScrollListener mDelegateOnScrollListener;

    /** Shadow for being recycled, can be null. */
    PinnedSection mRecycleSection;

    /** shadow instance with a pinned view, can be null. */
    PinnedSection mPinnedSection;

    /** Pinned view Y-translation. We use it to stick pinned view to the next section. */
    int mTranslateY;

	/** Scroll listener which does the magic */
	private final OnScrollListener mOnScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (mDelegateOnScrollListener != null) { // delegate
				mDelegateOnScrollListener.onScrollStateChanged(view, scrollState);
			}
		}

		@Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mDelegateOnScrollListener != null) { // delegate
                mDelegateOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }

            // get expected adapter or fail fast
            ListAdapter adapter = getAdapter();
            if (adapter == null || visibleItemCount == 0) return; // nothing to do
            // 第一个可见视图是否是pinned
            final boolean isFirstVisibleItemSection =
                    isItemViewTypePinned(adapter, adapter.getItemViewType(firstVisibleItem));

            if (isFirstVisibleItemSection) {
                View sectionView = getChildAt(0);
                if (sectionView.getTop() == getPaddingTop()) { // view sticks to the top, no need for pinned shadow
                    destroyPinnedShadow();
                } else { // section doesn't stick to the top, make sure we have a pinned shadow
                    ensureShadowForPosition(firstVisibleItem, firstVisibleItem, visibleItemCount);
                }

            } else { // section is not at the first visible position
                int sectionPosition = findCurrentSectionPosition(firstVisibleItem);
                if (sectionPosition > -1) { // we have section position
                    ensureShadowForPosition(sectionPosition, firstVisibleItem, visibleItemCount);
                } else { // there is no section for the first visible item, destroy shadow
                    destroyPinnedShadow();
                }
            }
		};

	};

	/** Default change observer. */
	/** 数据集观察者 */
    private final DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override public void onChanged() {
            recreatePinnedShadow();
        };
        @Override public void onInvalidated() {
            recreatePinnedShadow();
        }
    };

	//-- constructors

    //-- 在这些构造函数里面都有initView()方法调用
    public PinnedSectionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PinnedSectionListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    /** 初始化视图，设置scrollListener，slop,shadow */
    private void initView() {
        setOnScrollListener(mOnScrollListener);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        initShadow(true);
    }

    //-- public API methods
    //-- 实时刷新阴影
    public void setShadowVisible(boolean visible) {
        initShadow(visible);
        // 我这里加上了visible判断
        if (mPinnedSection != null && visible) {
            View v = mPinnedSection.view;
            invalidate(v.getLeft(), v.getTop(), v.getRight(), v.getBottom() + mShadowHeight);
        }
    }

    //-- pinned section drawing methods

    //-- 初始化阴影
    public void initShadow(boolean visible) {
        if (visible) {
            if (mShadowDrawable == null) {
                mShadowDrawable = new GradientDrawable(Orientation.TOP_BOTTOM,
                        new int[] { Color.parseColor("#ffa0a0a0"), Color.parseColor("#50a0a0a0"), Color.parseColor("#00a0a0a0")});
                mShadowHeight = (int) (8 * getResources().getDisplayMetrics().density);
            }
        } else {
            if (mShadowDrawable != null) {
                mShadowDrawable = null;
                mShadowHeight = 0;
            }
        }
    }

	/** Create shadow wrapper with a pinned view for a view at given position */
	void createPinnedShadow(int position) {
		// 尝试使用回收视图，很好的设计
		// try to recycle shadow
		PinnedSection pinnedShadow = mRecycleSection;
		mRecycleSection = null;

		// create new shadow, if needed
		if (pinnedShadow == null) pinnedShadow = new PinnedSection();
		// request new view using recycled view, if such
		View pinnedView = getAdapter().getView(position, pinnedShadow.view, PinnedSectionListView.this);

		// read layout parameters
		LayoutParams layoutParams = (LayoutParams) pinnedView.getLayoutParams();
		if (layoutParams == null) {
			// generateDefaultLayoutParams这个用得好
		    layoutParams = (LayoutParams) generateDefaultLayoutParams();
		    pinnedView.setLayoutParams(layoutParams);
		}

		// 得到视图的尺寸
//		int heightMode = MeasureSpec.getMode(layoutParams.height);
//		int heightSize = MeasureSpec.getSize(layoutParams.height);
//
//		if (heightMode == MeasureSpec.UNSPECIFIED) heightMode = MeasureSpec.EXACTLY;

		//-- 在这里将它们进行对比
		int maxHeight = getHeight() - getListPaddingTop() - getListPaddingBottom();
//		if (heightSize > maxHeight) heightSize = maxHeight;

		// measure & layout
		int ws = MeasureSpec.makeMeasureSpec(getWidth() - getListPaddingLeft() - getListPaddingRight(), MeasureSpec.EXACTLY);
//		int hs = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
		// 用listview的值去measure，得到更加精确
		pinnedView.measure(ws, 0);
		pinnedView.layout(0, 0, pinnedView.getMeasuredWidth(), Math.min(maxHeight / 2, pinnedView.getMeasuredHeight()));
		mTranslateY = 0;

		// initialize pinned shadow
		pinnedShadow.view = pinnedView;
		pinnedShadow.position = position;
		pinnedShadow.id = getAdapter().getItemId(position);

		// store pinned shadow
		mPinnedSection = pinnedShadow;
	}

	/** Destroy shadow wrapper for currently pinned view */
	void destroyPinnedShadow() {
	    if (mPinnedSection != null) {
	        // keep shadow for being recycled later
	    	// 这个地方用recycle section，很好
	        mRecycleSection = mPinnedSection;
	        mPinnedSection = null;
	    }
	}

	/** Makes sure we have an actual pinned shadow for given position. */
	/** 确保我们有钉住的阴影 */
    void ensureShadowForPosition(int sectionPosition, int firstVisibleItem, int visibleItemCount) {
        if (visibleItemCount < 2) { // no need for creating shadow at all, we have a single visible item
            destroyPinnedShadow();
            return;
        }

        if (mPinnedSection != null
                && mPinnedSection.position != sectionPosition) { // invalidate shadow, if required
        	destroyPinnedShadow();
        }

        if (mPinnedSection == null) { // create shadow, if empty
            createPinnedShadow(sectionPosition);
        }

        // align shadow according to next section position, if needed
        int nextPosition = sectionPosition + 1;
        if (nextPosition < getCount()) {
            int nextSectionPosition = findFirstVisibleSectionPosition(nextPosition,
                    visibleItemCount - (nextPosition - firstVisibleItem));
            if (nextSectionPosition > -1) {
                View nextSectionView = getChildAt(nextSectionPosition - firstVisibleItem);
                final int bottom = mPinnedSection.view.getBottom() + getPaddingTop();
                mSectionsDistanceY = nextSectionView.getTop() - bottom;
                if (mSectionsDistanceY < 0) {
                    // next section overlaps pinned shadow, move it up
                    mTranslateY = mSectionsDistanceY;
                } else {
                    // next section does not overlap with pinned, stick to top
                    mTranslateY = 0;
                }
            } else {
                // no other sections are visible, stick to top
                mTranslateY = 0;
                mSectionsDistanceY = Integer.MAX_VALUE;
            }
        }

    }

    // 找到第一个可见的区域视图
	int findFirstVisibleSectionPosition(int firstVisibleItem, int visibleItemCount) {
		ListAdapter adapter = getAdapter();

        int adapterDataCount = adapter.getCount();
        if (getLastVisiblePosition() >= adapterDataCount) return -1; // dataset has changed, no candidate

        //-- 这里重新计算可见视图数，防止后面数组越界
        if (firstVisibleItem + visibleItemCount >= adapterDataCount){//added to prevent index Outofbound (in case)
            visibleItemCount = adapterDataCount - firstVisibleItem;
        }

		for (int childIndex = 0; childIndex < visibleItemCount; childIndex++) {
			int position = firstVisibleItem + childIndex;
			int viewType = adapter.getItemViewType(position);
			if (isItemViewTypePinned(adapter, viewType)) return position;
		}
		return -1;
	}

	//-- 找到目前的所在的区域的钉子视图
	int findCurrentSectionPosition(int fromPosition) {
		ListAdapter adapter = getAdapter();

		if (fromPosition >= adapter.getCount()) return -1; // dataset has changed, no candidate
		
		// 快速的方法
		if (adapter instanceof SectionIndexer) {
			// try fast way by asking section indexer
			SectionIndexer indexer = (SectionIndexer) adapter;
			int sectionPosition = indexer.getSectionForPosition(fromPosition);
			int itemPosition = indexer.getPositionForSection(sectionPosition);
			int typeView = adapter.getItemViewType(itemPosition);
			if (isItemViewTypePinned(adapter, typeView)) {
				return itemPosition;
			} // else, no luck
		}

		// try slow way by looking through to the next section item above
		for (int position=fromPosition; position>=0; position--) {
			int viewType = adapter.getItemViewType(position);
			if (isItemViewTypePinned(adapter, viewType)) return position;
		}
		return -1; // no candidate found
	}

	void recreatePinnedShadow() {
	    destroyPinnedShadow();
        ListAdapter adapter = getAdapter();
        if (adapter != null && adapter.getCount() > 0) {
            int firstVisiblePosition = getFirstVisiblePosition();
            int sectionPosition = findCurrentSectionPosition(firstVisiblePosition);
            if (sectionPosition == -1) return; // no views to pin, exit
            ensureShadowForPosition(sectionPosition,
                    firstVisiblePosition, getLastVisiblePosition() - firstVisiblePosition);
        }
	}

	@Override
	public void setOnScrollListener(OnScrollListener listener) {
		if (listener == mOnScrollListener) {
			super.setOnScrollListener(listener);
		} else {
			mDelegateOnScrollListener = listener;
		}
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		super.onRestoreInstanceState(state);
		post(new Runnable() {
			@Override public void run() { // restore pinned view after configuration change
			    recreatePinnedShadow();
			}
		});
	}

	@Override
	public void setAdapter(ListAdapter adapter) {

	    // assert adapter in debug mode
		if (BuildConfig.DEBUG && adapter != null) {
			if (!(adapter instanceof PinnedSectionListAdapter))
				throw new IllegalArgumentException("Does your adapter implement PinnedSectionListAdapter?");
			if (adapter.getViewTypeCount() < 2)
				throw new IllegalArgumentException("Does your adapter handle at least two types" +
						" of views in getViewTypeCount() method: items and sections?");
		}
		
		//-- 从原生的下拉刷新来的
		if (null != mLvFooterLoadingFrame && !mAddedLvFooter) {
			addFooterView(mLvFooterLoadingFrame, null, false);
			mAddedLvFooter = true;
		}
		//-- 从原生的下拉刷新来的

		// unregister observer at old adapter and register on new one
		ListAdapter oldAdapter = getAdapter();
		if (oldAdapter != null) oldAdapter.unregisterDataSetObserver(mDataSetObserver);
		if (adapter != null) adapter.registerDataSetObserver(mDataSetObserver);

		// destroy pinned shadow, if new adapter is not same as old one
		if (oldAdapter != adapter) destroyPinnedShadow();

		super.setAdapter(adapter);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
	    super.onLayout(changed, l, t, r, b);
        if (mPinnedSection != null) {
            int parentWidth = r - l - getPaddingLeft() - getPaddingRight();
            int shadowWidth = mPinnedSection.view.getWidth();
            if (parentWidth != shadowWidth) {
                recreatePinnedShadow();
            }
        }
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		try
		{
			super.dispatchDraw(canvas);

			if (mPinnedSection != null)
			{

				// prepare variables
				int pLeft = getListPaddingLeft();
				int pTop = getListPaddingTop();
				View view = mPinnedSection.view;

				// draw child
				// 这不是必要的
				canvas.save();

				int clipHeight = view.getHeight()
						+ (mShadowDrawable == null ? 0 : Math
								.min(mShadowHeight, mSectionsDistanceY));
				canvas.clipRect(pLeft, pTop, pLeft + view.getWidth(), pTop + clipHeight);

				canvas.translate(pLeft, pTop + mTranslateY);
				drawChild(canvas, mPinnedSection.view, getDrawingTime());

				if (mShadowDrawable != null && mSectionsDistanceY > 0)
				{
					mShadowDrawable.setBounds(mPinnedSection.view.getLeft(),
							mPinnedSection.view.getBottom(), mPinnedSection.view.getRight(),
							mPinnedSection.view.getBottom() + mShadowHeight);
					mShadowDrawable.draw(canvas);
				}

				canvas.restore();
			}
		} catch (IndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
	}

	//-- touch handling methods

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
		try
		{
			final float x = ev.getX();
			final float y = ev.getY();
			final int action = ev.getAction();

			if (action == MotionEvent.ACTION_DOWN && mTouchTarget == null && mPinnedSection != null
					&& isPinnedViewTouched(mPinnedSection.view, x, y))
			{ // create touch target

				// user touched pinned view
				mTouchTarget = mPinnedSection.view;
				mTouchPoint.x = x;
				mTouchPoint.y = y;

				// copy down event for eventually be used later
				mDownEvent = MotionEvent.obtain(ev);
			}

			// 由touchTarget去处理点击事件
			if (mTouchTarget != null)
			{
				if (isPinnedViewTouched(mTouchTarget, x, y))
				{ // forward event to pinned view
					mTouchTarget.dispatchTouchEvent(ev);
				}

				if (action == MotionEvent.ACTION_UP)
				{ // perform onClick on pinned view
					super.dispatchTouchEvent(ev);
					performPinnedItemClick();
					clearTouchTarget();

				} else if (action == MotionEvent.ACTION_CANCEL)
				{ // cancel
					clearTouchTarget();

				} else if (action == MotionEvent.ACTION_MOVE)
				{
					if (Math.abs(y - mTouchPoint.y) > mTouchSlop)
					{

						// cancel sequence on touch target
						MotionEvent event = MotionEvent.obtain(ev);
						event.setAction(MotionEvent.ACTION_CANCEL);
						mTouchTarget.dispatchTouchEvent(event);
						event.recycle();

						// provide correct sequence to super class for further handling
						super.dispatchTouchEvent(mDownEvent);
						super.dispatchTouchEvent(ev);
						clearTouchTarget();

					}
				}

				return true;
			}

			// call super if this was not our pinned view
			return super.dispatchTouchEvent(ev);
		} catch (IndexOutOfBoundsException e)
		{
			e.printStackTrace();
			return false;
		}
    }

    private boolean isPinnedViewTouched(View view, float x, float y) {
        view.getHitRect(mTouchRect);

        // by taping top or bottom padding, the list performs on click on a border item.
        // we don't add top padding here to keep behavior consistent.
        mTouchRect.top += mTranslateY;

        mTouchRect.bottom += mTranslateY + getPaddingTop();
        mTouchRect.left += getPaddingLeft();
        mTouchRect.right -= getPaddingRight();
        return mTouchRect.contains((int)x, (int)y);
    }

    private void clearTouchTarget() {
        mTouchTarget = null;
        if (mDownEvent != null) {
            mDownEvent.recycle();
            mDownEvent = null;
        }
    }

    /** 在这里处理item的点击事件 */
    private boolean performPinnedItemClick() {
        if (mPinnedSection == null) return false;

        OnItemClickListener listener = getOnItemClickListener();
        if (listener != null && getAdapter().isEnabled(mPinnedSection.position)) {
            View view =  mPinnedSection.view;
            playSoundEffect(SoundEffectConstants.CLICK);
            if (view != null) {
                view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
            }
            listener.onItemClick(this, view, mPinnedSection.position, mPinnedSection.id);
            return true;
        }
        return false;
    }

    //-- 注意：这里我们可以用HeaderViewListAdapter来得到listAdapter
    public static boolean isItemViewTypePinned(ListAdapter adapter, int viewType) {
        if (adapter instanceof HeaderViewListAdapter) {
            adapter = ((HeaderViewListAdapter)adapter).getWrappedAdapter();
        }
        return ((PinnedSectionListAdapter) adapter).isItemViewTypePinned(viewType);
    }

    //-- 主要是从原生的下拉刷新视图来的
    private boolean mAddedLvFooter = false;
    private FrameLayout mLvFooterLoadingFrame;
    
    /**
     * 添加下面的footer视图
     */
    public void setFooterFramelayout(FrameLayout mLvFooterLoadingFrame)
    {
    	this.mLvFooterLoadingFrame = mLvFooterLoadingFrame;
    }
    
    @Override
	public void setEmptyView(View emptyView) {
		PinnedSectionListView.this.setEmptyView(emptyView);
	}
    
	@Override
	public void setEmptyViewInternal(View emptyView)
	{
		super.setEmptyView(emptyView);
	}

}
