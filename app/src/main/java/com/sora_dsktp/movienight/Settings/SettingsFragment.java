package com.sora_dsktp.movienight.Settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;


import com.sora_dsktp.movienight.R;

/**
 * Created by SoRa-DSKTP on 5/3/2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat
{

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_fragment_items);
    }
}
