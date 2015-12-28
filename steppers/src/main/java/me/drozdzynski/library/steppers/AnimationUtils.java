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
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(500);

        Animation collapse = new ExpandCollapse(view, view.getHeight(), 0);
        collapse.setInterpolator(new AccelerateInterpolator());
        collapse.setStartOffset(500);
        collapse.setDuration(500);

        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeOut);
        animation.addAnimation(collapse);

        view.startAnimation(animation);
    }

    protected static void show(final View view){
/*        final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if(viewTreeObserver != null)
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    float targetHeight = view.getMeasuredHeight();

                    Animation fadeIn = new AlphaAnimation(0, 1);
                    fadeIn.setInterpolator(new AccelerateInterpolator());
                    fadeIn.setStartOffset(500);
                    fadeIn.setDuration(500);

                    Animation collapse = new ExpandCollapse(view, 0, targetHeight * 2);
                    collapse.setInterpolator(new AccelerateInterpolator());
                    collapse.setDuration(500);

                    AnimationSet animation = new AnimationSet(false);
                    animation.addAnimation(collapse);
                    animation.addAnimation(fadeIn);

                    view.startAnimation(animation);

                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });*/

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setStartOffset(500);
        fadeIn.setDuration(500);

        Animation collapse = new ScaleAnimation(1, 1, 0, 1);
        collapse.setInterpolator(new AccelerateInterpolator());
        collapse.setDuration(500);

        AnimationSet animation = new AnimationSet(false);
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
