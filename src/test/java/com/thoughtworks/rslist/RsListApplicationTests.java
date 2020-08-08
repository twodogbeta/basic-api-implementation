package com.thoughtworks.rslist;

import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {
    @Autowired
    MockMvc mockMvc;
  /*  @Test
    void shouldConnectDataBase() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rsSystem", "root"  , "twodogs19") ;
        Statement stmt = connection.createStatement();

    }*/




}
