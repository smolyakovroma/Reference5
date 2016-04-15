package ru.smolyakovroma.reference5;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.smolyakovroma.reference5.model.SprElement;

public class SprElementAdapter extends ArrayAdapter<SprElement> {

    private List<SprElement> list;


    public SprElementAdapter(Context context, List<SprElement> list) {
        super(context, R.layout.listview_row_element, R.id.txt_element_name, list);
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_row_element, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_element_name);
            viewHolder.txtCode = (TextView) convertView.findViewById(R.id.txt_element_code);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_source);
            viewHolder.imageViewArrow = (ImageView) convertView.findViewById(R.id.img_source_arrow);
            convertView.setTag(viewHolder);

        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        SprElement sprElement = list.get(position);
        holder.txtName.setText(sprElement.getName());
        holder.txtCode.setText(sprElement.getCode());

        if(!sprElement.isFolder()) {
            if(!sprElement.isRemove()){
                holder.imageView.setImageResource(R.drawable.element);
            }else{
                holder.imageView.setImageResource(R.drawable.element_remove);
            }
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.spr_element));
        }else{
            if(sprElement.isTopFolder()){
                holder.imageViewArrow.setImageResource(R.drawable.down_arrow);
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.spr_top_folder));
            }else {
                holder.imageViewArrow.setImageResource(R.drawable.right_arrow);
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.spr_folder));
            }
            if(!sprElement.isRemove()){
                holder.imageView.setImageResource(R.drawable.folder);
            }else{
                holder.imageView.setImageResource(R.drawable.folder_remove);
            }
        }

        return convertView;
    }


    static class ViewHolder {
        public TextView txtName;
        public TextView txtCode;
        public ImageView imageView;
        public ImageView imageViewArrow;


    }
}
