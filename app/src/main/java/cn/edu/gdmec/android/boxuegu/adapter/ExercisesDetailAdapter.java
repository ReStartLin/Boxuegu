package cn.edu.gdmec.android.boxuegu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.bean.ExercisesBean;
import cn.edu.gdmec.android.boxuegu.utils.AnalysisUtils;

/**
 * Created by Administrator on 2018/3/7.
 */

public class ExercisesDetailAdapter extends BaseAdapter {
    private Context mContext;
    private List<ExercisesBean> eb1;
    private OnSelectListener onSelectListener;



    public ExercisesDetailAdapter(Context mContext,OnSelectListener onSelectListener){
        this.mContext = mContext;
        this.onSelectListener = onSelectListener;
    }

    public void setData(List<ExercisesBean> eb1){
        this.eb1 = eb1;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return eb1 == null ? 0 : eb1.size();
    }

    @Override
    public Object getItem(int position) {
        return eb1 == null ? null : eb1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //记录点击的位置

    private ArrayList<String> selectedPosition = new ArrayList<String>();
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder vh;

        if (convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.exercises_detail_list_item,null);
            vh.subject = convertView.findViewById(R.id.tv_subject);
            vh.tv_a = convertView.findViewById(R.id.tv_a);
            vh.tv_b = convertView.findViewById(R.id.tv_b);
            vh.tv_c = convertView.findViewById(R.id.tv_c);
            vh.tv_d = convertView.findViewById(R.id.tv_d);

            vh.iv_a = convertView.findViewById(R.id.iv_a);
            vh.iv_b = convertView.findViewById(R.id.iv_b);
            vh.iv_c = convertView.findViewById(R.id.iv_c);
            vh.iv_d = convertView.findViewById(R.id.iv_d);

            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        ExercisesBean bean = (ExercisesBean) getItem(position);
        if (bean != null){
            vh.subject.setText(bean.subject);
            vh.tv_a.setText(bean.a);
            vh.tv_b.setText(bean.b);
            vh.tv_c.setText(bean.c);
            vh.tv_d.setText(bean.d);
        }
        if (!selectedPosition.contains("" + position)){
            vh.iv_a.setImageResource(R.drawable.exercises_a);
            vh.iv_b.setImageResource(R.drawable.exercises_b);
            vh.iv_c.setImageResource(R.drawable.exercises_c);
            vh.iv_d.setImageResource(R.drawable.exercises_d);
            AnalysisUtils.setABCDEnable(true,vh.iv_a,vh.iv_b,vh.iv_c,vh.iv_d);
        }else{
            AnalysisUtils.setABCDEnable(false,vh.iv_a,vh.iv_b,vh.iv_c,vh.iv_d);
            switch (bean.select){
                case 0:
                    //用户选对了的情况
                    if (bean.answer == 1){
                        vh.iv_a.setImageResource(R.drawable.exercises_right_icon);
                        vh.iv_b.setImageResource(R.drawable.exercises_b);
                        vh.iv_c.setImageResource(R.drawable.exercises_c);
                        vh.iv_d.setImageResource(R.drawable.exercises_d);
                    }else if (bean.answer == 2){
                        vh.iv_a.setImageResource(R.drawable.exercises_a);
                        vh.iv_b.setImageResource(R.drawable.exercises_right_icon);
                        vh.iv_c.setImageResource(R.drawable.exercises_c);
                        vh.iv_d.setImageResource(R.drawable.exercises_d);
                    }else if (bean.answer == 3){
                        vh.iv_a.setImageResource(R.drawable.exercises_a);
                        vh.iv_b.setImageResource(R.drawable.exercises_b);
                        vh.iv_c.setImageResource(R.drawable.exercises_right_icon);
                        vh.iv_d.setImageResource(R.drawable.exercises_d);
                    }else if (bean.answer == 4){
                        vh.iv_a.setImageResource(R.drawable.exercises_a);
                        vh.iv_b.setImageResource(R.drawable.exercises_b);
                        vh.iv_c.setImageResource(R.drawable.exercises_c);
                        vh.iv_d.setImageResource(R.drawable.exercises_right_icon);
                    }
                    break;
                case 1:
                    //用户选错了的情况A
                    vh.iv_a.setImageResource(R.drawable.exercises_error_icon);
                    if (bean.answer == 2){
                        vh.iv_b.setImageResource(R.drawable.exercises_right_icon);
                        vh.iv_c.setImageResource(R.drawable.exercises_c);
                        vh.iv_d.setImageResource(R.drawable.exercises_d);
                    }else if (bean.answer == 3){
                        vh.iv_b.setImageResource(R.drawable.exercises_b);
                        vh.iv_c.setImageResource(R.drawable.exercises_right_icon);
                        vh.iv_d.setImageResource(R.drawable.exercises_d);
                    }else if (bean.answer == 4){
                        vh.iv_b.setImageResource(R.drawable.exercises_b);
                        vh.iv_c.setImageResource(R.drawable.exercises_c);
                        vh.iv_d.setImageResource(R.drawable.exercises_right_icon);
                    }
                    break;
                case 2:
                    //用户选错了的情况B
                    vh.iv_b.setImageResource(R.drawable.exercises_error_icon);
                    if (bean.answer == 1){
                        vh.iv_a.setImageResource(R.drawable.exercises_right_icon);
                        vh.iv_c.setImageResource(R.drawable.exercises_c);
                        vh.iv_d.setImageResource(R.drawable.exercises_d);
                    }else if (bean.answer == 3){
                        vh.iv_a.setImageResource(R.drawable.exercises_a);
                        vh.iv_c.setImageResource(R.drawable.exercises_right_icon);
                        vh.iv_d.setImageResource(R.drawable.exercises_d);
                    }else if (bean.answer == 4){
                        vh.iv_a.setImageResource(R.drawable.exercises_a);
                        vh.iv_c.setImageResource(R.drawable.exercises_c);
                        vh.iv_d.setImageResource(R.drawable.exercises_right_icon);
                    }
                    break;
                case 3:
                    //用户选错了的情况C
                    vh.iv_c.setImageResource(R.drawable.exercises_error_icon);
                    if (bean.answer == 1){
                        vh.iv_a.setImageResource(R.drawable.exercises_right_icon);
                        vh.iv_b.setImageResource(R.drawable.exercises_b);
                        vh.iv_d.setImageResource(R.drawable.exercises_d);
                    }else if (bean.answer == 2){
                        vh.iv_a.setImageResource(R.drawable.exercises_a);
                        vh.iv_b.setImageResource(R.drawable.exercises_right_icon);
                        vh.iv_d.setImageResource(R.drawable.exercises_d);
                    }else if (bean.answer == 4){
                        vh.iv_a.setImageResource(R.drawable.exercises_a);
                        vh.iv_b.setImageResource(R.drawable.exercises_b);
                        vh.iv_d.setImageResource(R.drawable.exercises_right_icon);
                    }
                    break;
                case 4:
                    //用户选错了的情况D
                    vh.iv_d.setImageResource(R.drawable.exercises_error_icon);
                    if (bean.answer == 1){
                        vh.iv_a.setImageResource(R.drawable.exercises_right_icon);
                        vh.iv_b.setImageResource(R.drawable.exercises_b);
                        vh.iv_c.setImageResource(R.drawable.exercises_c);
                    }else if (bean.answer == 2){
                        vh.iv_a.setImageResource(R.drawable.exercises_a);
                        vh.iv_b.setImageResource(R.drawable.exercises_right_icon);
                        vh.iv_c.setImageResource(R.drawable.exercises_c);
                    }else if (bean.answer == 3){
                        vh.iv_a.setImageResource(R.drawable.exercises_a);
                        vh.iv_b.setImageResource(R.drawable.exercises_b);
                        vh.iv_c.setImageResource(R.drawable.exercises_right_icon);
                    }
                    break;
                default:
                    break;
            }
        }

        //当用户点击A选项
        vh.iv_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断用户是否做过做过题
                if (selectedPosition.contains("" + position)){
                    selectedPosition.remove("" + position);
                }else{
                    selectedPosition.add(position + "");
                }
                onSelectListener.onSelectA(position,vh.iv_a,vh.iv_b,vh.iv_c,vh.iv_d);
            }
        });

        //当用户点击B选项
        vh.iv_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断用户是否做过做过题
                if (selectedPosition.contains("" + position)){
                    selectedPosition.remove("" + position);
                }else{
                    selectedPosition.add(position + "");
                }
                onSelectListener.onSelectB(position,vh.iv_a,vh.iv_b,vh.iv_c,vh.iv_d);
            }
        });

        //当用户点击C选项
        vh.iv_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断用户是否做过做过题
                if (selectedPosition.contains("" + position)){
                    selectedPosition.remove("" + position);
                }else{
                    selectedPosition.add(position + "");
                }
                onSelectListener.onSelectC(position,vh.iv_a,vh.iv_b,vh.iv_c,vh.iv_d);
            }
        });

        //当用户点击D选项
        vh.iv_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断用户是否做过这道题
                if (selectedPosition.contains("" + position)){
                    selectedPosition.remove("" + position);
                }else{
                    selectedPosition.add(position + "");
                }
                onSelectListener.onSelectD(position,vh.iv_a,vh.iv_b,vh.iv_c,vh.iv_d);
            }
        });

        return convertView;
    }

    class ViewHolder{
        public TextView subject,tv_a,tv_b,tv_c,tv_d;
        public ImageView iv_a,iv_b,iv_c,iv_d;
    }

    public interface OnSelectListener{
        void onSelectA(int position, ImageView iv_a, ImageView iv_b, ImageView iv_c, ImageView iv_d);
        void onSelectB(int position, ImageView iv_a, ImageView iv_b, ImageView iv_c, ImageView iv_d);
        void onSelectC(int position, ImageView iv_a, ImageView iv_b, ImageView iv_c, ImageView iv_d);
        void onSelectD(int position, ImageView iv_a, ImageView iv_b, ImageView iv_c, ImageView iv_d);
    }
}
