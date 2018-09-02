package com.bmniman.littleguitarians.Designs.Fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bmniman.littleguitarians.Designs.CustomExercise;
import com.bmniman.littleguitarians.Designs.DrawFretboard;
import com.bmniman.littleguitarians.Interfaces.GetData;
import com.bmniman.littleguitarians.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import me.samthompson.bubbleactions.BubbleActions;
import me.samthompson.bubbleactions.MenuCallback;

import static com.facebook.FacebookSdk.getApplicationContext;


public class CustomExerFragment extends Fragment {

    Button[] btn ;
    Map<String, Object> data = new LinkedHashMap<>(); // To store the data got from firebase

    ListFragmentListener activityCommander; // Interface listener
    String notes [] = new String[100];

    ImageView addBtn;
    String exerOrder="";

    public CustomExerFragment() {
        // Required empty public constructor
    }

    public interface ListFragmentListener  // Listner interface to know when the data from firebase has been received
    {
        void designComplete();
    }


    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try
        {
            activityCommander = (ListFragmentListener) context; //  To know that activitycommander is not empty
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString());
        }
    }





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_custom_exer, container, false);







        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        linearLayout = (LinearLayout) view.findViewById(R.id.linear);



        connectDB(new GetData() { // Listener to wait till data from firebase is recieved
            @Override
            public void getData(Map<String, Object> datas , Map<String ,Object> profile) {

                data.putAll(datas); //  Copy data from datas to data



                Log.d("First", String.valueOf(data));

                if(isAdded()) // To know if the fragment has been added,  without this if condition contect null error is received, therefore don't remove this
                {
                    activityCommander.designComplete(); // Call designComplete Methid in Activity Exercise List


                    convertToArray(); // Put all the buttons in the fragment. Number of buttons will be equal to the number of exercises


                }
            }
        });

    }

    public void connectDB(final GetData listener) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference docref = FirebaseFirestore.getInstance().document("Users/" + user.getUid() + "/Custom Exercises/Notes");


        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) { // To check if document exists in the DB
                    data = documentSnapshot.getData();

                    exerOrder = data.get("CustomExer Order").toString();
                    data.remove("CustomExer Order"); // Remove Exercise Order Key As It Does Not Contain Any Notes.

                   /* if (!data.isEmpty()) // If data is not empty
                    {*/

                    listener.getData(data,null); // Call GetData when data is recieved from firebase

                    //}

                }

                else { // If the document does not exist then no button will be created as null value will be passed
                    listener.getData(data,null);
                }
            }
        });

    }

    public void startDesign(Map<String, Object> data, String[] exerOrderArray) {

        LinearLayout linearLayout = getView().findViewById(R.id.linearlayout);



        addBtn =  getView().findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getContext(), CustomExercise.class); // When a button is clicked call DrawFretboard activity
                intent.putExtra("Exercise Order" ,exerOrder);

                startActivity(intent); // CAll The Activity
                // finish();

            }
        });


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int count = 0; // To set the index of the button





        if(!data.isEmpty())
        {
            Log.d("Lenght" , Arrays.toString(exerOrderArray));


            btn = new Button[exerOrderArray.length];
            for (String anExerOrderArray : exerOrderArray) {
                notes[count] = data.get(anExerOrderArray).toString();
                btn[count] = new Button(getActivity());
                btn[count].setText(anExerOrderArray);
                btn[count].setId(count);


                btn[count].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Button b = (Button) view;
                        int id = b.getId();

                        Intent intent = new Intent(getContext(), DrawFretboard.class); // When a button is clicked call DrawFretboard activity
                        intent.putExtra("Exercise Notes", notes[id]); // Pass Exercise Name to the activity
                        intent.putExtra("Type", "exercise");
                        startActivity(intent); // CAll The Activity

                    }
                });


                btn[count].setOnLongClickListener(new View.OnLongClickListener() { // Edit And Delete Option Will Be Shown On Long Press
                    @Override
                    public boolean onLongClick(View v) {
                        Button b = (Button) v;

                        String text = b.getText().toString();
                        showMenu(v, text, b.getId());
                        return true;
                    }
                });

                linearLayout.addView(btn[count], layoutParams);
                count++;
            }
        }


    }

    private void showMenu(final View v, final String text, final int id)
    {
        BubbleActions.on(v)
                .fromMenu(R.menu.customexercise, new MenuCallback() {
                    @Override
                    public void doAction(int itemId) {
                        switch (itemId) {
                            case R.id.edit:
                                Toast.makeText(v.getContext(), "Edit pressed!", Toast.LENGTH_SHORT).show();

                                edit(v,text); // Exercise To Edit

                                break;
                            case R.id.delete:
                                Toast.makeText(v.getContext(), "Delete pressed!", Toast.LENGTH_SHORT).show();

                                delete(text,id); // Exercise Name To Delete
                                break;
                        }
                    }
                })
                .show();


    }

    private void edit(View v, String text)
    {

        String notes  = (String) data.get(text); // Getting data according to the key Value
        Intent intent = new Intent(getContext(),CustomExercise.class); // When a button is clicked call DrawFretboard activity
        intent.putExtra("Notes", notes); // Pass Exercise Name to the activity
        intent.putExtra("Type" ,"Edit");
        intent.putExtra("Name" ,text);
        intent.putExtra("Exercise Order",exerOrder);
        startActivity(intent); // CAll The Activity

    }

    private void delete(final String exer , final int id)
    {


        new FancyAlertDialog.Builder(getActivity())
                .setTitle("Are You Sure You Want To Delete The Exercise")
                .setBackgroundColor(Color.parseColor("#303F9F"))  //Don't pass R.color.colorvalue
                .setMessage("Do you really want to Exit ?")
                .setNegativeBtnText("No")
                .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Yes")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.ic_star_border_black_24dp, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {  // Delete If Positive Button Clicked . i.e "Yes"
                    @Override
                    public void OnClick() {



                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        assert user != null;
                        DocumentReference docRef = FirebaseFirestore.getInstance().document("Users/" + user.getUid() + "/Custom Exercises/Notes");



                        Map<String,Object> updates = new HashMap<>();
                        updates.put(exer, FieldValue.delete());
                        updates.put("CustomExer Order" ,deleteFromExerciseOrder(exer));

                        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                btn[id].setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Exercise Successfully Deleted",Toast.LENGTH_SHORT).show();
                            }
                        });
                        // ...


                        updates.clear();

                        Toast.makeText(getApplicationContext(),"Rate",Toast.LENGTH_SHORT).show();
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {

                    }
                })
                .build();


    }

    private String deleteFromExerciseOrder(String exerName)
    {
        StringBuilder order = new StringBuilder();
        order.append(exerOrder);




        int i = order.indexOf(exerName); // Get the position of the note to delet in the  string builder

        if(i!=-1)
        {

            if(i==0) // If it is the first note entered then delete the note and and the comma after it.
            {
                order.delete(i, i + order.length()+1);  //  Start delete from the beginning of the not postion till the of of the note position

            }

            else {
                order.delete(i-1, i + order.length());  //  Start delete from the beginning of the not postion till the of of the note position


            }
        }

        exerOrder = order.toString(); // Updating exerorder Value

        return order.toString();


    }

    private void convertToArray()
    {

        String[] items = exerOrder.replaceAll("\\[", "").replaceAll("\\]", "").split(",");


        String [] exerOrderArray = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            try {
                exerOrderArray[i] = items[i];


            } catch (NumberFormatException ignored) {
            }
        }



        Log.d("StringValues" , "h"+ exerOrder);

        startDesign(data , exerOrderArray); // Put all the buttons in the fragment. Number of buttons will be equal to the number of exercises


    }


}