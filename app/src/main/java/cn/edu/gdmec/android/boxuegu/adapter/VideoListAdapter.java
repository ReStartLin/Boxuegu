package cn.edu.gdmec.android.boxuegu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.bean.VideoBean;

/**
 * Created by student on 17/12/27.
 */

public class VideoListAdapter extends BaseAdapter {

    private Context context;
    private List<VideoBean> vb1; //视频列表数据
    private int selectedPosition = -1; //点击选中的位置
    private OnSelectListener onSelectListener;

    public VideoListAdapter(Context context,OnSelectListener onSelectListener){
        this.context = context;
        this.onSelectListener = onSelectListener;
    }

    public void setSelectedPosition(int position){
        this.selectedPosition = position;
    }

    public void setData(List<VideoBean> vb1){
        this.vb1 = vb1;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return vb1 == null ? 0 : vb1.size();
    }

    @Override
    public Object getItem(int position) {
        return vb1 == null ? null : vb1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.video_list_item,null);
            vh.tv_title = convertView.findViewById(R.id.tv_video_title);
            vh.iv_icon = convertView.findViewById(R.id.iv_left_icon);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        final VideoBean bean = vb1.get(position);
        vh.iv_icon.setImageResource(R.drawable.course_bar_icon);
        vh.tv_title.setTextColor(Color.parseColor("#333333"));
        if (bean != null){
            vh.tv_title.setText(bean.secondTitle);
            if (selectedPosition == position){
                vh.iv_icon.setImageResource(R.drawable.course_intro_icon);
                vh.tv_title.setTextColor(Color.parseColor("#009958"));
            }else{
                vh.iv_icon.setImageResource(R.drawable.course_bar_icon);
                vh.tv_title.setTextColor(Color.parseColor("#333333"));
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean == null){
                    return;
                }
                //播放视频
                onSelectListener.onSelect(position,vh.iv_icon);
            }
        });
        return convertView;
    }

    class ViewHolder{
        public TextView tv_title;
        public ImageView iv_icon;
    }

    //创建OnSelectListener接口把位置position和控件ImageView传递给Activity界面
    public interface OnSelectListener{
        void onSelect(int position, ImageView iv);
    }
}
