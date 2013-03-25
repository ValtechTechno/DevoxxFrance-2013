package com.valtech.androidtoolkit.utils;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * A Bitmap LRU (Least Recently Used) Cache.<br/>
 * It uses two levels of cache, hard & soft.<br/>
 * When a bitmap is added or read in cache, it is placed in first position of the hard cache.<br/>
 * Least recently used bitmaps are placed in a soft cache that can be cleaned by GC.<br/>
 * An auto-clean task is defined and launched after {@value #DELAY_BEFORE_PURGE} milliseconds, as
 * you call at least once {@link #resetPurgeTimer()}<br/>
 * The cache is SHARED by all BitmapLruCache objects.
 * 
 * Adapted from {@link http://code.google.com/p/android-imagedownloader/}
 * 
 * @author thomas.meunier
 * 
 */
public class BitmapLruCache
{
    /*
     * Cache-related fields and methods.
     * 
     * We use a hard and a soft cache. Soft cache (static) is too aggressively cleaned by the GC.
     */
    public static final int HARD_CACHE_CAPACITY = 8;
    public static final int SOFT_CACHE_CAPACITY = HARD_CACHE_CAPACITY * 2;
    public static final int DELAY_BEFORE_PURGE = 1000 * 40; // in milliseconds

    // Hard cache, with a fixed maximum capacity and a life duration
    private static final Map<String, Bitmap> sHardCache = Collections
                    .synchronizedMap(new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {

                        private static final long serialVersionUID = 1L;


                        @Override
                        protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {
                            if (size() > HARD_CACHE_CAPACITY) {
                                // Entries push-out of hard reference cache are transferred to
                                // soft reference cache
                                sSoftCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
                                return true;
                            }

                            return false;
                        }
                    });

    // Soft cache for bitmaps kicked out of hard cache
    private final static Map<String, SoftReference<Bitmap>> sSoftCache = Collections
                    .synchronizedMap(new LinkedHashMap<String, SoftReference<Bitmap>>(SOFT_CACHE_CAPACITY / 2, 1.0f, true) {
                        private static final long serialVersionUID = 1L;


                        @Override
                        protected boolean removeEldestEntry(LinkedHashMap.Entry<String, SoftReference<Bitmap>> eldest) {
                            return size() > SOFT_CACHE_CAPACITY;
                        }
                    });

    private static final Handler purgeHandler = new Handler();

    private static final Runnable purger = new Runnable() {
        @Override
        public void run() {
            Logger.logInfo(BitmapLruCache.class.getSimpleName(), "Running auto clearcache");
            clearCache();
        }
    };


    /**
     * Adds this bitmap to the cache.
     * 
     * @param bitmap The newly downloaded bitmap.
     */
    public void addBitmapToCache(String identifier, Bitmap bitmap) {
        resetPurgeTimer();
        if (bitmap != null) {
            synchronized (sHardCache) {
                sHardCache.put(identifier, bitmap);
            }
        }
    }

    /**
     * Get the identified bitmap from the cache.
     * 
     * @param identifier The URL of the image that will be retrieved from the cache.
     * @return The cached bitmap or null if it was not found.
     */
    public Bitmap getBitmapFromCache(String identifier) {
        resetPurgeTimer();
        // First try the hard reference cache
        synchronized (sHardCache) {
            final Bitmap bitmap = sHardCache.get(identifier);
            if (bitmap != null) {
                // Bitmap found in hard cache
                // Move element to first position, so that it is removed last
                sHardCache.remove(identifier);
                sHardCache.put(identifier, bitmap);
                return bitmap;
            }
        }

        // Then try the soft reference cache
        SoftReference<Bitmap> bitmapReference = sSoftCache.get(identifier);
        if (bitmapReference != null) {
            final Bitmap bitmap = bitmapReference.get();

            sSoftCache.remove(identifier);
            if (bitmap != null) {
                // Bitmap found in soft cache
                // put it in hard cache
                sHardCache.put(identifier, bitmap);

                return bitmap;
            }
            // else
            // Soft reference has been Garbage Collected
        }

        return null;
    }

    /**
     * Clears the image cache used internally to improve performance. Note that for memory
     * efficiency reasons, the cache will automatically be cleared after a certain inactivity delay.
     */
    public static void clearCache() {
        sHardCache.clear();
        sSoftCache.clear();
    }

    /**
     * Allow a new delay before the automatic cache clear is done.
     */
    public void resetPurgeTimer() {
        purgeHandler.removeCallbacks(purger);
        purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
    }
}
