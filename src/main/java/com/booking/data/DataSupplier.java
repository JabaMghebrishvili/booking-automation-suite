package com.booking.data;

import com.booking.mappers.BookingCaseMapper;
import com.booking.model.BookingCase;
import com.booking.utils.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.DataProvider;

import java.util.List;

public class DataSupplier {
    @DataProvider(name = "bookingCasesProvider")
    public Object[][] getBookingCases() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            BookingCaseMapper mapper = session.getMapper(BookingCaseMapper.class);
            List<BookingCase> cases = mapper.getCasesData();

            Object[][] result = new Object[cases.size()][1];
            for (int i = 0; i < cases.size(); i++) {
                result[i][0] = cases.get(i);
            }
            return result;
        }
    }
}
