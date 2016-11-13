package com.dpizarro.uipicker.library.picker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dpizarro.uipicker.library.R;

import java.util.Arrays;
import java.util.List;

/*
 * Copyright (C) 2015 David Pizarro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class PickerUI extends LinearLayout {

    private static final String LOG_TAG = PickerUI.class.getSimpleName();

    private boolean itemsClickables = PickerUISettings.DEFAULT_ITEMS_CLICKABLES;

    private PickerUIItemClickListener mPickerUIListener;
    private PickerUIListView          mPickerUIListView;
    private Context                   mContext;
    private List<String>              items;
    private RelativeLayout            mMainLayoutPickerUI;
    private int                       position;
    private int                       backgroundColorPanel;
    private int                       colorLines;
    private int                       mColorTextCenterListView;
    private int                       mColorTextNoCenterListView;
    private PickerUISettings          mPickerUISettings;

    /**
     * Default constructor
     */
    public PickerUI(Context context) {
        super(context);
        mContext = context;
        if (isInEditMode()) {
            createEditModeView();
        }
        else {
            createView(null);
        }
    }

    /**
     * Default constructor
     */
    public PickerUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (isInEditMode()) {
            createEditModeView();
        }
        else {
            createView(attrs);
            getAttributes(attrs);
        }
    }

    /**
     * Default constructor
     */
    public PickerUI(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        if (isInEditMode()) {
            createEditModeView();
        }
        else {
            createView(attrs);
            getAttributes(attrs);
        }
    }

    /**
     * This method inflates the panel to be visible from Preview Layout
     */
    private void createEditModeView() {
        LayoutInflater inflater = (LayoutInflater) mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pickerui, this, true);
    }


    private void createView(AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pickerui, this, true);
        mPickerUIListView = (PickerUIListView) view.findViewById(R.id.picker_ui_listview);
        mMainLayoutPickerUI = (RelativeLayout) view.findViewById(R.id.picker_main_layout);

        setItemsClickables(itemsClickables);
    }

    /**
     * Retrieve styles attributes
     */
    private void getAttributes(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.PickerUI, 0, 0);

        if (typedArray != null) {
            try {
                itemsClickables = typedArray
                    .getBoolean(R.styleable.PickerUI_itemsClickables,
                        PickerUISettings.DEFAULT_ITEMS_CLICKABLES);
                backgroundColorPanel = typedArray.getColor(R.styleable.PickerUI_backgroundColor,
                    getResources().getColor(R.color.background_panel_pickerui));
                colorLines = typedArray.getColor(R.styleable.PickerUI_linesCenterColor,
                    getResources().getColor(R.color.lines_panel_pickerui));
                mColorTextCenterListView = typedArray
                    .getColor(R.styleable.PickerUI_textCenterColor,
                        getResources().getColor(R.color.text_center_pickerui));
                mColorTextNoCenterListView = typedArray
                    .getColor(R.styleable.PickerUI_textNoCenterColor,
                        getResources().getColor(R.color.text_no_center_pickerui));

                int idItems;
                idItems = typedArray.getResourceId(R.styleable.PickerUI_entries, -1);
                if (idItems != -1) {
                    setItems(mContext, Arrays.asList(getResources().getStringArray(idItems)));
                }

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error while creating the view PickerUI: ", e);
            } finally {
                typedArray.recycle();
            }
        }
    }

    /**
     * Slide the panel depending on the current state.
     * If slide up, the position is the half of the elements.
     */
    public void slide() {
        int position = 0;
        if (items != null) {
            position = items.size() / 2;
        }
        slide(position);
    }

    /**
     * Slide the panel depending on the current state.
     * If slide up, the position is the value selected.
     *
     * @param position the position to set in the center of the panel.
     */
    public void slide(final int position) {
        slideUp(position);
    }

    /**
     * Slide the panel to the desired direction.
     *
     * @param slide the movement of the slide. See {@link PickerUI.SLIDE}
     */
    public void slide(SLIDE slide) {
        if (slide == SLIDE.UP) {
            int position = 0;
            if (items != null) {
                position = items.size() / 2;
            }
            slideUp(position);
        }
    }

    /**
     * Show the panel to the position selected.
     *
     * @param position the position to set in the center of the panel.
     */
    private void slideUp(int position) {
        //Render to do the blur effect
        this.position = position;
        showPanelPickerUI();
    }

    /**
     * Sets the background color for the panel.
     *
     * @param color the color of the background
     */
    public void setBackgroundColorPanel(int color) {
        backgroundColorPanel = color;
    }

    /**
     * Sets the color of the lines of the center
     *
     * @param color the color of the lines
     */
    public void setLinesColor(int color) {
        colorLines = color;
    }

    /**
     * Method to enable the click of items
     *
     * @param itemsClickables the behaviour selected for items
     */
    public void setItemsClickables(boolean itemsClickables) {
        this.itemsClickables = itemsClickables;
        if (mPickerUIListView != null && mPickerUIListView.getPickerUIAdapter() != null) {
            mPickerUIListView.getPickerUIAdapter().setItemsClickables(itemsClickables);
        }
    }

    private void setTextColorsListView() {
        setColorTextCenter(mColorTextCenterListView);
        setColorTextNoCenter(mColorTextNoCenterListView);
    }

    /**
     * Method to set items to show in panel.
     * In this method, by default, the 'which' is 0 and the position is the half of the elements.
     *
     * @param items elements to show in panel
     */
    public void setItems(Context context, List<String> items) {
        if (items != null) {
            setItems(context, items, 0, items.size() / 2);
        }
    }

    /**
     * Method to set items to show in panel.
     *
     * @param context  {@link PickerUIListView} needs a context
     * @param items    elements to show in panel
     * @param which    id of the element has been clicked
     * @param position the position to set in the center of the panel.
     */
    public void setItems(Context context, List<String> items, int which, int position) {
        if (items != null) {
            this.items = items;
            mPickerUIListView.setItems(context, items, which, position, itemsClickables);
            setTextColorsListView();
        }
    }

    /**
     * Sets the text color for the item of the center.
     *
     * @param color the color of the text
     */
    public void setColorTextCenter(int color) {
        if (mPickerUIListView != null && mPickerUIListView.getPickerUIAdapter() != null) {

            int newColor;
            try {
                newColor = getResources().getColor(color);
            } catch (Resources.NotFoundException e) {
                newColor = color;
            }
            mColorTextCenterListView = newColor;
            mPickerUIListView.getPickerUIAdapter().setColorTextCenter(newColor);
        }
    }

    /**
     * Sets the text color for the items which aren't in the center.
     *
     * @param color the color of the text
     */
    public void setColorTextNoCenter(int color) {
        if (mPickerUIListView != null && mPickerUIListView.getPickerUIAdapter() != null) {
            int newColor;
            try {
                newColor = getResources().getColor(color);
            } catch (Resources.NotFoundException e) {
                newColor = color;
            }
            mColorTextNoCenterListView = newColor;
            mPickerUIListView.getPickerUIAdapter().setColorTextNoCenter(newColor);
        }
    }

    /**
     * Method to slide up the panel. Panel displays with an animation, and when it starts, the item
     * of the center is
     * selected.
     */
    private void showPanelPickerUI() {
        setBackgroundPanel();
        setBackgroundLines();

        if (mPickerUIListView != null && mPickerUIListView.getPickerUIAdapter() != null) {
            mPickerUIListView.getPickerUIAdapter().handleSelectEvent(position + 2);
            mPickerUIListView.clearFocus();
            mPickerUIListView.post(new Runnable() {
                @Override
                public void run() {
                    mPickerUIListView.setSelection(position);
                    mPickerUIListView.requestFocus();
                }
            });
        }
    }

    private void setBackgroundPanel() {
        int color;
        try {
            color = getResources().getColor(backgroundColorPanel);
        } catch (Resources.NotFoundException e) {
            color = backgroundColorPanel;
        }
        mMainLayoutPickerUI.setBackgroundColor(color);
    }

    private void setBackgroundLines() {
        int color;
        try {
            color = getResources().getColor(colorLines);
        } catch (Resources.NotFoundException e) {
            color = colorLines;
        }

        //Top line
        mMainLayoutPickerUI.findViewById(R.id.picker_line_top).setBackgroundColor(color);

        //Bottom line
        mMainLayoutPickerUI.findViewById(R.id.picker_line_bottom).setBackgroundColor(color);
    }

    /**
     * Set a callback listener for the item click.
     *
     * @param listener Callback instance.
     */
    public void setOnClickItemPickerUIListener(final PickerUIItemClickListener listener) {
        this.mPickerUIListener = listener;

        mPickerUIListView.setOnClickItemPickerUIListener(
            new PickerUIListView.PickerUIItemClickListener() {
                @Override
                public void onItemClickItemPickerUI(int which, int position,
                                                    String valueResult) {

                    if (mPickerUIListener == null) {
                        throw new IllegalStateException(
                            "You must assign a valid PickerUI.PickerUIItemClickListener first!");
                    }
                    mPickerUIListener.onItemClickPickerUI(which, position, valueResult);
                }
            });
    }

    /**
     * This method sets the desired functionalities of panel to make easy.
     *
     * @param pickerUISettings Object with all functionalities to make easy.
     */
    public void setSettings(PickerUISettings pickerUISettings) {
        mPickerUISettings = pickerUISettings;
        setColorTextCenter(pickerUISettings.getColorTextCenter());
        setColorTextNoCenter(pickerUISettings.getColorTextNoCenter());
        setItems(mContext, pickerUISettings.getItems());
        setBackgroundColorPanel(pickerUISettings.getBackgroundColor());
        setLinesColor(pickerUISettings.getLinesColor());
        setItemsClickables(pickerUISettings.areItemsClickables());
    }

    /**
     * Save the state of the panel when orientation screen changed.
     */
    @Override
    public Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putParcelable("stateSettings", mPickerUISettings);
        //save everything
        bundle.putInt("statePosition", mPickerUIListView.getItemInListCenter());
        return bundle;
    }

    /**
     * Retrieve the state of the panel when orientation screen changed.
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            //load everything
            PickerUISettings pickerUISettings = bundle.getParcelable("stateSettings");
            if (pickerUISettings != null) {
                setSettings(pickerUISettings);
            }

            final int statePosition = bundle.getInt("statePosition");

            ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    slideUp(statePosition);

                    if (android.os.Build.VERSION.SDK_INT
                        >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    else {
                        //noinspection deprecation
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                }
            });

            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }

    public enum SLIDE {
        UP,
        DOWN
    }

    /**
     * Interface for a callback when the item has been clicked.
     */
    public interface PickerUIItemClickListener {

        /**
         * Callback when the item has been clicked.
         *
         * @param which       id of the element has been clicked
         * @param position    Position of the current item.
         * @param valueResult Value of text of the current item.
         */
        public void onItemClickPickerUI(int which, int position, String valueResult);
    }

}
