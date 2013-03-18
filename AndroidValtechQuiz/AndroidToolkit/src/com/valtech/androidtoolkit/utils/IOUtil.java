package com.valtech.androidtoolkit.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.res.AssetManager;

import com.valtech.androidtoolkit.common.exception.HttpIOException;
import com.valtech.androidtoolkit.common.exception.InvalidDirectoryException;

// TODO PageFragment.readFile
public class IOUtil
{
    // 8Kb is the default JVM stream buffer size.
    private static final int BUFFER_SIZE = 1024 * 8;
    private static final int URL_CONNECTION_TIMEOUT = 6000;
    private static final int URL_READ_TIMEOUT = 22000;
    private static final int PROGRESS_THRESHOLD_IN_KB = 1024 * 100;
    private static final int PROGRESS_MIN_STEP = 10;


    /**
     * @throws IOException Can be a HttpIOException
     */
    public static String get(URL pUrl, Map<String, String> pParameters, String userAgent) throws HttpIOException {
        InputStream httpResponseStream = null;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            for (Entry<String, String> parameter : pParameters.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(parameter.getKey(), parameter.getValue()));
            }

            HttpClient httpclient = new DefaultHttpClient();
            String params = pParameters.isEmpty() ? "" : "?" + URLEncodedUtils.format(nameValuePairs, "utf-8");
            HttpGet httpGet = new HttpGet(pUrl.toString() + params);
            httpGet.setHeader("User-Agent", userAgent);
            HttpResponse response = httpclient.execute(httpGet);
            int httpStatusCode = response.getStatusLine().getStatusCode();

            // Authentication failure.
            if (httpStatusCode == 401) {
                throw new HttpIOException(pUrl, httpStatusCode);
            }
            // Any error, or redirection, anything else than a success
            else if ((httpStatusCode < 200) || (httpStatusCode >= 300)) {
                throw new HttpIOException(pUrl, httpStatusCode);
            }
            // Success.
            else {
                httpResponseStream = response.getEntity().getContent();
                return readStreamToString(httpResponseStream);
            }
        } catch (HttpIOException eHttpIOException) {
            throw eHttpIOException;
        } catch (IOException eIOException) {
            throw new HttpIOException(pUrl, eIOException);
        } finally {
            if (httpResponseStream != null) try {
                httpResponseStream.close();
            } catch (IOException eIOException) {
                Logger.error(IOUtil.class, eIOException);
            }
        }
    }

    /**
     * @throws IOException Can be a HttpIOException
     */
    public static String post(URL pUrl, Map<String, String> pParameters, String userAgent) throws HttpIOException {
        InputStream httpResponseStream = null;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            for (Entry<String, String> parameter : pParameters.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(parameter.getKey(), parameter.getValue()));
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(pUrl.toString());
            httpPost.setHeader("User-Agent", userAgent);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httpPost);
            int httpStatusCode = response.getStatusLine().getStatusCode();

            // Authentication failure.
            if (httpStatusCode == 401) {
                throw new HttpIOException(pUrl, httpStatusCode);
            }
            // Any error, or redirection, anything else than a success
            else if ((httpStatusCode < 200) || (httpStatusCode >= 300)) {
                throw new HttpIOException(pUrl, httpStatusCode);
            }
            // Success.
            else {
                httpResponseStream = response.getEntity().getContent();
                return readStreamToString(httpResponseStream);
            }
        } catch (HttpIOException eHttpIOException) {
            throw eHttpIOException;
        } catch (IOException eIOException) {
            throw new HttpIOException(pUrl, eIOException);
        } finally {
            if (httpResponseStream != null) try {
                httpResponseStream.close();
            } catch (IOException eIOException) {
                Logger.error(IOUtil.class, eIOException);
            }
        }
    }

    public static String downloadUrlToString(URL pUrl) throws IOException {
        Logger.info(IOUtil.class, "Downloading file from URL '%1$s'", pUrl);
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = openConnection(pUrl);
            // Opens a connection and then sends GET & headers
            // Can't get status (i.e. getResponseCode()) before getInputStream.
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            // If connection is okay, download the Url to a string.
            checkConnectionOk(pUrl, urlConnection);
            return readStreamToString(inputStream);
        } finally {
            try {
                if (urlConnection != null) urlConnection.disconnect();
                if (inputStream != null) inputStream.close();
            } catch (IOException eIOException) {
                Logger.error(IOUtil.class, eIOException);
            }
        }
    }

    public static void downloadUrlToFile(URL pUrl, File pToFile, IOProgressHandler pProgressHandler) throws IOException {
        Logger.info(IOUtil.class, "Downloading file from URL '%1$s' to %2$s", pUrl, pToFile);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(pToFile);
            downloadUrlToStream(pUrl, outputStream, pProgressHandler);
        } finally {
            try {
                if (outputStream != null) outputStream.close();
            } catch (IOException eIOException) {
                Logger.error(IOUtil.class, eIOException);
            }
        }
    }

    public static void downloadUrlToStream(URL pUrl, OutputStream outputStream, IOProgressHandler pProgressHandler)
        throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = openConnection(pUrl);
            // Opens a connection and then sends GET & headers
            // Can't get status (i.e. getResponseCode()) before getInputStream.
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            // File length works for files until 2GB, which is the limit for an Integer.
            int expectedFileLength = urlConnection.getContentLength();

            // If connection is okay, download the Url content to the specified file.
            checkConnectionOk(pUrl, urlConnection);
            downloadStreamToStream(inputStream, outputStream, pProgressHandler, expectedFileLength);
        } finally {
            try {
                if (urlConnection != null) urlConnection.disconnect();
                if (inputStream != null) inputStream.close();
            } catch (IOException eIOException) {
                Logger.error(IOUtil.class, eIOException);
            }
        }
    }

    public static void downloadStreamToStream(InputStream pInputStream,
                                              OutputStream pOutputStream,
                                              IOProgressHandler pProgressHandler,
                                              int pExpectedLength) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE]; // TODO In configuration?
        int read = 0;
        int progress = 0;
        int totalRead = 0;
        int progressThreshold = Math.min(PROGRESS_THRESHOLD_IN_KB, pExpectedLength / PROGRESS_MIN_STEP);

        pProgressHandler.onProgress(pExpectedLength, totalRead);
        while ((read = pInputStream.read(buffer)) != -1) {
            progress += read;
            totalRead += read;
            pOutputStream.write(buffer, 0, read);

            if (progress > progressThreshold) {
                progress = 0;
                pProgressHandler.onProgress(pExpectedLength, totalRead);
            }
        }
        pProgressHandler.onProgress(pExpectedLength, totalRead);
        pProgressHandler.onFinished();
    }

    public static HttpURLConnection openConnection(URL pUrl) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) pUrl.openConnection();
        urlConnection.setConnectTimeout(URL_CONNECTION_TIMEOUT);
        urlConnection.setReadTimeout(URL_READ_TIMEOUT);
        return urlConnection;
    }

    public static void checkConnectionOk(URL pUrl, HttpURLConnection pUrlConnection) throws IOException {
        // If request is redirected! Probably we are on a freewifi-like hotspot that requires
        // login...
        if (pUrl.getHost().equals(pUrlConnection.getURL().getHost())) {
            // A status in the range [200, 300[ means success.
            int status = pUrlConnection.getResponseCode();
            if ((status < 200) || (status >= 300)) {
                Logger.warn(IOUtil.class,
                            "Could not download Url '%1$s'. Server responded %2$s - %3$s.",
                            pUrl,
                            pUrlConnection.getResponseCode(),
                            pUrlConnection.getResponseMessage());
                throw new ConnectException();
            }
        } else {
            Logger.warn(IOUtil.class, "Url was redirected from '%1$s' to host %2$s.", pUrl.getHost(), pUrlConnection.getURL());
            throw new ConnectException();
        }
    }

    // TODO Remove files if unzipping fails and notify error.
    // TODO Remove original zip file after decompression.
    public static void unzipFileToDirectory(File pFile, File pToDirectory) throws FileNotFoundException, IOException {
        ZipInputStream zipInputStream = null;
        if (pToDirectory == null || !pToDirectory.isDirectory()) {
            throw new InvalidDirectoryException(pToDirectory);
        }

        try {
            Logger.info(IOUtil.class, "Decompressing zip file '%1$s'", pFile);
            zipInputStream = new ZipInputStream(new FileInputStream(pFile));
            ZipEntry zipEntry = null;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                // Process directories.
                if (zipEntry.isDirectory()) {
                    unzipDirectory(zipEntry, pToDirectory);
                }
                // Process files.
                else {
                    unzipFile(zipInputStream, zipEntry, pToDirectory);
                }
            }
        } finally {
            try {
                if (zipInputStream != null) zipInputStream.close();
            } catch (IOException eIOException) {
                Logger.error(IOUtil.class, eIOException);
            }
        }
    }

    public static void unzipDirectory(ZipEntry pZipEntry, File pToDirectory) {
        File inflatedDirectory = new File(pToDirectory, pZipEntry.getName());
        Logger.info(IOUtil.class, "Inflating directory '%1$s'", inflatedDirectory);
        if (!inflatedDirectory.isDirectory()) {
            inflatedDirectory.mkdirs();
        }
    }

    public static void unzipFile(ZipInputStream pZipInputStream, ZipEntry pZipEntry, File pToDirectory) {
        File inflatedFile = new File(pToDirectory, pZipEntry.getName());

        // Creating parent directory if neccessary. We need to do this because often,
        // directories come after the file they contain in a ZIP stream...
        File containingDirectory = inflatedFile.getParentFile();
        if (!containingDirectory.isDirectory()) {
            containingDirectory.mkdirs();
        }

        // Decompresses the zipped file.
        try {
            Logger.info(IOUtil.class, "Inflating file '%1$s'", inflatedFile);
            readStreamToFile(pZipInputStream, inflatedFile);
        } catch (FileNotFoundException eFileNotFoundException) {
            // We don't warn about errors yet.
            Logger.error(IOUtil.class, eFileNotFoundException);
        } catch (IOException eIOException) {
            // We don't warn about errors yet.
            Logger.error(IOUtil.class, eIOException);
        } finally {
            try {
                pZipInputStream.closeEntry();
            } catch (IOException eIOException) {
                Logger.error(IOUtil.class, eIOException);
            }
        }
    }

    public static String readAssetToString(AssetManager assetManager, String pAssetPath) throws IOException {
        InputStream input = null;
        try {
            input = assetManager.open(pAssetPath);
            // File can't be more than 2 Go...
            byte[] inputBuffer = new byte[input.available()];
            input.read(inputBuffer);
            return new String(inputBuffer);
        } finally {
            try {
                if (input != null) input.close();
            } catch (IOException ioException) {
                Logger.error(IOUtil.class, ioException);
            }
        }
    }

    public static String readFileToString(File pFile) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(pFile);
            return readStreamToString(inputStream);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException eIOException) {
                Logger.error(IOUtil.class, eIOException);
            }
        }
    }

    /**
     * Bufferize automatically.
     */
    public static String readStreamToString(InputStream pInputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        // Note: Be careful as we use the default platform charset here. Maybe we should enforce a
        // specific one.
        BufferedReader reader = new BufferedReader(new InputStreamReader(pInputStream));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            result.append(line);
        }
        return result.toString();
    }

    public static void readStreamToFile(InputStream pInputStream, File pToFile) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(pToFile);
            readStreamToStream(pInputStream, outputStream);
        } finally {
            try {
                if (outputStream != null) outputStream.close();
            } catch (IOException eIOException) {
                Logger.error(IOUtil.class, eIOException);
            }
        }
    }

    public static void readStreamToStream(InputStream pInputStream, OutputStream pOutputStream) throws IOException {
        int read = 0;
        byte[] buffer = new byte[BUFFER_SIZE]; // TODO In configuration?
        while ((read = pInputStream.read(buffer)) != -1) {
            pOutputStream.write(buffer, 0, read);
        }
    }

    public static void deleteDirectory(File pDirectory) {
        if (pDirectory.isDirectory()) {
            for (File fileOrDirectory : pDirectory.listFiles()) {
                if (fileOrDirectory.isDirectory()) {
                    deleteDirectory(fileOrDirectory);
                } else {
                    fileOrDirectory.delete();
                }
            }
        }
        pDirectory.delete();
    }
}
