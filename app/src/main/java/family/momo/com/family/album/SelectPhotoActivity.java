package family.momo.com.family.album;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import family.momo.com.family.R;
import family.momo.com.family.util.ImageItem;
import family.momo.com.family.util.UploadUtil;

public class SelectPhotoActivity extends FragmentActivity {

    private RecyclerView mRecyclerView;
    private SelectIVAdapter mAdapter;
    private SelectDirAdapter mDirAdapter;
    private ArrayList<String> pickedList;
    private ArrayList<ImageItem> allImageList;
    private ArrayList<ImageItem> tempImageList;
    private ArrayList<ArrayList<ImageItem>> allImageDirList;
    private LinearLayout mInnerBtn;
    private ImageView mBackBtn;
    private int allDirIndex = 0;
    private TextView tvPreview;
    private TextView mUpload;

    private RelativeLayout rl_dir;
    private RecyclerView mRecyclerViewDir;

//    private ArrayList<ListViewAdapter.SingleDirectory> imageDirectories;

    private ObjectAnimator animation;
    private ObjectAnimator reverseanimation;
    //unknown
    private boolean isFinish = false;
    private LayoutInflater inflater = null;

    // ini as all images
    private int currentShowPosition = 0;
//    private int currentState = SCROLL_STATE_IDLE;

    private ImageView ivMask;
    private RelativeLayout lvDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        initPhotos();
        initView();
        initSending();
    }

    protected void initPhotos() {
        mDirAdapter = new SelectDirAdapter();
        mAdapter = new SelectIVAdapter();

        allImageDirList = new ArrayList<ArrayList<ImageItem>>();

        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<String>();
        pickedList = new ArrayList<String>();
        String[] directories = null;
        if (u != null) {
//            c = managedQuery(u, projection, null, null, null);
            c = getContentResolver().query(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst())) {
            do {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try {
                    dirList.add(tempDir);
                } catch (Exception e) {
                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);
        }

        for (int i = 0; i < dirList.size(); i++) {
            boolean hasImage = false;
            File imageDir = new File(directories[i]);
            File[] imageList = imageDir.listFiles();
            if (imageList == null)
                continue;
            tempImageList = new ArrayList<ImageItem>();
            for (File imagePath : imageList) {
                try {
                    if (imagePath.isDirectory()) {
                        imageList = imagePath.listFiles();
                        continue;
                    }
                    if (imagePath.getName().contains(".jpg") || imagePath.getName().contains(".JPG")
                            || imagePath.getName().contains(".jpeg") || imagePath.getName().contains(".JPEG")
                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")) {
                        String path = imagePath.getAbsolutePath();
                        // add to the dir
                        hasImage = true;
                        tempImageList.add(new ImageItem(path, false));
                        allImageDirList.get(0).add(new ImageItem(path, false));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (hasImage) {
                allImageDirList.add(tempImageList);
                Log.d("indexDebug", "image dir: "+ new File(tempImageList.get(0).path).
                    getParentFile().getName());
            }
        }
    }

    protected void initView() {

        ivMask = findViewById(R.id.iv_mask);
        lvDir = findViewById(R.id.rl_dir);
        tvPreview = findViewById(R.id.text_preview);

        mInnerBtn = findViewById(R.id.btn_inner_album);
        mInnerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ivMask.getVisibility() != View.VISIBLE) {
                    ivMask.setVisibility(View.VISIBLE);
                    lvDir.setVisibility(View.VISIBLE);
                } else {
                    ivMask.setVisibility(View.INVISIBLE);
                    lvDir.setVisibility(View.INVISIBLE);
                }
            }
        });

        mBackBtn = findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView = findViewById(R.id.id_recyclerView_select);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        mRecyclerView.addItemDecoration(new IVDecoration(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerViewDir = findViewById(R.id.rv_dir);
        mRecyclerViewDir.setLayoutManager(new LinearLayoutManager(this));//GridLayoutManager(this, 1)
        mRecyclerViewDir.setAdapter(mDirAdapter);
        mRecyclerViewDir.setItemAnimator(new DefaultItemAnimator());

//        rl_dir = findViewById(R.id.rl_dir);

        mBackBtn = findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initSending(){
        mUpload = findViewById(R.id.btn_upload);
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo send pictures to server.
//                try {
//                    Log.d("Tag", "Start Upload:"+ pickedList.get(0));
//                    String requestURL = "http://missmo.oicp.io/api/uppicture";
//                    final Map<String, String> params = new HashMap<String, String>();
//                    params.put("Key", "inputname");
//
//                    final Map<String, File> files = new HashMap<String, File>();
//                    File file = new File(pickedList.get(0));
//                    files.put("VALUE", file);
//                    final String request = UploadUtil.post(requestURL, params, files);
//                    Log.d("Tag", "Uploaded:"+ pickedList.get(0));
//                    finish();
//
//                }catch (IOException e){
//                    e.printStackTrace();
//                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String uploadurl = "http://missmo.oicp.io/api/uppicture";//SharedPreferencesUtil.getServerUrls(getActivity()) + "mobileqrcode/uploadsignimg.html";
                        try {
                            Log.d("Tag", "Start Upload:"+ pickedList.get(0));
                            File file = new File(pickedList.get(0));
                            String result = UploadUtil.uploadImage(file, uploadurl);
                            Log.d("Tag", "Uploaded:"+ pickedList.get(0)+""+result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    class SelectDirAdapter extends RecyclerView.Adapter<SelectDirAdapter.SelectDirHolder>{
        @Override
        public SelectDirHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SelectDirHolder holder = new SelectDirHolder(LayoutInflater.from(
                    SelectPhotoActivity.this).inflate(R.layout.cl_image_directories, parent,
                    false));
            return holder;
        }

        class SelectDirHolder extends RecyclerView.ViewHolder {
            ImageView ivSelectState;
            TextView tvDir;
            ImageView ivDir;

            public SelectDirHolder(View view) {
                super(view);
                ivSelectState = (ImageView) view.findViewById(R.id.iv_dir_select_state);
                ivDir = view.findViewById(R.id.iv_dir);
                tvDir = view.findViewById(R.id.tv_dir_name);
            }
        }

        @Override
        public void onBindViewHolder(SelectDirHolder holder, int position){
            OnClickDirHolder listener = new OnClickDirHolder(holder);
            holder.itemView.setOnClickListener(listener);

            //load icon
            Log.d("indexDebug", ""+ "Dir index: "+ position);
            String path = allImageDirList.get(position).get(0).path;
            Log.d("indexDebug", ""+ "Dir null?: "+ path);
            Picasso.with(SelectPhotoActivity.this)
                    .load(new File(path)).placeholder(R.color.lightBlack)
                    .resize(115,115)
                    .error(R.drawable.icon_photo_nor).into(holder.ivDir);
            // for text
            if(position == 0){
                holder.tvDir.setText(getText(R.string.all_image)+"(" + allImageDirList.get(position).size() + ")");
            }else {
                holder.tvDir.setText(new File(allImageDirList.get(position).get(0).path).
                        getParentFile().getName() + "(" + allImageDirList.get(position).size() + ")");
            }
            // change the select state holder.ivDir.getTag()== null
            if(position != currentShowPosition){
                holder.ivSelectState.setImageResource(R.drawable.photo_btn_nor);
            }else {
                holder.ivSelectState.setImageResource(R.drawable.photo_btn_selected);
            }
//            View childView = mRecyclerViewDir.getLayoutManager().findViewByPosition(currentShowPosition);
//            holder = mRecyclerViewDir.getChildViewHolder(childView);
        }

        public int getItemCount()
        {
            return allImageDirList.size();
        }

        private class OnClickDirHolder implements View.OnClickListener {
            SelectDirHolder holder;

            public OnClickDirHolder(SelectDirHolder holder){
                this.holder = holder;
            }

            @Override
            public void onClick(View v){
                currentShowPosition = holder.getAdapterPosition();
                // todo set last one unselected

                // clean the list and show photos,
                ivMask.setVisibility(View.INVISIBLE);
                lvDir.setVisibility(View.GONE);

                mAdapter.notifyDataSetChanged();
                mDirAdapter.notifyDataSetChanged();

            }
        }
    }

    // adapter for photos.
    class SelectIVAdapter extends RecyclerView.Adapter<SelectIVAdapter.SelectIVHolder>{
        @Override
        public SelectIVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SelectIVHolder holder = new SelectIVHolder(LayoutInflater.from(
                    SelectPhotoActivity.this).inflate(R.layout.select_photo_item, parent,
                    false));
            return holder;
        }

        class SelectIVHolder extends RecyclerView.ViewHolder {
            ImageView ivContent;
            ImageView ivSelectState;

            public SelectIVHolder(View view) {
                super(view);
                ivContent = (ImageView) view.findViewById(R.id.iv_content);
                ivSelectState = (ImageView) view.findViewById(R.id.iv_select_state);
            }
        }

        @Override
        public void onBindViewHolder(SelectIVHolder holder, int position) {
//            holder.ivContent.setImageResource(tempImageList.get(position).path);
            OnClickIVHolder listener = new OnClickIVHolder(holder);
            holder.ivContent.setOnClickListener(listener);
            holder.ivSelectState.setOnClickListener(listener);


            // enter dir and draw
            if(allImageDirList.get(currentShowPosition).get(position).isPicked){
                holder.ivSelectState.setImageResource(R.drawable.photo_btn_selected);
            }else {
                holder.ivSelectState.setImageResource(R.drawable.photo_btn_nor);
            }

            String path = allImageDirList.get(currentShowPosition).get(position).path;
            Picasso.with(SelectPhotoActivity.this)
                    .load(new File(path)).placeholder(R.color.lightBlack)
                    .resize(115,115)
                    .error(R.drawable.icon_photo_nor).into(holder.ivContent);
        }

        @Override
        public int getItemCount()
        {
            return allImageDirList.get(currentShowPosition).size();//allImageDirList.get((currentShowPosition==-1)?(allImageDirList.size()-1):currentShowPosition).size();
        }

        private class OnClickIVHolder implements View.OnClickListener{
            SelectIVHolder holder;

            public OnClickIVHolder(SelectIVHolder holder){
                this.holder = holder;
            }

            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                int id = v.getId();

                switch (id){
                    case R.id.iv_content:
                //todo: click and get big image fragment
                        break;
//改
                    case R.id.iv_select_state:
                        if(allImageDirList.get(currentShowPosition).get(position).isPicked){
                            pickedList.remove(allImageDirList.get(currentShowPosition).get(position).path);
                        }else{
                            pickedList.add(allImageDirList.get(currentShowPosition).get(position).path);
                        }
                        allImageDirList.get(currentShowPosition).get(position).isPicked
                                = !allImageDirList.get(currentShowPosition).get(position).isPicked ;

                        if(pickedList.size()!=0) {
                            tvPreview.setText(getString(R.string.preview) + "(" + pickedList.size() + ")");
                        }else {
                            tvPreview.setText(getString(R.string.preview));
                        }

                        // refresh state IV
                        holder.ivSelectState.setImageResource(
                                allImageDirList.get(currentShowPosition).get(position).isPicked?
                                        R.drawable.photo_btn_selected:R.drawable.photo_btn_nor
                        );
                }
            }
        }
    }
//
//    public class IVDecoration extends RecyclerView.ItemDecoration{
//        private int dividerHeight;
//
//
//        public IVDecoration(Context context) {
//            dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.default_gap);
//        }
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//            super.getItemOffsets(outRect, view, parent, state);
//            outRect.bottom = dividerHeight;//类似加了一个bottom padding
//            outRect.left = dividerHeight;
//            outRect.right = dividerHeight;
//            outRect.top = dividerHeight;
//
//        }
//    }

}
