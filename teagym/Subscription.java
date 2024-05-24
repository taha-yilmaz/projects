package org.example.teagym;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class Subscription {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int duration;
    private long remainingTime;
    private String status;
    public Subscription(LocalDateTime startDate, int duration)
    {
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(duration);
        this.duration = duration;
        remainingTime = Duration.between(startDate, endDate).toDays();
        this.status = "active";
    }
    public Subscription(int duration)
    {
        this.startDate = LocalDateTime.now();
        this.endDate = startDate.plusMonths(duration);
        this.duration = duration;
        this.status = "active";
    }
    public boolean checkStatus(LocalDateTime dateTime)
    {
        status = dateTime.compareTo(endDate) > 0 ? "expired" : "active";
        return status.equals("active");
    }
    public long findRemainingTime()
    {
        return Duration.between(startDate, endDate).toDays();
    }

    public LocalDateTime getStartDate()
    {
        return startDate;
    }

    public LocalDateTime getEndDate()
    {
        return endDate;
    }

    public int getDuration()
    {
        return duration;
    }
    private boolean checkStatus()
    {
        status = LocalDateTime.now().compareTo(endDate) > 0 ? "expired" : "active";
        return status.equals("active");
    }

    public String getStatus()
    {
        return (LocalDateTime.now().compareTo(endDate) > 0) ? "expired" : "active";
    }

    public String toString()
    {
        checkStatus();
        String startDateStr = ""; String endDateStr = "";

        int index = startDate.toString().indexOf('T');
        if (index != -1)
            startDateStr = startDate.toString().substring(0, index);

        index = endDate.toString().indexOf('T');
        if (index != -1)
            endDateStr = endDate.toString().substring(0, index);


        return String.format("Start Date : %s\nEnd Date : %s\nSubscription Duration : %s\nRemaining time : %s\nStatus : %s",
                startDateStr, endDateStr, duration,
                status.trim().equalsIgnoreCase("active") ? ChronoUnit.DAYS.between(LocalDateTime.now(), endDate) : "------", status );
    }


}

