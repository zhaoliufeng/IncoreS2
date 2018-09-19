package com.ws.mesh.incores2.utils;

import com.ws.mesh.incores2.constant.PageId;
import com.ws.mesh.incores2.view.base.BaseFragment;
import com.ws.mesh.incores2.view.fragment.action.BreathFragment;
import com.ws.mesh.incores2.view.fragment.action.KotlinEditFragment;
import com.ws.mesh.incores2.view.fragment.setting.AboutUsFragment;
import com.ws.mesh.incores2.view.fragment.setting.NetworkListFragment;
import com.ws.mesh.incores2.view.fragment.NullFragment;
import com.ws.mesh.incores2.view.fragment.scene.SceneAddDeviceFragment;
import com.ws.mesh.incores2.view.fragment.scene.SceneAddFragment;
import com.ws.mesh.incores2.view.fragment.scene.SceneAddTimingFragment;
import com.ws.mesh.incores2.view.fragment.setting.SettingFragment;
import com.ws.mesh.incores2.view.fragment.setting.ShareManageFragment;
import com.ws.mesh.incores2.view.fragment.timing.TimingEditFragment;
import com.ws.mesh.incores2.view.fragment.timing.TimingEventsFragment;
import com.ws.mesh.incores2.view.fragment.timing.TimingFragment;
import com.ws.mesh.incores2.view.fragment.action.ZoneDeviceManageFragment;
import com.ws.mesh.incores2.view.fragment.music.MusicFragment;
import com.ws.mesh.incores2.view.fragment.share.ChooseShareMeshFragment;
import com.ws.mesh.incores2.view.fragment.share.ShareReceiveFragment;
import com.ws.mesh.incores2.view.fragment.share.ShareChooseRoleFragment;
import com.ws.mesh.incores2.view.fragment.share.ShareUserFragment;

public class FragmentFactory {
    public static BaseFragment create(int pageId) {
        switch (pageId) {
            case PageId.BREATH:
                return new BreathFragment();
            case PageId.MUSIC:
                return new MusicFragment();
            case PageId.TIMING:
                return new TimingFragment();
            case PageId.SETTING:
                return new SettingFragment();
            case PageId.ADD_TIMING_EVENT:
                return new TimingEventsFragment();
            case PageId.EDIT_DEVICE:
            case PageId.EDIT_ZONE:
//                return new EditFragment();
                return new KotlinEditFragment();
            case PageId.ADD_TIMING:
            case PageId.EDIT_TIMING:
                return new TimingEditFragment();
            case PageId.ZONE_DEVICE_MANAGE:
                return new ZoneDeviceManageFragment();
            case PageId.ADD_SCENE:
                return new SceneAddFragment();
            case PageId.ADD_SCENE_TIMING:
                return new SceneAddTimingFragment();
            case PageId.ADD_SCENE_DEVICE:
                return new SceneAddDeviceFragment();
            case PageId.NET_MANAGE:
                return new NetworkListFragment();
                case PageId.ABOUT_US:
                    return new AboutUsFragment();
            /**
             * 分享
             */
            case PageId.CHOOSE_ROLE:
                return new ShareChooseRoleFragment();
            case PageId.SHARE_DEVICE_ONLINE:
                return new ShareUserFragment();
            case PageId.SHARE_NETWORK:
                return new ChooseShareMeshFragment();
            case PageId.SHARE_RECEIVE:
                return new ShareReceiveFragment();
            case PageId.SHARE_HISTORY:
                return new ShareManageFragment();
            default:
                return new NullFragment();
        }
    }
}
