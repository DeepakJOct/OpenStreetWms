package com.example.lenovo.openstreetwms;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;

public class MainActivity extends AppCompatActivity {

    MapView mMap;
    Button showWms, showMapnikTileSource;

    //Declared global in order to remove it when required
    private TilesOverlay theTileOverlay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        mMap = (MapView) findViewById(R.id.mapView);
        showMapnikTileSource = (Button) findViewById(R.id.show_mapnik);
        showWms = (Button) findViewById(R.id.show_wms);
        mMap.getOverlayManager().clear();

        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMap.setMultiTouchControls(true);
        mMap.getController().setZoom(15.1);
        mMap.setMaxZoomLevel(22.0);

/*
        mMap.setTileSource(new OnlineTileSourceBase("gpw-v3-population-density_2000", 0, 18, 256, "",
                new String[] { "http://sedac.ciesin.columbia.edu/geoserver/wms?service=WMS&version=1.1.1&request=GetMap" }) {
            @Override
            public String getTileURLString(long pMapTileIndex) {
                Log.d("pMapTileIndex", "getTileURLString: The pMapTileIndex is " + pMapTileIndex);
                return getBaseUrl()
                        + MapTileIndex.getZoom(pMapTileIndex)
                        + "/" + MapTileIndex.getY(pMapTileIndex)
                        + "/" + MapTileIndex.getX(pMapTileIndex)
                        + mImageFilenameEnding;

            }
        });

        */
//        mMap.setTileSource(TileSourceFactory.MAPNIK);
//
//        mMap.setTileSource(new OnlineTileSourceBase("india_india_bluemarble3857.ecw", 0, 18, 256, ".jpeg",
//                new String[] {"https://soiimageservice.nic.in:443/erdas-iws/ogc/wms/?"}) {
//            @Override
//            public String getTileURLString(long pMapTileIndex) {
//                return getBaseUrl()
//                        + MapTileIndex.getZoom(pMapTileIndex)
//                        + "/" + MapTileIndex.getX(pMapTileIndex)
//                        + "/" + MapTileIndex.getY(pMapTileIndex)
//                        + mImageFilenameEnding;
//            }
//        });

        //setTileSource();
        //mMap.setTileSource(TileSourceFactory.MAPNIK);

        CompassOverlay compassOverlay = new CompassOverlay(this, mMap);
        compassOverlay.enableCompass();
        mMap.getOverlays().add(compassOverlay);

        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(this.mMap);
        mMap.getOverlays().add(scaleBarOverlay);

        //Zoom the map to India
        zoomToIndia();
        setWmsTile();
        setMapnikTileSource();


        /**

        mMap.setTileSource(new OnlineTileSourceBase("India Relief", 0, 18, 256, "jpeg",
                new String[] {"https://soiimageservice.nic.in:443/erdas-iws/erdas/imagex/?request=image&type=jpg&layers=/StateMosaicECW/Final-IndiaRelief/india_relief.ecw&style=default&sizex=100&sizey=100"}) {
            @Override
            public String getTileURLString(long pMapTileIndex) {
                return getBaseUrl()
                        + MapTileIndex.getZoom(pMapTileIndex)
                        + "/" + MapTileIndex.getY(pMapTileIndex)
                        + "/" + MapTileIndex.getX(pMapTileIndex)
                        + mImageFilenameEnding;
            }
        });

         */


    }

    public void zoomToIndia() {
        mMap.addOnFirstLayoutListener(new MapView.OnFirstLayoutListener() {
            @Override
            public void onFirstLayout(View v, int left, int top, int right, int bottom) {
                BoundingBox boundingBox= new BoundingBox(35.513327, 97.395359, 6.462700, 68.109700);
                mMap.zoomToBoundingBox(boundingBox, true);
                Log.d("Zoom to India", "Zooming to the Bounding Box of India!!");
            }
        });
        mMap.animate();
        mMap.setMinZoomLevel(5.1);
    }


    public void setWmsTile() {
        showWms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MapTileProviderBasic tileProviderForTileOverlay = new MapTileProviderBasic(MainActivity.this);

                final ITileSource tileSource = new WMSTileProvider("WMS", 0, 15, 256, ".png",
                            new String[] {"https://soiimageservice.nic.in/erdas-iws/ogc/wms?SERVICE=WMS&REQUEST=GetMap&VERSION=1.1.1&LAYERS=StateMosaicECW_Final-IndiaRelief_india_relief.ecw&STYLES=&FORMAT=image%2Fpng&TRANSPARENT=true&HEIGHT=512&WIDTH=512&SRS=EPSG%3A3857&BBOX="});

                System.out.println("Checking: The Tile Provider for Tile Overlay is: " + tileProviderForTileOverlay);
                System.out.println("Checking: The Tile Source for Tile Overlay is: " + tileSource);

                tileProviderForTileOverlay.setTileSource(tileSource);
                theTileOverlay = new TilesOverlay(tileProviderForTileOverlay, MainActivity.this);

                System.out.println("Checking: The final tile overlay is: " + theTileOverlay);
                mMap.getOverlays().add(theTileOverlay);
                mMap.invalidate();
            }
        });
    }

    public void setMapnikTileSource() {
        showMapnikTileSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mMap.getOverlays().remove(theTileOverlay);
                    mMap.setTileSource(TileSourceFactory.MAPNIK);
            }
        });
    }
}
