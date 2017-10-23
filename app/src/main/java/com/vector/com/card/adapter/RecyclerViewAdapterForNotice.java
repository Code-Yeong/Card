package com.vector.com.card.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.dao.NoticeDao;
import com.vector.com.card.daoimpl.TempDataDaoImpl;
import com.vector.com.card.domian.Notice;
import com.vector.com.card.domian.TempData;
import com.vector.com.card.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2017/9/24.
 */
public class RecyclerViewAdapterForNotice extends RecyclerView.Adapter {
    List<Notice> list;
    Context cxt;
    TempData tempData;

    public RecyclerViewAdapterForNotice(List<Notice> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        cxt = parent.getContext();
        tempData = Utils.readFromFile(cxt);
        View view = LayoutInflater.from(cxt).inflate(R.layout.self_notice_item, null);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.tv_content.setText(list.get(position).getContent());
        myHolder.tv_time.setText(list.get(position).getTime());
        String type = list.get(position).getNtype();
        String status = list.get(position).getStatus();

        if (status.equals("0")) {
            new NoticeDao(cxt).updateStatus(new Notice(list.get(position).getId(), "1"));
        }

        if (type.equals("A")) {
            myHolder.iv_icon.setImageResource(R.drawable.icon_in);
            myHolder.tv_title.setText("积分变更");
            myHolder.tv_subcontent.setText("完成日常学习任务赚取更多积分");
        } else if (type.equals("B")) {
            myHolder.iv_icon.setImageResource(R.drawable.icon_flag);
            myHolder.tv_title.setText("任务更新");
            myHolder.tv_subcontent.setText("好好学习天天向上");
        } else if (type.equals("C")) {
            myHolder.iv_icon.setImageResource(R.drawable.icon_text);
            myHolder.tv_title.setText("备忘录更新");
            myHolder.tv_subcontent.setText("记下生活中的点点滴滴");
        } else if (type.equals("E")) {
            myHolder.iv_icon.setImageResource(R.drawable.icon_task_on);
            myHolder.tv_title.setText("每日任务");
            myHolder.tv_subcontent.setText("进步在于每一天的积累");
        } else if (type.equals("S")) {
            myHolder.iv_icon.setImageResource(R.drawable.icon_setting);
            myHolder.tv_title.setText("系统通知");
            myHolder.tv_subcontent.setText("您的支持就是我们最大的动力");
        } else if (type.equals("D")) {
            myHolder.iv_icon.setImageResource(R.drawable.icon_person);
            myHolder.tv_title.setText("完善信息");
            myHolder.tv_subcontent.setText("不断完善自己，做最好的你");
        }
        ((MyHolder) holder).iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(cxt).inflate(R.layout.self_notice_option, null);
                LinearLayout ll_hide = (LinearLayout) view.findViewById(R.id.self_notice_option_hide);
                LinearLayout ll_reject = (LinearLayout) view.findViewById(R.id.self_notice_option_reject);
                final PopupWindow popupWindow = new PopupWindow(view, Utils.dp2pix(cxt, 130), Utils.dp2pix(cxt, 80));
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(v);

                ll_hide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (new NoticeDao(cxt).delete(list.get(position).getId()) > 0) {
                            popupWindow.dismiss();
                            list.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                });

                ll_reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.vibrate(cxt);
                        popupWindow.dismiss();
                        TempData t = Utils.readFromFile(cxt);
                        String type = list.get(position).getNtype();
                        if (type.equals("A")) {
                            t.setaNotice("no");
                        } else if (type.equals("B")) {
                            t.setbNotice("no");
                        } else if (type.equals("C")) {
                            t.setcNotice("no");
                        } else if (type.equals("D")) {
                            t.setdNotice("no");
                        } else if (type.equals("E")) {
                            t.seteNotice("no");
                        } else if (type.equals("S")) {
                            t.setsNotice("no");
                        }
                        refreshData(type);
                        new TempDataDaoImpl().saveLoginInfo(cxt, t);
                    }
                });
            }
        });
    }

    private void refreshData(String type) {
        NoticeDao noticeDao = new NoticeDao(cxt);
        if (noticeDao.deleteAllByType(tempData.getId(), type) > 0) {
            list = noticeDao.queryAllByUserId(tempData.getId());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public ImageView iv_icon, iv_more;
        public TextView tv_content, tv_subcontent, tv_time, tv_title;

        public MyHolder(View itemView) {
            super(itemView);
            iv_icon = (ImageView) itemView.findViewById(R.id.self_notice_icon);
            iv_more = (ImageView) itemView.findViewById(R.id.self_notice_more);
            tv_content = (TextView) itemView.findViewById(R.id.self_notice_content);
            tv_subcontent = (TextView) itemView.findViewById(R.id.self_notice_subcontent);
            tv_title = (TextView) itemView.findViewById(R.id.self_notice_title);
            tv_time = (TextView) itemView.findViewById(R.id.self_notice_time);
        }
    }
}
