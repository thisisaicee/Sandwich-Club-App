package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        try{
            String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
            String json = sandwiches[position];

            Sandwich sandwich = JsonUtils.parseSandwichJson(json);
            if (sandwich == null) {
                // Sandwich data unavailable
                closeOnError();
                return;
            }

            loadDetails(sandwich);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Display all Sandwich details
     * @param sandwich
     */
    private void loadDetails(Sandwich sandwich) {
        ImageView sandwichImage = findViewById(R.id.image);
        TextView alsoKnownAs = findViewById(R.id.also_known_as_details);
        TextView ingredients = findViewById(R.id.ingredients_details);
        TextView origin = findViewById(R.id.origin_details);
        TextView description = findViewById(R.id.description_details);

        Picasso.with(this)
                .load(sandwich.getImage())
                .into(sandwichImage);

        setTitle(sandwich.getMainName());
        List<String> sandwichNameList = sandwich.getAlsoKnownAs();
        for(int i=0 ; i< sandwichNameList.size() ; i++){

            if(i + 1 < sandwichNameList.size()){
                alsoKnownAs.append(" "+ sandwichNameList.get(i) + ",");
            }else if (!(i + 1 < sandwichNameList.size())){
                alsoKnownAs.append(" " + sandwichNameList.get(i) + ".");
            }
        }

        List<String> sandwichIngredientsList = sandwich.getIngredients();
        for(int i=0 ; i< sandwichIngredientsList.size() ; i++){
            if(i + 1 < sandwichIngredientsList.size()){
            ingredients.append(sandwichIngredientsList.get(i) + ", ");
            }
            else if (!(i + 1 < sandwichIngredientsList.size())){
                ingredients.append(sandwichIngredientsList.get(i) + ". ");
            }
        }

        origin.append(sandwich.getPlaceOfOrigin());
        description.append(sandwich.getDescription());
    }
}
