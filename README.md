<img src="https://api.travis-ci.org/drozdzynski/Steppers.svg" /> <a href="https://android-arsenal.com/details/1/3301"><img src="https://img.shields.io/badge/Android%20Arsenal-Steppers-green.svg?style=true"></a>
<a href="https://www.patreon.com/drozdzynski" rel="Patreon"><img src="https://img.shields.io/badge/donate-patreon-%23E6461A.svg" /></a>
<a href="https://gitter.im/drozdzynski/Steppers" rel="Gitter"><img src="https://img.shields.io/gitter/room/nwjs/nw.js.svg" /></a>
<a href="https://twitter.com/drozdzynskime" rel="some text"><img src="https://img.shields.io/twitter/follow/drozdzynskime.svg?style=social&label=Follow" /></a>

# Steppers

## Screen

<img src="https://drozdzynski.me/repo/steppers/screen.gif" />

## Setup

### 1. Add library to project

#### Grab via Gradle:
```groovy
dependencies {
    compile 'me.drozdzynski.library.steppers:steppers:1.0.0'
}
```

#### Manual
* Download the library folder.
* Copy to root project folder
* Add to your settings.gradle file the following code line: "include ':app', ':steppers'"
* Rebuild the project
* Add dependency
    * File → Project Structure
    * in Modules section click on "app"
    * Click on tab "Dependecies"
    * Click on the green plus
    * Module Dependecy
    * Select ":library"
* Done

### 2. Add view in XML Layout
```xml
<me.drozdzynski.library.steppers.SteppersView
    android:id="@+id/steppersView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

### 3. Setup config for SteppersView
```java
SteppersView.Config steppersViewConfig = new SteppersView.Config();
steppersViewConfig.setOnFinishAction(new OnFinishAction() {
    @Override
    public void onFinish() {
        // Action on last step Finish button
    }
});

steppersViewConfig.setOnCancelAction(new OnCancelAction() {
    @Override
    public void onCancel() {
        // Action when click cancel on one of steps
    }
});

steppersViewConfig.setOnChangeStepAction(new OnChangeStepAction() {
    @Override
    public void onChangeStep(int position, SteppersItem activeStep) {
        // Action when click continue on each step
    }
});

// Setup Support Fragment Manager for fragments in steps
steppersViewConfig.setFragmentManager(getSupportFragmentManager());
```

### 4. Create steps list
```java
ArrayList<SteppersItem> steps = new ArrayList<>();

SteppersItem stepFirst = new SteppersItem();

stepFirst.setLabel("Title of step");
stepFirst.setSubLabel("Subtitle of step");
stepFirst.setFragment(new SomeFragment());
stepFirst.setPositiveButtonEnable(false);

steps.add(stepFirst);
```

### 5. Set config, list and build view;
```java
SteppersView steppersView = (SteppersView) findViewById(R.id.steppersView);
steppersView.setConfig(steppersViewConfig);
steppersView.setItems(steps);
steppersView.build();
```

## Other functions

### Enable skip step button
Simple:
```java
item.setSkippable(true);
```

With callback:
```java
item.setSkippable(true, new OnSkipStepAction() {
    @Override
    public void onSkipStep() {
        // Some action after step is skipped
    }
});
```

### Override continue button
```java
item.setOnClickContinue(new OnClickContinue() {
    @Override
    public void onClick() {
        // Some action on click
        
        steppersView.nextStep(); // Now You must call next step
    }
});
```

### Change active step
```java
steppersView.setActiveItem(1); // step index from 0 
```

### Disable cancel button (hide)
```java
steppersViewConfig.setCancelAvailable(false);
```

## License
```
Copyright (C) 2015-2017 Krystian Drożdżyński

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
