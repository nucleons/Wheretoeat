package com.restaurantfinder.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by Aditya on 11-Apr-16.
 */
public class FragmentHelper {

    public static void replaceContentFragment(int id, FragmentActivity activity, Fragment frgmt) {
        if (activity != null && !activity.isFinishing()) {
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            manager.beginTransaction().replace(id, frgmt).commitAllowingStateLoss();
        }
    }

    public static void addAndReplaceContentFragment(int id, FragmentActivity activity, Fragment frgmt) {
        if (activity != null && !activity.isFinishing()) {
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction().replace(id, frgmt).addToBackStack(null).commitAllowingStateLoss();
        }
    }

    public static void popAndReplaceContentFragment(int id, FragmentActivity activity, Fragment frgmt) {
        if (activity != null && !activity.isFinishing()) {
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.popBackStackImmediate();
            manager.beginTransaction().replace(id, frgmt).addToBackStack(null).commitAllowingStateLoss();
        }
    }
}