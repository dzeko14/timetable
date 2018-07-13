package my.dzeko.timetable;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeBottomNavigationView();
    }

    private void initializeBottomNavigationView() {
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.schedule_bottom_navigation_main:
                        item.setChecked(true);
                        return true;
                    case R.id.calendar_bottom_navigation_main:
                        item.setChecked(true);
                        return true;
                    case R.id.settings_bottom_navigation_main:
                        item.setChecked(true);
                        return true;
                }
                return false;
            }
        });
    }
}
