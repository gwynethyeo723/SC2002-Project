package controller;

import entity.CareerCenterStaff;
import entity.CompanyRep;
import enumeration.CompanyRepStatus;

/**
 * Controller class responsible for handling account-related operations
 * for {@link CompanyRep} users. This includes approval and rejection
 * of company representative accounts by authorized {@link CareerCenterStaff}.
 *
 * <p>The methods in this controller enforce access control by ensuring
 * that only logged-in staff members may perform account moderation tasks.
 */


public class AccountsController {

    /**
     * Reviews a {@link CompanyRep} account and updates its status to either
     * {@link enumeration.CompanyRepStatus#APPROVED} or
     * {@link enumeration.CompanyRepStatus#REJECTED}, depending on the selection.
     *
     * <p>This action may only be performed by a logged-in
     * {@link CareerCenterStaff}. If the staff member is not logged in,
     * the operation is aborted and no changes are made.
     *
     * @param rep    the company representative whose account is being reviewed
     * @param staff  the logged-in staff member performing the review
     * @param approve {@code true} to approve the account, {@code false} to reject it
     */
    
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
