package me.drozdzynski.library.steppers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SteppersViewHolder extends RecyclerView.ViewHolder {

    private boolean isExpanded;

    protected View itemView;
    protected RoundedView roundedView;
    protected TextView textViewLabel;
    protected TextView textViewSubLabel;
    protected LinearLayout linearLayoutContent;
    protected FrameLayout frameLayout;
    protected LinearLayout frameLayoutsContainer;
    protected Button buttonContinue;
    protected Button buttonCancel;

    public SteppersViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.roundedView = (RoundedView) itemView.findViewById(R.id.roundedView);
        this.textViewLabel = (TextView) itemView.findViewById(R.id.textViewLabel);
        this.textViewSubLabel = (TextView) itemView.findViewById(R.id.textViewSubLabel);
        this.linearLayoutContent = (LinearLayout) itemView.findViewById(R.id.linearLayoutContent);
        this.frameLayout = (FrameLayout) itemView.findViewById(R.id.frameLayout);
        //this.frameLayoutsContainer = (LinearLayout) itemView.findViewById(R.id.frameLayoutsContainer);
        this.buttonContinue = (Button) itemView.findViewById(R.id.buttonContinue);
        this.buttonCancel = (Button) itemView.findViewById(R.id.buttonCancel);
    }

    /**
     * @return true if expanded, false if not
     */
    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
