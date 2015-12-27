package me.drozdzynski.library.steppers;

import android.support.v4.app.Fragment;
import android.view.View;

public class SteppersItem {

    private String label;
    private String subLabel;
    private Fragment fragment;

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
}
