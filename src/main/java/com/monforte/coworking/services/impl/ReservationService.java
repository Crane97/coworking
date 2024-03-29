package com.monforte.coworking.services.impl;

import com.monforte.coworking.domain.dto.requests.AppointmentDTO;
import com.monforte.coworking.domain.dto.requests.ReservationRecursiveTO;
import com.monforte.coworking.domain.dto.requests.ReservationRequestTO;
import com.monforte.coworking.domain.dto.responses.MyReservationsTO;
import com.monforte.coworking.domain.entities.Invoice;
import com.monforte.coworking.domain.entities.Reservation;
import com.monforte.coworking.domain.entities.Room;
import com.monforte.coworking.domain.entities.enums.ReservationStatus;
import com.monforte.coworking.exceptions.InvoiceNotFoundException;
import com.monforte.coworking.exceptions.LocalDateErrorException;
import com.monforte.coworking.exceptions.OverlapErrorException;
import com.monforte.coworking.repositories.ReservationRepository;
import com.monforte.coworking.repositories.RoomRepository;
import com.monforte.coworking.services.IInvoiceService;
import com.monforte.coworking.services.IPaymentService;
import com.monforte.coworking.services.IReservationService;
import com.monforte.coworking.services.IRoomService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReservationService implements IReservationService {

    @Autowired
    public ReservationRepository reservationRepository;

    @Autowired
    public IRoomService roomService;

    @Autowired
    public IInvoiceService invoiceService;

    @Autowired
    public IPaymentService paymentService;

    @Autowired
    public RoomRepository roomRepository;

    public List<Reservation> getReservations(){ return reservationRepository.findAll(); }

    public List<MyReservationsTO> getReservationsAsTO(){
        List<Reservation> reservation = reservationRepository.findAllOrderByStart();
        List<MyReservationsTO> myReservationsTOS = new ArrayList<>();

        for(Reservation res : reservation){
            MyReservationsTO myReservationsTO = new MyReservationsTO();

            if(res.getId()!=null)myReservationsTO.setId(res.getId());
            if(res.getDescription()!=null)myReservationsTO.setDescription(res.getDescription());
            if(res.getStart()!=null)myReservationsTO.setDate(res.getStart().toLocalDate().toString());
            if(res.getStart()!=null)myReservationsTO.setStart(res.getStart().toLocalTime().toString());
            if(res.getEnd()!=null)myReservationsTO.setEnd(res.getEnd().toLocalTime().toString());
            if(res.getStatus()!=null)myReservationsTO.setStatus(res.getStatus().toString());
            if(res.getPlace()!=null)myReservationsTO.setPlace(res.getPlace());
            if(res.getUser()!=null)myReservationsTO.setUser(res.getUser());
            if(res.getRoom()!=null)myReservationsTO.setRoom(res.getRoom());

            myReservationsTOS.add(myReservationsTO);
        }

        return myReservationsTOS;
    }

    public Reservation getReservationById(Integer id) throws NoSuchElementException {

        Optional<Reservation> res = reservationRepository.findById(id);

        if(res.isPresent()) {
            return res.get();
        }
        else{ throw new NoSuchElementException("No reservation with id "+ id);}
    }

    public Reservation addReservation(Reservation reservation) {
        if(compareLocalDateTimesReservations(reservation, getReservationsByRoom(reservation.getRoom().getId()))){
            reservation.setStatus(ReservationStatus.SCHEDULED);
            return reservationRepository.save(reservation);
        }
        else{
            throw new OverlapErrorException("The reservation time is overlaping with another appointment.");
        }
    }

    public Reservation updateReservation(Reservation reservation) throws OverlapErrorException{

        if(compareLocalDateTimesReservations(reservation, getReservationsByRoom(reservation.getRoom().getId()))){
            return reservationRepository.save(reservation);
        }
        else{
            throw new OverlapErrorException("The reservation time is overlaping with another appointment.");
        }
    }

    @Transactional
    public void deleteReservation(Integer id) throws InvoiceNotFoundException, StripeException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if(reservation.isPresent()){
            if(!reservation.get().getStatus().toString().equals("VISIT")) {
                Invoice invoice = invoiceService.getInvoiceByReservationId(reservation.get().getId());

                if(invoice.getNumber()!=null) {
                    paymentService.refundReservation(invoice.getNumber());
                }
            }
        }

        reservationRepository.deleteById(id);
    }

    @Transactional
    public void deleteCanceledReservations(Integer id){
        Optional<List<Reservation>> deleteReservations = reservationRepository.findByInvoiceId(id);
        if(deleteReservations.isPresent()){
            for(Reservation res : deleteReservations.get()){
                reservationRepository.delete(res);
            }
        }
        invoiceService.deleteInvoiceById(id);
    }

    public List<Reservation> getReservationsByRoom(Integer roomid){
        Room room = roomRepository.getById(roomid);
        return reservationRepository.findByRoom(room);
    }

    public List<Reservation> getReservationsByRoomByDay(Integer roomid, LocalDate day){

        LocalDateTime dateTime = LocalDateTime.of(day, LocalTime.of(0,0,0));
        LocalDateTime dateTime1 = LocalDateTime.of(day,LocalTime.of(23,59,59));

        Optional<List<Reservation>> reservationsByDay = reservationRepository.findByRoomByDay(roomid, dateTime, dateTime1);

        if(reservationsByDay.isPresent()){
            return reservationsByDay.get();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public Reservation addNormalReservation(ReservationRequestTO reservationTO){

        Reservation reservation = new Reservation();

        LocalDate actual = LocalDate.now();

        if(reservationTO.getDate().isAfter(ChronoLocalDate.from(actual)) || reservationTO.getDate().isEqual(ChronoLocalDate.from(actual))) {

            String[] splitStart = reservationTO.getStart().split(":");
            String[] splitEnd = reservationTO.getEnd().split(":");


            LocalDateTime start = LocalDateTime.of(reservationTO.getDate().getYear(), reservationTO.getDate().getMonth(), reservationTO.getDate().getDayOfMonth(), Integer.parseInt(splitStart[0]), Integer.parseInt(splitStart[1]));
            LocalDateTime end = LocalDateTime.of(reservationTO.getDate().getYear(), reservationTO.getDate().getMonth(), reservationTO.getDate().getDayOfMonth(), Integer.parseInt(splitEnd[0]), Integer.parseInt(splitEnd[1]));

            if (reservationTO.getDescription() != null) reservation.setDescription(reservationTO.getDescription());
            if (reservationTO.getDate() != null) reservation.setDescription(reservationTO.getDescription());
            if (reservationTO.getStart() != null) reservation.setStart(start);
            if (reservationTO.getEnd() != null) reservation.setEnd(end);
            if (reservationTO.getStatus() != null) reservation.setStatus(ReservationStatus.SCHEDULED);
            if (reservationTO.getPlace() != null) reservation.setPlace(reservationTO.getPlace());
            if (reservationTO.getRoom() != null) reservation.setRoom(reservationTO.getRoom());
            if (reservationTO.getUser() != null) reservation.setUser(reservationTO.getUser());
            reservation.setStatus(ReservationStatus.SCHEDULED);

            List<Reservation> reservations = new ArrayList<>();

            reservations.add(reservation);

            if (compareLocalDateTimesReservations(reservation, getReservationsByRoom(reservation.getRoom().getId()))) {
                Invoice invoice = invoiceService.newInvoice(reservations);
                reservation.setInvoice(invoice);
            }

            return addReservation(reservation);
        }

        else throw new LocalDateErrorException("La fecha seleccionada es anterior a la actual");
    }

    @Transactional
    public List<Reservation> addRecursiveReservations(ReservationRecursiveTO reservationRecursiveTO) throws OverlapErrorException {

        List<Reservation> reservationList = new ArrayList<>();

        LocalDate actual = LocalDate.now();

        if(reservationRecursiveTO.getEntryDate().isAfter(ChronoLocalDate.from(actual)) || reservationRecursiveTO.getEntryDate().isEqual(ChronoLocalDate.from(actual))) {

            LocalDate entryDate = reservationRecursiveTO.getEntryDate();

            String[] splitStart = reservationRecursiveTO.getStart().split(":");
            String[] splitEnd = reservationRecursiveTO.getEnd().split(":");


            while (entryDate.isBefore(reservationRecursiveTO.getFinalDate().plusDays(1))) {

                if (reservationRecursiveTO.getMonday() && getDayNumberNew(entryDate) == 1) {
                    Reservation reservation = reservationRecursiveTOtoReservation(reservationRecursiveTO, entryDate, splitStart, splitEnd);
                    if (compareLocalDateTimesReservations(reservation, getReservationsByRoom(reservation.getRoom().getId()))) {
                        Reservation reservation1 = reservationRepository.save(reservation);
                        reservationList.add(reservation1);
                    }
                }
                if (reservationRecursiveTO.getTuesday() && getDayNumberNew(entryDate) == 2) {
                    Reservation reservation = reservationRecursiveTOtoReservation(reservationRecursiveTO, entryDate, splitStart, splitEnd);
                    if (compareLocalDateTimesReservations(reservation, getReservationsByRoom(reservation.getRoom().getId()))) {
                        Reservation reservation1 = reservationRepository.save(reservation);
                        reservationList.add(reservation1);
                    }
                }
                if (reservationRecursiveTO.getWednesday() && getDayNumberNew(entryDate) == 3) {
                    Reservation reservation = reservationRecursiveTOtoReservation(reservationRecursiveTO, entryDate, splitStart, splitEnd);
                    if (compareLocalDateTimesReservations(reservation, getReservationsByRoom(reservation.getRoom().getId()))) {
                        Reservation reservation1 = reservationRepository.save(reservation);
                        reservationList.add(reservation1);
                    }
                }
                if (reservationRecursiveTO.getThursday() && getDayNumberNew(entryDate) == 4) {
                    Reservation reservation = reservationRecursiveTOtoReservation(reservationRecursiveTO, entryDate, splitStart, splitEnd);
                    if (compareLocalDateTimesReservations(reservation, getReservationsByRoom(reservation.getRoom().getId()))) {
                        Reservation reservation1 = reservationRepository.save(reservation);
                        reservationList.add(reservation1);
                    }
                }
                if (reservationRecursiveTO.getFriday() && getDayNumberNew(entryDate) == 5) {
                    Reservation reservation = reservationRecursiveTOtoReservation(reservationRecursiveTO, entryDate, splitStart, splitEnd);
                    if (compareLocalDateTimesReservations(reservation, getReservationsByRoom(reservation.getRoom().getId()))) {
                        Reservation reservation1 = reservationRepository.save(reservation);
                        reservationList.add(reservation1);
                    }
                }

                entryDate = entryDate.plusDays(1);
            }

            Invoice invoice = invoiceService.newInvoice(reservationList);

            for (Reservation res : reservationList) {
                res.setInvoice(invoice);
            }

            return reservationList;
        }

        else throw  new LocalDateErrorException("La fecha de entrada es anterior a la fecha actual");
    }

    public Reservation reservationRecursiveTOtoReservation(ReservationRecursiveTO reservationRecursiveTO, LocalDate entryDate, String[] splitStart, String[] splitEnd){
        Reservation reservation = new Reservation();
        reservation.setDescription(reservationRecursiveTO.getDescription());

        LocalDateTime start = LocalDateTime.of(entryDate.getYear(), entryDate.getMonth(), entryDate.getDayOfMonth(), Integer.parseInt(splitStart[0]), Integer.parseInt(splitStart[1]));
        LocalDateTime end = LocalDateTime.of(entryDate.getYear(), entryDate.getMonth(), entryDate.getDayOfMonth(), Integer.parseInt(splitEnd[0]), Integer.parseInt(splitEnd[1]));

        reservation.setStart(start);
        reservation.setEnd(end);
        reservation.setUser(reservationRecursiveTO.getUser());
        reservation.setStatus(ReservationStatus.SCHEDULED);
        reservation.setRoom(reservationRecursiveTO.getRoom());
        reservation.setPlace(reservationRecursiveTO.getPlace());
        //reservation.setStatus(reservationRecursiveTO.setStatus());

        return reservation;
    }

    @Transactional
    public List<Reservation> addDaysReservation(ReservationRecursiveTO reservationRecursiveTO) throws OverlapErrorException{

        List<Reservation> reservationList = new ArrayList<>();

        LocalDate actual = LocalDate.now();

        if(reservationRecursiveTO.getEntryDate().isAfter(ChronoLocalDate.from(actual)) || reservationRecursiveTO.getEntryDate().isEqual(ChronoLocalDate.from(actual))) {

            LocalDate entryDate = reservationRecursiveTO.getEntryDate();

            String[] splitStart = reservationRecursiveTO.getStart().split(":");
            String[] splitEnd = reservationRecursiveTO.getEnd().split(":");

            while (entryDate.isBefore(reservationRecursiveTO.getFinalDate().plusDays(1))) {

                if (getDayNumberNew(entryDate) != 6 && getDayNumberNew(entryDate) != 7) {
                    Reservation reservation = new Reservation();
                    reservation.setDescription(reservationRecursiveTO.getDescription());

                    LocalDateTime start = LocalDateTime.of(entryDate.getYear(), entryDate.getMonth(), entryDate.getDayOfMonth(), Integer.parseInt(splitStart[0]), Integer.parseInt(splitStart[1]));
                    LocalDateTime end = LocalDateTime.of(entryDate.getYear(), entryDate.getMonth(), entryDate.getDayOfMonth(), Integer.parseInt(splitEnd[0]), Integer.parseInt(splitEnd[1]));

                    reservation.setStart(start);
                    reservation.setEnd(end);
                    reservation.setUser(reservationRecursiveTO.getUser());
                    reservation.setStatus(ReservationStatus.SCHEDULED);
                    reservation.setRoom(reservationRecursiveTO.getRoom());
                    reservation.setPlace(reservationRecursiveTO.getPlace());

                    if (compareLocalDateTimesReservations(reservation, getReservationsByRoom(reservation.getRoom().getId()))) {
                        Reservation reservation1 = reservationRepository.save(reservation);
                        reservationList.add(reservation1);
                    }
                }

                entryDate = entryDate.plusDays(1);
            }

            Invoice invoice = invoiceService.newInvoice(reservationList);

            for (Reservation res : reservationList) {
                res.setInvoice(invoice);
            }

            return reservationList;
        }

        else throw new LocalDateErrorException("La fecha de entrada es anterior a la actual");
    }

    public Reservation addAppointment(AppointmentDTO appointmentDTO){

        LocalDate actual = LocalDate.now();

        LocalDateTime actual1 = LocalDateTime.now();

        if(appointmentDTO.getDate().isAfter(ChronoLocalDate.from(actual)) || appointmentDTO.getDate().isEqual(ChronoLocalDate.from(actual))) {

            Reservation result = new Reservation();

            result.setStart(LocalDateTime.of(appointmentDTO.getDate(), LocalTime.of(0, 0)));
            result.setEnd(LocalDateTime.of(appointmentDTO.getDate(), LocalTime.of(0, 0)));
            result.setUser(appointmentDTO.getUser());
            result.setStatus(ReservationStatus.VISIT);
            result.setDescription("Visita presencial de " + appointmentDTO.getUser().getName() + " " + appointmentDTO.getUser().getSurname());


            return reservationRepository.save(result);
        }

        else throw new LocalDateErrorException("La fecha seleccionada es anterior a la actual");
    }

    public List<LocalTime> getAvailableTimeByRoomByDay(Integer roomid, LocalDate day){

        List<Reservation> reservationsByDay = getReservationsByRoomByDay(roomid, day);
        Room room1 = roomService.getRoomById(roomid);
        List<LocalTime> availableTimeTO = new ArrayList<>();
        LocalTime localTime = LocalTime.of(8,0,0);

        int cont = 0;

        while(localTime.isBefore(LocalTime.of(20,0,1))){
            if(room1.getRoomType().toString().equals("REUNION")){
                cont = 1;
            }
            else{
                cont = room1.getCapacity();
            }
            Boolean flag = true;
            for(Reservation reservation : reservationsByDay){
                if((localTime.isAfter(reservation.getStart().toLocalTime())
                        && localTime.isBefore(reservation.getEnd().toLocalTime()))
                        || localTime.equals(reservation.getStart().toLocalTime())){
                    cont--;
                    if(cont <=0) {
                        flag = false;
                    }
                }
            }
            if(flag){
                availableTimeTO.add(localTime);
            }
            localTime = localTime.plusMinutes(30);
        }

        return availableTimeTO;
    }

    public List<MyReservationsTO> getReservationsByUser(Integer id){
        List<MyReservationsTO> myReservationsTOS = new ArrayList<>();

        List<Reservation> reservations = reservationRepository.findByUserIdOrderByStart(id);

        for(Reservation res : reservations){
            MyReservationsTO myReservationsTO = new MyReservationsTO();

            if(res.getId()!=null)myReservationsTO.setId(res.getId());
            if(res.getDescription()!=null)myReservationsTO.setDescription(res.getDescription());
            if(res.getStart()!=null)myReservationsTO.setDate(res.getStart().toLocalDate().toString());
            if(res.getStart()!=null)myReservationsTO.setStart(res.getStart().toLocalTime().toString());
            if(res.getEnd()!=null)myReservationsTO.setEnd(res.getEnd().toLocalTime().toString());
            if(res.getStatus()!=null)myReservationsTO.setStatus(res.getStatus().toString());
            if(res.getPlace()!=null)myReservationsTO.setPlace(res.getPlace());
            if(res.getUser()!=null)myReservationsTO.setUser(res.getUser());
            if(res.getRoom()!=null)myReservationsTO.setRoom(res.getRoom());

            myReservationsTOS.add(myReservationsTO);
        }

        return myReservationsTOS;
    }

    public boolean compareLocalDateTimesReservations(Reservation newReservation, List<Reservation> reservationList){

        boolean overlap = true;
        int cont = 0;

        if(newReservation.getRoom().getRoomType().toString().equals("REUNION")){
            cont = 1;
        }
        else{
            cont = newReservation.getRoom().getCapacity();
        }

        for(Reservation aux : reservationList){
            if(((newReservation.getStart().isAfter(aux.getStart()) && newReservation.getStart().isBefore(aux.getEnd())) || newReservation.getStart().isEqual(aux.getStart()))
            || ((newReservation.getEnd().isBefore(aux.getEnd()) && newReservation.getEnd().isAfter(aux.getStart())) || newReservation.getEnd().isEqual(aux.getEnd()))
            || (newReservation.getStart().isBefore(aux.getStart()) && newReservation.getEnd().isAfter(aux.getEnd()))){
                cont--;
                if(cont<=0) {
                    overlap = false;
                }
            }
        }

        return overlap;
    }

    //Devuelve un valor del 1 (Lunes) al 7 (Domingo)
    public int getDayNumberNew(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day.getValue();
    }
}
