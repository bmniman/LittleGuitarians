package com.bmniman.littleguitarians.Designs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bmniman.littleguitarians.Classes.Notes;
import com.bmniman.littleguitarians.Classes.ProcessPitch;
import com.bmniman.littleguitarians.GetterSetter.Track;
import com.bmniman.littleguitarians.Interfaces.NotesInterface;
import com.bmniman.littleguitarians.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import rm.com.youtubeplayicon.PlayIconDrawable;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;


public class CustomExercise extends AppCompatActivity {

    static ProcessPitch process = new ProcessPitch(); // To Call Process Pitch Method
    final int frets = 20; // Number Of Frets
    final int NUM_ROWS = 6;
    final int NUM_COLS = frets;
    public int[] notes; // Array To store the notes to play
    HorizontalScrollView scrollView;
    RelativeLayout relativelayout; // For Holding Linear Layout, Table Layout and Strings
    LinearLayout fretlayout, fretmarkerslayout; // For Frets, Fret Markers
    TableLayout labellayout, stringlayout, exerciselayout; // For Labels , Strings , Custom Exercise Clickable Button
    ImageView fret[] = new ImageView[frets]; // For Fret Markers
    ImageView string[] = new ImageView[6]; // For Srings
    TextView labels[][] = new TextView[6][frets]; // For Labels.
    Button custombtn[][] = new Button[6][frets]; // For Custom Exercise Clickable Button
    TextView[] markers = new TextView[frets];
    SeekBar tempo; // For Keeping Track of Tempo
    int temprow = -1; // For Animationn
    int tempcol = -1; //For Animation
    Animation animation; //  For String Shake Animation
    ImageView playBtn, saveBtn;
    ProgressBar progressBar;
    Boolean closeasynctask = false;
    int tempcount = 0;
    int track = 0; // To keep track of notes being played
    StringBuilder stringBuilder = new StringBuilder();
    int rate;
    Track trackobj = new Track(); // Track getter and setter object used to track the note being played
    int size;
    int customNoteCount = 0; // For Keeping Track of number of notes selected by the user in custom exercises4
    Context c = this;
    String exerName = "";
    String editExerName = "" ; // For When Edit Exercise Is clicked And Same Name is To Be kept this name will be displayed in the dialog box while saving
    boolean exerSaved = false;


    StringBuilder exerOrder = new StringBuilder(); // For Storing The Exercise Order


    int soundFiles[][] = {

            {R.raw.e_zero,R.raw.e_two,R.raw.e_two, R.raw.e_three, R.raw.e_four, R.raw.e_five , R.raw.e_six, R.raw.e_seven,R.raw.e_eigth, R.raw.e_nine, R.raw.e_ten , R.raw.e_eleven , R.raw.e_twlve , R.raw.e_thirteen, R.raw.e_forteen, R.raw.e_fifteen, R.raw.e_sixteen, R.raw.e_seventeen, R.raw.e_eighteen, R.raw.e_nineteen, R.raw.e_twenty},
            {R.raw.b_zero,R.raw.b_one,R.raw.b_two, R.raw.b_three , R.raw.b_four , R.raw.b_five , R.raw.b_six , R.raw.b_seven , R.raw.b_eigth, R.raw.b_nine , R.raw.b_ten , R.raw.b_eleven , R.raw.b_twelve , R.raw.b_thirteen , R.raw.b_forteen , R.raw.b_fifteen , R.raw.b_sixteen , R.raw.b_seventeen , R.raw.b_eighteen , R.raw.b_ninteen , R.raw.b_twenty },
            {R.raw.g_zero,R.raw.g_one,R.raw.g_two, R.raw.g_three , R.raw.g_four , R.raw.g_five , R.raw.g_six , R.raw.g_seven , R.raw.g_eigth , R.raw.g_nine , R.raw.g_ten , R.raw.g_eleven , R.raw.g_twlve , R.raw.g_thirteen , R.raw.g_forteen , R.raw.g_fifteen , R.raw.g_sixteen , R.raw.g_seventeen , R.raw.g_eighteen , R.raw.g_ninteen , R.raw.g_twenty },
            {R.raw.d_zero,R.raw.d_one,R.raw.d_two, R.raw.d_three , R.raw.d_four , R.raw.d_five , R.raw.d_six , R.raw.d_seven , R.raw.d_eigth , R.raw.d_nine , R.raw.d_ten , R.raw.d_eleven , R.raw.d_twelve , R.raw.d_thriteen , R.raw.d_forteen , R.raw.d_fifteen , R.raw.d_sixteen , R.raw.d_seventeen , R.raw.d_eighteen , R.raw.d_ninteen , R.raw.d_twenty },
            {R.raw.a_zero,R.raw.a_one,R.raw.a_two, R.raw.a_three , R.raw.a_four , R.raw.a_five , R.raw.a_six , R.raw.a_seven , R.raw.a_eigth, R.raw.a_nine , R.raw.a_ten , R.raw.a_eleven , R.raw.a_twelve , R.raw.a_thriteen , R.raw.a_forteen , R.raw.a_fifteen , R.raw.a_sixteen , R.raw.a_seventeen , R.raw.a_eighteen , R.raw.a_ninteen , R.raw.a_twenty },
            {R.raw.last_zero,R.raw.last_one,R.raw.last_two,R.raw.last_three , R.raw.last_four , R.raw.last_five , R.raw.last_six , R.raw.last_seven , R.raw.last_eigth , R.raw.last_nine , R.raw.last_ten , R.raw.last_eleven ,R.raw.last_twevel ,R.raw.last_thirteen , R.raw.last_forteen , R.raw.last_fifteen , R.raw.last_sixteen , R.raw.last_seventeen , R.raw.last_eighteen , R.raw.last_ninteen , R.raw.last_twenty}



    };

    SoundPool soundPool;
    int soundID;
    boolean loaded = false;

    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT); // Linear Layout Parameters

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        closeAyscTask();
        Toast.makeText(getApplicationContext(), "Activity Closed", Toast.LENGTH_SHORT).show();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        closeAyscTask();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_custom_exercise);

        scrollView = findViewById(R.id.horizontalScrollView);
        relativelayout = findViewById(R.id.relativelayout);
        fretlayout = findViewById(R.id.fretlayout);
        fretmarkerslayout = findViewById(R.id.fretmarkerslayout);
        labellayout = findViewById(R.id.labelslayout);
        exerciselayout = findViewById(R.id.excerisebutton);
        stringlayout = findViewById(R.id.stringslayout);
        playBtn = findViewById(R.id.imageView);
        saveBtn = findViewById(R.id.save);
        progressBar = findViewById(R.id.progress);
        animation = AnimationUtils.loadAnimation(this, R.anim.shake);


        saveBtn.setVisibility(View.INVISIBLE);// User Should Not Be Able To CLick Save Until A Note Has Been Selected
        playBtn.setVisibility(View.INVISIBLE);  // User Should Not Be Able To CLick Play Until A Note Has Been Selected

        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        rate = Integer.parseInt(audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE));
        size = Integer.parseInt(audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER));


        final PlayIconDrawable play = PlayIconDrawable.builder()
                .withColor(Color.WHITE)
                .withInterpolator(new FastOutSlowInInterpolator())
                .withDuration(300)

                .withInitialState(PlayIconDrawable.IconState.PLAY)
                .withAnimatorListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Log.d("Animation", "animationFinished");
                    }
                })
                .withStateListener(new PlayIconDrawable.StateListener() {
                    @Override
                    public void onStateChanged(PlayIconDrawable.IconState state) {
                        Log.d("IconState", "onStateChanged: " + state);
                    }
                })
                .into(playBtn);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;

        exerOrder.append(bundle.getString("Exercise Order"));

        playBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (play.getIconState() == PlayIconDrawable.IconState.PLAY) {  // If play is selected then start playign the notes
                    hideSelectedNotes(stringToArray(stringBuilder.toString())); // This will make all the notes that are selected by the user invisible
                    playAsyncTask(); // This will start the asynctask to play the notes

                } else { // And when pause button is selected stop playin the  notes

                    showSlectedNotes(stringToArray(stringBuilder.toString())); // This will make all the notes selected by the user visbile
                    closeAyscTask(); // This will terminate the asynctask when pause button is hit
                }
                play.toggle(true);


                // new CustomExercise.Play().execute(); // Start Play Asynstask
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);


                EditText userInputDialogEditText = mView.findViewById(R.id.userInputDialog);

                if (!editExerName.isEmpty()) // If It is the edit exercise then wriote the exercise name which was saved previously
                {
                    userInputDialogEditText.setText(editExerName);

                }

                userInputDialogEditText.addTextChangedListener(new TextWatcher() { // For Setting Errors
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        exerName = userInputDialogEditText.getText().toString();

                        if (exerName.isEmpty()) {
                            userInputDialogEditText.setError("Exercise Name Cannot Be Empty"); // Set Error If Exercise Name Is EMpty
                        } else {
                            userInputDialogEditText.setError(null);
                        }


                    }
                });
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setTitle("Exercise Name")
                        .setPositiveButton("Save", (dialogBox, id) -> {

                            progressBar.setVisibility(View.VISIBLE);
                            exerName = userInputDialogEditText.getText().toString();

                            if (!exerName.isEmpty()) { // If exercise Name is not empoty the go on and save the data

                                addExerciseOrder(); // For Exercise Order. This Function Will Add The Exercise Name At THe End Of THe StringBuilder
                                savetoDB();

                            }
                        })

                        .setNegativeButton("Cancel",
                                (dialogBox, id) -> dialogBox.cancel());

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });


        drawFrets();

        this.setVolumeControlStream(AudioManager.STREAM_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {   // If new version then use soundpoolbuilder else use  deprecated soundpool

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(audioAttrib)
                    .build();
        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }
        checkRecordPermission();
    }
    private void drawFrets() // To Make Frets
    {
        for (int i = 0; i < frets; i++) {

            fret[i] = new ImageView(this);
            fret[i].setImageResource(R.drawable.fret);
            layoutParams.setMargins(getPx(69), 0, 0, 0);  //135 // change
            fretlayout.addView(fret[i], layoutParams);
        }
        fretlayout.post(new Runnable() // To Make Sure That Fret Layout is Layouted
        {
            @Override
            public void run() {
                setStrings(fretlayout.getWidth()); // Function call For Setting Strings

            }
        });

    }

    public void setStrings(final int width) // For Displaying The Strings
    {


        ViewGroup.LayoutParams params = stringlayout.getLayoutParams();
        params.width = width; // Set Width
        stringlayout.setLayoutParams(params); // Set Width Of Table Layout

//        int w= width;

        int[] imageViews = {   // For Declaring String Images
                R.drawable.first,
                R.drawable.second,
                R.drawable.third,
                R.drawable.fourth,
                R.drawable.fifth,
                R.drawable.sixth

        }; // Integer Array for Storing String Names For Imageviews

        TableRow.LayoutParams stringparams = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getPx(4)); // Set Height And Width Of Strings ! Don't Change The Width of String or Write width in width section

        for (int rows = 0; rows < NUM_ROWS; rows++) {
            TableRow tablerow = new TableRow(this);
            tablerow.setLayoutParams(new TableLayout.LayoutParams(

                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            stringlayout.addView(tablerow);

            for (int cols = 0; cols < NUM_ROWS; cols++) {
                string[rows] = new ImageView(this);
                string[rows].setImageResource(imageViews[rows]); // Setting Images Of The String
                string[rows].setScaleType(ImageView.ScaleType.FIT_XY);
                string[rows].setBackgroundColor(Color.TRANSPARENT);
//                string[rows].setMinimumWidth(20);

                //left right top bottom
                stringparams.setMargins(0, getPx(16), 0, 0); // Set Location of strings      // change
                tablerow.addView(string[rows], stringparams);
            }
        }

        stringlayout.post(new Runnable() {
            @Override
            public void run() {

                drawLabels(width);  // Draw Labels After String Are Set


                drawClickableBtn(width); // Draw Clickable Buttons After Labels Are Set


                drawFretMarkers();
            }
        });
    }

    public void drawLabels(final int width) // To Make Labels. The Width Parameter is To Make Table Layout The Size Of Linear Layout.
    {

        ViewGroup.LayoutParams params = labellayout.getLayoutParams();
        params.width = width; // Get Width
        labellayout.setLayoutParams(params); // Set Width Of Table Layout


        //     String a1[] = {"E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#"}; // Notes In Guitar
        String a1[] = {"e", "f", "f#", "g", "g#", "a", "a#", "b", "c", "c#", "d", "d#"}; // Notes In Guitar

        String a2[] = {"e", "b", "g", "d", "a", "e"}; // Open Notes In Guitar

        ArrayList<String> notes = new ArrayList<>(); // Converting All Notes in Guitar Array into ArrayList
        ArrayList<String> mainnotes = new ArrayList<>(); // Converting Open Notes in Guitar Array into ArrayList

        notes.addAll(Arrays.asList(a1)); // Adding Elements
        mainnotes.addAll(Arrays.asList(a2));

        int a;

        for (int rows = 0; rows < NUM_ROWS; rows++) {
            TableRow tablerow = new TableRow(this);
            tablerow.setLayoutParams(new TableLayout.LayoutParams(

                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            labellayout.addView(tablerow);

            a = notes.indexOf(a2[rows]); // For Getting The Open Notes

            for (int cols = 0; cols < NUM_COLS; cols++) {
                labels[rows][cols] = new TextView(this);
                labels[rows][cols].setBackgroundResource(R.drawable.round_label); // Set Round Label
                labels[rows][cols].setVisibility(View.INVISIBLE); // Set The label invisible
                labels[rows][cols].setId((100 * rows) + cols); // To get rows and cols for playing exercise
                labels[rows][cols].setText(notes.get(a)); // Setting Notes Name
                labels[rows][cols].setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
                labels[rows][cols].setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);


                labels[rows][cols].setTypeface(null, Typeface.BOLD_ITALIC); // for Bold and Italic


                a++;

                if (a == 12) // For Iterating The Notes
                {
                    a = 0;
                }
                tablerow.addView(labels[rows][cols]);

            }
        }

        labellayout.post(new Runnable() //To Make Sure That Linear Layout is Layouted
        {
            @Override
            public void run() {
                setCoordinates();

            }
        });
    }

    public void drawClickableBtn(final int width) // this button will not be visible to the user but when it will be on the frets. and when the button is clicked corrresponding textview will apperar
    {

        ViewGroup.LayoutParams params = exerciselayout.getLayoutParams();
        params.width = width; // Get Width
        exerciselayout.setLayoutParams(params); // Set Width Of Table Layout


        for (int rows = 0; rows < NUM_ROWS; rows++) {
            TableRow tablerow = new TableRow(this);
            tablerow.setLayoutParams(new TableLayout.LayoutParams(

                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            exerciselayout.addView(tablerow);


            for (int cols = 0; cols < NUM_COLS; cols++) {
                custombtn[rows][cols] = new Button(this);
                custombtn[rows][cols].setId((100 * rows) + cols); // To get rows and cols for playing exercise
                custombtn[rows][cols].setBackgroundColor(Color.TRANSPARENT);
                custombtn[rows][cols].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.d("Click", "Clicked");

                        Button b = (Button) v;
                        int id = b.getId();
                        new PlayAudio().executeOnExecutor(THREAD_POOL_EXECUTOR,id/100 ,id%100);

                        shakeItBaby(b.getId());
                        vibrate();
                        makeLabelVisible(b.getId()); // Make Corresponding label of the button clicked visible
                    }
                });

                custombtn[rows][cols].setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

                        Log.d("Long Press", "Long PRessed Called");

                        Button b = (Button) v;
                        removeClickedNote(b.getId());
                        makeLabelInvisible(b.getId());


                        return true; //  Keep it true so that onClick listner is not called
                    }
                });

                tablerow.addView(custombtn[rows][cols]);

            }
        }

        exerciselayout.post(new Runnable() //To Make Sure That Linear Layout is Layouted
        {
            @Override
            public void run() {
                setCoordinates();

            }
        });

    }

    public void setCoordinates() // This Function Is To Specify The Position Of Lables In The DrawFretboard.
    {

        int fretDistance = (int) (fret[4].getX() - fret[3].getX());
        Log.d("distance", String.valueOf(fretDistance));

        for (int rows = 0; rows < NUM_ROWS; rows++) {
            for (int cols = 0; cols < NUM_COLS; cols++) {
                labels[rows][cols].setY(string[rows].getY() - getPx(10)); // Gets The Position OF Fret markers And Subtracts 80 to get set the labels.
                labels[rows][cols].setX((fret[cols].getX() - fretDistance/2) - getPx(7.5f));


                // Set Cordinates for clickable button if custom exercise is to be createrd

                custombtn[rows][cols].setWidth(getPx(12));

                if (cols == 0) // If button is to be set on the first positon then set X -aixis to 0
                {
                    custombtn[rows][cols].setX(0); // Gets The Position OF Fret markers And Subtracts 80 to get set the labels.

                } else {
                    custombtn[rows][cols].setX(fret[cols - 1].getX() + getPx(7.5f));
                }


            }
        }
        int i = 3;
        while (i >= 3 && i < frets) {
            markers[i].setX( (fret[i-1].getX() + fretDistance/2) - getPx(7.5f) ); // Get the previous frets Location  and add the difference of between frets . and the  - getpx() is the size of blinking label. Logic is that get
            markers[i].setY(fretlayout.getHeight() / 2 -  getPx(9));

            if (i == 9 || i == 12) {

                i += 3;
            } else {
                i += 2;
            }
        }






    }

    public void drawFretMarkers() {

        ViewGroup.LayoutParams params = fretmarkerslayout.getLayoutParams();
        params.width = fretlayout.getWidth(); // Get Width
        fretmarkerslayout.setLayoutParams(params); // Set Width Of Table Layout

        int i = 3;
        while (i >= 3 && i < frets) {

            markers[i] = new TextView(this);
            markers[i].setBackgroundResource(R.drawable.white_label);
            markers[i].setText(Integer.toString(i));
            markers[i].setTextColor(getResources().getColor(R.color.md_indigo));
            markers[i].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            markers[i].setTypeface(null, Typeface.BOLD_ITALIC);


            fretmarkerslayout.addView(markers[i]);

            if (i == 9 || i == 12) {

                i += 3;
            } else {
                i += 2;
            }

        }

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;



        if(Objects.equals(bundle.get("Type"), "Edit"))
        {

            editExerName = bundle.getString("Name");
            stringBuilder.append(bundle.getString("Notes"));

            Log.d("Notes" ,  "hjell" + stringBuilder.toString());

            showSlectedNotes(stringToArray(stringBuilder.toString()));
        }



        Log.d("Flow", "Freat Markers Drawn");
    }

    private int getPx(float dp)
    {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,r.getDisplayMetrics()));
    }

    private void playAudio(int row, int col) {


        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
                soundPool.play(soundID, 100, 100, 1, 0, 1f);

            }
        });


        soundID = soundPool.load(getApplicationContext(), soundFiles[row][col], 1);

/*
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        float actualVolume =  audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume =  audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume/maxVolume;

        if(loaded)
        {
            soundPool.play(soundID,volume,volume,1,0,1f);
        }*/

    }

    public void makeLabelVisible(int id) // For Custom Exercise Makes The button That is clicked Visbile
    {

        saveBtn.setVisibility(View.VISIBLE);
        playBtn.setVisibility(View.VISIBLE); // This will make the play button visible. It is done so that play button is not shown until a not is selected by the user
        int row = id / 100;
        int col = id % 100;
        labels[row][col].setVisibility(View.VISIBLE);

        storeNote(id);

    }

    private void storeNote(int id) {

        if (!stringBuilder.toString().equals("")) // This is to know if it is the first note to be added. and if it the first not then  the ',' is not needed
        {

            stringBuilder.append(',');
            stringBuilder.append(id);


        } else {
            stringBuilder.append(id);
        }

        Log.d("Notes ", "Before Delete" + String.valueOf(stringBuilder));

        customNoteCount++;


    }

    private void removeClickedNote(int id) {
        String note = String.valueOf(id);
        int i = stringBuilder.indexOf(note); // Get the position of the note to delet in the  string builder

        if (i != -1) {

            if (i == 0) // If it is the first note entered then delete the note and and the comma after it.
            {
                stringBuilder.delete(i, i + note.length() + 1);
            } else { // Else Deleet The Note And The Comma Before It
                stringBuilder.delete(i - 1, i + note.length());


            }
        }


        if (stringBuilder.length() == 0) // Make Bith Play And Save Buttons Invisible if no note is present
        {
            playBtn.setVisibility(View.INVISIBLE);
            saveBtn.setVisibility(View.INVISIBLE);
        }

        Log.d("Notes ", "After Delete " + String.valueOf(stringBuilder));
    }

    private void makeLabelInvisible(int id) {

        if (!checkAnotherNoteIsPresent(id)) // Same Note is not present then make the note invisible
        {
            int row = id / 100;
            int col = id % 100;
            labels[row][col].setVisibility(View.INVISIBLE);
        }


    }

    private boolean checkAnotherNoteIsPresent(int id) {
        String note = String.valueOf(id);
        int i = stringBuilder.indexOf(note); // Get the position of the note to delet in the  string builder

        return i != -1;

    }

    private void checkRecordPermission() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO},
                    123);
        }
    }

    private void addExerciseOrder() {

        Log.d("Contain" , exerOrder.toString());


            if (exerOrder.length() > 0) {

                if(!exerOrder.toString().contains(exerName))
                {
                    exerOrder.append(",");
                    exerOrder.append(exerName);
                }



            } else {
                exerOrder.append(exerName);
            }



    }

    private void savetoDB() {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docref = FirebaseFirestore.getInstance().document("Users/" + user.getUid() + "/Custom Exercises/Notes");

        Map<String, Object> docData = new HashMap<>();
        docData.put(exerName, stringBuilder.toString());
        Log.d("Contain" , exerOrder.toString());

        docData.put("CustomExer Order", exerOrder.toString());

        docref.set(docData, SetOptions.merge()); // Always use Setoptions.merge function else data will be ovewritten

        progressBar.setVisibility(View.INVISIBLE);
        startActivity(new Intent(getApplicationContext() , CustomExerciseList.class));


    }


    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(40);
    }

    private void shakeItBaby(int stringno) {
        string[stringno / 100].startAnimation(animation);

    }

    private void playAsyncTask() {


        new Notes().getNote(stringBuilder.toString(), new NotesInterface() { // To get the notes
            @Override
            public void process(int[] note) {
                notes = note.clone();  // Copy note to notes array

                new CustomExercise.Play().executeOnExecutor(THREAD_POOL_EXECUTOR); // Start Play Asynstask


            }
        });
    }

    private void closeAyscTask() {
        closeasynctask = true; // When The back button us pressed make the closeasynctask true,  to close the asynctask

    }

    private void showSlectedNotes(int[] ints) { // This will make all the labels visbel
        for (int note : ints) {


            Log.d("Notes" , "Enter");
            labels[note / 100][note % 100].setVisibility(View.VISIBLE);
        }
    }

    private void hideSelectedNotes(int[] ints) // This will make all the labels invisible
    {
        for (int anInt : ints) {
            labels[anInt / 100][anInt % 100].setVisibility(View.INVISIBLE);

        }


    }

    private int[] stringToArray(String stringnotes) {
        String[] items = stringnotes.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

        int[] notes = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            try {
                notes[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException nfe) {
                //NOTE: write something here if you need to recover from formatting errors
            }
        }
        return notes;
    }

    public class Play extends AsyncTask<Integer, Integer, Object> {
        @Override
        protected void onPostExecute(Object s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            int row = values[0], col = values[1]; // Get the rows and columns from the 'values' parameter

            int x = (int) labels[row][col].getX(); // Get the X-axis of the fret marker This is to know  if the fret note to to displayed os within the screen or if screen needs to be scrooled
            int y = (int) labels[row][col].getY(); // Get the Y-axis


            Rect rect = new Rect();
            if (!labels[row][col].getGlobalVisibleRect(rect) // Check To See if label in visible in the screen
                    && labels[row][col].getHeight() == rect.height()
                    && labels[row][col].getWidth() == rect.width()) {
                scrollView.smoothScrollTo(x, y);
            }


            if (temprow == -1)  // To check if it's the first note being played
            {

                labels[row][col].setVisibility(View.VISIBLE); // Set Visiblity of the label true
                new PlayAudio().executeOnExecutor(THREAD_POOL_EXECUTOR,values[0],values[1]);
            } else { // It it's not the first note then else will be called


                labels[temprow][tempcol].setVisibility(View.INVISIBLE); // Make the previous note invisible

                labels[row][col].setVisibility(View.VISIBLE);  //  Make the curent note visible

               new PlayAudio().executeOnExecutor(THREAD_POOL_EXECUTOR,values[0],values[1]);

            }

            temprow = row;
            tempcol = col;
        }

        @Override
        protected synchronized Object doInBackground(Integer... integers) {

            while (track < notes.length) {


                publishProgress(notes[track] / 100, notes[track] % 100);  // To publish the progress   parameter =  1 : row  2 :col
                Log.d("Test", "Row " + (notes[track] / 100) + " Col " + (notes[track] % 100));
                Log.d("Test", String.valueOf(notes.length));


               /* int speed = 1000;  // Get the speed of playing from seekbar
                long futuretime = System.currentTimeMillis() + speed; // To estimate the time to wait before playing the next note


                while (System.currentTimeMillis() < futuretime) { // The condition will wait until the specified time has passed
                }
*/

                double speed  = 6e+10 / 60; // Divided By 60 Coz The Speed Is Set at 60 bpm
                double futuretime = System.nanoTime() + speed;

                while(System.nanoTime() < futuretime)
                {

                }
                track++;

                if (track == notes.length) { // If the notes reach till the end start from the beginning
                    Log.d("Test", "End Reached");
                    track = 0;
                }


                if (closeasynctask) { // To Close The Asynctask When the Activity Is Destroyed or pause when when boom menu did show
                    closeasynctask = false;
                    track = 0;
                    temprow = tempcol = -1;
                    break; //  Break the while loop to pause or destroy the asynctask
                }


            }

            return null;

        }
    }

    public class PlayAudio extends AsyncTask<Integer, Integer, Float> {
        @Override
        protected Float doInBackground(Integer... integers) {

            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    loaded = true;
                    soundPool.play(soundID, 100, 100, 1, 0, 1f);

                }
            });


            soundID = soundPool.load(getApplicationContext(), soundFiles[integers[0]][integers[1]], 1);

            return null;
        }
    }


}


