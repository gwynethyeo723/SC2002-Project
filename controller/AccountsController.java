package controller;

import java.util.*;
import entity.CareerCenterStaff;
import enumeration.CareerCenterStaffStatus;


public class AccountsController {
    
    // Approve or reject a CompanyRep account
    public void reviewCompanyRep(CareerCenterStaff staff, boolean approve) {
        if (!staff.isLoggedIn()) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }
        if(approve) {
            staff.setStatus(CareerCenterStaffStatus.APPROVED);
            System.out.println("CompanyRep " + staff.getName() + " approved.");
        } else {
            staff.setStatus(CareerCenterStaffStatus.REJECTED);
            System.out.println("CompanyRep " + staff.getName() + " rejected.");
        }
    }
}
