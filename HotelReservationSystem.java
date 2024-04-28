package Hotel_Reservation_System;

import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Scanner;


import in.JdbcUtil.JDBCutil;

public class HotelReservationSystem {
   // choice 1
	public static void reserveRoom(Connection cn,Scanner sc)  {
		System.out.println("Enter Guest Name: ");
		String guestName = sc.next();
		sc.nextLine();
		System.out.println("Enter Room Number: ");
		int roomNumber = sc.nextInt();
		System.out.println("Enter Contact Number:");
		String contactNumber = sc.next();
		
	    try {
	    	if(cn !=null) {
	    	Statement st =  cn.createStatement();
	    	String Mysql = "insert into reservation(guest_name,room_number,contact_number)"+ 
	    	"VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";;
	    	int Rowaffected = st.executeUpdate(Mysql);
	    	if(Rowaffected==1) {
	    		System.out.println("Reservation Successful");
	    	}else {
	    		System.out.println("Reservation Not Successful");
	    	}
	    	}
	    }catch(SQLException e) {
	    	e.printStackTrace();
	    }
		
	}
	
	// choice 2
	public static void viewReservation(Connection cn) {
		try {
			String Mysql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservation";
			Statement st = cn.createStatement();
			ResultSet result = st.executeQuery(Mysql);
			
			System.out.println("Current Reservations:");
//			System.out.println("+----------------+-----------------+-------------+----------------+------------------+");
//			System.out.println("| Reservation ID |  Guest Name     | Room Number | Contact Number | Reservation Date |");
//			System.out.println("+----------------+-----------------+-------------+----------------+------------------+");
			 System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
	         System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number       | Reservation Date        |");
	         System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
			
			while(result.next()) {
				int reservationId = result.getInt("reservation_id");
				String guestName = result.getString("guest_name");
				int roomNumber = result.getInt("room_number");
				String contactNumber = result.getString("contact_number");
				String reservationDate = result.getTimestamp("reservation_date").toString();
				
			    // Format and display the reservation data in a table-like format
				System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",reservationId,guestName,roomNumber,contactNumber,reservationDate);
				
				 System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
			}
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void getRoomNumber(Connection cn , Scanner sc) throws SQLException {
		try {
		System.out.println("Enter Reservation ID:");
		int ReservationID = sc.nextInt();
		System.out.println("Enter GuestName");
		String guestname = sc.next();
		
		String MySql = "SELECT room_number FROM reservation " +
                "WHERE reservation_id = " + ReservationID +
                " AND guest_name = '" + guestname + "'";

		Statement st = cn.createStatement();
		ResultSet result = st.executeQuery(MySql);
		
		if(result.next()) {
			int RoomNumber = result.getInt("room_number");
			 System.out.println("Room number for Reservation ID " + ReservationID +
                     " and Guest " + guestname + " is: " + RoomNumber);
		}
		else {
            System.out.println("Reservation not found for the given ID and guest name.");
        }
		}catch (SQLException e) {
            e.printStackTrace();
        }
		
	}
	
	
	
	public static void  updateReservation(Connection cn,Scanner sc) {
		try {
			 System.out.print("Enter reservation ID to update: ");
			 int ReservationID = sc.nextInt();
			 sc.nextLine();
			 
			 if (!ReservationExit(cn, ReservationID)) {
	                System.out.println("Reservation not found for the given ID.");
	                return;
	            }
			 
			 System.out.println("Enter new Guest_Name");
			 String newGuestName = sc.next();
			 System.out.println("Enter new Romm_number");
			 int newRoomNumber = sc.nextInt();
			 System.out.println("Enter new Contact Number");
			 String newContactNumber = sc.next();
			 
			 String sql = "UPDATE reservation SET guest_name = '" + newGuestName + "', " +
	                    "room_number = " + newRoomNumber + ", " +
	                    "contact_number = '" + newContactNumber + "' " +
	                    "WHERE reservation_id = " + ReservationID;
			 
			 Statement st = cn.createStatement();
			 int affectedRows = st.executeUpdate(sql);
			 
			  if (affectedRows ==1) {
                  System.out.println("Reservation updated successfully!");
              } else {
                  System.out.println("Reservation update failed.");
              }
			 
		}
		catch (SQLException e) {
            e.printStackTrace();
		} 
		
	}
	
	public static void  deleteReservation(Connection cn , Scanner sc) throws SQLException{
		
			try {
	            System.out.print("Enter reservation ID to delete: ");
	            int ReservationID = sc.nextInt();

	            if (!ReservationExit(cn, ReservationID)) {
	                System.out.println("Reservation not found for the given ID.");
	                return;
	            }
	            
	            String MySql = "Delete from reservation Where reservation_id = "+ReservationID;
	            
	            Statement st = cn.createStatement();
	            int affectedRows = st.executeUpdate(MySql);
	            if (affectedRows > 0) {
                    System.out.println("Reservation deleted successfully!");
                } else {
                    System.out.println("Reservation deletion failed.");
                }
		}
			catch (SQLException e) {
	            e.printStackTrace();
			}
	}
	
	public static boolean ReservationExit(Connection cn,int ReservationID) {
		try {
			  String MySql = "SELECT reservation_id FROM reservation WHERE reservation_id = " + ReservationID;
			  
			  Statement st = cn.createStatement();
			  ResultSet result = st.executeQuery(MySql);
			  
			  return result.next();
		}
		catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	  
	  
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Connection cn = null;
		//Statement st = null;
		ResultSet result = null;
		Scanner sc = new Scanner(System.in);
		try {
			cn = JDBCutil.getJdbcConnection();
			if(cn!=null) {
				while(true) {
					System.out.println();
					System.out.println("Hotel Reservationb System");
					System.out.println("1. Reserv a Room");
					System.out.println("2. View Reservation");
					System.out.println("3. Get Room Number");
					System.out.println("4. Update Reservation");
					System.out.println("5. Delete Reservation");
					System.out.println("0. Exit");
					System.out.println("Choose an Option");
					int choice = sc.nextInt();
					switch(choice) {
					case 1:
					reserveRoom(cn,sc);
					break;
					case 2:
						viewReservation(cn);
						break;
					case 3:
						getRoomNumber(cn,sc);
						break;
					case 4:
					    updateReservation(cn,sc);
					    break;
					case 5:
					    deleteReservation(cn,sc);
					    break;
					case 0:
					    exit();
					    sc.close();
					    return;
					default :
						System.out.println("Invalid choice, try again");
					}
				}
			}
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
//		catch(InterruptedException e) {
//			throw new RuntimeException(e);
//		}
	}

	private static void exit() {
		// TODO Auto-generated method stub
		
	}

}
