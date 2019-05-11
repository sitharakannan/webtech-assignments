package com.example.skamalak.eventsearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link searchTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link searchTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class searchTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Activity mActivity;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    private RequestQueue mQueue;
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mContent = new ArrayList<>();
    private Double lat;
    private Double lon;
    private ProgressBar mProgressBar;


    public searchTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment searchTab.
     */
    // TODO: Rename and change types and number of parameters
    public static searchTab newInstance(String param1, String param2) {
        searchTab fragment = new searchTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
        mActivity = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_tab, container, false);

        Spinner category_spinner = view.findViewById(R.id.categSpinner);
        ArrayAdapter<CharSequence> categ_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.category_array, android.R.layout.simple_spinner_item);
        categ_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(categ_adapter);
        Log.i("Entered", "Into Fragmnent");
        Spinner dist_spinner = view.findViewById(R.id.distunitSpinner);
        ArrayAdapter<CharSequence> distunits_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.distUnits_array, android.R.layout.simple_spinner_item);
        distunits_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dist_spinner.setAdapter(distunits_adapter);

        Button clear = (Button) view.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "TESTING BUTTON CLICK 1",Toast.LENGTH_SHORT).show();
                ((TextView) getActivity().findViewById(R.id.keywordError)).setVisibility(View.INVISIBLE);
                ((TextView) getActivity().findViewById(R.id.locinputError)).setVisibility(View.INVISIBLE);
                ((EditText) getActivity().findViewById(R.id.auto_complete_edit_text)).setText("");
                ((EditText) getActivity().findViewById(R.id.auto_complete_edit_text)).setHint("Enter keyword");
                Spinner categSpinner = getActivity().findViewById(R.id.categSpinner);
                categSpinner.setSelection(0);
                EditText distText = getActivity().findViewById(R.id.distanceEditText);
                distText.setText("");
                distText.setHint("10");
                RadioButton rb = getActivity().findViewById(R.id.radio_curloc);
                rb.setChecked(true);
                EditText otherLocInputText = getActivity().findViewById(R.id.other_loc_input);
                otherLocInputText.setText("");
                otherLocInputText.setHint("Type in the location");

            }
        });

        Button submit = (Button) view.findViewById(R.id.search);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {

                Log.d("onsubmit", "inOnSubmit");
                Boolean flag = true;
                TextView txtView = getActivity().findViewById(R.id.keywordError);
                EditText keyword = getActivity().findViewById(R.id.auto_complete_edit_text);
                Log.d("keyword", keyword.toString());
                String keywordVal = keyword.getText().toString();
                if (keywordVal.isEmpty() || (keywordVal = keywordVal.trim()).length() == 0){
                    Log.d("keywordLen", "inside if");
                    txtView.setVisibility(View.VISIBLE);
                    flag = false;
                }
                else {
                    txtView.setVisibility(View.INVISIBLE);
                }

                RadioButton rb = getActivity().findViewById(R.id.radio_otherloc);

                if (rb.isChecked()){
                    TextView locinputError = getActivity().findViewById(R.id.locinputError);
                    EditText otherlocInput = getActivity().findViewById(R.id.other_loc_input);
                    Log.d("otherLocInput", otherlocInput.toString());
                    String otherLocVal = otherlocInput.getText().toString();
                    if (otherLocVal.isEmpty() || (otherLocVal = keywordVal.trim()).length() == 0){
                        Log.d("otherLocValLen", "inside iff");
                        locinputError.setVisibility(View.VISIBLE);
                        flag = false;
                    }
                    else {
                        locinputError.setVisibility(View.INVISIBLE);
                    }
                }

                if(flag){
                    mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar3);
                    mProgressBar.setVisibility(View.VISIBLE);
                    //go to events search
                    Log.d("formVals", "validation successful");
                    Intent intent = new Intent(mActivity, SearchActivity.class);

                    EditText keywordParam = getActivity().findViewById(R.id.auto_complete_edit_text);
                    String keywordMsg = keywordParam.getText().toString();

                    EditText radiusParam = getActivity().findViewById(R.id.distanceEditText);
                    String radiusParamMsg = radiusParam.getText().toString();

                    Spinner categ_spinner = getActivity().findViewById(R.id.categSpinner);
                    String categParam = categ_spinner.getSelectedItem().toString();

                    Spinner dist_spinner = getActivity().findViewById(R.id.distunitSpinner);
                    String unitParam = dist_spinner.getSelectedItem().toString();

                    EditText otherlocInput = getActivity().findViewById(R.id.other_loc_input);
                    String locInput = otherlocInput.getText().toString();

                    RadioButton otherlocButton = getActivity().findViewById(R.id.radio_otherloc);
                    Boolean otherlocChecked = otherlocButton.isChecked();


                    Log.d("formVals", keywordMsg);
                    Log.d("formVals", radiusParamMsg);
                    Log.d("formVals", unitParam);
                    Log.d("formVals", locInput);
                    Log.d("formVals", categParam);
                    Log.d("formVals", String.valueOf(otherlocChecked));

                    Bundle extras = new Bundle();
                    extras.putString("EXTRA_KEYWORD", keywordMsg);
                    extras.putString("EXTRA_RADIUS", radiusParamMsg);
                    extras.putString("EXTRA_UNIT", unitParam);
                    extras.putString("EXTRA_LOCINPUT", locInput);
                    extras.putString("EXTRA_CATEG", categParam);
                    extras.putBoolean("EXTRA_OTHERLOCCHECKED", otherlocChecked);

                    mProgressBar.setVisibility(View.GONE);
                    intent.putExtras(extras);
                    startActivity(intent);


                }
                else{
                    Toast.makeText(getActivity(), "Please fix all fields with errors",Toast.LENGTH_SHORT).show();
                }
            }

        });


        final AppCompatAutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.auto_complete_edit_text);
        final TextView selectedText = view.findViewById(R.id.selected_item);

        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedText.setText(autoSuggestAdapter.getObject(position));
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE, AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        //TODO: uncomment for working of autocomplete
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });

        return view;

    }


    private void makeApiCall(String text) {
        ApiCall.make(getActivity().getApplicationContext(), text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                Log.d("response", response);
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject _embedded = responseObject.getJSONObject("_embedded");
                    JSONArray attractions = _embedded.getJSONArray("attractions");
                    for (int i = 0; i < attractions.length(); i++) {
                        JSONObject attr = attractions.getJSONObject(i);
                        stringList.add(attr.getString("name"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley error", (error.networkResponse.data).toString());
            }
        });
    }


}
