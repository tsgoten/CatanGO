package com.example.tsgot.catango;

/**
 * Created by tsgot on 2/8/2018.
 */

public class Resource {
    private int imageID, count;
    private String name;

    public Resource(String name, int imageID){
        this.imageID = imageID;
        count = 0;
        this.name = name;
    }

    public int getImageID(){
        return imageID;
    }
    public int getCount(){
        return count;
    }
    public String getName(){
        return name;
    }
    public int addCount(int a){
        return count + a;
    }
    public int addCount(){
        return count++;
    }
    public int getResourceID(){
        switch(name){
            case "Clay":
                return 0;
            case "Wood":
                return 1;
            case "Wool":
                return 2;
            case "Grain":
                return 3;
            case "Ore":
                return 4;
            default:
                return -1;
        }
    }
}
