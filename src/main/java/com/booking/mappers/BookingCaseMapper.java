package com.booking.mappers;

import com.booking.model.BookingCase;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookingCaseMapper {

//    @Select("SELECT * FROM booking_cases")
//    List<BookingCase> getAllCases();

    @Select("SELECT id, destination, check_in AS checkIn, check_out AS checkOut, guests FROM booking_cases")
    List<BookingCase> getCasesData();

    @Insert("""
        INSERT INTO booking_cases (id, destination, check_in, check_out, guests)
        VALUES (#{id}, #{destination}, #{checkIn}, #{checkOut}, #{guests})
    """)
    void insertCase(BookingCase bookingCase);
}
