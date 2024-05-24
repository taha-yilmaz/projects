package org.example.teagym;
public class Staff extends ADTPerson{
    private StaffType staffType = StaffType.CLEANER;
    private double salary;
    private WorkSchedule weeklyWorkSchedule;
    public Staff(String name, int age, String phoneNo, String gender, String password,
                 String staffType, double salary, String[] weeklyWorkSchedule) {
        super(name, age, phoneNo, gender, password);
        this.salary = salary;
        this.weeklyWorkSchedule = new WorkSchedule(weeklyWorkSchedule);

        if(staffType == null) {
            this.staffType = StaffType.CLEANER;
            return;
        }

        switch (staffType.toUpperCase()) {
            case "CLEANER":
                this.staffType = StaffType.CLEANER;
                break;
            case "RECEPTIONIST":
                this.staffType = StaffType.RECEPTIONIST;
                break;
            case "TRAINER":
                this.staffType = StaffType.TRAINER;
                break;
        }
    }
    public void addWorkSchedule(WorkSchedule workSchedule)
    {
        this.weeklyWorkSchedule = workSchedule;
    }

    public double getSalary()
    {
        return salary;
    }

    public void setSalary(double salary)
    {
        this.salary = salary;
    }

    public void setStaffType(StaffType staffType)
    {
        this.staffType = staffType;
    }
    public StaffType getStaffType()
    {
        return staffType;
    }

//    public WorkSchedule getWeeklyWorkSchedule()
//    {
//        return weeklyWorkSchedule;
//    }
    public String printWorkSchedule(){
        return weeklyWorkSchedule.toString();
    }

    @Override
    public String toString()
    {
        String measurementsStr = printMeasurements().replaceAll("^\\[|\\]$", "");

        return String.format("%s, %s, %d, %s, %s, %s, %s,%s, %s", staffType == null ? StaffType.CLEANER : staffType, getName(), getAge(), getPhoneNo(), getGender() == null ? "" : getGender(), getPassword(), salary, weeklyWorkSchedule, measurementsStr);
    }

    enum StaffType {
        CLEANER, RECEPTIONIST, TRAINER;
    }
}
