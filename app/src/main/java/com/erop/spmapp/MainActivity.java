package com.erop.spmapp;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;

import com.erop.spmapp.SP.cards;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

//    public void deleteAllFilesAndLog() {
//        // Получаем каталог с файлами приложения
//        File directory = getFilesDir();
//
//        // Получаем все файлы в этом каталоге
//        File[] files = directory.listFiles();
//
//        if (files != null) {
//            for (File file : files) {
//                // Выводим информацию о каждом файле в лог
//                Log.d("FileDelete", "Обнаружен файл: " + file.getName() + ", размер: " + file.length() + " байт");
//
//                // Пытаемся удалить файл
//                boolean deleted = file.delete();
//                if (deleted) {
//                    Log.d("FileDelete", "Файл успешно удален: " + file.getName());
//                } else {
//                    Log.d("FileDelete", "Не удалось удалить файл: " + file.getName());
//                }
//            }
//        } else {
//            Log.d("FileDelete", "Нет файлов для удаления в директории: " + directory.getAbsolutePath());
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        deleteAllFilesAndLog();

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, com.erop.spmapp.menu.newInstance(), "menu")
                    .commit();
        }


        bottomNavigationView = findViewById(R.id.bottom_navigation);



        ColorStateList colorStateListCards = ContextCompat.getColorStateList(this, R.color.color_selector_cards);
        ColorStateList colorStateListNews = ContextCompat.getColorStateList(this, R.color.color_selector_news);
        ColorStateList colorStateListPlayers = ContextCompat.getColorStateList(this, R.color.color_selector_players);
        ColorStateList colorStateListMenu = ContextCompat.getColorStateList(this, R.color.color_selector_menu);

        bottomNavigationView.setItemIconTintList(null); // Отключите глобальное управление цветом

        bottomNavigationView.getMenu().findItem(R.id.navigation_cards).getIcon().setTintList(colorStateListCards);
        bottomNavigationView.getMenu().findItem(R.id.navigation_news).getIcon().setTintList(colorStateListNews);
        bottomNavigationView.getMenu().findItem(R.id.navigation_players).getIcon().setTintList(colorStateListPlayers);
        bottomNavigationView.getMenu().findItem(R.id.navigation_menu).getIcon().setTintList(colorStateListMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_news) {
                fragment = new com.erop.spmapp.SP.cards();
            } else if (itemId == R.id.navigation_cards) {
                fragment = new com.erop.spmapp.SPM.cards();
            } else if (itemId == R.id.navigation_players) {
                fragment = new com.erop.spmapp.SPB.cards();
            } else if (itemId == R.id.navigation_menu) {
                fragment = new menu();
            } else {
                return false;
            }
            loadFragment(fragment);
            return true;
        });

    }

    private void showVisibilityControlFragment() {
        menu fragment = menu.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    private void loadFragment(Fragment fragment) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void initializeData(String name, String number, int balance, int color) {
        Log.d("initializeData", "Looking for fragment with tag 'cards'");
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("cards");
        if (fragment == null) {
            Log.d("initializeData", "No fragment found with tag 'cards'");
        } else {
            Log.d("initializeData", "Fragment class: " + fragment.getClass().getSimpleName());
        }

        Log.d("initializeData1", "Adding card: Name=" + name + ", Number=" + number + ", Balance=" + balance);
        if (fragment instanceof com.erop.spmapp.SPM.cards) {
            Log.d("initializeData2", "Adding card: Name=" + name + ", Number=" + number + ", Balance=" + balance);

            ((com.erop.spmapp.SPM.cards) fragment).initializeData(name, number, balance, color);
        }
    }
}
