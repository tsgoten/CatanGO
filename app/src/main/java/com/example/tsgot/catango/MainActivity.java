package com.example.tsgot.catango;

import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView resource, dice, city, settlement;
    TextView clay, wood, wool, grain, ore;
    int currentImage;
    int numberOfSettlements = 1;

     int []  score = new int [5];
    ArrayList<TextView> displayScores = new ArrayList<>();

    private ConstraintLayout constraintLayout;

    boolean firstrun = true;

    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    RadioButton radioButton5;

    ArrayList<Resource> resources = new ArrayList<>();

    Resource resourceCurrent;
    int currentResourcePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        new ThreadAdder().start();
        constraintLayout = findViewById(R.id.myConstraintLayout);
        resource = findViewById(R.id.imageViewResource);
        dice = findViewById(R.id.imageViewDice);
        city = findViewById(R.id.imageViewCity);
        settlement = findViewById(R.id.imageViewSettlement);

        clay = findViewById(R.id.textView1);
        wood = findViewById(R.id.textView2);
        wool = findViewById(R.id.textView3);
        grain = findViewById(R.id.textView4);
        ore = findViewById(R.id.textView5);
        displayScores = new ArrayList<>();
        displayScores.add(clay);
        displayScores.add(wood);
        displayScores.add(wool);
        displayScores.add(grain);
        displayScores.add(ore);

        dice.setVisibility(View.GONE);
        settlement.setVisibility(View.GONE);
        city.setVisibility(View.GONE);

        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        radioButton5 = findViewById(R.id.radioButton5);

        final RadioButton [] radioButtons  =  {radioButton1,radioButton2,radioButton3,radioButton4,radioButton5};

        addClickOne(resource);

        resources.add(new Resource("Clay", R.drawable.brick));
        resources.add(new Resource("Wood", R.drawable.wood));
        resources.add(new Resource("Wool", R.drawable.wool));
        resources.add(new Resource("Grain", R.drawable.grain));
        resources.add(new Resource("Ore", R.drawable.ore));
        //currentResourcePosition = 5;

        diceRoll(radioButtons);

        dice.setClickable(true);
        dice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(score[currentResourcePosition]>=20) {
                    checkDiceVisibility();
                    fadeOut(dice);
                    diceRoll(radioButtons);
                }
            }
        });

        settlement.setClickable(true);
        city.setClickable(true);
        settlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((score[0]>=1 && score[1]>=1 && score[2]>=1 && score[3]>=1)) {
                    numberOfSettlements++;
                    add(-1, -1, -1, -1, 0);
                    cityAnimationUpgrade(R.drawable.house, false,settlement);
                    fadeOut(settlement);
                }
            }
        });
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(score[3]>=2 && score[4]>=3) {
                    numberOfSettlements += 2;
                    add(0, 0, 0, -2, -3);
                    cityAnimationUpgrade(R.drawable.city, true,city);
                    fadeOut(city);
                }

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton1:
                        resourceCurrent = resources.get(0);
                        currentResourcePosition = 0;
                        break;
                    case R.id.radioButton2:
                        resourceCurrent = resources.get(1);
                        currentResourcePosition = 1;
                        break;
                    case R.id.radioButton3:
                        resourceCurrent = resources.get(2);
                        currentResourcePosition = 2;
                        break;
                    case R.id.radioButton4:
                        resourceCurrent = resources.get(3);
                        currentResourcePosition = 3;
                        break;
                    case R.id.radioButton5:
                        resourceCurrent = resources.get(4);
                        currentResourcePosition = 4;
                        break;
                }
                resource.setImageResource(resourceCurrent.getImageID());
                currentImage = resourceCurrent.getImageID();
                checkDiceVisibility();
                checkSettlementVisibility();
                checkCityVisibility();
            }
        });
    }
    ///Functions of the game
    public void diceRoll(RadioButton[] radioButtons){
            if(!firstrun)
                add(-20);
            firstrun = false;

            int a = (int)(Math.random()*5);
            radioButtons[a].setVisibility(View.VISIBLE);
            radioButtons[a].setChecked(true);
            resource.setImageResource(resources.get(a).getImageID());
            currentImage = resources.get(a).getImageID();
            currentResourcePosition = a;
            resourceCurrent = resources.get(a);
            Toast.makeText(this, "You Rolled  " + resourceCurrent.getName(), Toast.LENGTH_LONG).show();
        Log.d("numberJKKJJKJK", a+"");
    }
    public void floatONes(){
        ImageView floaty = new ImageView(this);
        floaty.setId(View.generateViewId());
        floaty.setImageResource(currentImage);
        floaty.setScaleX(.1f);
        floaty.setScaleY(.1f);
        randomlyAppear(floaty);
        translateAndFade(floaty);
    }
    public void randomlyAppear(View v){
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT );
        v.setLayoutParams(params);
        constraintLayout.addView(v);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(v.getId(), ConstraintSet.TOP, R.id.imageViewResource, ConstraintSet.TOP);
        //constraintSet.connect(v.getId(), ConstraintSet.BOTTOM, R.id.imageViewResource, ConstraintSet.TOP);
        constraintSet.connect(v.getId(), ConstraintSet.LEFT, R.id.imageViewResource, ConstraintSet.LEFT);
        constraintSet.connect(v.getId(), ConstraintSet.RIGHT, R.id.myConstraintLayout, ConstraintSet.RIGHT);

        //constraintSet.setVerticalBias(v.getId(), (float)Math.random());
        constraintSet.setHorizontalBias(v.getId(), (float)Math.random());

        constraintSet.applyTo(constraintLayout);
    }
///Visibility
    public void checkDiceVisibility(){
        if(score[currentResourcePosition]>=20){
            if(dice.getVisibility()!=View.VISIBLE)
                fadeIn(dice);
            else
                dice.setVisibility(View.VISIBLE);
        }
        else{
            dice.setVisibility(View.GONE);
        }
    }
    public void checkSettlementVisibility(){
        if((score[0]>=1 && score[1]>=1 && score[2]>=1 && score[3]>=1)){
            if(settlement.getVisibility()!=View.VISIBLE)
                fadeIn(settlement);
            else
                settlement.setVisibility(View.VISIBLE);
        }
        else{
            settlement.setVisibility(View.GONE);
        }
    }
    public void checkCityVisibility(){
        if(score[3]>=2 && score[4]>=3){
            if(city.getVisibility()!=View.VISIBLE)
                fadeIn(city);
            else
                city.setVisibility(View.VISIBLE);
        }
        else{
            city.setVisibility(View.GONE);
        }
    }
 //Animations
    public void fadeIn(View v){
        v.setVisibility(View.VISIBLE);
        final Animation fadeInto = new AlphaAnimation(0,1);
        fadeInto.setDuration(300);
        v.startAnimation(fadeInto);
    }
    public void fadeOut(View v){
        //v.setVisibility(View.VISIBLE);
        final Animation fadeInto = new AlphaAnimation(1,0);
        fadeInto.setDuration(300);
        v.startAnimation(fadeInto);
    }
    public void translateAndFade(final View v){
        //final View myView = v;
        final TranslateAnimation translateAnimation = new TranslateAnimation(v.getX(), v.getY(), v.getX(), v.getY()-75);
        translateAnimation.setDuration(250);
        final Animation fadeAway = new AlphaAnimation(1,0);
        fadeAway.setDuration(200);
        AnimationSet transAndFade = new AnimationSet(false );
        fadeAway.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                constraintLayout.removeView(v);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        transAndFade.addAnimation(translateAnimation);
        transAndFade.addAnimation(fadeAway);
        v.startAnimation(transAndFade);

    }
    public void addClickOne(View v){
        final ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.2f,1.0f, 1.2f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100);
        final ScaleAnimation scaleAnimation2 = new ScaleAnimation(1.2f, 1.0f,1.2f, 1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation2.setDuration(50);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(scaleAnimation);
                view.startAnimation(scaleAnimation2);
                floatONes();
                add();
            }
        });
    }

    public void cityAnimationUpgrade(int imageId, boolean isCity, ImageView view){
        float a;
        if(isCity)
            a = 0f;
        else
            a=0.8f;
        ImageView clonedCity = new ImageView(this);
        clonedCity.setId(View.generateViewId());
        clonedCity.setImageResource(imageId);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(100, 100);
        clonedCity.setLayoutParams(params);
        constraintLayout.addView(clonedCity);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(clonedCity.getId(), ConstraintSet.TOP, R.id.imageViewResource, ConstraintSet.BOTTOM);
        constraintSet.connect(clonedCity.getId(), ConstraintSet.BOTTOM, R.id.radioGroup, ConstraintSet.TOP);
        constraintSet.connect(clonedCity.getId(), ConstraintSet.LEFT, R.id.myConstraintLayout, ConstraintSet.LEFT);
        constraintSet.connect(clonedCity.getId(), ConstraintSet.RIGHT, R.id.myConstraintLayout, ConstraintSet.RIGHT);

        constraintSet.setVerticalBias(clonedCity.getId(), (float)Math.random()/5 + a );
        constraintSet.setHorizontalBias(clonedCity.getId(), (float)Math.random());

        constraintSet.applyTo(constraintLayout);

        final AnimationSet shrinkAndSlide = new AnimationSet(false);
        final ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.2f, 1f,1.2f);
        scaleAnimation.setDuration(200);
        //shrinkAndSlide.addAnimation(translateAnimation);
        shrinkAndSlide.addAnimation(scaleAnimation);
        clonedCity.startAnimation(shrinkAndSlide);
    }
///Changing the score
    public synchronized void add(){
        score[currentResourcePosition]++;
        Log.d("Ran", score[currentResourcePosition]+"");
        displayScores.get(currentResourcePosition).setText(score[currentResourcePosition]+"");
        checkDiceVisibility();
        checkSettlementVisibility();
        checkCityVisibility();
    }
    public synchronized  void add(int a){
        score[currentResourcePosition]+=a;
        Log.d("Ran", score[currentResourcePosition]+"");
        displayScores.get(currentResourcePosition).setText(score[currentResourcePosition]+"");
        checkDiceVisibility();
        checkSettlementVisibility();
        checkCityVisibility();
    }
    public synchronized void add( int a, int b, int c, int d, int e){
        score [0] += a;
        score [1] += b;
        score[2]+= c;
        score[3]+= d;
        score[4]+=e;
        for(int i=0;i<displayScores.size();i++){
            displayScores.get(i).setText(score[i]+"");
        }
        checkDiceVisibility();
        checkSettlementVisibility();
        checkCityVisibility();
    }
    public class ThreadAdder extends Thread{
        public void run() {
            while (numberOfSettlements>=1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(numberOfSettlements>1)
                            add();
                        Log.d("shits working", score[currentResourcePosition]+"");
                    }
                });
                try {
                    sleep(5000 / numberOfSettlements);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}
