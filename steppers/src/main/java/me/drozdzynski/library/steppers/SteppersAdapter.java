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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class SteppersAdapter extends RecyclerView.Adapter<SteppersViewHolder> {
    private static final int VIEW_COLLAPSED = 0;
    private static final int VIEW_EXPANDED = 1;

    private static final String TAG = "SteppersAdapter";
    private SteppersView steppersView;
    private Context context;
    private SteppersView.Config config;
    private List<SteppersItem> items;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Map<Integer, Integer> frameLayoutIds = new HashMap<>();

    private int beforeStep = -1;
    private int currentStep = 0;

    public SteppersAdapter(SteppersView steppersView, SteppersView.Config config, List<SteppersItem> items) {
        this.steppersView = steppersView;
        this.context = steppersView.getContext();
        this.config = config;
        this.items = items;
        this.fragmentManager = config.getFragmentManager();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == currentStep ? VIEW_EXPANDED : VIEW_COLLAPSED);
    }

    @Override
    public SteppersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (viewType == VIEW_COLLAPSED) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.steppers_item, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.steppers_item_expanded, parent, false);
        }

        SteppersViewHolder vh = new SteppersViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final SteppersViewHolder holder, int p) {
        final int position = holder.getAdapterPosition();
        final SteppersItem steppersItem = items.get(position);

        holder.setChecked(position < currentStep);
        if(holder.isChecked()) {
            holder.roundedView.setChecked(true);
        } else {
            holder.roundedView.setChecked(false);
            holder.roundedView.setText(position + 1 + "");
        }

        if(position == currentStep || holder.isChecked()) holder.roundedView.setCircleAccentColor();
        else holder.roundedView.setCircleGrayColor();

        holder.textViewLabel.setText(steppersItem.getLabel());
        holder.textViewSubLabel.setText(steppersItem.getSubLabel());

        holder.linearLayoutContent.setVisibility(position == currentStep || position == beforeStep ? View.VISIBLE : View.GONE);

        holder.buttonContinue.setEnabled(steppersItem.isPositiveButtonEnable());
        steppersItem.addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                if(observable != null) {
                    SteppersItem item = (SteppersItem) observable;
                    holder.buttonContinue.setEnabled(item.isPositiveButtonEnable());
                }
            }
        });

        if (position == getItemCount() - 1) holder.buttonContinue.setText(context.getResources().getString(R.string.step_finish));
        else holder.buttonContinue.setText(context.getResources().getString(R.string.step_continue));

        holder.buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == getItemCount() - 1) config.getOnFinishAction().onFinish();
                else {
                    if(steppersItem.getOnClickContinue() != null) {
                        steppersItem.getOnClickContinue().onClick();
                    } else {
                        nextStep();
                    }
                }
            }
        });

        if (steppersItem.isSkippable() && position < getItemCount() - 1) {
            holder.buttonSkip.setVisibility(View.VISIBLE);
            holder.buttonSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (steppersItem.getOnSkipStepAction() != null) {
                        steppersItem.getOnSkipStepAction().onSkipStep();
                    }

                    nextStep();
                }
            });
        } else {
            holder.buttonSkip.setVisibility(View.GONE);
        }

        if(config.isCancelAvailable()) {
            if (config.getOnCancelAction() != null)
                holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        config.getOnCancelAction().onCancel();
                    }
                });
        } else {
            holder.buttonCancel.setVisibility(View.GONE);
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.frame_layout, holder.frameLayout, true);

        if(frameLayoutIds.get(position) == null) frameLayoutIds.put(position, findUnusedId(holder.itemView));

        //frameLayout.setId(frameLayoutIds.get(position));

        if(config.getFragmentManager() != null && steppersItem.getFragment() != null) {
            holder.frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
            holder.frameLayout.setTag(frameLayoutName());

            if (fragmentTransaction == null) {
                fragmentTransaction = fragmentManager.beginTransaction();
            }

            String name = makeFragmentName(steppersView.getId(), position);
            Fragment fragment = fragmentManager.findFragmentByTag(name);

            if(position < beforeStep) {
                if (fragment != null) {
                    if(BuildConfig.DEBUG) Log.v(TAG, "Removing item #" + position + ": f=" + fragment);
                    fragmentTransaction.detach(fragment);
                }
            } else if(position == beforeStep || position == currentStep) {
                if (fragment != null) {
                    if(BuildConfig.DEBUG) Log.v(TAG, "Attaching item #" + position + ": f=" + fragment + " d=" + fragment.isDetached());
                    fragmentTransaction.attach(fragment);
                } else {
                    fragment = steppersItem.getFragment();
                    if(BuildConfig.DEBUG) Log.v(TAG, "Adding item #" + position + ": f=" + fragment + " n=" + name);
                    fragmentTransaction.add(steppersView.getId(), fragment,
                            name);

                }
            }

            if (fragmentTransaction != null) {
                fragmentTransaction.commitAllowingStateLoss();
                fragmentTransaction = null;
                fragmentManager.executePendingTransactions();
            }

            if(fragmentManager.findFragmentByTag(name) != null &&
                    fragmentManager.findFragmentByTag(name).getView() != null) {

                View fragmentView = fragmentManager.findFragmentByTag(name).getView();

                if(fragmentView.getParent() != null && frameLayoutName() != ((View) fragmentView.getParent()).getTag()) {
                    steppersView.removeViewInLayout(fragmentView);

                    holder.frameLayout.removeAllViews();
                    holder.frameLayout.addView(fragmentView);
                }
            }
        }

        if(beforeStep == position) {
            AnimationUtils.hide(holder.linearLayoutContent);
        }
        if(currentStep == position && !steppersItem.isDisplayed()) {
            steppersItem.setDisplayed(true);
        }
    }

    protected void nextStep() {
        changeToStep(currentStep + 1);
    }

    protected void changeToStep(int position) {
        if(position != currentStep) {
            this.beforeStep = currentStep;
            this.currentStep = position;
            if(beforeStep < currentStep)
                notifyItemRangeChanged(beforeStep, currentStep);
            else
                notifyItemRangeChanged(currentStep, beforeStep);

            if(config.getOnChangeStepAction() != null) {
                SteppersItem steppersItem = items.get(this.currentStep);
                config.getOnChangeStepAction().onChangeStep(this.currentStep, steppersItem);
            }
        } else {
            if(BuildConfig.DEBUG) Log.i(TAG, "This step is currently active");
        }
    }

    protected void setItems(List<SteppersItem> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int fID = 87352142;

    public int findUnusedId(View view) {
        while( view.findViewById(++fID) != null );
        return fID;
    }

    private static String frameLayoutName() {
        return "android:steppers:framelayout";
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:steppers:" + viewId + ":" + id;
    }
}
