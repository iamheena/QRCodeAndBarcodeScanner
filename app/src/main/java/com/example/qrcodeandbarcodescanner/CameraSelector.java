package com.example.qrcodeandbarcodescanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.DialogFragment;

public class CameraSelector extends DialogFragment {
    public  interface CameraSelectorListener
    {
        public void onCameraSelected(int cameraId);
    }
    private  int mCmeraId;
    private CameraSelectorListener mListener;
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }
    public static CameraSelector newInstance(CameraSelectorListener listener,int cameraId)
    {
CameraSelector cameraSelector=new CameraSelector();
cameraSelector.mCmeraId=cameraId;
cameraSelector.mListener=listener;
return cameraSelector;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(mListener == null) {
            dismiss();
            return null;
        }

        int numberOfCameras = Camera.getNumberOfCameras();
        String[] cameraNames = new String[numberOfCameras];
        int checkedIndex = 0;

        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraNames[i] = "Front Facing";
            } else if(info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraNames[i] = "Rear Facing";
            } else {
                cameraNames[i] = "Camera ID: " + i;
            }
            if(i == mCmeraId) {
                checkedIndex = i;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle("Select a Camera")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(cameraNames, checkedIndex,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCmeraId = which;
                            }
                        })
                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedIndices results somewhere
                        // or return them to the component that opened the dialog
                        if (mListener != null) {
                            mListener.onCameraSelected(mCmeraId);
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }
}
