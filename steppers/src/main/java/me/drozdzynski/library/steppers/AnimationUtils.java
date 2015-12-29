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

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;

public class AnimationUtils {

    protected static void hide(View view){
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(500);

        Animation collapse = new ExpandCollapse(view, view.getHeight(), 0);
        collapse.setStartOffset(500);
        collapse.setDuration(500);

        AnimationSet animation = new AnimationSet(false);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.addAnimation(fadeOut);
        animation.addAnimation(collapse);

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

        view.startAnimation(animation);
    }

    protected static class ExpandCollapse extends Animation {
        private View mView;
        private float mToHeight;
        private float mFromHeight;

        public ExpandCollapse(View v, float fromHeight, float toHeight) {
            mToHeight = toHeight;
            mFromHeight = fromHeight;
            mView = v;
            setDuration(300);
        }
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float height =
                    (mToHeight - mFromHeight) * interpolatedTime + mFromHeight;
            ViewGroup.LayoutParams p = mView.getLayoutParams();
            p.height = (int) height;
            mView.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

}
