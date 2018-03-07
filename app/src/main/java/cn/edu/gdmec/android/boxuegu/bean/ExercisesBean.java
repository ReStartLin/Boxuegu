package cn.edu.gdmec.android.boxuegu.bean;

/**
 * Created by Administrator on 2018/3/7.
 */

public class ExercisesBean {
    public int id; //习题章节id
    public String title; //习题章节标题
    public String content; //习题数目
    public int background; //每章习题的背景id
    public int subjectId; //习题id
    public String subject; //习题题干
    public String a; //A选项
    public String b; //B选项
    public String c; //C选项
    public String d; //D选项
    public int answer; //答案
    /*
* select为0表示用户选对了，1表示选项中A为错的，2表示选项中B为错的。。。
* */
    public int select;
}
