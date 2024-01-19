package dev.karl.wordwander;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import dev.karl.wordwander.databinding.ActivityMainWordGameBinding;

public class MainWordGame extends AppCompatActivity {

    Integer LIGHT_BLUE = R.color.light_blue;
    Integer ORANGE = R.color.orange;
    Integer GRAY = R.color.gray;
    Integer WHITE = R.color.white;
    Integer BLACK = R.color.black;

    ActivityMainWordGameBinding binding;
    GridAdapter gridAdapter;
    ArrayList<LetterTileModel> letterList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainWordGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(ContextCompat.getColor(MainWordGame.this, R.color.dark_blue));
        getWindow().setNavigationBarColor(ContextCompat.getColor(MainWordGame.this, R.color.dark_blue));

        setupEmptyGridView();
    }

    void setupEmptyGridView(){
        letterList.add(new LetterTileModel("Q", WHITE, GRAY));
        letterList.add(new LetterTileModel("W", WHITE, LIGHT_BLUE));
        letterList.add(new LetterTileModel("E", WHITE, GRAY));
        letterList.add(new LetterTileModel("R", WHITE, LIGHT_BLUE));
        letterList.add(new LetterTileModel("T", WHITE, ORANGE));

        letterList.add(new LetterTileModel("Y", WHITE, GRAY));
        letterList.add(new LetterTileModel("U", WHITE, ORANGE));
        letterList.add(new LetterTileModel("I", WHITE, LIGHT_BLUE));
        letterList.add(new LetterTileModel("O", WHITE, GRAY));
        letterList.add(new LetterTileModel("P", WHITE, ORANGE));

        letterList.add(new LetterTileModel("A", BLACK, WHITE));
        letterList.add(new LetterTileModel("S", BLACK, WHITE));
        letterList.add(new LetterTileModel("D", BLACK, WHITE));
        letterList.add(new LetterTileModel("F", BLACK, WHITE));
        letterList.add(new LetterTileModel("G", BLACK, WHITE));

        letterList.add(new LetterTileModel("", BLACK, WHITE));
        letterList.add(new LetterTileModel("", BLACK, WHITE));
        letterList.add(new LetterTileModel("", BLACK, WHITE));
        letterList.add(new LetterTileModel("", BLACK, WHITE));
        letterList.add(new LetterTileModel("", BLACK, WHITE));

        letterList.add(new LetterTileModel("", BLACK, WHITE));
        letterList.add(new LetterTileModel("", BLACK, WHITE));
        letterList.add(new LetterTileModel("", BLACK, WHITE));
        letterList.add(new LetterTileModel("", BLACK, WHITE));
        letterList.add(new LetterTileModel("", BLACK, WHITE));

        letterList.add(new LetterTileModel("", BLACK, WHITE));
        letterList.add(new LetterTileModel("", BLACK, WHITE));
        letterList.add(new LetterTileModel("", BLACK, WHITE));
        letterList.add(new LetterTileModel("", BLACK, WHITE));
        letterList.add(new LetterTileModel("", BLACK, WHITE));

        gridAdapter = new GridAdapter(this, letterList);
        binding.gameGridView.setAdapter(gridAdapter);
        binding.gameGridView.setOnTouchListener((view, motionEvent) -> {
            return motionEvent.getAction() == MotionEvent.ACTION_MOVE;
        });

        binding.gameGridView.setOnItemClickListener((adapterView, view, i, l) -> {
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            //show options
        });
    }
}