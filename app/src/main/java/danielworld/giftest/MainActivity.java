package danielworld.giftest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlayGifView pGif = (PlayGifView) findViewById(R.id.viewGif);
//        pGif.setImageResource(R.drawable.giphy);
        pGif.setImageResource(R.drawable.catparty);
    }
}
