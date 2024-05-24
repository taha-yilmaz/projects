package org.example.teagym;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class GymService implements IAdminService, IMemberService {
    private static GymService instance;
    String name;
    String address;
    ArrayList<Equipment> equipments = new ArrayList<>();
    ArrayList<Staff> staffs = new ArrayList<>();
    ArrayList<Member> members = new ArrayList<>();
    ArrayList<Trainer> trainers = new ArrayList<>();
    double[] memberShipFeeList = new double[4];
    String membersFileFath;
    String staffsFilePath;
    // Private constructor to prevent instantiation
    private GymService(String name, String address, String membersFileFath, String staffsFilePath) {
        this.name = name;
        this.address = address;
        this.membersFileFath = membersFileFath;
        this.staffsFilePath = staffsFilePath;
        fileHandler();
    }

    // Static method to get the singleton instance
    public static GymService getInstance(String name, String address, String membersFileFath, String staffsFilePath) {
        if (instance == null) {
            instance = new GymService(name, address, membersFileFath, staffsFilePath);
        }
        return instance;
    }
    private void fileHandler()
    {
        try {
            File file = new File(membersFileFath);
            Scanner scanner = new Scanner(file);
            scanner.nextLine();  // Skip the header

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",", -1);
                if (parts.length > 8)
                    readMembersFromCsv(parts);
                else if(parts.length > 13)
                    readStaffFromCsv(parts);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            File file = new File(staffsFilePath);
            Scanner scanner = new Scanner(file);
            scanner.nextLine();  // Skip the header

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",", -1);
                if(parts.length > 13)
                    readStaffFromCsv(parts);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            File file = new File("equipments.csv");
            Scanner scanner = new Scanner(file);
            scanner.nextLine();  // Skip the header

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                Equipment equipment = new Equipment(parts[0].trim(), Integer.parseInt(parts[1].trim()));
                for (int i = 2; i < parts.length; i++)
                    equipment.addCategory(parts[i].trim());
                equipments.add(equipment);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
    private void readMembersFromCsv(String[] parts)
    {
        String[] dates = parts[7].split("-");
        LocalDateTime startDate = LocalDateTime.of(Integer.parseInt(dates[0].trim()), Integer.parseInt(dates[1].trim()), Integer.parseInt(dates[2].trim()), 12, 0);
        Subscription subscription = new Subscription(startDate, Integer.parseInt(parts[8].trim()));

        Member member = new Member(parts[1], parts[2].equals("") ? 0 : Integer.parseInt(parts[2].trim()), parts[3].trim(), parts[4], parts[5].trim(), (parts[6].trim().equals("")? 0 : Integer.parseInt(parts[6])));
        addNewMember(member);
        addSubscription(parts[3].trim(), subscription);
        String[] strs = new String[7];
        for (int i = 0; i < 7; i++)
            strs[i] = parts[9 + i];
        Training training = new Training(strs);
        member.addTraining(training);
        if (parts.length > 16)
            for (int i = 0; i < (parts.length - 16) / 3; i++)
                member.addMeasurement(new Measurement(Double.parseDouble(parts[16 + 3 * i].trim()), Double.parseDouble(parts[17 + 3 * i].trim()), Double.parseDouble(parts[18 + 3 * i].trim())));
    }
    private void readStaffFromCsv(String[] parts)
    {
        String[] works = new String[7];
        for(int i = 0; i < 7;i++)
            works[i] = parts[7 + i];
        Trainer trainer = null;
        Staff staff = null;
        if(parts[0].trim().equalsIgnoreCase("trainer")){
            trainer = new Trainer(parts[1], parts[2].equals("") ? 0 : Integer.parseInt(parts[2].trim()), parts[3].trim(), parts[4], parts[5].trim(), Double.parseDouble(parts[6].trim()), works);
            addNewTrainer(trainer);
        }
        else{
            staff = new Staff(parts[1], parts[2].equals("") ? 0 : Integer.parseInt(parts[2].trim()), parts[3].trim(), parts[4], parts[5].trim(), parts[0].trim(), Double.parseDouble(parts[6].trim()), works);
            addNewStaff(staff);
        }


        if (parts.length > 14 && parts[0].trim().equalsIgnoreCase("trainer"))
            for (int i = 0; i < (parts.length - 14) / 3; i++)
                trainer.addMeasurement(new Measurement(Double.parseDouble(parts[14 + 3 * i].trim()), Double.parseDouble(parts[15 + 3 * i].trim()), Double.parseDouble(parts[16 + 3 * i].trim())));
        else if(parts.length > 14)
            for (int i = 0; i < (parts.length - 14) / 3; i++)
                staff.addMeasurement(new Measurement(Double.parseDouble(parts[14 + 3 * i].trim()), Double.parseDouble(parts[15 + 3 * i].trim()), Double.parseDouble(parts[16 + 3 * i].trim())));

    }

    public void addNewMember(String name, int age, String phoneNo, String gender, String password, int cabinet)
    {
        if(getMember(phoneNo) == null) {
            Member member = new Member(name, age, phoneNo, gender, password, cabinet);
            members.add(member);
            FileOperationsHandler.appendToFile(membersFileFath, member.toString());
            return;
        }

        throw new RuntimeException("Member already exist!!!");
    }
    public void addNewTrainer(Trainer trainer)
    {
        trainers.add(trainer);
    }
    private void addNewMember(Member member)
    {
        members.add(member);
    }

    public void addSubscription(String phone, LocalDateTime startDate, int duration)
    {
        Member member = getMember(phone);
        if (member != null) {
            member.addSubscription(startDate, duration);
            FileOperationsHandler.updateFile(membersFileFath ,member.getPhoneNo().trim(), member.toString(), 3);
            return;
        }
        throw new RuntimeException("Member not exist!!!");
    }
    public void addSubscription(String phone, Subscription subscription)
    {
        Member member = getMember(phone);
        if (member != null)
            member.addSubscription(subscription);
    }

    public void addNewStaff(Staff staff)
    {
        staffs.add(staff);
    }
    public void addNewStaff(String name, int age, String phoneNo, String gender, String password, String type, double salary, String[] works)
    {
        if(getStaff(phoneNo) == null) {
            Staff staff = new Staff(name, age, phoneNo, gender, password, type, salary, works);
            staffs.add(staff);
            FileOperationsHandler.appendToFile(staffsFilePath, staff.toString());
            return;
        }
        throw new RuntimeException("Staff already exist!!!");
    }
    public void addNewTrainer(String name, int age, String phoneNo, String gender, String password, double salary, String[] works)
    {
        if(getTrainer(phoneNo) == null) {
            Trainer trainer = new Trainer(name, age, phoneNo, gender, password, salary, works);
            staffs.add(trainer);
            FileOperationsHandler.appendToFile(staffsFilePath, trainer.toString());
            return;
        }
        throw new RuntimeException("Staff already exist!!!");
    }
    public void addWorkSchedule(String phone, WorkSchedule workSchedule)
    {
        Staff staff = getStaff(phone.trim());
        Trainer trainer = getTrainer(phone.trim());
        if(trainer != null) {
            trainer.addWorkSchedule(workSchedule);
            FileOperationsHandler.updateFile(staffsFilePath, trainer.getPhoneNo(), trainer.toString(), 3);
            return;
        }
        if(staff != null) {
            staff.addWorkSchedule(workSchedule);
            FileOperationsHandler.updateFile(staffsFilePath, staff.getPhoneNo(), staff.toString(), 3);
            return;
        }
        throw new RuntimeException("Staff not exist!!!");
    }

    @Override
    public void changeMembershipFeeList(double[] fees)
    {
        memberShipFeeList = fees;
    }

    public void setDailyTraining(String phone, int dayNo, String t)
    {
        Member member = getMember(phone);
        if (member != null) {
            member.getTraining().addTraining(dayNo, t);
            FileOperationsHandler.updateFile(membersFileFath, member.getPhoneNo().trim(), member.toString(), 3);
            return;
        }
        throw new RuntimeException("Member not exist!!!");
    }

    public void setPassword(String phone, String password)
    {
        Member member = getMember(phone);
        Staff staff = getStaff(phone);
        Trainer trainer = getTrainer(phone);
        if(member != null) {
            member.setPassword(password);
            FileOperationsHandler.updateFile(membersFileFath, phone, member.toString(), 3);
            return;
        }
        else if(trainer != null) {
            trainer.setPassword(password);
            FileOperationsHandler.updateFile(staffsFilePath, phone, trainer.toString(), 3);
            return;
        }
        else if(staff != null) {
            staff.setPassword(password);
            FileOperationsHandler.updateFile(staffsFilePath, phone, member.toString(), 3);
            return;
        }
        throw new RuntimeException("Person not exist!!!");
    }

    public void addNewMeasurement(String phone, Measurement measurement)
    {
        Member member = getMember(phone);
        Staff staff = getStaff(phone);
        Trainer trainer = getTrainer(phone);
        if(member != null) {
            member.addMeasurement(measurement);
            FileOperationsHandler.updateFile(membersFileFath,phone, member.toString(), 3);
            return;
        }
        else if(trainers != null) {
            trainer.addMeasurement(measurement);
            FileOperationsHandler.updateFile(staffsFilePath,phone, trainer.toString(), 3);
            return;
        }
        else if(staff != null) {
            staff.addMeasurement(measurement);
            FileOperationsHandler.updateFile(staffsFilePath,phone, staff.toString(), 3);
            return;
        }
        throw new RuntimeException("Person not exist!!!");
    }

    public String getGymName()
    {
        return name;
    }

    @Override
    public void setGymName(String name)
    {
        this.name = name;
    }

    @Override
    public int memberCount()
    {
        int count = 0;
        for(Member m : members)
            if(m.checkStatus())
                count++;
        return count;
    }

    @Override
    public String membershipFeeList()
    {
        return String.format("1 month : %.2f\n3 months : %.2f\n6 months : %.2f\n12 months : %.2f\n", memberShipFeeList[0], memberShipFeeList[1], memberShipFeeList[2], memberShipFeeList[3]);
    }

    @Override
    public double hallCrowdRate()
    {
        return 100 * (members.size() / 100.);
    }

    @Override
    public double manWomanRate()
    {
        double menCount = 0;
        double womenCount = 0;
        for (Member member : members) {
            if (!member.getGender().trim().equalsIgnoreCase("") && member.getGender().trim().equalsIgnoreCase("male"))
                menCount++;
            else if (!member.getGender().trim().equalsIgnoreCase("") && member.getGender().trim().equalsIgnoreCase("female"))
                womenCount++;
        }

        return 100 * ((menCount + womenCount) > 0 ? menCount / (menCount + womenCount) : 0);
    }

    @Override
    public String getAddress()
    {
        return address;
    }

    @Override
    public String getGymPhone()
    {
        return name;
    }

    @Override
    public void addEquipment(Equipment equipment)
    {
        equipments.add(equipment);
    }
    public void addEquipment(String name, int quantity, String[] areas)
    {
        for (Equipment equipment : equipments)
            if(equipment.getName().trim().equalsIgnoreCase(name)) {
                equipment.addQuantity(quantity);
                FileOperationsHandler.updateFile("equipments.csv", equipment.getName().trim(), equipment.toString(),0);
                return;
            }
        Equipment equipment = new Equipment(name, areas, quantity);
        equipments.add(equipment);
        FileOperationsHandler.appendToFile("equipments.csv", equipment.toString());
    }

    public String viewMeasurements(ArrayList<Measurement> measurements)
    {
        String str = "";

        for (Measurement measurement: measurements)
            str += String.format("Height : %.2f\nWeight : %.2f\nFat Rate : %.2f\n-----------------\n",
                    measurement.getHeight(), measurement.getWeight(), measurement.getFat_rate());
        return str;
    }
    public String viewTraining(Member member)
    {
        return member.getTraining().printTraining();
    }

    @Override
    public Double totalSalary()
    {
        double salary = 0;

        for (Trainer trainer: trainers){
            salary += trainer.getSalary();
        }
        for (Staff staff : staffs){
            salary += staff.getSalary();
        }

        return salary;
    }

    @Override
    public String allEquipments()
    {
        return equipments.toString().replaceAll(",","\n").replaceAll("^\\[|\\]$", "");
    }

    @Override
    public void addCabinetNo(String phone, int cabinetNo)
    {
        Member member = getMember(phone);
        member.setCabinetNo(cabinetNo);
    }

    @Override
    public String getMemberPhone()
    {
        return null;
    }
    public String printGymDetailsMember()
    {
        return String.format("Name : %s\nAddress : %s\nHall Crowd Rate : %.2f\nHall Men-Women Rate : %.2f",
                getGymName(), getAddress(), hallCrowdRate(), manWomanRate());
    }
    public String printGymDetailsAdmin()
    {
        return String.format("Name : %s\nAddress : %s\nHall Crowd Rate : %.2f\nHall Men-Women Rate : %.2f\nTotal Salary : %.2f",
                getGymName(), getAddress(), hallCrowdRate(), manWomanRate(), totalSalary());
    }

    public Member getMember(String phone)
    {
        if(members.size() != 0)
            for (Member member : members) {
                if (phone.trim().equals(member.getPhoneNo().trim()))
                    return member;
            }
        return null;
    }
    public Staff getStaff(String phone)
    {
        if(staffs.size() != 0)
            for (Staff staff : staffs) {
                if (phone.trim().equals(staff.getPhoneNo().trim()))
                    return staff;
            }
        return null;
    }
    public Trainer getTrainer(String phone){
        if(trainers.size() != 0)
            for (Trainer trainer : trainers) {
                if (phone.trim().equals(trainer.getPhoneNo().trim()))
                    return trainer;
            }
        return null;
    }
}