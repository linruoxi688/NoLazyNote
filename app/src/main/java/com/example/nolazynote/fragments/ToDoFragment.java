package com.example.nolazynote.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.example.nolazynote.Adapter.ToDoListViewAdapter;
import com.example.nolazynote.MainActivity;
import com.example.nolazynote.activity.TomatoClockActivity;
import com.example.nolazynote.activity.TomatoClockSettingsActivity;
import com.example.nolazynote.entity.ToDos;
import com.example.nolazynote.Dao.ToDoDao;
import com.example.nolazynote.activity.NewToDoActivity;
import com.example.nolazynote.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ToDoFragment extends Fragment {

    private ListView todosListView;
    private ListView alreadyDosListView;
    private List<ToDos> todosList = new ArrayList<>();
    private List<ToDos> alreadyDosList = new ArrayList<>();
    private ToDoListViewAdapter todoListViewAdapter;
    private ToDoListViewAdapter alreadyDoListViewAdapter;
    private List<ToDos> localTodo;
    private LayoutInflater inflater_last;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private ToDos finishItem;
    private Button fabnew;
//    private static String userId = MainActivity.userId;

    public ToDoFragment() {
        // Required empty public constructor
    }

//    public static ToDoFragment newInstance(String arg) {
//        ToDoFragment fragment = new ToDoFragment();
//        userId = arg;
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    public void setUserId(String userId) {
//        this.userId = userId;
//    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todo, container, false);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        todosListView = (ListView) rootView.findViewById(R.id.toDoListView);
        inflater_last = LayoutInflater.from(getActivity());
        setTodosList();
        setAlreadydosList();
        todoListViewAdapter = new ToDoListViewAdapter(inflater_last, todosList, getActivity());
        todosListView.setAdapter(todoListViewAdapter);

        alreadyDosListView = (ListView) rootView.findViewById(R.id.alreadyDoListView);
        alreadyDoListViewAdapter = new ToDoListViewAdapter(inflater_last, alreadyDosList, getActivity());
        alreadyDosListView.setAdapter(alreadyDoListViewAdapter);
        todosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), TomatoClockSettingsActivity.class);
                ToDos temp = todosList.get(i);
                String todoTitle = temp.getTitle();
                intent.putExtra("title",todoTitle);
                startActivity(intent);
            }
        });
        todosListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int i, long l) {
                Log.i("todo", "delete " + i + l);
                alert = null;
                builder = new AlertDialog.Builder(getActivity() );
                alert = builder.setIcon(R.mipmap.ic_launcher)
                        .setTitle("待办设置")
                        .setMessage("待办事项设置")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToDos temp = todosList.get(i);
                                todosList.remove(i);
                                new ToDoDao(getContext()).delete(temp.getId());
                                todoListViewAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNeutralButton("完成", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishItem = todosList.get(i);
                                todosList.remove(i);
                                alreadyDosList.add(finishItem);
                                new ToDoDao(getContext()).setFinished(finishItem.getId());
                                todoListViewAdapter.notifyDataSetChanged();
                                alreadyDoListViewAdapter.notifyDataSetChanged();
                            }
                        }).create();
                alert.show();
                return true;
            }
        });
        alreadyDosListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int i, long l) {
                Log.i("alreadydo", "delete " + i + l);
                alert = null;
                builder = new AlertDialog.Builder(getActivity() );
                alert = builder.setIcon(R.mipmap.ic_launcher)
                        .setTitle("待办设置：")
                        .setMessage("完成事项设置")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToDos temp = alreadyDosList.get(i);
                                alreadyDosList.remove(i);
                                new ToDoDao(getContext()).delete(temp.getId());
                                alreadyDoListViewAdapter.notifyDataSetChanged();
                            }
                        }).create();
                alert.show();
                return true;
            }
        });
        fabnew =  rootView.findViewById(R.id.fabnew);
        fabnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewToDoActivity.class);
                Log.i("todoFragment",MainActivity.userId);
                Bundle bundle = new Bundle();
                bundle.putString("user", MainActivity.userId);
                intent.putExtra("users", bundle);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void setTodosList(){
        Calendar calendar = Calendar.getInstance();

        String todayDate = calendar.get(Calendar.YEAR) + "年"

                + (calendar.get(Calendar.MONTH)+1) + "月"//从0计算

                + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        Log.i("checkisok",todayDate);
        localTodo = new ToDoDao(getContext()).getAllTask(MainActivity.userId,todayDate);
        if (localTodo != null && localTodo.size() > 0) {
            todosList.clear();
            todosList.addAll(localTodo);
        }
        else{
            Log.i("check123","SETTODOLIST Failed");
        }
        Log.i("todolistSize","%%%%%%%%%%" + localTodo.size());
    }

    private void setAlreadydosList(){
        localTodo = new ToDoDao(getContext()).getAllFinishedTask(MainActivity.userId);
        if (localTodo != null && localTodo.size() > 0) {
            alreadyDosList.clear();
            alreadyDosList.addAll(localTodo);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("resume","todoresume++++++++++++++++++++++++++++++");
        setTodosList();
        setAlreadydosList();
        todoListViewAdapter.notifyDataSetChanged();
        alreadyDoListViewAdapter.notifyDataSetChanged();
    }
}