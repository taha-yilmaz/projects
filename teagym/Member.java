package org.example.teagym;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Member extends ADTPerson {
    private Subscription subscription = new Subscription(0);
    private int cabinetNo = 0;
    private Training training = new Training(new String[]{"","","","","","",""});

    public Member(String name, int age, String phoneNo, String gender, String password,
                  Subscription subscription, int cabinetNo, Training training)
    {
        super(name, age, phoneNo, gender, password);
        this.subscription = subscription;
        this.cabinetNo = cabinetNo;
        this.training = training;
    }
    public Member(String name, int age, String phoneNo, String gender, String password,
                  Subscription subscription)
    {
        super(name, age, phoneNo, gender, password);
        this.subscription = subscription;
    }
    public Member(String name, int age, String phoneNo, String gender, String password, int cabinetNo)
    {
        super(name, age, phoneNo, gender, password);
        this.cabinetNo = cabinetNo;
    }

    public void addTraining(Training training)
    {
        this.training = training;
    }

    public Training getTraining()
    {
        return training;
    }

    public int getCabinetNo()
    {
        return cabinetNo;
    }

    public void setCabinetNo(int cabinetNo)
    {
        this.cabinetNo = cabinetNo;
    }

    public void addSubscription(LocalDateTime startDate, int duration)
    {
        subscription = new Subscription(startDate, duration);
    }
    public void addSubscription(Subscription subscription)
    {
        this.subscription = subscription;
    }
    public boolean checkStatus()
    {
        String status = LocalDateTime.now().compareTo(subscription.getEndDate()) > 0 ? "expired" : "active";
        return status.equals("active");
    }
    public String printSubscription()
    {
        checkStatus();
        String startDateStr = ""; String endDateStr = "";

        int index = subscription.getStartDate().toString().indexOf('T');
        if (index != -1)
            startDateStr = subscription.getStartDate().toString().substring(0, index);

        index = subscription.getEndDate().toString().indexOf('T');
        if (index != -1)
            endDateStr = subscription.getEndDate().toString().substring(0, index);


        return String.format("Start Date : %s\nEnd Date : %s\nSubscription Duration : %s\nRemaining time : %s\nStatus : %s",
                startDateStr, endDateStr, subscription.getDuration(),
                checkStatus() ? ChronoUnit.DAYS.between(LocalDateTime.now(), subscription.getEndDate()) : "------", subscription.getStatus());
    }

    @Override
    public boolean equals(Object obj)
    {
        Member m = (Member) obj;
        return getPhoneNo().equals(m.getPhoneNo());
    }


    @Override
    public String toString()
    {
         String measurementsStr = printMeasurements().replaceAll("^\\[|\\]$", "");

         String str = subscription != null ? subscription.getStartDate().toString() : "";
        int index = str.indexOf('T');
        if (index != -1) {
            str = str.substring(0, index);
        }
         return "m, " + getName() + "," + getAge() + "," + getPhoneNo() + "," + (getGender() == null?"" : getGender())+ "," + getPassword()
            + "," + (cabinetNo == 0 ? "": cabinetNo)+ "," + str + "," + (subscription == null ? "" : subscription.getDuration()) + ","+ (training == null ? ",,,,,,,": training) + measurementsStr;
    }
}

