package com.omidavz.irantourismapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.omidavz.irantourismapp.databinding.ContentRowBinding

class ContentAdapter(val context: Context , var contentList : List<Content> ) :
    RecyclerView.Adapter<ContentAdapter.ContentViewHolder>(){


    lateinit var binding: ContentRowBinding;

    class  ContentViewHolder(var binding : ContentRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(content: Content) {
            binding.content = content
        }




        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        binding = ContentRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ContentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        var content : Content = contentList[position]

        Log.i("MYTAG", "onBindViewHolder: "+content.imageUrl)

        Glide.with(context)
            .load(content.imageUrl)
            .into(binding.contentImageList)
        holder.bind(content)


    }


}
