package com.tofu.mvp.anim;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.tofu.mvp.R;
import com.tofu.mvp.util.CollectionUtils;
import com.tofu.mvp.util.Print;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright：贵州玄机科技有限公司
 * Created by wxl on 2020/7/20 16:58
 * Description：
 * Modify time：
 * Modified by：
 */
public class AnimFragment extends Fragment {

    private static HashMap<String, Anim.Builder> anims = new HashMap<>();


    public void addBuilder(View target, Anim.Builder builder){
        String id = UUID.randomUUID().toString();
        target.setTag(R.id.anim_view_id, id);
        anims.put(id,builder);
    }

    public Anim.Builder getBuilder(View target){
        String tag = (String) target.getTag(R.id.anim_view_id);
        return anims.get(tag);
    }

    public void removeBuilder(View target){
        String tag = (String) target.getTag(R.id.anim_view_id);
        anims.remove(tag);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Print.e("Anim onDestroyView ");
        if(CollectionUtils.isNotEmpty(anims)){
            Iterator<Map.Entry<String, Anim.Builder>> iterator = anims.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, Anim.Builder> next = iterator.next();
                next.getValue().release();
            }
            anims.clear();
        }
    }
}
