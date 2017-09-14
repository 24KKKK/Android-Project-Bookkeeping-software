package com.example.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SecondActivity extends Activity{
    ExpandableListView mainlistview = null;
    List<String> parent = null;
    Map<String, List<String>> map1 = null;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondactivity);
        mainlistview = (ExpandableListView) this
                .findViewById(R.id.expandableListView);
    }
    protected void onResume() {
        super.onResume();
        DatabaseHelper dbHelper=new DatabaseHelper(SecondActivity.this,"moneydb");
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from consume", null);
        //绑定Layout里面的ListView
        final ListView list = (ListView) findViewById(R.id.ListView01);
        //生成动态数组，加入数据
       final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        while(cursor.moveToNext())
        {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String time= cursor.getString(cursor.getColumnIndex("time"));
            String money = cursor.getString(cursor.getColumnIndex("money"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle",time+"    消费条目："+name);
            map.put("ItemText",  "金钱："+money+"元");
            map.put("id",id);
            listItem.add(map);
        }
        //生成适配器的Item和动态数组对应的元素
       final SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源
                R.layout.secondlistview,//ListItem的XML实现
                new String[] {"ItemTitle", "ItemText"},
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[] {R.id.ItemTitle,R.id.ItemText}
        );
        listItemAdapter.notifyDataSetInvalidated();
        list.setAdapter(listItemAdapter);



        list.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
            {
                final AlertDialog.Builder builder=new AlertDialog.Builder(SecondActivity.this);
                final AdapterView.AdapterContextMenuInfo menuinfo2 = (AdapterView.AdapterContextMenuInfo) menuInfo;

            //    int selectedPosition = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
                builder.setTitle("");
                builder.setMessage("确定要删除这条记录吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    { //添加函数
                        DatabaseHelper dbHelper=new DatabaseHelper(SecondActivity.this,"moneydb");
                        SQLiteDatabase db=dbHelper.getWritableDatabase();
                        String idstr;
                        idstr=listItem.get(menuinfo2.position).get("id").toString();
                        db.delete("consume","id=?",new String[]{idstr});
                        db.close();
                        dbHelper.close();
                        listItem.remove(menuinfo2.position);
                        list.setAdapter(listItemAdapter);
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
        db.close();
        dbHelper.close();
        cursor.close();
    }
   /* protected  void onResume(){
        super.onResume();
        parent = new ArrayList<String>();
        map1 = new HashMap<String, List<String>>();
        DatabaseHelper dbHelper=new DatabaseHelper(SecondActivity.this,"moneydb");
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("select distinct time,sum(money) from consume group by time order by time desc", null);
        while(cursor1.moveToNext())
        {
            String time= cursor1.getString(cursor1.getColumnIndex("time"));
            String sum= cursor1.getString(cursor1.getColumnIndex("sum(money)"));
            List<String> list1 = new ArrayList<String>();
            parent.add(time+"   总共花费   "+sum+" 元");

        }
        Iterator<String> stringIterator=parent.iterator();
        while(stringIterator.hasNext()){
            String string=stringIterator.next();
            List<String> list = new ArrayList<String>();
         Cursor   cursor2=db.rawQuery("select * from consume where time="+"'"+string.substring(0,10)+"'"+"order by id desc",null);
            while (cursor2.moveToNext()){
                String name = cursor2.getString(cursor2.getColumnIndex("name"));
                String money=cursor2.getString(cursor2.getColumnIndex("money"));
                list.add(name+" 花费 "+money+" 元");
            }
            map1.put(string,list);
            cursor2.close();
        }
        db.close();
        dbHelper.close();
        cursor1.close();
        mainlistview.setAdapter(new MyAdapter());
    }
    class MyAdapter extends BaseExpandableListAdapter {

        //得到子item需要关联的数据
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String key = parent.get(groupPosition);
            return (map1.get(key).get(childPosition));
        }

        //得到子item的ID
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        //设置子item的组件
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            String key = SecondActivity.this.parent.get(groupPosition);
            String info = map1.get(key).get(childPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) SecondActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.second, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.parent_textview);
            tv.setTag(R.id.childview,childPosition);
            tv.setTag(R.id.groupview,groupPosition);
            tv.setText(info);
            return tv;
        }

        //获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            String key = parent.get(groupPosition);
            int size=map1.get(key).size();
            return size;
        }
        //获取当前父item的数据
        @Override
        public Object getGroup(int groupPosition) {
            return parent.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return parent.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        //设置父item组件
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) SecondActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.second, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.parent_textview);
            tv.setText(SecondActivity.this.parent.get(groupPosition));
            tv.setTag(R.id.groupview,groupPosition);
            tv.setTag(R.id.childview,-1);
            return tv;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }
 */

}

