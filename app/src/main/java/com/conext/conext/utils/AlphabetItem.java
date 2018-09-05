package com.conext.conext.utils;

/**
 * Created by Ashith VL on 6/21/2017.
 */

public class AlphabetItem {

    public int position;
    public String word;
    public boolean isActive;

    public AlphabetItem(int pos, String word, boolean isActive) {
        this.position = pos;
        this.word = word;
        this.isActive = isActive;
    }
}