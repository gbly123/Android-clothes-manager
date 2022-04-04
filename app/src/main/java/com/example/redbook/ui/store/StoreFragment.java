package com.example.redbook.ui.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redbook.databinding.FragmentStoreBinding;
import com.example.redbook.db.RedBookDataBase;
import com.example.redbook.db.entity.Diary;
import com.example.redbook.ui.detail.DetailActivity;
import com.example.redbook.utils.GridItemDecoration;

import java.util.List;

/**
 * 穿搭库
 */
public class StoreFragment extends Fragment implements StoreAdapter.OnItemClickListener {

    private StoreViewModel storeViewModel;
    private FragmentStoreBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        storeViewModel =
                new ViewModelProvider(this).get(StoreViewModel.class);

        binding = FragmentStoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.list;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridItemDecoration(10));
        int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        StoreAdapter adapter = new StoreAdapter(getContext(), screenWidth);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        RedBookDataBase.getRedBookDataBaseInstance(getContext()).getDiaryDao().getAllDiary().observe(getViewLifecycleOwner(), new Observer<List<Diary>>() {
            @Override
            public void onChanged(List<Diary> list) {
                adapter.setData(list);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void itemClick(Diary diary) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DetailActivity.KEY_DIARY, diary);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}