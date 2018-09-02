package com.bmniman.littleguitarians.Designs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
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
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bmniman.littleguitarians.Classes.Notes;
import com.bmniman.littleguitarians.Classes.ProcessPitch;
import com.bmniman.littleguitarians.Database.SendData.SaveDataService;
import com.bmniman.littleguitarians.GetterSetter.BoomButtons;
import com.bmniman.littleguitarians.GetterSetter.ScaleFormula;
import com.bmniman.littleguitarians.GetterSetter.Track;
import com.bmniman.littleguitarians.Interfaces.NotesInterface;
import com.bmniman.littleguitarians.R;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListenerAdapter;
import com.nightonke.boommenu.Util;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


public class DrawFretboard extends AppCompatActivity { //implements RecognitionListener{


    // Testing as

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
    TableRow tablerow;
    BubbleSeekBar tempo; // For Keeping Track of Tempo
    int temprow = -1; // For label Animationn
    int tempcol = -1; //For label Animation
    Animation animation; //For Shake String Animation
    ImageView playBtn;
    Boolean closeasynctask = false;
    int tempcount = 0;
    int track = 0; // To keep track of notes being played
    Track trackobj = new Track(); // Track getter and setter object used to track the note being played
    boolean correctpitch;
    boolean isscale = false; // To find if scale is selected
    BoomMenuButton bmb;
    String message;
    int rate;
    int size;
    int scalecount = 0;
    long startTym = 0L;
    long endTym = 0L;
    int totalTym = 0;
    int  temptym =0;
    SharedPreferences pref; // For Settings
    SharedPreferences.Editor editor; // for Settings to add preferences
    BoomButtons bmbgetter;
    boolean reverse = true;
    boolean exerciseUnlocked = false;  //This is to calcualte if the exercise has been unlocked or not
    int accuracy; // To Store The Accuracy of the Exercise
    String exerName;
    int tempCorrectNotes = 0;
    int tempInCorrectNotes = 0;
    String type;
    boolean customExer = true;
    String scale;
    String key;
    boolean shown = false;

    Context context = this;

    int vol=100;

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


    // Note counters


    int correctPracticeNotes = 0;
    int incorrectPracticeNotes = 0;
    int correctPerformNotes = 0;
    int incorrectPerformNotes = 0;


    // For Speech To Text

  /*  *//* Named searches allow to quickly reconfigure the decoder *//*
    private static final String KWS_SEARCH = "wakeup";
    private static final String FORECAST_SEARCH = "forecast";
    private static final String DIGITS_SEARCH = "digits";
    private static final String PHONE_SEARCH = "phones";
    private static final String MENU_SEARCH = "menu";
    private static final String PLAY = "play";
    private static final String PAUSE= "pause";


    *//* Keyword we are looking for to activate menu *//*
    private static final String KEYPHRASE = "hey watson";

    *//* Used to handle permission request *//*
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;
*/

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
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();




        pauseAsynctask(); // This will pause the notes playing on the fretboard

        Log.d("Tracking" , "Correct = " + correctPerformNotes + "  Incorrect = " + incorrectPerformNotes + " Time = " +totalTym );

        Intent intent = new Intent(this, SaveDataService.class);
        intent.putExtra("Correct Perform Notes", correctPerformNotes);
        intent.putExtra("Incorrect Perform Notes", incorrectPerformNotes);
        intent.putExtra("Tym Spent", totalTym);

        if (exerciseUnlocked && !customExer) { // To make the next default exercise unlocked
            intent.putExtra("Exercise Name", exerName);
        } else {
            intent.putExtra("Exercise Name", "");
        }


        startService(intent); // This service will save the data to the database
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        playAsyncTask();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_draw_fretboard);


        scrollView = findViewById(R.id.horizontalScrollView);
        relativelayout = findViewById(R.id.relativelayout);
        fretlayout = findViewById(R.id.fretlayout);
        fretmarkerslayout = findViewById(R.id.fretmarkerslayout);
        labellayout = findViewById(R.id.labelslayout);
        exerciselayout = findViewById(R.id.excerisebutton);
        stringlayout = findViewById(R.id.stringslayout);
        tempo = findViewById(R.id.tempo);
        playBtn = findViewById(R.id.imageView);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = pref.edit();
        bmbgetter = new BoomButtons();
        bmbgetter.setPlayMode("Perform");
        bmbgetter.setSound("Unmute");
        bmbgetter.setReverse(true);


        tempo.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

                 if(progress>125) {
                     if (!shown) {

                         shown = true;
                         Log.d("Reach", "Reahed");
                         SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

                         if (prefs.getBoolean("ShowTempo", true)) {

                             Log.d("Reach", "REaching!!!!!!!!!");
                             pauseAsynctask();
                             final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                             //Setting message manually and performing action on button click
                             builder.setMessage("Guitar sounds may not be in sync above 125 bpm tempo");
                             //This will not allow to close dialogbox until user selects an option
                             builder.setCancelable(false);
                             builder.setPositiveButton("Don't Show Again", new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {

                                     //builder.finish();
                                     editor.putBoolean("ShowTempo", false);
                                     editor.apply();
                                     dialog.cancel();
                                     playAsyncTask();
                                     shown = false;

                                 }
                             });
                             builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     //  Action for 'NO' Button

                                     shown = false;
                                     playAsyncTask();
                                 }
                             });
                             //Creating dialog box
                             AlertDialog alert = builder.create();
                             alert.show();


                         }

                     }
                 }

            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {


                Log.d("Finally" , "Finally Reached");




                }
        });


       /* // For speech To Text
        // Prepare the data for UI
        captions = new HashMap<>();
        captions.put(KWS_SEARCH, R.string.kws_caption);
        captions.put(MENU_SEARCH, R.string.menu_caption);
        captions.put(DIGITS_SEARCH, R.string.digits_caption);
        captions.put(PHONE_SEARCH, R.string.phone_caption);
        captions.put(FORECAST_SEARCH, R.string.forecast_caption);
        new SetupTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
*/

        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        rate = Integer.parseInt(audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE));
        size = Integer.parseInt(audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER));


        playBtn.setOnClickListener(v -> {


            if (!bmb.isBoomed()) {
                bmb.boom();
            }

        });


        bmb = findViewById(R.id.bmb);


        int imgs[] = {R.drawable.play,
                R.drawable.mute,
                R.drawable.performance,
                R.drawable.reverse};

        String txt[] = {

                "Play", "Sound Off", "Practice", "Reverse"
        };

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) { // Set boom bar image and text
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .normalImageRes(imgs[i])
                    .imageRect(new Rect(Util.dp2px(27), Util.dp2px(27), Util.dp2px(50), Util.dp2px(50)))
                    .normalText(txt[i])
                    .rippleEffect(true)
                    .shadowEffect(true);

            bmb.addBuilder(builder);
        }
        bmb.setAutoBoom(true);
        bmb.setBackgroundColor(Color.TRANSPARENT);



        Bundle bundle = getIntent().getExtras(); // Get the value passed from the the fragmnet
        assert bundle != null;
        message = bundle.getString("Exercise Notes");

        exerName = bundle.getString("Name");

        type = bundle.getString("Type");

        if (type.equals("exercise")) {
            customExer = false;
        }

        if (type.equals("scale")) // To know if scale is to be played
        {
            scale = bundle.getString("Scale");
            key = bundle.getString("Key");
            isscale = true;
            playBtn.setVisibility(View.INVISIBLE);

        }


        bmb.setOnBoomListener(new OnBoomListenerAdapter() {

            /**
             * When one of the boom-button is clicked.
             *
             * @param index      index of the clicked boom-button
             * @param boomButton the clicked boom-button
             */
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                super.onClicked(index, boomButton);


                boomButtonsPressed(index);

            }

            /**
             * When the BMB finishes hide animations.
             */

            @Override
            public void onBoomDidHide() { //  Call When boom menu hides
                super.onBoomDidHide();


                playAsyncTask();

            }

            /**
             * When the BMB finished boom animations.
             */
            @Override
            public void onBoomDidShow() { // Called When Boom Menu did show
                super.onBoomDidShow();


                pauseAsynctask();

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

    // Hello

    private void drawFrets() // To Make Frets
    {

        for (int i = 0; i < frets; i++) {

            fret[i] = new ImageView(this);
            fret[i].setImageResource(R.drawable.fret);

//            pref.getInt("fretDist" , null);
              layoutParams.setMargins(getPx(pref.getInt("fretDist",69)), 0, 0, 0);  //135 // change

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
            tablerow = new TableRow(this);
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
                stringparams.setMargins(0, getPx(15), 0, 0); // Set Location of strings      // change
                tablerow.addView(string[rows], stringparams);

            }
        }

        stringlayout.post(new Runnable() {
            @Override
            public void run() {

                drawLabels(width);  // Draw Labels After String Are Set
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

        notes.addAll(Arrays.asList(a1)); // Adding Elements


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
                labels[rows][cols].setBackgroundResource(R.drawable.yellow_circle); // Set Round Label
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


    public void setCoordinates() // This Function Is To Specify The Position Of Lables In The DrawFretboard.
    {

        int fretDistance = (int) (fret[4].getX() - fret[3].getX());
        Log.d("distance", String.valueOf(fretDistance));

        for (int rows = 0; rows < NUM_ROWS; rows++) {
            for (int cols = 0; cols < NUM_COLS; cols++) {
//                labels[rows][cols].setX(fret[cols].getX() - getPx(50)); // Gets The Position OF Fret markers And Subtracts 80 to get set the labels.

                labels[rows][cols].setX((fret[cols].getX() - fretDistance/2) - getPx(7.5f));
                labels[rows][cols].setY(string[rows].getY() - getPx(10)); // Gets The Position OF Fret markers And Subtracts 80 to get set the labels.

            }
        }
        int i = 3;
        while (i >= 3 && i < frets) {
//            markers[i].setX(fret[i].getX() - getPx(53));


            // Width of blinking lable is 17dp
            markers[i].setX( (fret[i-1].getX() + fretDistance/2) - getPx(7.5f) ); // Get the previous frets Location  and add the difference of between frets . and the  - getpx() is the size of blinking label. Logic is that get
            // the difference between two frets and deduct the difference of blinking  to put the fret marker in center
            markers[i].setY(fretlayout.getHeight() / 2 - getPx(9));

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
            markers[i].setTextColor(getResources().getColor(R.color.md_indigo));
            markers[i].setText(Integer.toString(i));
            markers[i].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            markers[i].setTypeface(null, Typeface.BOLD_ITALIC);
            fretmarkerslayout.addView(markers[i]);


            if (i == 9 || i == 12) {

                i += 3;
            } else {
                i += 2;
            }

        }
        Log.d("Flow", "Freat Markers Drawn");
    }

    private int getPx(float dp)
    {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,r.getDisplayMetrics()));
    }

    public int[] reverse(int[] nums) {  // function will reverse the passsed integer array to it
        int[] reversed = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            reversed[i] = nums[nums.length - 1 - i];
        }
        return reversed;
    }

    public void getScaleClicked() // Tne function will fill the root notes of the key and display them.
    {
        for (int rows = 0; rows < NUM_ROWS; rows++) {
            for (int cols = 0; cols < NUM_COLS; cols++) {
                if (labels[rows][cols].getText().equals(key)) // When the note is found make it visisble and set an on click listener on it.
                {
                    labels[rows][cols].setVisibility(View.VISIBLE);
                    labels[rows][cols].setClickable(true);
                    labels[rows][cols].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            TextView txtview = (TextView) v; // Cast view into Textview

                            makeLabelsInvisible(key);
                            calculateScaleNotes(txtview.getId());
                        }
                    });


                }
            }
        }


    }

    public void calculateScaleNotes(int id) // This function will calculate the notes of the scale to be played
    {
        String scaleNotes[] = getNotesInScale();

        int note[] = new int[scaleNotes.length]; // temp array to store the final notes
        int row = id / 100;
        int col = id % 100;
        int count = 0; // To keep the count of number of notes the are calculated
        int scalenotes = scaleNotes.length; // Total notes in the scale . Pentatonic scale consists of 5 notes
        int start;

        if (col > 3) { // Move Three Steps Away from the label selected. This is done as the scale is only three notes away from the main note
            start = col - 3;
        } else {
            start = 0;
        }

        while (count < scalenotes + 1) { // Count should be less than number of notes in the scale

            if (count == 0) // If First note then it is the note selected by the user and it is obviously a scale note
            {

                note[count] = id; // copy is to the array
                Log.d("Notes", String.valueOf(note[count]));
                count++;
            }



            /*

            The for loop should start from the beginning or from the 12th col as per the note clicked by the user.
            The for loop should be less the total number of cols and the loop should only check till the 4 frets

             */
            for (int cols = start; cols < col + 4 && cols < NUM_COLS; cols++) {

                if (labels[row][cols].getText().equals(scaleNotes[count])) // If the note from the scaleNotes array is found store it in the note arrat
                {

                    note[count] = labels[row][cols].getId();

                    count++;

                    if (count == scalenotes) //  if count is equal to the total number of notes break the loop
                    {
                        break;
                    }


                }

                if (cols + 1 >= NUM_COLS) // Ir FretBoard Is Going To End Then Break The Loop So Another String Could Be Reacehd.
                {
                    break;
                }


            }
            if (count == scalenotes) //  if count is equal to the total number of notes break the loop
            {
                break;
            }

            if (row != 0) // if row is not zero the decrement row by 1. this is done if the note isn't found on the top string
            {
                row--;
            } else // if last string is reached. i.e row =0
            {
                for (int cols = start; cols < NUM_COLS && count < scalenotes; cols++) {
                    if (labels[0][cols].getText().equals(scaleNotes[count])) // as it is the last string row will be 0
                    {
                        note[count] = labels[0][cols].getId();

                        count++;
                    }


                    if (cols + 1 >= NUM_COLS) // If On The LAst String End Of FretBoard Is Reached Start From The 0th Fret.
                    {
                        for (int cols1 = 0; count < scalenotes; cols1++) {
                            if (labels[0][cols1].getText().equals(scaleNotes[count])) {
                                note[count] = labels[0][cols1].getId();
                                count++;
                            }
                        }
                    }
                }

                break;
            }
        }

        notes = note.clone();
        showNotesToPlay(notes);  // This will make all the notes which are to be played by the user visible

        bmb.setVisibility(View.VISIBLE);
        playBtn.setVisibility(View.VISIBLE);
        new DrawFretboard.Play().execute(); // Start Play Asynstask


    }

    private String[] getNotesInScale() {


        int count = 0;
        int formula[] = new int[10];
        switch (scale) {
            case "Major":
                formula = new ScaleFormula().getMajor();
                break;

            case "Minor":
                formula = new ScaleFormula().getMinor();
                break;

            case "Major Pentatonic":
                formula = new ScaleFormula().getMajorPent();
                break;

            case "Minor Pentatonic":
                formula = new ScaleFormula().getMinorPent();
                break;

            case "Dorian":
                formula = new ScaleFormula().getDorian();
                break;
        }


        int formulaLen = formula.length;
        String scaleNotes[] = new String[formulaLen];
        String allNotes[] = {"c", "c#", "d", "d#", "e", "f", "f#", "g", "g#", "a", "a#", "b", "c", "c#", "d", "d#", "e", "f", "f#", "g", "g#", "a", "a#", "b"};
        int allNotesLen = allNotes.length - 1;
        int keyPoint = Arrays.asList(allNotes).indexOf(key);

        int addSteps;

        Log.d("Scale", String.valueOf(allNotesLen));


        while (count < formulaLen) {
            addSteps = formula[count];
            scaleNotes[count] = allNotes[addSteps + keyPoint];
            keyPoint += addSteps;
            Log.d("scales", Arrays.toString(scaleNotes));
            count++;

        }

        return scaleNotes;

    }

    /*
    This function will make all the labels invisble when a label is clicked
     */
    public void makeLabelsInvisible(String key) {
        for (int rows = 0; rows < NUM_ROWS; rows++) {

            for (int cols = 0; cols < NUM_COLS; cols++) {
                if (labels[rows][cols].getText().equals(key)) {
                    labels[rows][cols].setVisibility(View.INVISIBLE);
                    labels[rows][cols].setClickable(false);


                }
            }
        }
    } // For Scales

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

    private void checkRecordPermission() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO},
                    123);
        }
    }

    private void pauseAsynctask() {



        if(startTym!=0)
        {
            endTym = System.nanoTime(); // For Timer. This will calculate the end time

            temptym = totalTym;

            totalTym = (int) TimeUnit.SECONDS.convert( (endTym-startTym) , TimeUnit.NANOSECONDS);
            totalTym = totalTym+temptym;

            Log.d("Time" ,"After Convert " + String.valueOf(totalTym));


        }


        if (tempcount == 0) { // If boom menu showed for the first time i.e by deafult then increment tempcount and don't close the asynctask
            closeasynctask = false;
            tempcount++;
        } else { //  If boom menu didn't show for the first time then pause the asynnctask. i.e pause
            closeasynctask = true; // This will pause the asynctask

        }


    }

    private void playAsyncTask() {

//        showSoundDialog();

        startTym = System.nanoTime(); // This will calcualte the time in nano seconds the exercsie is played. The timer will start here

        if (trackobj.getTrack() == 0) { //  If Boom menu hid for the first time then call this if method nad set the value for notes array


            if (isscale && scalecount == 0){ // If it is scales then show the key notes for the user to click
                scalecount++;
                getScaleClicked();
                bmb.setVisibility(View.INVISIBLE);
            } else {

                if(!isscale)
                {
                    new Notes().getNote(message, new NotesInterface() { // To get the notes
                        @Override
                        public void process(int[] note) {
                            notes = note.clone();  // Copy note to notes array

                            showNotesToPlay(notes); // This will make all the notes which are to be played by the user visible

                            new DrawFretboard.Play().execute(); // Start Play Asynstask


                        }
                    });
                }

                else {
                    track = trackobj.getTrack(); // Get the note at which playing is paused
                    new DrawFretboard.Play().execute(); // Start Play Asynstask
                }

            }

        } else { // Boom Menu didn't hide for the first time then call this else method

            Log.d("Test", "Notes Len is" + notes.length);

            Log.d("Reach" , "came");
            showNotesToPlay(notes);  // This will make all the notes which are to be played by the user visible

            track = trackobj.getTrack(); // Get the note at which playing is paused



            new DrawFretboard.Play().execute(); // Start Play Asynstask




        }


    }

    private void showNotesToPlay(int[] notes)
    {
        for (int note : notes) {

            labels[note/100][note%100].setVisibility(View.VISIBLE);

        }
    }

    private void closeAyscTask() {
        closeasynctask = true; // When The back button us pressed make the closeasynctask true,  to close the asynctask

        Log.d("Time" , "Cose" + totalTym);

    }

    private void vibrate() {
        if (pref.getBoolean("vibrate", true)) // If vibrate option is selected by the user then the mobile will vibrate eilse it won't
        {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(40);
        }


    }

    private void boomButtonsPressed(int index) {


        Log.d("Pressed", String.valueOf(index));


        BoomButton boomButton1 = bmb.getBoomButton(index);

        ImageView image = boomButton1.getImageView();
        TextView text  = boomButton1.getTextView();

        if (index == 1) {
            if (image != null) {

                if (bmbgetter.getSound().equals("Unmute")) {


                    image.setImageResource(R.drawable.unmute);
                    text.setText("Sound On");
                    bmbgetter.setSound("Mute");
                    // Call function To Mute Here

                } else {

                    image.setImageResource(R.drawable.mute);
                    text.setText("Sound Off");
                    bmbgetter.setSound("Unmute");

                }

            }

        }

        if (index == 2) {
            if (bmbgetter.getPlayMode().equals("Perform")) {
                image.setImageResource(R.drawable.performance);
                text.setText("Performance");
                bmbgetter.setPlayMode("Practice");

                Log.d("Mode" , bmbgetter.getPlayMode());
            } else {
                bmbgetter.setPlayMode("Perform");
                text.setText("Practice");
                image.setImageResource(R.drawable.prectice);

                Log.d("Mode" , bmbgetter.getPlayMode());


            }

        }

        if (index == 3) {
            if (bmbgetter.isReverse()) {
                reverse = false;
                image.setImageResource(R.drawable.reverse); // Set Not Reverse Imaeg Here
                bmbgetter.setReverse(false);

                Log.d("Reverse", "If Conditoin");

            } else {
                reverse = true;
                bmbgetter.setReverse(true);

                Log.d("Reverse", "Else Conditoin");
            }
        }

    }

    public class Play extends AsyncTask<Integer, Integer, Object> {
        @Override
        protected void onPostExecute(Object s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {




            int row = values[0], col = values[1]; // Get the rows and columns from the 'values' parameter

            int x = (int) labels[row][col].getX(); // Get the X-axis of the fret marker
            int y = (int) labels[row][col].getY(); // Get the Y-axis


            Rect rect = new Rect();
            if (!labels[row][col].getGlobalVisibleRect(rect) // Check To See if label in visible in the screen
                    && labels[row][col].getHeight() == rect.height()
                    && labels[row][col].getWidth() == rect.width()) {
                scrollView.smoothScrollTo(x, y);
            }


            if (temprow == -1)  // To check if it's the first note being played
            {

//                labels[row][col].setVisibility(View.VISIBLE); // Set Visiblity of the label true
                labels[row][col].setBackgroundResource(R.drawable.round_label);
                 new PlayAudio().executeOnExecutor(THREAD_POOL_EXECUTOR,values[0],values[1]);


                vibrate();
                new Pitch().executeOnExecutor(THREAD_POOL_EXECUTOR, values[0], values[1]); // Start Checking the pitch


            } else { // It it's not the first note then else will be called

                labels[row][col].setBackgroundResource(R.drawable.round_label);

                if (correctpitch) {
//                    labels[temprow][tempcol].setBackgroundResource(R.drawable.round_label); // Changfe This

                    Log.d("Corr" , "Pitch is Correct");

                    labels[temprow][tempcol].setBackgroundResource(R.drawable.yellow_circle);

                }


//                labels[temprow][tempcol].setVisibility(View.INVISIBLE); // Make the previous note invisible

//                labels[row][col].setVisibility(View.VISIBLE);  //  Make the curent note visible
//                labels[row][col].setBackgroundResource(R.drawable.round_label);


                vibrate();
                new PlayAudio().executeOnExecutor(THREAD_POOL_EXECUTOR,values[0],values[1]);
                new Pitch().executeOnExecutor(THREAD_POOL_EXECUTOR, values[0], values[1]); // Start Checking the pitch Params : row , col


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
/*

            // For Millisecod precision

                int speed = 60000 / tempo.getProgress();  // Get the speed of playing from seekbar
                long futuretime = System.currentTimeMillis() + speed; // To estimate the time to wait before playing the next note
*/


// For Nanaoseconds precision

                double speed  = 6e+10 / tempo.getProgress();
                double futuretime = System.nanoTime() + speed;
              /*  while (System.currentTimeMillis() < futuretime) { // The condition will wait until the specified time has passed
                }*/

              while(System.nanoTime()  < futuretime)
              {

              }

                  track++;
                if (track == notes.length) { // If the notes reach till the end start from the beginning
                    Log.d("Test", "End Reached");
                    track = 0;


                    try
                    {
                        accuracy = (tempCorrectNotes * 100) / (tempInCorrectNotes + tempCorrectNotes);

                    }
                    catch (ArithmeticException ignored){ accuracy =0 ;}


                    if (accuracy > 50) {
                        exerciseUnlocked = true; // If The Accuracy is Above 50 PErcent then unlock The Exercise

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context , "Next Exercise Unlocked"  ,Toast.LENGTH_LONG).show();

                            }
                        });

                    }


                    tempCorrectNotes = tempInCorrectNotes = 0;


                    if (bmbgetter.isReverse()) {
                        notes = reverse(notes);
                        Log.d("Reverse", Arrays.toString(notes));
                    }
                }

                if (closeasynctask) { // To Close The Asynctask When the Activity Is Destroyed or pause when when boom menu did show

                    trackobj.setTrack(track); // Set The object of GetterSetter to the current track of the note
                    closeasynctask = false;

                    if (track == notes.length) { // If the asynctask is stopped at the last note then make track  = 0 , to restart playing from the beginning
                        track = 0;


                    }

                    break; //  Break the while loop to pause or destroy the asynctask
                }


            }

            return null;

        }


    }

    private void showSoundDialog()
    {


        pauseAsynctask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (prefs.getBoolean("ShowSound", true)) {



            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            //Setting message manually and performing action on button click
            builder.setMessage("Turn off Guitar Sounds To Accurately Detect If You Are Playing Correctly or Not!!!!");
            //This will not allow to close dialogbox until user selects an option
            builder.setCancelable(false);
            builder.setPositiveButton("Don't Show Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    //builder.finish();
                    editor.putBoolean("ShowSound", false);
                    editor.apply();
                    dialog.cancel();
                    playAsyncTask();
                    shown = false;

                }
            });
            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //  Action for 'NO' Button

                    shown = false;
                    playAsyncTask();
                }
            });
            //Creating dialog box
            AlertDialog alert = builder.create();
            alert.show();


        }





    }

    public class Pitch extends AsyncTask<Integer, Integer, float[]> {
        AudioDispatcher dispatcher;


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


            if (values[0] == 0) // not correct pitch
            {
                labels[values[1]][values[2]].setBackgroundResource(R.drawable.redlabel);
                incorrectPerformNotes++;
                tempInCorrectNotes++;


                if (bmbgetter.getPlayMode().equals("Practice") )
                {
                    // It Practice Mode Is Seleceted Then Keep the note on the same value until correct note is played

                    track--;
                }





            } else {
                labels[values[1]][values[2]].setBackgroundResource(R.drawable.yellow_circle);

                correctPerformNotes++;
                tempCorrectNotes++;

            }


        }

        @Override
        protected void onPostExecute(float[] s) {

        }

        @Override
        protected float[] doInBackground(Integer... integers) {
            final float[] pitch = {0};


            Log.d("Tag", "Size :" + size + " & Rate: " + rate);

            dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0); // 22050 , 1024
            PitchDetectionHandler pdh = new PitchDetectionHandler() {
                @Override
                public void handlePitch(PitchDetectionResult res, AudioEvent e) {
                    pitch[0] = res.getPitch();
                    Log.d("Pitch ", String.valueOf(pitch[0]));
                    dispatcher.stop();


                }
            };

            AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.YIN, 22050, 1024, pdh);
            dispatcher.addAudioProcessor(pitchProcessor);
            dispatcher.run();


            if (process.checkPitch(integers[0], integers[1], pitch)) { // If Pitch Is Correct
                correctpitch = true;
                publishProgress(1, integers[0], integers[1]); // Param 1 : boolean value to notify correct note is played , 2 : row , 3 : col
            } else {
                correctpitch = false;
                publishProgress(0, integers[0], integers[1]);

            }


            return new float[]{pitch[0], integers[0], integers[1]};
        }


    }

    public class PlayAudio extends AsyncTask<Integer, Integer, Float> {
        @Override
        protected Float doInBackground(Integer... integers) {

            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    loaded = true;

                    if(bmbgetter.getSound()=="Unmute")
                    {
                        soundPool.play(soundID, 100, 100, 1, 0, 1f);

                    }

                    else {

                        soundPool.play(soundID, 00, 00, 1, 0, 1f);

                    }


                }
            });


            soundID = soundPool.load(getApplicationContext(), soundFiles[integers[0]][integers[1]], 1);

            return null;
        }
    }




    /*private static class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<DrawFretboard> activityReference;
        SetupTask(DrawFretboard activity) {
            this.activityReference = new WeakReference<>(activity);
        }
        @Override
        protected Exception doInBackground(Void... params) {
            try {
                Assets assets = new Assets(activityReference.get());
                File assetDir = assets.syncAssets();
                activityReference.get().setupRecognizer(assetDir);
            } catch (IOException e) {
                return e;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Exception result) {
            if (result != null) {   // when Speech To text initialization Failed

              // Shoe error Message Here

            } else {
                activityReference.get().switchSearch(KWS_SEARCH);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recognizer initialization is a time-consuming and it involves IO,
                // so we execute it in async task
                new DrawFretboard.SetupTask(this).execute();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

    *//**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     *//*
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();

        Log.d("Speech" , text);

        if (text.equals(KEYPHRASE))
            switchSearch(MENU_SEARCH);
        else if (text.equals(PAUSE))
        {
            bmb.boom();
        }
        else if (text.equals(PLAY))
        {
            this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        }

        else
        {
            Log.d("Speech" , hypothesis.getHypstr());
        }

    }

    *//**
     * This callback is called when we stop the recognizer.
     *//*
    @Override
    public void onResult(Hypothesis hypothesis) {
//        ((TextView) findViewById(R.id.result_text)).setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    *//**
     * We stop recognizer here to get a final result
     *//*
    @Override
    public void onEndOfSpeech() {
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 10000);

        String caption = getResources().getString(captions.get(searchName));
//        ((TextView) findViewById(R.id.caption_text)).setText(caption);
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)

                .getRecognizer();
        recognizer.addListener(this);

        *//* In your application you might not need to add all those searches.
          They are added here for demonstration. You can leave just one.
         *//*

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

        // Create grammar-based search for digit recognition
        File digitsGrammar = new File(assetsDir, "digits.gram");
        recognizer.addGrammarSearch(DIGITS_SEARCH, digitsGrammar);

        // Create language model search
        File languageModel = new File(assetsDir, "weather.dmp");
        recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);

        // Phonetic search
        File phoneticModel = new File(assetsDir, "en-phone.dmp");
        recognizer.addAllphoneSearch(PHONE_SEARCH, phoneticModel);
    }
    @Override
    public void onError(Exception error) {
//        ((TextView) findViewById(R.id.caption_text)).setText(error.getMessage());
    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }*/

}


