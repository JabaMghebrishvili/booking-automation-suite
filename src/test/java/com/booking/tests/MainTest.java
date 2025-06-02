package com.booking.tests;

import com.booking.mappers.BookingCaseMapper;
import com.booking.model.BookingCase;
import com.booking.utils.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.Test;

import java.util.List;

public class MainTest {

    @Test
    public void testName() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            BookingCaseMapper mapper = session.getMapper(BookingCaseMapper.class);

            List<BookingCase> allCases = mapper.getCasesData();
            allCases.forEach(c -> System.out.println(c.getCheckOutAsString()));
        }
    }
}
