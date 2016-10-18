package com.jeremy;

import java.util.Date;


public class ResolvedTicket extends Ticket{

    //creating private variables
    private Date resDate;
    private String resolution;
    private Ticket resTicket;

    //creating a resolved ticket constructor
    public ResolvedTicket(Ticket resTicket, Date resDate, String resolution) {
        this.resDate = resDate;
        this.resolution = resolution;
        this.resTicket = resTicket;
    }

    //a to string method to add the extra resolution date and why the ticket was closed
    public String toString(){
        return("ID = " + resTicket.getTicketID() + " Issued: " + resTicket.getDescription() + " Priority: " + resTicket.getPriority() + " Reported by: "
                + resTicket.getReporter() + " Reported on: " + resTicket.getDateReported() + " Resolution date: " + this.resDate +
                " Resolution why ticket was closed: " + this.resolution);
    }


    public String getResolution(){
        return resolution;
    }



}
