package com.ws.mesh.incores2.utils;

import com.ws.mesh.incores2.constant.PageId;
import com.ws.mesh.incores2.view.base.BaseFragment;
import com.ws.mesh.incores2.view.fragment.BreathFragment;
import com.ws.mesh.incores2.view.fragment.EditFragment;
import com.ws.mesh.incores2.view.fragment.music.MusicFragment;
import com.ws.mesh.incores2.view.fragment.NullFragment;
import com.ws.mesh.incores2.view.fragment.TimingFragment;

public class FragmentFactory {
    public static BaseFragment create(int pageId) {
        switch (pageId) {
            case PageId.BREATH:
                return new BreathFragment();
            case PageId.MUSIC:
                return new MusicFragment();
            case PageId.TIMING:
                return new TimingFragment();
            case PageId.EDIT_DEVICE:
            case PageId.EDIT_ZONE:
                return new EditFragment();
            default:
                return new NullFragment();
        }
    }
}
