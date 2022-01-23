package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    Button camera;
    Button gallery;
    ImageView iv;
    int SELECT_PICTURE=200;
    int CAM_REQ=123;
    int CAM_PER=188;

    public HomeFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View HomeFrag= inflater.inflate(R.layout.fragment_home,container,false);
        camera=HomeFrag.findViewById(R.id.camera);
        gallery=HomeFrag.findViewById(R.id.gallery);
        //iv=HomeFrag.findViewById(R.id.iv);
        iv=getActivity().findViewById(R.id.iv);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA},CAM_PER);
                }else{
                    Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,CAM_REQ);
                }
            }
        });
        return HomeFrag;
    }


    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==CAM_PER){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(),"Camera Permission Granted", Toast.LENGTH_SHORT).show();
                Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAM_REQ);
            }else{
                Toast.makeText(getActivity(), "Camera Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    void imageChooser(){
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Picture"),SELECT_PICTURE);
    }
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==CAM_REQ && resultCode==RESULT_OK){
            Bitmap photo = (Bitmap)data.getExtras().get("data");
            iv.setImageBitmap(photo);
            ((MainActivity)getContext()).setIval(1);
        }

        if(resultCode==RESULT_OK){
            if(requestCode==SELECT_PICTURE){
                Uri selectImageUri=data.getData();
                if(null!=selectImageUri){
                    iv.setImageURI(selectImageUri);
                    //Bitmap photo2=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectImageUri)
                    ((MainActivity)getContext()).setIval(2);
                }
            }
        }
    }
}
