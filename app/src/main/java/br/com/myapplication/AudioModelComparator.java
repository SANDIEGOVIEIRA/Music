package br.com.myapplication;

import java.util.Comparator;

public class AudioModelComparator implements Comparator<AudioModel> {
    @Override
    public int compare(AudioModel song1, AudioModel song2) {
        return song1.getTitle().compareToIgnoreCase(song2.getTitle());
    }
}

