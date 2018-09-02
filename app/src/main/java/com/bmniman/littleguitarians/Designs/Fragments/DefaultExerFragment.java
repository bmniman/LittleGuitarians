package com.bmniman.littleguitarians.Designs.Fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;


import com.bmniman.littleguitarians.Designs.DrawFretboard;
import com.bmniman.littleguitarians.Interfaces.GetData;
import com.bmniman.littleguitarians.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import me.grantland.widget.AutofitHelper;
import me.grantland.widget.AutofitTextView;


import java.util.LinkedHashMap;
import java.util.Map;


public class DefaultExerFragment extends Fragment {

    AutofitTextView[] btn;


    Map<String, Object> data = new LinkedHashMap<>(); // To store the data got from firebase. This will store the Default exercises

    Map<String, Object> profile = new LinkedHashMap<>(); // To store the data got from firebase. This will store the profile data

    ListFragmentListener activityCommander; // Interface listener
    String notes[] = new String[100];

    String type;
    FirebaseUser user;

    String exerOrder;

    StringBuilder unLockedExers = new StringBuilder();


    int count = 0;


    public DefaultExerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            activityCommander = (ListFragmentListener) context; //  To know that activitycommander is not empty
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        linearLayout = (LinearLayout) view.findViewById(R.id.linear);


        connectDB(new GetData() { // Listener to wait till data from firebase is recieved
            @Override
            public void getData(Map<String, Object> datas, Map<String, Object> profiles) {

                data.putAll(datas); //  Copy data from datas to data
                profile.putAll(profiles);

                unLockedExers.append(profile.get("Unlocked Exercises").toString()); // Get The Unlocked Exercises Name and Store then in unLockedExers StringBuilder



                if (isAdded()) // To know if the fragment has been added,  without this if condition contect null error is received, therefore don't remove this
                {
                    activityCommander.designComplete(); // Call designComplete Methid in Activity Exercise List

                    convertToArray(); // This function will convert the exercie ORder to Array and then call the startDesign method.

                }
            }
        });

    }

    public void connectDB(final GetData listener) {


        user = FirebaseAuth.getInstance().getCurrentUser();


        DocumentReference dataRef = FirebaseFirestore.getInstance().document("Exercises/Default Exercises");

        dataRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    data = documentSnapshot.getData();


                    exerOrder = data.get("Exercise Order").toString();
                    data.remove("Exercise Order"); // Remove Exercise Order After store in another variable as this could cause problems


                    if (!data.isEmpty()) // If data is not empty
                    {

                        count++;

                        if (count == 2) // The counter variable is kept so we can know if bioth the profile and data are acquired
                        {
                            listener.getData(data, profile); // Call GetData when data is recieved from firebase
                        }


                    }

                }
            }
        });

        DocumentReference profileRef = FirebaseFirestore.getInstance().document("Users/" + user.getUid() + "/Profile Details/Data");

        profileRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    profile = documentSnapshot.getData();
                    if (!profile.isEmpty()) {
                        count++;

                        if (count == 2) // The counter variable is kept so we can know if bioth the profile and data are acquired.
                        {
                            listener.getData(data, profile); // Call GetData when profile is got from the firebase.
                        }

                    }
                }

            }
        });


    }

    public void startDesign(Map<String, Object> data, String[] exerOrderArray) {

        LinearLayout linearLayout = getView().findViewById(R.id.linearlayout);


        int count = 0; // To set the index of the button
        btn = new AutofitTextView[exerOrderArray.length]; // Make The AutofitTextView According to The Number OF Exercises


        for (String anExerOrderArray : exerOrderArray) {
            Log.d("String", anExerOrderArray);


            notes[count] = data.get(anExerOrderArray).toString();  //
            btn[count] = new AutofitTextView(getActivity());

            int width = (int) getResources().getDimension(R.dimen.exerWidth);
            int heigth = (int) getResources().getDimension(R.dimen.exerHeight);

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(width, heigth, 1.0f);
            layoutParams1.setMargins(3, 3, 3, 25); // left , top , right ,bottom

            btn[count].setLayoutParams(layoutParams1);


          btn[count].setBackground(getResources().getDrawable(R.drawable.circular_textview));




            btn[count].setId(count); // Set id to the count. Meaning number of buttons



            // Text Settings
            btn[count].setText(anExerOrderArray);  // Set Text of the button to the key of the received data.
            Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/AccidentalPresidency.ttf");
            btn[count].setTypeface(custom_font);
            btn[count].setTextSize(30);
            btn[count].setMinTextSize(18);
            btn[count].setMaxLines(2);
            btn[count].setTextColor(Color.WHITE);
            btn[count].setGravity(Gravity.CENTER);
            btn[count].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            if(isLocked(btn[count].getText().toString()))
            {
                btn[count].setBackgroundResource(R.drawable.locked);

            }
            else {
                btn[count].setBackgroundResource(R.drawable.curved_guitar);

            }


            btn[count].setPadding(5, 5, 5, 5);


            btn[count].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // Set onclick listener for every button

                    AutofitTextView b = (AutofitTextView) view;
                    int id = b.getId();
                    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                    b.startAnimation(buttonClick);

                    if (isLocked(b.getText().toString())) // If the exercise is locked don't open it
                    {
                        Log.d("Locke", "Exerrcise Is Locked");

                    } else {
                        Intent intent = new Intent(getContext(), DrawFretboard.class); // When a button is clicked call DrawFretboard activity
                        intent.putExtra("Exercise Notes", notes[id]); // Pass Exercise Notes to the activity
                        intent.putExtra("Type", "exercise");

                        if (exerOrderArray.length > id + 1) // It is not the last exercise
                        {
                            intent.putExtra("Name", btn[id + 1].getText().toString()); // Send next exercise name to be unlocked if the user reaches the said accuracy level
                        } else {

                            intent.putExtra("Name", ""); // Send enmpty intent data if it is the last exercise
                        }


                        startActivity(intent); // CAll The Activity
                    }
                }
            });


            linearLayout.addView(btn[count]);
            count++;
        }

    }

    private boolean isLocked(String exerName) // This function will check if the exercise is locked or not
    {
        int i = unLockedExers.indexOf(exerName); // Check if the exercise name exists or not

        return i == -1; // If Exerise Name Is Not Present it will return false. Meaning the exercise is locked

    }

    private void convertToArray() {
        String[] items = exerOrder.replaceAll("\\[", "").replaceAll("\\]", "").split(",");


        String[] exerOrderArray = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            try {
                exerOrderArray[i] = items[i];


            } catch (NumberFormatException ignored) {
            }
        }


        Log.d("StringValues", "h" + exerOrder);

        startDesign(data, exerOrderArray); // Put all the buttons in the fragment. Number of buttons will be equal to the number of exercises


    }

    public interface ListFragmentListener  // Listner interface to know when the data from firebase has been received
    {
        void designComplete();
    }


}