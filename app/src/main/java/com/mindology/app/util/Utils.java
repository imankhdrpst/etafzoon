package com.mindology.app.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
import com.mindology.app.models.Attachment;
import com.mindology.app.models.ErrorResponse;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.common.FileResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import it.sephiroth.android.library.exif2.ExifInterface;
import it.sephiroth.android.library.exif2.ExifTag;
import retrofit2.HttpException;

public class Utils {
    private static final Utils ourInstance = new Utils();

    public static Utils getInstance() {
        return ourInstance;
    }

    private Utils() {
    }

    public static final Bitmap stringBase64ToBitmap(String base64) {
        try {
            if (base64 == null || TextUtils.isEmpty(base64))
                return null;

            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bitmapToStringBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Bitmap getBitmapFromBase64String(String base64Content) {
        try {
            byte[] decodedString = Base64.decode(base64Content, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;
        } catch (Exception e) {
            return null;
        }
    }

    public static synchronized ErrorResponse fetchError(Throwable throwable) {
        ErrorResponse errorResponse = new ErrorResponse();
        if (throwable instanceof UnknownHostException) {
            errorResponse.setMessage("خطا در اتصال به اینترنت");
            return errorResponse;
        } else if (throwable instanceof SocketTimeoutException) {
            errorResponse.setMessage("لطفا دوباره سعی کنید");
            return errorResponse;
        } else if (throwable instanceof ConnectException) {
            errorResponse.setMessage("خطای برقراری ارتباط با سرور");
            return errorResponse;
        }
        try {
            errorResponse = (new Gson()).fromJson(((HttpException) throwable).response().errorBody().string(), ErrorResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            errorResponse.setMessage(e.getMessage());
        }

        return errorResponse;
    }

    public static synchronized File compressAndSavePanorama(Bitmap bitmap) {
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        options.height = 1024;
        options.width = 2048;
        options.quality = 100;
        FileResult selectedFileResult = Tiny.getInstance().source(bitmap).asFile().withOptions(options).compressSync();
        File savedFile = Utils.saveFile(selectedFileResult.outfile);

        return savedFile;
    }

    private static synchronized File compressBitmap(File file) {
        double fileSize = file.length() / 1024; // mb
        double expectedSize = 2048; // mb
        if (file.exists() && fileSize > 2048) {
            Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
            options.quality = (int) Math.ceil((expectedSize / fileSize) * 100);
            FileResult result = Tiny.getInstance().source(file).asFile().withOptions(options).compressSync();
            return new File(result.outfile);
        }
        return file;
    }

    private static File getFileFromCapturedFileName(String relatedFileName) {
        File file = new File(Environment.getExternalStorageDirectory(), "Pictures"
                + "/"
                + Constants.FOLDER_NAME
                + "/"
                + relatedFileName);

        if (file.exists()) {
            return file;
        }
        return null;
    }

    private static synchronized File saveFile(String selectedFileResult) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + Constants.FOLDER_NAME);
        myDir.mkdirs();
        String fileName = "Image-" + Calendar.getInstance().getTimeInMillis() + ".jpg";
        File source = new File(selectedFileResult);
        File destination = new File(myDir + File.separator + fileName);
        if (source.exists()) {
            FileChannel src = null;
            try {
                src = new FileInputStream(source).getChannel();
                FileChannel dst = new FileOutputStream(destination).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return destination;
    }

    public static String getDateFormat(Date date) {
        return (new SimpleDateFormat(Constants.newDateFormat)).format(date);
    }

    public static String getRawViewDateFormat(Date date) {
        return (new SimpleDateFormat(Constants.viewDateFormat)).format(date);
    }

    public static String convertDateToDayCaption(Date date) {
        Date newTime = new Date();
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(newTime);
            Calendar oldCal = Calendar.getInstance();
            oldCal.setTime(date);

            int oldYear = oldCal.get(Calendar.YEAR);
            int year = cal.get(Calendar.YEAR);
            int oldDay = oldCal.get(Calendar.DAY_OF_YEAR);
            int day = cal.get(Calendar.DAY_OF_YEAR);

            if (oldYear == year) {
                int value = oldDay - day;
//                if (value == -1) {
//                    return "Yesterday";
//                } else
                if (value == 0) {
                    return "Today";
                } else if (value == 1) {
                    return "Tomorrow";
                } else {
                    return "";//(new SimpleDateFormat(Constants.viewDateFormat)).format(date);
                }
            }
        } catch (Exception e) {

        }
        return "";//(new SimpleDateFormat(Constants.viewDateFormat)).format(date);
    }

    public static Attachment getAttachment(Context context, Uri item) {
        Attachment attachment = new Attachment();
        File relatedFile = new File(Utils.getRealPathFromURI(context, item));
        if (!relatedFile.exists()) {
            relatedFile = Utils.getFileFromCapturedFileName((new File(item.getPath())).getName());
        }
        relatedFile = Utils.compressBitmap(relatedFile);
        attachment.setFileName(relatedFile.getName());
        attachment.setLocalPath(relatedFile.getAbsolutePath());
        attachment.setMimeType("image/jpeg");

        Date date = Calendar.getInstance().getTime();
        try {
            ExifInterface exifInterface = new ExifInterface();
            exifInterface.readExif(context.getContentResolver().openInputStream(item), ExifInterface.Options.OPTION_ALL);
            ExifTag tag = exifInterface.getTag(ExifInterface.TAG_DATE_TIME_ORIGINAL);
            if (tag != null) {
                date = ExifInterface.getDateTime(tag.getValueAsString(), TimeZone.getDefault());
            }
        } catch (IOException e) {
        }
        DateFormat format = new SimpleDateFormat(Constants.dateFormat);
        String strDate = format.format(new Date(date.getTime()));
        attachment.setDateTaken(strDate);
        return attachment;
    }

    public static Attachment getAttachment(Context context, Image item) {
        Attachment attachment = new Attachment();
        File relatedFile = new File(item.getPath());
        if (!relatedFile.exists()) {
            relatedFile = Utils.getFileFromCapturedFileName((new File(item.getPath())).getName());
        }
        relatedFile = Utils.compressBitmap(relatedFile);
        attachment.setFileName(relatedFile.getName());
        attachment.setLocalPath(relatedFile.getAbsolutePath());
        attachment.setMimeType("image/jpeg");

        Date date = Calendar.getInstance().getTime();
        try {
            ExifInterface exifInterface = new ExifInterface();
            exifInterface.readExif(context.getContentResolver().openInputStream(Uri.fromFile(relatedFile)), ExifInterface.Options.OPTION_ALL);
            ExifTag tag = exifInterface.getTag(ExifInterface.TAG_DATE_TIME_ORIGINAL);
            if (tag != null) {
                date = ExifInterface.getDateTime(tag.getValueAsString(), TimeZone.getDefault());
            }
        } catch (IOException e) {
        }
        DateFormat format = new SimpleDateFormat(Constants.dateFormat);
        String strDate = format.format(new Date(date.getTime()));
        attachment.setDateTaken(strDate);
        return attachment;
    }
}
