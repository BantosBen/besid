# BESID (BEautiful and SImple Dialogs)
[![](https://jitpack.io/v/BantosBen/besid.svg)](https://jitpack.io/#BantosBen/besid)

The library available in jitpack repository. You can get it using the following steps

Step 1: Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2: Add the dependency
``` 
dependencies {
  implementation 'com.github.BantosBen:besid:1.0.2'
}
```
**Note:** The library requires minimum API level 22.

[BESIDWatchDogDialog](https://github.com/BantosBen/besid/blob/main/besid/src/main/java/com/sanj/besid/watchDog/BESIDWatchDogDialog.java) class is an inheritor of a AlertDialog class. You can use it just like simple [AlertDialog](http://developer.android.com/reference/android/app/AlertDialog.html). For example:
```java
AlertDialog dialog = new BESIDWatchDogDialog.build(context).build();
dialog.show();
...
dialog.dismiss();
```
### Customization

Use android styles to customize the dialog.
Next custom attributes provided:
* Text : string
* Message : string
* Action : listener

**For example:**
```
 AlertDialog dialog = new BESIDTextInputDialog.Builder(this)
         .setCancelable(true)
         .setMessage("Type target year")
         .setTitle("TextInput Dialog")
         .setNegativeButton("Back", AppCompatDialog::dismiss)
         .setPositiveButton("Submit", (dialog, userInput) -> {
             Toast.makeText(MainActivity.this, userInput, Toast.LENGTH_SHORT).show();
             dialog.dismiss();
         })
         .setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
         .build();
dialog.show();
```
