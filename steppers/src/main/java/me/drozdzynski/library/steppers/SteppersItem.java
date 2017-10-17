/*
 * Copyright (C) 2015 Krystian Drożdżyński
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

package me.drozdzynski.library.steppers;

import android.support.v4.app.Fragment;

import java.util.Observable;

import me.drozdzynski.library.steppers.interfaces.OnClickContinue;
import me.drozdzynski.library.steppers.interfaces.OnSkipStepAction;

public class SteppersItem extends Observable {

    private String label;
    private String subLabel;
    private boolean buttonEnable = true;
    private Fragment fragment;
    private OnClickContinue onClickContinue;
    private boolean skippable = false;
    private OnSkipStepAction onSkipStepAction;

    private boolean displayed = false;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSubLabel() {
        return subLabel;
    }

    public void setSubLabel(String subLabel) {
        this.subLabel = subLabel;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public boolean isPositiveButtonEnable() {
        return buttonEnable;
    }

    public void setPositiveButtonEnable(boolean buttonEnable) {
        synchronized (this) {
            this.buttonEnable = buttonEnable;
        }
        setChanged();
        notifyObservers();
    }

    public OnClickContinue getOnClickContinue() {
        return onClickContinue;
    }

    public void setOnClickContinue(OnClickContinue onClickContinue) {
        this.onClickContinue = onClickContinue;
    }

    public boolean isSkippable() {
        return skippable;
    }

    public OnSkipStepAction getOnSkipStepAction() {
        return onSkipStepAction;
    }

    public void setSkippable(boolean skippable) {
        this.skippable = skippable;
    }

    public void setSkippable(boolean skippable, OnSkipStepAction onSkipStepAction) {
        this.skippable = skippable;
        this.onSkipStepAction = onSkipStepAction;
    }

    protected synchronized boolean isDisplayed() {
        return displayed;
    }

    protected void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }
}
