package com.ikonnykov.yevhen.fec;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class IF97_Fragment_Results extends Fragment {
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_if97_results, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if (sharedPreferences.getInt(CalculationChoiceActivity.UNITS, 0) == 1){
            setUnitsOfMeasurements(v);
        }

        TextView state = (TextView)v.findViewById(R.id.stateResultTextView);
        TextView p = (TextView)v.findViewById(R.id.pressureResultTextView);
        TextView t = (TextView)v.findViewById(R.id.temperatureResultTextView);
        TextView ts = (TextView)v.findViewById(R.id.temperatureSatResultTextView);
        TextView h = (TextView)v.findViewById(R.id.enthalpyResultTextView);
        TextView dn = (TextView)v.findViewById(R.id.densityResultTextView);
        TextView vf = (TextView)v.findViewById(R.id.vapourResultTextView);
        TextView vol = (TextView)v.findViewById(R.id.volumeResultTextView);
        TextView kv = (TextView)v.findViewById(R.id.kinViscosityResultTextView);
        TextView dv = (TextView)v.findViewById(R.id.dynViscosityResultTextView);
        TextView cp = (TextView)v.findViewById(R.id.cpResultTextView);
        TextView cv = (TextView)v.findViewById(R.id.cvResultTextView);
        TextView u = (TextView)v.findViewById(R.id.uResultTextView);
        TextView s = (TextView)v.findViewById(R.id.sResultTextView);
        TextView cond = (TextView)v.findViewById(R.id.tcResultTextView);
        TextView dc = (TextView)v.findViewById(R.id.dcResultTextView);
        TextView exp = (TextView)v.findViewById(R.id.expResultTextView);
        TextView compr = (TextView)v.findViewById(R.id.comprResultTextView);
        TextView pr = (TextView)v.findViewById(R.id.prResultTextView);
        TextView w = (TextView)v.findViewById(R.id.wResultTextView);
        TextView st = (TextView)v.findViewById(R.id.stResultTextView);

        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.getDouble(IF97Activity.PRESSURE)!=-1) {
                p.setText(String.valueOf(bundle.getDouble(IF97Activity.PRESSURE)));
            } else {
                p.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.TEMPERATURE)!=-1) {
                t.setText(String.valueOf(bundle.getDouble(IF97Activity.TEMPERATURE)));
            } else {
                t.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.TEMPERATURE_SATURATION)!=-1) {
                ts.setText(String.valueOf(bundle.getDouble(IF97Activity.TEMPERATURE_SATURATION)));
            } else {
                ts.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.ENTHALPY)!=-1) {
                h.setText(String.valueOf(bundle.getDouble(IF97Activity.ENTHALPY)));
            } else {
                h.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.DENSITY)!=-1) {
                dn.setText(String.valueOf(bundle.getDouble(IF97Activity.DENSITY)));
            } else {
                dn.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.VAPOUR_FRACTION)!=-1) {
                vf.setText(String.valueOf(bundle.getDouble(IF97Activity.VAPOUR_FRACTION)));
            } else {
                vf.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.VOLUME)!=-1) {
                vol.setText(String.valueOf(bundle.getDouble(IF97Activity.VOLUME)));
            } else {
                vol.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.KINEMATIC_VISCOSITY)!=-1) {
                kv.setText(String.valueOf(bundle.getDouble(IF97Activity.KINEMATIC_VISCOSITY)));
            } else {
                kv.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.DYNAMIC_VISCOSITY)!=-1) {
                dv.setText(String.valueOf(bundle.getDouble(IF97Activity.DYNAMIC_VISCOSITY)));
            } else {
                dv.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.ISOBARIC_HEAT_CAPACITY)!=-1) {
                cp.setText(String.valueOf(bundle.getDouble(IF97Activity.ISOBARIC_HEAT_CAPACITY)));
            } else {
                cp.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.ISOCHORIC_HEAT_CAPASITY)!=-1) {
                cv.setText(String.valueOf(bundle.getDouble(IF97Activity.ISOCHORIC_HEAT_CAPASITY)));
            } else {
                cv.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.INTERNAL_ENERGY)!=-1) {
                u.setText(String.valueOf(bundle.getDouble(IF97Activity.INTERNAL_ENERGY)));
            } else {
                u.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.ENTROPY)!=-1) {
                s.setText(String.valueOf(bundle.getDouble(IF97Activity.ENTROPY)));
            } else {
                s.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.THERMAL_CONDUCTIVITY)!=-1) {
                cond.setText(String.valueOf(bundle.getDouble(IF97Activity.THERMAL_CONDUCTIVITY)));
            } else {
                cond.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.DIELECTRIC_CONSTANT)!=-1) {
                dc.setText(String.valueOf(bundle.getDouble(IF97Activity.DIELECTRIC_CONSTANT)));
            } else {
                dc.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.ISOBARIC_EXPANSION_COEFFICIENT)!=-1) {
                exp.setText(String.valueOf(bundle.getDouble(IF97Activity.ISOBARIC_EXPANSION_COEFFICIENT)));
            } else {
                exp.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.ISOTHERMAL_COMPRESSIBILITY)!=-1) {
                compr.setText(String.valueOf(bundle.getDouble(IF97Activity.ISOTHERMAL_COMPRESSIBILITY)));
            } else {
                compr.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.PRANDTL_NUMBER)!=-1) {
                pr.setText(String.valueOf(bundle.getDouble(IF97Activity.PRANDTL_NUMBER)));
            } else {
                pr.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.SPEED_OF_SOUND)!=-1) {
                w.setText(String.valueOf(bundle.getDouble(IF97Activity.SPEED_OF_SOUND)));
            } else {
                w.setText(getResources().getString(R.string.n_a));
            }

            if (bundle.getDouble(IF97Activity.SURFACE_TENSION)!=-1) {
                st.setText(String.valueOf(bundle.getDouble(IF97Activity.SURFACE_TENSION)));
            } else {
                st.setText(getResources().getString(R.string.n_a));
            }

            state.setText(bundle.getString(IF97Activity.STATE));
        }

        return v;
    }

    private void setUnitsOfMeasurements (View v){
        TextView p = (TextView)v.findViewById(R.id.pressureUnitTextView);
        p.setText(R.string.p_im);
        TextView t = (TextView)v.findViewById(R.id.temperatureUnitTextView);
        t.setText(R.string.t_im);
        TextView ts = (TextView)v.findViewById(R.id.temperatureSatUnitTextView);
        ts.setText(R.string.t_im);
        TextView h = (TextView)v.findViewById(R.id.enthalpyUnitTextView);
        h.setText(R.string.en_im);
        TextView dn = (TextView)v.findViewById(R.id.densityUnitTextView);
        dn.setText(R.string.dn_im);
        TextView vf = (TextView)v.findViewById(R.id.vapourUnitTextView);
        vf.setText(R.string.vf_im);
        TextView vol = (TextView)v.findViewById(R.id.volumeUnitTextView);
        vol.setText(R.string.v_im);
        TextView kv = (TextView)v.findViewById(R.id.kinViscosityUnitTextView);
        kv.setText(R.string.kv_im);
        TextView dv = (TextView)v.findViewById(R.id.dynViscosityUnitTextView);
        dv.setText(R.string.dv_im);
        TextView cp = (TextView)v.findViewById(R.id.cpUnitTextView);
        cp.setText(R.string.hc_s_im);
        TextView cv = (TextView)v.findViewById(R.id.cvUnitTextView);
        cv.setText(R.string.hc_s_im);
        TextView u = (TextView)v.findViewById(R.id.uUnitTextView);
        u.setText(R.string.en_im);
        TextView s = (TextView)v.findViewById(R.id.sUnitTextView);
        s.setText(R.string.hc_s_im);
        TextView cond = (TextView)v.findViewById(R.id.tcUnitTextView);
        cond.setText(R.string.tc_im);
        TextView exp = (TextView)v.findViewById(R.id.expUnitTextView);
        exp.setText(R.string.exp_im);
        TextView compr = (TextView)v.findViewById(R.id.comprUnitTextView);
        compr.setText(R.string.compr_im);
        TextView w = (TextView)v.findViewById(R.id.wUnitTextView);
        w.setText(R.string.w_im);
        TextView st = (TextView)v.findViewById(R.id.stUnitTextView);
        st.setText(R.string.st_im);
    }
}
