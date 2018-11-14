package oops.hotel_list_page;

import oops.project.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalTime;

// Class that implements all the login in the frame
public class HotelStayDetailsLogic {
  User user = new User();
  String datePattern = "";
  public void setUserDetails(String checkInDate, String checkOutDate, int duration, int noPpl) {
    try {
      FileReader fr = new FileReader(HotelStayDetails.FILE);
      CSVReader csr = new CSVReader(fr);
      String[] recordReader;
      FileWriter fw = new FileWriter(HotelStayDetails.FILE);
      CSVWriter csw = new CSVWriter(fw);
      String[] recordWriter;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // check whether the data input from the user is valid
  protected boolean dateOrder(String checkIn, String checkOut) {
    Date date1 = null;
    Date date2 = null;
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      date1 = sdf.parse(checkIn);
      date2 = sdf.parse(checkOut);
      if (date1.before(date2)) {
        return false;
      }
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return true;
  }

  protected boolean legitDate(String checkIn) {
    Date date1 = null;
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      date1 = sdf.parse(checkIn);
      Date dCurrentDate = sdf.parse(sdf.format(new Date()));
      if (dCurrentDate.after(date1)) {
        return true;
      }
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return false;
  }

  public static boolean isValidDate(String format, String value) {
  Date date = null;
  try {
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      date = sdf.parse(value);
      if (!value.equals(sdf.format(date))) {
        return false;
      }
  } catch (ParseException ex) {
      ex.printStackTrace();
  }
  return true;
}

  // Add the data to the csv
  // We first read all the data from the csv
  // Then remove the row of the user who has inputted the data
  // Then add the row of the user with the extra data inputted by the user
  // Then add all the contents into the csv again
  protected int addData(String location, String checkIn, String checkOut, String room, String ppl) {
    user.setCheckInDate(checkIn);
    user.setCheckOutDate(checkOut);
    user.setRoom(room);
    user.setPpl(ppl);
    user.setLocation(location);
    int locationOfUserInFile = 0;
    try {
      FileReader fr = new FileReader(HotelStayDetails.FILE);
      CSVReader csr = new CSVReader(fr);
      String[] recordReaderEach;
      List<String[]> recordReaderWhole = new ArrayList<String[]>();
      int counter = 0;
      int sizeOfRow;
      while ((recordReaderEach = csr.readNext()) != null) {
        recordReaderWhole.add(recordReaderEach);
        if (recordReaderEach[0].equals(user.getUserName())) {
          locationOfUserInFile = counter;
        }
        counter++;
      }
      recordReaderWhole.remove(locationOfUserInFile);
      recordReaderWhole.add(new String[] {
        user.getUserName(),
        user.getName(),
        user.getBirthday(),
        user.getPassword(),
        user.getAddress(),
        checkIn,
        checkOut,
        room,
        ppl,
        location,
      });
      FileWriter fw = new FileWriter(HotelStayDetails.FILE, false);
      CSVWriter csw = new CSVWriter(fw);
      csw.writeAll(recordReaderWhole);
      fr.close();
      csr.close();
      fw.close();
      csw.close();
    } catch (IOException ee) {
      ee.printStackTrace();
    }
    return 1;
  }
}
