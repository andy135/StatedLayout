# StatedLayout

[![](https://jitpack.io/v/andy135/StatedLayout.svg)](https://jitpack.io/#andy135/StatedLayout)

## Description
StatedLayout is a simple library for Android to easily manage states (**Loading**, **Error**, **Empty** and **Content**) when obtaining remote data.

##Screenshots
<img src="https://github.com/andy135/StatedLayout/blob/master/screenshots/Loading.png" width="400"/>
<img src="https://github.com/andy135/StatedLayout/blob/master/screenshots/Error.png" width="400"/>
<img src="https://github.com/andy135/StatedLayout/blob/master/screenshots/Empty.png" width="400"/>
<img src="https://github.com/andy135/StatedLayout/blob/master/screenshots/Content.png" width="400"/>

## Usage

###1: Add the dependency:

Add this lines your root gradle project:

```gradle
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

Include the following dependency in your *app* gradle module:

```gradle
	compile 'com.github.andy135:StatedLayout:1.0.0'
```

###2: Add the view to your layout xml file:

```xml
    <com.andiag.statedlayout.StatedLayout
        android:id="@+id/statedLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/buttons"
        andiag:imageSize="100dp">

        <include layout="@layout/content" />

    </com.andiag.statedlayout.StatedLayout>
```

The StatedLayout can have only one directly child, the content layout.

###3: The activity or fragment should implement [OnRetryListener](statedlayout/src/main/java/com/andiag/statedlayout/OnRetryListener.java):

```java
public class ActivityMain extends AppCompatActivity implements OnRetryListener {

    StatedLayout statedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	
        statedLayout = (StatedLayout) findViewById(R.id.statedLayout);
        statedLayout.setOnRetryListener(this);
    }
}
```

###4: You can also add a [OnStateChangeListener](statedlayout/src/main/java/com/andiag/statedlayout/OnStateChangeListener.java):
```java
	statedLayout.setOnStateChangeListener(new OnStateChangeListener() {
	    @Override
	    public void onLoading() {

	    }

	    @Override
	    public void onError() {
		Toast.makeText(ActivityMain.this, "Error loading data!", Toast.LENGTH_SHORT).show();
	    }

	    @Override
	    public void onContent() {

	    }

	    @Override
	    public void onEmpty() {

	    }
	});
```

See more in the example app code.

## Usable XML attributes
| Name | Type | Default | Description |
|:----:|:----:|:-------:|:-----------:|
|tint|color|colorAccent|Image tint color|
|textSize|dimension|18dp|Text size|
|textColor|color|textColorSecondary|Text color|
|andiag:imageSize|dimension|150dp|The image size|
|andiag:alternateIcons|boolean|false|Use the secondary icons|
|andiag:emptyLabel|reference|R.string.default_empty|Empty state label|
|andiag:loadingLabel|reference|R.string.default_loading|Loading state label|
|andiag:errorLabel|reference|R.string.default_error|Error state label|
|andiag:emptyImage|reference|R.drawable.stated_empty|Empty image drawable|
|andiag:loadingImage|reference|R.drawable.stated_loading|Loading image drawable|
|andiag:errorImage|reference| R.drawable.stated_error|Error image drawable|


## Maintained By
[IagoCanalejas](https://github.com/iagocanalejas) ([@iagocanalejas](https://twitter.com/Iagocanalejas))

[Andy](https://github.com/andy135) ([@ANDYear21](https://twitter.com/ANDYear21))


  LICENSE
============
  
	  Copyright 2016 AndIag

	  Licensed under the Apache License, Version 2.0 (the "License");
	  you may not use this file except in compliance with the License.
	  You may obtain a copy of the License at

	      http://www.apache.org/licenses/LICENSE-2.0

	  Unless required by applicable law or agreed to in writing, software
	  distributed under the License is distributed on an "AS IS" BASIS,
	  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	  See the License for the specific language governing permissions and
	  limitations under the License.
