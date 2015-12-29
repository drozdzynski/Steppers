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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SteppersAdapter extends RecyclerView.Adapter<SteppersViewHolder> {

    private Context context;
    private SteppersView.Config config;
    private List<SteppersItem> items;
    private FragmentManager fragmentManager;

    private Map<Integer, Integer> frameLayoutIds = new HashMap<>();

    private int VIEW_COLLAPSED = 0;
    private int VIEW_EXPANDED = 1;

    protected SteppersAdapter(Context context, SteppersView.Config config, List<SteppersItem> items) {
        this.context = context;
        this.config = config;
        this.items = items;
        this.fragmentManager = config.getFragmentManager();
    }

    private int beforeStep = -1;
    private int currentStep = 0;

    private boolean possitiveButtonEnable = true;

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
    public void onBindViewHolder(final SteppersViewHolder holder, final int position) {
        holder.setExpanded(position == currentStep);
        holder.setChecked(position < currentStep);
        final SteppersItem steppersItem = items.get(position);

        if(holder.isChecked()) {
            holder.roundedView.setChecked(true);
        } else {
            holder.roundedView.setChecked(false);
            holder.roundedView.setText(position + 1 + "");
        }

        if(holder.isExpanded() || holder.isChecked()) holder.roundedView.setCircleAccentColor();
        else holder.roundedView.setCircleGrayColor();

        holder.textViewLabel.setText(steppersItem.getLabel());
        holder.textViewSubLabel.setText(steppersItem.getSubLabel());

        if (position != currentStep && position != beforeStep)
            holder.linearLayoutContent.setVisibility(holder.isExpanded() || position == beforeStep ? View.VISIBLE : View.GONE);

        holder.buttonContinue.setEnabled(possitiveButtonEnable);

        if (position == getItemCount() - 1) holder.buttonContinue.setText(context.getResources().getString(R.string.step_finish));
        else holder.buttonContinue.setText(context.getResources().getString(R.string.step_continue));

        holder.buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == getItemCount() - 1) config.getOnFinishAction().onFinish();
                else nextStep();
            }
        });

        if(config.getOnCancelAction() != null)
            holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    config.getOnCancelAction().onCancel();
                }
            });

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.frame_layout, holder.frameLayout, true);

        if(frameLayoutIds.get(position) == null) frameLayoutIds.put(position, findUnusedId(holder.itemView));

        frameLayout.setId(frameLayoutIds.get(position));

        if(config.getFragmentManager() != null && steppersItem.getFragment() != null && holder.isExpanded()) {
            holder.frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));

            if(fragmentManager.getFragments() != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                List<Fragment> fragments = fragmentManager.getFragments();
                for(Fragment fragment : fragments) {
                    if(fragments.size() > 1)
                        fragmentTransaction.detach(fragment);
                }
                fragmentTransaction.commit();
            }

            fragmentManager.beginTransaction()
                    .replace(frameLayoutIds.get(position), steppersItem.getFragment()).commit();
        }

        if(beforeStep == position) {
            AnimationUtils.hide(holder.linearLayoutContent);
        }
        if(currentStep == position && position != 0) {
            AnimationUtils.show(holder.linearLayoutContent);
        }
    }

    private void nextStep() {
        this.beforeStep = currentStep;
        this.currentStep = this.currentStep + 1;
        //notifyDataSetChanged();
        notifyItemChanged(beforeStep);
        notifyItemChanged(currentStep);
    }

    protected void setPossitiveButtonEnable(boolean enable){
        possitiveButtonEnable = enable;
        notifyDataSetChanged();
    }

    protected void setItems(List<SteppersItem> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    int fID = 190980;

    public int findUnusedId(View view) {
        while( view.getRootView().findViewById(++fID) != null );
        return fID;
    }
}
