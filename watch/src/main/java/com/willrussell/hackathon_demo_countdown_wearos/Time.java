package com.willrussell.hackathon_demo_countdown_wearos;

import com.google.firebase.database.IgnoreExtraProperties;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@IgnoreExtraProperties
public class Time {
    @NonNull
    private Integer time;
    @NonNull
    private Boolean start;

    @NonNull
    public Integer getTime() {
        return this.time;
    }

    @NonNull
    public Boolean getStart() {
        return this.start;
    }
}
