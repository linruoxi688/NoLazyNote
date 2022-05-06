package com.example.nolazynote.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.nolazynote.entity.ToDos;
import com.example.nolazynote.R;

import java.io.InputStream;
import java.util.List;

public class ToDoListViewAdapter extends BaseAdapter {

    private List<ToDos> todosList;
    private Context context;
    private LayoutInflater mInflater;

    public ToDoListViewAdapter(LayoutInflater inflater, List<ToDos> todos, Context context) {
        this.todosList = todos;
        this.context=context;
        this.mInflater = inflater;
    }

    @Override
    public int getCount() {
        return todosList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View viewToDo = mInflater.inflate(R.layout.item_todo, null);
        ToDos toDos = todosList.get(position);
        TextView todo_title = (TextView) viewToDo.findViewById(R.id.todo_title);
        TextView todo_desc = (TextView) viewToDo.findViewById(R.id.todo_desc);
        TextView todo_date = (TextView) viewToDo.findViewById(R.id.todo_date);
        TextView isRepeat = (TextView) viewToDo.findViewById(R.id.isRepeat);
        ImageView card_background = (ImageView) viewToDo.findViewById(R.id.card_bg);
        todo_title.setText(toDos.getTitle());
        todo_desc.setText(toDos.getDesc());
        todo_date.setText(toDos.getDate() + " "+ toDos.getTime());
        BitmapFactory.Options opt = new  BitmapFactory.Options();
        opt.inPreferredConfig =  Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is =  context.getResources().openRawResource(toDos.getImgId());
        card_background.setImageBitmap(BitmapFactory.decodeStream(is, null, opt));
        if (toDos.getIsRepeat() == 1){
            isRepeat.setText("重复");
        }else {
            isRepeat.setText("单次");
        }
        isRepeat.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        return viewToDo;
    }
}
