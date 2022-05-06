package com.example.nolazynote.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nolazynote.Dao.NoteDao;
import com.example.nolazynote.MainActivity;
import com.example.nolazynote.R;
import com.example.nolazynote.activity.AddTaskActivity;
import com.example.nolazynote.entity.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class TaskFragment extends Fragment {
    ListView listView;
    private List<Task> taskList = new ArrayList<Task>();
    private TaskAdapter taskAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NoteDao noteDao =new NoteDao(this.getContext());
        taskList = noteDao.getAll(MainActivity.userId);
    }

    @Override
    public void onResume() {
        super.onResume();
        NoteDao noteDao =new NoteDao(this.getContext());
        taskList = noteDao.getAll(MainActivity.userId);
        taskAdapter = new TaskAdapter();
        listView.setAdapter(taskAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        taskAdapter = new TaskAdapter();
        View mView = inflater.inflate(R.layout.fragment_task, container, false);
        Button btn = mView.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),AddTaskActivity.class);
                startActivity(intent);
            }
        });
        listView = mView.findViewById(R.id.list_view);
        listView.setAdapter(taskAdapter);
        return mView;
    }

    private class TaskAdapter extends BaseAdapter {
        //返回多少条记录
        @Override
        public int getCount() {
            System.out.println("task数量为："+taskList.size());
            // TODO Auto-generated method stub
            return taskList.size();
        }
        //每一个item项，返回一次界面
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            //布局不变，数据变
            //如果缓存为空，我们生成新的布局作为1个item
            if (convertView == null) {
                Log.i("info:", "没有缓存，重新生成" + position);
                //因为getView()返回的对象，adapter会自动赋给ListView
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.task, null);
            } else {
                Log.i("info:", "有缓存，不需要重新生成" + position);
                view = convertView;
            }

            Task m = taskList.get(position);

            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            final String taskContent = m.getContent();
            tv_content.setText(taskContent);
            tv_content.setTextSize(12);

            TextView tv_datetime = (TextView) view.findViewById(R.id.tv_date);
            tv_datetime.setText(m.getDate());
            tv_datetime.setTextSize(12);

            TextView tv_id = view.findViewById(R.id.tv_id);
            final String taskId = m.getTaskId()+"";
            tv_id.setText(taskId);

            LinearLayout linearLayout = view.findViewById(R.id.lin_note);
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    delete(Integer.parseInt(taskId));
                    return true;
                }
            });

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("content",taskContent);
                    intent.setClass(getActivity(),AddTaskActivity.class);
                    startActivity(intent);
                    NoteDao dao = new NoteDao(getContext());
                    int resultCode = dao.delete(Integer.parseInt(taskId));//验证删除了几条数据
                    taskList = dao.getAll(MainActivity.userId);
                    taskAdapter = new TaskAdapter();
                    listView.setAdapter(taskAdapter);
                }
            });

            return view;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public void delete(final long id){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("提示");
            builder.setMessage("确认删除吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    NoteDao dao = new NoteDao(getContext());
                    int resultCode = dao.delete(id);//验证删除了几条数据
                    taskList = dao.getAll(MainActivity.userId);
                    taskAdapter = new TaskAdapter();
                    listView.setAdapter(taskAdapter);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        }

    };




}