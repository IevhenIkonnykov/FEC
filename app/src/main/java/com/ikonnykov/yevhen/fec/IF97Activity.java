package com.ikonnykov.yevhen.fec;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.hummeling.if97.IF97;

import com.github.lguipeng.library.animcheckbox.AnimCheckBox;

import fr.ganfra.materialspinner.MaterialSpinner;

public class IF97Activity extends AppCompatActivity implements IF97_Fragment_Input.CalculationListener {

    private IF97_Fragment_Input mIF97FragmentInput;
    private IF97_Fragment_Results mIF97FragmentResults;

    private static final String INPUT_TAG = "Input";
    private static final String RESULT_TAG = "Result";

    public static final String PRESSURE = "Pressure";
    public static final String TEMPERATURE = "Temperature";
    public static final String TEMPERATURE_SATURATION = "Temperature (saturation)";
    public static final String ENTHALPY = "Enthalpy";
    public static final String ENTROPY = "Entropy";
    public static final String DENSITY = "Density";
    public static final String VOLUME = "Volume";
    public static final String KINEMATIC_VISCOSITY = "Kinematic viscosity";
    public static final String DYNAMIC_VISCOSITY = "Dynamic viscosity";
    public static final String VAPOUR_FRACTION = "Vapour fraction";
    public static final String ISOBARIC_HEAT_CAPACITY = "Isobaric heat capacity";
    public static final String ISOCHORIC_HEAT_CAPASITY = "Isochoric heat capacity";
    public static final String INTERNAL_ENERGY = "Internal energy";
    public static final String THERMAL_CONDUCTIVITY = "Thermal conductivity";
    public static final String DIELECTRIC_CONSTANT = "Dielectric constant";
    public static final String ISOBARIC_EXPANSION_COEFFICIENT = "Isobaric expansion coefficient";
    public static final String ISOTHERMAL_COMPRESSIBILITY = "Isothermal compressibility";
    public static final String PRANDTL_NUMBER = "Prandtl number";
    public static final String SPEED_OF_SOUND = "Speed of sound";
    public static final String SURFACE_TENSION = "Surface tension";
    public static final String STATE = "Physical state";
    public static final String TAG = "INFO";

    private IF97 if97;

    private final double precision = 10000000.0;

    private double p;
    private double t;
    private double ts;
    private double h;
    private double dn;
    private double vf;
    private double v;
    private double kv;
    private double dv;
    private double cp;
    private double cv;
    private double u;
    private double s;
    private double cond;
    private double dc;
    private double exp;
    private double compr;
    private double pr;
    private double w;
    private double st;
    private String state = "";
    private double firstArgument;
    private double secondArgument;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_if97);

        setTitle("IF97");

        //Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            mIF97FragmentInput = new IF97_Fragment_Input();
            //Add the IF97_Fragment_Input to the fragment_if97_container
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_if97_container, mIF97FragmentInput, INPUT_TAG);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void calculation(double unitFirst, double unitSecond,
                            boolean isSaturation, int function,
                            int unitFirstPosition, int unitSecondPosition,
                            int unitsOfMeasurement) {
        resetVariables();
        if (unitsOfMeasurement == 0){
            if97 = new IF97(IF97.UnitSystem.ENGINEERING);
        } else {
            if97 = new IF97(IF97.UnitSystem.IMPERIAL);
        }

        Bundle bundle = new Bundle();
        int func = function;

        if (isSaturation) {
            func += 6;
        }

        try {
            switch (func) {
                case 0: {
                    p = getConvertedP (unitFirst,  unitsOfMeasurement, unitFirstPosition);
                    t = getConvertedT (unitSecond, unitsOfMeasurement, unitSecondPosition);
                    bundle = getBundleForPTFunction(p, t);
                    break;
                }
                case 1: {
                    p = getConvertedP (unitFirst,  unitsOfMeasurement, unitFirstPosition);
                    h = getConvertedH(unitSecond, unitsOfMeasurement, unitSecondPosition);
                    bundle = getBundleForPHFunction(p, h);
                    break;
                }
                case 2: {
                    p = getConvertedP (unitFirst,  unitsOfMeasurement, unitFirstPosition);
                    s = getConvertedS(unitSecond, unitsOfMeasurement, unitSecondPosition);
                    bundle = getBundleForPSFunction(p, s);
                    break;
                }
                case 3: {
                    h = getConvertedH(unitFirst, unitsOfMeasurement, unitFirstPosition);
                    s = getConvertedS(unitSecond, unitsOfMeasurement, unitSecondPosition);
                    bundle = getBundleForHSFunction(h, s);
                    break;
                }
                case 4: {
                    t = getConvertedT (unitFirst, unitsOfMeasurement, unitFirstPosition);
                    s = getConvertedS(unitSecond, unitsOfMeasurement, unitSecondPosition);
                    bundle = getBundleForTSFunction(t, s);
                    break;
                }
                case 5: {
                    dn = getConvertedDn(unitFirst, unitsOfMeasurement, unitFirstPosition);
                    t = getConvertedT (unitSecond, unitsOfMeasurement, unitSecondPosition);
                    bundle = getBundleForRhoTFunction(dn, t);
                    break;
                }
                case 6: {
                    p = getConvertedP (unitFirst,  unitsOfMeasurement, unitFirstPosition);
                    bundle = getBundleForPXFunction(p, unitSecond/100.0);
                    break;
                }
                case 7: {
                    t = getConvertedT (unitFirst, unitsOfMeasurement, unitFirstPosition);
                    bundle = getBundleForTXFunction(t, unitSecond/100.0);
                    break;
                }
                default:
                    break;
            }

            mIF97FragmentResults = new IF97_Fragment_Results();
            mIF97FragmentResults.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.fragment_if97_container, mIF97FragmentResults, RESULT_TAG);
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (com.hummeling.if97.OutOfRangeException e) {
            Toast.makeText(IF97Activity.this, "Arguments are out of range",
                    Toast.LENGTH_LONG).show();
            Log.i(TAG, "Inside exception.");
            Log.i(TAG, "The limit is " + e.getLimit());
            Log.i(TAG, "The quantity is " + e.getQuantity());
            Log.i(TAG, "Exceeding value is " + e.getValue());
        }
    }

    private double getConvertedDn(double unit, int unitsOfMeasurement, int unitPosition) {
        if (unitsOfMeasurement == 0 && unitPosition == 1) {
            return preciseValue(16.0184634 * unit, precision);
        }
        if (unitsOfMeasurement == 1 && unitPosition == 0) {
            return preciseValue(0.062428 * unit, precision);
        }
        return unit;
    }

    private double getConvertedS(double unit, int unitsOfMeasurement, int unitPosition) {
        if (unitsOfMeasurement == 0 && unitPosition == 1) {
            return preciseValue(4.1868 * unit, precision);
        }
        if (unitsOfMeasurement == 1 && unitPosition == 0) {
            return preciseValue(0.2388459 * unit, precision);
        }
        return unit;
    }

    private double getConvertedH(double unit, int unitsOfMeasurement, int unitPosition) {
        if (unitsOfMeasurement == 0 && unitPosition == 1) {
            return preciseValue(2.326 * unit, precision);
        }
        if (unitsOfMeasurement == 1 && unitPosition == 0) {
            return preciseValue(0.429923 * unit, precision);
        }
        return unit;
    }

    private double getConvertedT(double unit, int unitsOfMeasurement, int unitPosition) {
        if (unitsOfMeasurement == 0 && unitPosition == 1) {
            return preciseValue((unit - 32) * 5 / 9, precision);
        }
        if (unitsOfMeasurement == 1 && unitPosition == 0) {
            return preciseValue(unit * 9 / 5 +32, precision);
        }
        return unit;
    }

    private double getConvertedP(double unit, int unitsOfMeasurement, int unitPosition) {
        if (unitsOfMeasurement == 0 && unitPosition == 1) {
            return preciseValue(0.0689476 * unit, precision);
        }
        if (unitsOfMeasurement == 1 && unitPosition == 0) {
            return preciseValue(14.5037738 * unit, precision);
        }
        return unit;
    }

    private Bundle getBundleForPTFunction(double unitFirst, double unitSecond) {
        p = unitFirst;
        t = unitSecond;
//        ts = preciseValue(if97.saturationTemperatureP(unitFirst), precision);
        h = preciseValue(if97.specificEnthalpyPT(unitFirst, unitSecond), precision);
        dn = preciseValue(if97.densityPT(unitFirst, unitSecond), precision);
        vf = preciseValue(if97.vapourFractionPH(unitFirst, h), precision)*100;
        v = preciseValue(if97.specificVolumePT(unitFirst, unitSecond), precision);
        kv = preciseValue(if97.kinematicViscosityPT(unitFirst, unitSecond), precision);
        dv = preciseValue(if97.dynamicViscosityPT(unitFirst, unitSecond), precision);
        cp = preciseValue(if97.isobaricHeatCapacityPT(unitFirst, unitSecond), precision);
        cv = preciseValue(if97.isochoricHeatCapacityPT(unitFirst, unitSecond), precision);
        u = preciseValue(if97.specificInternalEnergyPT(unitFirst, unitSecond), precision);
        s = preciseValue(if97.specificEntropyPT(unitFirst, unitSecond), precision);
        cond = preciseValue(if97.thermalConductivityPT(unitFirst, unitSecond), precision);
        dc = preciseValue(if97.dielectricConstantPT(unitFirst, unitSecond), precision);
        exp = preciseValue(if97.isobaricCubicExpansionCoefficientPT(unitFirst, unitSecond), precision);
        compr = preciseValue(if97.compressibilityPT(unitFirst, unitSecond), precision);
        pr = preciseValue(if97.PrandtlPT(unitFirst, unitSecond), precision);
        w = preciseValue(if97.speedOfSoundPT(unitFirst, unitSecond), precision);
        setState(vf);
        return getPackedBundle(p, t, ts, h, dn, vf, v, kv, dv, cp, cv, u, s, cond, dc, exp, compr, pr, w, st);
    }

    private Bundle getBundleForPHFunction(double unitFirst, double unitSecond) {
        p = unitFirst;
        t = preciseValue(if97.temperaturePH(unitFirst, unitSecond), precision);
        ts = preciseValue(if97.saturationTemperatureP(unitFirst), precision);
        h = unitSecond;
        dn = preciseValue(if97.densityPH(unitFirst, unitSecond), precision);
        vf = preciseValue(if97.vapourFractionPH(unitFirst, unitSecond), precision)*100;
        v = preciseValue(if97.specificVolumePH(unitFirst, unitSecond), precision);
        kv = preciseValue(if97.kinematicViscosityPH(unitFirst, unitSecond), precision);
        dv = preciseValue(if97.dynamicViscosityPH(unitFirst, unitSecond), precision);
        cp = preciseValue(if97.isobaricHeatCapacityPH(unitFirst, unitSecond), precision);
        cv = preciseValue(if97.isochoricHeatCapacityPH(unitFirst, unitSecond), precision);
        u = preciseValue(if97.specificInternalEnergyPH(unitFirst, unitSecond), precision);
        s = preciseValue(if97.specificEntropyPH(unitFirst, unitSecond), precision);
        cond = preciseValue(if97.thermalConductivityPH(unitFirst, unitSecond), precision);
        dc = preciseValue(if97.dielectricConstantPH(unitFirst, unitSecond), precision);
        exp = preciseValue(if97.isobaricCubicExpansionCoefficientPH(unitFirst, unitSecond), precision);
        compr = preciseValue(if97.compressibilityPH(unitFirst, unitSecond), precision);
        pr = preciseValue(if97.PrandtlPH(unitFirst, unitSecond), precision);
        w = preciseValue(if97.speedOfSoundPH(unitFirst, unitSecond), precision);
        setState(vf);
        return getPackedBundle(p, t, ts, h, dn, vf, v, kv, dv, cp, cv, u, s, cond, dc, exp, compr, pr, w, st);
    }

    private Bundle getBundleForPSFunction(double unitFirst, double unitSecond) {
        p = unitFirst;
        t = preciseValue(if97.temperaturePS(unitFirst, unitSecond), precision);
        ts = preciseValue(if97.saturationTemperatureP(unitFirst), precision);
        h = preciseValue(if97.specificEnthalpyPS(unitFirst, unitSecond), precision);
        dn = preciseValue(if97.densityPS(unitFirst, unitSecond), precision);
        vf = preciseValue(if97.vapourFractionPS(unitFirst, unitSecond), precision)*100;
        v = preciseValue(if97.specificVolumePS(unitFirst, unitSecond), precision);
        kv = preciseValue(if97.kinematicViscosityPS(unitFirst, unitSecond), precision);
        dv = preciseValue(if97.dynamicViscosityPS(unitFirst, unitSecond), precision);
        cp = preciseValue(if97.isobaricHeatCapacityPS(unitFirst, unitSecond), precision);
        cv = preciseValue(if97.isochoricHeatCapacityPS(unitFirst, unitSecond), precision);
        u = preciseValue(if97.specificInternalEnergyPS(unitFirst, unitSecond), precision);
        s = unitSecond;
        cond = preciseValue(if97.thermalConductivityPS(unitFirst, unitSecond), precision);
        dc = preciseValue(if97.dielectricConstantPS(unitFirst, unitSecond), precision);
        exp = preciseValue(if97.isobaricCubicExpansionCoefficientPS(unitFirst, unitSecond), precision);
        compr = preciseValue(if97.compressibilityPS(unitFirst, unitSecond), precision);
        pr = preciseValue(if97.PrandtlPS(unitFirst, unitSecond), precision);
        w = preciseValue(if97.speedOfSoundPS(unitFirst, unitSecond), precision);
        setState(vf);
        return getPackedBundle(p, t, ts, h, dn, vf, v, kv, dv, cp, cv, u, s, cond, dc, exp, compr, pr, w, st);
    }

    private Bundle getBundleForHSFunction(double unitFirst, double unitSecond) {
        p = preciseValue(if97.pressureHS(unitFirst, unitSecond), precision);
        t = preciseValue(if97.temperatureHS(unitFirst, unitSecond), precision);
        ts = preciseValue(if97.saturationTemperatureP(p), precision);
        h = unitFirst;
        dn = preciseValue(if97.densityHS(unitFirst, unitSecond), precision);
        vf = preciseValue(if97.vapourFractionHS(unitFirst, unitSecond), precision)*100;
        v = preciseValue(if97.specificVolumeHS(unitFirst, unitSecond), precision);
        kv = preciseValue(if97.kinematicViscosityHS(unitFirst, unitSecond), precision);
        dv = preciseValue(if97.dynamicViscosityHS(unitFirst, unitSecond), precision);
        cp = preciseValue(if97.isobaricHeatCapacityHS(unitFirst, unitSecond), precision);
        cv = preciseValue(if97.isochoricHeatCapacityHS(unitFirst, unitSecond), precision);
        u = preciseValue(if97.specificInternalEnergyHS(unitFirst, unitSecond), precision);
        s = unitSecond;
        cond = preciseValue(if97.thermalConductivityHS(unitFirst, unitSecond), precision);
        dc = preciseValue(if97.dielectricConstantHS(unitFirst, unitSecond), precision);
        exp = preciseValue(if97.isobaricCubicExpansionCoefficientHS(unitFirst, unitSecond), precision);
        compr = preciseValue(if97.compressibilityHS(unitFirst, unitSecond), precision);
        pr = preciseValue(if97.PrandtlHS(unitFirst, unitSecond), precision);
        w = preciseValue(if97.speedOfSoundHS(unitFirst, unitSecond), precision);
        setState(vf);
        return getPackedBundle(p, t, ts, h, dn, vf, v, kv, dv, cp, cv, u, s, cond, dc, exp, compr, pr, w, st);
    }

    private Bundle getBundleForTSFunction(double unitFirst, double unitSecond) {
        t = unitFirst;
        vf = preciseValue(if97.vapourFractionTS(unitFirst, unitSecond), precision)*100;
        s = unitSecond;
        setState(vf);
        return getPackedBundle(p, t, ts, h, dn, vf, v, kv, dv, cp, cv, u, s, cond, dc, exp, compr, pr, w, st);
    }

    private Bundle getBundleForRhoTFunction(double unitFirst, double unitSecond) {
        t = unitSecond;
        dn = unitFirst;
        v = preciseValue(1 / dn, precision);
        kv = preciseValue(if97.kinematicViscosityRhoT(unitFirst, unitSecond), precision);
        dv = preciseValue(if97.dynamicViscosityRhoT(unitFirst, unitSecond), precision);

        cond = preciseValue(if97.thermalConductivityRhoT(unitFirst, unitSecond), precision);
        dc = preciseValue(if97.dielectricConstantRhoT(unitFirst, unitSecond), precision);
        state = getResources().getString(R.string.n_a);
        return getPackedBundle(p, t, ts, h, dn, vf, v, kv, dv, cp, cv, u, s, cond, dc, exp, compr, pr, w, st);
    }

    private Bundle getBundleForPXFunction(double unitFirst, double unitSecond) {
        p = unitFirst;
        t = preciseValue(if97.saturationTemperatureP(unitFirst), precision);
        ts = t;
        h = preciseValue(if97.specificEnthalpyPX(unitFirst, unitSecond), precision);
        dn = preciseValue(if97.densityPX(unitFirst, unitSecond), precision);
        vf = unitSecond*100;
        v = preciseValue(if97.specificVolumePX(unitFirst, unitSecond), precision);

        kv = preciseValue(if97.kinematicViscosityPH(p, h), precision);
        dv = preciseValue(if97.dynamicViscosityPH(p, h), precision);
        cp = preciseValue(if97.isobaricHeatCapacityPH(p, h), precision);
        cv = preciseValue(if97.isochoricHeatCapacityPH(p, h), precision);
        u = preciseValue(if97.specificInternalEnergyPH(p, h), precision);

        s = preciseValue(if97.specificEntropyPX(unitFirst, unitSecond), precision);

        cond = preciseValue(if97.thermalConductivityPH(p, h), precision);
        dc = preciseValue(if97.dielectricConstantPH(p, h), precision);
        exp = preciseValue(if97.isobaricCubicExpansionCoefficientPH(p, h), precision);
        compr = preciseValue(if97.compressibilityPH(p, h), precision);
        pr = preciseValue(if97.PrandtlPH(p, h), precision);
        w = preciseValue(if97.speedOfSoundPH(p, h), precision);

        st = preciseValue(if97.surfaceTensionP(unitFirst), precision);
        setSaturationState(vf);
        return getPackedBundle(p, t, ts, h, dn, vf, v, kv, dv, cp, cv, u, s, cond, dc, exp, compr, pr, w, st);
    }

    private Bundle getBundleForTXFunction(double unitFirst, double unitSecond) {
        p = preciseValue(if97.saturationPressureT(unitFirst), precision);
        t = unitFirst;
        ts = t;
        h = preciseValue(if97.specificEnthalpyTX(unitFirst, unitSecond), precision);
        dn = preciseValue(if97.densityTX(unitFirst, unitSecond), precision);
        vf = unitSecond * 100;
        v = preciseValue(if97.specificVolumeTX(unitFirst, unitSecond), precision);

        kv = preciseValue(if97.kinematicViscosityPH(p, h), precision);
        dv = preciseValue(if97.dynamicViscosityPH(p, h), precision);
        cp = preciseValue(if97.isobaricHeatCapacityPH(p, h), precision);
        cv = preciseValue(if97.isochoricHeatCapacityPH(p, h), precision);
        u = preciseValue(if97.specificInternalEnergyPH(p, h), precision);

        s = preciseValue(if97.specificEntropyTX(unitFirst, unitSecond), precision);

        cond = preciseValue(if97.thermalConductivityPH(p, h), precision);
        dc = preciseValue(if97.dielectricConstantPH(p, h), precision);
        exp = preciseValue(if97.isobaricCubicExpansionCoefficientPH(p, h), precision);
        compr = preciseValue(if97.compressibilityPH(p, h), precision);
        pr = preciseValue(if97.PrandtlPH(p, h), precision);
        w = preciseValue(if97.speedOfSoundPH(p, h), precision);

        st = preciseValue(if97.surfaceTensionT(unitFirst), precision);
        setSaturationState(vf);
        return getPackedBundle(p, t, ts, h, dn, vf, v, kv, dv, cp, cv, u, s, cond, dc, exp, compr, pr, w, st);
    }


    private Bundle getPackedBundle(double p, double t, double ts, double h, double dn,
                                   double vf, double v, double kv, double dv, double cp,
                                   double cv, double u, double s, double cond, double dc,
                                   double exp, double compr, double pr, double w, double st) {
        Bundle bundle = new Bundle();
        bundle.putDouble(PRESSURE, p);
        bundle.putDouble(TEMPERATURE, t);
        bundle.putDouble(TEMPERATURE_SATURATION, ts);
        bundle.putDouble(ENTHALPY, h);
        bundle.putDouble(DENSITY, dn);
        bundle.putDouble(VAPOUR_FRACTION, vf);
        bundle.putDouble(VOLUME, v);
        bundle.putDouble(KINEMATIC_VISCOSITY, kv);
        bundle.putDouble(DYNAMIC_VISCOSITY, dv);
        bundle.putDouble(ISOBARIC_HEAT_CAPACITY, cp);
        bundle.putDouble(ISOCHORIC_HEAT_CAPASITY, cv);
        bundle.putDouble(INTERNAL_ENERGY, u);
        bundle.putDouble(ENTROPY, s);
        bundle.putDouble(THERMAL_CONDUCTIVITY, cond);
        bundle.putDouble(DIELECTRIC_CONSTANT, dc);
        bundle.putDouble(ISOBARIC_EXPANSION_COEFFICIENT, exp);
        bundle.putDouble(ISOTHERMAL_COMPRESSIBILITY, compr);
        bundle.putDouble(PRANDTL_NUMBER, pr);
        bundle.putDouble(SPEED_OF_SOUND, w);
        bundle.putDouble(SURFACE_TENSION, st);
        bundle.putString(STATE, state);

        return bundle;
    }

    private double preciseValue(double value, double precision) {
        return Math.round(value * precision) / precision;
    }

    //We have to handle the popping up of the back stack upon Back button pressed
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void resetVariables() {
        p = -1;
        t = -1;
        ts = -1;
        h = -1;
        dn = -1;
        vf = -1;
        v = -1;
        kv = -1;
        dv = -1;
        cp = -1;
        cv = -1;
        u = -1;
        s = -1;
        cond = -1;
        dc = -1;
        exp = -1;
        compr = -1;
        pr = -1;
        w = -1;
        st = -1;
        state = "";
    }

    private void setSaturationState (double x){
        if (x == 0){
            state = getResources().getString(R.string.saturated_liquid);
        }
        if (x == 100){
            state = getResources().getString(R.string.saturated_vapour);
        }
        if (x > 0 && x < 100){
            state = getResources().getString(R.string.liquid_vapour_mixture);
        }
    }

    private void setState (double x){
        if (x == 0){
            state = getResources().getString(R.string.liquid);
        }
        if (x == 100){
            state = getResources().getString(R.string.vapour);
        }
        if (x > 0 && x < 100){
            state = getResources().getString(R.string.liquid_vapour_mixture);
        }
    }
}


