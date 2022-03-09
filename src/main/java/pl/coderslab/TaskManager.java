package pl.coderslab;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TaskManager {

    private static final String FILEPATH = "tasks.csv";

    private static String[][] addToArray2D(String[][] tab, String newElement[]) {
        String[][] resizedTab = new String[tab.length + 1][];
        for (int i = 0; i < tab.length; i++) {
            resizedTab[i] = tab[i];
        }
        resizedTab[resizedTab.length - 1] = newElement;
        return resizedTab;
    }

    private static String[][] removeFromArray2D(String[][] tab, int erasedElementNumber) {
        String[][] resizedTab = new String[tab.length - 1][];
        for (int i = 1; i < tab.length; i++) {
            if(i<erasedElementNumber+1) {
                resizedTab[i-1] = tab[i-1];
            } else if(i>=erasedElementNumber+1){
                resizedTab[i-1] = tab[i];
            }
        }
        return resizedTab;
    }

    private static void listAvailableOptions(){
        String[] availableOptions = new String[]{"add","remove","list","exit"};
        System.out.println(ConsoleColors.BLUE + "Please select an option:" + ConsoleColors.RESET);
        for(String k : availableOptions){
            System.out.println(k);
        }
    }

    private static void listAllTasks(String[][] taskArray) {
        int counter = 0;
        for(String[] k : taskArray){
            System.out.println(counter + " : " + k[0]+ ", " +k[1]+ ", " +k[2]);
            counter++;
        }

    }

    private static String[][] loadTaskArray() {
        String[][] tasksArray = new String[0][3];
        Scanner scan = null;
        File tasksDataBase = new File(FILEPATH);
        try {
            scan = new Scanner(tasksDataBase);
            while (scan.hasNextLine()) {
                tasksArray = addToArray2D(tasksArray, scan.nextLine().split(","));
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            System.out.println("no database file exist");
        } finally {
            if(scan!=null) {
                scan.close();
            }
        }

        return tasksArray;
    }

    private static String[][] addNewTask (String[][] taskArray){
        Scanner scan = new Scanner(System.in);
        boolean validOption = false;
        String choice = "";
        String[] newTask = new String[3];
        System.out.println("Please add task description:");
        newTask[0] = scan.nextLine();
        System.out.println("Please add task due date:");
        newTask[1] = scan.nextLine();
        while (!validOption){
            System.out.println("Is your task important: true/false");
            choice = scan.nextLine();
            if(choice.equals("true")){
                validOption = true;
            }
            if(choice.equals("false")){
                validOption = true;
            }
        }
        newTask[2]=choice;
        taskArray=addToArray2D(taskArray,newTask);
        return taskArray;
    }

    private static String[][] removeTask (String[][] taskArray){
        Scanner scan = new Scanner(System.in);
        boolean isValidNumber = false;
        int choice = 0;
        System.out.println("Please select which task you would like to remove:");
       while(!isValidNumber){
           try{
               choice = Integer.parseInt(scan.nextLine());
               if(choice<taskArray.length && choice>=0) {
                   isValidNumber = true;
               } else {
                   System.out.println("No task assigned to this value. Try again: ");
               }
           } catch (NumberFormatException e){
               System.out.println("Only numeric value accepted. Try again:");
           }
       }
       taskArray=removeFromArray2D(taskArray,choice);
       return taskArray;
    }

    private static void saveToFile(String[][] taskArray){
        File file = new File(FILEPATH);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file,false);
                for(String[] k : taskArray){
                    fileWriter.write(k[0]+","+k[1]+","+k[2]+"\n");
                }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileWriter!=null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        String[][] taskArray = loadTaskArray();
        listAvailableOptions();
        Scanner scan = new Scanner(System.in);

        while(true){
            switch (scan.nextLine()){
            case "add":
                taskArray=addNewTask(taskArray);
                listAvailableOptions();
                break;
            case "remove":
                taskArray=removeTask(taskArray);
                listAvailableOptions();
                break;
            case "list":
                listAllTasks(taskArray);
                listAvailableOptions();
                break;
            case "exit":
                 saveToFile(taskArray);
                System.out.println(ConsoleColors.RED+"Bye, bye");
                 return;
            default:
                listAvailableOptions();
        }
        }
    }
}
