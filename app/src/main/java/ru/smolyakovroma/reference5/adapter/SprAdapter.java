package ru.smolyakovroma.reference5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.smolyakovroma.reference5.R;
import ru.smolyakovroma.reference5.model.SprElement;

public class SprAdapter extends ArrayAdapter<SprElement> {

    private List<SprElement> list;
    private boolean isPicker = false;


    public SprAdapter(Context context, List<SprElement> list, boolean isPicker) {
        super(context, R.layout.spr_item, R.id.txt_element_name, list);
        this.list = list;
        this.isPicker = isPicker;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spr_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_element_name);
            viewHolder.txtCode = (TextView) convertView.findViewById(R.id.txt_element_code);
            viewHolder.txtAmount = (TextView) convertView.findViewById(R.id.txt_amount);
            viewHolder.txtUnit = (TextView) convertView.findViewById(R.id.txt_unit);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_source);
            viewHolder.imageViewArrow = (ImageView) convertView.findViewById(R.id.img_source_arrow);
            convertView.setTag(viewHolder);

        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        SprElement sprElement = list.get(position);

        if(isPicker){
//            if(list.contains(sprElement)){
                holder.txtUnit.setText(sprElement.getUnit());
                holder.txtAmount.setText(Integer.toString(sprElement.getAmount()));

//         }
        }
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
        public TextView txtAmount;
        public TextView txtUnit;
        public ImageView imageView;
        public ImageView imageViewArrow;


    }
}
