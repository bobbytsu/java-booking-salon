package com.bengkel.booking.services;

import com.bengkel.booking.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class BengkelService {
	
	//Silahkan tambahkan fitur-fitur utama aplikasi disini

    public static List<BookingOrder> listAllBookingOrder = new ArrayList<>();
    public static int lastBookingId = 0;
	
	//Login
    public static Customer login(AtomicBoolean isLooping, Scanner input, List<Customer> listAllCustomers) {
        String[] listMenuLogin = {"Login",  "Exit"};
        int menuChoice = 0;
        Customer loggedInCustomer = null;

        // login
        do {
            PrintService.printMenu(listMenuLogin, "Aplikasi Booking Bengkel");
            menuChoice = Validation.validasiNumberWithRange("Masukan Pilihan Menu: ", "Input Harus Berupa Angka!", "^[0-9]+$", listMenuLogin.length-1, 0);
            System.out.println(menuChoice);

            if(menuChoice == 1) {
                String customerId;
                do {
                    System.out.println("Masukkan Customer Id:");
                    customerId = input.nextLine();
                    loggedInCustomer = getCustomerById(customerId, listAllCustomers);
                    if(loggedInCustomer == null) {
                        System.out.println("Customer Id Tidak Ditemukan atau Salah!");
                    }
                } while(loggedInCustomer == null);

                String password;
                int attempt = 0;
                do {
                    System.out.println("Masukkan Password:");
                    password = input.nextLine();
                    if (!loggedInCustomer.getPassword().equals(password)) {
                        System.out.println("Password yang anda Masukan Salah!");
                        attempt++;
                    }
                    if(attempt == 3){
                        System.out.println("Exitting . . .");
                        isLooping.set(false);
                    }
                } while(!loggedInCustomer.getPassword().equals(password) && attempt <= 2);
                if(loggedInCustomer.getPassword().equals(password)){
                    MenuService.mainMenu(isLooping, loggedInCustomer);
                }
            } else {
                System.out.println("Exitting . . .");
                isLooping.set(false);
            }
        } while(isLooping.get());
        return loggedInCustomer;
    }

    private static Customer getCustomerById(String customerId, List<Customer> listAllCustomers) {
        for(Customer customer : listAllCustomers) {
            if(customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }
	
	//Info Customer
    public static void infoCustomer(Customer loggedInCustomer, List<MemberCustomer> lisAllMemberCustomers){

        boolean member = false;
        double saldoKoin = 0.0;
        for(MemberCustomer memberCustomer : lisAllMemberCustomers){
            if(memberCustomer.getCustomerId().equals(loggedInCustomer.getCustomerId())){
                member = true;
                saldoKoin = memberCustomer.getSaldoCoin();
                break;
            }
        }

        System.out.println("Customer Id: " + loggedInCustomer.getCustomerId());
        System.out.println("Nama: " + loggedInCustomer.getName());
        if(member){
            System.out.println("Customer Status: Member");
        } else {
            System.out.println("Customer Status: Non Member");
        }
        System.out.println("Alamat: " + loggedInCustomer.getAddress());
        if(member){
            System.out.println("Saldo Koin: " + saldoKoin);
        }
        System.out.println("List Kendaraan: ");
        PrintService.printVechicle(loggedInCustomer.getVehicles());
    }
	
	//Booking atau Reservation
    public static void bookingBengkel(Scanner input, Customer loggedInCustomer, List<MemberCustomer> lisAllMemberCustomers, List<ItemService> listAllItemService) {

        List<Vehicle> customerVehicles = loggedInCustomer.getVehicles();
        String vehicleId;
        boolean vehicleFound = false;
        do {
            System.out.println("Masukkan Vehicle Id: ");
            vehicleId = input.nextLine();
            for (Vehicle vehicle : customerVehicles) {
                if (vehicle.getVehiclesId().equals(vehicleId)) {
                    vehicleFound = true;
                    break;
                }
            }
            if (!vehicleFound) {
                System.out.println("Kendaraan Tidak ditemukan.");
            }
        } while (!vehicleFound);

        System.out.println("List Service Yang Tersedia: ");
        PrintService.printItemService(listAllItemService);
        System.out.println("0. Kembali Ke Home Menu");

        ItemService itemService;
        List<ItemService> selectedService = new ArrayList<>();
        String itemServiceId;
        do {
            System.out.println("Silahkan Masukkan Service Id: ");
            itemServiceId = input.nextLine();
            if (itemServiceId.equals("0")) {
                return;
            }
            itemService = getItemServiceById(itemServiceId, listAllItemService);
            if (itemService == null) {
                System.out.println("Service dengan ID " + itemServiceId + " tidak ditemukan.");
            } else {
                selectedService.add(itemService);
            }
        } while (itemService == null);

        MemberCustomer memberCustomer = null;
        boolean member = false;
        for (MemberCustomer mc : lisAllMemberCustomers) {
            if (mc.getCustomerId().equals(loggedInCustomer.getCustomerId())) {
                member = true;
                memberCustomer = mc;
                break;
            }
        }
        if (member) {
            String choice = "";
            int count = 1;
            do {
                System.out.println("Apakah anda ingin menambahkan Service Lainnya? (Y/T)");
                choice = input.nextLine();
                if (choice.equalsIgnoreCase("Y")) {
                    System.out.println("Silahkan Masukkan Service Id: ");
                    itemServiceId = input.nextLine();
                    itemService = getItemServiceById(itemServiceId, listAllItemService);
                    if (itemService == null) {
                        System.out.println("Service dengan ID " + itemServiceId + " tidak ditemukan.");
                    } else {
                        selectedService.add(itemService);
                        count++;
                    }
                } else {
                    break;
                }
            } while (count <= 1);
        }

        double totalHarga = 0.0;
        double totalPembayaran;
        for (ItemService is : selectedService) {
            totalHarga += is.getPrice();
        }
        totalPembayaran = totalHarga;
        double totalHargaDiskon = totalHarga - (totalHarga * 0.1);
        String metodePembayaran = "";
        if (member) {
            System.out.println("Silahkan Pilih Metode Pembayaran (Saldo Coin atau Cash)");
            metodePembayaran = input.nextLine();
            if (metodePembayaran.equalsIgnoreCase("Saldo Coin") && memberCustomer.getSaldoCoin() > totalHargaDiskon) {
                totalPembayaran = totalHargaDiskon;
                memberCustomer.setSaldoCoin(memberCustomer.getSaldoCoin() - totalHargaDiskon);
                System.out.println("Pembayaran Saldo Coin");
                System.out.println("Total Harga Service: " + totalHarga);
                System.out.println("Total Pembayaran: " + totalPembayaran);
            } else {
                metodePembayaran = "Cash";
                System.out.println("Saldo Coin Tidak Cukup!");
                System.out.println("Pembayaran Cash");
                System.out.println("Total Harga Service: " + totalHarga);
                System.out.println("Total Pembayaran: " + totalPembayaran);
            }
        } else {
            metodePembayaran = "Cash";
            System.out.println("Pembayaran Cash");
            System.out.println("Total Harga Service: " + totalHarga);
            System.out.println("Total Pembayaran: " + totalPembayaran);
        }

        lastBookingId++;
        String bookingId = String.format("Book-" + loggedInCustomer.getCustomerId() + "-%03d", lastBookingId);
        BookingOrder bookingOrder = new BookingOrder(bookingId, loggedInCustomer, selectedService, metodePembayaran, totalHarga, totalPembayaran);
        listAllBookingOrder.add(bookingOrder);
    }

    public static ItemService getItemServiceById(String itemServiceId, List<ItemService> listAllItemService){
        for(ItemService itemService : listAllItemService) {
            if(itemService.getServiceId().equals(itemServiceId)) {
                return itemService;
            }
        }
        return null;
    }
	
	//Top Up Saldo Coin Untuk Member Customer
    public static void topUp(Scanner input, Customer loggedInCustomer, List<MemberCustomer> lisAllMemberCustomers){

        MemberCustomer memberCustomer = null;
        boolean member = false;
        for(MemberCustomer mc : lisAllMemberCustomers){
            if(mc.getCustomerId().equals(loggedInCustomer.getCustomerId())){
                member = true;
                memberCustomer = mc;
                break;
            }
        }
        if(!member){
            System.out.println("Maaf fitur ini hanya untuk Member saja!");
        } else {
            System.out.println("Masukkan Besaran Top Up: ");
            double jumlahTopUp = input.nextDouble();
            memberCustomer.setSaldoCoin(memberCustomer.getSaldoCoin() + jumlahTopUp);
        }
    }

    //Booking Order
    public static void bookingOrder(Scanner input){

        System.out.println("Booking Order Menu");
        PrintService.printBookingOrder(listAllBookingOrder);
        System.out.println("0. Kembali Ke Home Menu");
        String choice = input.nextLine();
        if(choice.equals("0")){
            return;
        }
    }

	//Logout
	
}
