package com.lavan.pdfviewer;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.MyViewHolder> {

    private List<PdfFile> pdfFileList;
    private Context context;


    public PdfAdapter(List<PdfFile> pdfFileList, Context context) {
        this.pdfFileList = pdfFileList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_pdf, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PdfFile loanRequestDetails = pdfFileList.get(position);
        holder.tvName.setText(loanRequestDetails.getName());
        holder.tv_pdf_file_size.setText(loanRequestDetails.getSize());
    }

    @Override
    public int getItemCount() {
        return pdfFileList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tv_pdf_file_size;
        public ImageView ivFile;
        public RelativeLayout llout_item;

        public MyViewHolder(View view) {
            super(view);
            llout_item = (RelativeLayout) view.findViewById(R.id.llout_pdf_item);
            tvName = (TextView) view.findViewById(R.id.tv_pdf_name);
            ivFile = (ImageView) view.findViewById(R.id.iv_pdf_icon);
            tv_pdf_file_size = (TextView) view.findViewById(R.id.tv_pdf_file_size);
        }
    }
}