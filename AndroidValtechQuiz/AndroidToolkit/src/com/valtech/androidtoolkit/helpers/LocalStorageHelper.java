package com.valtech.androidtoolkit.helpers;

import java.io.File;

import android.content.Context;
import android.text.TextUtils;

import com.valtech.androidtoolkit.utils.Logger;
import com.valtech.androidtoolkit.utils.Toolkit;

/**
 * Helper used to access files on external storage.<br/>
 * It creates storage folder on initialization.
 * 
 * @author thomas.meunier
 * 
 */
public class LocalStorageHelper
{
    public static final String CACHE_DIR = "cache";

    private static String LOG_TAG = LocalStorageHelper.class.getSimpleName();


    // private static Pattern WS_IMAGE_SIZE_PATTERN = Pattern.compile("^[0-9]+x[0-9]+$");

    private LocalStorageHelper() {}

    /**
     * Initialization of the helper from the application context
     * 
     * @param context the context of the application
     * @return a boolean that indicates if the operation was successful or not
     */
    public static boolean initLocalStorage(Context context) {
        boolean isSuccessful = true;

        String defaultStoragePath = getLocalStoragePath(context);
        isSuccessful = createDirs(defaultStoragePath);
        Logger.logInfo(LOG_TAG, "STORAGE PATH: " + defaultStoragePath);

        String imagesStoragePath = getImagesDir(context);
        isSuccessful &= createDirs(imagesStoragePath);
        Logger.logInfo(LOG_TAG, "IMAGES STORAGE PATH: " + imagesStoragePath);

        String databasesStoragePath = getDatabasesDir(context);
        isSuccessful &= createDirs(databasesStoragePath);
        Logger.logInfo(LOG_TAG, "DB STORAGE PATH: " + databasesStoragePath);

        return isSuccessful;
    }

    private static boolean createDirs(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return true;
    }

    /**
     * Retrieve the path of the storage (internal or external) for this app
     * 
     * @return the path of the storage associated to the application - may be internal or
     *         external...
     */
    public static String getLocalStoragePath(Context ctx) {
        String dir = "";
        // FIXME TODO : laisser le choix d'utiliser le stockage externe ??
        /*
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
        	mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
        	mExternalStorageAvailable = true;
        	mExternalStorageWriteable = false;
        } else {
        	mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        if (mExternalStorageAvailable && mExternalStorageWriteable) {
        	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
        		dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.roularta/files/"
        				+ ctx.getString(R.string.app_name);
        	} else {
        		dir = ctx.getExternalFilesDir(null).getAbsolutePath() + File.separator
        				+ ctx.getString(R.string.app_name);
        	}
        } else {
        	Logger.logError(LOG_TAG, "External storage unavailable");
        }
        */
        dir = CACHE_DIR /* + "-" + ctx.getString(R.string.app_name)*/;
        File tmp = ctx.getDir(dir, Context.MODE_PRIVATE);
        return tmp.getAbsolutePath();
    }

    public static File getLocalStorageDir(Context ctx) {
        String dir = getLocalStoragePath(ctx);
        File tmp = new File(dir);
        if (tmp.exists()) return tmp;
        return null;
    }

    /**
     * @return the path of the images directory
     */
    public static String getImagesDir(Context ctx) {
        // return getLocalStoragePath(ctx) + File.separator + "images";
        return getLocalStoragePath(ctx);
    }

    /**
     * @return the path of the databases directory
     */
    public static String getDatabasesDir(Context ctx) {
        return getLocalStoragePath(ctx) + File.separator + "databases";
    }

    /**
     * Retrieve the path of an image from its url
     * 
     * @param url the url of the image
     * @return the path of the image on the filesystem
     */
    public static String getImageStoragePathFromUrl(String url, Context ctx) {
        String imageName = getImageNameFromUrl(url);
        if (!TextUtils.isEmpty(imageName)) {
            return getImagesDir(ctx) + File.separator + imageName;
        }

        return null;
    }

    /**
     * Get an image name from its url
     * 
     * @param url the url of the image
     * @return the local name of the image
     */
    public static String getImageNameFromUrl(String url) {
        /*
        String[] splitted = url.split("/");

        // take last part if not a resolution, else the one before last
        String imageName = splitted[splitted.length - 1];

        Matcher matcher = WS_IMAGE_SIZE_PATTERN.matcher(imageName);
        if (matcher.find()) {
        	// resolution found
        	imageName = splitted[splitted.length - 2];
        }

        // remove dotted part if existent
        int lastIndexOfDot = imageName.lastIndexOf(".");
        // remove extension if present
        if (lastIndexOfDot != -1) {
        	imageName = imageName.substring(0, lastIndexOfDot);
        }
        return imageName;
        */
        if (url != null && !TextUtils.isEmpty(url)) {
            return Toolkit.md5(url);
        }
        return "";
    }

    /**
     * Delete an image on the filesystem from its url (if it exists)
     * 
     * @param url the url of the image
     * @return true if the image was deleted, false otherwise
     */
    public static boolean deleteImageFromUrl(String url, Context ctx) {
        if (!TextUtils.isEmpty(url)) {
            String path = getImageStoragePathFromUrl(url, ctx);

            File image = new File(path);
            if (image.exists() && !image.isDirectory()) {
                return image.delete();
            }
        }

        return false;
    }

    // deletes an dir and its content
    private static boolean deleteWholeDir(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                if (f.isDirectory()) {
                    deleteWholeDir(f);
                } else {
                    f.delete();
                }
            }
        }

        return dir.delete();
    }

    public static boolean deleteImagesDir(Context ctx) {
        File imagesDir = new File(getImagesDir(ctx));

        return deleteWholeDir(imagesDir);
    }

    public static boolean deleteDatabasesDir(Context ctx) {
        File databasesDir = new File(getDatabasesDir(ctx));

        return deleteWholeDir(databasesDir);
    }

    public static boolean deleteExternalStorageDir(Context ctx) {
        File storageDir = new File(getLocalStoragePath(ctx));

        return deleteWholeDir(storageDir);
    }
}