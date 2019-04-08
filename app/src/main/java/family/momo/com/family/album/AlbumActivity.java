package family.momo.com.family.album;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import family.momo.com.family.R;


public class AlbumActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
    private ArrayList<String> mPhotos;
    private HomeAdapter mAdapter;
    private String fileNames[];

    private ImageView mMainPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        initPhotos();
        initRecylerView();
        setMainPhoto();

        if (checkPermissions().size() == 0) {
//            delayEnter();
        } else {
            requestPermissions();
        }

        ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            Intent intent = new Intent(AlbumActivity.this, SelectPhotoActivity.class);
            startActivity(intent);
            }
        });
    }

    protected void setMainPhoto(){
        //todo: set the main photo from the album.
    };

    protected void initPhotos(){
        //todo connet to server and get the photos...
//        try{
//
//        }
//        catch (){
//
//        }
        mAdapter = new HomeAdapter();
        mPhotos = new ArrayList<String>();

        try {
                fileNames =this.getAssets().list("preview/");
                mMainPhoto = findViewById(R.id.id_photo_main);
                Random ra =new Random();
                InputStream is = getAssets().open("preview/"+fileNames[ra.nextInt(fileNames.length)]);//mPhotos.get(position)
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                int w = bitmap.getWidth();//get widthint
                int h = bitmap.getHeight();//get height
                int aspRat = h/w;
//                mMainPhoto. = mMainPhoto.getWidth()*aspRat;
                bitmap = Bitmap.createScaledBitmap(bitmap, w/10, h/10, false);
                mMainPhoto.setImageBitmap(bitmap);
        } catch(IOException e){
            e.printStackTrace();
        }
        for(String file:fileNames) {
            Log.d("Tag", "file names: "+file);
        }
    }

    public void initRecylerView(){
        mRecyclerView = findViewById(R.id.id_album_recyclerView);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if(fileNames.length <= 0){
//            mRecyclerView.setBackgroundResource(R.drawable.icon_background);
            findViewById(R.id.rl_begin).setVisibility(View.VISIBLE);
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    AlbumActivity.this).inflate(R.layout.content_photo_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
//            holder.setText(mPhotos.get(position));
//            holder.mIV.setImageResource(mPhotos.get(position));
            try {
                InputStream is = getAssets().open("preview/"+fileNames[position]);//mPhotos.get(position)
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                int w = bitmap.getWidth();//get widthint
                int h = bitmap.getHeight();//get height
                int aspRat = w / h;
//                int W =
                bitmap = Bitmap.createScaledBitmap(bitmap, w/10, h/10, false);
                holder.mIV.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount()
        {
//            return mPhotos.size();
            return fileNames.length;
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {
//            TextView tv;
            private ImageView mIV;
            public MyViewHolder(View view)
            {
                super(view);
//                tv = (TextView) view.findViewById(R.id.id_num);
                mIV = view.findViewById(R.id.iv_main_content);

            }
        }
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_album, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
    /**
    * permissions for whole app
    */
    private List<String> checkPermissions() {
        List<String> permissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            permissions.add(Manifest.permission.READ_PHONE_STATE);
//        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }
        return permissions;
    }

    private void requestPermissions() {
        List<String> permissions = checkPermissions();
        if (permissions.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 0);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                FileLogManager.init();
                break;
            }
        }
    }
}
