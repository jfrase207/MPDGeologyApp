package jfrase207.bgsdatastarter.mpd.gcu.mpdgeologyapp;


/*  Starter project for Mobile Platform Development in Semester B Session 2018/2019
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 Jamie Fraser
// Student ID           S1718278
// Programme of Study   Computer Games Software Development
//

// Update the package name to include your Student Identifier


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jfrase207.bgsdatastarter.mpd.gcu.mpdgeologyapp.XMLParser.parse;

public class MainActivity extends AppCompatActivity
{
    public static final String EXTRA_MESSAGE = "jfrase207.bgsdatastarter.mpd.gcu.mpdgeologyapp.POSITION";
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    List<XMLParser.Item> myItems = new ArrayList<>();
    private Spinner spinner;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public Button button;
    public static MainActivity current;
    public static XMLParser.Item[] originalmDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        current = this;
        startProgress();
    }

    public void resViewSetup(List<XMLParser.Item> thisList)
    {
        recyclerView = findViewById(R.id.rView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        XMLParser.Item[] items = new XMLParser.Item[thisList.size()];
        int i = 0;
        for (XMLParser.Item Item: thisList)
        {
            items[i] = Item;
            i++;
        }

        //Update Recycler view list
        mAdapter = new MyAdapter(items);
        recyclerView.setAdapter(mAdapter);
    }


    public void upRecView(String starDate, String endDate)
    {
        List<XMLParser.Item> list = new ArrayList<>();

        boolean start = false;


        for (XMLParser.Item item: myItems) {
            if(item.pubDate == endDate)
                start = true;
            else if(item.pubDate == starDate)
                start = false;

            if(start)
                list.add(item);
        }



        XMLParser.Item[] newArray = new XMLParser.Item[list.size()];

        int i = 0;
        for (XMLParser.Item Item: list)
        {
            newArray[i] = Item;
            i++;
        }



        mAdapter = new MyAdapter(newArray);
        recyclerView.setAdapter(mAdapter);
    }

    public void upRecViewMag(String maxMag,String minMag)
    {
        List<XMLParser.Item> magList = new ArrayList<>();

        for (XMLParser.Item item: myItems) {

            if(Float.parseFloat(item.magnitude) <= Float.parseFloat(maxMag) && Float.parseFloat(item.magnitude) >= Float.parseFloat(minMag))
                magList.add(item);
        }


        XMLParser.Item[] newArray = new XMLParser.Item[magList.size()];

        int i = 0;
        for (XMLParser.Item Item: magList)
        {
            newArray[i] = Item;
            i++;
        }


        mAdapter = new MyAdapter(newArray);
        recyclerView.setAdapter(mAdapter);
    }


    public void addItemsToSpinner(List<XMLParser.Item> items)
    {
        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        spinner4 = findViewById(R.id.spinner4);
        List<String> list = new ArrayList<>();
        List<String> magList = new ArrayList<>();
        Float maglist[];
        String dateList[];

        int i = 0;
        int j = 1;
        for (XMLParser.Item item:items) {

            if(!list.contains(item.pubDate))
                list.add(item.pubDate);


            if(!magList.contains(item.magnitude)) {
                magList.add(item.magnitude);

            }
        }

        maglist =  new Float[magList.size()];

        for (String item:magList) {
            maglist[i] = Float.parseFloat(item);
            i++;
        }

        dateList = new String[list.size()];

        for (String item:list) {
            dateList[list.size()-j] = item;
            j++;
        }





        Arrays.sort(maglist);

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dateList);
        final ArrayAdapter<Float> magAdapter = new ArrayAdapter<Float>(this, android.R.layout.simple_spinner_item, maglist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        magAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter);
        spinner3.setAdapter(magAdapter);
        spinner4.setAdapter(magAdapter);

        spinner.setSelection(0);
        spinner2.setSelection(dataAdapter.getCount()-1);
        spinner3.setSelection(magAdapter.getCount()-1);
        spinner4.setSelection(0);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            upRecView(spinner.getSelectedItem().toString(),spinner2.getSelectedItem().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parentView) {

        }

    });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                upRecView(spinner.getSelectedItem().toString(),spinner2.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                upRecViewMag(spinner3.getSelectedItem().toString(),spinner4.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                upRecViewMag(spinner3.getSelectedItem().toString(),spinner4.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }




    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            try {

                aurl = new URL(url);
                yc = aurl.openConnection();

                myItems = parse(yc.getInputStream());

                for (XMLParser.Item item : myItems)
                {
                    item.title = item.summary.split(";")[1];

                    String[] date = item.pubDate.split(" ");
                    item.pubDate = date[1];
                    item.pubDate += " " + date[2];
                    item.pubDate += " " + date[3];

                    String[] mag = item.summary.split(";");
                    String[] newMag = mag[4].split(":");
                    item.magnitude = newMag[1];

                }

            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            MainActivity.this.runOnUiThread(new Runnable() {

                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    resViewSetup(myItems);
                    addItemsToSpinner(myItems);

                }
                //rawDataDisplay.setText(myItems.get(0).title);
            });
        }

    }

}