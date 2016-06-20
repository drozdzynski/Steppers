![alt tag](https://api.travis-ci.org/drozdzynski/Steppers.svg)
<a href="https://twitter.com/drozdzynskime" rel="some text">![alt text][twitter]</a>
<a href="https://android-arsenal.com/details/1/3301" rel="Android Arsenal">![alt text][androidarsenal]</a>
[twitter]: https://img.shields.io/twitter/follow/drozdzynskime.svg?style=social&label=Follow "Twitter @drozdzynskime"
[androidarsenal]: https://img.shields.io/badge/Android%20Arsenal-Steppers-green.svg?style=true "Android Arsenal"

# Steppers

##Screen
![alt tag](https://drozdzynski.me/repo/steppers/screen.gif)

##Setup

###1. Add library to project

#### Grab via Gradle:
```groovy
repositories {
    mavenCentral()
    maven {
        url "https://oss.sonatype.org/content/repositories/snapshots/"
    }
}

dependencies {
    compile 'me.drozdzynski.library.steppers:steppers:0.3.3-SNAPSHOT'
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

###2. Add view in XML Layout
```xml
<me.drozdzynski.library.steppers.SteppersView
    android:id="@+id/steppersView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

###3. Setup config for SteppersView
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

// Setup Support Fragment Manager for fragments in steps
steppersViewConfig.setFragmentManager(getSupportFragmentManager());
```

###4. Create steps list
```java
ArrayList<SteppersItem> steps = new ArrayList<>();

SteppersItem stepFirst = new SteppersItem();

stepFirst.setLabel("Title of step");
stepFirst.setSubLabel("Subtitle of step");
stepFirst.setFragment(new SomeFragment());
stepFirst.setPositiveButtonEnable(false);

steps.add(stepFirst);
```

###5. Set config, list and build view;
```java
SteppersView steppersView = (SteppersView) findViewById(R.id.steppersView);
steppersView.setConfig(steppersViewConfig);
steppersView.setItems(steps);
steppersView.build();
```

## License
```
Copyright (C) 2015 Krystian Drożdżyński

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
