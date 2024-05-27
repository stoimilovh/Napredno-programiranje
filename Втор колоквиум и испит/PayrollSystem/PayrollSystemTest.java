//Да се имплементира апликација за евиденција на работниот ангажман на вработени во една
//        ИТ компанија. За таа цел да се имплементира класата PayrollSystem во која што
//        ќе се чуваат информации за вработени во компанијата. Постојат два типа на вработени
//        HourlyEmployee и FreelanceEmployee. HourlyEmployee добиваат плата базирана на вкупниот
//        број на изработени часови, додека пак FreelanceEmployee добиваат плата базирана на
//        поените на тикетите што ги решиле. За класата PayrollSystem да се имплементираат:
//
//        PayrollSystem(Map<String,Double> hourlyRateByLevel, Map<String,Double> ticketRateByLevel) -
//        конструктор со два аргументи - мапи. Првата мапа означува колку е саатницата за соодветно
//        ниво за вработените што земаат плата по час работа, а втората мапа означува колку е платата
//        по поен од тикет за соодветното ниво за фриленсерите.
//        void readEmployeesData (InputStream is) - метод за вчитување на податоците за вработените
//        во компанијата, при што за секој вработен податоците се дадени во нов ред. Податоците за
//        вработените се во следниот формат:
//        Доколку вработениот е HourlyEmployee: H;ID;level;hours;
//        Доколку вработениот е FreelanceEmployee: F;ID;level;ticketPoints1;ticketPoints2;
//        ...;ticketPointsN;
//        Map<String, Collection<Employee>> printEmployeesByLevels
//        (OutputStream os, Set<String> levels) - метод којшто нa излезен поток ќе врати мапа од
//        вработeните во нивоата levels групирани по нивоа. Вработените да бидат сортирани според
//        плата во опаѓачки редослед во рамките на нивото. Доколку платата е иста, да се споредуваат
//        според нивото.
//        Дополнителни информации:
//
//        Платата на HourlyEmployee се пресметува така што сите часови работа до 40 часа се множат со
//        саатницата определена за нивото, а сите часови работа над 40 часа, се множат со саатницата на
//        нивото зголемена за коефициент 1.5.
//        Платата на FreelanceEmployee се пресметува така што сумата на поените на тикетите коишто
//        програмерот ги решил се множат со плата по тикет (ticket rate) за нивото.

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

abstract class Employee{
    String id;
    String level;
    double payment;

    public Employee(String id, String level, double payment) {
        this.id = id;
        this.level = level;
        this.payment = payment;
    }
    public String getLevel() {
        return level;
    }
    abstract double getSalary();
    @Override
    public abstract String toString();
}

class FreelanceEmployee extends Employee{
    List<Integer>points;

    public FreelanceEmployee(String id, String level, double paymet, List<Integer> points) {
        super(id, level, paymet);
        this.points = points;
    }
    int sumTickets(){
        return (int) points.stream().mapToDouble(Integer::doubleValue).sum();
    }
    double getSalary(){
        return this.sumTickets()*payment;
    }
    @Override
    public String toString(){
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d", id, level, this.getSalary(), points.size(), this.sumTickets());
    }
}
class HourlyEmployee extends Employee{
    double hours;

    public HourlyEmployee(String id, String level, double paymet, double hours) {
        super(id, level, paymet);
        this.hours = hours;
    }
    double getSalary(){
        double salary;
        if(hours<=40){
            salary=hours*payment;
        }
        else {
            salary=40*payment;
        }
        double number=hours-40;
        if(number>0){
            salary+=number*(payment*1.5);
        }
        return salary;
    }
    @Override
    public String toString(){
        double number=40;
        if(hours<40){
            number=hours;
        }
        double overtime=0;
        if(hours-40>0){
            overtime=hours-40;
        }
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f", id, level, this.getSalary(), number, overtime);
    }
}
class PayrollSystem{
    Map<String,Double> hourlyRateByLevel;
    Map<String,Double> ticketRateByLevel;
    Map<String, List<Employee>>emplyee;

    public PayrollSystem(Map<String,Double> hourlyRateByLevel, Map<String,Double> ticketRateByLevel){
        this.hourlyRateByLevel=hourlyRateByLevel;
        this.ticketRateByLevel=ticketRateByLevel;
        this.emplyee=new HashMap<>();
    }
    void readEmployees (InputStream is){
        Scanner sc=new Scanner(is);
        while (sc.hasNextLine()){
            String s=sc.nextLine();
            String []parts=s.split(";");
            if(parts[0].equals("F")){
                List<Integer>list=new ArrayList<>();
                for(int i=3; i<parts.length; i++){
                    list.add(Integer.parseInt(parts[i]));
                }
                double payment=0;
                if(ticketRateByLevel.containsKey(parts[2])){
                    payment=ticketRateByLevel.get(parts[2]);
                }
                FreelanceEmployee fe=new FreelanceEmployee(parts[1], parts[2], payment, list);
                List<Employee>list1=new ArrayList<>();
                if(emplyee.containsKey(parts[2])){
                    list1=emplyee.get(parts[2]);
                }
                list1.add(fe);
                emplyee.put(parts[2], list1);
            }
            if(parts[0].equals("H")){
                double payment=0;
                if(hourlyRateByLevel.containsKey(parts[2])){
                    payment=hourlyRateByLevel.get(parts[2]);
                }
                HourlyEmployee he=new HourlyEmployee(parts[1], parts[2], payment, Double.parseDouble(parts[3]));
                List<Employee>list1=new ArrayList<>();
                if(emplyee.containsKey(parts[2])){
                    list1=emplyee.get(parts[2]);
                }
                list1.add(he);
                emplyee.put(parts[2], list1);
            }
        }
    }
    Map<String, Set<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels){
        Map<String, Set<Employee>>result=new HashMap<>();
        for (String s:levels) {
            if(emplyee.containsKey(s)){
                List<Employee>employeeList=emplyee.get(s);
                employeeList.sort(Comparator.comparingDouble(Employee::getSalary).reversed().thenComparing(Employee::getLevel));
                Set<Employee>employeeSet=new LinkedHashSet<>(employeeList);
                result.put(s, employeeSet);
            }
        }
        Map<String, Set<Employee>>tmp=new TreeMap<>(result);
        return tmp;
    }
}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i=5;i<=10;i++) {
            levels.add("level"+i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });
    }
}