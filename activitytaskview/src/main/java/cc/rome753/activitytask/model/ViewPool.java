package cc.rome753.activitytask.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;

import cc.rome753.activitytask.AUtils;
import cc.rome753.activitytask.view.ATextView;
import cc.rome753.activitytask.view.FragmentTaskView;


/**
 * View缓存池
 * 考虑到视图中主要都是Textview，刷新时它们仅仅是颜色文本等属性有一些变化，
 * 没必要重新创建，因此可以使用一个缓存池把它们缓存起来。视图销毁时将它们保存到ViewPool中，
 * 视图重建时再从ViewPool中取出来使用。
 */
public class ViewPool extends Observable {

    LinkedList<ATextView> pool = new LinkedList<>();
    HashMap<String, FragmentTaskView> map = new HashMap<>();

    private static ViewPool factory = new ViewPool();

    public static ViewPool get() {
        return factory;
    }

    public void recycle(ViewGroup viewGroup) {
        if (viewGroup != null) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ATextView) {
                    AUtils.removeParent(view);
                    view.setTag(null);
                    pool.add((ATextView) view);
                } else if (view instanceof FragmentTaskView) {
                    // don't recycle
                } else if (view instanceof ViewGroup) {
                    recycle((ViewGroup) view);
                }
            }
        }
    }

    public ATextView getOne(Context context) {
        ATextView view;
        if (pool.isEmpty()) {
            view = new ATextView(context);
            addObserver(view);
        } else {
//            sca: 检索并删除此列表的头（第一个元素）。删除的值会被返回。
            view = pool.remove();
        }
        return view;
    }

    public void notifyLifecycleChange(LifecycleInfo info) {
        setChanged();
        notifyObservers(info);
    }


    public FragmentTaskView getF(String activity) {
        return map.get(activity);
    }

    public FragmentTaskView addF(Context context, String activity) {
        FragmentTaskView view = new FragmentTaskView(context);
        map.put(activity, view);
        return view;
    }

    public void removeF(String activity) {
        map.remove(activity);
    }

    public void clearF() {
        map.clear();
    }
}
