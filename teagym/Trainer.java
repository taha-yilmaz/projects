package org.example.teagym;
public class Trainer extends Staff{
    String certification;
    public Trainer(String name, int age, String phoneNo, String gender, String password,
                   double salary, String[] weeklyWorkSchedule, String certification)
    {
        super(name, age, phoneNo, gender, password, "trainer", salary, weeklyWorkSchedule);
        this.certification = certification;
    }
    public Trainer(String name, int age, String phoneNo, String gender, String password,
                   double salary, String[] weeklyWorkSchedule)
    {
        super(name, age, phoneNo, gender, password, "trainer", salary, weeklyWorkSchedule);
        this.certification = "";
    }
    public String getCertification()
    {
        return certification;
    }

    public void setCertification(String certification)
    {
        this.certification = certification;
    }
    public String toString()
    {
        String measurementsStr = printMeasurements().replaceAll("^\\[|\\]$", "");

        return String.format("%s, %s, %d, %s, %s, %s, %s,%s, %s", getStaffType(), getName(), getAge(), getPhoneNo(), getGender() == null ? "" : getGender(), getPassword(), getSalary(), printWorkSchedule(), measurementsStr);
    }
}
