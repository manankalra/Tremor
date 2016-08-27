
/**
 * This class parses JSON data into useful information.
 *
 * @author Manan Kalra
 * @version 2.0
 */

package io.github.manankalra.tremor;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();

    private static ArrayList<Earthquake> data = new ArrayList<Earthquake>(); //ArrayList that contains the data to be displayed in the app

    /**
     * Formatting functions.
     */
    public static String decimalFormat(Double toFormat) {
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(toFormat);
    }

    public static String dateFormat(Date toFormat) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, ''yy");
        return dateFormatter.format(toFormat);
    }

    public static String timeFormat(Date toFormat) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        return timeFormatter.format(toFormat);
    }

    private static List<Earthquake> extractFeatureFromJson(String earthquakeJSON) {
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        List<Earthquake> earthquakes = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");
            for (int i = 0; i < earthquakeArray.length(); i++) {
                JSONObject temp = earthquakeArray.optJSONObject(i);
                JSONObject property = temp.optJSONObject("properties");
                Double value = Double.parseDouble(property.optString("mag").toString());
                String mag = decimalFormat(value).toString();
                String loc = property.optString("place").toString();
                long timeInMillis = Long.parseLong(property.optString("time").toString());
                Date dateObj = new Date(timeInMillis);
                String dat = dateFormat(dateObj);
                String time = timeFormat(dateObj);
                String res = property.optString("url");
                Earthquake obj = new Earthquake(mag, loc, dat, time, res);
                earthquakes.add(obj);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }

    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);
        return earthquakes;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
