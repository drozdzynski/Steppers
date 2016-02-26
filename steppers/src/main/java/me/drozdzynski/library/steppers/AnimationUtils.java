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

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;

public class AnimationUtils {

    protected static void hide(final View view){
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(500);

        Animation collapse = new ExpandCollapse(view, view.getHeight(), 0);
        collapse.setStartOffset(500);
        collapse.setDuration(500);

        AnimationSet animation = new AnimationSet(false);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.addAnimation(fadeOut);
        animation.addAnimation(collapse);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animation);
    }

    protected static void show(final View view){
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setStartOffset(500);
        fadeIn.setDuration(500);

        Animation collapse = new ScaleAnimation(1, 1, 0, 1);
        collapse.setDuration(500);

        AnimationSet animation = new AnimationSet(false);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.addAnimation(collapse);
        animation.addAnimation(fadeIn);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animation);
    }

    protected static class ExpandCollapse extends Animation {
        private View mView;
        private final int mStartHeight;
        private final int mDeltaHeight;

        public ExpandCollapse(View view, int startHeight, int endHeight) {
            mView = view;
            mStartHeight = startHeight;
            mDeltaHeight = endHeight - startHeight;
            Log.d("Expand", startHeight + ", " + endHeight + " : " + mDeltaHeight);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            android.view.ViewGroup.LayoutParams lp = mView.getLayoutParams();
            lp.height = (int) (mStartHeight + mDeltaHeight * interpolatedTime);
            mView.setLayoutParams(lp);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

}
