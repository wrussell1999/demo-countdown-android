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

    public Time(Long time, Boolean start, Long epoch) {
        this.time = time;
        this.start = start;
        this.epoch = epoch;
    }

    @NonNull
    private Long time;
    @NonNull
    private Boolean start;

    @NonNull
    private Long epoch;

    @NonNull
    public Long getTime() {
        return this.time;
    }

    @NonNull
    public Boolean getStart() {
        return this.start;
    }

    @NonNull
    public Long getEpoch() { return this.epoch; }
}
