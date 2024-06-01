package com.bengkel.booking.services;

import java.security.Provider;
import java.util.List;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Car;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.Vehicle;

public class PrintService {
	
	public static void printMenu(String[] listMenu, String title) {
		String line = "+---------------------------------+";
		int number = 1;
		String formatTable = " %-2s. %-25s %n";
		
		System.out.printf("%-25s %n", title);
		System.out.println(line);
		
		for (String data : listMenu) {
			if (number < listMenu.length) {
				System.out.printf(formatTable, number, data);
			}else {
				System.out.printf(formatTable, 0, data);
			}
			number++;
		}
		System.out.println(line);
		System.out.println();
	}
	
	public static void printVechicle(List<Vehicle> listVehicle) {
		String formatTable = "| %-2s | %-15s | %-10s | %-15s | %-15s |%n";
		String line = "+----+-----------------+------------+-----------------+-----------------+-------+-----------------+%n";
		System.out.format(line);
	    System.out.format(formatTable, "No", "Vechicle Id", "Warna",  "Tipe Kendaraan", "Tahun");
	    System.out.format(line);
	    int number = 1;
	    String vehicleType = "";
	    for (Vehicle vehicle : listVehicle) {
	    	if (vehicle instanceof Car) {
				vehicleType = "Mobil";
			}else {
				vehicleType = "Motor";
			}
	    	System.out.format(formatTable, number, vehicle.getVehiclesId(), vehicle.getColor(), vehicleType, vehicle.getYearRelease());
	    	number++;
	    }
	    System.out.printf(line);
	}

	public static void printItemService(List<ItemService> listItemService) {
		String formatTable = "| %-2s | %-15s | %-15s | %-15s | %-15s |%n";
		String line = "+----+-----------------+------------+-----------------+-----------------+-------+-----------------+%n";
		System.out.format(line);
		System.out.format(formatTable, "No", "Service Id", "Nama Service",  "Tipe Kendaraan", "Harga");
		System.out.format(line);
		int number = 1;
		for (ItemService itemService : listItemService) {
			System.out.format(formatTable, number, itemService.getServiceId(), itemService.getServiceName(), itemService.getVehicleType(), itemService.getPrice());
			number++;
		}
		System.out.printf(line);
	}

	public static void printBookingOrder(List<BookingOrder> listBookingOrder) {
		String formatTable = "| %-2s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s |%n";
		String line = "+----+---------------------+---------------------+---------------------+---------------------+---------------------+---------------------+---------------------+%n";
		System.out.format(line);
		System.out.format(formatTable, "No", "Booking Id", "Nama Customer",  "Paymet Method", "Total Service", "Total Payment", "List Service");
		System.out.format(line);
		int number = 1;
		for (BookingOrder bookingOrder : listBookingOrder) {
			System.out.format(formatTable, number, bookingOrder.getBookingId(), bookingOrder.getCustomer().getName(), bookingOrder.getPaymentMethod(), bookingOrder.getTotalServicePrice(), bookingOrder.getTotalPayment(), printServices(bookingOrder.getServices()));
			number++;
		}
		System.out.printf(line);
	}

	public static String printServices(List<ItemService> itemServiceList){
		String result = "";
		for (ItemService itemService : itemServiceList) {
			result += itemService.getServiceName() + ", ";
		}
		return result;
	}
	
	//Silahkan Tambahkan function print sesuai dengan kebutuhan.
	
}
