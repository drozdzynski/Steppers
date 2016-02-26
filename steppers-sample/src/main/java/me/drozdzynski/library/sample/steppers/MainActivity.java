package me.drozdzynski.library.sample.steppers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import me.drozdzynski.library.steppers.OnCancelAction;
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

        steppersViewConfig.setFragmentManager(getSupportFragmentManager());
        ArrayList<SteppersItem> steps = new ArrayList<>();

        int i = 0;
        while (i <= 10) {
            Fragment fragment = (i % 2 == 0 ? new BlankFragment() : new BlankSecondFragment());

            SteppersItem item = new SteppersItem();
            item.setLabel("Step nr " + i);
            item.setSubLabel("Fragment: " + fragment.getClass().getSimpleName());
            item.setFragment(fragment);

            steps.add(item);
            i++;
        }

        /*SteppersItem stepFirst = new SteppersItem();
        stepFirst.setLabel("Title of step");
        stepFirst.setSubLabel("Subtitle of step");
        stepFirst.setFragment(new BlankFragment());

        SteppersItem stepSecond = new SteppersItem();
        stepSecond.setLabel("Title of second step");
        stepSecond.setSubLabel("Subtitle of second step");
        stepSecond.setFragment(new BlankSecondFragment());

        SteppersItem stepLast = new SteppersItem();
        stepLast.setLabel("Title of last step");
        stepLast.setSubLabel("Subtitle of last step");
        stepLast.setFragment(new BlankFragment());

        steps.add(stepFirst);
        steps.add(stepSecond);
        steps.add(stepLast);*/

        SteppersView steppersView = (SteppersView) findViewById(R.id.steppersView);
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
