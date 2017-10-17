package me.drozdzynski.library.steppers.interfaces;

import me.drozdzynski.library.steppers.SteppersItem;

public interface OnChangeStepAction {

    public void onChangeStep(int position, SteppersItem activeStep);

}
