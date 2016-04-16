package com.ikonnykov.yevhen.fec;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.lguipeng.library.animcheckbox.AnimCheckBox;

import fr.ganfra.materialspinner.MaterialSpinner;
import me.zhanghai.android.materialedittext.MaterialEditText;
import mehdi.sakout.fancybuttons.FancyButton;


public class IF97_Fragment_Input extends Fragment {

    private TextView mUnitFirst;
    private TextView mUnitSecond;

    private MaterialSpinner mSpinnerArguments;
    private MaterialSpinner mSpinnerUnitFirst;
    private MaterialSpinner mSpinnerUnitSecond;

    private AnimCheckBox mCheckbox;

    private MaterialEditText mUnitFirstEditText;
    private MaterialEditText mUnitSecondEditText;

    private String[] ITEMS_ARGUMENTS_FULL_LIST;
    private String[] ITEMS_ARGUMENTS_SHORT_LIST;
    private String[] ITEMS_PRESSURE;
    private String[] ITEMS_TEMPERATURE;
    private String[] ITEMS_ENTHALPY;
    private String[] ITEMS_ENTROPY;
    private String[] ITEMS_DENSITY;
    private String[] ITEMS_VAPOUR;

    private boolean checkBoxState = false;

    private int spinnerArgumentsPosition = 0;
    private int spinnerUnitFirstPosition = -1;
    private int spinnerUnitSecondPosition = 0;

    private String unitFirstValue = "";
    private String unitSecondValue = "";

    private ArrayAdapter<String> adapterArguments;
    private ArrayAdapter<String> adapterUnitFirst;
    private ArrayAdapter<String> adapterUnitSecond;

    private int unitFirstType = R.string.pressure;
    private int unitSecondType = R.string.temperature;

    private static final String CHECK_BOX = "CHECK_BOX";
    private static final String TAG = "INFO";

    private SharedPreferences sharedPreferences;
    private int unitsOfMeasurement;

    public interface CalculationListener {
        public void calculation(double unitFirst, double unitSecond,
                                boolean isSaturation, int function,
                                int unitFirstPosition, int unitSecondPosition,
                                int unitsOfMeasurement);
    }

    private CalculationListener mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (CalculationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CalculationListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "Inside onActivityCreated.");
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        unitsOfMeasurement = sharedPreferences.getInt(CalculationChoiceActivity.UNITS, 0);
        if (spinnerUnitFirstPosition == -1) {
            switch (unitsOfMeasurement) {
                case 0: {
                    spinnerUnitFirstPosition = 0;
                    spinnerUnitSecondPosition = 0;
                    break;
                }
                case 1: {
                    spinnerUnitFirstPosition = 1;
                    spinnerUnitSecondPosition = 1;
                    break;
                }
                default:
                    break;
            }
        }
        mCheckbox.setChecked(checkBoxState);
        if (checkBoxState) {
            mSpinnerArguments.setAdapter(getArrayAdapter(adapterArguments, R.string.shortListArgs));
        } else {
            mSpinnerArguments.setAdapter(getArrayAdapter(adapterArguments, R.string.longListArgs));
        }
        mSpinnerArguments.setSelection(spinnerArgumentsPosition);
        Log.i(TAG, "Inside onActivityCreated. spinnerArgumentsPosition " + spinnerArgumentsPosition);
        Log.i(TAG, "Inside onActivityCreated. spinnerUnitFirstPosition " + spinnerUnitFirstPosition);
        mSpinnerUnitFirst.setSelection(spinnerUnitFirstPosition, false);
//        mSpinnerUnitFirst.post(new Runnable() {
//            @Override
//            public void run() {
//                mSpinnerUnitFirst.setSelection(spinnerUnitFirstPosition);
//            }
//        });

        Log.i(TAG, "Inside onActivityCreated. spinnerUnitSecondPosition " + spinnerUnitSecondPosition);
        mSpinnerUnitSecond.setSelection(spinnerUnitSecondPosition, false);
//        mSpinnerUnitSecond.post(new Runnable() {
//            @Override
//            public void run() {
//                mSpinnerUnitSecond.setSelection(spinnerUnitSecondPosition);
//            }
//        });

        mUnitFirstEditText.setText(unitFirstValue);
        mUnitSecondEditText.setText(unitSecondValue);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_if97_input_data, container, false);

        mUnitFirst = (TextView) v.findViewById(R.id.textViewUnitFirst);
        mUnitSecond = (TextView) v.findViewById(R.id.textViewUnitSecond);

        mUnitFirstEditText = (MaterialEditText) v.findViewById(R.id.editTextUnitFirst);
        mUnitSecondEditText = (MaterialEditText) v.findViewById(R.id.editTextUnitSecond);

        FancyButton calculateButton = (FancyButton) v.findViewById(R.id.btnCalculate);

        Resources res = getResources();
        ITEMS_ARGUMENTS_FULL_LIST = res.getStringArray(R.array.arguments_array);
        ITEMS_ARGUMENTS_SHORT_LIST = res.getStringArray(R.array.arguments_array_saturation);
        ITEMS_PRESSURE = res.getStringArray(R.array.pressure_array);
        ITEMS_TEMPERATURE = res.getStringArray(R.array.temperature_array);
        ITEMS_ENTHALPY = res.getStringArray(R.array.enthalpy_array);
        ITEMS_ENTROPY = res.getStringArray(R.array.entropy_array);
        ITEMS_DENSITY = res.getStringArray(R.array.density_array);
        ITEMS_VAPOUR = res.getStringArray(R.array.vapour_array);


        mSpinnerArguments = (MaterialSpinner) v.findViewById(R.id.spinnerFunction);
        mSpinnerUnitFirst = (MaterialSpinner) v.findViewById(R.id.spinnerUnitFirst);
        mSpinnerUnitSecond = (MaterialSpinner) v.findViewById(R.id.spinnerUnitSecond);
        mSpinnerArguments.setAdapter(getArrayAdapter(adapterArguments, R.string.longListArgs));
        mSpinnerArguments.setSelection(spinnerArgumentsPosition, true);
        mSpinnerUnitFirst.setSelection(spinnerUnitFirstPosition, true);
        mSpinnerUnitSecond.setSelection(spinnerUnitSecondPosition);


        //Arguments change
        mSpinnerArguments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getCount() == 8) {
                    Log.i(TAG, "Got here after backstack");
                    switch (position) {
                        case 0: {
                            changeInputInfo(R.string.pressure, R.string.temperature);
                            break;
                        }
                        case 1: {
                            changeInputInfo(R.string.pressure, R.string.enthalpy);
                            break;
                        }
                        case 2: {
                            changeInputInfo(R.string.pressure, R.string.entropy);
                            break;
                        }
                        case 3: {
                            changeInputInfo(R.string.enthalpy, R.string.entropy);
                            break;
                        }
                        case 4: {
                            changeInputInfo(R.string.temperature, R.string.entropy);
                            break;
                        }
                        case 5: {
                            changeInputInfo(R.string.density, R.string.temperature);
                            break;
                        }
                        case 6: {
                            changeInputInfo(R.string.pressure, R.string.vapour_fraction);
                            break;
                        }
                        case 7: {
                            changeInputInfo(R.string.temperature, R.string.vapour_fraction);
                            break;
                        }
                        default:
                            break;
                    }
                } else {
                    switch (position) {
                        case 0: {
                            changeInputInfo(R.string.pressure, R.string.vapour_fraction);
                            break;
                        }
                        case 1: {
                            changeInputInfo(R.string.temperature, R.string.vapour_fraction);
                            break;
                        }
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //It is not used yet
            }
        });

        mSpinnerUnitFirst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Inside  mSpinnerUnitFirst.OnItemSelectedListener");
                spinnerUnitFirstPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //It is not used yet
            }
        });

        mSpinnerUnitSecond.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Inside mSpinnerUnitSecond.OnItemSelectedListener");
                spinnerUnitSecondPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //It is not used yet
            }
        });

        //Saturation CheckBox
        mCheckbox = (AnimCheckBox) v.findViewById(R.id.checkBoxSaturation);

        //Saturation state
        mCheckbox.setOnCheckedChangeListener(new AnimCheckBox.OnCheckedChangeListener() {
            @Override
            public void onChange(boolean checked) {
                if (mCheckbox.isChecked()) {
                    mSpinnerArguments.setAdapter(getArrayAdapter(adapterArguments, R.string.shortListArgs));
                    changeInputInfo(R.string.pressure, R.string.vapour_fraction);
                } else {
                    mSpinnerArguments.setAdapter(getArrayAdapter(adapterArguments, R.string.longListArgs));
                    changeInputInfo(R.string.pressure, R.string.temperature);
                }
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputFields()) {
                    mCallback.calculation(Double.parseDouble(mUnitFirstEditText.getText().toString()),
                            Double.parseDouble(mUnitSecondEditText.getText().toString()),
                            mCheckbox.isChecked(), mSpinnerArguments.getSelectedItemPosition(),
                            mSpinnerUnitFirst.getSelectedItemPosition(),
                            mSpinnerUnitSecond.getSelectedItemPosition(),
                            unitsOfMeasurement);
                }

            }
        });

        return v;
    }

    private ArrayAdapter<String> getArrayAdapter(ArrayAdapter<String> adapter, int argument) {
        if (adapter == null) {
            adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item);
        }
        adapter.clear();
        switch (argument) {
            case R.string.pressure: {
                adapter.addAll(ITEMS_PRESSURE);
                break;
            }
            case R.string.temperature: {
                adapter.addAll(ITEMS_TEMPERATURE);
                break;
            }
            case R.string.enthalpy: {
                adapter.addAll(ITEMS_ENTHALPY);
                break;
            }
            case R.string.entropy: {
                adapter.addAll(ITEMS_ENTROPY);
                break;
            }
            case R.string.density: {
                adapter.addAll(ITEMS_DENSITY);
                break;
            }
            case R.string.vapour_fraction: {
                adapter.addAll(ITEMS_VAPOUR);
                break;
            }
            case R.string.shortListArgs: {
                adapter.addAll(ITEMS_ARGUMENTS_SHORT_LIST);
                break;
            }
            case R.string.longListArgs: {
                adapter.addAll(ITEMS_ARGUMENTS_FULL_LIST);
                break;
            }
            default:
                break;
        }
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        return adapter;
    }

    private void changeInputInfo(int firstUnit, int secondUnit) {
        Log.i(TAG, "Got into changeInputInfo");
        mUnitFirst.setText(firstUnit);
        mUnitSecond.setText(secondUnit);
        mSpinnerUnitFirst.setAdapter(getArrayAdapter(adapterUnitFirst, firstUnit));
        mSpinnerUnitFirst.setSelection(unitsOfMeasurement);
        mSpinnerUnitSecond.setAdapter(getArrayAdapter(adapterUnitSecond, secondUnit));
        mSpinnerUnitSecond.setSelection(unitsOfMeasurement);
    }

    private boolean checkInputFields() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mUnitFirstEditText.getText().toString().length() == 0
                || mUnitFirstEditText.getText().toString().equals(".")) {
            mUnitFirstEditText.requestFocus();
            imm.showSoftInput(mUnitFirstEditText, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (mUnitSecondEditText.getText().length() == 0
                || mUnitSecondEditText.getText().toString().equals(".")) {
            mUnitSecondEditText.requestFocus();
            imm.showSoftInput(mUnitFirstEditText, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        checkBoxState = mCheckbox.isChecked();
        spinnerArgumentsPosition = mSpinnerArguments.getSelectedItemPosition();
//        spinnerUnitFirstPosition = mSpinnerUnitFirst.getSelectedItemPosition();
//        spinnerUnitSecondPosition = mSpinnerUnitSecond.getSelectedItemPosition();

        unitFirstValue = mUnitFirstEditText.getText().toString();
        unitSecondValue = mUnitSecondEditText.getText().toString();

        Log.i(TAG, "Inside onPause.");
        Log.i(TAG, "spinnerUnitFirstPosition " + spinnerUnitFirstPosition);
        Log.i(TAG, "spinnerUnitSecondPosition " + spinnerUnitSecondPosition);
    }
}
