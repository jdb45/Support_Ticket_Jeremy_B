package com.jeremy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class TicketManager {
    //creating two linkedlists to store the tickets and resolved tickets 
    public static ResolvedTicket resolvedTickets;
    public static LinkedList<Ticket> ticketQueue = new LinkedList<Ticket>();


    public static void main(String[] args) throws IOException {
        
        Scanner scan = new Scanner(System.in);
        //creating two variables
        Scanner wordFile;
        String ticketWord;
        //try block to check if the file exists or not
        try {
            wordFile = new Scanner(new FileReader("open_tickets.txt"));
            wordFile.useDelimiter(" ");
        //creating variables to store the data from the open tickets files
        ticketWord = wordFile.next();
        int ticketID = 0;
        ArrayList<String> description = new ArrayList<>();
        int priority = 0;
        ArrayList<String> reporter = new ArrayList<>();
        String descString = "";
        String repoterString = "";
        ArrayList<String> dateFormat = new ArrayList<>();
            String time = "";
        //setting a while loop to check if the file has data
        while (wordFile.hasNext()) {
            //getting the ID from the file
            if (ticketWord.contains("ID=")) {
                ticketWord = wordFile.next();
                ticketID = Integer.parseInt(ticketWord);
                ticketWord = wordFile.next();
                continue;
                //getting the description of the problem, having it check if the word equals Issued, and then ending on Priority to get
                //the whole description, and adding it to an arraylist
            }
            else if (ticketWord.contains("Issued:")) {
                ticketWord = wordFile.next();
                while (!ticketWord.equals("Priority:")) {
                    for (int i = 0; i < 1; i++) {
                        description.add(ticketWord);
                        ticketWord = wordFile.next();
                    }
                }
                continue;

            } //checking when the word contains Priority, and parsing it to an int
            else if (ticketWord.contains("Priority:")) {
                ticketWord = wordFile.next();
                priority = Integer.parseInt(ticketWord);
                ticketWord = wordFile.next();
                continue;

            } //this will skip past the word Reported
            else if (ticketWord.contains("Reported")) {
                ticketWord = wordFile.next();
                continue;

            } //getting the info for who reported the ticket, and ending on reported to get the whole user who reported it
            else if (ticketWord.contains("by:")) {
                ticketWord = wordFile.next();
                while (!ticketWord.equals("Reported")) {
                    for (int i = 0; i < 1; i++) {
                        reporter.add(ticketWord);
                        ticketWord = wordFile.next();
                    }
                }
                continue;

            } //skipping past the word on
            else if (ticketWord.contains("on:")) {
                ticketWord = wordFile.next();
                continue;

            } /*else to add the date format to an arraylist, and end the reading of each line by ending it with a "."
                and each for loop to convert each arrylist to a string, and split each word*/
            else {
                dateFormat.add(ticketWord);
                ticketWord = wordFile.next();
                if (ticketWord.contains(".")) {
                    for (String s : description) {
                        descString += s + " ";
                    }
                    for (String s : reporter) {
                        repoterString += s + " ";
                    }
                    dateFormat.add("2016");
                    for (String s : dateFormat) {
                        time += s + " ";
                    }
                    //converting the date to the correct format
                    DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                    Date date = df.parse(time);
                    /*adding each ticket to the ticket method, and adding it to the Ticket priority method
                    also resetting each variable to 0 or empty string for the next ticket*/
                    Ticket t = new Ticket(descString, priority, repoterString, date);
                    addTicketInPriorityOrder(ticketQueue, t);
                    description = new ArrayList<>();
                    reporter = new ArrayList<>();
                    repoterString = "";
                    descString = "";
                    dateFormat = new ArrayList<>();
                    time = "";


                }
            }

        }
        //catch blocks to catch any errors
        } catch (FileNotFoundException e) {
            System.err.println(e);
            System.out.println();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //print out a list that will show up unless the user picks to exit
        while(true){
            try {
            System.out.println("1. Enter Ticket\n2. Delete Ticket by ID \n3. Delete by Issue \n4. Search by Name \n5. Display All Tickets\n6. Quit");
            int task = Integer.parseInt(scan.nextLine());

            if (task == 1) {
                //Call addTickets, which will let us enter any number of new tickets
                addTickets(ticketQueue);

            } else if (task == 2) {
                printAllTickets(ticketQueue);
                //delete a ticket by ID
                deleteTicket(ticketQueue);

            } else if (task == 3) {
                ticketSearch(ticketQueue);
                deleteTicket(ticketQueue);

            } else if (task == 4) {
                ticketSearch(ticketQueue);
                //this else if will exit the program and save a file for teh open tickts and the resolved tickets
            } else if (task == 6) {
                Date date = new Date();
                FileWriter writerResolved = new FileWriter("Resolved_tickets_as_of_" + date + ".txt");
                FileWriter writerOpenTickets = new FileWriter("open_tickets.txt");
                BufferedWriter bufWriterOpen = new BufferedWriter(writerOpenTickets);
                BufferedWriter bufWriterResolved = new BufferedWriter(writerResolved);
                for(Ticket ticketPrint : ticketQueue){
                bufWriterOpen.write(String.valueOf(ticketPrint + "\n"));
                }
                bufWriterResolved.write(String.valueOf(resolvedTickets));
                bufWriterOpen.close();
                bufWriterResolved.close();
                System.out.println("Quitting program");
                break;
            } else {
                //this will happen for 5 or any other selection that is a valid int
                //Default will be print all tickets
                printAllTickets(ticketQueue);
            }
            //try catch for checking user input to make sure its availd number
        }catch (Exception ioe){
            System.out.println("Please enter one of the numbers listed in the menu");
        }

        }

        scan.close();

    }


    protected static void addTickets(LinkedList<Ticket> ticketQueue) {
        Scanner sc = new Scanner(System.in);
        boolean moreProblems = true;
        String description, reporter;
        Date dateReported = new Date(); //Default constructor creates date with current date/time
        int priority;

        while (moreProblems){
            System.out.println("Enter problem");
            description = sc.nextLine();
            System.out.println("Who reported this issue?");
            reporter = sc.nextLine();
            System.out.println("Enter priority of " + description);
            priority = Integer.parseInt(sc.nextLine());

            Ticket t = new Ticket(description, priority, reporter, dateReported);
            //ticketQueue.add(t);
            addTicketInPriorityOrder(ticketQueue, t);
            printAllTickets(ticketQueue);

            System.out.println("More tickets to add?");
            String more = sc.nextLine();
            if (more.equalsIgnoreCase("N")) {
                moreProblems = false;
            }
        }
    }

    protected static void addTicketInPriorityOrder(LinkedList<Ticket> tickets, Ticket newTicket){

        //Logic: assume the list is either empty or sorted

        if (tickets.size() == 0 ) {//Special case - if list is empty, add ticket and return
            tickets.add(newTicket);
            return;
        }

        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end

        int newTicketPriority = newTicket.getPriority();

        for (int x = 0; x < tickets.size() ; x++) {    //use a regular for loop so we know which element we are looking at

            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= tickets.get(x).getPriority()) {
                tickets.add(x, newTicket);
                return;
            }
        }

        //Will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other tickets. So, add to the end.
        tickets.addLast(newTicket);
    }

    protected static void printAllTickets(LinkedList<Ticket> tickets) {
        System.out.println(" ------- All open tickets ----------");

        for (Ticket t : tickets ) {
            System.out.println(t); //Write a toString method in Ticket class
            //println will try to call toString on its argument
        }
        System.out.println(" ------- End of ticket list ----------");

    }
    protected static void deleteTicket(LinkedList<Ticket> ticketQueue) {
        //printAllTickets(ticketQueue);   //display list for user

        Date dateResolved = new Date();

        if (ticketQueue.size() == 0) {    //no tickets!
            System.out.println("No tickets to delete!\n");
            return;
        }
        boolean found = false;
        while (!found) {
    try {
        Scanner deleteScanner = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter ID of ticket to delete");
        int deleteID = deleteScanner.nextInt();

        //Loop over all tickets. Delete the one with this ticket ID
        //adding getting user input to ask why the ticket was deleted, and adding the deleted tickets to a resloved tickets list
        for (Ticket ticket : ticketQueue) {
            if (ticket.getTicketID() == deleteID) {
                found = true;
                System.out.println(String.format("Ticket %d deleted", deleteID));
                System.out.println("Why was the ticket closed? ");
                String reason = scanner.nextLine();
                resolvedTickets = new ResolvedTicket(ticket, dateResolved, reason);
                ticketQueue.remove(ticket);
                break; //don't need loop any more.
            }
        }
        if (!found) {
            System.out.println("Ticket ID not found, please enter a correct ticket number");

        }
        printAllTickets(ticketQueue);  //print updated list
    }catch (Exception ioe){
        System.out.println("Please enter a valid ticket number");
    }

        }
    }
    //creating a ticket search method to be able to keyword search the tickets lists, it will give the results of the keyword tickets
    protected static void ticketSearch(LinkedList<Ticket> searchTickets) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a keyword to search");
        String userInput = scanner.nextLine();
        LinkedList userLinked = new LinkedList();
        for (Ticket ticket : searchTickets) {
            if (ticket.getDescription().contains(userInput)) {
                userLinked.add(ticket);
            }
        }
        System.out.println(" ------- All open tickets with keyword " + "\"" + userInput + "\"" + " ----------");
        for(Object i : userLinked){
            System.out.println(i);
        }
        System.out.println(" ------- End of ticket list with keyword " + "\"" + userInput + "\"" + " ----------");

        }
    }





