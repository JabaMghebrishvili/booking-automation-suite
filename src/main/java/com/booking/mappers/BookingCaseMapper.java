package com.booking.mappers;

import com.booking.model.BookingCase;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookingCaseMapper {

    @Select("""
            SELECT id, destination, check_in AS checkIn, check_out AS checkOut, guests
            FROM [DB_Booking].[dbo].[booking_cases]
    """)
    List<BookingCase> getCasesData();

    @Select("""
        SELECT id, destination, check_in AS checkIn, check_out AS checkOut, guests
        FROM [DB_Booking].[dbo].[booking_cases]
        WHERE id = #{id}
    """)
    BookingCase getCaseById(@Param("id") int id);

    @Insert("""
        INSERT INTO [DB_Booking].[dbo].[booking_cases] (id, destination, check_in, check_out, guests)
        VALUES (#{id}, #{destination}, #{checkIn}, #{checkOut}, #{guests})
    """)
    void insertCase(BookingCase bookingCase);

    @Update("""
        UPDATE [DB_Booking].[dbo].[booking_cases]
        SET guests = #{guests}
        WHERE id = #{id}
    """)
    void updateGuestsById(@Param("id") int id,
                          @Param("guests") int guests);

    @Delete("""
    DELETE FROM [DB_Booking].[dbo].[booking_cases]
    WHERE id = #{id}
    """)
    void deleteCaseById(@Param("id") int id);

}
