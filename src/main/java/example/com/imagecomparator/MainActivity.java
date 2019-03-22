package example.com.imagecomparator;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button capture, simmilar, gallery,check;
    ImageView img,img1;
    Bitmap capture_img;
    LinearLayout linearLayout;
    int count=0;
    int count1=0;
    ArrayList<Bitmap> gal_image=new ArrayList<Bitmap>();
    ArrayList<Bitmap> local_bm=new ArrayList<Bitmap>();
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        capture = findViewById(R.id.capture);
        simmilar = findViewById(R.id.simmilar);
        gallery = findViewById(R.id.img_gal);
        check=findViewById(R.id.check);
        linearLayout=findViewById(R.id.ll2);

        img = findViewById(R.id.img);
        img1 = findViewById(R.id.img1);

        capture.setOnClickListener(this);
        simmilar.setOnClickListener(this);
        gallery.setOnClickListener(this);
        check.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.capture:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
                break;

            case R.id.simmilar:
                //importing only camera images and storing in ArrayList of class Images     type
                String[] projection = {
                    MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.DISPLAY_NAME
                };
                String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";
                String[] selectionArgs = new String[] {
                    "0"
                };
                Cursor mImageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null );
                if (mImageCursor != null)
                {
                    mImageCursor.moveToFirst();
                    for (int i = 0; i < mImageCursor.getCount(); i++)
                    {
                        MediaStore.Images im=new MediaStore.Images();
                        //eachImageView=new ImageView(this);
                        int imageId = mImageCursor.getInt((mImageCursor.getColumnIndex( MediaStore.Images.Media._ID)));
                        Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(),imageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
                        //im.setBitmap(bm);
                        //img.setImageBitmap(bm);
                        //im.setImageView(img);
                        gal_image.add(bm);
                        //arrayOfImages.add(im);
                        mImageCursor.moveToNext();
                    }
                }

                for(Bitmap bitmap:gal_image){
                    if(checkIfSimmilar(local_bm.get(0),bitmap)){
                        ImageView image = new ImageView(MainActivity.this);
                        image.setLayoutParams(new android.view.ViewGroup.LayoutParams(500,500));
                        //image.setMaxHeight(20);
                        //image.setMaxWidth(20);
                        linearLayout.addView(image,500,500);
                        image.setImageBitmap(bitmap);
                        //img1.setImageBitmap(bitmap);
                        //break;
                        count1++;
                    }
                }
                if (count1==0){
                    Toast.makeText(getApplicationContext(),"no simmilar images exist",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_gal:
                Intent pickFromGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickFromGallery.setType("image/*");
                startActivityForResult(pickFromGallery, REQUEST_IMAGE_GALLERY);
                break;
            case R.id.check:
                boolean test=checkIfSame(local_bm.get(0),local_bm.get(1));
                if(test==true)
                    Toast.makeText(getApplicationContext(),"both the images are same",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"both the images are not same",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                capture_img = (Bitmap) data.getExtras().get("data");
                local_bm.add(capture_img);
                if(count==0) {
                    img.setImageBitmap(capture_img);
                    count++;
                }
                else if(count==1){
                    img1.setImageBitmap(capture_img);
                    count--;
                }
                String path = Environment.getExternalStorageDirectory().toString();
                OutputStream fOut = null;
                File file = new File(path, "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                try {
                    fOut = new FileOutputStream(file);
                    capture_img.compress(Bitmap.CompressFormat.JPEG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    fOut.flush(); // Not really required
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // do not forget to close the stream

                try {
                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == REQUEST_IMAGE_GALLERY){
                Uri uri=data.getData();
                try {
                    Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    local_bm.add(bitmap);
                    if (count==0) {
                        img.setImageBitmap(bitmap);
                        count++;
                    }
                    else if (count==1){
                        img1.setImageBitmap(bitmap);
                        count--;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    boolean checkIfSame(Bitmap b1,Bitmap b2){
        if (b1.getHeight() != b2.getHeight())
            return false;
        if (b1.getWidth() != b2.getWidth())
            return false;

        for (int y = 0; y < b1.getHeight(); ++y) {
            for (int x = 0; x < b1.getWidth(); ++x) {
                if (x < b2.getWidth() && y < b2.getHeight()) {
                    if (b1.getPixel(x, y) != b2.getPixel(x, y))
                        return false;
                }
            }
        }
        return true;
    }
    boolean checkIfSimmilar(Bitmap b1,Bitmap b2){
        int temp=0;
        /*if (b1.getHeight() != b2.getHeight())
            return false;
        if (b1.getWidth() != b2.getWidth())
            return false;*/
        for (int y = 0; y < b1.getHeight(); ++y) {
            for (int x = 0; x < b1.getWidth(); ++x) {
                if (x < b2.getWidth() && y < b2.getHeight()) {
                    if (b1.getPixel(x, y) == b2.getPixel(x, y))
                        temp++;
                }
            }
        }
        if (temp>=20)
            return true;
        else
            return false;
    }
}