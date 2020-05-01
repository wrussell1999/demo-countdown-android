package com.willrussell.hackathon_demo_countdown_android.countdown;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

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
    private Long timestamp;

    @NonNull
    public Integer getTime() {
        return this.time;
    }

    @NonNull
    public Boolean getStart() {
        return this.start;
    }

    @NonNull
    public Long getTimestamp() { return this.timestamp; }
}
