package com.bengkel.booking.services;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.repositories.CustomerRepository;
import com.bengkel.booking.repositories.ItemServiceRepository;

public class MenuService {
	private static List<Customer> listAllCustomers = CustomerRepository.getAllCustomer();
	private static List<MemberCustomer> lisAllMemberCustomers = CustomerRepository.getAllMemberCustomer();
	private static List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
	private static Scanner input = new Scanner(System.in);
	public static void run() {
		AtomicBoolean isLooping = new AtomicBoolean(true);
		do {
			Customer loggedInCustomer = BengkelService.login(isLooping, input, listAllCustomers);
			if(isLooping.get()){
				mainMenu(isLooping, loggedInCustomer);
			}
		} while(isLooping.get());
		
	}

	public static void mainMenu(AtomicBoolean isLooping, Customer loggedInCustomer) {
		String[] listMenu = {"Informasi Customer", "Booking Bengkel", "Top Up Bengkel Coin", "Informasi Booking", "Logout"};
		int menuChoice = 0;
		
		do {
			PrintService.printMenu(listMenu, "Booking Bengkel Menu");
			menuChoice = Validation.validasiNumberWithRange("Masukan Pilihan Menu: ", "Input Harus Berupa Angka!", "^[0-9]+$", listMenu.length-1, 0);
			System.out.println(menuChoice);
			
			switch (menuChoice) {
			case 1:
				//panggil fitur Informasi Customer
				BengkelService.infoCustomer(loggedInCustomer, lisAllMemberCustomers);
				break;
			case 2:
				//panggil fitur Booking Bengkel
				BengkelService.bookingBengkel(input, loggedInCustomer, lisAllMemberCustomers, listAllItemService);
				break;
			case 3:
				//panggil fitur Top Up Saldo Coin
				BengkelService.topUp(input, loggedInCustomer, lisAllMemberCustomers);
				break;
			case 4:
				//panggil fitur Informasi Booking Order
				BengkelService.bookingOrder(input);
				break;
			default:
				System.out.println("Logout");
				isLooping.set(false);
				break;
			}
		} while(isLooping.get());
		
		
	}
	
	//Silahkan tambahkan kodingan untuk keperluan Menu Aplikasi
}
