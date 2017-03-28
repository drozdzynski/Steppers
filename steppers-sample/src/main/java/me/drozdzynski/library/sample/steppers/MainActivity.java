package me.drozdzynski.library.sample.steppers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import me.drozdzynski.library.steppers.OnCancelAction;
import me.drozdzynski.library.steppers.OnChangeStepAction;
import me.drozdzynski.library.steppers.OnFinishAction;
import me.drozdzynski.library.steppers.SteppersItem;
import me.drozdzynski.library.steppers.SteppersView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SteppersView.Config steppersViewConfig = new SteppersView.Config();
        steppersViewConfig.setOnFinishAction(new OnFinishAction() {
            @Override
            public void onFinish() {
                MainActivity.this.startActivity(new Intent(MainActivity.this, MainActivity.class));
                MainActivity.this.finish();
            }
        });

        steppersViewConfig.setOnCancelAction(new OnCancelAction() {
            @Override
            public void onCancel() {
                MainActivity.this.startActivity(new Intent(MainActivity.this, MainActivity.class));
                MainActivity.this.finish();
            }
        });

        steppersViewConfig.setOnChangeStepAction(new OnChangeStepAction() {
            @Override
            public void onChangeStep(int position, SteppersItem activeStep) {
                Toast.makeText(MainActivity.this, "Step changed to: " + activeStep.getLabel() + " (" + position + ")",
                        Toast.LENGTH_SHORT).show();
            }
        });

        steppersViewConfig.setFragmentManager(getSupportFragmentManager());
        steppersViewConfig.setUseCustomColors(true);
        steppersViewConfig.setStepCheckedColor(Color.parseColor("#0DB151"));
        steppersViewConfig.setStepUncheckedColor(Color.DKGRAY);
        steppersViewConfig.setIsButtonsEnabled(false);
        ArrayList<SteppersItem> steps = new ArrayList<>();
        final SteppersView steppersView = (SteppersView) findViewById(R.id.steppersView);

        int i = 0;
        while (i <= 5) {

            final SteppersItem item = new SteppersItem();
            item.setLabel("Step nr " + i);

            final BlankFragment blankFragment = new BlankFragment();
            blankFragment.setPosition(i);
            blankFragment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        steppersView.OnStepDone(blankFragment.getPosition());
                   }
                });
            item.setSubLabel("Fragment: " + blankFragment.getClass().getSimpleName());
            item.setFragment(blankFragment);

            steps.add(item);
            i++;
        }

        steppersView.setConfig(steppersViewConfig);
        steppersView.setItems(steps);
        steppersView.build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
