package com.erop.spmapp;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {
    private Context context;

    public FileHelper(Context context) {
        this.context = context;
    }

    public void createFileInSubfolder() {
        // Получение основной директории файлов
        File directory = context.getFilesDir();

        // Создание объекта File, представляющего вложенную папку
        File subfolder = new File(directory, "subfolder");

        // Проверка существования папки и создание ее при необходимости
        if (!subfolder.exists()) {
            boolean created = subfolder.mkdir();
            if (!created) {
                Log.e("File Directory", "Failed to create directory: " + subfolder.getPath());
                return;
            }
        }

        // Создание нового файла в подкаталоге
        File newFile = new File(subfolder, "example.txt");

        try {
            // Проверка существования файла и создание его при необходимости
            if (!newFile.exists()) {
                boolean created = newFile.createNewFile();
                if (created) {
                    // Запись данных в файл
                    FileWriter writer = new FileWriter(newFile);
                    writer.write("Hello, world!");
                    writer.close();
                } else {
                    Log.e("File Directory", "Failed to create file: " + newFile.getPath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
