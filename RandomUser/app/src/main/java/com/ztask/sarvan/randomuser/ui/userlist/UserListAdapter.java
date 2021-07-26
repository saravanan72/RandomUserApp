package com.ztask.sarvan.randomuser.ui.userlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ztask.sarvan.randomuser.R;
import com.ztask.sarvan.randomuser.data.localdb.entity.RandomUserEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private OnItemClickListener listener;
    private List<RandomUserEntities> results;
    private UserListFragment fragment;
    private List<Integer> dynamicSizes = new ArrayList();
    public UserListAdapter(UserListFragment fragment, List<RandomUserEntities> results) {
        this.fragment = fragment;
        this.listener = fragment;
        this.results = results;
        dynamicSizes.add(320);
        dynamicSizes.add(440);
        dynamicSizes.add(360);
        dynamicSizes.add(400);
        dynamicSizes.add(430);
        dynamicSizes.add(350);
        dynamicSizes.add(370);
    }

    public void setFilter(List<RandomUserEntities> temp) {
        results = temp;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv, tvUserAge;
        ImageView iv;

        ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.itemusenametv);
            tvUserAge = itemView.findViewById(R.id.tvUserAge);
            iv = itemView.findViewById(R.id.itemuserlogoiv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(results.get(getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = results.get(position).getTitle() +". "+
                results.get(position).getFirst() +" "+
                results.get(position).getLast();
        String imgurl = results.get(position).getUserimg();

        holder.tvUserAge.setText("Age: "+ results.get(position).getAge());

        int random = ThreadLocalRandom.current().nextInt(0, 6);
        int height = dynamicSizes.get(random);
        holder.iv.getLayoutParams().height = height;
        holder.tv.setText(name);
        Glide.with(fragment)
                .load(imgurl)
                .placeholder(R.drawable.ic_randomuser_img) //placeholder
                .error(R.drawable.ic_randomuser_img) //error
                .into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
    interface OnItemClickListener{
        void OnItemClick(RandomUserEntities item);
    }
}