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
import ru.smolyakovroma.reference5.model.DocElement;

public class DocAdapter extends ArrayAdapter<DocElement> {

    private List<DocElement> list;

    public DocAdapter(Context context, List<DocElement> list) {
        super(context, R.layout.doc_item, R.id.txt_element_name, list);
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.doc_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_element_name);
            viewHolder.txtCode = (TextView) convertView.findViewById(R.id.txt_element_code);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_source);
            convertView.setTag(viewHolder);

        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        DocElement docElement = list.get(position);


        holder.txtName.setText(Long.toString(docElement.getDoc_datetime()));
        holder.txtCode.setText(Integer.toString(docElement.getStatus()));

        if (docElement.getStatus() == 0) {
            holder.imageView.setImageResource(R.drawable.document_save);
        } else if (docElement.getStatus() == 1) {
            holder.imageView.setImageResource(R.drawable.document_conduct);
        } else {
            holder.imageView.setImageResource(R.drawable.document_remove);
        }


        return convertView;
    }

    static class ViewHolder {
        public TextView txtName;
        public TextView txtCode;
        public ImageView imageView;


    }
}