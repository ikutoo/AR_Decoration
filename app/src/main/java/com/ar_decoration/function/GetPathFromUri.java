package com.ar_decoration.function;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class GetPathFromUri {
    @SuppressLint("NewApi")
    public static String getPath(final Context vContext, final Uri vUri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(vContext, vUri)) {

            final String documentId = DocumentsContract.getDocumentId(vUri);

            if (isExternalStorageDocument(vUri)) {
                final String[] split = documentId.split(":");
                final String documentType = split[0];

                if ("primary".equalsIgnoreCase(documentType)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            else if (isDownloadsDocument(vUri)) {

                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));

                return getDataColumn(vContext, contentUri, null, null);
            }

            else if (isMediaDocument(vUri)) {
                final String[] split = documentId.split(":");
                final String documentType = split[0];

                Uri contentUri = null;
                if ("image".equals(documentType)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(documentType)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(documentType)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(vContext, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(vUri.getScheme())) {
            return getDataColumn(vContext, vUri, null, null);
        }
        else if ("file".equalsIgnoreCase(vUri.getScheme())) {
            return vUri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context vContext, Uri vUri, String vSelection,
                                       String[] vSelectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = vContext.getContentResolver().query(vUri, projection, vSelection, vSelectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
