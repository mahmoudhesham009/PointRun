package com.example.pointrun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.example.pointrun.R;
import com.example.pointrun.databinding.SlideLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    SlideLayoutBinding binding;
    public List<Integer> images = new ArrayList<>();
    public List<String> text=new ArrayList<String>();

    public ViewPagerAdapter(Context context) {
        this.context = context;
        this.addPage(R.drawable.ic_undraw_runner_start_x0uu,"follow your passion to run  by completing the tricky mission point");
        this.addPage(R.drawable.ic_undraw_map_light_3hjy,"Base on your location we set missions around your place");
        this.addPage(R.drawable.ic_undraw_working_out_6psf,"Challenge your friend by different rank between each other");
    }

    public void addPage(Integer image,String tex) {
        images.add(image);
        text.add(tex);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            binding= DataBindingUtil.inflate(inflater,R.layout.slide_layout, container, false);

            binding.coverImage.setImageResource(images.get(position));
            binding.Text.setText(text.get(position));

            container.addView(binding.getRoot());
            return binding.getRoot();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}