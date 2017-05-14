package ru.nobird.github.search.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

import ru.nobird.github.search.R;

public class FragmentMgr {
    private WeakReference<AppCompatActivity> appReference;
    private static FragmentMgr instance;

    private FragmentMgr() {
        appReference = new WeakReference<>(null);
    }

    public void attach(final AppCompatActivity context) {
        appReference = new WeakReference<>(context);
    }

    public synchronized static void init() {
        if (instance == null) {
            instance = new FragmentMgr();
        }
    }

    public synchronized static FragmentMgr getInstance() {
        return instance;
    }

    public void addFragment(final int rootID, final Fragment fragment, final boolean backStack) {
        final AppCompatActivity app = appReference.get();
        if (app == null) return;

        final FragmentTransaction transaction = app.getSupportFragmentManager().beginTransaction();

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add((rootID == 0 ? R.id.activity_main_fragment_container : rootID), fragment, fragment.getTag());
        if (backStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void replaceFragment(final int rootID, final Fragment fragment, final boolean backStack) {
        final AppCompatActivity app = appReference.get();
        if (app == null) return;

        final FragmentTransaction transaction = app.getSupportFragmentManager().beginTransaction();

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace((rootID == 0 ? R.id.activity_main_fragment_container : rootID), fragment, fragment.getTag());
        if (backStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }


}
