package com.booking.service;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import com.booking.models.Reservation;
import com.booking.models.Customer;
import com.booking.models.Employee;
import com.booking.models.Service;


public class ReservationService {

    public static int lastReservationId = 0;
    public static void createReservation(Scanner input, List<Customer> customerList, List<Employee> employeeList, List<Service> serviceList, List<Reservation> reservationList) {

        PrintService.showAllCustomer(customerList);
        Customer customer;
        String customerId;
        do {
            System.out.println("\nSilahkan Masukkan Customer Id:");
            customerId = input.nextLine();
            customer = getCustomerByCustomerId(customerId, customerList);
            if (customer == null) {
                System.out.println("Pelanggan dengan ID " + customerId + " tidak ditemukan.");
            }
        } while(customer == null);

        PrintService.showAllEmployee(employeeList);
        Employee employee;
        String employeeId;
        do {
            System.out.println("\nSilahkan Masukkan Employee Id");
            employeeId = input.nextLine();
            employee = getEmployeeByEmployeeId(employeeId, employeeList);
            if(employee == null) {
                System.out.println("Karyawan dengan ID " + employeeId + " tidak ditemukan.");
            }
        } while(employee == null);

        PrintService.showAllService(serviceList);
        Service service;
        List<Service> selectedServices = new ArrayList<>();
        String choice = "";
        do {
            System.out.println("\nSilahkan Masukkan Service Id");
            String serviceId = input.nextLine();
            service = getServiceById(serviceId, serviceList);
            if(service == null) {
                System.out.println("Layanan dengan ID " + serviceId + " tidak ditemukan.");
            } else {
                if(service != null && !selectedServices.contains(service)) {
                    selectedServices.add(service);
                    if(selectedServices.size() == serviceList.size()) {
                        break;
                    }
                    System.out.println("Ingin pilih service yang lain (Y/T)?");
                    choice = input.nextLine();
                } else {
                    System.out.println("Layanan sudah dipilih.");
                }
            }
        } while(service == null || choice.equalsIgnoreCase("Y"));

        double totalBiaya = 0;
        for(Service s : selectedServices) {
            totalBiaya += s.getPrice();
        }
        double diskon = 0;
        if(customer.getMember().getMembershipName().equalsIgnoreCase("Silver")){
            diskon = totalBiaya * 0.05;
        } else if(customer.getMember().getMembershipName().equalsIgnoreCase("Gold")){
            diskon = totalBiaya * 0.10;
        }
        totalBiaya -= diskon;

        if(totalBiaya > customer.getWallet()){
            System.out.println("Booking Gagal!");
            System.out.println("Uang Tidak Cukup");
        } else {
            lastReservationId++;
            String reservationId = String.format("Rsv-%02d", lastReservationId);

            Reservation reservation = new Reservation(reservationId, getCustomerByCustomerId(customerId, customerList), getEmployeeByEmployeeId(employeeId, employeeList), selectedServices, "In Process");
            reservationList.add(reservation);

            System.out.println("Booking Berhasil!");
            System.out.println("Total Biaya Booking: Rp. " + totalBiaya);
        }
    }

    public static Customer getCustomerByCustomerId(String customerId, List<Customer> customerList){
        for(Customer customer : customerList) {
            if(customer.getId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    public static Employee getEmployeeByEmployeeId(String employeeId, List<Employee> employeeList){
        for(Employee employee : employeeList) {
            if(employee.getId().equals(employeeId)) {
                return employee;
            }
        }
        return null;
    }

    public static Service getServiceById(String serviceId, List<Service> serviceList){
        for(Service service : serviceList) {
            if(service.getServiceId().equals(serviceId)) {
                return service;
            }
        }
        return null;
    }

    public static void editReservationWorkstage(Scanner input, List<Reservation> reservationList) {

        PrintService.showRecentReservation(reservationList);
        Reservation reservation;
        String reservationId;
        do {
            System.out.println("\nSilahkan Masukkan Reservation Id");
            reservationId = input.nextLine();
            reservation = getReservationById(reservationId, reservationList);
            if(reservation == null) {
                System.out.println("Layanan dengan ID " + reservationId + " tidak ditemukan.");
            } else if(!reservation.getWorkstage().equalsIgnoreCase("In Process")){
                System.out.println("Layanan dengan ID " + reservationId + " sudah selesai.");
            }
        } while(reservation == null || !reservation.getWorkstage().equalsIgnoreCase("In Process"));

        System.out.println("\nSelesaikan Reservasi");
        String workStage = input.nextLine();

        reservation.setWorkstage(workStage);

        Customer customer = reservation.getCustomer();
        if(workStage.equalsIgnoreCase("Finish")){
            customer.setWallet(customer.getWallet() - reservation.getReservationPrice());
        }

        System.out.println("\nReservasi dengan ID " + reservationId + " sudah " + workStage);
    }

    public static Reservation getReservationById(String reservationId, List<Reservation> reservationList){
        for(Reservation reservation : reservationList) {
            if(reservation.getReservationId().equals(reservationId)) {
                return reservation;
            }
        }
        return null;
    }

    // Silahkan tambahkan function lain, dan ubah function diatas sesuai kebutuhan
}
