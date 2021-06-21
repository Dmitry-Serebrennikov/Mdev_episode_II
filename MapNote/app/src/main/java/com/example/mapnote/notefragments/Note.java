package com.example.mapnote.notefragments;

public class Note {

    private String title, desctiption, soundPath = "";

    boolean hasSound = false;

    boolean isLoadedSound = true;

    public void setIsLoadedSound(boolean isLoaded){
        isLoadedSound = isLoaded;
    }
    public boolean getIsLoadedSound(){
        return isLoadedSound;
    }

    public Note(String title, String desctiption){

        this.title = title;
        this.desctiption = desctiption;
    }

    public Note(String title, String desctiption, String soundPath){
        this.title = title;
        this.desctiption = desctiption;
        this.soundPath = soundPath;
        hasSound = true;
    }


    public void setDesctiption(String desctiption){
        this.desctiption = desctiption;
    }
    public String getDescription(){
        return desctiption;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    public boolean isHasSoud(){
        return hasSound;
    }

    public void setSoundPath(String soundPath){
        this.soundPath = soundPath;
    }

    public String getSoundPath(){
        return soundPath;
    }

    public void setHasSound(boolean has){
        hasSound = has;
    }
}
