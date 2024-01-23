package dev.karl.wordwander;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Queue;

import dev.karl.wordwander.databinding.ActivityMainWordGameBinding;

public class MainWordGame extends AppCompatActivity {
    String userWordGuess, masterWord;
    TextView tvQ,tvW,tvE,tvR,tvT,tvY,tvU,tvI,tvO,tvP,tvA,tvS,tvD,tvF,tvG,tvH,tvJ,tvK,tvL,tvZ,tvX,tvC,tvV,tvB,tvN,tvM, tvEnter, tvClear, tvHintText;
    Integer LIGHT_BLUE = R.color.light_blue;
    Integer ORANGE = R.color.orange;
    Integer GRAY = R.color.gray;
    Integer WHITE = R.color.white;
    Integer BLACK = R.color.black;
    boolean b, isGameOver, isWon, isSoundEnabled, isMusicEnabled;
    ActivityMainWordGameBinding binding;
    GridAdapter gridAdapter;
    ArrayList<LetterTileModel> letterList = new ArrayList<>();
    int gridViewCursor, currentTurnCursor, triesCounter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainWordGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(ContextCompat.getColor(MainWordGame.this, R.color.dark_blue));
        getWindow().setNavigationBarColor(ContextCompat.getColor(MainWordGame.this, R.color.dark_blue));

        WordsDatasetHelper.initialize(this);

        tvHintText = findViewById(R.id.tvHintText);

        initializeTextviews();
        setupEmptyGridView();
        setTextviewClickListeners();

        //
        isMusicEnabled = true;
        isSoundEnabled = true;

        tvEnter = findViewById(R.id.tvEnter);
        tvClear = findViewById(R.id.tvClear);

        tvEnter.setOnClickListener(view -> {
            //
//            masterWord = WordsDatasetHelper.getNewRandomWord();

//            boolean b = WordsDatasetHelper.checkIfWordExists(userWordGuess);;
//            Toast.makeText(this, masterWord+":"+userWordGuess+":"+b, Toast.LENGTH_SHORT).show();
            if(isGameOver){
                //
            }else if (userWordGuess.length() < 5){
                tvHintText.setText("Must have 5 letters");
            }else if (!WordsDatasetHelper.checkIfWordExists(userWordGuess)){
                tvHintText.setText("\"" + userWordGuess + "\" is not in the word list");
            }else{
                checkMatch(userWordGuess, masterWord);
                currentTurnCursor = 0;
                userWordGuess = "";
                triesCounter++;
                if (triesCounter == 6 || isWon){
                    //show dialog
                    endGameDialog(isWon);
                    isGameOver = true;
                    tvHintText.setText("Correct answer is: " + masterWord);
                }
            }
        });
        tvClear.setOnClickListener(view -> {
            if(gridViewCursor > 0 && currentTurnCursor > 0){
                gridAdapter.setLetter(gridViewCursor-1, "");
                gridViewCursor--;
                userWordGuess = userWordGuess.substring(0,currentTurnCursor-1);
                currentTurnCursor--;
                tvHintText.setText("");
            }
        });
    }

    void setupEmptyGridView(){
        for (int q1 = 0 ; q1 < "QWERTYUIOPASDFGHJKLZXCVBNM".length() ; q1++){
            setLetterKeyColor("QWERTYUIOPASDFGHJKLZXCVBNM".charAt(q1), R.drawable.ripple_blue);
        }
        masterWord = WordsDatasetHelper.getNewRandomWord().toUpperCase();
        Log.d("masterword:", masterWord);
        gridViewCursor = 0;
        currentTurnCursor = 0;
        userWordGuess = "";
        isGameOver = false;
        isWon = false;
        triesCounter = 0;
        tvHintText.setText("");

        letterList.clear();

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
//            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            //show options
            gameOptions();
        });
    }
    void initializeTextviews(){
        tvQ = findViewById(R.id.tvLetterQ);
        tvW = findViewById(R.id.tvLetterW);
        tvE = findViewById(R.id.tvLetterE);
        tvR = findViewById(R.id.tvLetterR);
        tvT = findViewById(R.id.tvLetterT);
        tvY = findViewById(R.id.tvLetterY);
        tvU = findViewById(R.id.tvLetterU);
        tvI = findViewById(R.id.tvLetterI);
        tvO = findViewById(R.id.tvLetterO);
        tvP = findViewById(R.id.tvLetterP);
        tvA = findViewById(R.id.tvLetterA);
        tvS = findViewById(R.id.tvLetterS);
        tvD = findViewById(R.id.tvLetterD);
        tvF = findViewById(R.id.tvLetterF);
        tvG = findViewById(R.id.tvLetterG);
        tvH = findViewById(R.id.tvLetterH);
        tvJ = findViewById(R.id.tvLetterJ);
        tvK = findViewById(R.id.tvLetterK);
        tvL = findViewById(R.id.tvLetterL);
        tvZ = findViewById(R.id.tvLetterZ);
        tvX = findViewById(R.id.tvLetterX);
        tvC = findViewById(R.id.tvLetterC);
        tvV = findViewById(R.id.tvLetterV);
        tvB = findViewById(R.id.tvLetterB);
        tvN = findViewById(R.id.tvLetterN);
        tvM = findViewById(R.id.tvLetterM);
    }
    void setTextviewClickListeners(){
        tvQ.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "Q");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "Q";
                tvHintText.setText("");
            }
        });
        tvW.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "W");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "W";
                tvHintText.setText("");
            }
        });
        tvE.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "E");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "E";
                tvHintText.setText("");
            }
        });
        tvR.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "R");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "R";
                tvHintText.setText("");
            }
        });
        tvT.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "T");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "T";
                tvHintText.setText("");
            }
        });
        tvY.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "Y");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "Y";
                tvHintText.setText("");
            }
        });
        tvU.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "U");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "U";
                tvHintText.setText("");
            }
        });
        tvI.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "I");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "I";
                tvHintText.setText("");
            }
        });
        tvO.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "O");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "O";
                tvHintText.setText("");
            }
        });
        tvP.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "P");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "P";
                tvHintText.setText("");
            }
        });
        tvA.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "A");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "A";
                tvHintText.setText("");
            }
        });
        tvS.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "S");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "S";
                tvHintText.setText("");
            }
        });
        tvD.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "D");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "D";
                tvHintText.setText("");
            }
        });
        tvF.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "F");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "F";
                tvHintText.setText("");
            }
        });
        tvG.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "G");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "G";
                tvHintText.setText("");
            }
        });
        tvH.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "H");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "H";
                tvHintText.setText("");
            }
        });
        tvJ.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "J");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "J";
                tvHintText.setText("");
            }
        });
        tvK.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "K");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "K";
                tvHintText.setText("");
            }
        });
        tvL.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "L");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "L";
                tvHintText.setText("");
            }
        });
        tvZ.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "Z");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "Z";
                tvHintText.setText("");
            }
        });
        tvX.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "X");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "X";
                tvHintText.setText("");
            }
        });
        tvC.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "C");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "C";
                tvHintText.setText("");
            }
        });
        tvV.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "V");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "V";
                tvHintText.setText("");
            }
        });
        tvB.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "B");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "B";
                tvHintText.setText("");
            }
        });
        tvN.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "N");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "N";
                tvHintText.setText("");
            }
        });
        tvM.setOnClickListener(view -> {
            if(currentTurnCursor < 5 && !isGameOver){
                gridAdapter.setLetter(gridViewCursor, "M");
                gridViewCursor++;
                currentTurnCursor++;
                userWordGuess = userWordGuess + "M";
                tvHintText.setText("");
            }
        });
    }
    void checkMatch(String guess, String master){
        StringBuilder guessStrBldr = new StringBuilder(guess);
        StringBuilder masterStrBldr = new StringBuilder(master);
        if (guess.equals(master)){
            //win
            isGameOver = true;
            isWon = true;
        }
        //check oranges first
        for (int q1 = 0 ; q1 < 5 ; q1++){
            if(guessStrBldr.charAt(q1) == masterStrBldr.charAt(q1)){
                setLetterKeyColor(guessStrBldr.charAt(q1), R.drawable.ripple_orange);
                //
                guessStrBldr.setCharAt(q1, '.');
                masterStrBldr.setCharAt(q1, ',');
                gridAdapter.setTextColorResource(((gridViewCursor-5) + q1), WHITE);
                gridAdapter.setBgColorResource(((gridViewCursor-5) + q1), ORANGE);
                //also update letter keys, idk how yet
            }
        }
        //check lightblues
        for (int q1 = 0 ; q1 < 5 ; q1++){
            //
            for (int q2 = 0 ; q2 < 5 ; q2++){
                //
//                b = guessStrBldr.charAt(q1) == masterStrBldr.charAt(q2);
                if (guessStrBldr.charAt(q1) == masterStrBldr.charAt(q2)){
                    setLetterKeyColor(guessStrBldr.charAt(q1), R.drawable.ripple_light_blue);
                    //
                    guessStrBldr.setCharAt(q1, '.');
                    masterStrBldr.setCharAt(q2, ',');
                    gridAdapter.setTextColorResource(((gridViewCursor-5) + q1), WHITE);
                    gridAdapter.setBgColorResource(((gridViewCursor-5) + q1), LIGHT_BLUE);
                    //also update letter keys, idk how yet
                    break;
                }
            }
        }
        //make the rest grays
        for (int q1 = 0 ; q1 < 5 ; q1++){
            //
            if (guessStrBldr.charAt(q1) != '.'){
                setLetterKeyColor(guessStrBldr.charAt(q1), R.drawable.ripple_gray);
                gridAdapter.setTextColorResource(((gridViewCursor-5) + q1), WHITE);
                gridAdapter.setBgColorResource(((gridViewCursor-5) + q1), GRAY);
            }
        }
    }
    void setLetterKeyColor(Character tv, @DrawableRes Integer ripple){
        switch (tv){
            case 'Q':
                //
                tvQ.setBackgroundResource(ripple);
                break;
            case 'W':
                //
                tvW.setBackgroundResource(ripple);
                break;
            case 'E':
                //
                tvE.setBackgroundResource(ripple);
                break;
            case 'R':
                //
                tvR.setBackgroundResource(ripple);
                break;
            case 'T':
                //
                tvT.setBackgroundResource(ripple);
                break;
            case 'Y':
                //
                tvY.setBackgroundResource(ripple);
                break;
            case 'U':
                //
                tvU.setBackgroundResource(ripple);
                break;
            case 'I':
                //
                tvI.setBackgroundResource(ripple);
                break;
            case 'O':
                //
                tvO.setBackgroundResource(ripple);
                break;
            case 'P':
                //
                tvP.setBackgroundResource(ripple);
                break;
            case 'A':
                //
                tvA.setBackgroundResource(ripple);
                break;
            case 'S':
                //
                tvS.setBackgroundResource(ripple);
                break;
            case 'D':
                //
                tvD.setBackgroundResource(ripple);
                break;
            case 'F':
                //
                tvF.setBackgroundResource(ripple);
                break;
            case 'G':
                //
                tvG.setBackgroundResource(ripple);
                break;
            case 'H':
                //
                tvH.setBackgroundResource(ripple);
                break;
            case 'J':
                //
                tvJ.setBackgroundResource(ripple);
                break;
            case 'K':
                //
                tvK.setBackgroundResource(ripple);
                break;
            case 'L':
                //
                tvL.setBackgroundResource(ripple);
                break;
            case 'Z':
                //
                tvZ.setBackgroundResource(ripple);
                break;
            case 'X':
                //
                tvX.setBackgroundResource(ripple);
                break;
            case 'C':
                //
                tvC.setBackgroundResource(ripple);
                break;
            case 'V':
                //
                tvV.setBackgroundResource(ripple);
                break;
            case 'B':
                //
                tvB.setBackgroundResource(ripple);
                break;
            case 'N':
                //
                tvN.setBackgroundResource(ripple);
                break;
            case 'M':
                //
                tvM.setBackgroundResource(ripple);
                break;

        }
    }
    void endGameDialog(boolean hasWon){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.gameover_dialog, null);

        TextView header = v.findViewById(R.id.dialogTvHeader);
        TextView description = v.findViewById(R.id.gameOverDialogTv);
        TextView correctWord = v.findViewById(R.id.tvGameOverDialogCorrectAnswer);
        TextView button = v.findViewById(R.id.tvTryAgainBtn);

        correctWord.setText(masterWord);
        if (hasWon){
            header.setText("You got it!");
            description.setText("You guessed correctly:");
        }else{
            header.setText("Game Over.");
            description.setText("The correct word was:");
        }

        builder.setView(v);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        button.setOnClickListener(view -> {
            //
            setupEmptyGridView();
            alertDialog.dismiss();
        });
    }
    void gameOptions(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.alert_dialog, null);

        CheckBox music = v.findViewById(R.id.musicCb);
        CheckBox sound = v.findViewById(R.id.soundCb);
        TextView button = v.findViewById(R.id.tvNewWord);
        ImageView close = v.findViewById(R.id.closeDia);

        music.setChecked(isMusicEnabled);
        sound.setChecked(isSoundEnabled);

        builder.setView(v);
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        button.setOnClickListener(view -> {
            //
            setupEmptyGridView();
            alertDialog.dismiss();
        });

        music.setOnCheckedChangeListener((compoundButton, b) -> {
            //
        });
        sound.setOnCheckedChangeListener((compoundButton, b1) -> {
            //
        });
        close.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
    }
}