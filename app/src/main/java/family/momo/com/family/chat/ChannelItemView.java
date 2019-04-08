package family.momo.com.family.chat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import family.momo.com.family.R;


public class ChannelItemView extends RelativeLayout {

    private TextView mChannelId;


    public ChannelItemView(Context context) {
        this(context, null);
    }

    public ChannelItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_channel_item, this);
        mChannelId = (TextView) findViewById(R.id.channel_id);
    }

    public void bindView(final String channel) {
        mChannelId.setText(channel);
    }
}
