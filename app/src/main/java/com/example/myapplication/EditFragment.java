package com.example.myapplication;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import static android.app.Activity.RESULT_OK;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditFragment extends Fragment {

    Button undo;
    Button rotate;
    Button crop;
    Button save;
    ImageView iv;
    int PIC_CROP=123;
    public EditFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View EditFrag= inflater.inflate(R.layout.fragment_edit,container,false);
        undo=EditFrag.findViewById(R.id.undo);
        rotate=EditFrag.findViewById(R.id.rotate);
        crop=EditFrag.findViewById(R.id.crop);
        save=EditFrag.findViewById(R.id.save);
        iv=getActivity().findViewById(R.id.iv);

        final MainActivity xx=(MainActivity)getActivity();
        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
                    Bitmap curr = bitmapDrawable.getBitmap();
                    xx.setKalu(curr);
                    xx.setIval(2);
                    goToCrop(curr);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(xx.getIval()==90){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
                    Bitmap curr = bitmapDrawable.getBitmap();

                    Matrix matrix = new Matrix();
                    matrix.postRotate(-90);

                    Bitmap rotated = Bitmap.createBitmap(curr,0,0,curr.getWidth(),curr.getHeight(),matrix,true);
                    iv.setImageBitmap(rotated);

                    xx.setIval(0);
                }
                else if(xx.getIval()==2){
                    iv.setImageBitmap(xx.getKalu());
                    xx.setKalu(null);
                    xx.setIval(0);
                }
                else Toast.makeText(getActivity(),"Nothing to undo",Toast.LENGTH_SHORT).show();
            }
        });

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
                Bitmap curr = bitmapDrawable.getBitmap();

                Matrix matrix = new Matrix();
                matrix.postRotate(90);

                Bitmap rotated = Bitmap.createBitmap(curr,0,0,curr.getWidth(),curr.getHeight(),matrix,true);
                iv.setImageBitmap(rotated);

                xx.setIval(90);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},500);
                }else {
                    try {
                        saveToGallery();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(),"Maa Chuda",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return EditFrag;
    }
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) throws IOException {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==500){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(),"Storage Permission Granted", Toast.LENGTH_SHORT).show();
                saveToGallery();
            }else{
                Toast.makeText(getActivity(), "Storage Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void goToCrop(Bitmap curr) throws IOException {

        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        tempDir.mkdir();

        File tempFile = File.createTempFile("title", ".jpg", tempDir);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        curr.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] bitmapData = bytes.toByteArray();
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(bitmapData);
        fos.flush();
        fos.close();
        CropImage.activity(Uri.fromFile(tempFile)).start(getContext(), this);
        //assert xx != null;
        //xx.setKalu(curr);
    }
    public void saveToGallery() throws IOException {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        FileOutputStream outStream = null;
        //FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/MyPics");
        dir.mkdirs();

        String filename = String.format("%d.jpg",System.currentTimeMillis());
        File outFile = new File(dir,filename);
        outStream = new FileOutputStream(outFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        outStream.flush();
        outStream.close();
        String msg = "Pic captured at " + file.getAbsolutePath();
        Toast.makeText(getActivity(), msg,Toast.LENGTH_LONG).show();

        goToHome();
    }
    public void goToHome(){
        ViewPager vp=(ViewPager)getActivity().findViewById(R.id.pager);
        vp.setCurrentItem(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //Uri resultUri = result.getUri();
                iv.setImageURI(result.getUri());
                //xx.setIval(2);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
