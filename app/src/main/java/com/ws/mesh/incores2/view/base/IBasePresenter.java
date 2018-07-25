package com.ws.mesh.incores2.view.base;

import java.lang.ref.WeakReference;

public class IBasePresenter<V extends IBaseView> {

    /**
     * 持有UI接口的弱引用
     */
    private WeakReference<V> viewRef;

    protected void attachView(V view){
        this.viewRef = new WeakReference<>(view);
    }

    /**
     * 视图解绑
     */
    protected void detach(){
        if (viewRef != null){
            viewRef.clear();
            viewRef = null;
        }
    }

    public V getView(){
        return viewRef.get();
    }
}
