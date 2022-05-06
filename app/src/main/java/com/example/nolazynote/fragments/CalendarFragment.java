package com.example.nolazynote.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.nolazynote.MainActivity;
import com.example.nolazynote.activity.AddRelationActivity;
import com.example.nolazynote.Dao.Matter_update;
import com.example.nolazynote.DaoHelper.MyDatabaseHelper;
import com.example.nolazynote.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    private ListView listView;
    String date="";
    private CalendarView calendar;
    private Button add_sym;
    private boolean isGetData = false;
    private ImageView add;
    private LayoutInflater inflater_last;
    //public static AutoTouch autoTouch = new AutoTouch();
    //private static final String TAG = "MainActivity";

    public CalendarFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isGetData = true;
        //autoTouch.autoClickRatio(getActivity(),1,1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_main1, container, false);
        //setContentView(R.layout.activity_main1);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        listView=(ListView) rootView.findViewById(R.id.listView);
        calendar = (CalendarView) rootView.findViewById(R.id.calendarView3);
        //add = (ImageView) rootView.findViewById(R.id.imageView);
        add_sym = rootView.findViewById(R.id.add_sym);
        inflater_last = LayoutInflater.from(getActivity());
        getDataFromDB();
        add_sym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AddRelationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence("id",date);
                intent.putExtras(bundle);
                startActivity(intent);
                //startActivityForResult(intent,0x111);
            }
        });
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = String.valueOf(year)+String.valueOf(month)+String.valueOf(dayOfMonth);

                //System.out.println(data);
                getDataFromDB();

            }
        });

        //super.onCreate(savedInstanceState);
        //autoTouch.autoClickRatio(getActivity(),1,1);
        return rootView;
    }

    private void getDataFromDB()
    {
        final MyDatabaseHelper dbHelper=new MyDatabaseHelper(this.getActivity().getApplicationContext(), "Data.db");
        Cursor cursor=dbHelper.query(date, MainActivity.userId);
        String[] from={"todotitle","todotime"};
        int[] to={R.id.title_,R.id.des_};
        SimpleCursorAdapter scadapter=new SimpleCursorAdapter(this.getActivity().getApplicationContext(),R.layout.relationlist,cursor,from,to);
        listView.setAdapter(scadapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
                Intent intent=new Intent(getActivity(), Matter_update.class);
                Bundle bundle = new Bundle();
                //System.out.println(data);
                int t = (int)id;
                bundle.putCharSequence("id",Integer.toString(t));
                intent.putExtras(bundle);
                startActivityForResult(intent,0x111);
            }
        });
        /*Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        dbHelper.del(data);
                        Cursor cursor1 = dbHelper.query(data);
                        String[] from={"name","tel","groupName"};
                        int[] to={R.id.name,R.id.tel,R.id.group};
                        SimpleCursorAdapter scadapter=new SimpleCursorAdapter(getApplicationContext(),R.layout.relationlist,cursor1,from,to);
                        MainActivity.this.listView.setAdapter(scadapter);

            }
        });*/
        dbHelper.close();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isGetData){
            return;
        }else {
            getDataFromDB();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromDB();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x111&&resultCode==0x111)
            getDataFromDB();
    }

}