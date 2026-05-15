package models;
import java.time.LocalDate;
public class Complaint {
    private static int counter = 2;
    private int complaintID;
    private String StudentEmail;
    private String description;
    private String status;
    private LocalDate dateSubmitted;
    private String resolutionDetails;


    public Complaint (String StudentEmail, String Email) {
        this.complaintID = counter++;
        this.StudentEmail = StudentEmail;
        this.description = description;
        this.status = "pending";
        this.dateSubmitted = LocalDate.now();
        this.resolutionDetails = "";
    }
        public int getComplaintID() {
            return complaintID;
        }
        public String getStudentEmail () {
        return StudentEmail;
        }
        public String getDescription () {
        return description;
        }
        public String getStatus () {
        return status;
        }
        public LocalDate getDateSubmitted () {
        return dateSubmitted;
        }
        public String getResolutionDetails () {
        return resolutionDetails;
        }
        public void setStatus (String status) {
        this.status = status;
        }
        public void setResolutionDetails (String details) {
        this.resolutionDetails = details;
        }
        public void displayComplaint () {
        System.out.println(" Complain ID  : " + complaintID);
        System.out.println("  Student  : " + StudentEmail);
        System.out.println("  Date    : " + dateSubmitted);
        System.out.println("  Description  : " + description);
        System.out.println("  Status  : " + status);
        if ( !resolutionDetails.isEmpty()) {
            System.out.println("  Resolution  : " + resolutionDetails);
        }
    }
}
