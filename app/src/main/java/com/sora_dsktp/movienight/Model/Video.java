/*
 * Copyright © 2018 by Georgios Kostogloudis
 * All rights reserved.
 */

package com.sora_dsktp.movienight.Model;

import com.google.gson.annotations.SerializedName;

/**
 * This file created by Georgios Kostogloudis
 * and was last modified on 31/3/2018.
 * The name of the project is MovieNight and it was created as part of
 * UDACITY ND programm.
 */
public class Video
{
    @SerializedName("id")
    private String video_id;
    @SerializedName("key")
    private String youtubeKey;
    private String name;

    public Video(String video_id, String youtubeKey, String name) {
        this.video_id = video_id;
        this.youtubeKey = youtubeKey;
        this.name = name;
    }

    public String getVideo_id()
    {
        return video_id;
    }

    public void setVideo_id(String video_id)
    {
        this.video_id = video_id;
    }

    public String getYoutubeKey()
    {
        return youtubeKey;
    }

    public void setYoutubeKey(String youtubeKey) {
        this.youtubeKey = youtubeKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
