/**
 * Custom ArrayAdapter for the data that we need to display in a single ListItem which is defined by the earthquake_list_item.xml file.
 *
 * @author Manan Kalra
 */

package io.github.manankalra.tremor;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<io.github.manankalra.tremor.Earthquake> {

    private static final String LOCATION_SEPARATOR = " of ";

    /**
     * Constructor
     *
     * @param context     of the app.
     * @param earthquakes is the list of quakes, which is the data source of the adapter.
     */
    public EarthquakeAdapter(Context context, List<io.github.manankalra.tremor.Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    public String[] locationFormat(io.github.manankalra.tremor.Earthquake e) {
        String originalLocation = e.getLocation();
        String location;
        String locationMain;
        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            location = parts[0];
            locationMain = parts[1];
            return parts;
        } else {
            location = getContext().getString(R.string.near_the);
            locationMain = originalLocation;
            String[] parts = {location, locationMain};
            return parts;
        }
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 1:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }
        io.github.manankalra.tremor.Earthquake currentEarthquake = getItem(position);
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        magnitudeView.setText(currentEarthquake.getMagnitude());
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(Double.parseDouble(currentEarthquake.getMagnitude().toString()));
        magnitudeCircle.setColor(magnitudeColor);
        String loc[] = locationFormat(currentEarthquake);
        TextView locationView = (TextView) listItemView.findViewById(R.id.location);
        locationView.setText(loc[0] + LOCATION_SEPARATOR);
        TextView locationMainView = (TextView) listItemView.findViewById(R.id.locationMain);
        locationMainView.setText(loc[1]);
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(currentEarthquake.getDate());
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        timeView.setText(currentEarthquake.getTime());
        return listItemView;
    }
}
