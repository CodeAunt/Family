package family.momo.com.family.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import family.momo.com.family.R;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> implements View.OnClickListener,View.OnLongClickListener {

    private List<Msg> mMsgList;

    private OnItemClickListener onItemClickListener =null;
    private OnItemLongClickListener onItemLongClickListener =null;


    /**
     * 创建 ViewHolder 加载 RecycleView 子项的布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_msg_style, parent, false);
        //设置点击事件
        return new ViewHolder(view);
    }

    /**
     * 为 RecycleView 子项赋值
     * 赋值通过 position 判断子项位置
     * 当子项进入界面时执行
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //将position保存在itemView的Tag中
        holder.itemView.setTag(position);
        holder.leftImg.setTag(position);
        holder.rightImg.setTag(position);
        holder.leftMsg.setTag(position);
        holder.rightMsg.setTag(position);
        holder.leftName.setTag(position);
        holder.leftLayout.setTag(position);
        holder.rightLayout.setTag(position);

        Msg msg = mMsgList.get(position);
        if (msg.getType() == Msg.TYPE_RECEIVED) {
            // 如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
            holder.leftName.setText(msg.getName());
            if (msg.isGroup){
                holder.leftName.setVisibility(View.VISIBLE);
            }else {
                holder.leftName.setVisibility(View.GONE);
            }
        } else if (msg.getType() == Msg.TYPE_SENT) {
            // 如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
    @Override
    public void onClick(View v) {
        //注意这里使用getTag方法获取position
        int position = (int) v.getTag();      //getTag()获取数据
        if (onItemClickListener != null) {
            switch (v.getId()){
                case R.id.chat_msg_list_msgs:
                    onItemClickListener.onItemClick(v, mMsgList, position);
                    break;
                default:
                    onItemClickListener.onItemClick(v, mMsgList, position);
                    break;
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Log.e("momo",v.toString());
        //注意这里使用getTag方法获取position
        int position = (int) v.getTag();      //getTag()获取数据
        if (onItemLongClickListener != null) {
            switch (v.getId()){
                case R.id.chat_msg_list_msgs:
                    onItemLongClickListener.onItemLongClick(v, mMsgList, position);
                    break;
                default:
                    onItemLongClickListener.onItemLongClick(v, mMsgList, position);
                    break;
            }
        }
        return false;
    }


    //点击事件回调接口
    interface OnItemClickListener{
        void onItemClick(View v, List<Msg> msgs, int position);
    }
    interface OnItemLongClickListener{
        void onItemLongClick(View v,List<Msg> msgs, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftLayout;

        LinearLayout rightLayout;

        TextView leftMsg;

        TextView rightMsg;

        ImageView leftImg;

        ImageView rightImg;

        TextView leftName;

        // view表示父类的布局，用其获取子项
        public ViewHolder(View view) {
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.chat_msg_style_linearlayout_left);
            rightLayout = (LinearLayout) view.findViewById(R.id.chat_msg_style_linearlayout_right);
            leftMsg = (TextView) view.findViewById(R.id.chat_msg_style_txt_msg_left);
            rightMsg = (TextView) view.findViewById(R.id.chat_msg_style_txt_msg_right);
            leftImg = view.findViewById(R.id.chat_msg_style_img_headimg_left);
            rightImg = view.findViewById(R.id.chat_msg_style_img_headimg_right);
            leftName = view.findViewById(R.id.chat_msg_style_txt_name_left);

            leftMsg.setOnClickListener(MsgAdapter.this);
            leftMsg.setOnLongClickListener(MsgAdapter.this);

            rightMsg.setOnClickListener(MsgAdapter.this);
            rightMsg.setOnLongClickListener(MsgAdapter.this);

            leftImg.setOnClickListener(MsgAdapter.this);
            leftImg.setOnLongClickListener(MsgAdapter.this);

            rightImg.setOnClickListener(MsgAdapter.this);
            rightImg.setOnLongClickListener(MsgAdapter.this);

            leftLayout.setOnClickListener(MsgAdapter.this);
            leftLayout.setOnLongClickListener(MsgAdapter.this);

            rightLayout.setOnClickListener(MsgAdapter.this);
            rightLayout.setOnLongClickListener(MsgAdapter.this);

            leftName.setOnClickListener(MsgAdapter.this);
            leftName.setOnLongClickListener(MsgAdapter.this);
        }
    }

    public MsgAdapter(List<Msg> msgList) {
        mMsgList = msgList;
    }



}