/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;


import com.sora_dsktp.movienight.R;

/**
 This file created by Georgios Kostogloudis
 and was last modified on 5/3/2018.
 The name of the project is MovieNight and it was created as part of
 UDACITY ND programm.
 */

/**
 * This class is a settings Fragment that
 * has reference to all the preference items from the settings layout
 */
public class SettingsFragment extends PreferenceFragmentCompat
{

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_fragment_items);
    }
}
