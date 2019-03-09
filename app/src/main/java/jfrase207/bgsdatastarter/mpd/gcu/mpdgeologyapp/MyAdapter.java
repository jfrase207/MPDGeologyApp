package jfrase207.bgsdatastarter.mpd.gcu.mpdgeologyapp;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static jfrase207.bgsdatastarter.mpd.gcu.mpdgeologyapp.MainActivity.EXTRA_MESSAGE;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    public  static MyAdapter current;
    public static XMLParser.Item[] mDataset;


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        protected TextView title;
        protected TextView description;
        protected TextView link;

        public MyViewHolder(View v)
        {
            super(v);
            title = v.findViewById(R.id.title);
            description = v.findViewById(R.id.description);
            link = v.findViewById(R.id.link);

        }
    }

    public MyAdapter(XMLParser.Item[] myDataset)
    {
        current = this;
        mDataset = myDataset;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position) {

        holder.title.setText(mDataset[position].title);
        holder.description.setText(mDataset[position].summary);
        holder.link.setText(mDataset[position].link);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.current, MapActivity.class);
                intent.putExtra(EXTRA_MESSAGE, position);
                MainActivity.current.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return mDataset.length;
    }
}