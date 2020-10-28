package com.example.qrcodeandbarcodescanner;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SeconActivity extends AppCompatActivity implements  ZXingScannerView.ResultHandler{
    // private ZXingScannerView mScannerView;

    TextView content,formate;
    private ZXingScannerView mScannerView;
    private Barcode barcodeResult,barcodeformate,barcodeimage;
    ImageView imageView,share,copy,home;
    public static final String BARCODE_KEY = "BARCODE";
    public static final String BARCOD_FORMATE = "FORMATE";
    public static final String BARCOD_IMAGE = "URI";
    Bitmap mBitmap;
    private Context context ;
    private Uri imageUri;
    String EditTextValue ;
    public final static int QRcodeWidth = 350 ;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        content = (TextView) findViewById(R.id.txtcontent);
        formate = (TextView) findViewById(R.id.txtformate);
        imageView = (ImageView) findViewById(R.id.imgcode);
        share = (ImageView) findViewById(R.id.share);
        home = (ImageView) findViewById(R.id.imghome);
        copy = (ImageView) findViewById(R.id.copy);
        context = this;
        // mScannerView = new ZXingScannerView(this);
        // setContentView(mScannerView);
        final ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData myClip = ClipData.newPlainText("text", content.getText().toString());
                clipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SeconActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                String appLink = "https://play.google.com/store/apps/details?id=" + context.getPackageName();
                sharingIntent.setType("text/plain");
                String shareBodyText = "Check Out The Cool Barcode Reader App \n Link: " + appLink + " \n" +
                        " #Barcode #Android";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Barcode Reader Android App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Share"));


            }
        });

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA)
                .check();

    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(SeconActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
           startScan();


        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(SeconActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };

    Bitmap TextToImageEncode(Barcode Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = (BitMatrix) new MultiFormatWriter().encode(
                    String.valueOf(Value),
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.colorBlack):getResources().getColor(R.color.colorWhite);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 350, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private void startScan() {

        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(SeconActivity.this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withText("Scanning...")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                     /* Intent i=new Intent(SeconActivity.this,ResultClass.class);
                        i.putExtra("barcode",barcode.rawValue);
                        startActivity(i);*/
                        barcodeResult = barcode;
                        barcodeformate=barcode;
                        barcodeimage=barcodeResult;

                        content.setText(barcode.rawValue);
                        formate.setText(Integer.toString(barcode.format));
                       /* String  str_bit= null;
                        if(BarcodeFormat.QR_CODE==){
                            str_bit = BitMapToString(generateQRBitMap(str_content, rawResult.getBarcodeFormat()));
                        }else {
                            try {
                                str_bit = BitMapToString(createBarcodeBitmap(str_content, rawResult.getBarcodeFormat(), 800, 480));
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                        }*/
                       /*try {

                            mBitmap = (Bitmap) TextToImageEncode(barcodeResult);
                            imageView.setImageBitmap(mBitmap);

                        } catch (WriterException e) {
                            e.printStackTrace();
                        }*/



                    }

                })
                .build();
        materialBarcodeScanner.startScan();
    }

    public String getScanTime() {
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a" , Locale.getDefault());
        return  timeFormat.format(new Date());
    }

    public String getScanDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());
        return dateFormat.format(new Date());
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BARCODE_KEY, (Parcelable) barcodeResult);
        outState.putParcelable(BARCOD_FORMATE, (Parcelable) (barcodeformate));
        outState.putParcelable(BARCOD_IMAGE, (Parcelable) (barcodeimage));
        super.onSaveInstanceState(outState);
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    private Bitmap createBarcodeBitmap(String data, BarcodeFormat format,int width, int height) throws WriterException {
        MultiFormatWriter writer = new MultiFormatWriter();
        String finalData = Uri.encode(data);

        // Use 1 as the height of the matrix as this is a 1D Barcode.
        BitMatrix bm = writer.encode(finalData, format, width, 1);
        int bmWidth = bm.getWidth();

        Bitmap imageBitmap = Bitmap.createBitmap(bmWidth, height, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < bmWidth; i++) {
            // Paint columns of width 1
            int[] column = new int[height];
            Arrays.fill(column, bm.get(i, 0) ? Color.BLACK : Color.WHITE);
            imageBitmap.setPixels(column, 0, 1, i, 0, 1, height);
        }

        return imageBitmap;
    }
    private Bitmap generateQRBitMap(final String content,BarcodeFormat format) {

        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();

        hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content,format, 512, 512, hints);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {

                    bmp.setPixel(x , y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void handleResult(Result result) {

    }
}