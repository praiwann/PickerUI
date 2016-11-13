package com.dpizarro.uipicker.library.picker;

import android.os.Parcel;
import android.os.Parcelable;

import com.dpizarro.uipicker.library.R;

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
public class PickerUISettings implements Parcelable {

    public static final Parcelable.Creator<PickerUISettings> CREATOR
            = new Parcelable.Creator<PickerUISettings>() {
        public PickerUISettings createFromParcel(Parcel source) {
            return new PickerUISettings(source);
        }

        public PickerUISettings[] newArray(int size) {
            return new PickerUISettings[size];
        }
    };

    /**
     * Default behaviour of items
     */
    public static boolean DEFAULT_ITEMS_CLICKABLES = true;
    private List<String> mItems;
    private int mColorTextCenter;
    private int mColorTextNoCenter;
    private int mBackgroundColor;
    private int mLinesColor;
    private boolean mItemsClickables;

    private PickerUISettings(Builder builder) {
        setItems(builder.mItems);
        setColorTextCenter(builder.mColorTextCenter);
        setColorTextNoCenter(builder.mColorTextNoCenter);
        setBackgroundColor(builder.mBackgroundColor);
        setLinesColor(builder.mLinesColor);
        setItemsClickables(builder.mItemsClickables);
    }

    private PickerUISettings(Parcel in) {
        in.readStringList(this.mItems);
        this.mColorTextCenter = in.readInt();
        this.mColorTextNoCenter = in.readInt();
        this.mBackgroundColor = in.readInt();
        this.mLinesColor = in.readInt();
        this.mItemsClickables = in.readByte() != 0;
    }

    public List<String> getItems() {
        return mItems;
    }

    void setItems(List<String> items) {
        mItems = items;
    }

    public int getColorTextCenter() {
        return mColorTextCenter;
    }

    void setColorTextCenter(int colorTextCenter) {
        mColorTextCenter = colorTextCenter;
    }

    public int getColorTextNoCenter() {
        return mColorTextNoCenter;
    }

    void setColorTextNoCenter(int colorTextNoCenter) {
        mColorTextNoCenter = colorTextNoCenter;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public int getLinesColor() {
        return mLinesColor;
    }

    void setLinesColor(int linesColor) {
        mLinesColor = linesColor;
    }

    public boolean areItemsClickables() {
        return mItemsClickables;
    }

    void setItemsClickables(boolean itemsClickables) {
        mItemsClickables = itemsClickables;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.mItems);
        dest.writeInt(this.mColorTextCenter);
        dest.writeInt(this.mColorTextNoCenter);
        dest.writeInt(this.mBackgroundColor);
        dest.writeInt(this.mLinesColor);
        dest.writeByte(mItemsClickables ? (byte) 1 : (byte) 0);
    }

    public static final class Builder {

        private List<String> mItems;
        private int mColorTextCenter            = R.color.text_center_pickerui;
        private int mColorTextNoCenter          = R.color.text_no_center_pickerui;
        private int mBackgroundColor            = R.color.background_panel_pickerui;
        private int mLinesColor                 = R.color.lines_panel_pickerui;
        private boolean mItemsClickables        = DEFAULT_ITEMS_CLICKABLES;

        public Builder() {
        }

        private Builder(Builder builder) {}

        public Builder withItems(List<String> mItems) {
            this.mItems = mItems;
            return this;
        }

        public Builder withColorTextCenter(int mColorTextCenter) {
            this.mColorTextCenter = mColorTextCenter;
            return this;
        }

        public Builder withColorTextNoCenter(int mColorTextNoCenter) {
            this.mColorTextNoCenter = mColorTextNoCenter;
            return this;
        }

        public Builder withBackgroundColor(int mBackgroundColor) {
            this.mBackgroundColor = mBackgroundColor;
            return this;
        }

        public Builder withLinesColor(int mLinesColor) {
            this.mLinesColor = mLinesColor;
            return this;
        }

        public Builder withItemsClickables(boolean mItemsClickables) {
            this.mItemsClickables = mItemsClickables;
            return this;
        }

        public PickerUISettings build() {
            return new PickerUISettings(this);
        }

    }
}
