package com.example.lenovo.openstreetwms;

import android.util.Log;

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.MapTileIndex;

import java.util.Locale;

public class WMSTileProvider extends OnlineTileSourceBase {

    // Web Mercator n/w corner of the map.
    private static final double[] TILE_ORIGIN = {-20037508.34789244, 20037508.34789244};
    //array indexes for that data
    private static final int ORIG_X = 0;
    private static final int ORIG_Y = 1; // "

    // Size of square world map in meters, using WebMerc projection.
    private static final double MAP_SIZE = 20037508.34789244 * 2;

    // array indexes for array to hold bounding boxes.
    protected static final int MINX = 0;
    protected static final int MAXX = 1;
    protected static final int MINY = 2;
    protected static final int MAXY = 3;
    private String layer="";
    private String TAG = "WMSTileProvider";

    public WMSTileProvider(String aName, int aZoomMinLevel, int aZoomMaxLevel, int aTileSizePixels, String aImageFilenameEnding, String[] aBaseUrl) {
        super(aName, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels, aImageFilenameEnding, aBaseUrl);
    }

    /*
    final String WMS_FORMAT_STRING =
            "%s" +
                    "?service=WMS" +
                    "&version=1.1.1" +
                    "&request=GetMap" +
                    "&layers=%s" +
                    "&bbox=%f,%f,%f,%f" +
                    "&width=256" +
                    "&height=256" +
                    "&srs=EPSG:900913" +
                    "&format=image/png" +
                    "$styles=default" +
                    "&transparent=true";
*/
    // Return a web Mercator bounding box given tile x/y indexes and a zoom
    // level.
    protected double[] getBoundingBox(int x, int y, int zoom) {
        double tileSize = MAP_SIZE / Math.pow(2, zoom);
        double minx = TILE_ORIGIN[ORIG_X] + x * tileSize;
        double maxx = TILE_ORIGIN[ORIG_X] + (x+1) * tileSize;
        double miny = TILE_ORIGIN[ORIG_Y] - (y+1) * tileSize;
        double maxy = TILE_ORIGIN[ORIG_Y] - y * tileSize;

        double[] bbox = new double[4];
        bbox[MINX] = minx;
        bbox[MINY] = miny;
        bbox[MAXX] = maxx;
        bbox[MAXY] = maxy;

        if(bbox != null) {
            Log.d(TAG, "getBoundingBox: The bounding box is bbox = " + bbox);
        }
        return bbox;
    }


    @Override
    public String getTileURLString(long pMapTileIndex) {
        double[] bbox = getBoundingBox(MapTileIndex.getX(pMapTileIndex),
                MapTileIndex.getY(pMapTileIndex),
                MapTileIndex.getZoom(pMapTileIndex));
        String s = getBaseUrl()+ String.valueOf(bbox[0])+","+String.valueOf(bbox[2])+","+String.valueOf(bbox[1])+","+String.valueOf(bbox[3]);
        Log.d("WMS String", s);
        return s;
    }
}
