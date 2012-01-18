/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.android.inputmethod.keyboard.internal;

import android.test.AndroidTestCase;

public abstract class KeyboardStateTestsBase extends AndroidTestCase
        implements MockKeyboardSwitcher.Constants {
    protected MockKeyboardSwitcher mSwitcher;

    public abstract boolean hasDistinctMultitouch();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mSwitcher = new MockKeyboardSwitcher();
        mSwitcher.setAutoCapsMode(NO_AUTO_CAPS);

        final String layoutSwitchBackSymbols = "";
        loadKeyboard(layoutSwitchBackSymbols, ALPHABET_UNSHIFTED);
    }

    public void setAutoCapsMode(boolean autoCaps) {
        mSwitcher.setAutoCapsMode(autoCaps);
    }

    public void updateShiftState(int afterUpdate) {
        mSwitcher.updateShiftState();
        assertEquals(afterUpdate, mSwitcher.getLayoutId());
    }

    public void loadKeyboard(String layoutSwitchBackSymbols, int afterLoad) {
        mSwitcher.loadKeyboard(layoutSwitchBackSymbols, hasDistinctMultitouch());
        updateShiftState(afterLoad);
    }

    public void pressKey(int code, int afterPress) {
        mSwitcher.onPressKey(code);
        assertEquals(afterPress, mSwitcher.getLayoutId());
    }

    public void releaseKey(int code, int afterRelease) {
        mSwitcher.onCodeInput(code, SINGLE);
        mSwitcher.onReleaseKey(code, NOT_SLIDING);
        assertEquals(afterRelease, mSwitcher.getLayoutId());
    }

    public void pressAndReleaseKey(int code, int afterPress, int afterRelease) {
        pressKey(code, afterPress);
        releaseKey(code, afterRelease);
    }

    public void chordingPressKey(int code, int afterPress) {
        pressKey(code, afterPress);
    }

    public void chordingReleaseKey(int code, int afterRelease) {
        mSwitcher.onCodeInput(code, MULTI);
        mSwitcher.onReleaseKey(code, NOT_SLIDING);
        assertEquals(afterRelease, mSwitcher.getLayoutId());
    }

    public void chordingPressAndReleaseKey(int code, int afterPress, int afterRelease) {
        chordingPressKey(code, afterPress);
        chordingReleaseKey(code, afterRelease);
    }

    public void pressAndSlideFromKey(int code, int afterPress, int afterSlide) {
        pressKey(code, afterPress);
        mSwitcher.onReleaseKey(code, SLIDING);
        assertEquals(afterSlide, mSwitcher.getLayoutId());
    }

    public void longPressShiftKey(int afterPress, int afterLongPress) {
        // Long press shift key
        mSwitcher.onPressKey(CODE_SHIFT);
        assertEquals(afterPress, mSwitcher.getLayoutId());
        // Long press recognized in LatinKeyboardView.KeyTimerHandler.
        mSwitcher.onCodeInput(CODE_CAPSLOCK, SINGLE);
        assertEquals(afterLongPress, mSwitcher.getLayoutId());
        mSwitcher.onReleaseKey(CODE_SHIFT, NOT_SLIDING);
        assertEquals(afterLongPress, mSwitcher.getLayoutId());
    }

    public void secondTapShiftKey(int afterTap) {
        mSwitcher.onCodeInput(CODE_CAPSLOCK, SINGLE);
        assertEquals(afterTap, mSwitcher.getLayoutId());
    }
}