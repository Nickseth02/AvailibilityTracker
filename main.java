import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LibrarySystem library = new LibrarySystem();
        LabSystem lab = new LabSystem();

        while (true) {
            System.out.println("\n1. Student\n2. Admin\n3. Exit");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("1. View Library Seats\n2. View Lab PCs");
                    int sChoice = sc.nextInt();
                    if (sChoice == 1) library.viewSeats();
                    else lab.viewPCs();
                    break;

                case 2:
                    System.out.println("1. Update Library Seat\n2. Update Lab PC");
                    int aChoice = sc.nextInt();
                    if (aChoice == 1) library.updateSeat();
                    else lab.updatePC();
                    break;

                case 3:
                    System.exit(0);
            }
        }
    }
}
