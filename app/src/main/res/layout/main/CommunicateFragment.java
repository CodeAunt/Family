package fragmenttest.momo.com.myapplication.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fragmenttest.momo.com.myapplication.Chat_msg;
import fragmenttest.momo.com.myapplication.R;
import fragmenttest.momo.com.myapplication.VariableDataUtil;

public class CommunicateFragment extends Fragment {
    ListView chatList;
    ArrayList<Map<String, Object>> data = new ArrayList<>();//定义全局数据集合
    List<String> names = new LinkedList();
    List<String> msgs = new LinkedList();

    public static PhotoFragment newInstance() {
        return new PhotoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.communicate_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        getUsers();
        showUsers();
    }

    private void initViews(){
        chatList = getView().findViewById(R.id.chat_list_users);
    }
    //获取
    private void getUsers(){
        names.clear();
        msgs.clear();
        names.add(VariableDataUtil.groupname);
        msgs.add(" ");
        names.add("小明");
        msgs.add("聊天信息");
        names.add("小红");
        msgs.add("聊天信息");
        names.add("老明");
        msgs.add("聊天信息");
    }
    //显示聊天成员
    private void showUsers(){
        data.clear();
        for (int i = 0; i < names.size(); i++){
            Map<String, Object> item = new HashMap<>();
            // item.put("img", R.mipmap.classroom);
            item.put("name", names.get(i));
            Log.e("momo","name"+names.get(i));
            item.put("msg", msgs.get(i));
            Log.e("momo","msg"+msgs.get(i));
            data.add(item);
        }
        //获得simpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),data,R.layout.chat_style,new String[]{ "name","msg"},new int[]{R.id.chat_style_txt_name,R.id.chat_style_txt_msg});
        Log.e("momo","simpleAdapter OK");
        chatList.setAdapter(simpleAdapter);
        Log.e("momo","set Adapter OK");
        chatList.setOnItemClickListener(new getInChatMsg());

    }
    //转入私人聊天
    class getInChatMsg implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           Log.e("moo",names.get(position));
        }
    }

}

