package controller;

import java.util.*;
import entity.CareerCenterStaff;
import entity.CompanyRep;
import entity.User;
import enumeration.CareerCenterStaffStatus;
import enumeration.CompanyRepStatus;


public class AccountsController {
    
    // Approve or reject a CompanyRep account
    public static void reviewCompanyRep(CompanyRep rep,CareerCenterStaff staff, boolean approve) {
        if (!staff.isLoggedIn()) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }

        if (approve) {
            rep.setStatus(CompanyRepStatus.APPROVED); // assuming you have a CompanyRepStatus enum
            System.out.println("CompanyRep " + rep.getName() + " approved.");
        } else {
            rep.setStatus(CompanyRepStatus.REJECTED);
            System.out.println("CompanyRep " + rep.getName() + " rejected.");
        }
    }
}
